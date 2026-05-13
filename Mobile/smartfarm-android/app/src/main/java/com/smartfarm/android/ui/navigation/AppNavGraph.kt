package com.smartfarm.android.ui.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartfarm.android.R
import com.smartfarm.android.ui.calendar.CalendarScreen
import com.smartfarm.android.ui.dashboard.DashboardScreen
import com.smartfarm.android.ui.finance.FinanceScreen
import com.smartfarm.android.ui.onboarding.OnboardingScreen
import com.smartfarm.android.ui.settings.SettingsScreen
import com.smartfarm.android.ui.splash.SplashScreen

sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", R.string.nav_dashboard, Icons.Default.Dashboard)
    object Finance   : Screen("finance",   R.string.nav_finance,   Icons.Default.AttachMoney)
    object Calendar  : Screen("calendar",  R.string.nav_calendar,  Icons.Default.CalendarMonth)
    object Settings  : Screen("settings",  R.string.nav_settings,  Icons.Default.Settings)
}

private const val ROUTE_ONBOARDING = "onboarding"
private const val ROUTE_SPLASH = "splash"
private val bottomNavItems = listOf(Screen.Dashboard, Screen.Finance, Screen.Calendar, Screen.Settings)

@Composable
fun AppNavGraph(
    isDarkTheme: Boolean = false,
    onThemeToggle: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("smartfarm_prefs", Context.MODE_PRIVATE) }
    val hasCompletedOnboarding = remember { prefs.getBoolean("hasCompletedOnboarding", false) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isOnboarding = currentDestination?.route == ROUTE_ONBOARDING
            || currentDestination?.route == ROUTE_SPLASH

    Scaffold(
        bottomBar = {
            if (!isOnboarding) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.labelRes)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = ROUTE_SPLASH) {
            composable(ROUTE_SPLASH) {
                SplashScreen {
                    val dest = if (hasCompletedOnboarding) Screen.Dashboard.route else ROUTE_ONBOARDING
                    navController.navigate(dest) {
                        popUpTo(ROUTE_SPLASH) { inclusive = true }
                    }
                }
            }
            composable(ROUTE_ONBOARDING) {
                OnboardingScreen {
                    prefs.edit().putBoolean("hasCompletedOnboarding", true).apply()
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(ROUTE_ONBOARDING) { inclusive = true }
                    }
                }
            }
            composable(Screen.Dashboard.route) { DashboardScreen(innerPadding) }
            composable(Screen.Finance.route)   { FinanceScreen(innerPadding) }
            composable(Screen.Calendar.route)  { CalendarScreen(innerPadding) }
            composable(Screen.Settings.route)  {
                SettingsScreen(
                    innerPadding = innerPadding,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }
}
