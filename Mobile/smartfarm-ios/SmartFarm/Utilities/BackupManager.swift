import CoreData
import Foundation

struct BackupData: Codable {
    let version: Int
    let exportedAt: Double
    let transactions: [TxBackup]
    let activities: [ActBackup]

    struct TxBackup: Codable {
        let title: String
        let amount: Double
        let type: String
        let category: String
        let note: String
        let date: Double
    }

    struct ActBackup: Codable {
        let title: String
        let type: String
        let notes: String
        let date: Double
    }
}

struct BackupManager {

    static func exportJSON(transactions: [Transaction], activities: [FarmActivity]) throws -> Data {
        let txBackups = transactions.map {
            BackupData.TxBackup(
                title: $0.title ?? "",
                amount: $0.amount,
                type: $0.type ?? "income",
                category: $0.category ?? "",
                note: $0.note ?? "",
                date: $0.date?.timeIntervalSince1970 ?? Date().timeIntervalSince1970
            )
        }
        let actBackups = activities.map {
            BackupData.ActBackup(
                title: $0.title ?? "",
                type: $0.type ?? "",
                notes: $0.notes ?? "",
                date: $0.date?.timeIntervalSince1970 ?? Date().timeIntervalSince1970
            )
        }
        let backup = BackupData(
            version: 1,
            exportedAt: Date().timeIntervalSince1970,
            transactions: txBackups,
            activities: actBackups
        )
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        return try encoder.encode(backup)
    }

    static func importJSON(_ data: Data, context: NSManagedObjectContext) throws {
        let backup = try JSONDecoder().decode(BackupData.self, from: data)

        // Clear existing data
        let txReq: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: "Transaction")
        let actReq: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: "FarmActivity")
        try context.execute(NSBatchDeleteRequest(fetchRequest: txReq))
        try context.execute(NSBatchDeleteRequest(fetchRequest: actReq))
        context.reset()

        // Re-insert transactions
        for tb in backup.transactions {
            let tx = Transaction(context: context)
            tx.id = UUID()
            tx.title = tb.title
            tx.amount = tb.amount
            tx.type = tb.type
            tx.category = tb.category
            tx.note = tb.note
            tx.date = Date(timeIntervalSince1970: tb.date)
        }
        // Re-insert activities
        for ab in backup.activities {
            let act = FarmActivity(context: context)
            act.id = UUID()
            act.title = ab.title
            act.type = ab.type
            act.notes = ab.notes
            act.date = Date(timeIntervalSince1970: ab.date)
            act.isNotified = false
        }
        try context.save()
    }
}
