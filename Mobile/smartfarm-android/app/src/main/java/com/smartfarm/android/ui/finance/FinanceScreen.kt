package com.smartfarm.android.ui.finance

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.key
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

private val categories = listOf("ជី", "ពូជ", "ការងារ", "ឧបករណ៍", "លក់")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    innerPadding: PaddingValues,
    viewModel: FinanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<FinanceEntry?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_finance)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_entry))
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            SummaryCard(uiState, onToggleCurrency = viewModel::toggleCurrency)

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (uiState.monthlyBars.isNotEmpty()) {
                key(uiState.monthlyBars) {
                    MonthlyBarChart(bars = uiState.monthlyBars, showKhr = uiState.showKhr)
                }
            }

            HorizontalDivider()

            val filteredEntries = if (searchQuery.isBlank()) uiState.entries
            else uiState.entries.filter { e ->
                e.title.contains(searchQuery, ignoreCase = true) ||
                e.category.contains(searchQuery, ignoreCase = true) ||
                e.note.contains(searchQuery, ignoreCase = true)
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(filteredEntries, key = { it.id }) { entry ->
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
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(stringResource(R.string.balance), style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = state.run { formatAmount(balance, showKhr) },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.balance >= 0) IncomeGreen else ExpenseRed
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = state.showKhr,
                        onClick = { if (!state.showKhr) onToggleCurrency() },
                        label = { Text("KHR ៛", fontSize = 12.sp) })
                    FilterChip(selected = !state.showKhr,
                        onClick = { if (state.showKhr) onToggleCurrency() },
                        label = { Text("USD \$", fontSize = 12.sp) })
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryHalf(stringResource(R.string.income), state.run { formatAmount(totalIncome, showKhr) }, IncomeGreen, Modifier.weight(1f))
                SummaryHalf(stringResource(R.string.expense), state.run { formatAmount(totalExpense, showKhr) }, ExpenseRed, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryHalf(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(Modifier.size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}

private fun FinanceUiState.formatAmount(khr: Double, showKhr: Boolean): String =
    if (showKhr) "%,.0f ៛".format(khr) else "$%.2f".format(khr / 4000.0)

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
    val canSave = title.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (editingEntry == null) stringResource(R.string.add_entry) else stringResource(R.string.edit_entry),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            // Type selector
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(shape = SegmentedButtonDefaults.itemShape(0, 2),
                    onClick = { type = FinanceType.INCOME }, selected = type == FinanceType.INCOME) {
                    Text(stringResource(R.string.income))
                }
                SegmentedButton(shape = SegmentedButtonDefaults.itemShape(1, 2),
                    onClick = { type = FinanceType.EXPENSE }, selected = type == FinanceType.EXPENSE) {
                    Text(stringResource(R.string.expense))
                }
            }
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    isError = title.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount_khr)) },
                    singleLine = true,
                    isError = amount.toDoubleOrNull() == null || (amount.toDoubleOrNull() ?: 0.0) <= 0,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory, onValueChange = {}, readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat) },
                                onClick = { selectedCategory = cat; categoryDropdownExpanded = false })
                        }
                    }
                }
                OutlinedTextField(
                    value = note, onValueChange = { note = it },
                    label = { Text(stringResource(R.string.note)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: return@Button
                        onConfirm(FinanceEntry(
                            id = editingEntry?.id ?: 0,
                            title = title.trim(), amount = amt, type = type,
                            category = selectedCategory, note = note,
                            dateMillis = editingEntry?.dateMillis ?: System.currentTimeMillis()
                        ))
                    },
                    enabled = canSave,
                    modifier = Modifier.weight(1f)
                ) { Text(stringResource(R.string.save)) }
            }
        }
    }
}
