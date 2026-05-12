import SwiftUI

struct FinanceListView: View {
    @EnvironmentObject var vm: FarmViewModel
    @State private var showAdd = false
    @State private var filterType = "all"
    @State private var editingTransaction: Transaction? = nil
    @State private var showEditSheet = false

    private let filters = ["all": "ទាំងអស់", "income": "ចំណូល", "expense": "ចំណាយ"]

    var filtered: [Transaction] {
        if filterType == "all" { return vm.transactions }
        return vm.transactions.filter { $0.type == filterType }
    }

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Summary card
                HStack(spacing: 24) {
                    summaryItem(label: "ចំណូល", value: vm.currentMonthIncome, color: Color("PrimaryGreen"))
                    Divider().frame(height: 36)
                    summaryItem(label: "ចំណាយ", value: vm.currentMonthExpense, color: Color("ExpenseRed"))
                    Divider().frame(height: 36)
                    summaryItem(label: "ចំណេញ", value: vm.currentMonthProfit,
                                color: vm.currentMonthProfit >= 0 ? Color("PrimaryGreen") : Color("ExpenseRed"))
                }
                .padding()
                .background(Color(.systemBackground))

                // Monthly bar chart
                MonthlyChartView(data: vm.monthlyChartData, format: vm.format)
                    .padding(.vertical, 4)

                // Currency + filter row
                HStack {
                    Picker("", selection: $vm.showKHR) {
                        Text("KHR ៛").tag(true)
                        Text("USD $").tag(false)
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    .frame(width: 150)
                    Spacer()
                    ForEach(["all", "income", "expense"], id: \.self) { key in
                        Button(filters[key]!) { filterType = key }
                            .font(.caption)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 5)
                            .background(filterType == key ? Color("PrimaryGreen") : Color(.systemGray5))
                            .foregroundColor(filterType == key ? .white : .primary)
                            .cornerRadius(12)
                    }
                }
                .padding(.horizontal)
                .padding(.vertical, 8)
                .background(Color(.systemGroupedBackground))

                // List
                if filtered.isEmpty {
                    Spacer()
                    Text("មិនមានប្រតិបត្តិការ").foregroundColor(.secondary)
                    Spacer()
                } else {
                    List {
                        ForEach(filtered, id: \.id) { tx in
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(tx.title ?? "").font(.subheadline)
                                    Text("\(tx.category ?? "")  ·  \(tx.date.map { dateStr($0) } ?? "")")
                                        .font(.caption).foregroundColor(.secondary)
                                }
                                Spacer()
                                Text(vm.format(khr: tx.amount))
                                    .fontWeight(.medium)
                                    .foregroundColor(tx.type == "income" ? Color("PrimaryGreen") : Color("ExpenseRed"))
                            }
                            .contentShape(Rectangle())
                            .onTapGesture {
                                editingTransaction = tx
                                showEditSheet = true
                            }
                        }
                        .onDelete { indices in
                            indices.forEach { vm.deleteTransaction(filtered[$0]) }
                        }
                    }
                    .listStyle(PlainListStyle())
                }
            }
            .navigationTitle("ហិរញ្ញវត្ថុ")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showAdd = true }) { Image(systemName: "plus") }
                }
            }
            .sheet(isPresented: $showAdd) {
                TransactionFormSheet(isPresented: $showAdd)
                    .environmentObject(vm)
            }
            .sheet(isPresented: $showEditSheet, onDismiss: { editingTransaction = nil }) {
                if let tx = editingTransaction {
                    TransactionFormSheet(isPresented: $showEditSheet, editingTransaction: tx)
                        .environmentObject(vm)
                }
            }
        }
    }

    private func summaryItem(label: String, value: Double, color: Color) -> some View {
        VStack(spacing: 2) {
            Text(label).font(.caption).foregroundColor(.secondary)
            Text(vm.format(khr: value)).font(.subheadline).fontWeight(.semibold).foregroundColor(color)
        }
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
    @Binding var isPresented: Bool
    var editingTransaction: Transaction?

    @State private var title: String
    @State private var amountText: String
    @State private var type: String
    @State private var category: String
    @State private var note: String
    @State private var date: Date

    private let categories = ["Seeds", "Fertilizer", "Labor", "Tools", "Sales"]

    init(isPresented: Binding<Bool>, editingTransaction: Transaction? = nil) {
        _isPresented = isPresented
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
                    TextField("ចំណងជើង", text: $title)
                    TextField("ចំនួនទឹកប្រាក់ (KHR)", text: $amountText)
                        .keyboardType(.decimalPad)
                    Picker("ប្រភេទ", selection: $type) {
                        Text("ចំណូល").tag("income")
                        Text("ចំណាយ").tag("expense")
                    }
                    Picker("ក្រុម", selection: $category) {
                        ForEach(categories, id: \.self) { Text($0).tag($0) }
                    }
                    DatePicker("កាលបរិច្ឆេទ", selection: $date, displayedComponents: .date)
                    TextField("កំណត់ចំណាំ", text: $note)
                }
            }
            .navigationTitle(editingTransaction == nil ? "បន្ថែមប្រតិបត្តិការ" : "កែប្រែ")
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("បោះបង់") { isPresented = false }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("រក្សាទុក") {
                        guard canSave, let amount = Double(amountText) else { return }
                        let t = title.trimmingCharacters(in: .whitespaces)
                        if let tx = editingTransaction {
                            vm.updateTransaction(tx, title: t, amount: amount,
                                                 type: type, category: category, note: note, date: date)
                        } else {
                            vm.addTransaction(title: t, amount: amount,
                                              type: type, category: category, note: note, date: date)
                        }
                        isPresented = false
                    }
                    .disabled(!canSave)
                }
            }
        }
    }
}

struct FinanceListView_Previews: PreviewProvider {
    static var previews: some View {
        FinanceListView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
