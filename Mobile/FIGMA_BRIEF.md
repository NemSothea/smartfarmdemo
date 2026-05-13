# SmartFarm — Figma Native Design Brief
> Two separate prompt sets: **iOS (HIG)** and **Android (Material 3)**.
> Paste the Design Token block once, then paste each screen prompt one at a time.

---

# PART A — iOS NATIVE (Apple HIG)

---

## A0 · iOS Design Token Block — paste this ONCE first

```
Native iOS 17 app. Strictly follow Apple Human Interface Guidelines (HIG).

APP: SmartFarm — offline farm management for Cambodian farmers.
LANGUAGE: Khmer (ខ្មែរ) primary text throughout all screens.

NATIVE iOS RULES (non-negotiable):
- All icons: SF Symbols (filled variant for active states)
- Typography: SF Pro Display for large titles · SF Pro Text for body
- Navigation: UINavigationBar with large title style (collapses on scroll)
- Tab bar: UITabBar · 4 tabs · system-style icons + Khmer labels below
- Lists: UITableView insetGrouped style — white rounded-rect sections,
  gray (#F2F2F7) background, hairline separators between rows
- Sheets: UISheetPresentationController — bottom sheet with grabber pill
  at top center, white surface, rounded top corners 10pt
- Swipe actions: red "Delete" swipe-to-delete on list rows
- Buttons: filled rounded rect for primary, tinted for secondary
- No web-style CSS cards with 1px borders — use native grouped table sections

BRAND COLORS (applied as app tint color — replaces iOS system blue):
  Primary (tint)  #2E6E43   (income, active tab, CTA buttons)
  Danger          #B0382A   (expense amounts, delete, destructive)
  Background      #F2F2F7   (iOS system grouped background — matches native)
  Surface         #FFFFFF
  Label           #000000 / system label (auto dark mode)
  Secondary label #3C3C43 60% opacity (iOS system secondary label)

DEVICE FRAME: iPhone 15 Pro · 393×852 pt · Dynamic Island status bar
```

---

## A1 · iOS — Splash Screen

```
Native iOS 17 splash screen for SmartFarm.
Device: iPhone 15 Pro (393×852 pt), Dynamic Island.

Full-bleed background: #2E6E43 (primary green). Status bar icons white.

Center content (vertically centered, no navigation bar, no tab bar):
- SF Symbol "leaf.fill" · 72pt · white color · slightly scaled up with spring animation implied
- "SmartFarm" · SF Pro Display · 34pt · Bold · white · 16pt below icon
- "កសិកម្មឆ្លាតវៃ ងាយស្រួល" · SF Pro Text · 17pt · Regular · white 80% opacity · 8pt below title

Pure brand screen. No buttons, no UI chrome.
Background fills under Dynamic Island cutout.
```

---

## A2 · iOS — Onboarding (3-page PageView)

```
Native iOS 17 onboarding for SmartFarm. 3 pages with UIPageViewController (dot indicator).
Device: iPhone 15 Pro (393×852 pt).

SHARED LAYOUT (all 3 pages):
- White (#FFFFFF) background, no navigation bar
- Vertically centered illustration block (upper 60% of screen):
  · Large SF Symbol icon (80pt, filled, tinted with page accent color)
  · Title text below icon: SF Pro Display · 28pt · Bold · label color · centered
  · Subtitle below title: SF Pro Text · 17pt · Regular · secondary label · centered
    max-width 320pt · 2-line max
- Bottom fixed area (safe area + 24pt margin):
  · UIPageControl dots — 3 dots · active = #2E6E43 · inactive = tertiaryLabel · 8pt margin-bottom
  · Full-width CTA button: UIButton filled style · #2E6E43 background · white Bold 17pt title
    56pt tall · 20pt corner radius · "បន្ទាប់" (page 1–2) / "ចាប់ផ្ដើម" (page 3)

PAGE CONTENT:
Page 1: SF Symbol "chart.line.uptrend.xyaxis" · accent #2E6E43
  Title: "តាមដានហិរញ្ញវត្ថុ"
  Subtitle: "កត់ត្រាចំណូល និងចំណាយជា រៀល ឬ ដុល្លារ"

Page 2: SF Symbol "calendar.badge.plus" · accent #1565C0 (blue)
  Title: "គ្រោងសកម្មភាព"
  Subtitle: "កុំភ្លេចការដាំ ឬ ប្រមូលផល ជាមួយការជូនដំណឹង"

Page 3: SF Symbol "chart.bar.fill" · accent #6D3E91 (purple)
  Title: "រីកចម្រើនជាមួយទិន្នន័យ"
  Subtitle: "របាយការណ៍ប្រចាំខែជួយសម្រេចចិត្ត"
```

