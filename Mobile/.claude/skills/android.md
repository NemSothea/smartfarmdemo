# SmartFarm Android — Build Skill

Building the SmartFarm Android app with Kotlin + Jetpack Compose + Room + Hilt.
Read `.claude/memory.md` for full context and `../PLAN.md` for the feature matrix.
For visual design spec, read `../FIGMA_BRIEF.md` (Part B — Android Native, sections B0–B7).

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

## Actual Project Structure

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
│   ├── onboarding/
│   │   └── OnboardingScreen.kt
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
│   │   ├── SettingsScreen.kt
│   │   └── SettingsViewModel.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── di/
│   └── AppModule.kt
├── util/
│   ├── BackupManager.kt
│   ├── ExportManager.kt
│   └── NotificationScheduler.kt
└── worker/
    └── ReminderWorker.kt
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
    object Dashboard  : Screen("dashboard")
    object Finance    : Screen("finance")
    object Calendar   : Screen("calendar")
    object Settings   : Screen("settings")
}
```

---

## Notifications (WorkManager)

```kotlin
class ReminderWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        // show notification
        return Result.success()
    }
}

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

## Calendar Month Grid

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

## Finance Bar Chart (Canvas)

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

## CSV & PDF Export

```kotlin
fun exportCsv(entries: List<FinanceEntry>): String {
    val sb = StringBuilder("date,type,amount,category,note\n")
    entries.forEach { e ->
        sb.append("${e.dateMillis},${e.type},${e.amount},${e.category},${e.note}\n")
    }
    return sb.toString()
}

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

## JSON Backup & Restore

```kotlin
@Serializable
data class BackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val transactions: List<FinanceEntry>,
    val events: List<EventEntry>
)

suspend fun exportJson(context: Context): Uri {
    val data = BackupData(transactions = financeDao.getAllOnce(), events = eventDao.getAllOnce())
    val json = Json.encodeToString(data)
    val file = File(context.cacheDir, "smartfarm_backup.json")
    file.writeText(json)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

suspend fun importJson(json: String) {
    val data = Json.decodeFromString<BackupData>(json)
    financeDao.deleteAll()
    eventDao.deleteAll()
    data.transactions.forEach { financeDao.insert(it.copy(id = 0)) }
    data.events.forEach { eventDao.insert(it.copy(id = 0)) }
}
```

---

## Language Switching

The app must support Khmer ↔ English toggle from Settings, persisted across launches.

### Approach (API 26+ compatible)
Use `SharedPreferences` to store `"km"` or `"en"`, override `attachBaseContext` in `MainActivity`, and restart the activity on change.

```kotlin
// util/LocaleHelper.kt
object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun getLanguage(context: Context): String =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language", "km") ?: "km"

    fun saveLanguage(context: Context, language: String) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit().putString("language", language).apply()
    }
}
```

```kotlin
// MainActivity.kt — override attachBaseContext
override fun attachBaseContext(newBase: Context) {
    val lang = LocaleHelper.getLanguage(newBase)
    super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
}
```

```kotlin
// SettingsViewModel.kt — trigger language change
fun setLanguage(context: Context, language: String) {
    LocaleHelper.saveLanguage(context, language)
    // caller must restart Activity for locale to take effect
}
```

```kotlin
// SettingsScreen.kt — restart activity after selection
val context = LocalContext.current
SegmentedButton(
    selected = language == "km",
    onClick = {
        viewModel.setLanguage(context, "km")
        (context as Activity).recreate()
    }
) { Text("🇰🇭 ខ្មែរ") }
```

### strings.xml convention
```xml
<!-- res/values/strings.xml (English fallback) -->
<string name="tab_dashboard">Dashboard</string>
<string name="tab_finance">Finance</string>
<string name="tab_calendar">Calendar</string>
<string name="tab_settings">Settings</string>
<string name="finance_balance">Balance</string>
<string name="finance_income">Income</string>
<string name="finance_expense">Expense</string>

