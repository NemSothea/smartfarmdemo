import SwiftUI

private struct OnboardingPage {
    let icon: String
    let title: String
    let subtitle: String
    let color: Color
}

private let pages: [OnboardingPage] = [
    OnboardingPage(
        icon: "chart.line.uptrend.xyaxis",
        title: "តាមដានហិរញ្ញវត្ថុ",
        subtitle: "កត់ត្រាចំណូល និងចំណាយ\nជា រៀល ឬ ដុល្លារ",
        color: Color("PrimaryGreen")
    ),
    OnboardingPage(
        icon: "calendar.badge.plus",
        title: "គ្រោងសកម្មភាព",
        subtitle: "កុំភ្លេចការដាំ ឬ ប្រមូលផល\nជាមួយការជូនដំណឹង",
        color: .blue
    ),
    OnboardingPage(
        icon: "chart.bar.fill",
        title: "រីកចម្រើនជាមួយទិន្នន័យ",
        subtitle: "របាយការណ៍ប្រចាំខែ\nជួយសម្រេចចិត្ត",
        color: Color(red: 0.43, green: 0.24, blue: 0.57)
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
                        .font(AppFont.semibold(size: 17))
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
                .font(AppFont.title())
                .multilineTextAlignment(.center)
            Text(page.subtitle)
                .font(AppFont.body())
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
