import SwiftUI

private struct OnboardingPage {
    let icon: String
    let title: String
    let subtitle: String
    let color: Color
}

private let pages: [OnboardingPage] = [
    OnboardingPage(
        icon: "leaf.fill",
        title: "ស្វាគមន៍មកកាន់\nSmartFarm",
        subtitle: "ជំនួយការគ្រប់គ្រងកសិដ្ឋាន\nសម្រាប់កសិករខ្មែរ",
        color: Color("PrimaryGreen")
    ),
    OnboardingPage(
        icon: "dollarsign.circle.fill",
        title: "ហិរញ្ញវត្ថុ & ប្រតិទិន",
        subtitle: "តាមដានចំណូល ចំណាយ\nនិងសកម្មភាពកសិកម្មប្រចាំថ្ងៃ",
        color: .blue
    ),
    OnboardingPage(
        icon: "lock.shield.fill",
        title: "ឯកជន & Offline",
        subtitle: "ទិន្នន័យទាំងអស់ត្រូវបានរក្សាទុក\nក្នុងឧបករណ៍របស់អ្នក",
        color: .orange
    ),
]

struct OnboardingView: View {
    var onComplete: () -> Void
    @State private var currentPage = 0

    var body: some View {
        ZStack(alignment: .bottom) {
            TabView(selection: $currentPage) {
                ForEach(pages.indices, id: \.self) { index in
                    OnboardingPageView(page: pages[index])
                        .tag(index)
                }
            }
            .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
            .ignoresSafeArea()

            VStack(spacing: 20) {
                HStack(spacing: 8) {
                    ForEach(pages.indices, id: \.self) { i in
                        Capsule()
                            .fill(i == currentPage ? Color("PrimaryGreen") : Color.gray.opacity(0.35))
                            .frame(width: i == currentPage ? 24 : 8, height: 8)
                            .animation(.spring(), value: currentPage)
                    }
                }

                Button(action: {
                    if currentPage < pages.count - 1 {
                        withAnimation { currentPage += 1 }
                    } else {
                        onComplete()
                    }
                }) {
                    Text(currentPage < pages.count - 1 ? "បន្ទាប់" : "ចាប់ផ្ដើម")
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color("PrimaryGreen"))
                        .cornerRadius(14)
                }
                .padding(.horizontal, 32)
            }
            .padding(.bottom, 52)
        }
        .background(Color.white.opacity(0))
    }
}

private struct OnboardingPageView: View {
    let page: OnboardingPage

    var body: some View {
        VStack(spacing: 24) {
            Spacer()
            Circle()
                .fill(page.color.opacity(0.12))
                .frame(width: 140, height: 140)
                .overlay(
                    Image(systemName: page.icon)
                        .font(.system(size: 60))
                        .foregroundColor(page.color)
                )
            Text(page.title)
                .font(.title).fontWeight(.bold)
                .multilineTextAlignment(.center)
            Text(page.subtitle)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Spacer()
            Spacer()
        }
    }
}

struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingView(onComplete: {})
    }
}
