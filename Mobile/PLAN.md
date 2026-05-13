# SmartFarm Mobile — Cross-Platform Build Plan

> Single source of truth for iOS and Android.  
> Platform-specific implementation details live in each project's own folder.

---

## Design References

- **Figma Brief:** `FIGMA_BRIEF.md` — primary native design spec (iOS HIG + Material 3), all screens A0–A7 / B0–B7
- **Web demo:** `../../src/App.jsx` — original process-flow reference (mobile is a superset)

**App flow:** Splash (1.5s animation) → Onboarding (3 slides) → Main app

**Onboarding slides:**
1. 💰 "តាមដានហិរញ្ញវត្ថុ" — track income/expense in KHR or USD
2. 📅 "គ្រោងសកម្មភាព" — schedule farming activities with reminders
3. 📊 "រីកចម្រើនជាមួយទិន្នន័យ" — monthly reports for better decisions

**All three platforms (Web, iOS, Android) now have full feature parity.**

| Screen | Web | iOS | Android |
|--------|-----|-----|---------|
| Splash | ✅ | ✅ | ✅ |
| Onboarding (3 slides) | ✅ | ✅ | ✅ |
| Dashboard — balance hero + bar chart | ✅ | ✅ | ✅ |
| Dashboard — upcoming 7-day activities | ✅ | ✅ | ✅ |
| Dashboard — recent transactions | ✅ | ✅ | ✅ |
| Finance — balance card + KHR/USD toggle | ✅ | ✅ | ✅ |
| Finance — filter chips (All/Income/Expense) | ✅ | ✅ | ✅ |
| Finance — FAB → bottom sheet CRUD | ✅ | ✅ | ✅ |
| Finance — bar chart (6 months) | ✅ | ✅ | ✅ |
| Finance — edit record | — | ✅ | ✅ |
| Calendar — month grid + dot markers | ✅ | ✅ | ✅ |
| Calendar — per-day list + done toggle | ✅ | ✅ | ✅ |
| Calendar — edit activity | — | ✅ | ✅ |
| Settings — theme toggle | ✅ | ✅ | ✅ |
| Settings — language switch (KH/EN) | ✅ | ✅ | ✅ |
| Settings — CSV export | ✅ | ✅ | ✅ |
| Settings — JSON backup/restore | ✅ | ✅ | ✅ |
| Settings — clear all data | ✅ | ✅ | ✅ |
| Dark mode | ✅ (CSS vars) | ✅ | ✅ |

---

## Project Overview

SmartFarm Mobile is an offline-first farm-management app for Cambodian small-scale farmers, built for both iOS (SwiftUI) and Android (Jetpack Compose). Both platforms deliver the same feature set with a Khmer-first UI and no internet dependency for core features.

| | iOS | Android |
|--|-----|---------|
| **Framework** | SwiftUI + CoreData | Jetpack Compose + Room |
| **Architecture** | MVVM + ObservableObject | MVVM + Hilt + StateFlow |
| **Min OS** | iOS 14.0 | Android 8.0 (SDK 26) |
| **Localization** | Khmer + English | Khmer + English |
| **Dependencies** | None (zero pods/SPM) | Room, Hilt, Navigation Compose, core-splashscreen |
| **Completion** | ~100% ✅ | ~100% ✅ |

---

## Feature Matrix

