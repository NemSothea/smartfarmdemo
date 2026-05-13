# SmartFarm Build Skill — iOS

You are helping build the SmartFarm iOS app — a SwiftUI MVVM app for Cambodian small-scale farmers.
Read `.claude/memory.md` for full project context and `../PLAN.md` for the phased build order.
For visual design spec, read `../FIGMA_BRIEF.md` (Part A — iOS Native, sections A0–A7).

---

## Environment (NON-NEGOTIABLE)

| Item | Value |
|------|-------|
| Xcode | 26.4 (Build 17E192) |
| Swift | 5.5 compatible (iOS 14 target) |
| iOS Deployment Target | **14.0** (already set in project.pbxproj) |
| Dependencies | None — no CocoaPods, no SPM packages |

---

## Developer

| Member | Role | Owns |
|--------|------|------|
| **nemsothea** | Senior iOS & Android Developer | All phases — solo |

---

## iOS 14 / Xcode 13-compat Rules

Even though Xcode 26.4 is installed, the deployment target is iOS 14. These APIs do not back-deploy.

### ALWAYS USE
- `NavigationView` + `NavigationLink(destination:label:)` — never `NavigationStack`
- `TabView` + `.tabItem`
- `@StateObject`, `@ObservedObject`, `@EnvironmentObject`, `@Published`
- `@FetchRequest(sortDescriptors:animation:)` for CoreData
- `ToolbarItem(placement: .navigationBarTrailing / .navigationBarLeading)`
- `struct X_Previews: PreviewProvider` — never `#Preview {}` macro
- `UNUserNotificationCenter` for notifications
- `UIActivityViewController` in `UIViewControllerRepresentable` for sharing
- `UIDocumentPickerViewController` in `UIViewControllerRepresentable` for files
- `PDFKit` for PDF export
- `GeometryReader` + `Rectangle` for bar charts
- `if let x = x { }` — never `if let x { }` (shorthand requires Swift 5.7 / Xcode 14)

### NEVER USE
- `NavigationStack`, `NavigationSplitView` — iOS 16+
- `.searchable()` — use custom `TextField` search bar
- `Charts` / `Chart {}` — iOS 16+
- `SwiftData`, `@Model` — iOS 17+
- `ShareLink` — iOS 16+
- `@Observable` — iOS 17+
- `ContentUnavailableView` — iOS 17+
- `#Preview {}` — Xcode 15+

---

## Actual Project Folder Structure

```
SmartFarm/
├── SmartFarmApp.swift
├── Persistence.swift              ← CoreData stack (PersistenceController singleton)
├── SmartFarm.xcdatamodeld         ← Transaction + FarmActivity entities
├── FarmViewModel.swift            ← (legacy location — now also in ViewModels/)
├── Assets.xcassets/
│   ├── AccentColor.colorset
│   ├── PrimaryGreen.colorset      ← #2E6E43 light / #6FBE8E dark ✅
│   └── ExpenseRed.colorset        ← #B0382A light / #E26B5C dark ✅
├── ViewModels/
│   └── FarmViewModel.swift        ← shared MVVM, full CRUD + updateTransaction/updateActivity
├── Views/
│   ├── MainTabView.swift          ← 4 tabs, .accentColor(Color("PrimaryGreen"))
│   ├── Onboarding/
│   │   ├── SplashView.swift       ← 1.5s animated green splash
│   │   └── OnboardingView.swift   ← 3-slide PageTabView, first-launch gated @AppStorage
│   ├── Dashboard/
│   │   └── DashboardView.swift
│   ├── Finance/
│   │   ├── FinanceListView.swift  ← list + TransactionFormSheet (add/edit unified)
│   │   └── MonthlyChartView.swift ← GeometryReader bar chart
│   ├── Calendar/
│   │   └── CalendarTabView.swift  ← month grid + ActivityFormSheet (add/edit unified)
│   └── Settings/
│       └── SettingsView.swift
└── Utilities/
    ├── BackupManager.swift        ← JSON export/restore (Codable)
    ├── DocumentPicker.swift       ← UIDocumentPickerViewController wrapper
    ├── ExportManager.swift        ← CSV + PDF via UIGraphicsPDFRenderer
    └── NotificationManager.swift  ← UNCalendarNotificationTrigger reminders
```

