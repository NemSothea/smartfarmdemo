import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("isDarkMode") private var isDarkMode = false
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @State private var showExport = false
    @State private var showDocPicker = false
    @State private var showRestoreConfirm = false
    @State private var showClearConfirm = false
    @State private var showResultAlert = false
    @State private var resultMessage = ""
    @State private var exportItems: [Any] = []
    @State private var pendingRestoreData: Data?

    var body: some View {
        NavigationView {
            List {
                // Appearance
                Section(L10n.t("settings.appearance")) {
                    HStack {
                        Text(L10n.t("settings.theme"))
                        Spacer()
                        Picker("", selection: $isDarkMode) {
                            Text(L10n.t("settings.light")).tag(false)
                            Text(L10n.t("settings.dark")).tag(true)
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .frame(width: 160)
                    }
                    HStack {
                        Text(L10n.t("settings.language"))
                        Spacer()
                        Picker("", selection: $appLanguage) {
                            Text(L10n.t("settings.langKH")).tag("km")
                            Text(L10n.t("settings.langEN")).tag("en")
                        }
                        .pickerStyle(SegmentedPickerStyle())
                        .frame(width: 160)
                    }
                }

                // Stats
                Section(L10n.t("settings.stats")) {
                    HStack {
                        Text(L10n.t("settings.txCount"))
                        Spacer()
                        Text("\(vm.transactions.count)").foregroundColor(.secondary)
                    }
                    HStack {
                        Text(L10n.t("settings.actCount"))
                        Spacer()
                        Text("\(vm.activities.count)").foregroundColor(.secondary)
                    }
                    Toggle(L10n.t("settings.showKHR"), isOn: $vm.showKHR)
                }

                // Export
                Section(L10n.t("settings.export")) {
                    Button { prepareCSV() } label: {
                        Label(L10n.t("settings.exportCSV"), systemImage: "tablecells")
                    }
                    Button { preparePDF() } label: {
                        Label(L10n.t("settings.exportPDF"), systemImage: "doc.richtext")
                    }
                }

                // Backup
                Section(L10n.t("settings.backup")) {
                    Button { prepareBackup() } label: {
                        Label(L10n.t("settings.backupJSON"), systemImage: "arrow.down.circle")
                    }
                    Button { showDocPicker = true } label: {
                        Label(L10n.t("settings.restore"), systemImage: "arrow.up.circle")
                    }
                }

                // Danger
                Section {
                    Button(role: .destructive) {
                        showClearConfirm = true
                    } label: {
                        HStack {
                            Spacer()
                            Text(L10n.t("settings.clearAll"))
                            Spacer()
                        }
                    }
                }

                // Version footer
                Section {
                    HStack {
                        Spacer()
                        Text(L10n.t("settings.version"))
                            .font(AppFont.caption())
                            .foregroundColor(.secondary)
                        Spacer()
                    }
                }
            }
            .navigationTitle(L10n.t("settings.title"))
            .sheet(isPresented: $showExport) {
                ActivityView(items: exportItems)
            }
            .sheet(isPresented: $showDocPicker) {
                DocumentPickerView { data in
                    pendingRestoreData = data
                    showRestoreConfirm = true
                }
            }
            .alert(L10n.t("alert.restoreTitle"), isPresented: $showRestoreConfirm) {
                Button(L10n.t("alert.restoreBtn"), role: .destructive) {
                    if let data = pendingRestoreData { performRestore(data) }
                }
                Button(L10n.t("form.cancel"), role: .cancel) {}
            } message: {
                Text(L10n.t("alert.restoreBody"))
            }
            .alert(L10n.t("alert.clearTitle"), isPresented: $showClearConfirm) {
                Button(L10n.t("alert.clearBtn"), role: .destructive) { performClearAll() }
                Button(L10n.t("form.cancel"), role: .cancel) {}
            } message: {
                Text(L10n.t("alert.clearBody"))
            }
            .alert(resultMessage, isPresented: $showResultAlert) {
                Button(L10n.t("alert.ok")) {}
            }
        }
        .preferredColorScheme(isDarkMode ? .dark : .light)
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

    private func performClearAll() {
        vm.transactions.forEach { vm.deleteTransaction($0) }
        vm.activities.forEach { vm.deleteActivity($0) }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
