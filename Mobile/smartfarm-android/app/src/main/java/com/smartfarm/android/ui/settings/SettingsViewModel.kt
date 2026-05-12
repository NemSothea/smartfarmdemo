package com.smartfarm.android.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartfarm.android.data.local.entity.FinanceType
import com.smartfarm.android.data.repository.EventRepository
import com.smartfarm.android.data.repository.FinanceRepository
import com.smartfarm.android.util.BackupManager
import com.smartfarm.android.util.ExportManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val txCount: Int = 0,
    val evCount: Int = 0,
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val financeRepo: FinanceRepository,
    private val eventRepo: EventRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(financeRepo.getAll(), eventRepo.getAll()) { tx, ev ->
                _uiState.value.copy(txCount = tx.size, evCount = ev.size)
            }.collect { _uiState.value = it }
        }
    }

    fun exportCsv(onUri: (Uri) -> Unit) = viewModelScope.launch {
        val entries = financeRepo.getAllOnce()
        onUri(ExportManager.exportCsv(context, entries))
    }

    fun exportPdf(onUri: (Uri) -> Unit) = viewModelScope.launch {
        val entries = financeRepo.getAllOnce()
        val income = entries.filter { it.type == FinanceType.INCOME }.sumOf { it.amount }
        val expense = entries.filter { it.type == FinanceType.EXPENSE }.sumOf { it.amount }
        onUri(ExportManager.exportPdf(context, entries, income, expense))
    }

    fun exportBackup(onUri: (Uri) -> Unit) = viewModelScope.launch {
        val entries = financeRepo.getAllOnce()
        val events = eventRepo.getAllOnce()
        onUri(BackupManager.exportJson(context, entries, events))
    }

    fun restoreBackup(json: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val (entries, events) = BackupManager.parseJson(json)
            financeRepo.deleteAll()
            eventRepo.deleteAll()
            entries.forEach { financeRepo.save(it.copy(id = 0)) }
            events.forEach { eventRepo.save(it.copy(id = 0)) }
            _uiState.update { it.copy(isLoading = false, message = "ស្ដារទិន្នន័យបានជោគជ័យ!") }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, message = "មានបញ្ហា: ${e.message}") }
        }
    }

    fun clearMessage() = _uiState.update { it.copy(message = null) }
}
