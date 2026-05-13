import CoreData
import SwiftUI

class FarmViewModel: ObservableObject {

    // MARK: - Published state
    @Published var transactions: [Transaction] = []
    @Published var activities: [FarmActivity] = []
    @Published private(set) var dataVersion: Int = 0
    @Published var showKHR: Bool {
        didSet { UserDefaults.standard.set(showKHR, forKey: "showKHR") }
    }

    let managedContext: NSManagedObjectContext
    private var context: NSManagedObjectContext { managedContext }

    static let USD_RATE: Double = 4000

    // MARK: - Init
    init(context: NSManagedObjectContext) {
        self.managedContext = context
        self.showKHR = UserDefaults.standard.object(forKey: "showKHR") as? Bool ?? true
        fetchAll()
        seedIfNeeded()
    }

    // MARK: - Fetch
    func fetchAll() {
        let txReq: NSFetchRequest<Transaction> = Transaction.fetchRequest()
        txReq.sortDescriptors = [NSSortDescriptor(key: "date", ascending: false)]

        let actReq: NSFetchRequest<FarmActivity> = FarmActivity.fetchRequest()
        actReq.sortDescriptors = [NSSortDescriptor(key: "date", ascending: true)]

        transactions = (try? context.fetch(txReq)) ?? []
        activities = (try? context.fetch(actReq)) ?? []
        dataVersion += 1
    }

    // MARK: - Transaction CRUD
    func addTransaction(title: String, amount: Double, type: String, category: String, note: String, date: Date) {
        let tx = Transaction(context: context)
        tx.id = UUID()
        tx.title = title
        tx.amount = amount
        tx.type = type
        tx.category = category
        tx.note = note
        tx.date = date
        save()
    }

    func deleteTransaction(_ tx: Transaction) {
        context.delete(tx)
        save()
    }

    // MARK: - Activity CRUD
    func addActivity(title: String, type: String, notes: String, date: Date) {
        let act = FarmActivity(context: context)
        act.id = UUID()
        act.title = title
        act.type = type
        act.notes = notes
        act.date = date
        act.isNotified = false
        save()
        NotificationManager.scheduleReminder(for: act)
    }

    func deleteActivity(_ act: FarmActivity) {
        if let id = act.id { NotificationManager.cancelReminder(id: id) }
        context.delete(act)
        save()
    }

    func toggleDone(_ activity: FarmActivity) {
        activity.isDone = !activity.isDone
        save()
    }

    func updateTransaction(_ tx: Transaction, title: String, amount: Double, type: String, category: String, note: String, date: Date) {
        tx.title = title
        tx.amount = amount
        tx.type = type
        tx.category = category
        tx.note = note
        tx.date = date
        save()
    }

    func updateActivity(_ act: FarmActivity, title: String, type: String, notes: String, date: Date) {
        act.title = title
        act.type = type
        act.notes = notes
        act.date = date
        save()
    }

    // MARK: - Computed finance
    var currentMonthIncome: Double {
        let cal = Calendar.current; let now = Date()
        return transactions
            .filter { $0.type == "income" && cal.isDate($0.date ?? .distantPast, equalTo: now, toGranularity: .month) }
            .reduce(0) { $0 + $1.amount }
    }

    var currentMonthExpense: Double {
        let cal = Calendar.current; let now = Date()
        return transactions
            .filter { $0.type == "expense" && cal.isDate($0.date ?? .distantPast, equalTo: now, toGranularity: .month) }
            .reduce(0) { $0 + $1.amount }
    }

    var currentMonthProfit: Double { currentMonthIncome - currentMonthExpense }

    var upcomingActivities: [FarmActivity] {
        let now = Date()
        let week = Calendar.current.date(byAdding: .day, value: 7, to: now)!
        return activities.filter { guard let d = $0.date else { return false }; return !$0.isDone && d >= now && d <= week }
    }

    // MARK: - Monthly chart data (last 6 months)
    struct MonthBar {
        let label: String
        let income: Double
        let expense: Double
        var profit: Double { income - expense }
    }

    var monthlyChartData: [MonthBar] {
        let cal = Calendar.current
        let now = Date()
        let fmt = DateFormatter()
        fmt.dateFormat = "MMM"
        fmt.locale = Locale(identifier: UserDefaults.standard.string(forKey: "appLanguage") ?? "km")
        return (-5...0).map { offset in
            let d = cal.date(byAdding: .month, value: offset, to: now)!
            let inc = transactions
                .filter { $0.type == "income" && cal.isDate($0.date ?? .distantPast, equalTo: d, toGranularity: .month) }
                .reduce(0) { $0 + $1.amount }
            let exp = transactions
                .filter { $0.type == "expense" && cal.isDate($0.date ?? .distantPast, equalTo: d, toGranularity: .month) }
                .reduce(0) { $0 + $1.amount }
            return MonthBar(label: fmt.string(from: d), income: inc, expense: exp)
        }
    }

    // MARK: - Currency formatting
    func format(khr: Double) -> String {
        if showKHR {
            return "\(Int(khr).formattedWithSeparator) ៛"
        } else {
            return String(format: "$%.2f", khr / FarmViewModel.USD_RATE)
        }
    }

    // MARK: - Seed
    private func seedIfNeeded() {
        guard !UserDefaults.standard.bool(forKey: "hasSeededData") else { return }
        UserDefaults.standard.set(true, forKey: "hasSeededData")
        addTransaction(title: "លក់ស្រូវ", amount: 1_200_000, type: "income", category: "Sales", note: "", date: Date())
        addTransaction(title: "ទិញជី", amount: 250_000, type: "expense", category: "Fertilizer", note: "", date: Date())
        addActivity(title: "ស្រោចទឹក", type: "ស្រោចទឹក", notes: "", date: Calendar.current.date(byAdding: .day, value: 1, to: Date())!)
        addActivity(title: "ដាក់ជី", type: "ដាក់ជី", notes: "", date: Calendar.current.date(byAdding: .day, value: 3, to: Date())!)
    }

    // MARK: - Public refresh (called by views on appear)
    func refresh() { fetchAll() }

    // MARK: - Save
    private func save() {
        guard context.hasChanges else { return }
        try? context.save()
        fetchAll()
    }
}

private extension Int {
    var formattedWithSeparator: String {
        let f = NumberFormatter()
        f.numberStyle = .decimal
        return f.string(from: NSNumber(value: self)) ?? "\(self)"
    }
}
