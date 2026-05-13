import SwiftUI

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

struct FinanceListView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @State private var activeSheet: FinanceSheet? = nil
    @State private var filterType = "all"
    @State private var searchText = ""

    private var filters: [String: String] {
        ["all": L10n.t("finance.all"), "income": L10n.t("finance.income"), "expense": L10n.t("finance.expense")]
    }

    var filtered: [Transaction] {
        let q = searchText.trimmingCharacters(in: .whitespaces).lowercased()
        return vm.transactions.filter { tx in
            let matchType = filterType == "all" || tx.type == filterType
            let matchSearch = q.isEmpty ||
                (tx.title ?? "").lowercased().contains(q) ||
                (tx.category ?? "").lowercased().contains(q) ||
                (tx.note ?? "").lowercased().contains(q)
            return matchType && matchSearch
        }
    }

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Balance hero card
                VStack(spacing: 0) {
                    HStack(alignment: .top) {
                        VStack(alignment: .leading, spacing: 4) {
                            Text(L10n.t("finance.balance")).font(AppFont.caption()).foregroundColor(.secondary)
                            Text(vm.format(khr: vm.currentMonthProfit))
                                .font(AppFont.bold(size: 28))
                                .foregroundColor(vm.currentMonthProfit >= 0 ? Color("PrimaryGreen") : Color("ExpenseRed"))
                        }
                        Spacer()
                        Picker("", selection: $vm.showKHR) {
                            Text("KHR ៛").tag(true)
                            Text("USD $").tag(false)
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .frame(width: 130)
                    }
                    .padding(.horizontal).padding(.top, 14)

                    Divider().padding(.horizontal).padding(.top, 10)

                    HStack(spacing: 0) {
                        summaryItem(label: L10n.t("finance.income"), value: vm.currentMonthIncome, color: Color("PrimaryGreen"), dot: true)
                        Divider().frame(height: 28)
                        summaryItem(label: L10n.t("finance.expense"), value: vm.currentMonthExpense, color: Color("ExpenseRed"), dot: true)
                    }
                    .padding(.vertical, 10)
                }
                .background(Color(.systemBackground))

                // Search bar
                HStack {
                    Image(systemName: "magnifyingglass").foregroundColor(.secondary)
                    TextField(L10n.t("finance.search"), text: $searchText)
                    if !searchText.isEmpty {
                        Button(action: { searchText = "" }) {
                            Image(systemName: "xmark.circle.fill").foregroundColor(.secondary)
                        }
                    }
                }
                .padding(10)
                .background(Color(.systemGray6))
                .cornerRadius(10)
                .padding(.horizontal)
                .padding(.top, 8)

                // Monthly bar chart
                MonthlyChartView()
                    .environmentObject(vm)
                    .padding(.vertical, 4)

                // Filter chips
                HStack(spacing: 8) {
                    ForEach(["all", "income", "expense"], id: \.self) { key in
                        Button(filters[key]!) { filterType = key }
                            .font(filterType == key ? AppFont.semibold(size: 12) : AppFont.caption())
                            .padding(.horizontal, 14)
                            .padding(.vertical, 7)
                            .background(filterType == key ? Color("PrimaryGreen") : Color(.systemGray5))
                            .foregroundColor(filterType == key ? .white : .primary)
                            .cornerRadius(16)
                    }
                    Spacer()
                }
                .padding(.horizontal)
                .padding(.vertical, 8)
                .background(Color(.systemGroupedBackground))

                // List
                if filtered.isEmpty {
                    Spacer()
                    Text(L10n.t("finance.empty")).foregroundColor(.secondary)
                    Spacer()
                } else {
                    List {
                        ForEach(filtered, id: \.id) { tx in
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(tx.title ?? "").font(AppFont.subheadline())
                                    Text("\(tx.category ?? "")  ·  \(tx.date.map { dateStr($0) } ?? "")")
                                        .font(AppFont.caption()).foregroundColor(.secondary)
                                }
                                Spacer()
                                Text(vm.format(khr: tx.amount))
                                    .fontWeight(.medium)
                                    .foregroundColor(tx.type == "income" ? Color("PrimaryGreen") : Color("ExpenseRed"))
                            }
                            .contentShape(Rectangle())
                            .onTapGesture { activeSheet = .edit(tx) }
                        }
                        .onDelete { indices in
                            indices.forEach { vm.deleteTransaction(filtered[$0]) }
                        }
                    }
                    .listStyle(PlainListStyle())
                }
            }
            .navigationTitle(L10n.t("finance.title"))
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { activeSheet = .add }) { Image(systemName: "plus") }
                }
            }
            .sheet(item: $activeSheet) { sheet in
                switch sheet {
                case .add:
                    TransactionFormSheet().environmentObject(vm)
                case .edit(let tx):
                    TransactionFormSheet(editingTransaction: tx).environmentObject(vm)
                }
            }
        }
    }

    private func summaryItem(label: String, value: Double, color: Color, dot: Bool = false) -> some View {
        VStack(spacing: 4) {
            HStack(spacing: 5) {
                if dot { Circle().fill(color).frame(width: 8, height: 8) }
                Text(label).font(AppFont.caption()).foregroundColor(.secondary)
            }
            Text(vm.format(khr: value)).font(AppFont.semibold(size: 15)).foregroundColor(color)
        }
        .frame(maxWidth: .infinity)
    }

    private func dateStr(_ date: Date) -> String {
        let fmt = DateFormatter()
        fmt.dateStyle = .short
        return fmt.string(from: date)
    }
}

