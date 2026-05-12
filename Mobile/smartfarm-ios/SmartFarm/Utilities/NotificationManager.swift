import UserNotifications
import CoreData

struct NotificationManager {

    static func requestPermission() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { _, _ in }
    }

    static func scheduleReminder(for activity: FarmActivity) {
        guard let date = activity.date, let id = activity.id else { return }
        let center = UNUserNotificationCenter.current()

        let content = UNMutableNotificationContent()
        content.title = "ការរំលឹកកសិកម្ម"
        content.body = activity.title ?? ""
        content.sound = .default

        // 1-day-before reminder
        if let dayBefore = Calendar.current.date(byAdding: .day, value: -1, to: date),
           dayBefore > Date() {
            let trigger = UNCalendarNotificationTrigger(
                dateMatching: Calendar.current.dateComponents([.year, .month, .day, .hour, .minute], from: dayBefore),
                repeats: false
            )
            let request = UNNotificationRequest(identifier: "\(id)-before", content: content, trigger: trigger)
            center.add(request, withCompletionHandler: nil)
        }

        // On-the-day reminder (8 AM)
        if date > Date() {
            var dayOf = Calendar.current.dateComponents([.year, .month, .day], from: date)
            dayOf.hour = 8
            dayOf.minute = 0
            let trigger = UNCalendarNotificationTrigger(dateMatching: dayOf, repeats: false)
            let request = UNNotificationRequest(identifier: "\(id)-day", content: content, trigger: trigger)
            center.add(request, withCompletionHandler: nil)
        }
    }

    static func cancelReminder(id: UUID) {
        UNUserNotificationCenter.current().removePendingNotificationRequests(
            withIdentifiers: ["\(id)-before", "\(id)-day"]
        )
    }
}