---

## CoreData Entities

### Transaction
```
id: UUID | amount: Double | type: String | category: String | note: String | date: Date
type → "income" / "expense"
category → "Seeds" / "Fertilizer" / "Labor" / "Tools" / "Sales"
```

### FarmActivity
```
id: UUID | title: String | type: String | notes: String | date: Date | isNotified: Bool | isDone: Bool
```
type values: `ដំណាំ` · `ស្រោចទឹក` · `ដាក់ជី` · `ថែទាំ` · `ការពារ` · `ច្រូតកាត់` · `ប្រមូលផល` · `ត្រួតពិនិត្យ`

---

## Color System (Sprint 7 — COMPLETE ✅)

All screens use named color assets. No hardcoded hex values remain.

| Token | Asset name | Light | Dark |
|-------|-----------|-------|------|
| Primary green | `Color("PrimaryGreen")` | `#2E6E43` | `#6FBE8E` |
| Expense red | `Color("ExpenseRed")` | `#B0382A` | `#E26B5C` |
| Background | `Color(.systemGroupedBackground)` | system | auto |
| Surface / card | `Color(.systemBackground)` | system | auto |
| Secondary text | `Color(.secondaryLabel)` | system | auto |
| Dividers | `Color(.separator)` | system | auto |

### ColorSet JSON template
```json
{
  "colors": [
    {
      "idiom": "universal",
      "color": { "color-space": "srgb", "components": { "red": "0.180", "green": "0.431", "blue": "0.263", "alpha": "1.000" } }
    },
    {
      "appearances": [{ "appearance": "luminosity", "value": "dark" }],
      "idiom": "universal",
      "color": { "color-space": "srgb", "components": { "red": "0.435", "green": "0.745", "blue": "0.557", "alpha": "1.000" } }
    }
  ],
  "info": { "author": "xcode", "version": 1 }
}
```

---

## Code Templates

### PersistenceController (Persistence.swift)
```swift
import CoreData

struct PersistenceController {
    static let shared = PersistenceController()

    let container: NSPersistentContainer

    init(inMemory: Bool = false) {
        container = NSPersistentContainer(name: "SmartFarm")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        container.loadPersistentStores { _, error in
            if let error = error { fatalError("CoreData failed: \(error)") }
        }
        container.viewContext.automaticallyMergesChangesFromParent = true
    }
}
```

### FarmViewModel pattern
```swift
class FarmViewModel: ObservableObject {
    let context: NSManagedObjectContext
    @Published var transactions: [Transaction] = []
    @Published var activities: [FarmActivity] = []

    init(context: NSManagedObjectContext) {
        self.context = context
        fetchAll()
    }

    func fetchAll() {
        transactions = (try? context.fetch(Transaction.fetchRequest())) ?? []
        activities = (try? context.fetch(FarmActivity.fetchRequest())) ?? []
    }

    func save() { try? context.save(); fetchAll() }
}
```

### PreviewProvider pattern
```swift
struct MyView_Previews: PreviewProvider {
    static var previews: some View {
        MyView()
            .environment(\.managedObjectContext,
                PersistenceController(inMemory: true).container.viewContext)
    }
}
```

---

## Language Switching

The app must support Khmer ↔ English toggle from Settings, persisted across launches.

### Approach
Use `@AppStorage("appLanguage")` with values `"km"` (default) and `"en"`.
Inject a custom `Locale` into the environment so all `Text` and date formatters respect it.

```swift
// SmartFarmApp.swift — wrap root view
@AppStorage("appLanguage") var appLanguage: String = "km"

var body: some Scene {
    WindowGroup {
        RootView()
            .environment(\.locale, Locale(identifier: appLanguage))
    }
}
```

```swift
// SettingsView.swift — language picker
@AppStorage("appLanguage") private var appLanguage: String = "km"

Picker("ភាសា", selection: $appLanguage) {
    Text("🇰🇭 ខ្មែរ").tag("km")
    Text("🇬🇧 English").tag("en")
}
.pickerStyle(.segmented)
```

