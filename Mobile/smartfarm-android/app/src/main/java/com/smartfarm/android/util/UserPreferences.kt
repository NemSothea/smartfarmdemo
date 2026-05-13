package com.smartfarm.android.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val _showKhr = MutableStateFlow(prefs.getBoolean(KEY_SHOW_KHR, true))
    val showKhr: StateFlow<Boolean> = _showKhr.asStateFlow()

    fun toggleShowKhr() {
        val new = !_showKhr.value
        _showKhr.value = new
        prefs.edit().putBoolean(KEY_SHOW_KHR, new).apply()
    }

    companion object {
        private const val PREFS = "smartfarm_prefs"
        private const val KEY_SHOW_KHR = "show_khr"
    }
}
