import UIKit
import PDFKit

struct ExportManager {

    // MARK: - CSV
    static func csvString(from transactions: [Transaction]) -> String {
        let fmt = DateFormatter()
        fmt.dateStyle = .short
        var lines = ["date,type,amount_khr,category,note,title"]
        for tx in transactions {
            let date = tx.date.map { fmt.string(from: $0) } ?? ""
            let safe: (String?) -> String = { ($0 ?? "").replacingOccurrences(of: ",", with: ";") }
            lines.append("\(date),\(safe(tx.type)),\(Int(tx.amount)),\(safe(tx.category)),\(safe(tx.note)),\(safe(tx.title))")
        }
        return lines.joined(separator: "\n")
    }

    // MARK: - PDF
    static func pdfData(transactions: [Transaction], income: Double, expense: Double, profit: Double) -> Data {
        let pageRect = CGRect(x: 0, y: 0, width: 612, height: 792)
        let margin: CGFloat = 50
        let renderer = UIGraphicsPDFRenderer(bounds: pageRect)

        return renderer.pdfData { ctx in
            ctx.beginPage()
            var y: CGFloat = margin

            // Title
            draw("SmartFarm Report", at: &y, x: margin, font: .boldSystemFont(ofSize: 22),
                 color: UIColor(red: 0.18, green: 0.43, blue: 0.26, alpha: 1))
            y += 8
            let dateFmt = DateFormatter(); dateFmt.dateStyle = .long
            draw(dateFmt.string(from: Date()), at: &y, x: margin, font: .systemFont(ofSize: 11), color: .gray)

            y += 16
            drawLine(at: y, in: pageRect, margin: margin); y += 12

            draw("Summary", at: &y, x: margin, font: .boldSystemFont(ofSize: 13))
            draw("ចំណូល:  \(fmt(income))", at: &y, x: margin + 12, font: .systemFont(ofSize: 11), color: UIColor(red: 0.26, green: 0.63, blue: 0.28, alpha: 1))
            draw("ចំណាយ: \(fmt(expense))", at: &y, x: margin + 12, font: .systemFont(ofSize: 11), color: .red)
            draw("ចំណេញ:  \(fmt(profit))", at: &y, x: margin + 12, font: .boldSystemFont(ofSize: 11), color: profit >= 0 ? UIColor(red: 0.26, green: 0.63, blue: 0.28, alpha: 1) : .red)

            y += 12
            drawLine(at: y, in: pageRect, margin: margin); y += 12
            draw("Transactions", at: &y, x: margin, font: .boldSystemFont(ofSize: 13))

            let cols: [CGFloat] = [margin, margin + 80, margin + 170, margin + 270, margin + 355]
            let headers = ["Date", "Type", "Amount (KHR)", "Category", "Title"]
            for (i, h) in headers.enumerated() {
                h.draw(at: CGPoint(x: cols[i], y: y), withAttributes: [.font: UIFont.boldSystemFont(ofSize: 9), .foregroundColor: UIColor.gray])
            }
            y += 14
            drawLine(at: y, in: pageRect, margin: margin); y += 6

            let txFmt = DateFormatter(); txFmt.dateStyle = .short
            let rowAttr: [NSAttributedString.Key: Any] = [.font: UIFont.systemFont(ofSize: 9)]
            for tx in transactions {
                guard y < pageRect.height - margin else { break }
                [txFmt.string(from: tx.date ?? Date()),
                 tx.type ?? "", "\(Int(tx.amount))", tx.category ?? "",
                 String((tx.title ?? "").prefix(25))]
                    .enumerated().forEach { i, s in
                        s.draw(at: CGPoint(x: cols[i], y: y), withAttributes: rowAttr)
                    }
                y += 14
            }
        }
    }

    // MARK: - helpers
    @discardableResult
    private static func draw(_ text: String, at y: inout CGFloat, x: CGFloat,
                              font: UIFont, color: UIColor = .black, lineHeight: CGFloat = 20) -> CGRect {
        let attr: [NSAttributedString.Key: Any] = [.font: font, .foregroundColor: color]
        text.draw(at: CGPoint(x: x, y: y), withAttributes: attr)
        y += lineHeight
        return .zero
    }

    private static func drawLine(at y: CGFloat, in rect: CGRect, margin: CGFloat) {
        UIColor.lightGray.setStroke()
        let path = UIBezierPath()
        path.move(to: CGPoint(x: margin, y: y))
        path.addLine(to: CGPoint(x: rect.width - margin, y: y))
        path.stroke()
    }

    private static func fmt(_ n: Double) -> String {
        let f = NumberFormatter(); f.numberStyle = .decimal
        return (f.string(from: NSNumber(value: Int(n))) ?? "\(Int(n))") + " ៛"
    }
}

// MARK: - UIActivityViewController SwiftUI wrapper
import SwiftUI

struct ActivityView: UIViewControllerRepresentable {
    let items: [Any]

    func makeUIViewController(context: Context) -> UIActivityViewController {
        UIActivityViewController(activityItems: items, applicationActivities: nil)
    }
    func updateUIViewController(_ uvc: UIActivityViewController, context: Context) {}
}