---

## A3 · iOS — Finance Screen (ហិរញ្ញវត្ថុ)

```
Native iOS 17 Finance screen for SmartFarm.
Device: iPhone 15 Pro (393×852 pt).

NAVIGATION BAR (UINavigationBar, large title style):
  Large title: "ហិរញ្ញវត្ថុ" · SF Pro Display · 34pt · Bold
  Trailing button: "+" (SF Symbol "plus") · tinted #2E6E43
  Bar tint: white (#FFFFFF), translucent blur under large title as it collapses

TAB BAR (UITabBar, bottom, 4 tabs):
  📊 ផ្ទាំង  💰 ហិរញ្ញ  📅 ប្រតិទិន  ⚙️ ការកំណត់
  Active tab icon+label: #2E6E43 · Inactive: systemGray
  Finance tab is selected.

SCROLL CONTENT (insetGrouped UITableView background #F2F2F7):

── Section 1: Summary (no header, white rounded section) ──
  Single cell (non-interactive, 88pt tall):
    Left side:
      "សមតុល្យ" · 12pt · secondaryLabel
      "340,000 ៛" · 28pt · Bold · #1E5230 (or #B0382A if negative)
    Right side:
      Segmented control (UISegmentedControl):
        [KHR ៛]  [USD $]   · selected = #2E6E43 tint
    Bottom inset row (within same cell, separated by hairline):
      ⬤ ចំណូល  430,000 ៛ · green · 14pt SemiBold  |  ⬤ ចំណាយ  90,000 ៛ · red · 14pt SemiBold

── Section 2: Search + Filter (no section header) ──
  Search cell: UISearchBar embedded in table header (native search style)
    Placeholder: "ស្វែងរក..." · cancel button appears on focus
  Filter row: horizontal ScrollView with UIButton chip pills (not table cells):
    [ទាំងអស់]  [ចំណូល]  [ចំណាយ]
    Selected: #2E6E43 filled pill · white text · 16pt horizontal padding · 20pt radius
    Unselected: systemGray6 fill · label color

── Section 3: Transactions (header: "ប្រតិបត្តិការ") ──
  Each row (UITableViewCell, insetGrouped, 64pt tall, swipe-to-delete enabled):
    Leading content:
      Title: note text "លក់ស្រូវ" · SF Pro Text · 15pt · SemiBold · label color
      Subtitle: "លក់ · 10 មេសា 2026" · 13pt · secondaryLabel
    Trailing content:
      Amount "+250,000 ៛" · 15pt · SemiBold · #2E6E43 (income) or #B0382A (expense)
    Swipe left → red "Delete" (UISwipeActionsConfiguration, destructive)
    Hairline separator between rows (native iOS style)

  Show 5 rows: 3 income (green), 2 expense (red).

FAB: NO native iOS FAB — use the "+" in navigation bar trailing instead.
(FAB is Android pattern. iOS uses nav bar buttons for primary actions.)
```

---

## A4 · iOS — Add Transaction Sheet

