import SwiftUI

struct MainTabView: View {
    @EnvironmentObject var vm: FarmViewModel

    var body: some View {
        TabView {
            DashboardView()
                .tabItem {
                    Label("ផ្ទាំងគ្រប់គ្រង", systemImage: "chart.bar.fill")
                }

            FinanceListView()
                .tabItem {
                    Label("ហិរញ្ញវត្ថុ", systemImage: "dollarsign.circle.fill")
                }

            CalendarTabView()
                .tabItem {
                    Label("ប្រតិទិន", systemImage: "calendar")
                }

            SettingsView()
                .tabItem {
                    Label("ការកំណត់", systemImage: "gearshape.fill")
                }
        }
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
