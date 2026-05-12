package com.smartfarm.android.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.util.ExportManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showRestoreConfirm by remember { mutableStateOf(false) }
    var pendingJson by remember { mutableStateOf<String?>(null) }

    // File picker for restore
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val json = context.contentResolver.openInputStream(it)?.bufferedReader()?.readText()
            if (json != null) {
                pendingJson = json
                showRestoreConfirm = true
            }
        }
    }

    // Message snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_settings)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // Finance export
                item {
                    SettingsSectionHeader(title = stringResource(R.string.section_export))
                }
                item {
                    SettingsItem(
                        icon = Icons.Default.TableChart,
                        title = stringResource(R.string.export_csv),
                        subtitle = stringResource(R.string.export_csv_sub)
                    ) {
                        viewModel.exportCsv { uri ->
                            ExportManager.share(context, uri, "text/csv")
                        }
                    }
                }
                item {
                    SettingsItem(
                        icon = Icons.Default.PictureAsPdf,
                        title = stringResource(R.string.export_pdf),
                        subtitle = stringResource(R.string.export_pdf_sub)
                    ) {
                        viewModel.exportPdf { uri ->
                            ExportManager.share(context, uri, "application/pdf")
                        }
                    }
                }

                // Backup / Restore
                item { SettingsSectionHeader(title = stringResource(R.string.section_backup)) }
                item {
                    SettingsItem(
                        icon = Icons.Default.Upload,
                        title = stringResource(R.string.backup_export),
                        subtitle = stringResource(R.string.backup_export_sub)
                    ) {
                        viewModel.exportBackup { uri ->
                            ExportManager.share(context, uri, "application/json")
                        }
                    }
                }
                item {
                    SettingsItem(
                        icon = Icons.Default.Download,
                        title = stringResource(R.string.backup_restore),
                        subtitle = stringResource(R.string.backup_restore_sub)
                    ) {
                        restoreLauncher.launch(arrayOf("application/json", "text/plain"))
                    }
                }

                // Stats
                item { SettingsSectionHeader(title = stringResource(R.string.section_stats)) }
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.stat_transactions)) },
                        trailingContent = { Text("${uiState.txCount}", style = MaterialTheme.typography.bodyLarge) }
                    )
                    HorizontalDivider()
                }
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.stat_activities)) },
                        trailingContent = { Text("${uiState.evCount}", style = MaterialTheme.typography.bodyLarge) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showRestoreConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreConfirm = false },
            title = { Text(stringResource(R.string.restore_confirm_title)) },
            text = { Text(stringResource(R.string.restore_confirm_body)) },
            confirmButton = {
                TextButton(onClick = {
                    pendingJson?.let { viewModel.restoreBackup(it) }
                    showRestoreConfirm = false
                }) { Text(stringResource(R.string.restore), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showRestoreConfirm = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle, style = MaterialTheme.typography.bodySmall) },
        modifier = Modifier.padding(vertical = 2.dp)
    )
    HorizontalDivider()
}
