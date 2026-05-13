package com.smartfarm.android.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import com.smartfarm.android.ui.theme.ExpenseRed
import com.smartfarm.android.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.*

private val activityTypeColors = mapOf(
    "ដំណាំ" to androidx.compose.ui.graphics.Color(0xFF2E6E43),
    "ស្រោចទឹក" to androidx.compose.ui.graphics.Color(0xFF00ACC1),
    "ដាក់ជី" to androidx.compose.ui.graphics.Color(0xFF795548),
    "ថែទាំ" to androidx.compose.ui.graphics.Color(0xFF1E88E5),
    "ការពារ" to androidx.compose.ui.graphics.Color(0xFFFB8C00),
    "ច្រូតកាត់" to androidx.compose.ui.graphics.Color(0xFF8E24AA),
    "ប្រមូលផល" to androidx.compose.ui.graphics.Color(0xFFFFB300),
    "ត្រួតពិនិត្យ" to androidx.compose.ui.graphics.Color(0xFF757575)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    innerPadding: PaddingValues,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_dashboard)) }) }
    ) { scaffoldPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Monthly balance hero card
                item { MonthlySummaryCard(uiState) { viewModel.formatAmount(it, uiState.showKhr) } }

                // Upcoming activities
                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.upcoming_events),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f))
                    }
                }
                if (uiState.upcomingEvents.isEmpty()) {
                    item {
                        Text(stringResource(R.string.no_upcoming_events),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    items(uiState.upcomingEvents) { event -> UpcomingEventCard(event) }
                }

                // Recent transactions
                if (uiState.recentTransactions.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.recent_transactions),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                    }
                    items(uiState.recentTransactions) { tx ->
                        RecentTransactionCard(tx) { viewModel.formatAmount(it, uiState.showKhr) }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlySummaryCard(state: DashboardUiState, fmt: (Double) -> String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.monthName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(
                text = fmt(state.balance),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (state.balance >= 0) IncomeGreen else ExpenseRed
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(Modifier.size(8.dp).background(IncomeGreen, RoundedCornerShape(50)))
                        Text(stringResource(R.string.income), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(fmt(state.monthIncome), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = IncomeGreen)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(Modifier.size(8.dp).background(ExpenseRed, RoundedCornerShape(50)))
                        Text(stringResource(R.string.expense), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(fmt(state.monthExpense), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = ExpenseRed)
                }
            }
        }
    }
}

@Composable
private fun UpcomingEventCard(event: EventEntry) {
    val dateStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(event.dateMillis))
    val typeColor = activityTypeColors[event.type] ?: MaterialTheme.colorScheme.primary

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Activity type badge
            Surface(
                color = typeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = event.type,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = typeColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                if (event.description.isNotBlank())
                    Text(event.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(dateStr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RecentTransactionCard(tx: FinanceEntry, fmt: (Double) -> String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tx.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(tx.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = "${if (tx.type == FinanceType.INCOME) "+" else "−"}${fmt(tx.amount)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (tx.type == FinanceType.INCOME) IncomeGreen else ExpenseRed
            )
        }
    }
}
