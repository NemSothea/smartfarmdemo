import SwiftUI

struct MainTabView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"

    var body: some View {
        TabView {
            DashboardView()
                .tabItem {
                    Label(L10n.t("tab.dashboard"), systemImage: "chart.bar.fill")
                }

            FinanceListView()
                .tabItem {
                    Label(L10n.t("tab.finance"), systemImage: "dollarsign.circle.fill")
                }

            CalendarTabView()
                .tabItem {
                    Label(L10n.t("tab.calendar"), systemImage: "calendar")
                }

            SettingsView()
                .tabItem {
                    Label(L10n.t("tab.settings"), systemImage: "gearshape.fill")
                }
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