```
Native iOS 17 Add Transaction sheet for SmartFarm.
Presented as UISheetPresentationController over Finance screen.

SHEET:
  Grabber pill: 36×4pt · systemFill color · centered top 8pt
  Background: white (#FFFFFF) · top corners rounded 10pt
  Detent: medium (half screen) expandable to large (full)
  Navigation bar inside sheet: title "បន្ថែមប្រតិបត្តិការ" · 17pt SemiBold · centered
    Leading: "បោះបង់" (Cancel) · tinted #2E6E43
    Trailing: "រក្សាទុក" (Save) · Bold · #2E6E43 (disabled = gray when form empty)

FORM CONTENT (UITableView insetGrouped inside sheet):

Section 1 (no header):
  Row 1 — Type picker (segmented):
    Label "ប្រភេទ" left · UISegmentedControl right: [ចំណូល][ចំណាយ] · tint #2E6E43

Section 2 (no header):
  Row 1 — Date: "កាលបរិច្ឆេដ" label + UIDatePicker (compact style, right-aligned, tinted)
  Row 2 — Currency: "រូបិយប័ណ្ណ" label + [KHR ៛][USD $] segmented control right
  Row 3 — Amount: UITextField · placeholder "0" · right-aligned · numeric keyboard
  Row 4 — Category: "ក្រុម" disclosure row → picker: ជី / ពូជ / ការងារ / ឧបករណ៍ / លក់
  Row 5 — Note: UITextField · placeholder "កំណត់ចំណាំ" · left-aligned

All rows: 52pt tall, standard iOS insetGrouped styling.
```

---

## A5 · iOS — Calendar Screen (ប្រតិទិន)

```
Native iOS 17 Calendar screen for SmartFarm.
Device: iPhone 15 Pro (393×852 pt).

NAVIGATION BAR: Large title "ប្រតិទិន", trailing "+" button tinted #2E6E43.

SCROLL CONTENT (insetGrouped, #F2F2F7 background):

── Section 1: Search ──
  UISearchBar in table header · placeholder "ស្វែងរក..."

── Section 2: Month Calendar (white rounded section, 12pt padding) ──
  Month nav row: "‹" button · "មេសា 2026" · 15pt SemiBold centered · "›" button
    Chevrons use SF Symbol "chevron.left" / "chevron.right", tinted #2E6E43

  Weekday header: "អា  ច  អ  ព  ព្រ  សុ  ស" · 11pt SemiBold · tertiaryLabel · 7-column grid

  Day cells (7×5 grid, 40pt cells):
    Normal: number centered · 15pt · label color
    Today: number in #ECF5EE circle background · #2E6E43 text
    Selected: filled #2E6E43 circle · white number · 15pt SemiBold
    Has activity: 4pt dot below number · #2E6E43 (white if selected)

── Section 3: Activities for selected day ──
  Section header: "មេសា 15, 2026" · UITableView section header style (all caps, gray)
  Each row (64pt tall, insetGrouped, swipe-to-delete):
    Leading: colored vertical bar (4×32pt, activity type color, rounded) + VStack:
      Title "ដាំស្រូវ" · 15pt SemiBold · label
      Type "ដំណាំ" · 13pt · secondaryLabel
    Trailing: checkmark button (SF Symbol "circle" / "checkmark.circle.fill" tinted #2E6E43)
    Done state: strikethrough title, row dimmed to 60% opacity

  Empty state (no activities): centered text "គ្មានសកម្មភាពថ្ងៃនេះ" · secondaryLabel

TAB BAR: Calendar tab active.
```

---

## A6 · iOS — Settings Screen (ការកំណត់)

