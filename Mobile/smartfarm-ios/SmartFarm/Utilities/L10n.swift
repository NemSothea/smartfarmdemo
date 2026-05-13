import Foundation

struct L10n {
    static var language: String {
        UserDefaults.standard.string(forKey: "appLanguage") ?? "km"
    }

    static func t(_ key: String) -> String {
        (strings[language] ?? strings["km"]!)[key] ?? key
    }

    private static let strings: [String: [String: String]] = [
        "km": [
            // Tabs
            "tab.dashboard": "ផ្ទាំងគ្រប់គ្រង",
            "tab.finance":   "ហិរញ្ញវត្ថុ",
            "tab.calendar":  "ប្រតិទិន",
            "tab.settings":  "ការកំណត់",
            // Finance
            "finance.title":   "ហិរញ្ញវត្ថុ",
            "finance.all":     "ទាំងអស់",
            "finance.balance": "សមតុល្យ",
            "finance.income":  "ចំណូល",
            "finance.expense": "ចំណាយ",
            "finance.search":  "ស្វែងរក...",
            "finance.empty":   "មិនមានប្រតិបត្តិការ",
            "finance.chart":   "ក្រាហ្វប្រចាំខែ (៦ ខែចុងក្រោយ)",
            "finance.add":     "បន្ថែមប្រតិបត្តិការ",
            "finance.edit":    "កែប្រែ",
            // Form
            "form.title":    "ចំណងជើង",
            "form.amount":   "ចំនួនទឹកប្រាក់ (KHR)",
            "form.type":     "ប្រភេទ",
            "form.category": "ក្រុម",
            "form.date":     "កាលបរិច្ឆេទ",
            "form.note":     "កំណត់ចំណាំ",
            "form.save":     "រក្សាទុក",
            "form.cancel":   "បោះបង់",
            // Calendar
            "calendar.title":   "ប្រតិទិន",
            "calendar.search":  "ស្វែងរក...",
            "calendar.empty":   "គ្មានសកម្មភាពថ្ងៃនេះ",
            "calendar.add":     "បន្ថែមសកម្មភាព",
            "calendar.edit":    "កែប្រែ",
            // Dashboard
            "dashboard.title":    "ផ្ទាំងគ្រប់គ្រង",
            "dashboard.upcoming": "សកម្មភាពខាងមុខ",
            "dashboard.recent":   "ប្រតិបត្តិការថ្មីៗ",
            // Settings
            "settings.title":       "ការកំណត់",
            "settings.appearance":  "ចំណូលចិត្ត",
            "settings.theme":       "រចនាបថ",
            "settings.language":    "ភាសា",
            "settings.stats":       "ស្ថិតិ",
            "settings.txCount":     "ប្រតិបត្តិការ",
            "settings.actCount":    "សកម្មភាព",
            "settings.export":      "នាំចេញ",
            "settings.exportCSV":   "នាំចេញជា CSV",
            "settings.exportPDF":   "នាំចេញ PDF",
            "settings.backup":      "ការបម្រុងទុក",
            "settings.backupJSON":  "បម្រុងទុក JSON",
            "settings.restore":     "ស្ដារពីឯកសារ",
            "settings.clearAll":    "លុបទិន្នន័យទាំងអស់",
            "settings.version":     "SmartFarm · កំណែ 1.0.0",
            "settings.light":       "☀️ ភ្លឺ",
            "settings.dark":        "🌙 ងងឹត",
            "settings.langKH":      "🇰🇭 ខ្មែរ",
            "settings.langEN":      "🇬🇧 English",
            "settings.showKHR":     "បង្ហាញជា KHR",
            // Alerts
            "alert.restoreTitle":   "ស្ដារការបម្រុងទុក?",
            "alert.restoreBody":    "ទិន្នន័យបច្ចុប្បន្នទាំងអស់នឹងត្រូវជំនួស។ តើអ្នកប្រាកដទេ?",
            "alert.restoreBtn":     "ស្ដារ",
            "alert.clearTitle":     "លុបទិន្នន័យទាំងអស់?",
            "alert.clearBody":      "ប្រតិបត្តិការ និងសកម្មភាពទាំងអស់នឹងត្រូវលុប។ មិនអាចទាញយកមកវិញបានទេ។",
            "alert.clearBtn":       "លុប",
            "alert.ok":             "យល់ព្រម",
        ],
        "en": [
            // Tabs
            "tab.dashboard": "Dashboard",
            "tab.finance":   "Finance",
            "tab.calendar":  "Calendar",
            "tab.settings":  "Settings",
            // Finance
            "finance.title":   "Finance",
            "finance.all":     "All",
            "finance.balance": "Balance",
            "finance.income":  "Income",
            "finance.expense": "Expense",
            "finance.search":  "Search...",
            "finance.empty":   "No transactions",
            "finance.chart":   "Monthly Chart (last 6 months)",
            "finance.add":     "Add Transaction",
            "finance.edit":    "Edit",
            // Form
            "form.title":    "Title",
            "form.amount":   "Amount (KHR)",
            "form.type":     "Type",
            "form.category": "Category",
            "form.date":     "Date",
            "form.note":     "Note",
            "form.save":     "Save",
            "form.cancel":   "Cancel",
            // Calendar
            "calendar.title":   "Calendar",
            "calendar.search":  "Search...",
            "calendar.empty":   "No activities today",
            "calendar.add":     "Add Activity",
            "calendar.edit":    "Edit",
            // Dashboard
            "dashboard.title":    "Dashboard",
            "dashboard.upcoming": "Upcoming Activities",
            "dashboard.recent":   "Recent Transactions",
            // Settings
            "settings.title":       "Settings",
            "settings.appearance":  "Preferences",
            "settings.theme":       "Theme",
            "settings.language":    "Language",
            "settings.stats":       "Statistics",
            "settings.txCount":     "Transactions",
            "settings.actCount":    "Activities",
            "settings.export":      "Export",
            "settings.exportCSV":   "Export CSV",
            "settings.exportPDF":   "Export PDF",
            "settings.backup":      "Backup",
            "settings.backupJSON":  "Backup JSON",
            "settings.restore":     "Restore from File",
            "settings.clearAll":    "Clear All Data",
            "settings.version":     "SmartFarm · Version 1.0.0",
            "settings.light":       "☀️ Light",
            "settings.dark":        "🌙 Dark",
            "settings.langKH":      "🇰🇭 Khmer",
            "settings.langEN":      "🇬🇧 English",
            "settings.showKHR":     "Show as KHR",
            // Alerts
            "alert.restoreTitle":   "Restore Backup?",
            "alert.restoreBody":    "All current data will be replaced. Are you sure?",
            "alert.restoreBtn":     "Restore",
            "alert.clearTitle":     "Clear All Data?",
            "alert.clearBody":      "All transactions and activities will be deleted. This cannot be undone.",
            "alert.clearBtn":       "Delete",
            "alert.ok":             "OK",
        ]
    ]
}
