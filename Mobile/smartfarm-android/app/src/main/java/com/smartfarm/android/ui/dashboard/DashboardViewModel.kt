package com.smartfarm.android.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.repository.EventRepository
import com.smartfarm.android.data.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DashboardUiState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val upcomingEvents: List<EventEntry> = emptyList(),
    val isLoading: Boolean = true
) {
    val balance get() = totalIncome - totalExpense
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    financeRepo: FinanceRepository,
    eventRepo: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        val now = System.currentTimeMillis()
        val weekAhead = now + 7L * 24 * 60 * 60 * 1000

        viewModelScope.launch {
            combine(
                financeRepo.totalIncome(),
                financeRepo.totalExpense(),
                eventRepo.getInRange(now, weekAhead)
            ) { income, expense, events ->
                DashboardUiState(
                    totalIncome = income,
                    totalExpense = expense,
                    upcomingEvents = events,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }
}
