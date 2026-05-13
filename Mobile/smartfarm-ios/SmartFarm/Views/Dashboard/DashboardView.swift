import SwiftUI

struct DashboardView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"

    private var monthName: String {
        let fmt = DateFormatter()
        fmt.dateFormat = "MMMM yyyy"
        fmt.locale = Locale(identifier: appLanguage)
        return "ខែ \(fmt.string(from: Date()))"
    }

    var body: some View {
        NavigationView {
            List {
                // Monthly balance hero
                Section {
                    VStack(spacing: 0) {
                        VStack(spacing: 4) {
                            Text(monthName)
                                .font(AppFont.subheadline())
                                .foregroundColor(.secondary)
                            Text(vm.format(khr: vm.currentMonthProfit))
                                .font(AppFont.bold(size: 32))
                                .foregroundColor(vm.currentMonthProfit >= 0 ? Color("PrimaryGreen") : Color("ExpenseRed"))
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 16)

                        Divider()

                        HStack(spacing: 0) {
                            SummaryHalf(
                                dot: Color("PrimaryGreen"),
                                label: L10n.t("finance.income"),
                                value: vm.format(khr: vm.currentMonthIncome),
                                color: Color("PrimaryGreen")
                            )
                            Divider().frame(height: 32)
                            SummaryHalf(
                                dot: Color("ExpenseRed"),
                                label: L10n.t("finance.expense"),
                                value: vm.format(khr: vm.currentMonthExpense),
                                color: Color("ExpenseRed")
                            )
                        }
                        .padding(.vertical, 12)
                    }
                }

                // Upcoming activities
                if !vm.upcomingActivities.isEmpty {
                    Section(L10n.t("dashboard.upcoming")) {
                        ForEach(vm.upcomingActivities.prefix(3), id: \.id) { act in
                            UpcomingActivityRow(
                                type: act.type ?? "",
                                title: act.title ?? "",
                                date: act.date
                            )
                        }
                    }
                }

                // Recent transactions
                if !vm.transactions.isEmpty {
                    Section(L10n.t("dashboard.recent")) {
                        ForEach(vm.transactions.prefix(3), id: \.id) { tx in
                            RecentTransactionRow(
                                title: tx.title ?? "",
                                category: tx.category ?? "",
                                formattedAmount: vm.format(khr: tx.amount),
                                isIncome: tx.type == "income"
                            )
                        }
                    }
                }
            }
            .id(vm.dataVersion)
            .listStyle(InsetGroupedListStyle())
            .navigationTitle(L10n.t("dashboard.title"))
        }
    }
}

// MARK: - Sub-views

private struct SummaryHalf: View {
    let dot: Color
    let label: String
    let value: String
    let color: Color

    var body: some View {
        VStack(spacing: 4) {
            HStack(spacing: 5) {
                Circle().fill(dot).frame(width: 8, height: 8)
                Text(label).font(AppFont.caption()).foregroundColor(.secondary)
            }
            Text(value).font(AppFont.semibold(size: 15)).foregroundColor(color)
        }
        .frame(maxWidth: .infinity)
    }
}

private struct UpcomingActivityRow: View {
    let type: String
    let title: String
    let date: Date?

    var body: some View {
        HStack(spacing: 12) {
            Text(type)
                .font(AppFont.semibold(size: 12))
                .foregroundColor(.white)
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .background(Color("PrimaryGreen"))
                .cornerRadius(6)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(AppFont.semibold(size: 15))
            }
            Spacer()
            if let date = date {
                Text(date, style: .date)
                    .font(AppFont.caption())
                    .foregroundColor(.secondary)
            }
        }
        .padding(.vertical, 2)
    }
}

private struct RecentTransactionRow: View {
    let title: String
    let category: String
    let formattedAmount: String
    let isIncome: Bool

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(AppFont.semibold(size: 15))
                Text(category).font(AppFont.caption()).foregroundColor(.secondary)
            }
            Spacer()
            Text(formattedAmount)
                .font(AppFont.semibold(size: 15))
                .foregroundColor(isIncome ? Color("PrimaryGreen") : Color("ExpenseRed"))
        }
        .padding(.vertical, 2)
    }
}

struct DashboardView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