### Localizable.strings file structure
Add two localization folders in Xcode (Project → Info → Localizations: Khmer + English).

```
SmartFarm/
├── km.lproj/
│   └── Localizable.strings   ← Khmer strings
└── en.lproj/
    └── Localizable.strings   ← English strings
```

Key naming convention:
```
// km.lproj/Localizable.strings
"tab.dashboard"   = "ផ្ទាំងគ្រប់គ្រង";
"tab.finance"     = "ហិរញ្ញវត្ថុ";
"tab.calendar"    = "ប្រតិទិន";
"tab.settings"    = "ការកំណត់";
"finance.balance" = "សមតុល្យ";
"finance.income"  = "ចំណូល";
"finance.expense" = "ចំណាយ";
// ... (full list in implementation)

// en.lproj/Localizable.strings
"tab.dashboard"   = "Dashboard";
"tab.finance"     = "Finance";
"tab.calendar"    = "Calendar";
"tab.settings"    = "Settings";
"finance.balance" = "Balance";
"finance.income"  = "Income";
"finance.expense" = "Expense";
```

Reference in views:
```swift
Text(NSLocalizedString("finance.balance", comment: ""))
// or with LocalizedStringKey (SwiftUI auto-looks up):
Text("finance.balance")
```

> **Note:** Date formatters in `FarmViewModel` and views that use `fmt.locale = Locale(identifier: "km")` must be updated to read from `@AppStorage("appLanguage")` or receive locale via environment instead of hardcoding `"km"`.

---

## NSManagedObject in SwiftUI ForEach — Value Extraction Rule

**Bug:** Sub-views that receive `NSManagedObject` subclasses (`Transaction`, `FarmActivity`) as props never re-render when the object's properties change. SwiftUI compares struct identity — same pointer = no re-render — even after `fetchAll()` and `dataVersion` increment.

**Rule:** Always extract primitive value types in the parent `ForEach` closure and pass them to sub-views. Never pass a `NSManagedObject` reference to a sub-view that renders its properties.

```swift
// ✅ CORRECT — extract at parent level, sub-view sees changed struct
ForEach(vm.transactions.prefix(3), id: \.id) { tx in
    RecentTransactionRow(
        title: tx.title ?? "",
        category: tx.category ?? "",
        formattedAmount: vm.format(khr: tx.amount),
        isIncome: tx.type == "income"
    )
}

private struct RecentTransactionRow: View {
    let title: String
    let category: String
    let formattedAmount: String
    let isIncome: Bool
    // ...
}

// ❌ WRONG — sub-view receives same NSManagedObject pointer, body never re-evaluated
ForEach(vm.transactions.prefix(3), id: \.id) { tx in
    RecentTransactionRow(tx: tx, format: vm.format)
}
```

---

## dataVersion Counter — Force Dashboard Refresh

When a `List` contains sub-views that receive `NSManagedObject` references, add a `dataVersion` counter to force the `List` to fully rebuild on every `fetchAll()`:

```swift
// FarmViewModel
@Published private(set) var dataVersion: Int = 0

func fetchAll() {
    transactions = (try? context.fetch(txReq)) ?? []
    activities   = (try? context.fetch(actReq)) ?? []
    dataVersion += 1
}
```

```swift
// DashboardView — .id() forces List to discard and recreate all cells
List { ... }
    .id(vm.dataVersion)
```

This ensures computed properties like `currentMonthProfit` (evaluated at the parent level) and extracted primitives all reflect fresh data.

---

## Sheet Management — Single .sheet(item:) with Enum

iOS 14 only honors the **last** `.sheet` modifier on a view. If you attach two `.sheet` modifiers, only the second one fires.

**Rule:** Always use a single `.sheet(item:)` with an `Identifiable` enum that covers all cases.

```swift
private enum FinanceSheet: Identifiable {
    case add
    case edit(Transaction)
    var id: String {
        switch self {
        case .add: return "add"
        case .edit(let tx): return tx.id?.uuidString ?? "edit"
        }
    }
}

// In view:
@State private var activeSheet: FinanceSheet?

.sheet(item: $activeSheet) { sheet in
    switch sheet {
    case .add:         TransactionFormSheet().environmentObject(vm)
    case .edit(let tx): TransactionFormSheet(editingTransaction: tx).environmentObject(vm)
    }
}
```