| Feature | iOS | Android |
|---------|:---:|:-------:|
| **Splash screen** | ✅ | ✅ |
| **Onboarding** — 3-slide first-launch intro | ✅ | ✅ |
| **Finance Tracker** — income/expense CRUD | ✅ | ✅ |
| **Finance** — dual currency KHR / USD | ✅ | ✅ |
| **Finance** — category filters | ✅ | ✅ |
| **Finance** — live balance summary card | ✅ | ✅ |
| **Finance** — monthly bar chart | ✅ | ✅ |
| **Finance** — CSV export | ✅ | ✅ |
| **Finance** — PDF report | ✅ | ✅ |
| **Calendar** — activity CRUD | ✅ | ✅ |
| **Calendar** — month grid with dot markers | ✅ | ✅ |
| **Calendar** — per-day activity list | ✅ | ✅ |
| **Calendar** — activity type color coding | ✅ | ✅ |
| **Calendar** — mark done toggle | ✅ | ✅ |
| **Notifications** — 1-day-before reminder | ✅ | ✅ |
| **Notifications** — on-the-day reminder | ✅ | ✅ |
| **Dashboard** — summary cards | ✅ | ✅ |
| **Dashboard** — upcoming 7-day events | ✅ | ✅ |
| **Dashboard** — monthly profit/loss | ✅ | ✅ |
| **Backup** — JSON export | ✅ | ✅ |
| **Backup** — JSON restore | ✅ | ✅ |
| **Settings** tab | ✅ | ✅ |
| **Khmer UI** — full localization | ✅ | ✅ |
| **Dark mode** | ✅ | ✅ |
| **Language switch (KH/EN)** | ✅ | ✅ |
| **Localizable.strings / strings.xml complete** | ✅ | ✅ |
| **App icon** | ✅ | ✅ |
| **Custom fonts (Hanuman + Inter)** | ✅ | ✅ |

**Legend:** ✅ Done · 🟡 Partial · 🔴 Not started

---

## Shared Data Models

Both platforms implement the same logical entities. Field names map to platform conventions.

### Transaction (FinanceEntry on Android)

| Field | Type | Values |
|-------|------|--------|
| `id` | UUID / Long | auto-generated |
| `title` | String | free text |
| `amount` | Double | stored in KHR |
| `type` | Enum | `income` / `expense` |
| `category` | String | Seeds · Fertilizer · Labor · Tools · Sales |
| `note` | String | optional |
| `date` | Date / Long (millis) | |

### FarmActivity (EventEntry on Android)

| Field | Type | Notes |
|-------|------|-------|
| `id` | UUID / Long | auto-generated |
| `title` | String | |
| `description` / `notes` | String | optional |
| `type` | String | ដំណាំ · ស្រោចទឹក · ដាក់ជី · ថែទាំ · ការពារ · ច្រូតកាត់ · ប្រមូលផល · ត្រួតពិនិត្យ |
| `date` | Date / Long (millis) | |
| `isNotified` / `hasReminder` | Bool | |
| `reminderOffsetMinutes` | Int | default 1440 (1 day) |

> **Rule:** Amount is always stored in KHR. Conversion to USD is display-only at 4,000 KHR/USD.

---

## Shared UX Design System

Both platforms use the same visual language.

### Colors

| Token | Light | Dark | Usage |
|-------|-------|------|-------|
| `primary` | `#2E6E43` | `#6FBE8E` | Buttons, active tabs, income |
| `expense` / `danger` | `#B0382A` | `#E26B5C` | Expense amounts, delete |
| `surface` | `#FFFFFF` | `#171C19` | Cards, sheets |
| `background` | `#F5F6F5` | `#0F1312` | Screen background |
| `neutral` | `#6B6E6A` | `#9EA3A0` | Secondary text |

### Tab Structure

```
Tab 1 — 📊 Dashboard   (ផ្ទាំងគ្រប់គ្រង)
Tab 2 — 💰 Finance     (ហិរញ្ញវត្ថុ)
Tab 3 — 📅 Calendar    (ប្រតិទិន)
Tab 4 — ⚙️ Settings    (ការកំណត់)   [iOS only — Android uses system settings]
```

### Interaction Patterns

- **Add entry** — floating action button (+) → bottom sheet / dialog
- **Delete entry** — swipe-to-delete (iOS) / trash icon (Android)
- **Currency toggle** — KHR / USD chip pair in finance header
- **Category filter** — horizontal chip row: All · Income · Expense

---

## iOS Build Plan

