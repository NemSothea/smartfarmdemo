import { useState, useEffect } from "react";

const KM = {
  appName: "SmartFarm",
  tagline: "កសិកម្មឆ្លាតវៃ ងាយស្រួល",
  next: "បន្ទាប់",
  getStarted: "ចាប់ផ្តើម",
  tabDashboard: "ផ្ទាំងគ្រប់គ្រង",
  tabFinance: "ហិរញ្ញវត្ថុ",
  tabCalendar: "ប្រតិទិន",
  tabSettings: "ការកំណត់",
  dashUpcoming: "សកម្មភាពខាងមុខ",
  dashRecent: "ប្រតិបត្តិការថ្មីៗ",
  dashChart: "ក្រាបប្រចាំខែ",
  all: "ទាំងអស់",
  income: "ចំណូល",
  expense: "ចំណាយ",
  balance: "សមតុល្យ",
  addTransaction: "បន្ថែមប្រតិបត្តិការ",
  amount: "ចំនួនទឹកប្រាក់",
  date: "កាលបរិច្ឆេទ",
  currency: "រូបិយប័ណ្ណ",
  category: "ប្រភេទ",
  note: "កំណត់ចំណាំ",
  save: "រក្សាទុក",
  cancel: "បោះបង់",
  delete: "លុប",
  addActivity: "បន្ថែមសកម្មភាព",
  activityName: "ឈ្មោះសកម្មភាព",
  noData: "មិនមានទិន្នន័យ",
  search: "ស្វែងរក...",
  searchResults: "លទ្ធផលស្វែងរក",
  noResults: "គ្មានលទ្ធផល",
  dataExport: "នាំចេញទិន្នន័យ",
  exportTxnsCSV: "នាំចេញប្រតិបត្តិការ (CSV)",
  backupRestore: "បម្រុងទុក & ស្ដារ",
  backupAll: "បម្រុងទុកទាំងអស់ (JSON)",
  restoreFromFile: "ស្ដារពីឯកសារ JSON",
  restoreConfirm: "ការស្ដារនឹងជំនួសទិន្នន័យបច្ចុប្បន្ន។ បន្តដែរឬទេ?",
  restoreSuccess: "ស្ដារទិន្នន័យជោគជ័យ",
  restoreFail: "ឯកសារមិនត្រឹមត្រូវ",
  clearAll: "លុបទិន្នន័យទាំងអស់",
  clearConfirm: "លុបទិន្នន័យទាំងអស់? ប្រតិបត្តិការនេះមិនអាចត្រឡប់វិញបាន។",
  dataStats: "ស្ថិតិទិន្នន័យ",
  transactions: "ប្រតិបត្តិការ",
  activities: "សកម្មភាព",
  preferences: "ចំណូលចិត្ត",
  theme: "រចនាបថ",
  themeLight: "ភ្លឺ",
  themeDark: "ងងឹត",
  language: "ភាសា",
  langKm: "ខ្មែរ",
  langEn: "English",
  appVersion: "កំណែ",
  categories: ["ជី","ពូជ","ការងារ","ឧបករណ៍","លក់"],
  months: ["មករា","កុម្ភៈ","មីនា","មេសា","ឧសភា","មិថុនា","កក្កដា","សីហា","កញ្ញា","តុលា","វិច្ឆិកា","ធ្នូ"],
  days: ["អា","ច","អ","ព","ព្រ","សុ","ស"],
  onboardPages: [
    { icon:"💰", title:"តាមដានហិរញ្ញវត្ថុ", sub:"កត់ត្រាចំណូល និងចំណាយជា រៀល ឬ ដុល្លារ" },
    { icon:"📅", title:"គ្រោងសកម្មភាព", sub:"កុំភ្លេចការដាំ ឬ ប្រមូលផល ជាមួយការជូនដំណឹង" },
    { icon:"📊", title:"រីកចម្រើនជាមួយទិន្នន័យ", sub:"របាយការណ៍ប្រចាំខែជួយសម្រេចចិត្ត" },
  ],
};

const EN = {
  appName: "SmartFarm",
  tagline: "Smart Farming, Made Simple",
  next: "Next",
  getStarted: "Get Started",
  tabDashboard: "Dashboard",
  tabFinance: "Finance",
  tabCalendar: "Calendar",
  tabSettings: "Settings",
  dashUpcoming: "Upcoming",
  dashRecent: "Recent Transactions",
  dashChart: "Monthly Chart",
  all: "All",
  income: "Income",
  expense: "Expense",
  balance: "Balance",
  addTransaction: "Add Transaction",
  amount: "Amount",
  date: "Date",
  currency: "Currency",
  category: "Category",
  note: "Note",
  save: "Save",
  cancel: "Cancel",
  delete: "Delete",
  addActivity: "Add Activity",
  activityName: "Activity Name",
  noData: "No data",
  search: "Search...",
  searchResults: "Search Results",
  noResults: "No results",
  dataExport: "Export Data",
  exportTxnsCSV: "Export Transactions (CSV)",
  backupRestore: "Backup & Restore",
  backupAll: "Backup All (JSON)",
  restoreFromFile: "Restore from JSON File",
  restoreConfirm: "Restore will replace current data. Continue?",
  restoreSuccess: "Data restored successfully",
  restoreFail: "Invalid file",
  clearAll: "Clear All Data",
  clearConfirm: "Delete all data? This action cannot be undone.",
  dataStats: "Data Statistics",
  transactions: "Transactions",
  activities: "Activities",
  preferences: "Preferences",
  theme: "Theme",
  themeLight: "Light",
  themeDark: "Dark",
  language: "Language",
  langKm: "ខ្មែរ",
  langEn: "English",
  appVersion: "App Version",
  categories: ["Fertilizer","Seeds","Labor","Tools","Sales"],
  months: ["January","February","March","April","May","June","July","August","September","October","November","December"],
  days: ["Su","Mo","Tu","We","Th","Fr","Sa"],
  onboardPages: [
    { icon:"💰", title:"Track Finances", sub:"Record income and expenses in KHR or USD" },
    { icon:"📅", title:"Plan Activities", sub:"Never miss planting or harvest with reminders" },
    { icon:"📊", title:"Grow with Data", sub:"Monthly reports help you make better decisions" },
  ],
};

const APP_VERSION = "1.0.0";

