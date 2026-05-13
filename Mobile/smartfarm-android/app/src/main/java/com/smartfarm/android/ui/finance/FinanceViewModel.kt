package com.smartfarm.android.ui.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import com.smartfarm.android.data.repository.FinanceRepository
import com.smartfarm.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class MonthlyBar(val label: String, val income: Double, val expense: Double) {
    val profit: Double get() = income - expense
}

data class FinanceUiState(
    val entries: List<FinanceEntry> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val monthlyBars: List<MonthlyBar> = emptyList(),
    val showKhr: Boolean = true,
    val isLoading: Boolean = true
) {
    val balance get() = totalIncome - totalExpense
}

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val repository: FinanceRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAll(),
                repository.totalIncome(),
                repository.totalExpense(),
                userPrefs.showKhr
            ) { entries, income, expense, showKhr ->
                FinanceUiState(
                    entries = entries,
                    totalIncome = income,
                    totalExpense = expense,
                    monthlyBars = buildMonthlyBars(entries),
                    showKhr = showKhr,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun toggleCurrency() = userPrefs.toggleShowKhr()

    fun save(entry: FinanceEntry) = viewModelScope.launch { repository.save(entry) }
    fun delete(entry: FinanceEntry) = viewModelScope.launch { repository.delete(entry) }

    private fun buildMonthlyBars(entries: List<FinanceEntry>): List<MonthlyBar> {
        val cal = Calendar.getInstance()
        val monthLabels = listOf("មករា","កុម្ភៈ","មីនា","មេសា","ឧសភា","មិថុនា","កក្កដា","សីហា","កញ្ញា","តុលា","វិច្ឆិកា","ធ្នូ")
        val bars = mutableListOf<MonthlyBar>()

        for (offset in 5 downTo 0) {
            val targetCal = Calendar.getInstance().apply { add(Calendar.MONTH, -offset) }
            val y = targetCal.get(Calendar.YEAR)
            val m = targetCal.get(Calendar.MONTH)
            val monthEntries = entries.filter { entry ->
                val ec = Calendar.getInstance().apply { timeInMillis = entry.dateMillis }
                ec.get(Calendar.YEAR) == y && ec.get(Calendar.MONTH) == m
            }
            val income = monthEntries.filter { it.type == FinanceType.INCOME }.sumOf { it.amount }
            val expense = monthEntries.filter { it.type == FinanceType.EXPENSE }.sumOf { it.amount }
            bars.add(MonthlyBar(label = monthLabels.getOrElse(m) { "" }, income = income, expense = expense))
        }
        return bars
    }

    fun formatAmount(khr: Double, showKhr: Boolean): String =
        if (showKhr) "%,.0f ៛".format(khr) else "$%.2f".format(khr / 4000.0)
}
