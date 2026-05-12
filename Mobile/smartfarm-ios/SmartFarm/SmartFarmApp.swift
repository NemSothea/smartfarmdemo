import SwiftUI

@main
struct SmartFarmApp: App {
    let persistenceController = PersistenceController.shared

    @StateObject private var farmViewModel: FarmViewModel = {
        FarmViewModel(context: PersistenceController.shared.container.viewContext)
    }()

    @AppStorage("hasCompletedOnboarding") private var hasCompletedOnboarding = false
    @State private var splashActive = true

    var body: some Scene {
        WindowGroup {
            ZStack {
                if splashActive {
                    SplashView()
                        .transition(.opacity)
                        .zIndex(2)
                } else if !hasCompletedOnboarding {
                    OnboardingView { hasCompletedOnboarding = true }
                        .transition(.opacity)
                        .zIndex(1)
                } else {
                    MainTabView()
                        .environment(\.managedObjectContext, persistenceController.container.viewContext)
                        .environmentObject(farmViewModel)
                        .transition(.opacity)
                }
            }
            .animation(.easeInOut(duration: 0.4), value: splashActive)
            .animation(.easeInOut(duration: 0.4), value: hasCompletedOnboarding)
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                    splashActive = false
                }
            }
        }
    }
}