```
Native iOS 17 Settings screen for SmartFarm.
Device: iPhone 15 Pro (393×852 pt).

NAVIGATION BAR: Large title "ការកំណត់". No trailing buttons.

SCROLL CONTENT (insetGrouped UITableView, #F2F2F7 background):

── Section 1: Appearance ──
  Header: "ចំណូលចិត្ត" (Preferences)
  Row 1 — Theme:
    Label "រចនាបថ" (Theme) · left
    UISegmentedControl right: [☀️ ភ្លឺ][🌙 ងងឹត] · tint #2E6E43
  Row 2 — Language:
    Label "ភាសា" (Language) · left
    UISegmentedControl right: [🇰🇭 ខ្មែរ][🇬🇧 English] · tint #2E6E43

── Section 2: Data ──
  Header: "ស្ថិតិ" (Statistics)
  Row 1 — "ប្រតិបត្តិការ" (Transactions) · right: "12" secondaryLabel
  Row 2 — "សកម្មភាព" (Activities) · right: "8" secondaryLabel

── Section 3: Export ──
  Header: "នាំចេញ" (Export)
  Row 1 — SF Symbol "tablecells" icon + "នាំចេញ CSV" · standard disclosure row (›)

── Section 4: Backup ──
  Header: "ការបម្រុងទុក" (Backup)
  Row 1 — SF Symbol "arrow.down.circle" + "បម្រុងទុក JSON" · disclosure ›
  Row 2 — SF Symbol "arrow.up.circle" + "ស្ដារពីឯកសារ" · disclosure ›

── Section 5: Danger zone ──
  No header
  Row 1 — "លុបទិន្នន័យទាំងអស់" · iOS destructive red (#FF3B30 system red) · centered text · no disclosure

── Footer: Version ──
  Section footer text (centered, secondaryLabel small):
  "SmartFarm · កំណែ 1.0.0"

TAB BAR: Settings tab active.
```

---

## A7 · iOS — Dashboard Screen (ផ្ទាំងគ្រប់គ្រង)

```
Native iOS 17 Dashboard screen for SmartFarm.
Device: iPhone 15 Pro (393×852 pt).

NAVIGATION BAR: Large title "ផ្ទាំងគ្រប់គ្រង". No trailing buttons.

SCROLL CONTENT (plain UITableView or ScrollView, #F2F2F7 background):

── Block 1: Monthly Summary Card ──
  UITableView insetGrouped section (white rounded, 16pt padding inside cell):
  Center-aligned:
    "ខែ មេសា 2026" · 13pt · secondaryLabel
    "340,000 ៛" · 34pt · Bold · #1E5230 (positive) or #B0382A (negative)
    Hairline divider
    HStack with 2 columns:
      ⬤ ចំណូល / "430,000 ៛" · 14pt SemiBold #2E6E43
      ⬤ ចំណាយ / "90,000 ៛" · 14pt SemiBold #B0382A

── Block 2: Upcoming Activities ──
  Section header: "សកម្មភាពខាងមុខ"
  3 rows (standard UITableViewCell with leading green pill badge):
    Leading: UILabel badge "ដំណាំ" · 11pt · white on #2E6E43 · 8pt corners
    Title: "ដាំស្រូវ" · 15pt SemiBold
    Detail: "15 មេសា" · 13pt secondaryLabel (right-aligned trailing)
    Disclosure indicator (›)

── Block 3: Recent Transactions ──
  Section header: "ប្រតិបត្តិការថ្មីៗ"
  3 rows (UITableViewCell):
    Title: note text · 15pt SemiBold
    Subtitle: category · 13pt secondaryLabel
    Trailing: amount · 15pt SemiBold · green or red

TAB BAR: Dashboard tab active.
```

---
---

# PART B — ANDROID NATIVE (Material Design 3)

---

## B0 · Android Design Token Block — paste this ONCE first

