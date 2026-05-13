package com.smartfarm.android.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartfarm.android.R
import com.smartfarm.android.data.local.entity.EventEntry
import java.text.SimpleDateFormat
import java.util.*

private val activityTypeColors = mapOf(
    "ដំណាំ"        to Color(0xFF43A047),
    "ស្រោចទឹក"    to Color(0xFF00ACC1),
    "ដាក់ជី"       to Color(0xFF795548),
    "ថែទាំ"        to Color(0xFF1E88E5),
    "ការពារ"       to Color(0xFFFB8C00),
    "ច្រូតកាត់"    to Color(0xFF8E24AA),
    "ប្រមូលផល"     to Color(0xFFFFB300),
    "ត្រួតពិនិត្យ" to Color(0xFF757575)
)

private val activityTypes   = listOf("ដំណាំ", "ស្រោចទឹក", "ដាក់ជី", "ថែទាំ", "ការពារ", "ច្រូតកាត់", "ប្រមូលផល", "ត្រួតពិនិត្យ")
private val weekDayLabels   = listOf("អា", "ច", "អ", "ព", "ព្រ", "សុ", "ស")
private val monthNames      = listOf("មករា","កុម្ភៈ","មីនា","មេសា","ឧសភា","មិថុនា","កក្កដា","សីហា","កញ្ញា","តុលា","វិច្ឆិកា","ធ្នូ")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    innerPadding: PaddingValues,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEvent by remember { mutableStateOf<EventEntry?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.nav_calendar)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_event))
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (searchQuery.isNotBlank()) {
                // Search results — flat cross-date list
                val results = uiState.events.filter { e ->
                    e.title.contains(searchQuery, ignoreCase = true) ||
                    e.type.contains(searchQuery, ignoreCase = true)
                }.sortedBy { it.dateMillis }
                if (results.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_results), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn {
                        items(results, key = { it.id }) { event ->
                            EventItem(event = event,
                                onDelete = { viewModel.delete(event) },
                                onToggleDone = { viewModel.toggleDone(event) },
                                onEdit = { editingEvent = event })
                        }
                    }
                }
            } else {
            MonthNavigationHeader(uiState.year, uiState.month, viewModel::prevMonth, viewModel::nextMonth)
            MonthGrid(uiState.year, uiState.month, uiState.activeDaysInMonth, uiState.selectedDay, viewModel::selectDay)
            HorizontalDivider()

            val dayEvents = uiState.eventsForSelectedDay
            if (dayEvents.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_events_today), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn {
                    items(dayEvents, key = { it.id }) { event ->
                        EventItem(
                            event = event,
                            onDelete = { viewModel.delete(event) },
                            onToggleDone = { viewModel.toggleDone(event) },
                            onEdit = { editingEvent = event }
                        )
                    }
                }
            }
            } // end else (no search)
        }
    }

    if (showAddDialog) {
        EventFormDialog(
            selectedYear = uiState.year,
            selectedMonth = uiState.month,
            selectedDay = uiState.selectedDay,
            onDismiss = { showAddDialog = false },
            onConfirm = { event -> viewModel.save(event); showAddDialog = false }
        )
    }

    editingEvent?.let { event ->
        EventFormDialog(
            editingEvent = event,
            selectedYear = uiState.year,
            selectedMonth = uiState.month,
            selectedDay = uiState.selectedDay,
            onDismiss = { editingEvent = null },
            onConfirm = { updated -> viewModel.save(updated); editingEvent = null }
        )
    }
}

@Composable
private fun MonthNavigationHeader(year: Int, month: Int, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPrev) { Icon(Icons.Default.ChevronLeft, null) }
        Text("${monthNames.getOrElse(month) { "" }} $year",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold)
        IconButton(onClick = onNext) { Icon(Icons.Default.ChevronRight, null) }
    }
}

