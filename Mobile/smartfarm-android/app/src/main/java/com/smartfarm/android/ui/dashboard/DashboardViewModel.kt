package com.smartfarm.android.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.repository.EventRepository
import com.smartfarm.android.data.repository.FinanceRepository
import com.smartfarm.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DashboardUiState(
    val monthIncome: Double = 0.0,
    val monthExpense: Double = 0.0,
    val monthName: String = "",
    val recentTransactions: List<FinanceEntry> = emptyList(),
    val upcomingEvents: List<EventEntry> = emptyList(),
    val showKhr: Boolean = true,
    val isLoading: Boolean = true
) {
    val balance get() = monthIncome - monthExpense
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val financeRepo: FinanceRepository,
    private val eventRepo: EventRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        val cal = Calendar.getInstance()
        val monthStart = cal.apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val monthEnd = cal.apply {
            set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59)
        }.timeInMillis

        val khmerMonths = listOf("មករា","កុម្ភៈ","មីនា","មេសា","ឧសភា","មិថុនា","កក្កដា","សីហា","កញ្ញា","តុលា","វិច្ឆិកា","ធ្នូ")
        val now = Calendar.getInstance()
        val monthLabel = "ខែ ${khmerMonths[now.get(Calendar.MONTH)]} ${now.get(Calendar.YEAR)}"

        val now7 = System.currentTimeMillis()
        val weekAhead = now7 + 7L * 24 * 60 * 60 * 1000

        viewModelScope.launch {
            combine(
                financeRepo.monthIncome(monthStart, monthEnd),
                financeRepo.monthExpense(monthStart, monthEnd),
                financeRepo.getRecent(3),
                eventRepo.getInRange(now7, weekAhead),
                userPrefs.showKhr
            ) { income, expense, recent, events, showKhr ->
                DashboardUiState(
                    monthIncome = income,
                    monthExpense = expense,
                    monthName = monthLabel,
                    recentTransactions = recent,
                    upcomingEvents = events,
                    showKhr = showKhr,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun formatAmount(amount: Double, showKhr: Boolean): String =
        if (showKhr) "%,.0f ៛".format(amount) else "$%.2f".format(amount / 4000.0)
}
