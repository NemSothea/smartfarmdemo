package com.smartfarm.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.smartfarm.android.ui.navigation.AppNavGraph
import com.smartfarm.android.ui.theme.SmartFarmTheme
import com.smartfarm.android.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

private const val PREFS = "smartfarm_prefs"
private const val KEY_DARK = "dark_mode"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getLanguage(newBase)
        super.attachBaseContext(LocaleHelper.applyLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        setContent {
            var isDark by remember { mutableStateOf(prefs.getBoolean(KEY_DARK, false)) }

            SmartFarmTheme(darkTheme = isDark, dynamicColor = false) {
                AppNavGraph(
                    isDarkTheme = isDark,
                    onThemeToggle = { value ->
                        isDark = value
                        prefs.edit().putBoolean(KEY_DARK, value).apply()
                    }
                )
            }
        }
    }
}