// ── AppColors: centralized color system ─────────────────────────
// Modern fresh-agriculture theme. All values resolve to CSS variables
// defined in index.css, so switching `data-theme="dark"` on <html>
// swaps the entire palette (light ↔ dark) with zero component changes.
//
// Light mode hex (for reference):
//   primary 600 #2E6E43  secondary 600 #8B5A2B  bg #F5F6F5  surface #FFF
//   neutral 500 #6B6E6A  danger main #B0382A    accent bg #E8F3EB
// Dark mode hex:
//   primary 600 #6FBE8E  secondary 600 #D5A47A  bg #0F1312  surface #171C19
//   neutral 500 #9EA3A0  danger main #E26B5C    accent bg #1F3626
const AppColors = {
  primary: {
    50:  "var(--sf-g-50)",
    100: "var(--sf-g-100)",
    200: "var(--sf-g-200)",
    500: "var(--sf-g-500)",
    600: "var(--sf-g-600)",
    700: "var(--sf-g-700)",
    800: "var(--sf-g-800)",
  },
  secondary: {
    50:  "var(--sf-sec-50)",
    100: "var(--sf-sec-100)",
    500: "var(--sf-sec-500)",
    600: "var(--sf-sec-600)",
    700: "var(--sf-sec-700)",
  },
  accent: {
    bg:   "var(--sf-accent-bg)",
    tint: "var(--sf-accent-tint)",
  },
  neutral: {
    50:  "var(--sf-gr-50)",
    100: "var(--sf-gr-100)",
    200: "var(--sf-gr-200)",
    400: "var(--sf-gr-400)",
    500: "var(--sf-gr-500)",
    600: "var(--sf-gr-600)",
    700: "var(--sf-gr-700)",
    800: "var(--sf-gr-800)",
  },
  bg:      "var(--sf-bg)",
  surface: "var(--sf-surface)",
  desk:    "var(--sf-desk)",
  danger: {
    main:   "var(--sf-danger-main)",
    dark:   "var(--sf-danger-dark)",
    bg:     "var(--sf-danger-bg)",
    border: "var(--sf-danger-border)",
  },
};
// Shorthand aliases used across the UI — all resolve to AppColors
const G      = AppColors.primary;
const GR     = AppColors.neutral;
const SEC    = AppColors.secondary;
const DANGER = AppColors.danger;

// ── Shared money formatter ───────────────────────────────────────
// Kept at module scope so Finance and Settings format identically.
const USD_RATE = 4100;
function formatMoney(n, currency) {
  return currency === "KHR"
    ? `${Math.round(n).toLocaleString()} ៛`
    : `$${(n / USD_RATE).toFixed(2)}`;
}

// ── Seed data ────────────────────────────────────────────────────
const _d = (offset) => {
  const d = new Date();
  d.setDate(d.getDate() + offset);
  return [d.getFullYear(), String(d.getMonth()+1).padStart(2,'0'), String(d.getDate()).padStart(2,'0')].join('-');
};
const INITIAL_TXNS = [
  { id:1, type:"income",  amount:250000, currency:"KHR", category:"លក់",    note:"លក់ស្រូវ",      date:_d(-3)  },
  { id:2, type:"expense", amount:45000,  currency:"KHR", category:"ជី",     note:"ទិញជីគីមី",      date:_d(-5)  },
  { id:3, type:"expense", amount:30000,  currency:"KHR", category:"ការងារ", note:"ប្រាក់ខែកម្មករ", date:_d(-7)  },
  { id:4, type:"income",  amount:180000, currency:"KHR", category:"លក់",    note:"លក់បន្លែ",      date:_d(-10) },
  { id:5, type:"expense", amount:15000,  currency:"KHR", category:"ពូជ",    note:"ទិញពូជស្រូវ",    date:_d(-12) },
];
const INITIAL_ACTS = [
  { id:1, name:"ដាំស្រូវ",  date:_d(2),  type:"ដំណាំ",  done:false },
  { id:2, name:"ស្រោចទឹក", date:_d(-1), type:"ថែទាំ",  done:true  },
  { id:3, name:"បាញ់ថ្នាំ", date:_d(4),  type:"ការពារ", done:false },
  { id:4, name:"ប្រមូលផល", date:_d(6),  type:"ប្រមូល", done:false },
];
// ── Persistence hook ────────────────────────────────────────────
function usePersistedState(key, initial) {
  const [value, setValue] = useState(() => {
    try {
      const stored = localStorage.getItem(key);
      return stored ? JSON.parse(stored) : initial;
    } catch {
      return initial;
    }
  });
  useEffect(() => {
    try { localStorage.setItem(key, JSON.stringify(value)); } catch { /* ignore */ }
  }, [key, value]);
  return [value, setValue];
}

// Shared input style — ensures consistent look locally and in sandbox
const inputStyle = {
  padding:"10px 12px", borderRadius:10,
  border:`1px solid ${GR[200]}`, fontSize:14,
  width:"100%", boxSizing:"border-box",
  outline:"none", fontFamily:"inherit",
  background:AppColors.surface, color:GR[800],
};
const selectStyle = {
  ...inputStyle, width:"100%", cursor:"pointer", appearance:"none",
  backgroundImage:`url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%236b7280' d='M6 8L1 3h10z'/%3E%3C/svg%3E")`,
  backgroundRepeat:"no-repeat", backgroundPosition:"right 12px center",
  paddingRight:32,
};
// Search bar with clear button
function SearchBar({ value, onChange, style, placeholder = "Search..." }) {
  return (
    <div style={{ position:"relative", margin:"0 12px 10px", ...style }}>
      <span style={{ position:"absolute", left:11, top:"50%", transform:"translateY(-50%)", fontSize:13, color:GR[400], pointerEvents:"none" }}>🔍</span>
      <input
        value={value}
        onChange={e => onChange(e.target.value)}
        placeholder={placeholder}
        style={{ ...inputStyle, paddingLeft:32, paddingRight:value ? 30 : 12 }}
      />
      {value && (
        <button onClick={() => onChange("")}
          style={{ position:"absolute", right:10, top:"50%", transform:"translateY(-50%)", background:"none", border:"none", cursor:"pointer", fontSize:13, color:GR[400], lineHeight:1, padding:0 }}>✕</button>
      )}
    </div>
  );
}

