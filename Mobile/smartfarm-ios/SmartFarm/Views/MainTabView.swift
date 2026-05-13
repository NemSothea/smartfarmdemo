import SwiftUI

struct MainTabView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @AppStorage("selectedTab") private var selectedTab: Int = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            DashboardView()
                .tabItem {
                    Label(L10n.t("tab.dashboard"), systemImage: "chart.bar.fill")
                }
                .tag(0)

            FinanceListView()
                .tabItem {
                    Label(L10n.t("tab.finance"), systemImage: "dollarsign.circle.fill")
                }
                .tag(1)

            CalendarTabView()
                .tabItem {
                    Label(L10n.t("tab.calendar"), systemImage: "calendar")
                }
                .tag(2)

            SettingsView()
                .tabItem {
                    Label(L10n.t("tab.settings"), systemImage: "gearshape.fill")
                }
                .tag(3)
        }
        .id(appLanguage)
        .accentColor(Color("PrimaryGreen"))
        .onAppear {
            NotificationManager.requestPermission()
        }
    }
}

struct MainTabView_Previews: PreviewProvider {
    static var previews: some View {
        MainTabView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
