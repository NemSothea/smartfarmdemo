import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var vm: FarmViewModel
    @State private var showExport = false
    @State private var showDocPicker = false
    @State private var showRestoreConfirm = false
    @State private var showResultAlert = false
    @State private var resultMessage = ""
    @State private var exportItems: [Any] = []
    @State private var pendingRestoreData: Data?

    var body: some View {
        NavigationView {
            List {
                // Finance export
                Section("ជំរើស​ ហិរញ្ញវត្ថុ") {
                    Button { prepareCSV() } label: {
                        Label("ส่งออកជា CSV", systemImage: "tablecells")
                    }
                    Button { preparePDF() } label: {
                        Label("ស្នើ​ PDF Report", systemImage: "doc.richtext")
                    }
                }

                // Backup / Restore
                Section("ការបម្រុងទុក") {
                    Button { prepareBackup() } label: {
                        Label("Export JSON Backup", systemImage: "square.and.arrow.up")
                    }
                    Button { showDocPicker = true } label: {
                        Label("Restore from Backup", systemImage: "square.and.arrow.down")
                    }
                }

                // App stats
                Section("ស្ថិតិ") {
                    HStack {
                        Text("ប្រតិបត្តិការ")
                        Spacer()
                        Text("\(vm.transactions.count)").foregroundColor(.secondary)
                    }
                    HStack {
                        Text("សកម្មភាព")
                        Spacer()
                        Text("\(vm.activities.count)").foregroundColor(.secondary)
                    }
                    Toggle("បង្ហាញជា KHR", isOn: $vm.showKHR)
                }
            }
            .navigationTitle("ការកំណត់")
            // Export sheet
            .sheet(isPresented: $showExport) {
                ActivityView(items: exportItems)
            }
            // Document picker for restore
            .sheet(isPresented: $showDocPicker) {
                DocumentPickerView { data in
                    pendingRestoreData = data
                    showRestoreConfirm = true
                }
            }
            // Restore confirmation
            .alert("Restore Backup?", isPresented: $showRestoreConfirm) {
                Button("Restore", role: .destructive) {
                    if let data = pendingRestoreData { performRestore(data) }
                }
                Button("Cancel", role: .cancel) {}
            } message: {
                Text("ទិន្នន័យបច្ចុប្បន្នទាំងអស់នឹងត្រូវជំនួស។ តើអ្នកប្រាកដទេ?")
            }
            // Result alert
            .alert(resultMessage, isPresented: $showResultAlert) {
                Button("OK") {}
            }
        }
    }

    // MARK: - Actions

    private func prepareCSV() {
        let csv = ExportManager.csvString(from: vm.transactions)
        let url = FileManager.default.temporaryDirectory.appendingPathComponent("smartfarm_finance.csv")
        try? csv.data(using: .utf8)?.write(to: url)
        exportItems = [url]
        showExport = true
    }

    private func preparePDF() {
        let data = ExportManager.pdfData(
            transactions: vm.transactions,
            income: vm.currentMonthIncome,
            expense: vm.currentMonthExpense,
            profit: vm.currentMonthProfit
        )
        let url = FileManager.default.temporaryDirectory.appendingPathComponent("smartfarm_report.pdf")
        try? data.write(to: url)
        exportItems = [url]
        showExport = true
    }

    private func prepareBackup() {
        guard let data = try? BackupManager.exportJSON(
            transactions: vm.transactions,
            activities: vm.activities
        ) else { return }
        let url = FileManager.default.temporaryDirectory.appendingPathComponent("smartfarm_backup.json")
        try? data.write(to: url)
        exportItems = [url]
        showExport = true
    }

    private func performRestore(_ data: Data) {
        do {
            try BackupManager.importJSON(data, context: vm.managedContext)
            vm.fetchAll()
            resultMessage = "ស្ដារទិន្នន័យបានជោគជ័យ!"
        } catch {
            resultMessage = "មានបញ្ហា: \(error.localizedDescription)"
        }
        showResultAlert = true
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
