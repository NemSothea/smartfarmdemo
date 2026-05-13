import SwiftUI

let activityTypeColors: [String: Color] = [
    "ដំណាំ":         .green,
    "ស្រោចទឹក":      .cyan,
    "ដាក់ជី":        .brown,
    "ថែទាំ":         .blue,
    "ការពារ":        .orange,
    "ច្រូតកាត់":     .purple,
    "ប្រមូលផល":      .yellow,
    "ត្រួតពិនិត្យ":  .gray
]

private enum CalendarSheet: Identifiable {
    case add
    case edit(FarmActivity)
    var id: String {
        switch self {
        case .add: return "add"
        case .edit(let act): return act.id?.uuidString ?? "edit"
        }
    }
}

struct CalendarTabView: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @State private var displayedMonthDate: Date = {
        let cal = Calendar.current
        return cal.date(from: cal.dateComponents([.year, .month], from: Date()))!
    }()
    @State private var selectedDate: Date = Date()
    @State private var activeSheet: CalendarSheet? = nil
    @State private var searchText = ""

    private var searchResults: [FarmActivity]? {
        let q = searchText.trimmingCharacters(in: .whitespaces).lowercased()
        guard !q.isEmpty else { return nil }
        return vm.activities.filter {
            ($0.title ?? "").lowercased().contains(q) || ($0.type ?? "").lowercased().contains(q)
        }.sorted { ($0.date ?? Date()) < ($1.date ?? Date()) }
    }

    private let activityTypes = ["ដំណាំ", "ស្រោចទឹក", "ដាក់ជី", "ថែទាំ", "ការពារ", "ច្រូតកាត់", "ប្រមូលផល", "ត្រួតពិនិត្យ"]

    var activitiesForSelectedDay: [FarmActivity] {
        vm.activities.filter {
            guard let d = $0.date else { return false }
            return Calendar.current.isDate(d, inSameDayAs: selectedDate)
        }
    }

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Search bar
                HStack {
                    Image(systemName: "magnifyingglass").foregroundColor(.secondary)
                    TextField(L10n.t("calendar.search"), text: $searchText)
                    if !searchText.isEmpty {
                        Button(action: { searchText = "" }) {
                            Image(systemName: "xmark.circle.fill").foregroundColor(.secondary)
                        }
                    }
                }
                .padding(10)
                .background(Color(.systemGray6))
                .cornerRadius(10)
                .padding(.horizontal)
                .padding(.top, 8)
                .padding(.bottom, 4)

                if let results = searchResults {
                    // Search results — flat list across all dates
                    if results.isEmpty {
                        Spacer()
                        Text("រកមិនឃើញ").foregroundColor(.secondary)
                        Spacer()
                    } else {
                        List {
                            ForEach(results, id: \.id) { act in
                                VStack(alignment: .leading, spacing: 2) {
                                    ActivityRowCell(activity: act, onToggle: { vm.toggleDone(act) }, onEdit: {
                                        activeSheet = .edit(act)
                                    })
                                    if let date = act.date {
                                        Text(date, style: .date).font(AppFont.caption2()).foregroundColor(.secondary)
                                            .padding(.leading, 16)
                                    }
                                }
                            }
                            .onDelete { indices in indices.forEach { vm.deleteActivity(results[$0]) } }
                        }
                        .listStyle(PlainListStyle())
                    }
                } else {
                    MonthHeader(displayedMonthDate: $displayedMonthDate, selectedDate: $selectedDate)
                    MonthGridView(
                        activities: vm.activities,
                        displayedMonthDate: displayedMonthDate,
                        selectedDate: $selectedDate
                    )
                    .padding(.horizontal)
                    .padding(.bottom, 8)
                    Divider()
                    if activitiesForSelectedDay.isEmpty {
                        Spacer()
                        Text(L10n.t("calendar.empty")).foregroundColor(.secondary)
                        Spacer()
                    } else {
                        List {
                            ForEach(activitiesForSelectedDay, id: \.id) { act in
                                ActivityRowCell(
                                    activity: act,
                                    onToggle: { vm.toggleDone(act) },
                                    onEdit: { activeSheet = .edit(act) }
                                )
                            }
                            .onDelete { indices in
                                indices.forEach { vm.deleteActivity(activitiesForSelectedDay[$0]) }
                            }
                        }
                        .listStyle(PlainListStyle())
                    }
                }
            }
            .navigationTitle(L10n.t("calendar.title"))
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { activeSheet = .add }) { Image(systemName: "plus") }
                }
            }
            .sheet(item: $activeSheet) { sheet in
                switch sheet {
                case .add:
                    ActivityFormSheet(defaultDate: selectedDate, activityTypes: activityTypes).environmentObject(vm)
                case .edit(let act):
                    ActivityFormSheet(defaultDate: selectedDate, activityTypes: activityTypes, editingActivity: act).environmentObject(vm)
                }
            }
        }
    }
}