<!-- res/values-km/strings.xml (Khmer) -->
<string name="tab_dashboard">ផ្ទាំងគ្រប់គ្រង</string>
<string name="tab_finance">ហិរញ្ញវត្ថុ</string>
<string name="tab_calendar">ប្រតិទិន</string>
<string name="tab_settings">ការកំណត់</string>
<string name="finance_balance">សមតុល្យ</string>
<string name="finance_income">ចំណូល</string>
<string name="finance_expense">ចំណាយ</string>
```

---

## ModalBottomSheet / Dialog Layout

**Bug:** TextFields not filling full width inside `ModalBottomSheet`.

**Rules:**
- Every `OutlinedTextField` and `Button` inside a sheet must have `modifier = Modifier.fillMaxWidth()`
- Wrap sheet content in `Column(modifier = Modifier.fillMaxWidth().padding(...))` — never use `Box` or `Row` as root
- Always add `WindowCompat.setDecorFitsSystemWindows(window, false)` + `imePadding()` so the keyboard doesn't overlap fields

```kotlin
// Correct ModalBottomSheet content structure
ModalBottomSheet(onDismissRequest = onDismiss) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .imePadding(),   // ← keyboard pushes content up
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("បន្ថែមប្រតិបត្តិការ", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(R.string.amount)) },
            modifier = Modifier.fillMaxWidth(),   // ← required
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text(stringResource(R.string.note)) },
            modifier = Modifier.fillMaxWidth()    // ← required
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.cancel))
            }
            Button(
                onClick = { onSave(); onDismiss() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E6E43))
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
```

---

## Splash Screen Architecture

**Problem:** `core-splashscreen` API only controls icon + background color. It cannot render text ("SmartFarm", "កសិកម្មឆ្លាតវៃ"). The Khmer/English branding text must live in a custom Composable splash shown **after** the system splash.

### Two-layer approach

**Layer 1 — System splash** (`themes.xml`, instant, system-managed):
```xml
<style name="Theme.SmartFarm.Splash" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">#2E6E43</item>
    <item name="windowSplashScreenAnimatedIcon">@drawable/ic_splash_leaf</item>
    <item name="postSplashScreenTheme">@style/Theme.SmartFarm</item>
