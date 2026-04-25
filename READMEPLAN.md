
## Project build plan 
####  Project Setup & MVVM Architecture

    - Setting up the Xcode project with proper folder structure and iOS 13+ deployment target
    - Introduction to MVVM with `ObservableObject` and `@Published` properties (iOS 13+)
    - Creating the core data models: `Transaction`, `FarmActivity`, `Pest`
    - Building a `FarmManager` (main view model) to coordinate all features
    - Set up the project with folders: `Models`, `ViewModels`, `Views`, `Utilities`
    - Implement the `FarmViewModel` with `@Published` arrays for each data type
    - Create a main tab view with placeholders for 3 tabs (Finance, Calendar, Guide)

#### CoreData Persistence

    - Setting up the `.xcdatamodeld` schema for all four models
    - Configuring `NSPersistentContainer` and `NSManagedObjectContext`
    - Using `@FetchRequest` for automatic UI updates
    - Basic CRUD operations (Create, Read, Update, Delete) with CoreData
    - **Live Coding:** Implement all four CoreData entities, test saving and fetching
    - Complete CoreData implementation for all models
    - Add sample data on first launch using a seed method
    - Verify data persists after app restart

#### Navigation & Tab Coordination

    - `NavigationView` and `NavigationLink` for each tab (iOS 13+)
    - Creating a `NavigationCoordinator` using `@State` and `@Binding` to manage navigation state
    - Passing data between screens (e.g., from list to detail)
    - Deep linking simulation: Opening to a specific transaction via `NavigationLink` tag/selection
    - Implement a `NavigationCoordinator` for the Finance tab
    - Create list → detail navigation for transactions
    - Add "Edit" functionality with proper navigation


#### Finance Tracker Module
    - Creating the transaction list with filtering (expense/income/all)
    - Building an "Add Transaction" form with category picker
    - Implementing real-time calculations (total expenses, income, profit)
    - Formatting currency for local users (Riel and Dollar support)
    - Complete Finance tab with all CRUD operations
    - Add a summary card showing current balance, total expenses, total income
    - Implement category filtering (Seeds, Fertilizer, Labor, Tools, Sales)

#### Calendar & Reminders Module
    - Building a calendar view with `DatePicker` and custom grid
    - Creating the `FarmActivity` model with date, type, notes
    - Requesting notification permissions
    - Scheduling local notifications with `UNUserNotificationCenter`
    - Handling notification tap to open specific activity
    - Complete the Calendar tab with activity CRUD
    - Schedule notifications for each activity (1 day before, on the day)
    - Test notifications work when app is closed

#### Pest & Disease Guide Module
    - Designing the `Pest` CoreData entity with name, symptoms, treatment, image name
    - Creating a searchable list with a custom `TextField` search bar (iOS 13+, no `.searchable` dependency)
    - Implementing detail view with expandable sections using `@State` toggles
    - Pre-loading data from a bundled JSON file on first launch
    - Making it work completely offline
    - Create a JSON file with at least 10 pests/diseases
    - Load this data into CoreData on first launch
    - Implement search and category tabs (Insects, Fungal, Bacterial)

#### Dashboard & Cross-Module Integration
    - Building a dashboard tab with summary cards
    - Showing recent transactions, upcoming activities
    - Displaying total profit/loss for current month
    - Creating "Quick Actions" buttons for common tasks
    - Create a Home tab with 4-6 summary cards
    - Add navigation from each card to the relevant tab
    - Show upcoming reminders for the next 7 days

#### Advanced UI & Animations
    - Creating custom `ViewModifier`s for consistent styling
    - Building reusable components: `FarmCard`, `PrimaryButton`, `SectionHeader`
    - Adding subtle animations: fade-in for lists, scale for buttons
    - Implementing pull-to-refresh and loading states
    - Dark mode support and accessibility
    - Create a design system file with colors, fonts, spacing
    - Build 3 reusable components and replace throughout app
    - Add at least 3 animations (list appearance, button taps, transitions)

#### Data Export & Reports
    - Creating monthly profit/loss reports with custom bar charts built using `GeometryReader` and `Shape` (iOS 13+, no Swift Charts dependency)
    - Exporting data as CSV or PDF for sharing
    - Implementing `UIActivityViewController` for sharing reports (iOS 13+)
    - Building a simple PDF generator with `PDFKit` (iOS 11+)
    - Create a monthly profit bar chart using `GeometryReader` and `Rectangle` shapes
    - Add "Share Report" button that exports CSV
    - Generate a simple PDF summary of the month

#### Backup & Restore

    - Understanding the importance of data backup for farmers
    - Implementing export/import of all CoreData records to JSON
    - Using `UIDocumentPickerViewController` wrapped in `UIViewControllerRepresentable` to save/load backup files (iOS 13+)
    - Adding automatic reminder to backup weekly
    - Cloud backup basics (iCloud Drive integration)
    - Complete backup feature that exports all CoreData records to JSON
    - Implement restore that clears and re-imports from backup file
    - Test backup/restore across multiple devices


| Module | Features | Week Completed |
|--------|----------|----------------|
| **Finance Tracker** | Income/expense tracking, profit reports, categories 
| **Calendar & Reminders** | Activity scheduling, local notifications 
| **Pest & Disease Guide** | Offline reference library with search
| **Dashboard** | Unified view of all farm data 
| **Reports & Charts** | Visual profit/loss analysis 
| **Backup & Restore** | Data safety and portability 
| **TestFlight** | Real-world testing with farmers 

---

## This Works Perfectly

1. **Real-world value** - Actual farmers could use this app
2. **Portfolio-ready** - A complete app with multiple features demonstrates comprehensive skills
3. **Cultural relevance** - Designed specifically for Cambodian small-scale farmers
4. **SMART goals met** - Specific, Measurable, Achievable, Realistic, Timely
