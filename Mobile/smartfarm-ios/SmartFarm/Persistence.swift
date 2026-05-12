import CoreData

struct PersistenceController {
    static let shared = PersistenceController()

    static var preview: PersistenceController = {
        let result = PersistenceController(inMemory: true)
        let ctx = result.container.viewContext

        let tx = Transaction(context: ctx)
        tx.id = UUID()
        tx.title = "លក់ស្រូវ"
        tx.amount = 500000
        tx.type = "income"
        tx.category = "Sales"
        tx.note = ""
        tx.date = Date()

        let act = FarmActivity(context: ctx)
        act.id = UUID()
        act.title = "ស្រោចទឹកដំណាំ"
        act.type = "ស្រោចទឹក"
        act.notes = ""
        act.date = Calendar.current.date(byAdding: .day, value: 1, to: Date())!
        act.isNotified = false
        act.isDone = false

        try? ctx.save()
        return result
    }()

    let container: NSPersistentContainer

    init(inMemory: Bool = false) {
        container = NSPersistentContainer(name: "SmartFarm")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        if let description = container.persistentStoreDescriptions.first {
            description.setOption(true as NSNumber, forKey: NSMigratePersistentStoresAutomaticallyOption)
            description.setOption(true as NSNumber, forKey: NSInferMappingModelAutomaticallyOption)
        }
        container.viewContext.automaticallyMergesChangesFromParent = true
        container.loadPersistentStores { _, error in
            if let error = error as NSError? {
                fatalError("CoreData load failed: \(error), \(error.userInfo)")
            }
        }
    }
}
