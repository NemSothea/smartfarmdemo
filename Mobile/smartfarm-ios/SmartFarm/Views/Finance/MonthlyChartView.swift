import SwiftUI

struct MonthlyChartView: View {
    let data: [FarmViewModel.MonthBar]
    let format: (Double) -> String

    private var maxVal: Double {
        data.flatMap { [$0.income, $0.expense] }.max() ?? 1
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("ក្រាហ្វប្រចាំខែ (៦ ខែចុងក្រោយ)")
                .font(.subheadline).fontWeight(.semibold)
                .padding(.horizontal)

            GeometryReader { geo in
                let w = geo.size.width
                let h = geo.size.height - 20
                let groupW = w / CGFloat(data.count)
                let barW = groupW * 0.28
                let gap  = groupW * 0.04

                ZStack(alignment: .bottom) {
                    // Grid lines
                    ForEach([0.25, 0.5, 0.75, 1.0], id: \.self) { frac in
                        Path { p in
                            p.move(to: CGPoint(x: 0, y: h * (1 - CGFloat(frac))))
                            p.addLine(to: CGPoint(x: w, y: h * (1 - CGFloat(frac))))
                        }
                        .stroke(Color.secondary.opacity(0.15), lineWidth: 0.5)
                    }

                    // Bars
                    ForEach(data.indices, id: \.self) { i in
                        let groupX = CGFloat(i) * groupW + groupW * 0.08

                        // Income bar
                        let incH = maxVal > 0 ? CGFloat(data[i].income / maxVal) * h : 0
                        Rectangle()
                            .fill(Color("PrimaryGreen"))
                            .frame(width: barW, height: max(incH, 1))
                            .offset(x: groupX, y: -(h - incH) / 2)

                        // Expense bar
                        let expH = maxVal > 0 ? CGFloat(data[i].expense / maxVal) * h : 0
                        Rectangle()
                            .fill(Color.red.opacity(0.8))
                            .frame(width: barW, height: max(expH, 1))
                            .offset(x: groupX + barW + gap, y: -(h - expH) / 2)
                    }
                }
                .frame(height: h)

                // Month labels
                HStack(spacing: 0) {
                    ForEach(data.indices, id: \.self) { i in
                        Text(data[i].label)
                            .font(.system(size: 9))
                            .foregroundColor(.secondary)
                            .frame(width: groupW)
                    }
                }
                .offset(y: h + 4)
            }
            .frame(height: 140)
            .padding(.horizontal)

            // Legend
            HStack(spacing: 16) {
                Spacer()
                LegendDot(color: Color("PrimaryGreen"), label: "ចំណូល")
                LegendDot(color: .red, label: "ចំណាយ")
                Spacer()
            }
            .font(.caption2)
        }
        .padding(.vertical, 8)
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.05), radius: 4, x: 0, y: 2)
        .padding(.horizontal)
    }
}

private struct LegendDot: View {
    let color: Color
    let label: String
    var body: some View {
        HStack(spacing: 4) {
            Circle().fill(color).frame(width: 7, height: 7)
            Text(label).foregroundColor(.secondary)
        }
    }
}