```
Native Android app. Strictly follow Material Design 3 (Material You).

APP: SmartFarm — offline farm management for Cambodian farmers.
LANGUAGE: Khmer (ខ្មែរ) primary text throughout.

NATIVE ANDROID RULES (non-negotiable):
- Icons: Material Symbols (Rounded style)
- Typography: Roboto · Material 3 type scale
- Top bar: TopAppBar (CenterAligned) or MediumTopAppBar — NOT a custom header
- Bottom navigation: NavigationBar (72dp) · 4 destinations · filled icon when active
- Cards: ElevatedCard or FilledCard (shadow elevation, NOT CSS borders)
- Sheets: ModalBottomSheet with drag handle · 28dp top radius
- FAB: FloatingActionButton (56dp) or ExtendedFAB · containerColor = brand green
- Buttons: FilledButton (primary), OutlinedButton (secondary), TextButton (tertiary)
- Chips: FilterChip for category filters (Material 3 chip style)
- Lists: LazyColumn with ListItem composable · Divider between items
- Ripple on all interactive surfaces (Material 3 default)
- Do NOT use iOS-style grouped table sections

MATERIAL 3 COLOR SCHEME (custom seed color #2E6E43):
  Primary         #2E6E43   onPrimary #FFFFFF
  PrimaryContainer #D9EADD  onPrimaryContainer #1E5230
  Error           #B0382A   onError #FFFFFF
  ErrorContainer  #F9ECE9   onErrorContainer #8E2A1F
  Surface         #FFFFFF
  SurfaceVariant  #F2F4F2
  Background      #F5F6F5
  OnSurface       #1E211E
  OnSurfaceVariant #4D504D
  Outline         #90928E

DEVICE FRAME: Pixel 8 · 393×851 dp · pill camera cutout status bar
```

---

## B1 · Android — Splash Screen

```
Native Android splash screen for SmartFarm using core-splashscreen API.

Device: Pixel 8 (393×851 dp).
Full-bleed background: #2E6E43.
Status bar: transparent, light icons.

Center icon (animated vector drawable style):
  Material Symbol "eco" (rounded) · 72dp · white · inside white circle 120dp / #FFFFFF1A bg
  OR plant/leaf icon from Material Symbols

Below icon:
  "SmartFarm" · Roboto · 34sp · Bold · white
  "កសិកម្មឆ្លាតវៃ ងាយស្រួល" · 16sp · Regular · white 80% opacity

System-managed screen. No buttons. Branding only.
Navigation bar: transparent, light icons.
```

---

## B2 · Android — Onboarding

```
Native Android onboarding for SmartFarm using HorizontalPager (Compose Pager).
Device: Pixel 8 (393×851 dp).

SHARED STRUCTURE (all 3 pages):
- White Surface background (#FFFFFF)
- Status bar: white, dark icons

Page illustration area (top 60%):
  Circular container (140dp diameter, primaryContainer #D9EADD background):
    Material Symbol icon inside · 72dp · primary color #2E6E43
  Title below circle: Roboto · 26sp · Bold · OnSurface #1E211E · center-aligned
  Subtitle: 16sp · Regular · OnSurfaceVariant #4D504D · center-aligned · max 320dp width

Bottom area (24dp from nav bar):
  Pager dots row: 3 dots centered
    Active: 24dp wide × 8dp tall capsule · #2E6E43
    Inactive: 8×8dp circle · #D5D6D2 · 8dp gap between
  FilledButton (full-width, 56dp tall, 16dp radius):
    containerColor #2E6E43 · contentColor white · Roboto 16sp SemiBold
    "បន្ទាប់" (pages 1–2) · "ចាប់ផ្ដើម" (page 3)

PAGE CONTENT:
Page 1: Symbol "trending_up" · accent #2E6E43
  Title: "តាមដានហិរញ្ញវត្ថុ"
  Subtitle: "កត់ត្រាចំណូល និងចំណាយជា រៀល ឬ ដុល្លារ"

Page 2: Symbol "event_note" · accent #1565C0
  Title: "គ្រោងសកម្មភាព"
  Subtitle: "កុំភ្លេចការដាំ ឬ ប្រមូលផល ជាមួយការជូនដំណឹង"

Page 3: Symbol "bar_chart" · accent #6D3E91
  Title: "រីកចម្រើនជាមួយទិន្នន័យ"
  Subtitle: "របាយការណ៍ប្រចាំខែជួយសម្រេចចិត្ត"
```

---

## B3 · Android — Finance Screen (ហិរញ្ញវត្ថុ)

