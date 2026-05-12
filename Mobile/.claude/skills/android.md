# SmartFarm Android — Build Skill

Building the SmartFarm Android app with Kotlin + Jetpack Compose + Room + Hilt.
Read `.claude/memory.md` for full context and `../PLAN.md` for the feature matrix.

---

## Environment

| Item | Value |
|------|-------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |
| Database | Room (KSP) |
| DI | Hilt (KSP) |
| State | `StateFlow` + `@HiltViewModel` |
| Navigation | Navigation Compose |
| Build | Gradle with version catalog (`libs.versions.toml`) |

---

## Project Structure

```
app/src/main/java/com/smartfarm/android/
├── MainActivity.kt
├── SmartFarmApplication.kt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── FinanceDao.kt
│   │   ├── EventDao.kt
│   │   └── entity/
│   │       ├── FinanceEntry.kt
│   │       └── EventEntry.kt
│   └── repository/
│       ├── FinanceRepository.kt
│       └── EventRepository.kt
├── ui/
│   ├── navigation/AppNavGraph.kt
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── finance/
│   │   ├── FinanceScreen.kt
│   │   └── FinanceViewModel.kt
│   ├── calendar/
│   │   ├── CalendarScreen.kt
│   │   └── CalendarViewModel.kt
│   ├── settings/
│   │   ├── SettingsScreen.kt        ← Phase F (not yet)
│   │   └── SettingsViewModel.kt     ← Phase F (not yet)
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── di/
    └── AppModule.kt
res/
├── values/strings.xml               ← English
└── values-km/strings.xml            ← Khmer
```

---

## Patterns to Follow

### ViewModel (HiltViewModel)
```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val repository: FeatureRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeatureUiState())
    val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            repository.getAll().collect { items ->
                _uiState.update { it.copy(items = items) }
            }
        }
    }

    fun add(item: FeatureEntry) {
        viewModelScope.launch { repository.insert(item) }
    }

    fun delete(item: FeatureEntry) {
        viewModelScope.launch { repository.delete(item) }
    }
}

data class FeatureUiState(
    val items: List<FeatureEntry> = emptyList(),
    val isLoading: Boolean = false
)
```

### Room Entity
```kotlin
@Entity(tableName = "feature_entries")
data class FeatureEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateMillis: Long = System.currentTimeMillis()
)
```

### Room DAO
```kotlin
@Dao
interface FeatureDao {
    @Query("SELECT * FROM feature_entries ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<FeatureEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FeatureEntry)

    @Delete
    suspend fun delete(entry: FeatureEntry)
}
```

### Composable Screen
```kotlin
@Composable
fun FeatureScreen(viewModel: FeatureViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    // ...
}
```

### Navigation route
```kotlin
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Finance   : Screen("finance")
    object Calendar  : Screen("calendar")
    object Settings  : Screen("settings")
}
```

---

## Phase A — Notifications (WorkManager)

```kotlin
// build.gradle.kts — add dependency
implementation("androidx.work:work-runtime-ktx:2.9.0")

// ReminderWorker.kt
class ReminderWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        // show notification
        return Result.success()
    }
}

// Schedule in EventRepository after insert
fun scheduleReminder(event: EventEntry) {
    if (!event.hasReminder) return
    val delay = event.dateMillis - event.reminderOffsetMinutes * 60_000L - System.currentTimeMillis()
    if (delay <= 0) return
    val data = workDataOf("title" to event.title)
    val request = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(data)
        .addTag(event.id.toString())
        .build()
    WorkManager.getInstance(context).enqueue(request)
}
```

---

## Phase B — Calendar Month Grid

```kotlin
@Composable
fun MonthGrid(
    year: Int, month: Int,
    activeDays: Set<Int>,
    selectedDay: Int,
    onDayClick: (Int) -> Unit
) {
    val firstDayOfWeek = remember(year, month) {
        Calendar.getInstance().apply { set(year, month, 1) }.get(Calendar.DAY_OF_WEEK) - 1
    }
    val daysInMonth = remember(year, month) {
        Calendar.getInstance().apply { set(year, month, 1) }
            .getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    LazyVerticalGrid(columns = GridCells.Fixed(7)) {
        items(firstDayOfWeek) { Spacer(Modifier.aspectRatio(1f)) }
        items(daysInMonth) { i ->
            val day = i + 1
            DayCell(
                day = day,
                isSelected = day == selectedDay,
                hasDot = activeDays.contains(day),
                onClick = { onDayClick(day) }
            )
        }
    }
}
```

---

## Phase C — Finance Bar Chart (Canvas)

```kotlin
@Composable
fun MonthlyBarChart(data: List<Pair<String, Double>>) {
    val maxVal = data.maxOfOrNull { it.second.coerceAtLeast(0.0) } ?: 1.0
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        val barWidth = size.width / (data.size * 2f)
        data.forEachIndexed { i, (_, value) ->
            val barHeight = (value / maxVal * size.height).toFloat().coerceAtLeast(0f)
            val color = if (value >= 0) Color(0xFF43A047) else Color(0xFFE53935)
            drawRect(
                color = color,
                topLeft = Offset(x = i * barWidth * 2 + barWidth / 2, y = size.height - barHeight),
                size = Size(barWidth, barHeight)
            )
        }
    }
}
```

---

## Phase D — CSV & PDF Export

```kotlin
// CSV
fun exportCsv(entries: List<FinanceEntry>): String {
    val sb = StringBuilder("date,type,amount,category,note\n")
    entries.forEach { e ->
        sb.append("${e.dateMillis},${e.type},${e.amount},${e.category},${e.note}\n")
    }
    return sb.toString()
}

// Share intent
fun shareCsv(context: Context, csv: String) {
    val file = File(context.cacheDir, "smartfarm_export.csv")
    file.writeText(csv)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Export CSV"))
}
```

---

## Phase E — JSON Backup & Restore

```kotlin
@Serializable
data class BackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val transactions: List<FinanceEntry>,
    val events: List<EventEntry>
)

// Export
suspend fun exportJson(context: Context): Uri {
    val data = BackupData(transactions = financeDao.getAllOnce(), events = eventDao.getAllOnce())
    val json = Json.encodeToString(data)
    val file = File(context.cacheDir, "smartfarm_backup.json")
    file.writeText(json)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

// Restore
suspend fun importJson(json: String) {
    val data = Json.decodeFromString<BackupData>(json)
    financeDao.deleteAll()
    eventDao.deleteAll()
    data.transactions.forEach { financeDao.insert(it.copy(id = 0)) }
    data.events.forEach { eventDao.insert(it.copy(id = 0)) }
}
```

---

## Khmer Strings Convention

- All UI labels must have a Khmer entry in `values-km/strings.xml`
- English fallback in `values/strings.xml`
- Reference via `stringResource(R.string.key)`

---

## Always Check

- `.claude/memory.md` — shared rules (amount storage, FarmActivity types, categories)
- `../PLAN.md` — feature matrix and Android phase order before starting any phase
