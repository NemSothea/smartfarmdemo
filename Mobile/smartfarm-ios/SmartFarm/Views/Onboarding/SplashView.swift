import SwiftUI

struct SplashView: View {
    @State private var scale: CGFloat = 0.8
    @State private var opacity: Double = 0.0

    var body: some View {
        ZStack {
            Color("PrimaryGreen").ignoresSafeArea()
            VStack(spacing: 16) {
                Circle()
                    .fill(Color.white.opacity(0.15))
                    .frame(width: 120, height: 120)
                    .overlay(
                        Image(systemName: "leaf.fill")
                            .font(.system(size: 56))
                            .foregroundColor(.white)
                    )
                Text("SmartFarm")
                    .font(.largeTitle).fontWeight(.bold)
                    .foregroundColor(.white)
                Text("ជំនួយការកសិករ")
                    .font(.title3)
                    .foregroundColor(.white.opacity(0.85))
            }
            .scaleEffect(scale)
            .opacity(opacity)
            .onAppear {
                withAnimation(.easeOut(duration: 0.6)) {
                    scale = 1.0
                    opacity = 1.0
                }
            }
        }
    }
}

struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView()
    }
}
