import SwiftUI

struct SplashView: View {
    @State private var scale: CGFloat = 0.8
    @State private var opacity: Double = 0.0

    var body: some View {
        ZStack {
            Color("PrimaryGreen").ignoresSafeArea()
            VStack(spacing: 16) {
                Image("SplashLogo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 120, height: 120)
                    .clipShape(RoundedRectangle(cornerRadius: 26, style: .continuous))
                    .shadow(color: .black.opacity(0.2), radius: 12, x: 0, y: 6)
                Text("SmartFarm")
                    .font(AppFont.bold(size: 32))
                    .foregroundColor(.white)
                Text("កសិកម្មឆ្លាតវៃ ងាយស្រួល")
                    .font(AppFont.regular(size: 17))
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