// MARK: - Month navigation header

private struct MonthHeader: View {
    @Binding var displayedMonthDate: Date
    @Binding var selectedDate: Date
    @AppStorage("appLanguage") private var appLanguage: String = "km"

    private var title: String {
        let fmt = DateFormatter()
        fmt.dateFormat = "MMMM yyyy"
        fmt.locale = Locale(identifier: appLanguage)
        return fmt.string(from: displayedMonthDate)
    }

    var body: some View {
        HStack {
            Button(action: { shiftMonth(-1) }) {
                Image(systemName: "chevron.left").padding(.horizontal, 12)
            }
            Spacer()
            Text(title).font(AppFont.headline())
            Spacer()
            Button(action: { shiftMonth(1) }) {
                Image(systemName: "chevron.right").padding(.horizontal, 12)
            }
        }
        .padding(.vertical, 10)
    }

    private func shiftMonth(_ delta: Int) {
        if let d = Calendar.current.date(byAdding: .month, value: delta, to: displayedMonthDate) {
            displayedMonthDate = d
        }
    }
}

// MARK: - Month grid

private struct MonthGridView: View {
    let activities: [FarmActivity]
    let displayedMonthDate: Date
    @Binding var selectedDate: Date

    private var calendar: Calendar { Calendar.current }
    private var daysInMonth: Int { calendar.range(of: .day, in: .month, for: displayedMonthDate)!.count }
    private var firstWeekday: Int { calendar.component(.weekday, from: displayedMonthDate) - 1 }

    private var activeDays: Set<Int> {
        Set(activities.compactMap { act -> Int? in
            guard let d = act.date,
                  calendar.isDate(d, equalTo: displayedMonthDate, toGranularity: .month)
            else { return nil }
            return calendar.component(.day, from: d)
        })
    }

    private let weekLabels = ["អា", "ច", "អ", "ព", "ព្រ", "សុ", "ស"]

    var body: some View {
        VStack(spacing: 4) {
            HStack(spacing: 0) {
                ForEach(weekLabels, id: \.self) { label in
                    Text(label).font(AppFont.caption2()).foregroundColor(.secondary).frame(maxWidth: .infinity)
                }
            }
            LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 0), count: 7), spacing: 4) {
                ForEach(0..<firstWeekday, id: \.self) { _ in Color.clear.frame(height: 36) }
                ForEach(1...daysInMonth, id: \.self) { day in
                    let date = dateFor(day: day)
                    DayCell(
                        day: day,
                        isToday: calendar.isDateInToday(date),
                        isSelected: calendar.isDate(date, inSameDayAs: selectedDate),
                        hasDot: activeDays.contains(day),
                        onTap: { selectedDate = date }
                    )
                }
            }
        }
    }

    private func dateFor(day: Int) -> Date {
        var comps = calendar.dateComponents([.year, .month], from: displayedMonthDate)
        comps.day = day
        return calendar.date(from: comps) ?? displayedMonthDate
    }
}

private struct DayCell: View {
    let day: Int; let isToday: Bool; let isSelected: Bool; let hasDot: Bool; let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: 2) {
                Text("\(day)").font(AppFont.subheadline()).frame(width: 32, height: 32)
                    .background(Circle().fill(
                        isSelected ? Color("PrimaryGreen") :
                        isToday    ? Color("PrimaryGreen").opacity(0.2) : Color.clear
                    ))
                    .foregroundColor(isSelected ? .white : .primary)
                Circle().fill(hasDot ? Color("PrimaryGreen") : Color.clear).frame(width: 5, height: 5)
            }
        }
        .buttonStyle(PlainButtonStyle())
    }
}