> Environment rules and code templates: `.claude/skills/ios.md`  
> Design spec: `FIGMA_BRIEF.md` Part A (A0–A7)

### Phase Summary

| Phase | Owner | Goal | Dependency |
|-------|-------|------|------------|
| **1** | nemsothea | CoreData schema + folder structure + `MainTabView` | — |
| **2a** | nemsothea | Finance Tracker (CRUD, summary card, filters) | Phase 1 |
| **2b** | nemsothea | Calendar & Reminders (CRUD, `NotificationManager`) | Phase 1 |
| **3** | nemsothea | Dashboard — live data from Finance + Calendar | Phase 2 |
| **4** | nemsothea | Design system, reusable components, animations, dark mode | Phase 3 |
| **5** | nemsothea | Export & Reports (chart, CSV, PDF, share sheet) | Phase 4 |
| **6** | nemsothea | Backup & Restore (JSON export/import, iCloud Drive) | Phase 5 |

### iOS Constraints (Xcode 13 / iOS 14)

- `NavigationView` + `NavigationLink` — **not** `NavigationStack` (iOS 16+)
- `GeometryReader` + `Rectangle` for charts — **not** Swift Charts (iOS 16+)
- `PreviewProvider` structs — **not** `#Preview` macro (Xcode 15+)
- `ObservableObject` + `@Published` — **not** `@Observable` (iOS 17+)
- Custom `TextField` search bar — **not** `.searchable` (iOS 15+)
- `UNUserNotificationCenter` for local notifications

### iOS Current Status

**Phases 1–6 complete + Splash/Onboarding (Sprint 4)**

- [x] CoreData schema — `Transaction` + `FarmActivity` entities
- [x] `FarmViewModel` — full CRUD, KHR/USD toggle, monthly chart data
- [x] `MainTabView` — 4 tabs: Dashboard, Finance, Calendar, Settings
- [x] `DashboardView`, `FinanceListView`, `CalendarTabView`, `SettingsView`
- [x] `MonthlyChartView` — GeometryReader + Rectangle bars
- [x] `ExportManager` — CSV + PDF via UIGraphicsPDFRenderer
- [x] `BackupManager` — JSON export/restore with Codable
- [x] `NotificationManager` — UNCalendarNotificationTrigger reminders
- [x] `SplashView` — 1.5s animated green splash (Sprint 4)
- [x] `OnboardingView` — 3-slide PageTabView, first-launch gated via @AppStorage (Sprint 4)
- [x] `isDone` attribute on FarmActivity — CoreData lightweight migration enabled (Sprint 5)
- [x] `toggleDone(_:)` in FarmViewModel — flips isDone + saves (Sprint 5)
- [x] Calendar activity row — checkmark toggle, strikethrough, opacity dim when done (Sprint 5)
- [x] Disabled Save buttons — AddActivitySheet + AddTransactionSheet (Sprint 5)

---

## Android Build Plan

> Source: `smartfarm-android/app/src/main/java/com/smartfarm/android/`  
> Environment rules and code templates: `.claude/skills/android.md`  
> Design spec: `FIGMA_BRIEF.md` Part B (B0–B7)

### What Is Done

- Full MVVM architecture with Hilt DI
- Room database v2 — `FinanceEntry`, `EventEntry` (with `type` field) + DAOs + Repositories + Migration
- 4-tab navigation — Dashboard, Finance, Calendar, Settings
- Finance screen — list, summary card, KHR/USD toggle, bar chart, add dialog, delete, CSV/PDF export
- Calendar screen — month grid, activity type colors, event list, add dialog, delete, WorkManager reminders
- Dashboard screen — finance summary + upcoming events (7 days)
- Settings screen — CSV/PDF export, JSON backup/restore, stats
- Full Khmer localization (`values-km/strings.xml`)
- Material3 dark mode
- System splash screen via `core-splashscreen` + `Theme.SmartFarm.Splash` (Sprint 4)
- Onboarding screen — 3-slide HorizontalPager, first-launch gated via SharedPreferences (Sprint 4)
- Room DB v3 migration — `isDone` column on events table (Sprint 5)
- `toggleDone` in CalendarViewModel (Sprint 5)
- Calendar EventItem — Checkbox, strikethrough text, alpha dim when done (Sprint 5)
- AddEventDialog — red outline on empty title, Save disabled when blank (Sprint 5)