```
Native Android Finance screen for SmartFarm using Jetpack Compose + Material 3.
Device: Pixel 8 (393×851 dp).

TOP APP BAR (CenterAlignedTopAppBar, Material 3):
  Title: "ហិរញ្ញវត្ថុ" · Roboto · 22sp · OnSurface · centered
  Actions: none (FAB handles primary add action)
  Container color: Surface #FFFFFF · elevation shadow on scroll

BOTTOM NAVIGATION BAR (NavigationBar, 72dp):
  4 items: 
    grid_view "ផ្ទាំង" · payments "ហិរញ្ញ" · calendar_today "ប្រតិទិន" · settings "ការកំណត់"
  Active: filled icon + label · indicatorColor = PrimaryContainer #D9EADD
  Inactive: outlined icon + label · OnSurfaceVariant

LAZY COLUMN CONTENT (background #F5F6F5, 12dp horizontal padding):

── Balance ElevatedCard (elevation 2dp, 16dp radius, white surface) ──
  Padding 16dp:
  Row: [Left side] / [Right side KHR/USD chips]
    Left: "សមតុល្យ" · 12sp LabelSmall · OnSurfaceVariant
          "340,000 ៛" · 28sp HeadlineMedium · Bold · Primary #2E6E43 (positive) / Error (negative)
    Right: two FilterChips [KHR ៛] [USD $] · selected = PrimaryContainer fill
  Divider (1dp, OutlineVariant)
  Row below divider:
    Left half: green dot + "ចំណូល" 12sp · "430,000 ៛" 14sp SemiBold Primary
    Right half: red dot + "ចំណាយ" 12sp · "90,000 ៛" 14sp SemiBold Error
  8dp bottom margin

── Search (OutlinedTextField, Material 3 style) ──
  leadingIcon: Material Symbol "search" · placeholder "ស្វែងរក..." · 16sp
  trailingIcon: Symbol "close" (shows when text entered)
  shape: RoundedCornerShape(50%) — pill search field
  Full width, 8dp bottom margin

── Filter Chips row (LazyRow, 4dp gap) ──
  [ទាំងអស់] [ចំណូល] [ចំណាយ]
  FilterChip (Material 3): selected = containerColor PrimaryContainer, border Primary
  Unselected: Surface, border Outline

── Transaction List (LazyColumn) ──
  Each ListItem (Material 3 ListItem composable, 64dp tall):
    headlineContent: note text "លក់ស្រូវ" · 16sp · OnSurface
    supportingContent: "លក់ · 10 មេសា 2026" · 14sp · OnSurfaceVariant
    trailingContent: "+250,000 ៛" · 15sp · SemiBold · Primary or Error color
                     IconButton Symbol "delete" (shows on long press or trailing swipe)
    Divider between items (HorizontalDivider, 1dp)

FAB (FloatingActionButton, 56dp, bottom-end):
  containerColor #2E6E43 · contentColor white
  Symbol "add" · 24dp
  Position: 16dp from bottom nav, 16dp from end edge
```

---

## B4 · Android — Add Transaction Sheet

```
Native Android Add Transaction sheet for SmartFarm.
Material 3 ModalBottomSheet over Finance screen.

SHEET:
  Drag handle: 32×4dp capsule · OnSurfaceVariant · centered top 8dp
  Shape: top corners 28dp radius · Surface #FFFFFF background
  Scrim: 45% black overlay behind sheet

SHEET CONTENT (Column, 20dp horizontal padding, 16dp vertical padding):

Title: "បន្ថែមប្រតិបត្តិការ" · 18sp · SemiBold · OnSurface · 16dp bottom margin

Type selector (two ElevatedButton or SegmentedButton):
  [ចំណូល]  [ចំណាយ]
  Material 3 SegmentedButton · selected segment: PrimaryContainer fill + Primary border
  Full width, 48dp tall, 4dp bottom margin

Date row + Currency row (two OutlinedTextFields in Row):
  Left: label "កាលបរិច្ឆេទ" · DatePickerDialog on tap · trailingIcon calendar symbol
  Right: label "រូបិយប័ណ្ណ" · SegmentedButton [KHR][USD] inline OR two FilterChips

Amount field: OutlinedTextField · label "ចំនួនទឹកប្រាក់" · keyboardType number · full width
Category field: ExposedDropdownMenu · label "ក្រុម" · options: ជី/ពូជ/ការងារ/ឧបករណ៍/លក់
Note field: OutlinedTextField · label "កំណត់ចំណាំ"

Action row (Row, 8dp gap):
  OutlinedButton "បោះបង់" · weight 1f
  FilledButton "រក្សាទុក" · weight 1f · containerColor #2E6E43
  Both 48dp tall, full weight fill
```