// Bottom sheet modal wrapper
function BottomSheet({ onClose, title, children }) {
  return (
    <div style={{ position:"absolute", inset:0, background:"rgba(0,0,0,0.45)", display:"flex", alignItems:"flex-end", zIndex:20 }}
      onClick={e => { if(e.target===e.currentTarget) onClose(); }}>
      <div style={{ background:AppColors.surface, width:"100%", borderRadius:"20px 20px 0 0", padding:"20px 16px", display:"flex", flexDirection:"column", gap:12, maxHeight:"85%", overflowY:"auto" }}>
        <div style={{ fontSize:15, fontWeight:700, color:GR[800], marginBottom:4 }}>{title}</div>
        {children}
      </div>
    </div>
  );
}

function ActionRow({ onCancel, onSave, T }) {
  return (
    <div style={{ display:"flex", gap:8, marginTop:4 }}>
      <button onClick={onCancel} style={{ flex:1, padding:"12px", borderRadius:12, border:`1px solid ${GR[200]}`, background:AppColors.surface, color:GR[600], fontSize:14, cursor:"pointer", fontFamily:"inherit" }}>{T.cancel}</button>
      <button onClick={onSave}   style={{ flex:1, padding:"12px", borderRadius:12, border:"none", background:G[600], color:"#fff", fontSize:14, fontWeight:600, cursor:"pointer", fontFamily:"inherit" }}>{T.save}</button>
    </div>
  );
}

function FAB({ onClick }) {
  return (
    <button onClick={onClick}
      style={{ position:"absolute", bottom:76, right:14, width:50, height:50, borderRadius:25, background:G[600], color:"#fff", border:"none", fontSize:26, cursor:"pointer", display:"flex", alignItems:"center", justifyContent:"center", boxShadow:"0 3px 10px rgba(0,0,0,0.25)", zIndex:10, lineHeight:1 }}>+</button>
  );
}

