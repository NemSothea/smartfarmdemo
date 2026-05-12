package com.smartfarm.android.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.ui.theme.ExpenseRed
import com.smartfarm.android.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.*

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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { FinanceSummaryCard(uiState) }
                item {
                    Text(
                        stringResource(R.string.upcoming_events),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (uiState.upcomingEvents.isEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.no_upcoming_events),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(uiState.upcomingEvents) { event -> UpcomingEventCard(event) }
                }
            }
        }
    }
}

@Composable
private fun FinanceSummaryCard(state: DashboardUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.finance_summary), style = MaterialTheme.typography.titleLarge)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryCol(label = stringResource(R.string.income), value = "${"%.2f".format(state.totalIncome)}", color = IncomeGreen)
                SummaryCol(label = stringResource(R.string.expense), value = "${"%.2f".format(state.totalExpense)}", color = ExpenseRed)
                SummaryCol(
                    label = stringResource(R.string.balance),
                    value = "${"%.2f".format(state.balance)}",
                    color = if (state.balance >= 0) IncomeGreen else ExpenseRed
                )
            }
        }
    }
}

@Composable
private fun SummaryCol(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge, color = color)
    }
}

@Composable
private fun UpcomingEventCard(event: EventEntry) {
    val dateStr = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date(event.dateMillis))
    Card(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(event.title) },
            supportingContent = { if (event.description.isNotBlank()) Text(event.description) },
            trailingContent = { Text(dateStr, style = MaterialTheme.typography.labelSmall) }
        )
    }
}