---

## B5 · Android — Calendar Screen (ប្រតិទិន)

```
Native Android Calendar screen for SmartFarm using Jetpack Compose + Material 3.
Device: Pixel 8 (393×851 dp).

TOP APP BAR: CenterAlignedTopAppBar · title "ប្រតិទិន"

LAZY COLUMN (background #F5F6F5, 12dp horizontal padding):

── Search ── (same OutlinedTextField pill style as Finance)

── Month Calendar ElevatedCard (2dp elevation, 16dp radius) ──
  Padding 12dp:
  Month nav Row:
    IconButton Symbol "chevron_left" · "មេសា 2026" · 16sp SemiBold centered · IconButton "chevron_right"
    Icon buttons tinted #2E6E43

  Weekday labels row (LazyVerticalGrid 7 columns):
    "អា  ច  អ  ព  ព្រ  សុ  ស" · 11sp · OnSurfaceVariant · Bold · centered per cell

  Day grid (LazyVerticalGrid 7 columns, fixed cell 40dp):
    Normal cell: Text number centered · 15sp · OnSurface
    Today: Box 36dp circle · PrimaryContainer #D9EADD bg · Primary #2E6E43 text
    Selected: Box 36dp circle · Primary #2E6E43 bg · OnPrimary white text · Bold
    Has event: dot below number · 4dp circle · Primary color
  8dp bottom margin

── Day label ── (outside card)
  "មេសា 15, 2026" · 14sp · SemiBold · OnSurface · 8dp margins

── Activity list (LazyColumn) ──
  Each Card (ElevatedCard 1dp, 12dp radius, 8dp gap):
    Row padding 12dp:
      Leading: 4×36dp rounded bar (activity type color)
      Column: name "ដាំស្រូវ" · 15sp SemiBold + type "ដំណាំ" · 13sp OnSurfaceVariant
      Trailing: Checkbox (Material 3, checked = #2E6E43) + IconButton Symbol "close" ErrorColor
    Done state: strikethrough text, 60% alpha on whole row

FAB: same style as Finance screen.
```

---

## B6 · Android — Settings Screen (ការកំណត់)