### Sprint 6 — Edit Records + iOS Dark Mode (complete)

**iOS:**
- `ExpenseRed.colorset` — adaptive `#B0382A` light / `#E26B5C` dark
- `updateTransaction` + `updateActivity` in `FarmViewModel`
- `FinanceListView` — tap row → `TransactionFormSheet` (unified add/edit), expense color → `Color("ExpenseRed")`
- `CalendarTabView` — pencil button → `ActivityFormSheet` (unified add/edit)

**Android:**
- `FinanceScreen` — `EntryFormDialog(editingEntry:)` pre-populated; edit icon on each entry
- `CalendarScreen` — `EventFormDialog(editingEvent:)` pre-populated; edit icon on each event item
- `strings.xml` + `strings-km.xml` — `edit_entry`, `edit_event`

### Sprint 7 — iOS Dark Mode (COMPLETE ✅)

All screens now use `Color("PrimaryGreen")`, `Color("ExpenseRed")`, and semantic system colors.
Both `PrimaryGreen.colorset` and `ExpenseRed.colorset` exist in `Assets.xcassets`. No hardcoded hex values remain.

### Sprint 8 — Bug Fixes + Language Support (COMPLETE ✅)

All 6 issues fixed: language toggle (KH/EN) in Settings on both platforms, full Khmer localization, Android dialog layout, bar chart reactivity, custom splash with Khmer text, USD rate corrected to 4,000.

### Sprint 10 — Bug Fixes + Web Dashboard (COMPLETE ✅)

**iOS:**
- `DashboardView` — fixed stale `RecentTransactionRow` / `UpcomingActivityRow`: extract primitive value types (`title`, `category`, `formattedAmount`, `isIncome`, etc.) in the `ForEach` closure instead of passing `NSManagedObject` references; SwiftUI now sees changed structs and re-renders rows immediately
- `FarmViewModel` — added `@Published var dataVersion: Int`, incremented in `fetchAll()`; `List` uses `.id(vm.dataVersion)` to force full rebuild on any data change
- `MonthlyChartView` — fixed bar chart: replaced broken `ZStack(alignment: .bottom) + .offset()` with `ZStack(alignment: .topLeading) + .position(x:y:)` to correctly bottom-anchor bars
- `FinanceListView` / `CalendarTabView` — fixed multiple `.sheet` bug (iOS 14 only honors the last one): unified into a single `.sheet(item:)` using a `FinanceSheet` / `CalendarSheet` enum
- `MainTabView` — added `.id(appLanguage)` to `TabView` to force tab bar recreation when language changes
- `FarmViewModel.showKHR` — persisted to `UserDefaults` so KHR/USD preference survives tab switches and relaunches

**Android:**
- `UserPreferences` — new `@Singleton` class backed by `SharedPreferences`, exposes `showKhr: StateFlow<Boolean>`; injected into `FinanceViewModel` + `DashboardViewModel` via Hilt so currency preference is shared and persists
- All hardcoded Khmer strings moved to `strings.xml` / `strings-km/strings.xml`

**Web:**
- Added Dashboard tab (📊) as the first tab — monthly balance hero card, 6-month income/expense bar chart, upcoming 7-day activities, recent transactions
- Fixed seed data: replaced hardcoded April 2026 dates with `_d(offset)` helper producing today-relative local dates
- Fixed timezone bug in Dashboard: `sameMonth()` and upcoming filter now parse `"YYYY-MM-DD"` by splitting on `-` instead of `new Date(dateStr)` (UTC midnight → local time shift was filtering out current-month data)
- Bumped localStorage keys to `sf_txns_v2` / `sf_acts_v2` to evict stale seed data from earlier sessions