@Composable
private fun MonthGrid(year: Int, month: Int, activeDays: Set<Int>, selectedDay: Int, onDayClick: (Int) -> Unit) {
    val cal = Calendar.getInstance().apply { set(year, month, 1) }
    val firstWeekday = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val todayCal = Calendar.getInstance()
    val isCurrentMonth = todayCal.get(Calendar.YEAR) == year && todayCal.get(Calendar.MONTH) == month
    val todayDay = if (isCurrentMonth) todayCal.get(Calendar.DAY_OF_MONTH) else -1

    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDayLabels.forEach { label ->
                Text(label, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(4.dp))
        val rows = (firstWeekday + daysInMonth + 6) / 7
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val day = row * 7 + col - firstWeekday + 1
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (day in 1..daysInMonth) {
                            DayCell(day, day == selectedDay, day == todayDay, activeDays.contains(day)) { onDayClick(day) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(day: Int, isSelected: Boolean, isToday: Boolean, hasDot: Boolean, onClick: () -> Unit) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday    -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else       -> Color.Transparent
    }
    Column(
        modifier = Modifier.padding(2.dp).size(36.dp).clip(CircleShape).background(bgColor).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(day.toString(),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal)
        if (hasDot) {
            Box(modifier = Modifier.size(4.dp).background(
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, CircleShape))
        }
    }
}

@Composable
private fun EventItem(event: EventEntry, onDelete: () -> Unit, onToggleDone: () -> Unit, onEdit: () -> Unit) {
    val typeColor = activityTypeColors[event.type] ?: MaterialTheme.colorScheme.primary
    val dateStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.dateMillis))
    val alpha = if (event.isDone) 0.5f else 1f

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = event.isDone, onCheckedChange = { onToggleDone() },
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary))
        Box(modifier = Modifier.width(4.dp).height(40.dp).background(typeColor.copy(alpha = alpha), MaterialTheme.shapes.small))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(event.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium,
                textDecoration = if (event.isDone) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(event.type, style = MaterialTheme.typography.labelSmall, color = typeColor.copy(alpha = alpha))
                Text("·", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dateStr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (event.hasReminder) {
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
            if (event.description.isNotBlank()) {
                Text(event.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha))
            }
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventFormDialog(
    editingEvent: EventEntry? = null,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    onDismiss: () -> Unit,
    onConfirm: (EventEntry) -> Unit
) {
    var title by remember { mutableStateOf(editingEvent?.title ?: "") }
    var description by remember { mutableStateOf(editingEvent?.description ?: "") }
    var hasReminder by remember { mutableStateOf(editingEvent?.hasReminder ?: false) }
    var selectedType by remember { mutableStateOf(editingEvent?.type ?: activityTypes.first()) }
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    val dateMillis = editingEvent?.dateMillis ?: remember(selectedYear, selectedMonth, selectedDay) {
        Calendar.getInstance().apply { set(selectedYear, selectedMonth, selectedDay, 8, 0, 0) }.timeInMillis
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        Text(
            text = if (editingEvent == null) stringResource(R.string.add_event) else stringResource(R.string.edit_event),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    isError = title.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = typeDropdownExpanded,
                    onExpandedChange = { typeDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedType, onValueChange = {}, readOnly = true,
                        label = { Text(stringResource(R.string.activity_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = typeDropdownExpanded, onDismissRequest = { typeDropdownExpanded = false }) {
                        activityTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Box(modifier = Modifier.size(10.dp)
                                            .background(activityTypeColors[type] ?: Color.Gray, CircleShape))
                                        Text(type)
                                    }
                                },
                                onClick = { selectedType = type; typeDropdownExpanded = false }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasReminder, onCheckedChange = { hasReminder = it })
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.set_reminder))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        onConfirm(EventEntry(
                            id = editingEvent?.id ?: 0,
                            title = title.trim(), type = selectedType,
                            description = description, dateMillis = dateMillis,
                            hasReminder = hasReminder, isDone = editingEvent?.isDone ?: false
                        ))
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) { Text(stringResource(R.string.save)) }
            }
        }
    }
}