// MARK: - Unified Add / Edit Sheet

private struct TransactionFormSheet: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @Environment(\.dismiss) private var dismiss
    var editingTransaction: Transaction?

    @State private var title: String
    @State private var amountText: String
    @State private var type: String
    @State private var category: String
    @State private var note: String
    @State private var date: Date

    private let categories = ["ជី", "ពូជ", "ការងារ", "ឧបករណ៍", "លក់"]

    init(editingTransaction: Transaction? = nil) {
        self.editingTransaction = editingTransaction
        let tx = editingTransaction
        _title      = State(initialValue: tx?.title ?? "")
        _amountText = State(initialValue: tx.map { String(format: "%.0f", $0.amount) } ?? "")
        _type       = State(initialValue: tx?.type ?? "income")
        _category   = State(initialValue: tx?.category ?? "Sales")
        _note       = State(initialValue: tx?.note ?? "")
        _date       = State(initialValue: tx?.date ?? Date())
    }

    private var canSave: Bool {
        !title.trimmingCharacters(in: .whitespaces).isEmpty && (Double(amountText) ?? 0) > 0
    }

    var body: some View {
        NavigationView {
            Form {
                Section {
                    TextField(L10n.t("form.title"), text: $title)
                    TextField(L10n.t("form.amount"), text: $amountText)
                        .keyboardType(.decimalPad)
                    Picker(L10n.t("form.type"), selection: $type) {
                        Text(L10n.t("finance.income")).tag("income")
                        Text(L10n.t("finance.expense")).tag("expense")
                    }
                    Picker(L10n.t("form.category"), selection: $category) {
                        ForEach(categories, id: \.self) { Text($0).tag($0) }
                    }
                    DatePicker(L10n.t("form.date"), selection: $date, displayedComponents: .date)
                    TextField(L10n.t("form.note"), text: $note)
                }
            }
            .navigationTitle(editingTransaction == nil ? L10n.t("finance.add") : L10n.t("finance.edit"))
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(L10n.t("form.cancel")) { dismiss() }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(L10n.t("form.save")) {
                        guard canSave, let amount = Double(amountText) else { return }
                        let t = title.trimmingCharacters(in: .whitespaces)
                        if let tx = editingTransaction {
                            vm.updateTransaction(tx, title: t, amount: amount,
                                                 type: type, category: category, note: note, date: date)
                        } else {
                            vm.addTransaction(title: t, amount: amount,
                                              type: type, category: category, note: note, date: date)
                        }
                        dismiss()
                    }
                    .disabled(!canSave)
                }
            }
        }
        .navigationViewStyle(.stack)
    }
}

struct FinanceListView_Previews: PreviewProvider {
    static var previews: some View {
        FinanceListView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}

