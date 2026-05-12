# SmartFarm iOS App (កសិកម្ម ឆ្លាតវៃ)

កម្មវិធី SwiftUI MVVM សម្រាប់កសិករខ្នាតតូចនៅកម្ពុជា — តាមដានហិរញ្ញវត្ថុ សកម្មភាពកសិកម្ម និងកាលវិភាគក្នុងកន្លែងតែមួយ។

---

## គោលបំណង និងអត្ថប្រយោជន៍

### បញ្ហាដែលកសិករកម្ពុជាប្រឈមមុខ

- **គ្មាន records ច្បាស់លាស់** — ចំណូល និងចំណាយត្រូវបានកត់ត្រានៅលើក្រដាស ឬចងចាំក្នុងក្បាល ដែលងាយបាត់បង់ និងមិនត្រឹមត្រូវ
- **មិនដឹង profit/loss ពិតប្រាកដ** — ពួកគេដាំដុះដោយមិនដឹងថាតើខែនេះ ឬរដូវកាលនេះចំណេញ ឬខាត
- **ភ្លេចកាលបរិច្ឆេទសំខាន់ៗ** — ការព្រងើយកន្តើយនឹងពេលវេលាបន្លិចទឹក បាច់ជី ឬច្រូតកាត់ ធ្វើឱ្យខូចទិន្នផល
- **គ្មានទិន្នន័យសម្រាប់ការសម្រេចចិត្ត** — ពួកគេសម្រេចចិត្តដោយផ្អែកលើអារម្មណ៍ មិនមែនផ្អែកលើ data ពិតប្រាកដ

---

## SmartFarm ដោះស្រាយបញ្ហាទាំងនេះ

#### Finance Tracker — ដឹងច្បាស់ថាលុយទៅណា
កសិករបញ្ចូល income (ប្រាក់ចូល) និង expense (ចំណាយ) រៀងរាល់ថ្ងៃ។ App គណនា profit/loss ស្វ័យប្រវត្តិ ហើយបង្ហាញ summary card ដែលងាយមើល។ គាំទ្ររូបិយប័ណ្ណ Riel (KHR) និង Dollar (USD)។

#### Calendar & Reminders — មិនភ្លេចការងារសំខាន់
កសិករកំណត់ schedule សម្រាប់សកម្មភាពកសិកម្ម (ដំណាំ, ស្រោចទឹក, ដាក់ជី, ថែទាំ, ការពារ, ច្រូតកាត់, ប្រមូលផល, ត្រួតពិនិត្យ)។ App ផ្ញើ notification ជូនដំណឹង 1 ថ្ងៃមុន និងនៅថ្ងៃនោះ — ទោះបីជា app បិទក្ដី។

#### Dashboard — រូបភាពទូទៅនៃ farm ទាំងមូល
Home screen បង្ហាញ summary cards ដែលសង្ខេបទិន្នន័យទាំងអស់ — profit/loss ខែនេះ, reminders ខាងមុខ, transactions ចុងក្រោយ។

#### Export & Reports — ចែករំលែក និងបង្ហាញទៅធនាគារ
Generate PDF report ឬ export CSV ដើម្បីចែករំលែកជាមួយ គ្រួសារ, ធនាគារ, microfinance, ឬអ្នកជំនាញកសិកម្ម។

#### Backup & Restore — ទិន្នន័យមិនបាត់
Export ទិន្នន័យទាំងអស់ជា JSON ទៅ iCloud Drive ឬ local storage។ បើ phone ខូច អាច restore ទិន្នន័យឡើងវិញបានភ្លាមៗ។

---

## អត្ថប្រយោជន៍សំខាន់ៗ

| អត្ថប្រយោជន៍ | ការពន្យល់ |
|--------------|-----------|
| **Works Offline** | មិនត្រូវការ internet — ប្រើបានគ្រប់ទីកន្លែង |
| **ភាសាខ្មែរ** | UI ជាភាសាខ្មែរ ងាយយល់សម្រាប់អ្នកប្រើប្រាស់ក្នុងស្រុក |
| **KHR និង USD** | គាំទ្ររូបិយប័ណ្ណទាំងពីរ ស្របតាមការប្រើប្រាស់ជាក់ស្ដែង |
| **Simple UI** | រចនាឡើងសម្រាប់អ្នកដែលមិនជំនាញបច្ចេកវិទ្យា |
| **Data Safety** | Backup ស្វ័យប្រវត្តិ ទិន្នន័យមិនបាត់ |
| **Free & Private** | គ្មាន subscription, ទិន្នន័យនៅក្នុង device តែប៉ុណ្ណោះ |

---

## តម្រូវការ (Requirements)

| ធាតុ | តម្រូវការ |
|------|-----------|
| **macOS** | 11.0+ |
| **Xcode** | 13.x+ |
| **Swift** | 5.5 |
| **iOS Deployment Target** | 14.0 |
| **SwiftUI** | iOS 14+ APIs only |
| **Dependencies** | None — zero CocoaPods / SPM packages |

### APIs ដែលត្រូវប្រើ (Xcode 13 safe)
- `NavigationView` + `NavigationLink` — **មិន**ប្រើ `NavigationStack` (iOS 16+)
- `TabView` ជាមួយ `tabItem`
- `@StateObject`, `@ObservedObject`, `@EnvironmentObject`, `@Published`
- `@FetchRequest` សម្រាប់ CoreData
- `UNUserNotificationCenter` សម្រាប់ local notifications
- `UIActivityViewController` បង្កប់ក្នុង `UIViewControllerRepresentable`
- `UIDocumentPickerViewController` បង្កប់ក្នុង `UIViewControllerRepresentable`
- `PDFKit` សម្រាប់បង្កើត PDF
- `GeometryReader` + `Rectangle` shapes សម្រាប់ charts
- `PreviewProvider` — **មិន**ប្រើ `#Preview {}` macro (Xcode 15+)

### APIs ដែលត្រូវជៀសវាង
- `NavigationStack`, `NavigationSplitView` — iOS 16+
- `.searchable()` — ប្រើ custom `TextField` ជំនួស
- `Charts` (Swift Charts) — iOS 16+
- `SwiftData`, `@Model` — iOS 17+
- `ShareLink` — iOS 16+
- `@Observable` — iOS 17+
- `#Preview {}` — Xcode 15+

---

## Developer

| ឈ្មោះ | តួនាទី | ការទទួលខុសត្រូវ |
|-------|--------|----------------|
| **nemsothea** | Senior iOS & Android Developer | គ្រប់ phases — solo |

---

## ផែនការសាងសង់

> ព័ត៌មានលម្អិត: `../.claude/plan.md`  
> Feature matrix: `../PLAN.md`

| Phase | គោលដៅ |
|-------|--------|
| **1** | CoreData schema + folder structure + `MainTabView` |
| **2a** | Finance Tracker — CRUD, summary card, KHR/USD |
| **2b** | Calendar & Reminders — month grid, notifications |
| **3** | Dashboard — live data from Finance + Calendar |
| **4** | Design system, dark mode, animations |
| **5** | Export & Reports — chart, CSV, PDF |
| **6** | Backup & Restore — JSON, iCloud Drive |

---

## Modules

| Module | មុខងារ |
|--------|--------|
| **Finance Tracker** | Income/expense tracking, profit reports, categories |
| **Calendar & Reminders** | Activity scheduling (8 types), local notifications |
| **Dashboard** | Unified view of all farm data |
| **Reports & Charts** | Visual profit/loss bar chart |
| **Backup & Restore** | JSON export/import, iCloud Drive |