// ── SPLASH ──────────────────────────────────────────────────────
function Splash({ onDone, T }) {
  const [vis, setVis] = useState(false);
  useEffect(() => {
    const t1 = setTimeout(() => setVis(true), 100);
    const t2 = setTimeout(() => onDone(), 2400);
    return () => { clearTimeout(t1); clearTimeout(t2); };
  }, [onDone]);
  return (
    <div style={{ flex:1, background:G[600], display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", gap:20 }}>
      <div style={{ fontSize:72, lineHeight:1, height:88, display:"flex", alignItems:"center", justifyContent:"center", transform:vis?"scale(1)":"scale(0.4)", opacity:vis?1:0, transition:"all 0.65s cubic-bezier(.34,1.56,.64,1)" }}>🌱</div>
      <div style={{ color:"#fff", fontSize:28, fontWeight:700, lineHeight:1.3, opacity:vis?1:0, transition:"opacity 0.5s 0.3s", fontFamily:"inherit" }}>{T.appName}</div>
      <div style={{ color:"rgba(255,255,255,0.85)", fontSize:14, lineHeight:1.5, opacity:vis?1:0, transition:"opacity 0.5s 0.5s", fontFamily:"inherit" }}>{T.tagline}</div>
    </div>
  );
}

// ── ONBOARDING ──────────────────────────────────────────────────
function Onboarding({ onDone, T }) {
  const [page, setPage] = useState(0);
  const p = T.onboardPages[page];
  return (
    <div style={{ flex:1, display:"flex", flexDirection:"column", padding:"32px 24px 24px", background:AppColors.surface }}>
      <div style={{ flex:1, display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", gap:24, textAlign:"center" }}>
        <div style={{ fontSize:80, lineHeight:1, height:96, display:"flex", alignItems:"center", justifyContent:"center" }}>{p.icon}</div>
        <div style={{ fontSize:22, fontWeight:700, color:GR[800], lineHeight:1.4, maxWidth:280 }}>{p.title}</div>
        <div style={{ fontSize:14, color:GR[500], lineHeight:1.75, maxWidth:300 }}>{p.sub}</div>
      </div>
      <div style={{ display:"flex", justifyContent:"center", gap:8, marginBottom:24 }}>
        {T.onboardPages.map((_,i) => (
          <div key={i} style={{ width:i===page?22:8, height:8, borderRadius:4, background:i===page?G[600]:GR[200], transition:"all .3s" }} />
        ))}
      </div>
      <button onClick={() => page < 2 ? setPage(p=>p+1) : onDone()}
        style={{ background:G[600], color:"#fff", border:"none", borderRadius:14, padding:"14px", fontSize:16, fontWeight:600, cursor:"pointer", fontFamily:"inherit" }}>
        {page < 2 ? T.next : T.getStarted}
      </button>
    </div>
  );
}

// ── FINANCE ─────────────────────────────────────────────────────
function Finance({ txns, setTxns, currency, setCurrency, T }) {
  const [filter, setFilter] = useState("all");
  const [search, setSearch] = useState("");
  const [showForm, setShowForm] = useState(false);
  const todayStr = new Date().toISOString().slice(0, 10);
  const [form, setForm] = useState({ type:"income", amount:"", inputCurrency:"KHR", date:todayStr, category:T.categories[0], note:"" });

  const fmt = (n) => formatMoney(n, currency);
  const totalIncome  = txns.filter(t=>t.type==="income").reduce((s,t)=>s+t.amount,0);
  const totalExpense = txns.filter(t=>t.type==="expense").reduce((s,t)=>s+t.amount,0);
  const balance = totalIncome - totalExpense;
  const q = search.trim().toLowerCase();
  const filtered = txns.filter(t => {
    const matchType   = filter === "all" || t.type === filter;
    const matchSearch = !q || t.note.toLowerCase().includes(q) || t.category.toLowerCase().includes(q);
    return matchType && matchSearch;
  });

  function addTxn() {
    if (!form.amount) return;
    const rawAmount = Number(form.amount);
    if (!Number.isFinite(rawAmount) || rawAmount <= 0) return;
    const amountKHR = form.inputCurrency === "USD" ? rawAmount * USD_RATE : rawAmount;
    setTxns(p => [...p, { id:Date.now(), type:form.type, amount:amountKHR, currency:form.inputCurrency, category:form.category, note:form.note || "—", date:form.date || todayStr }]);
    setShowForm(false);
    setForm({ type:"income", amount:"", inputCurrency:"KHR", date:todayStr, category:T.categories[0], note:"" });
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], position:"relative" }}>
      {/* Balance card */}
      <div style={{ margin:"12px 12px 8px", background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
        <div style={{ padding:"14px 16px 10px", borderBottom:`1px solid ${GR[100]}`, display:"flex", justifyContent:"space-between", alignItems:"center" }}>
          <div>
            <div style={{ fontSize:11, color:GR[400], marginBottom:3 }}>{T.balance}</div>
            <div style={{ fontSize:24, fontWeight:700, color:balance>=0?G[700]:DANGER.main }}>{fmt(balance)}</div>
          </div>
          <div style={{ display:"flex", gap:6 }}>
            {["KHR","USD"].map(c => (
              <button key={c} onClick={()=>setCurrency(c)}
                style={{ fontSize:11, padding:"4px 10px", borderRadius:20, border:`1.5px solid ${currency===c?G[600]:GR[200]}`, background:currency===c?G[50]:AppColors.surface, color:currency===c?G[700]:GR[500], cursor:"pointer", fontWeight:currency===c?600:400, fontFamily:"inherit" }}>{c}</button>
            ))}
          </div>
        </div>
        <div style={{ display:"flex" }}>
          {[[G[600],T.income,totalIncome],[DANGER.main,T.expense,totalExpense]].map(([col,label,val],i) => (
            <div key={i} style={{ flex:1, padding:"10px 16px", borderRight:i===0?`1px solid ${GR[100]}`:"none" }}>
              <div style={{ display:"flex", alignItems:"center", gap:5, marginBottom:3 }}>
                <div style={{ width:7, height:7, borderRadius:4, background:col }} />
                <span style={{ fontSize:11, color:GR[400] }}>{label}</span>
              </div>
              <div style={{ fontSize:14, fontWeight:600, color:col }}>{fmt(val)}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Search bar */}
      <SearchBar value={search} onChange={setSearch} placeholder={T.search} />

      {/* Filter chips */}
      <div style={{ display:"flex", gap:6, padding:"0 12px 10px" }}>
        {[["all",T.all],["income",T.income],["expense",T.expense]].map(([k,l]) => (
          <button key={k} onClick={()=>setFilter(k)}
            style={{ fontSize:12, padding:"5px 12px", borderRadius:20, border:`1px solid ${filter===k?G[600]:GR[200]}`, background:filter===k?G[100]:AppColors.surface, color:filter===k?G[700]:GR[600], cursor:"pointer", fontFamily:"inherit" }}>{l}</button>
        ))}
      </div>

      {/* Transaction list */}
      <div style={{ padding:"0 12px", display:"flex", flexDirection:"column", gap:8 }}>
        {filtered.length === 0 && (
          <div style={{ color:GR[400], fontSize:13, textAlign:"center", padding:"24px 0" }}>{T.noResults}</div>
        )}
        {filtered.map(t => (
          <div key={t.id} style={{ background:AppColors.surface, borderRadius:12, padding:"12px 14px", display:"flex", justifyContent:"space-between", alignItems:"center", border:`1px solid ${GR[200]}` }}>
            <div>
              <div style={{ fontSize:13, fontWeight:600, color:GR[800] }}>{t.note}</div>
              <div style={{ fontSize:11, color:GR[400], marginTop:2 }}>{t.category} · {t.date}</div>
            </div>
            <div style={{ display:"flex", alignItems:"center", gap:8 }}>
              <div style={{ fontSize:14, fontWeight:600, color:t.type==="income"?G[600]:DANGER.main }}>
                {t.type==="income"?"+":"-"}{fmt(t.amount)}
              </div>
              <button onClick={()=>setTxns(p=>p.filter(x=>x.id!==t.id))}
                style={{ fontSize:13, color:DANGER.main, background:"none", border:"none", cursor:"pointer", lineHeight:1 }}>✕</button>
            </div>
          </div>
        ))}
      </div>

      {showForm && (
        <BottomSheet onClose={()=>setShowForm(false)} title={T.addTransaction}>
          <div style={{ display:"flex", gap:8 }}>
            {[["income",T.income],["expense",T.expense]].map(([k,l]) => (
              <button key={k} onClick={()=>setForm(f=>({...f,type:k}))}
                style={{ flex:1, padding:"9px", borderRadius:10, border:`1.5px solid ${form.type===k?G[600]:GR[200]}`, background:form.type===k?G[50]:AppColors.surface, color:form.type===k?G[700]:GR[600], fontSize:13, cursor:"pointer", fontFamily:"inherit", fontWeight:form.type===k?600:400 }}>{l}</button>
            ))}
          </div>
          <div style={{ display:"flex", gap:8 }}>
            <div style={{ flex:1, display:"flex", flexDirection:"column", gap:6 }}>
              <div style={{ fontSize:12, color:GR[500], fontWeight:600 }}>{T.date}</div>
              <input type="date" value={form.date} onChange={e=>setForm(f=>({...f,date:e.target.value}))} style={inputStyle} />
            </div>
            <div style={{ flex:1, display:"flex", flexDirection:"column", gap:6 }}>
              <div style={{ fontSize:12, color:GR[500], fontWeight:600 }}>{T.currency}</div>
              <div style={{ display:"flex", gap:6 }}>
                {["KHR","USD"].map(c => (
                  <button key={c} onClick={()=>setForm(f=>({...f,inputCurrency:c}))}
                    style={{ flex:1, fontSize:12, padding:"9px 10px", borderRadius:10, border:`1.5px solid ${form.inputCurrency===c?G[600]:GR[200]}`, background:form.inputCurrency===c?G[50]:AppColors.surface, color:form.inputCurrency===c?G[700]:GR[600], cursor:"pointer", fontWeight:form.inputCurrency===c?600:400, fontFamily:"inherit" }}>{c}</button>
                ))}
              </div>
            </div>
          </div>
          <input placeholder={T.amount} type="number" value={form.amount} onChange={e=>setForm(f=>({...f,amount:e.target.value}))} style={inputStyle} />
          <select value={form.category} onChange={e=>setForm(f=>({...f,category:e.target.value}))} style={selectStyle}>
            {T.categories.map(c=><option key={c}>{c}</option>)}
          </select>
          <input placeholder={T.note} value={form.note} onChange={e=>setForm(f=>({...f,note:e.target.value}))} style={inputStyle} />
          <ActionRow onCancel={()=>setShowForm(false)} onSave={addTxn} T={T} />
        </BottomSheet>
      )}

      <div style={{ height:80 }} />
      <FAB onClick={()=>setShowForm(true)} />
    </div>
  );
}

// ── CALENDAR ────────────────────────────────────────────────────
function CalendarTab({ activities, setActivities, T }) {
  const today = new Date();
  const [year]  = useState(today.getFullYear());
  const [month] = useState(today.getMonth());
  const [selected, setSelected] = useState(today.getDate());
  const [search, setSearch] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name:"", date:"", type:T.categories[0] });

  const daysInMonth = new Date(year, month+1, 0).getDate();
  const firstDay    = new Date(year, month, 1).getDay();
  const selStr      = `${year}-${String(month+1).padStart(2,"0")}-${String(selected).padStart(2,"0")}`;
  const actDates    = new Set(
    activities
      .filter(a => {
        const d = new Date(a.date);
        return d.getFullYear() === year && d.getMonth() === month;
      })
      .map(a => new Date(a.date).getDate())
  );
  const selActs     = activities.filter(a => a.date === selStr);
  const sq = search.trim().toLowerCase();
  const searchResults = sq
    ? activities
        .filter(a => a.name.toLowerCase().includes(sq) || a.type.toLowerCase().includes(sq))
        .sort((a, b) => a.date.localeCompare(b.date))
    : null;

  const activityTypes = ["ដំណាំ","ថែទាំ","ការពារ","ប្រមូល"];

  function openForm() {
    setShowForm(true);
    setForm({ name:"", date:selStr, type:activityTypes[0] });
  }

  function addActivity() {
    if (!form.name || !form.date) return;
    setActivities(p => [...p, { id:Date.now(), ...form, done:false }]);
    setShowForm(false);
    setForm({ name:"", date:selStr, type:activityTypes[0] });
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px", position:"relative" }}>
      {/* Search bar */}
      <SearchBar value={search} onChange={setSearch} style={{ margin:"0 0 10px" }} placeholder={T.search} />

      {searchResults ? (
        /* ── Search results mode ── */
        <div>
          <div style={{ fontSize:12, color:GR[500], fontWeight:600, marginBottom:8 }}>
            {T.searchResults} ({searchResults.length})
          </div>
          {searchResults.length === 0 && (
            <div style={{ color:GR[400], fontSize:13, textAlign:"center", padding:"24px 0" }}>{T.noResults}</div>
          )}
          {searchResults.map(a => (
            <div key={a.id} style={{ background:AppColors.surface, borderRadius:12, padding:"12px 14px", marginBottom:8, display:"flex", justifyContent:"space-between", alignItems:"center", border:`1px solid ${GR[200]}` }}>
              <div>
                <div style={{ fontSize:13, fontWeight:600, color:GR[800] }}>{a.name}</div>
                <div style={{ fontSize:11, color:GR[400], marginTop:2 }}>{a.type} · {a.date}</div>
              </div>
              <div style={{ display:"flex", gap:8, alignItems:"center" }}>
                <button onClick={()=>setActivities(p=>p.map(x=>x.id===a.id?{...x,done:!x.done}:x))}
                  style={{ fontSize:18, background:"none", border:"none", cursor:"pointer", lineHeight:1 }}>{a.done?"✅":"⬜"}</button>
                <button onClick={()=>setActivities(p=>p.filter(x=>x.id!==a.id))}
                  style={{ fontSize:13, color:DANGER.main, background:"none", border:"none", cursor:"pointer" }}>✕</button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        /* ── Normal calendar mode ── */
        <>
          {/* Calendar grid */}
          <div style={{ background:AppColors.surface, borderRadius:14, padding:"12px", border:`1px solid ${GR[200]}`, marginBottom:12 }}>
            <div style={{ textAlign:"center", fontWeight:700, color:GR[800], marginBottom:10, fontSize:14 }}>
              {T.months[month]} {year}
            </div>
            <div style={{ display:"grid", gridTemplateColumns:"repeat(7,1fr)", gap:2, marginBottom:6 }}>
              {T.days.map(d => <div key={d} style={{ textAlign:"center", fontSize:10, color:GR[400], fontWeight:600 }}>{d}</div>)}
            </div>
            <div style={{ display:"grid", gridTemplateColumns:"repeat(7,1fr)", gap:2 }}>
              {Array(firstDay).fill(null).map((_,i) => <div key={"e"+i} />)}
              {Array(daysInMonth).fill(null).map((_,i) => {
                const d = i+1;
                const isToday = d===today.getDate() && month===today.getMonth() && year===today.getFullYear();
                const isSel   = d===selected;
                const hasDot  = actDates.has(d);
                return (
                  <div key={d} onClick={()=>setSelected(d)}
                    style={{ aspectRatio:"1", display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", borderRadius:"50%", cursor:"pointer",
                      background:isSel?G[600]:isToday?G[100]:"transparent", color:isSel?"#fff":isToday?G[700]:GR[700], fontSize:13, fontWeight:isSel?600:400 }}>
                    {d}
                    {hasDot && <div style={{ width:4, height:4, borderRadius:2, background:isSel?"#fff":G[500], marginTop:1 }} />}
                  </div>
                );
              })}
            </div>
          </div>

          <div style={{ fontWeight:600, color:GR[700], fontSize:13, marginBottom:8 }}>{T.months[month]} {selected}, {year}</div>
          {selActs.length === 0 && <div style={{ color:GR[400], fontSize:13, textAlign:"center", padding:"20px 0" }}>{T.noData}</div>}
          {selActs.map(a => (
            <div key={a.id} style={{ background:AppColors.surface, borderRadius:12, padding:"12px 14px", marginBottom:8, display:"flex", justifyContent:"space-between", alignItems:"center", border:`1px solid ${GR[200]}` }}>
              <div>
                <div style={{ fontSize:13, fontWeight:600, color:GR[800] }}>{a.name}</div>
                <div style={{ fontSize:11, color:GR[400], marginTop:2 }}>{a.type}</div>
              </div>
              <div style={{ display:"flex", gap:8, alignItems:"center" }}>
                <button onClick={()=>setActivities(p=>p.map(x=>x.id===a.id?{...x,done:!x.done}:x))}
                  style={{ fontSize:18, background:"none", border:"none", cursor:"pointer", lineHeight:1 }}>{a.done?"✅":"⬜"}</button>
                <button onClick={()=>setActivities(p=>p.filter(x=>x.id!==a.id))}
                  style={{ fontSize:13, color:DANGER.main, background:"none", border:"none", cursor:"pointer" }}>✕</button>
              </div>
            </div>
          ))}
        </>
      )}

      {showForm && (
        <BottomSheet onClose={()=>setShowForm(false)} title={T.addActivity}>
          <input placeholder={T.activityName} value={form.name} onChange={e=>setForm(f=>({...f,name:e.target.value}))} style={inputStyle} />
          <input type="date" value={form.date || selStr} onChange={e=>setForm(f=>({...f,date:e.target.value}))} style={inputStyle} />
          <select value={form.type} onChange={e=>setForm(f=>({...f,type:e.target.value}))} style={selectStyle}>
            {activityTypes.map(t=><option key={t}>{t}</option>)}
          </select>
          <ActionRow onCancel={()=>setShowForm(false)} onSave={addActivity} T={T} />
        </BottomSheet>
      )}

      <div style={{ height:80 }} />
      <FAB onClick={openForm} />
    </div>
  );
}

// ── SETTINGS (Reports, Export, Backup/Restore) ──────────────────
function downloadFile(filename, content, mime) {
  const blob = new Blob([content], { type: mime });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

function csvEscape(v) {
  const s = String(v ?? "");
  return /[",\n]/.test(s) ? `"${s.replace(/"/g,'""')}"` : s;
}

function SettingsCard({ icon, title, children }) {
  return (
    <div style={{ background:AppColors.surface, borderRadius:14, padding:"14px", border:`1px solid ${GR[200]}`, marginBottom:12 }}>
      <div style={{ fontSize:13, fontWeight:700, color:GR[800], marginBottom:12, display:"flex", alignItems:"center", gap:6 }}>
        <span style={{ fontSize:16 }}>{icon}</span>{title}
      </div>
      {children}
    </div>
  );
}

function SettingsBtn({ onClick, label, icon, danger }) {
  return (
    <button onClick={onClick} style={{
      width:"100%", padding:"11px 12px", borderRadius:10,
      border:`1px solid ${danger?DANGER.border:GR[200]}`,
      background:danger?DANGER.bg:"#fff",
      color:danger?DANGER.dark:GR[700],
      fontSize:13, fontWeight:500, cursor:"pointer", fontFamily:"inherit",
      marginBottom:8, textAlign:"left", display:"flex", alignItems:"center", gap:8,
      boxSizing:"border-box",
    }}>
      <span style={{ fontSize:15 }}>{icon}</span>{label}
    </button>
  );
}

function Settings({ txns, activities, setTxns, setActivities, theme, setTheme, lang, setLang, T }) {
  function exportTxnsCSV() {
    const header = "date,type,amount,currency,category,note\n";
    const rows = txns.map(t => [t.date,t.type,t.amount,t.currency,t.category,t.note].map(csvEscape).join(",")).join("\n");
    downloadFile(`smartfarm-transactions-${new Date().toISOString().slice(0,10)}.csv`, header + rows, "text/csv;charset=utf-8");
  }

  function exportBackup() {
    const data = {
      app: "smartfarm",
      version: 1,
      exportedAt: new Date().toISOString(),
      txns, activities,
    };
    downloadFile(`smartfarm-backup-${new Date().toISOString().slice(0,10)}.json`, JSON.stringify(data, null, 2), "application/json");
  }

  function onRestoreFile(e) {
    const file = e.target.files?.[0];
    e.target.value = "";
    if (!file) return;
    if (!window.confirm(T.restoreConfirm)) return;
    const reader = new FileReader();
    reader.onload = (ev) => {
      try {
        const d = JSON.parse(ev.target.result);
        if (Array.isArray(d.txns)) setTxns(d.txns);
        if (Array.isArray(d.activities)) setActivities(d.activities);
        window.alert(T.restoreSuccess);
      } catch {
        window.alert(T.restoreFail);
      }
    };
    reader.readAsText(file);
  }

  function clearAll() {
    if (!window.confirm(T.clearConfirm)) return;
    setTxns([]);
    setActivities([]);
  }

  const toggleBtnStyle = (active) => ({
    flex:1, padding:"10px", borderRadius:10,
    border:`1.5px solid ${active?G[600]:GR[200]}`,
    background:active?G[50]:AppColors.surface,
    color:active?G[700]:GR[600],
    fontSize:13, fontWeight:active?700:500,
    cursor:"pointer", fontFamily:"inherit",
    display:"flex", alignItems:"center", justifyContent:"center", gap:6,
  });

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px" }}>
      {/* Preferences — theme + language */}
      <SettingsCard icon="🎨" title={T.preferences}>
        <div style={{ fontSize:11, color:GR[600], fontWeight:600, marginBottom:8 }}>{T.theme}</div>
        <div style={{ display:"flex", gap:8, marginBottom:14 }}>
          {[["light",T.themeLight,"☀️"],["dark",T.themeDark,"🌙"]].map(([k,l,ic]) => (
            <button key={k} onClick={()=>setTheme(k)} style={toggleBtnStyle(theme===k)}>
              <span style={{ fontSize:14 }}>{ic}</span>{l}
            </button>
          ))}
        </div>
        <div style={{ fontSize:11, color:GR[600], fontWeight:600, marginBottom:8 }}>{T.language}</div>
        <div style={{ display:"flex", gap:8 }}>
          {[["km",T.langKm,"🇰🇭"],["en",T.langEn,"🇬🇧"]].map(([k,l,ic]) => (
            <button key={k} onClick={()=>setLang(k)} style={toggleBtnStyle(lang===k)}>
              <span style={{ fontSize:14 }}>{ic}</span>{l}
            </button>
          ))}
        </div>
      </SettingsCard>

      {/* Data stats */}
      <SettingsCard icon="📦" title={T.dataStats}>
        <div style={{ display:"flex", gap:8 }}>
          {[
            [txns.length, T.transactions],
            [activities.length, T.activities],
          ].map(([n, l], i) => (
            <div key={i} style={{ flex:1, padding:"10px", background:GR[50], borderRadius:10, textAlign:"center" }}>
              <div style={{ fontSize:18, fontWeight:700, color:G[700] }}>{n}</div>
              <div style={{ fontSize:10, color:GR[500], marginTop:2 }}>{l}</div>
            </div>
          ))}
        </div>
      </SettingsCard>

      {/* Export */}
      <SettingsCard icon="📤" title={T.dataExport}>
        <SettingsBtn onClick={exportTxnsCSV} icon="📄" label={T.exportTxnsCSV} />
      </SettingsCard>

      {/* Backup & Restore */}
      <SettingsCard icon="💾" title={T.backupRestore}>
        <SettingsBtn onClick={exportBackup} icon="⬇️" label={T.backupAll} />
        <label style={{
          width:"100%", padding:"11px 12px", borderRadius:10,
          border:`1px solid ${GR[200]}`, background:AppColors.surface, color:GR[700],
          fontSize:13, fontWeight:500, cursor:"pointer", fontFamily:"inherit",
          marginBottom:8, textAlign:"left", display:"flex", alignItems:"center", gap:8,
          boxSizing:"border-box",
        }}>
          <span style={{ fontSize:15 }}>⬆️</span>{T.restoreFromFile}
          <input type="file" accept="application/json,.json" onChange={onRestoreFile} style={{ display:"none" }} />
        </label>
        <SettingsBtn onClick={clearAll} icon="🗑️" label={T.clearAll} danger />
      </SettingsCard>

      {/* App version */}
      <div style={{ textAlign:"center", padding:"8px 0 4px" }}>
        <div style={{ fontSize:11, color:GR[400] }}>{T.appVersion} {APP_VERSION}</div>
      </div>

      <div style={{ height:20 }} />
    </div>
  );
}

// ── DASHBOARD ───────────────────────────────────────────────────
function Dashboard({ txns, activities, currency, T }) {
  const fmt = (n) => formatMoney(n, currency);
  const now = new Date();

  const sameMonth = (dateStr) => {
    const [y, m] = dateStr.split('-').map(Number);
    return m - 1 === now.getMonth() && y === now.getFullYear();
  };

  const monthIncome  = txns.filter(t => t.type === "income"  && sameMonth(t.date)).reduce((s, t) => s + t.amount, 0);
  const monthExpense = txns.filter(t => t.type === "expense" && sameMonth(t.date)).reduce((s, t) => s + t.amount, 0);
  const balance = monthIncome - monthExpense;

  const todayMs = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();
  const weekMs  = todayMs + 7 * 24 * 60 * 60 * 1000;
  const upcoming = activities
    .filter(a => {
      const [y,m,d] = a.date.split('-').map(Number);
      const ms = new Date(y, m-1, d).getTime();
      return !a.done && ms >= todayMs && ms <= weekMs;
    })
    .sort((a, b) => a.date.localeCompare(b.date))
    .slice(0, 3);

  const recent = [...txns].sort((a, b) => b.date.localeCompare(a.date)).slice(0, 3);

  const chartData = Array.from({ length: 6 }, (_, i) => {
    const d = new Date(now.getFullYear(), now.getMonth() - 5 + i, 1);
    const m = d.getMonth(); const y = d.getFullYear();
    const inc = txns.filter(t => t.type==="income"  && new Date(t.date).getMonth()===m && new Date(t.date).getFullYear()===y).reduce((s,t)=>s+t.amount,0);
    const exp = txns.filter(t => t.type==="expense" && new Date(t.date).getMonth()===m && new Date(t.date).getFullYear()===y).reduce((s,t)=>s+t.amount,0);
    return { label: T.months[m].slice(0, 3), income: inc, expense: exp };
  });
  const maxVal = Math.max(...chartData.flatMap(d => [d.income, d.expense]), 1);
  const BAR_H = 80;

  const monthName = `ខែ ${T.months[now.getMonth()]} ${now.getFullYear()}`;

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px", display:"flex", flexDirection:"column", gap:12 }}>

      {/* Monthly balance hero */}
      <div style={{ background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
        <div style={{ padding:"16px", textAlign:"center", borderBottom:`1px solid ${GR[100]}` }}>
          <div style={{ fontSize:11, color:GR[400], marginBottom:4 }}>{monthName}</div>
          <div style={{ fontSize:28, fontWeight:700, color:balance>=0?G[700]:DANGER.main }}>{fmt(balance)}</div>
        </div>
        <div style={{ display:"flex" }}>
          {[[G[600], T.income, monthIncome], [DANGER.main, T.expense, monthExpense]].map(([col, label, val], i) => (
            <div key={i} style={{ flex:1, padding:"10px 16px", borderRight:i===0?`1px solid ${GR[100]}`:"none" }}>
              <div style={{ display:"flex", alignItems:"center", gap:5, marginBottom:3 }}>
                <div style={{ width:7, height:7, borderRadius:4, background:col }} />
                <span style={{ fontSize:11, color:GR[400] }}>{label}</span>
              </div>
              <div style={{ fontSize:14, fontWeight:600, color:col }}>{fmt(val)}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Bar chart — last 6 months */}
      <div style={{ background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, padding:"14px" }}>
        <div style={{ fontSize:12, fontWeight:700, color:GR[700], marginBottom:12 }}>{T.dashChart}</div>
        <div style={{ display:"flex", alignItems:"flex-end", height:BAR_H + 20, gap:2 }}>
          {chartData.map((d, i) => {
            const incH = Math.max((d.income / maxVal) * BAR_H, 2);
            const expH = Math.max((d.expense / maxVal) * BAR_H, 2);
            return (
              <div key={i} style={{ flex:1, display:"flex", flexDirection:"column", alignItems:"center" }}>
                <div style={{ display:"flex", alignItems:"flex-end", gap:2, height:BAR_H }}>
                  <div style={{ width:9, height:incH, background:G[600], borderRadius:"3px 3px 0 0" }} />
                  <div style={{ width:9, height:expH, background:DANGER.main, borderRadius:"3px 3px 0 0", opacity:0.85 }} />
                </div>
                <div style={{ fontSize:8, color:GR[400], marginTop:4, textAlign:"center" }}>{d.label}</div>
              </div>
            );
          })}
        </div>
        <div style={{ display:"flex", justifyContent:"center", gap:16, marginTop:8 }}>
          {[[G[600], T.income], [DANGER.main, T.expense]].map(([col, label]) => (
            <div key={label} style={{ display:"flex", alignItems:"center", gap:4, fontSize:10, color:GR[500] }}>
              <div style={{ width:8, height:8, borderRadius:4, background:col }} />{label}
            </div>
          ))}
        </div>
      </div>

      {/* Upcoming activities */}
      {upcoming.length > 0 && (
        <div style={{ background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
          <div style={{ padding:"12px 14px 8px", fontSize:12, fontWeight:700, color:GR[700] }}>{T.dashUpcoming}</div>
          {upcoming.map(a => (
            <div key={a.id} style={{ padding:"10px 14px", borderTop:`1px solid ${GR[100]}`, display:"flex", alignItems:"center", gap:10 }}>
              <div style={{ background:G[600], borderRadius:6, padding:"3px 8px", fontSize:10, fontWeight:600, color:"#fff", whiteSpace:"nowrap" }}>{a.type}</div>
              <div style={{ flex:1, fontSize:13, fontWeight:500, color:GR[800] }}>{a.name}</div>
              <div style={{ fontSize:11, color:GR[400] }}>{a.date}</div>
            </div>
          ))}
        </div>
      )}

      {/* Recent transactions */}
      {recent.length > 0 && (
        <div style={{ background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
          <div style={{ padding:"12px 14px 8px", fontSize:12, fontWeight:700, color:GR[700] }}>{T.dashRecent}</div>
          {recent.map(tx => (
            <div key={tx.id} style={{ padding:"10px 14px", borderTop:`1px solid ${GR[100]}`, display:"flex", justifyContent:"space-between", alignItems:"center" }}>
              <div>
                <div style={{ fontSize:13, fontWeight:600, color:GR[800] }}>{tx.note}</div>
                <div style={{ fontSize:11, color:GR[400], marginTop:1 }}>{tx.category}</div>
              </div>
              <div style={{ fontSize:14, fontWeight:600, color:tx.type==="income"?G[600]:DANGER.main }}>
                {tx.type==="income"?"+":"−"}{fmt(tx.amount)}
              </div>
            </div>
          ))}
        </div>
      )}

      <div style={{ height:16 }} />
    </div>
  );
}

// ── ROOT ────────────────────────────────────────────────────────
export default function App() {
  const [screen, setScreen] = useState("splash");
  const [tab, setTab] = useState(0);
  const [txns, setTxns] = usePersistedState("sf_txns_v2", INITIAL_TXNS);
  const [activities, setActivities] = usePersistedState("sf_acts_v2", INITIAL_ACTS);
  const [currency, setCurrency] = usePersistedState("sf_currency", "KHR");
  const [theme, setTheme] = usePersistedState("sf_theme", "light");
  const [lang, setLang] = usePersistedState("sf_lang", "km");
  const T = lang === "km" ? KM : EN;

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
  }, [theme]);

  const tabs = [
    { label:T.tabDashboard, icon:"📊", comp:<Dashboard txns={txns} activities={activities} currency={currency} T={T} /> },
    { label:T.tabFinance,   icon:"💰", comp:<Finance txns={txns} setTxns={setTxns} currency={currency} setCurrency={setCurrency} T={T} /> },
    { label:T.tabCalendar,  icon:"📅", comp:<CalendarTab activities={activities} setActivities={setActivities} T={T} /> },
    { label:T.tabSettings,  icon:"⚙️", comp:<Settings txns={txns} activities={activities} setTxns={setTxns} setActivities={setActivities} theme={theme} setTheme={setTheme} lang={lang} setLang={setLang} T={T} /> },
  ];

  return (
    <div style={{ minHeight:"100vh", background:AppColors.desk, display:"flex", alignItems:"center", justifyContent:"center", padding:"20px 0", fontFamily:"-apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif" }}>
      {/* Phone frame */}
      <div style={{ width:390, height:844, background:AppColors.surface, borderRadius:50, overflow:"hidden", display:"flex", flexDirection:"column", position:"relative", boxShadow:"0 0 0 10px #111827, 0 30px 80px rgba(0,0,0,0.5)" }}>
        {/* Status bar */}
        <div style={{ background:screen==="splash"?G[600]:AppColors.surface, padding:"12px 24px 6px", display:"flex", justifyContent:"space-between", alignItems:"center", flexShrink:0, position:"relative" }}>
          <span style={{ fontSize:12, fontWeight:600, color:screen==="splash"?"#fff":GR[800], zIndex:1 }}>9:41</span>
          <div style={{ width:110, height:28, background:"#111827", borderRadius:14, position:"absolute", left:"50%", transform:"translateX(-50%)", top:10, zIndex:2 }} />
          <span style={{ fontSize:12, color:screen==="splash"?"#fff":GR[800], zIndex:1 }}>▲▲▲</span>
        </div>

        {screen==="splash"  && <Splash onDone={()=>setScreen("onboard")} T={T} />}
        {screen==="onboard" && <Onboarding onDone={()=>setScreen("main")} T={T} />}

        {screen==="main" && (
          <>
            <div style={{ background:AppColors.surface, padding:"8px 16px 10px", borderBottom:`1px solid ${GR[200]}`, flexShrink:0 }}>
              <span style={{ fontSize:17, fontWeight:700, color:GR[800] }}>{tabs[tab].icon} {tabs[tab].label}</span>
            </div>
            <div style={{ flex:1, overflow:"hidden", position:"relative", display:"flex", flexDirection:"column" }}>
              {tabs[tab].comp}
            </div>
            {/* Tab bar */}
            <div style={{ display:"flex", background:AppColors.surface, borderTop:`1px solid ${GR[200]}`, paddingBottom:16, flexShrink:0, zIndex:20 }}>
              {tabs.map((t,i) => (
                <button key={i} onClick={()=>setTab(i)}
                  style={{ flex:1, display:"flex", flexDirection:"column", alignItems:"center", padding:"8px 0 4px", background:"none", border:"none", cursor:"pointer", gap:3, fontFamily:"inherit" }}>
                  <span style={{ fontSize:20 }}>{t.icon}</span>
                  <span style={{ fontSize:9, color:i===tab?G[600]:GR[400], fontWeight:i===tab?700:400 }}>{t.label}</span>
                </button>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
