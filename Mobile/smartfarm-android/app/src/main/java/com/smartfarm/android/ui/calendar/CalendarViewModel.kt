package com.smartfarm.android.ui.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.repository.EventRepository
import com.smartfarm.android.util.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class CalendarUiState(
    val events: List<EventEntry> = emptyList(),
    val isLoading: Boolean = true,
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH), // 0-indexed
    val selectedDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
) {
    val eventsForSelectedDay: List<EventEntry>
        get() = events.filter { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.dateMillis }
            cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month &&
                cal.get(Calendar.DAY_OF_MONTH) == selectedDay
        }

    val activeDaysInMonth: Set<Int>
        get() = events.mapNotNull { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.dateMillis }
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month)
                cal.get(Calendar.DAY_OF_MONTH)
            else null
        }.toSet()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: EventRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll()
                .collect { events ->
                    _uiState.update { it.copy(events = events, isLoading = false) }
                }
        }
    }

    fun selectDay(day: Int) = _uiState.update { it.copy(selectedDay = day) }

    fun prevMonth() = _uiState.update { state ->
        val cal = Calendar.getInstance().apply { set(state.year, state.month, 1) }
        cal.add(Calendar.MONTH, -1)
        state.copy(year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), selectedDay = 1)
    }

    fun nextMonth() = _uiState.update { state ->
        val cal = Calendar.getInstance().apply { set(state.year, state.month, 1) }
        cal.add(Calendar.MONTH, 1)
        state.copy(year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), selectedDay = 1)
    }

    fun save(event: EventEntry) = viewModelScope.launch {
        repository.save(event)
        NotificationScheduler.schedule(context, event)
    }

    fun delete(event: EventEntry) = viewModelScope.launch {
        NotificationScheduler.cancel(context, event)
        repository.delete(event)
    }

    fun toggleDone(event: EventEntry) = viewModelScope.launch {
        repository.save(event.copy(isDone = !event.isDone))
    }
}