### Sprint 9 — App Icons + Custom Fonts (COMPLETE ✅)

| # | Task | Platform | Details |
|---|------|----------|---------|
| 1 | App icons from `AppIcons/` | iOS + Android | Replaced AppIcon.appiconset (18 sizes) + mipmap-\* (5 densities, square + round) |
| 2 | Khmer font: Hanuman (Regular + Bold) | iOS + Android | Google Fonts OFL, ~120 KB each |
| 3 | English font: Inter (Regular/Medium/SemiBold/Bold) | iOS + Android | rsms/inter v4.0, ~400 KB each |
| 4 | Language-aware font switching | iOS + Android | iOS: `AppFont.regular/medium/semibold/bold(size:)` reads `appLanguage`; Android: `smartFarmTypography(language)` in Theme |
| 5 | Splash icon updated to match app icon | iOS + Android | iOS: `Image("SplashLogo")` from `Assets.xcassets/SplashLogo.imageset/`; Android: `R.drawable.ic_launcher_splash` copied from xxxhdpi mipmap |

**iOS font registration:** `CTFontManagerRegisterFontsForURL` called in `SmartFarmApp.init()`. Fonts in `SmartFarm/Fonts/`, registered in Xcode Resources build phase.

**Android font registration:** `res/font/` resource directory. `HanumanFontFamily` + `InterFontFamily` defined in `Type.kt`, switched via `LocaleHelper.getLanguage()` in `SmartFarmTheme`.

### Sprint 8 — Bug Fixes + Language Support

| # | Issue | Platform | Notes |
|---|-------|----------|-------|
| 1 | Language switch in Settings (KH ↔ EN) | iOS + Android | Settings screen has no toggle yet; locale is hardcoded |
| 2 | Full localization (`Localizable.strings` / `strings.xml`) | iOS + Android | iOS has zero `.strings` files; Android `values-km` incomplete |
| 3 | Android Add/Edit dialog UI — TextField not full-width, layout broken | Android | ModalBottomSheet fields need `fillMaxWidth()` + proper spacing |
| 4 | Bar chart not updating when data changes | iOS + Android | iOS: computed property not `@Published`; Android: chart not collecting from StateFlow |
| 5 | Android splash screen missing Khmer/English text | Android | `core-splashscreen` cannot render text — needs custom Composable splash after system splash |
| 6 | USD conversion rate wrong | iOS + Android | Was 4,100 → correct rate is **4,000 KHR/USD** |

---

## Definition of Done (Both Platforms)

- [ ] All CRUD operations persist after app restart
- [ ] Finance balance and calendar state match after kill/relaunch
- [ ] Notifications fire correctly with app closed
- [ ] CSV export opens correctly in Excel / Google Sheets
- [ ] Backup JSON can restore on a fresh install
- [ ] UI renders correctly in light mode and dark mode
- [ ] All visible labels are in Khmer (with English fallback)
- [ ] Amount display toggles correctly between KHR and USD at 4,000 rate
- [ ] No crashes on empty state (no transactions, no activities)

---

## Team

| Member | Role | Platform | Responsibility |
|--------|------|----------|----------------|
| **nemsothea** | Senior iOS & Android Developer | iOS + Android | All phases — architecture, all modules, export, backup, UI polish |

---

## Repository Layout

```
Mobile/
├── PLAN.md                  ← this file
├── FIGMA_BRIEF.md           ← native design spec (iOS HIG + Material 3), all screens
├── .claude/
│   ├── memory.md            ← shared rules, data models, team
│   └── skills/
│       ├── ios.md           ← iOS environment, templates, patterns
│       └── android.md       ← Android environment, templates, patterns
├── smartfarm-ios/
│   └── SmartFarm/           ← Xcode project source
└── smartfarm-android/
    └── app/src/main/        ← Android source
```
