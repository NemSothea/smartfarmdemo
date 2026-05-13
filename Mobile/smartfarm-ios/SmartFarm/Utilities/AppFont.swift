import SwiftUI
import CoreText
import UIKit

enum AppFont {
    static func registerAll() {
        let names = [
            "Hanuman-Regular", "Hanuman-Bold",
            "Inter-Regular", "Inter-Medium", "Inter-SemiBold", "Inter-Bold"
        ]
        for name in names {
            if let url = Bundle.main.url(forResource: name, withExtension: "ttf") {
                CTFontManagerRegisterFontsForURL(url as CFURL, .process, nil)
            }
        }
    }

    static func applyNavigationBarFont() {
        let khmer = isKhmer
        let inline = khmer
            ? UIFont(name: "Hanuman-Bold", size: 17) ?? .boldSystemFont(ofSize: 17)
            : UIFont(name: "Inter-SemiBold", size: 17) ?? .boldSystemFont(ofSize: 17)
        let large = khmer
            ? UIFont(name: "Hanuman-Bold", size: 34) ?? .boldSystemFont(ofSize: 34)
            : UIFont(name: "Inter-Bold", size: 34) ?? .boldSystemFont(ofSize: 34)
        UINavigationBar.appearance().titleTextAttributes      = [.font: inline]
        UINavigationBar.appearance().largeTitleTextAttributes = [.font: large]
    }

    // MARK: - Semantic scale (mirrors iOS Dynamic Type sizes)
    static func caption2() -> Font  { isKhmer ? .custom("Hanuman", size: 11) : .custom("Inter-Regular", size: 11) }
    static func caption() -> Font   { isKhmer ? .custom("Hanuman", size: 12) : .custom("Inter-Regular", size: 12) }
    static func subheadline() -> Font { isKhmer ? .custom("Hanuman", size: 15) : .custom("Inter-Regular", size: 15) }
    static func body() -> Font      { isKhmer ? .custom("Hanuman", size: 17) : .custom("Inter-Regular", size: 17) }
    static func headline() -> Font  { isKhmer ? .custom("Hanuman-Bold", size: 17) : .custom("Inter-SemiBold", size: 17) }
    static func title3() -> Font    { isKhmer ? .custom("Hanuman-Bold", size: 20) : .custom("Inter-SemiBold", size: 20) }
    static func title2() -> Font    { isKhmer ? .custom("Hanuman-Bold", size: 22) : .custom("Inter-Bold", size: 22) }
    static func title() -> Font     { isKhmer ? .custom("Hanuman-Bold", size: 28) : .custom("Inter-Bold", size: 28) }
    static func largeTitle() -> Font { isKhmer ? .custom("Hanuman-Bold", size: 34) : .custom("Inter-Bold", size: 34) }

    // MARK: - Explicit weight + size
    static func regular(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman", size: size) : .custom("Inter-Regular", size: size)
    }
    static func medium(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman", size: size) : .custom("Inter-Medium", size: size)
    }
    static func semibold(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman-Bold", size: size) : .custom("Inter-SemiBold", size: size)
    }
    static func bold(size: CGFloat) -> Font {
        isKhmer ? .custom("Hanuman-Bold", size: size) : .custom("Inter-Bold", size: size)
    }

    static var isKhmer: Bool {
        (UserDefaults.standard.string(forKey: "appLanguage") ?? "km") == "km"
    }
}
