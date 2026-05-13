# SmartFarm Mobile — Project Memory

Cross-platform context for **nemsothea** (Senior iOS & Android Developer), solo build.

---

## Project

- **App:** SmartFarm — offline farm-management for Cambodian small-scale farmers
- **Platforms:** iOS (SwiftUI) + Android (Jetpack Compose)
- **Goal:** Finance tracking + activity calendar + dashboard, fully offline, Khmer-first UI
- **Version:** v1 — small-scale farmers only
- **Repo:** https://github.com/nemsothea/smartfarm.git
- **Plan:** See `../PLAN.md` for full feature matrix and build order

---

## Developer

| Member | Role |
|--------|------|
| **nemsothea** | Senior iOS & Android Developer — all phases, both platforms |

---

## Shared Rules (Both Platforms)

- Amount always stored in **KHR**; USD display-only at **4,000 KHR/USD**
- All visible UI text in **Khmer** (English as fallback)
- **Offline first** — zero network calls for core features
- No pest & disease guide (removed from v1 scope)

### FarmActivity Types (8)
`ដំណាំ` · `ស្រោចទឹក` · `ដាក់ជី` · `ថែទាំ` · `ការពារ` · `ច្រូតកាត់` · `ប្រមូលផល` · `ត្រួតពិនិត្យ`

### Transaction Categories (5)
`Seeds` · `Fertilizer` · `Labor` · `Tools` · `Sales`

---

## iOS Context

**Path:** `smartfarm-ios/SmartFarm/`  
**Skill file:** `.claude/skills/ios.md`  
**Detailed plan:** `.claude/plan.md`

| Decision | Value | Why |
|----------|-------|-----|
| iOS Deployment Target | 14.0 | Broad device coverage, Xcode 13 compatible |
| Swift | 5.5 | Xcode 13 default |
| Dependencies | None | Zero CocoaPods/SPM — no setup friction |
| No NavigationStack | Use `NavigationView` | iOS 16+ only |
| No Swift Charts | `GeometryReader` + `Rectangle` | iOS 16+ only |
| No `#Preview` macro | Use `PreviewProvider` | Xcode 15+ only |
| No `@Observable` | Use `ObservableObject` | iOS 17+ only |
| No `.searchable` | Custom `TextField` | iOS 15+ only |

### CoreData Entities

**Transaction:** `id: UUID` · `amount: Double` · `type: String` · `category: String` · `note: String` · `date: Date`

**FarmActivity:** `id: UUID` · `title: String` · `type: String` · `notes: String` · `date: Date` · `isNotified: Bool`

### iOS Files in Place (Sprint 6 complete)

| File | Status |
|------|--------|
| `SmartFarmApp.swift` | ✅ |
| `Persistence.swift` | ✅ |
| `SmartFarm.xcdatamodeld` | ✅ Transaction + FarmActivity entities |
| `ViewModels/FarmViewModel.swift` | ✅ full CRUD + updateTransaction + updateActivity |
| `Views/MainTabView.swift` | ✅ 4 tabs |
| `Views/Onboarding/SplashView.swift` | ✅ |
| `Views/Onboarding/OnboardingView.swift` | ✅ 3-slide, first-launch gated |
| `Views/Dashboard/DashboardView.swift` | ✅ |
| `Views/Finance/FinanceListView.swift` | ✅ + TransactionFormSheet (add/edit) |
| `Views/Finance/MonthlyChartView.swift` | ✅ GeometryReader bars |
| `Views/Calendar/CalendarTabView.swift` | ✅ + ActivityFormSheet (add/edit) |
| `Views/Settings/SettingsView.swift` | ✅ |
| `Utilities/ExportManager.swift` | ✅ CSV + PDF |
| `Utilities/BackupManager.swift` | ✅ JSON backup/restore |
| `Utilities/NotificationManager.swift` | ✅ |
| `Utilities/DocumentPicker.swift` | ✅ |

### iOS Remaining — Sprint 7 (Dark Mode)
- Full adaptive color pass (see `.claude/skills/ios.md` Sprint 7 section)
- Create `PrimaryGreen.colorset` in Assets.xcassets
- Replace hardcoded hex colors in all View files

### Architecture
- `PersistenceController` — singleton, injected via `.environment(\.managedObjectContext)`
- `FarmViewModel` — shared `@EnvironmentObject` across all tabs
- `NotificationManager` — static utility struct, not in ViewModel

---

## Android Context

**Path:** `smartfarm-android/app/src/main/java/com/smartfarm/android/`  
**Skill file:** `.claude/skills/android.md`

| Decision | Value |
|----------|-------|
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Database | Room |
| DI | Hilt |
| State | StateFlow + ViewModel |
| Navigation | Navigation Compose |

### Android Status — Sprint 6 complete, feature-complete

All phases A–F done:
- Full MVVM + Hilt DI architecture
- Room DB v3 — `FinanceEntry`, `EventEntry` (with `isDone` + `type`) + DAOs + Repositories
- 4-tab navigation — Dashboard, Finance, Calendar, Settings
- Finance screen — list, summary card, KHR/USD, chart, add/edit/delete, CSV/PDF export
- Calendar screen — month grid, activity types, done toggle, add/edit/delete, WorkManager reminders
- Dashboard — finance summary + 7-day events
- Settings — stats, CSV/PDF export, JSON backup/restore, clear all
- Khmer localization (`values-km/strings.xml`)
- Material3 dark mode
- Splash (core-splashscreen) + Onboarding (3-slide HorizontalPager)

---

## v2 Features (Do Not Add in v1)

- Multi-field / plot management
- Worker & labor attendance
- Inventory & stock levels
- Yield recording per crop
- Profit per field / per crop
- Supplier contacts
