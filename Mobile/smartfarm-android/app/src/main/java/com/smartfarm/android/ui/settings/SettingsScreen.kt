package com.smartfarm.android.ui.settings

import android.app.Activity
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.util.ExportManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showRestoreConfirm by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }
    var pendingJson by remember { mutableStateOf<String?>(null) }

    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val json = context.contentResolver.openInputStream(it)?.bufferedReader()?.readText()
            if (json != null) { pendingJson = json; showRestoreConfirm = true }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.message) {
        uiState.message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() }
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
                // Appearance
                item { SettingsSectionHeader(title = stringResource(R.string.section_appearance)) }
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.theme)) },
                        trailingContent = {
                            SingleChoiceSegmentedButtonRow {
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                                    onClick = { onThemeToggle(false) },
                                    selected = !isDarkTheme
                                ) { Text(stringResource(R.string.theme_light)) }
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                                    onClick = { onThemeToggle(true) },
                                    selected = isDarkTheme
                                ) { Text(stringResource(R.string.theme_dark)) }
                            }
                        }
                    )
                    HorizontalDivider()
                }
                item {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.language)) },
                        trailingContent = {
                            SingleChoiceSegmentedButtonRow {
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                                    onClick = {
                                        if (uiState.language != "km") viewModel.setLanguage("km") { (context as Activity).recreate() }
                                    },
                                    selected = uiState.language == "km" || uiState.language.isEmpty()
                                ) { Text(stringResource(R.string.lang_khmer)) }
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                                    onClick = {
                                        if (uiState.language != "en") viewModel.setLanguage("en") { (context as Activity).recreate() }
                                    },
                                    selected = uiState.language == "en"
                                ) { Text(stringResource(R.string.lang_english)) }
                            }
                        }
                    )
                    HorizontalDivider()
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

                // Export
                item { SettingsSectionHeader(title = stringResource(R.string.section_export)) }
                item {
                    SettingsItem(Icons.Default.TableChart, stringResource(R.string.export_csv), stringResource(R.string.export_csv_sub)) {
                        viewModel.exportCsv { uri -> ExportManager.share(context, uri, "text/csv") }
                    }
                }
                item {
                    SettingsItem(Icons.Default.PictureAsPdf, stringResource(R.string.export_pdf), stringResource(R.string.export_pdf_sub)) {
                        viewModel.exportPdf { uri -> ExportManager.share(context, uri, "application/pdf") }
                    }
                }

                // Backup
                item { SettingsSectionHeader(title = stringResource(R.string.section_backup)) }
                item {
                    SettingsItem(Icons.Default.Upload, stringResource(R.string.backup_export), stringResource(R.string.backup_export_sub)) {
                        viewModel.exportBackup { uri -> ExportManager.share(context, uri, "application/json") }
                    }
                }
                item {
                    SettingsItem(Icons.Default.Download, stringResource(R.string.backup_restore), stringResource(R.string.backup_restore_sub)) {
                        restoreLauncher.launch(arrayOf("application/json", "text/plain"))
                    }
                }

                // Danger zone
                item {
                    ListItem(
                        leadingContent = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        headlineContent = {
                            Text(stringResource(R.string.clear_all), color = MaterialTheme.colorScheme.error)
                        },
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    HorizontalDivider()
                }
                item { Spacer(Modifier.height(8.dp)) }
                item {
                    FilledTonalButton(
                        onClick = { showClearConfirm = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.clear_all))
                    }
                }

                // Version footer
                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.version),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )
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

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(stringResource(R.string.clear_all_confirm_title)) },
            text = { Text(stringResource(R.string.clear_all_confirm_body)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    showClearConfirm = false
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showClearConfirm = false }) { Text(stringResource(R.string.cancel)) } }
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