---

## TabView Language Reactivity

SwiftUI `TabView .tabItem` labels do not react to `@AppStorage` or `@Published` changes. To force the tab bar to re-render when language changes, add `.id(appLanguage)` to the `TabView`:

```swift
@AppStorage("appLanguage") private var appLanguage: String = "km"

TabView(selection: $selectedTab) { ... }
    .id(appLanguage)   // forces full TabView recreation on language change
```

---

## Bar Chart — .position() vs .offset()

**Bug:** `ZStack(alignment: .bottom)` + `.offset(y: -(h - barH) / 2)` appears to bottom-anchor bars but the math cancels out — all bars end up vertically centered regardless of height.

**Rule:** Use `ZStack(alignment: .topLeading)` + `.position(x:y:)` for absolute bar placement. `.position(x:y:)` sets the view's **center** at absolute coordinates within the parent frame.

```swift
ZStack(alignment: .topLeading) {
    ForEach(data.indices, id: \.self) { i in
        let groupX = CGFloat(i) * groupW + groupW * 0.08
        let incH = maxVal > 0 ? CGFloat(data[i].income / maxVal) * h : 0

        Rectangle()
            .fill(Color("PrimaryGreen"))
            .frame(width: barW, height: max(incH, 2))
            .position(x: groupX + barW / 2,
                      y: h - max(incH, 2) / 2)   // ← center at (bottom - halfHeight)
    }
}
.frame(width: w, height: h)
```

---

## Bar Chart Reactivity

**Bug:** `MonthlyChartView` can read a computed property that is not itself `@Published`, so the chart does not redraw when transactions change.

**Rule:** The chart must be driven by a `@Published` array or a `@Published` derived value — never a plain `var` computed from another `@Published`.

```swift
// FarmViewModel.swift — CORRECT pattern
@Published var transactions: [Transaction] = []   // ← changes trigger redraw

// Computed is fine as long as the View observes the ViewModel
// and the computed reads from a @Published var:
var monthlyChartData: [(String, Double, Double)] {
    // derive from self.transactions — no separate @Published needed
    // SwiftUI re-evaluates the body whenever transactions changes
}
```

```swift
// MonthlyChartView.swift — must receive the ViewModel, not a plain array snapshot
struct MonthlyChartView: View {
    @ObservedObject var vm: FarmViewModel   // ← reactive; NOT a let data: [...]

    var body: some View {
        // reads vm.monthlyChartData — redraws on every vm.transactions change
    }
}
```

**Anti-pattern to avoid:**
```swift
// BAD — chart is given a snapshot, never updates
MonthlyChartView(data: vm.monthlyChartData)  // only evaluated once at call-site
```

---

## App Icons

Source: `Mobile/AppIcons/Assets.xcassets/AppIcon.appiconset/` (19 PNG files, sizes 20–1024).

Copy PNGs into `SmartFarm/Assets.xcassets/AppIcon.appiconset/` and update `Contents.json` to map each size to the correct filename (e.g. `"filename": "180.png"` for 60×60@3×). The `Contents.json` structure uses `"idiom"`, `"scale"`, `"size"`, and `"filename"` keys for every image entry.

---

## Custom Fonts

**Fonts used:**
| Font | Script | Weights | PostScript name |
|------|--------|---------|-----------------|
| Hanuman | Khmer | Regular, Bold | `Hanuman`, `Hanuman-Bold` |
| Inter | Latin | Regular, Medium, SemiBold, Bold | `Inter-Regular`, `Inter-Medium`, `Inter-SemiBold`, `Inter-Bold` |

**Files:** `SmartFarm/Fonts/*.ttf` (6 files, registered in Xcode Resources build phase in `project.pbxproj`)

**Registration:** Called once in `SmartFarmApp.init()`:
```swift
init() {
    AppFont.registerAll()
}
```

