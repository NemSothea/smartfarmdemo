import SwiftUI

struct DashboardView: View {
    @EnvironmentObject var vm: FarmViewModel

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 16) {
                    // Profit summary card
                    ProfitCard(
                        income: vm.currentMonthIncome,
                        expense: vm.currentMonthExpense,
                        profit: vm.currentMonthProfit,
                        format: vm.format
                    )

                    // Upcoming activities
                    if !vm.upcomingActivities.isEmpty {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("សកម្មភាពខាងមុខ")
                                .font(.headline)
                                .padding(.horizontal)

                            ForEach(vm.upcomingActivities, id: \.id) { act in
                                ActivityRow(activity: act)
                            }
                        }
                    }

                    // Recent transactions
                    if !vm.transactions.isEmpty {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("ប្រតិបត្តិការថ្មីៗ")
                                .font(.headline)
                                .padding(.horizontal)

                            ForEach(vm.transactions.prefix(5), id: \.id) { tx in
                                TransactionRow(tx: tx, format: vm.format)
                            }
                        }
                    }
                }
                .padding(.vertical)
            }
            .navigationTitle("ផ្ទាំងគ្រប់គ្រង")
        }
    }
}

// MARK: - Sub-views

private struct ProfitCard: View {
    let income: Double
    let expense: Double
    let profit: Double
    let format: (Double) -> String

    var body: some View {
        VStack(spacing: 12) {
            Text("ខែនេះ")
                .font(.subheadline)
                .foregroundColor(.secondary)

            Text(format(profit))
                .font(.system(size: 32, weight: .bold))
                .foregroundColor(profit >= 0 ? Color("PrimaryGreen") : .red)

            HStack(spacing: 24) {
                VStack {
                    Text("ចំណូល")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(format(income))
                        .font(.subheadline)
                        .foregroundColor(Color("PrimaryGreen"))
                }
                Divider().frame(height: 32)
                VStack {
                    Text("ចំណាយ")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(format(expense))
                        .font(.subheadline)
                        .foregroundColor(.red)
                }
            }
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.06), radius: 6, x: 0, y: 2)
        .padding(.horizontal)
    }
}

private struct ActivityRow: View {
    let activity: FarmActivity

    var body: some View {
        HStack {
            Text(activity.type ?? "")
                .font(.caption)
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .background(Color("PrimaryGreen").opacity(0.15))
                .cornerRadius(6)

            VStack(alignment: .leading, spacing: 2) {
                Text(activity.title ?? "")
                    .font(.subheadline)
                if let date = activity.date {
                    Text(date, style: .date)
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
            }
            Spacer()
        }
        .padding(.horizontal)
        .padding(.vertical, 6)
        .background(Color(.systemBackground))
    }
}

private struct TransactionRow: View {
    let tx: Transaction
    let format: (Double) -> String

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 2) {
                Text(tx.title ?? "")
                    .font(.subheadline)
                Text(tx.category ?? "")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            Spacer()
            Text(format(tx.amount))
                .font(.subheadline)
                .fontWeight(.medium)
                .foregroundColor(tx.type == "income" ? Color("PrimaryGreen") : .red)
        }
        .padding(.horizontal)
        .padding(.vertical, 6)
        .background(Color(.systemBackground))
    }
}

struct DashboardView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
