# SmartFarm — កសិកម្មឆ្លាតវៃ ងាយស្រួល

An offline-first farm-management demo designed for Cambodian small-scale farmers. SmartFarm bundles finance tracking, an activity calendar, and backup/restore tools into a single Khmer-language interface.

**Live demo:** https://nemsothea.github.io/smartfarmdemo/

**Pitch deck:** https://docs.google.com/presentation/d/1n3ROa-Cp5QTVe4Zn2yL60gpJ9mYfBeYU/edit?usp=sharing&ouid=103841083623835396121&rtpof=true&sd=true

---

## Screenshots

| Splash | Onboarding | Dashboard |
|:--:|:--:|:--:|
| ![Splash screen](images/Splashscreen.png) | ![Intro screen](images/Introscreen.png) | ![Dashboard tab](images/DashboardTab.png) |

| Finance | Calendar | Settings |
|:--:|:--:|:--:|
| ![Finance tab](images/FinanceTap.png) | ![Calendar tab](images/CalendarTab.png) | ![Settings tab](images/SettingTap.png) |

---

## Features

- **ផ្ទាំងគ្រប់គ្រង — Dashboard:** current-month profit/loss hero card, 6-month income vs expense bar chart, upcoming 7-day activities, and recent transactions — all updating live as Finance and Calendar data changes.
- **ហិរញ្ញវត្ថុ — Finance tracker:** income/expense entries with category filters, KHR/USD toggle, and live balance/profit summaries.
- **ប្រតិទិន — Calendar & activities:** monthly grid with activity markers, per-day task list, and completion toggles.
- **ការកំណត់ — Settings:** light/dark theme, language switch (Khmer/English), data stats, CSV export, JSON backup & restore, and full data wipe.
- **Khmer-first UI** with Cambodian-friendly currency formatting (KHR ↔ USD at 4,100 rate).
- **Offline by design** — no network calls for core features.

---

## Tech stack

- React + Vite (web demo)
- Plain CSS, no UI framework
- Deployed to GitHub Pages via GitHub Actions

The product roadmap and module breakdown for the iOS/SwiftUI build live in [`READMEPLAN.md`](./READMEPLAN.md).

---

## Local development

```bash
npm install
npm run dev
```

Build for production:

```bash
npm run build
npm run preview
```

---

## Deployment (GitHub Pages)

This repo deploys `main` to GitHub Pages via GitHub Actions.

1. In GitHub: **Settings → Pages → Build and deployment → Source: GitHub Actions**
2. Push to `main` (or run the **Deploy to GitHub Pages** workflow manually)
