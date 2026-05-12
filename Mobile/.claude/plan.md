# SmartFarm iOS — Implementation Plan
> Solo build by **nemsothea** (Senior iOS & Android Developer)

---

## Phase 1 — Project Foundation

### 1.1 Folder Structure
```
SmartFarm/
├── Models/
├── ViewModels/
├── Views/
│   ├── Dashboard/
│   ├── Finance/
│   └── Calendar/
└── Utilities/
```

### 1.2 CoreData Schema
Define `.xcdatamodeld` with 2 entities:

**Transaction**
- id: UUID
- amount: Double
- type: String (`income` / `expense`)
- category: String (`Seeds` / `Fertilizer` / `Labor` / `Tools` / `Sales`)
- note: String
- date: Date

**FarmActivity**
- id: UUID
- title: String
- type: String (`ដំណាំ` / `ស្រោចទឹក` / `ដាក់ជី` / `ថែទាំ` / `ការពារ` / `ច្រូតកាត់` / `ប្រមូលផល` / `ត្រួតពិនិត្យ`)
- notes: String
- date: Date
- isNotified: Bool

### 1.3 Core Files to Create
- `Utilities/PersistenceController.swift` — replace Persistence.swift boilerplate
- `ViewModels/FarmViewModel.swift` — shared `ObservableObject` with `@Published` arrays
- `Views/MainTabView.swift` — `TabView` with 3 tabs: Dashboard · Finance · Calendar
- `SmartFarmApp.swift` — inject `managedObjectContext` + `FarmViewModel` via environment

### 1.4 Seed Data
`seedSampleData()` in PersistenceController, guarded by `UserDefaults` flag `"hasSeededData"`.
Seed: 3 Transactions (2 income, 1 expense) + 3 FarmActivities across different types.

**Deliverable:** App builds and runs showing an empty 3-tab view. CoreData persists after restart.

---

## Phase 2a — Finance Tracker

### FinanceViewModel
```
@Published var transactions: [Transaction]
var totalIncome: Double
var totalExpense: Double
var profit: Double
func add(amount:type:category:note:date:)
func delete(_ transaction: Transaction)
func filtered(by type: String) -> [Transaction]
```

### Views
- `FinanceListView` — List + filter segmented control (All / Income / Expense)
- `SummaryCardView` — balance, income, expense in colored cards
- `AddTransactionView` — Form: type toggle, amount TextField, DatePicker, category Picker, note
- `TransactionRowView` — single row with amount color-coded green/red

### Currency
- Store in KHR always
- Display toggle: KHR ↔ USD at 4,100 rate
- `NumberFormatter` for thousands separator

**Deliverable:** Finance tab fully functional — CRUD, live totals, KHR/USD toggle.

---

## Phase 2b — Calendar & Reminders

### CalendarViewModel
```
@Published var activities: [FarmActivity]
@Published var selectedDate: Date
func activitiesFor(date: Date) -> [FarmActivity]
func add(title:type:notes:date:)
func delete(_ activity: FarmActivity)
func toggleDone(_ activity: FarmActivity)
```

### Views
- `CalendarView` — 7×6 month grid (custom, not DatePicker) + dot markers + day tap
- `DayActivitiesView` — list of activities for selected day
- `AddActivityView` — Form: title, type Picker (8 types), notes, DatePicker, reminder toggle
- `ActivityRowView` — title, type badge, done toggle (⬜/✅)

### NotificationManager (`Utilities/NotificationManager.swift`)
```
requestPermission()
schedule(for activity: FarmActivity)   // on-day at 08:00 + 1-day-before at 08:00
cancel(for activity: FarmActivity)
```

**Deliverable:** Calendar tab functional — month grid, per-day list, mark done, local notifications.

---

## Phase 3 — Dashboard

- `DashboardView` with 4–6 summary cards
- Data sourced from FarmViewModel (shared via `@EnvironmentObject`)
- Cards: Balance, This Month Income, This Month Expense, Upcoming Activities (next 7 days)
- `NavigationLink` from each card to the relevant tab detail

**Deliverable:** Dashboard shows live data from Finance + Calendar.

---

## Phase 4 — Design System & UI Polish

- `Utilities/DesignSystem.swift` — `AppColors`, `AppFonts`, spacing constants
- `AppColors` uses `Color` assets for automatic dark mode
- Reusable components: `FarmCard`, `PrimaryButton`, `SectionHeader`, `EmptyStateView`
- List animations: `.transition(.opacity.combined(with: .slide))`
- Button feedback: `.scaleEffect` on tap via `@State var isPressed`
- Pull-to-refresh: `UIViewRepresentable` wrapper for `UIRefreshControl`

**Deliverable:** Consistent design system across all screens, dark mode verified.

---

## Phase 5 — Export & Reports

- `ReportViewModel` — group `[Transaction]` by month, compute monthly profit/loss array
- Bar chart: `GeometryReader` + `Rectangle` shapes (no Swift Charts)
- Horizontal scroll for 6-month history
- CSV export: map transactions to comma-separated rows → `UIActivityViewController`
- PDF export: `PDFKit` → `PDFPage` with drawn text + chart snapshot
- Share sheet: `UIActivityViewController` in `UIViewControllerRepresentable`

**Deliverable:** Report screen with bar chart + Share button exporting CSV and PDF.

---

## Phase 6 — Backup & Restore

- `BackupManager.swift`
  - `exportJSON() -> Data` — `Codable` encode all Transactions + Activities
  - `importJSON(_ data: Data)` — decode → clear CoreData → re-insert
- `DocumentPickerView` — `UIDocumentPickerViewController` in `UIViewControllerRepresentable`
  - Save mode: write `.smartfarm` JSON file
  - Open mode: read file → confirm alert → restore
- Weekly backup reminder via `UNUserNotificationCenter`
- iCloud Drive: `FileManager.url(for: .documentDirectory)` + ubiquity container

**Deliverable:** Full backup and restore working across app reinstall.

---

## Build Order

```
Phase 1 (Foundation)
    ├── Phase 2a (Finance)
    └── Phase 2b (Calendar)          ← run after Phase 1, can overlap 2a
            └── Phase 3 (Dashboard)
                    └── Phase 4 (UI Polish)
                            └── Phase 5 (Export)
                                    └── Phase 6 (Backup)
```

---

## Definition of Done

- [ ] Builds with 0 errors, 0 warnings on Xcode 13
- [ ] All CRUD operations persist after app restart
- [ ] Notifications fire when app is closed
- [ ] CSV opens correctly in Numbers / Excel
- [ ] PDF renders finance data correctly
- [ ] Backup JSON restores on a fresh install
- [ ] Light mode and dark mode both verified
- [ ] All visible text is in Khmer