```
Native Android Settings screen for SmartFarm, Material 3.
Device: Pixel 8 (393×851 dp).

TOP APP BAR: CenterAlignedTopAppBar · title "ការកំណត់"

LAZY COLUMN (background #F5F6F5, vertical padding 8dp):

── Preference Group Card (ElevatedCard, 2dp elevation, 16dp radius, 16dp margin horizontal) ──
  Card padding 16dp:
  Card title row: Symbol "palette" + "ចំណូលចិត្ត" · 14sp Bold · 12dp bottom margin

  Theme label: "រចនាបថ" · 12sp LabelSmall · OnSurfaceVariant · 4dp below
  SegmentedButton row (full width):
    [☀️ ភ្លឺ]  [🌙 ងងឹត]
    Material 3 SegmentedButton · selected = PrimaryContainer + Primary border

  Spacer 16dp

  Language label: "ភាសា" · 12sp · 4dp below
  SegmentedButton row:
    [🇰🇭 ខ្មែរ]  [🇬🇧 English]
    Same SegmentedButton style

── Data Stats Card ── (ElevatedCard, same style)
  Title: Symbol "inventory_2" + "ស្ថិតិទិន្នន័យ"
  Row of 2 stat Surface cards (SurfaceVariant bg, 10dp radius, 10dp padding, 8dp gap):
    Left: "12" · 20sp Bold Primary · "ប្រតិបត្តិការ" 11sp below
    Right: "8" · 20sp Bold Primary · "សកម្មភាព" 11sp below

── Export Card ── (ElevatedCard)
  Title: Symbol "upload_file" + "នាំចេញទិន្នន័យ"
  ListItem: Symbol "table_chart" · "នាំចេញ CSV" · trailingContent Symbol "chevron_right"
  Material ripple on tap

── Backup Card ── (ElevatedCard)
  Title: Symbol "save" + "ការបម្រុងទុក"
  ListItem 1: Symbol "download" · "បម្រុងទុក JSON"
  ListItem 2: Symbol "upload" · "ស្ដារពីឯកសារ"
  Divider between
  ListItem 3: Symbol "delete_forever" · "លុបទិន្នន័យទាំងអស់"
    textColor = Error #B0382A · containerColor = ErrorContainer #F9ECE9

── Version footer ──
  Text centered · 12sp · OnSurfaceVariant:
  "SmartFarm · កំណែ 1.0.0"
```

---

## B7 · Android — Dashboard Screen (ផ្ទាំងគ្រប់គ្រង)

```
Native Android Dashboard for SmartFarm, Material 3.
Device: Pixel 8 (393×851 dp).

TOP APP BAR: CenterAlignedTopAppBar · title "ផ្ទាំងគ្រប់គ្រង"

LAZY COLUMN (background #F5F6F5, 16dp padding):

── Monthly Summary ElevatedCard (4dp elevation, 16dp radius) ──
  Padding 20dp, center-aligned content:
  "ខែ មេសា 2026" · 13sp · OnSurfaceVariant
  "340,000 ៛" · 32sp · Bold · Primary #2E6E43 · 4dp top margin
  HorizontalDivider · 12dp vertical margin
  Row (even split):
    ⬤ "ចំណូល" 12sp secondary · "430,000 ៛" 15sp SemiBold Primary
    ⬤ "ចំណាយ" 12sp secondary · "90,000 ៛" 15sp SemiBold Error

── Upcoming Activities ── (16dp top margin)
  Row: "សកម្មភាពខាងមុខ" · 16sp SemiBold · weight 1 · TextButton "ទាំងអស់ ›"
  3 ElevatedCards (1dp, 12dp radius, 8dp gap):
    Row padding 12dp:
      SuggestionChip/AssistChip "ដំណាំ" · 12sp · PrimaryContainer bg · Primary text
      Column: name 15sp SemiBold · date 13sp OnSurfaceVariant trailing

── Recent Transactions ── (16dp top margin)
  Row: "ប្រតិបត្តិការថ្មីៗ" · 16sp SemiBold
  3 ElevatedCards (1dp, 12dp radius, 8dp gap):
    ListItem: headline note · supporting category · trailing amount colored

NavigationBar: Dashboard tab active (filled icon + indicator).
```

---

## CHECKLIST — All Artboards

| # | Screen | iOS frame | Android frame |
|---|--------|-----------|---------------|
| A1/B1 | Splash | 393×852 | 393×851 |
| A2/B2 | Onboarding ×3 | 393×852 | 393×851 |
| A3/B3 | Finance | 393×852 | 393×851 |
| A4/B4 | Add Transaction sheet | overlay | overlay |
| A5/B5 | Calendar | 393×852 | 393×851 |
| — /— | Add Activity sheet | overlay | overlay |
| A6/B6 | Settings | 393×852 | 393×851 |
| A7/B7 | Dashboard | 393×852 | 393×851 |

**Total: 20 artboards** (10 iOS · 10 Android)
