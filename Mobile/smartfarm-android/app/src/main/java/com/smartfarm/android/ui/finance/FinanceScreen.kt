package com.smartfarm.android.ui.finance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import com.smartfarm.android.ui.theme.ExpenseRed
import com.smartfarm.android.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.*

private val categories = listOf("Seeds", "Fertilizer", "Labor", "Tools", "Sales")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    innerPadding: PaddingValues,
    viewModel: FinanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<FinanceEntry?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_finance)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_entry))
            }
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            SummaryCard(uiState, onToggleCurrency = viewModel::toggleCurrency)

            if (uiState.monthlyBars.isNotEmpty()) {
                MonthlyBarChart(bars = uiState.monthlyBars, showKhr = uiState.showKhr)
            }

            HorizontalDivider()

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(uiState.entries, key = { it.id }) { entry ->
                        FinanceEntryItem(
                            entry = entry,
                            showKhr = uiState.showKhr,
                            formatAmount = viewModel::formatAmount,
                            onDelete = { viewModel.delete(entry) },
                            onEdit = { editingEntry = entry }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        EntryFormDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { entry ->
                viewModel.save(entry)
                showAddDialog = false
            }
        )
    }

    editingEntry?.let { entry ->
        EntryFormDialog(
            editingEntry = entry,
            onDismiss = { editingEntry = null },
            onConfirm = { updated ->
                viewModel.save(updated)
                editingEntry = null
            }
        )
    }
}

@Composable
private fun SummaryCard(state: FinanceUiState, onToggleCurrency: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryItem(stringResource(R.string.income),  state.run { formatAmount(totalIncome, showKhr) },  IncomeGreen)
                SummaryItem(stringResource(R.string.expense), state.run { formatAmount(totalExpense, showKhr) }, ExpenseRed)
                SummaryItem(stringResource(R.string.balance), state.run { formatAmount(balance, showKhr) },
                    if (state.balance >= 0) IncomeGreen else ExpenseRed)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FilterChip(selected = state.showKhr,  onClick = { if (!state.showKhr) onToggleCurrency() },
                    label = { Text("KHR ៛") }, modifier = Modifier.padding(end = 8.dp))
                FilterChip(selected = !state.showKhr, onClick = { if (state.showKhr) onToggleCurrency() },
                    label = { Text("USD \$") })
            }
        }
    }
}

private fun FinanceUiState.formatAmount(khr: Double, showKhr: Boolean): String =
    if (showKhr) "%,.0f ៛".format(khr) else "$%.2f".format(khr / 4100.0)

@Composable
private fun SummaryItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = color)
    }
}

@Composable
private fun MonthlyBarChart(bars: List<MonthlyBar>, showKhr: Boolean) {
    val maxVal = bars.maxOf { maxOf(it.income, it.expense) }.coerceAtLeast(1.0)
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(stringResource(R.string.monthly_chart), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            val groupWidth = size.width / bars.size
            val barW = groupWidth * 0.3f
            val gap = groupWidth * 0.05f
            val chartH = size.height - 20.dp.toPx()
            bars.forEachIndexed { i, bar ->
                val groupX = i * groupWidth + groupWidth * 0.1f
                val incH = (bar.income / maxVal * chartH).toFloat().coerceAtLeast(0f)
                drawRect(Color(0xFF43A047), topLeft = Offset(groupX, chartH - incH), size = Size(barW, incH))
                val expH = (bar.expense / maxVal * chartH).toFloat().coerceAtLeast(0f)
                drawRect(Color(0xFFE53935), topLeft = Offset(groupX + barW + gap, chartH - expH), size = Size(barW, expH))
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            bars.forEach { bar ->
                Text(bar.label, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                    fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
            LegendDot(Color(0xFF43A047), stringResource(R.string.income))
            Spacer(Modifier.width(16.dp))
            LegendDot(Color(0xFFE53935), stringResource(R.string.expense))
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Canvas(modifier = Modifier.size(8.dp)) { drawCircle(color) }
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun FinanceEntryItem(
    entry: FinanceEntry,
    showKhr: Boolean,
    formatAmount: (Double, Boolean) -> String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(entry.dateMillis))
    ListItem(
        headlineContent = { Text(entry.title) },
        supportingContent = { Text("$dateStr · ${entry.category}") },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${if (entry.type == FinanceType.INCOME) "+" else "−"}${formatAmount(entry.amount, showKhr)}",
                    color = if (entry.type == FinanceType.INCOME) IncomeGreen else ExpenseRed,
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryFormDialog(
    editingEntry: FinanceEntry? = null,
    onDismiss: () -> Unit,
    onConfirm: (FinanceEntry) -> Unit
) {
    var title by remember { mutableStateOf(editingEntry?.title ?: "") }
    var amount by remember { mutableStateOf(editingEntry?.amount?.let { "%.0f".format(it) } ?: "") }
    var selectedCategory by remember { mutableStateOf(editingEntry?.category ?: categories.first()) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf(editingEntry?.note ?: "") }
    var type by remember { mutableStateOf(editingEntry?.type ?: FinanceType.INCOME) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editingEntry == null) stringResource(R.string.add_entry) else stringResource(R.string.edit_entry)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    isError = title.isEmpty()
                )
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount_khr)) },
                    singleLine = true,
                    isError = amount.toDoubleOrNull() == null || (amount.toDoubleOrNull() ?: 0.0) <= 0,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                ExposedDropdownMenuBox(expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = it }) {
                    OutlinedTextField(
                        value = selectedCategory, onValueChange = {}, readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat) },
                                onClick = { selectedCategory = cat; categoryDropdownExpanded = false })
                        }
                    }
                }
                OutlinedTextField(value = note, onValueChange = { note = it },
                    label = { Text(stringResource(R.string.note)) })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = type == FinanceType.INCOME, onClick = { type = FinanceType.INCOME },
                        label = { Text(stringResource(R.string.income)) })
                    FilterChip(selected = type == FinanceType.EXPENSE, onClick = { type = FinanceType.EXPENSE },
                        label = { Text(stringResource(R.string.expense)) })
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: return@TextButton
                    onConfirm(FinanceEntry(
                        id = editingEntry?.id ?: 0,
                        title = title.trim(),
                        amount = amt,
                        type = type,
                        category = selectedCategory,
                        note = note,
                        dateMillis = editingEntry?.dateMillis ?: System.currentTimeMillis()
                    ))
                },
                enabled = title.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}
