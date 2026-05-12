# SmartFarm Mobile — Cross-Platform Build Plan

> Single source of truth for iOS and Android.  
> Platform-specific implementation details live in each project's own folder.

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
| **Completion** | ~90% | ~95% |

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
| **Dark mode** | 🔴 | ✅ |

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

> **Rule:** Amount is always stored in KHR. Conversion to USD is display-only at 4,100 KHR/USD.

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

> Full phase breakdown: `smartfarm-ios/.claude/plan.md`  
> Environment rules: `smartfarm-ios/.claude/memory.md`

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

### Remaining Work

#### Phase F — UI Polish (optional)
- Consistent use of design system tokens
- iOS Dark mode — full adaptive color pass (most screens already use system colors)

---

## Definition of Done (Both Platforms)

- [ ] All CRUD operations persist after app restart
- [ ] Finance balance and calendar state match after kill/relaunch
- [ ] Notifications fire correctly with app closed
- [ ] CSV export opens correctly in Excel / Google Sheets
- [ ] Backup JSON can restore on a fresh install
- [ ] UI renders correctly in light mode and dark mode
- [ ] All visible labels are in Khmer (with English fallback)
- [ ] Amount display toggles correctly between KHR and USD at 4,100 rate
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
├── smartfarm-ios/
│   ├── .claude/
│   │   ├── plan.md          ← iOS phase details + code templates
│   │   └── memory.md        ← iOS environment decisions
│   └── SmartFarm/           ← Xcode project source
└── smartfarm-android/
    └── app/src/main/        ← Android source
```