// MARK: - Activity row with done toggle + edit

private struct ActivityRowCell: View {
    let activity: FarmActivity
    let onToggle: () -> Void
    let onEdit: () -> Void

    var body: some View {
        HStack(spacing: 10) {
            RoundedRectangle(cornerRadius: 3)
                .fill(activityTypeColors[activity.type ?? ""] ?? Color("PrimaryGreen"))
                .frame(width: 4, height: 44)

            VStack(alignment: .leading, spacing: 2) {
                Text(activity.title ?? "")
                    .font(AppFont.subheadline())
                    .strikethrough(activity.isDone)
                    .foregroundColor(activity.isDone ? .secondary : .primary)
                Text(activity.type ?? "").font(AppFont.caption()).foregroundColor(.secondary)
                if let notes = activity.notes, !notes.isEmpty {
                    Text(notes).font(AppFont.caption2()).foregroundColor(.secondary)
                }
            }
            Spacer()
            Button(action: onEdit) {
                Image(systemName: "pencil")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .padding(.horizontal, 4)
            }
            .buttonStyle(PlainButtonStyle())
            Button(action: onToggle) {
                Image(systemName: activity.isDone ? "checkmark.circle.fill" : "circle")
                    .font(.title2)
                    .foregroundColor(activity.isDone ? Color("PrimaryGreen") : .secondary)
            }
            .buttonStyle(PlainButtonStyle())
        }
        .padding(.vertical, 4)
        .opacity(activity.isDone ? 0.72 : 1.0)
    }
}

// MARK: - Unified Add / Edit Activity Sheet

private struct ActivityFormSheet: View {
    @EnvironmentObject var vm: FarmViewModel
    @AppStorage("appLanguage") private var appLanguage: String = "km"
    @Environment(\.dismiss) private var dismiss
    let activityTypes: [String]
    var editingActivity: FarmActivity?

    @State private var title: String
    @State private var selectedType: String
    @State private var notes: String
    @State private var date: Date

    init(defaultDate: Date, activityTypes: [String], editingActivity: FarmActivity? = nil) {
        self.activityTypes = activityTypes
        self.editingActivity = editingActivity
        let act = editingActivity
        _title        = State(initialValue: act?.title ?? "")
        _selectedType = State(initialValue: act?.type ?? activityTypes.first ?? "ដំណាំ")
        _notes        = State(initialValue: act?.notes ?? "")
        _date         = State(initialValue: act?.date ?? defaultDate)
    }

    private var canSave: Bool { !title.trimmingCharacters(in: .whitespaces).isEmpty }

    var body: some View {
        NavigationView {
            Form {
                Section {
                    TextField(L10n.t("form.title"), text: $title)
                    Picker(L10n.t("form.type"), selection: $selectedType) {
                        ForEach(activityTypes, id: \.self) { type in
                            HStack {
                                Circle().fill(activityTypeColors[type] ?? Color("PrimaryGreen")).frame(width: 8, height: 8)
                                Text(type)
                            }
                            .tag(type)
                        }
                    }
                    DatePicker(L10n.t("form.date"), selection: $date, displayedComponents: .date)
                    TextField(L10n.t("form.note"), text: $notes)
                }
            }
            .navigationTitle(editingActivity == nil ? L10n.t("calendar.add") : L10n.t("calendar.edit"))
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(L10n.t("form.cancel")) { dismiss() }
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(L10n.t("form.save")) {
                        let t = title.trimmingCharacters(in: .whitespaces)
                        if let act = editingActivity {
                            vm.updateActivity(act, title: t, type: selectedType, notes: notes, date: date)
                        } else {
                            vm.addActivity(title: t, type: selectedType, notes: notes, date: date)
                        }
                        dismiss()
                    }
                    .disabled(!canSave)
                }
            }
        }
        .navigationViewStyle(.stack)
    }
}

struct CalendarTabView_Previews: PreviewProvider {
    static var previews: some View {
        CalendarTabView()
            .environmentObject(FarmViewModel(context: PersistenceController.preview.container.viewContext))
    }
}