</style>
```

**Layer 2 — Custom splash Composable** (shown in-app for ~1.5s before Onboarding):
```kotlin
// ui/splash/SplashScreen.kt
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) { delay(1500); onFinished() }
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2E6E43)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_splash),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(26.dp))
                    .clip(RoundedCornerShape(26.dp))
            )
            Text("SmartFarm", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Text(stringResource(R.string.splash_tagline), style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
        }
    }
}
```

**Splash icon source:** `res/drawable/ic_launcher_splash.png` — copied from `AppIcons/android/mipmap-xxxhdpi/ic_launcher.png` (192×192). Do NOT use `R.mipmap.*` in Compose `Image()` — mipmaps need `ImageVector`; use a plain drawable instead.

**NavGraph routing:**
```kotlin
// Show SplashScreen → then OnboardingScreen (if first launch) → else MainApp
composable("splash") {
    SplashScreen(onFinished = {
        if (isFirstLaunch) navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
        else navController.navigate("main") { popUpTo("splash") { inclusive = true } }
    })
}
```

---

## Bar Chart Reactivity

**Bug:** The chart composable receives a snapshot of data at composition time and does not recompose when the underlying list changes.

**Rule:** The chart must read directly from `uiState` collected via `collectAsState()` — never from a one-shot value passed as a plain parameter.

```kotlin
// CORRECT — chart recomposed whenever uiState.entries changes
@Composable
fun FinanceScreen(viewModel: FinanceViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    MonthlyBarChart(entries = uiState.entries)  // ← reactive
}
```

```kotlin
// MonthlyBarChart — derive chart data inside the composable so it recomputes
@Composable
fun MonthlyBarChart(entries: List<FinanceEntry>) {
    val chartData = remember(entries) {   // ← recomputes when entries reference changes
        buildMonthlyTotals(entries)
    }
    Canvas(...) { /* draw chartData */ }
}
```

**Anti-pattern to avoid:**
```kotlin
// BAD — chartData computed once in ViewModel init, never updates
val chartData = financeDao.getAllOnce()   // snapshot, not a Flow
```

---

## Khmer Strings Convention

- All UI labels must have a Khmer entry in `values-km/strings.xml`
- English fallback in `values/strings.xml`
- Reference via `stringResource(R.string.key)`

---

## App Icons

Source: `Mobile/AppIcons/android/mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/ic_launcher.png`

Copy each density bucket to `app/src/main/res/mipmap-*/`:
- `ic_launcher.png` — square icon
- `ic_launcher_round.png` — round icon (copy same file for both)

Densities: mdpi (48px), hdpi (72px), xhdpi (96px), xxhdpi (144px), xxxhdpi (192px).

---

## Custom Fonts

**Fonts used:**
| Font | Script | Weights |
|------|--------|---------|
| Hanuman | Khmer | Regular (`hanuman_regular.ttf`), Bold (`hanuman_bold.ttf`) |
| Inter | Latin | Regular, Medium, SemiBold, Bold (`inter_*.ttf`) |

**Files:** `app/src/main/res/font/*.ttf` (6 files — lowercase, underscores, no hyphens)

**FontFamily definitions** (`ui/theme/Type.kt`):
```kotlin
val HanumanFontFamily = FontFamily(
    Font(R.font.hanuman_regular, FontWeight.Normal),
    Font(R.font.hanuman_bold,    FontWeight.Bold)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_regular,  FontWeight.Normal),
    Font(R.font.inter_medium,   FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold,     FontWeight.Bold)
)
```

**Language-aware typography** — `smartFarmTypography(language: String)` returns a full `Typography` with the right `FontFamily` based on `"km"` or `"en"`.

**Wired in theme** (`ui/theme/Theme.kt`):
```kotlin
val language = LocaleHelper.getLanguage(context)
MaterialTheme(
    colorScheme = colorScheme,
    typography = smartFarmTypography(language),
    content = content
)
```

---

## Shared Preferences as Singleton State (UserPreferences)

When a user preference (e.g., KHR/USD toggle) needs to be shared across multiple ViewModels and persist across launches, use a `@Singleton` class backed by `SharedPreferences` exposing a `StateFlow`.

```kotlin
// util/UserPreferences.kt
@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("smartfarm_prefs", Context.MODE_PRIVATE)
    private val _showKhr = MutableStateFlow(prefs.getBoolean("show_khr", true))
    val showKhr: StateFlow<Boolean> = _showKhr.asStateFlow()

    fun toggleShowKhr() {
        val new = !_showKhr.value
        _showKhr.value = new
        prefs.edit().putBoolean("show_khr", new).apply()
    }
}
```

Inject into any ViewModel that needs it — Hilt provides the same instance everywhere:

```kotlin
@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val financeRepo: FinanceRepository,
    private val userPrefs: UserPreferences    // ← shared singleton
) : ViewModel() {

    init {
        viewModelScope.launch {
            combine(financeRepo.getAll(), userPrefs.showKhr) { entries, showKhr ->
                FinanceUiState(entries = entries, showKhr = showKhr)
            }.collect { _uiState.value = it }
        }
    }

    fun toggleCurrency() = userPrefs.toggleShowKhr()
}
```

Currency formatting that respects the shared preference:
```kotlin
fun formatAmount(amount: Double, showKhr: Boolean): String =
    if (showKhr) "%,.0f ៛".format(amount) else "$%.2f".format(amount / 4000.0)
```

---

## Current Status — Feature-Complete ✅

| Milestone | Status |
|-----------|--------|
| MVVM + Hilt + Room (DB v3) | ✅ |
| 4-tab navigation (Dashboard/Finance/Calendar/Settings) | ✅ |
| Splash (core-splashscreen) + Onboarding (HorizontalPager) | ✅ |
| Finance — CRUD, summary card, KHR/USD, chart, CSV/PDF, edit | ✅ |
| Calendar — month grid, activity types, done toggle, reminders, edit | ✅ |
| Dashboard — finance summary + 7-day events | ✅ |
| Settings — theme, stats, export, backup/restore, clear | ✅ |
| Khmer localization (`values-km/strings.xml`) | ✅ |
| Material3 dark mode | ✅ |
| App icon from `AppIcons/` | ✅ |
| Custom fonts — Hanuman (Khmer) + Inter (English) | ✅ |

**Android is feature-complete. No known remaining work.**

---

## Always Check

- `.claude/memory.md` — shared rules (amount in KHR, USD rate **4,000**, FarmActivity types, categories)
- `../PLAN.md` — feature matrix
- `../FIGMA_BRIEF.md` (Part B, B0–B7) — Material 3 design spec for all screens