`AppFont.registerAll()` uses `CTFontManagerRegisterFontsForURL` — no `Info.plist` entry needed:
```swift
// Utilities/AppFont.swift
import CoreText

enum AppFont {
    static func registerAll() {
        let names = ["Hanuman-Regular","Hanuman-Bold","Inter-Regular","Inter-Medium","Inter-SemiBold","Inter-Bold"]
        for name in names {
            if let url = Bundle.main.url(forResource: name, withExtension: "ttf") {
                CTFontManagerRegisterFontsForURL(url as CFURL, .process, nil)
            }
        }
    }

    static func regular(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman", size: size) : .custom("Inter-Regular", size: size)
    }
    static func medium(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman", size: size) : .custom("Inter-Medium", size: size)
    }
    static func semibold(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman-Bold", size: size) : .custom("Inter-SemiBold", size: size)
    }
    static func bold(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman-Bold", size: size) : .custom("Inter-Bold", size: size)
    }
    private static var isKhmer: Bool {
        (UserDefaults.standard.string(forKey: "appLanguage") ?? "km") == "km"
    }
}
```

**Usage in views:**
```swift
Text("SmartFarm").font(AppFont.bold(size: 24))
Text(L10n.t("finance.balance")).font(AppFont.regular(size: 14))
```

**pbxproj entries needed for each font file** (same pattern as Swift files but in Resources phase, not Sources):
```
PBXBuildFile:   XXXXXXXX /* Hanuman-Regular.ttf in Resources */ = {isa = PBXBuildFile; fileRef = YYYYYYYY; };
PBXFileReference: YYYYYYYY /* Hanuman-Regular.ttf */ = {isa = PBXFileReference; lastKnownFileType = file; path = "Hanuman-Regular.ttf"; sourceTree = "<group>"; };
PBXGroup (Fonts): add YYYYYYYY to children
PBXResourcesBuildPhase: add XXXXXXXX to files
```

---

## Current Status

| Sprint | What | Status |
|--------|------|--------|
| 1–3 | CoreData, MVVM, Finance, Calendar, Dashboard, Settings | ✅ |
| 4 | Splash + Onboarding | ✅ |
| 5 | isDone toggle, CalendarTabView checkmark/strikethrough, disabled Save buttons | ✅ |
| 6 | Edit records — TransactionFormSheet + ActivityFormSheet (unified add/edit) | ✅ |
| 7 | Dark mode — PrimaryGreen/ExpenseRed colorsets, all screens adaptive | ✅ |
| 8 | Language switch (KH/EN), full L10n, bar chart fix, splash fix, USD rate | ✅ |
| 9 | App icons (AppIcons/), Hanuman + Inter custom fonts, splash icon updated | ✅ |

**Splash icon:** `Assets.xcassets/SplashLogo.imageset/SplashLogo.png` (1024×1024 from `AppIcons/appstore.png`). Displayed in `SplashView.swift` at 120pt with `RoundedRectangle(cornerRadius: 26)` + drop shadow. No pbxproj change needed — Assets.xcassets is already a folder reference.

**iOS is feature-complete. No known remaining work.**

| Sprint | What | Status |
|--------|------|--------|
| 1–3 | CoreData, MVVM, Finance, Calendar, Dashboard, Settings | ✅ |
| 4 | Splash + Onboarding | ✅ |
| 5 | isDone toggle, checkmark/strikethrough, disabled Save buttons | ✅ |
| 6 | Edit records — unified TransactionFormSheet + ActivityFormSheet | ✅ |
| 7 | Dark mode — PrimaryGreen/ExpenseRed colorsets, all screens adaptive | ✅ |
| 8 | Language switch (KH/EN), full L10n, bar chart fix, splash fix, USD rate | ✅ |
| 9 | App icons, Hanuman + Inter custom fonts, splash icon | ✅ |
| 10 | Dashboard stale data fix, bar chart position fix, sheet enum, TabView.id, showKHR persistence | ✅ |

---

## When building a new module

Generate complete Swift files following all rules above. Always output:
1. Full Swift file content
2. File path relative to `SmartFarm/`
3. Any `.xcdatamodeld` changes needed
4. Any changes needed in `SmartFarmApp.swift`

Check `../PLAN.md` for feature context, `../FIGMA_BRIEF.md` (Part A) for screen design spec, and `../.claude/memory.md` for shared rules (amount in KHR, USD rate **4,000**).
