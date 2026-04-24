import { useState, useEffect } from "react";

const KM = {
  appName: "SmartFarm",
  tagline: "កសិកម្មឆ្លាតវៃ ងាយស្រួល",
  next: "បន្ទាប់",
  getStarted: "ចាប់ផ្តើម",
  tabFinance: "ហិរញ្ញវត្ថុ",
  tabCalendar: "ប្រតិទិន",
  tabGuide: "មគ្គុទ្ទេសក៍",
  tabJournal: "កំណត់ហេតុ",
  tabSettings: "ការកំណត់",
  all: "ទាំងអស់",
  income: "ចំណូល",
  expense: "ចំណាយ",
  balance: "សមតុល្យ",
  addTransaction: "បន្ថែមប្រតិបត្តិការ",
  amount: "ចំនួនទឹកប្រាក់",
  category: "ប្រភេទ",
  note: "កំណត់ចំណាំ",
  save: "រក្សាទុក",
  cancel: "បោះបង់",
  delete: "លុប",
  search: "ស្វែងរក...",
  addActivity: "បន្ថែមសកម្មភាព",
  activityName: "ឈ្មោះសកម្មភាព",
  noData: "មិនមានទិន្នន័យ",
  addEntry: "បន្ថែមកំណត់ហេតុ",
  content: "មាតិកា",
  weather: "អាកាសធាតុ",
  symptoms: "រោគសញ្ញា",
  treatment: "វិធីព្យាបាល",
  prevention: "វិធីការពារ",
  insects: "សត្វល្អិត",
  fungal: "ផ្សិត",
  bacterial: "បាក់តេរី",
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
  journalEntries: "កំណត់ហេតុ",
  preferences: "ចំណូលចិត្ត",
  theme: "រចនាបថ",
  themeLight: "ភ្លឺ",
  themeDark: "ងងឹត",
  categories: ["ជី","ពូជ","ការងារ","ឧបករណ៍","លក់"],
  months: ["មករា","កុម្ភៈ","មីនា","មេសា","ឧសភា","មិថុនា","កក្កដា","សីហា","កញ្ញា","តុលា","វិច្ឆិកា","ធ្នូ"],
  days: ["អា","ច","អ","ព","ព្រ","សុ","ស"],
  onboardPages: [
    { icon:"💰", title:"តាមដានហិរញ្ញវត្ថុ", sub:"កត់ត្រាចំណូល និងចំណាយជា រៀល ឬ ដុល្លារ" },
    { icon:"📅", title:"គ្រោងសកម្មភាព", sub:"កុំភ្លេចការដាំ ឬ ប្រមូលផល ជាមួយការជូនដំណឹង" },
    { icon:"📊", title:"រីកចម្រើនជាមួយទិន្នន័យ", sub:"របាយការណ៍ប្រចាំខែជួយសម្រេចចិត្ត" },
  ],
};

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
const INITIAL_TXNS = [
  { id:1, type:"income",  amount:250000, currency:"KHR", category:"លក់",    note:"លក់ស្រូវ",      date:"2024-04-10" },
  { id:2, type:"expense", amount:45000,  currency:"KHR", category:"ជី",     note:"ទិញជីគីមី",      date:"2024-04-08" },
  { id:3, type:"expense", amount:30000,  currency:"KHR", category:"ការងារ", note:"ប្រាក់ខែកម្មករ", date:"2024-04-06" },
  { id:4, type:"income",  amount:180000, currency:"KHR", category:"លក់",    note:"លក់បន្លែ",      date:"2024-04-03" },
  { id:5, type:"expense", amount:15000,  currency:"KHR", category:"ពូជ",    note:"ទិញពូជស្រូវ",    date:"2024-04-01" },
];
const INITIAL_ACTS = [
  { id:1, name:"ដាំស្រូវ",  date:"2024-04-15", type:"ដំណាំ",  done:false },
  { id:2, name:"ស្រោចទឹក", date:"2024-04-12", type:"ថែទាំ",  done:true  },
  { id:3, name:"បាញ់ថ្នាំ", date:"2024-04-18", type:"ការពារ", done:false },
  { id:4, name:"ប្រមូលផល", date:"2024-04-25", type:"ប្រមូល", done:false },
];
const INITIAL_ENTRIES = [
  { id:1, date:"2024-04-11", weather:"☀️", content:"ថ្ងៃនេះ ដាំស្រូវ ក្នុងផ្លូវ ១ ហ្គិចដ ទឹកហូរ ល្អ ។" },
  { id:2, date:"2024-04-09", weather:"🌧️", content:"ភ្លៀង ៣ ម៉ោង ដំណើរការ ល្អ ។ ស្រូវ ងើបឡើង ។" },
  { id:3, date:"2024-04-07", weather:"⛅", content:"បន្ថែមជី ម្ដង ទៀត ។ ស្លឹក ក្រហម ខ្លះ ត្រូវ ប្រុងប្រយ័ត្ន ។" },
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
    try { localStorage.setItem(key, JSON.stringify(value)); } catch {}
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
const textareaStyle = {
  ...inputStyle, resize:"none", lineHeight:1.6,
};

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

function ActionRow({ onCancel, onSave }) {
  return (
    <div style={{ display:"flex", gap:8, marginTop:4 }}>
      <button onClick={onCancel} style={{ flex:1, padding:"12px", borderRadius:12, border:`1px solid ${GR[200]}`, background:AppColors.surface, color:GR[600], fontSize:14, cursor:"pointer", fontFamily:"inherit" }}>{KM.cancel}</button>
      <button onClick={onSave}   style={{ flex:1, padding:"12px", borderRadius:12, border:"none", background:G[600], color:"#fff", fontSize:14, fontWeight:600, cursor:"pointer", fontFamily:"inherit" }}>{KM.save}</button>
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
function Splash({ onDone }) {
  const [vis, setVis] = useState(false);
  useEffect(() => {
    const t1 = setTimeout(() => setVis(true), 100);
    const t2 = setTimeout(() => onDone(), 2400);
    return () => { clearTimeout(t1); clearTimeout(t2); };
  }, []);
  return (
    <div style={{ flex:1, background:G[600], display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", gap:20 }}>
      <div style={{ fontSize:72, lineHeight:1, height:88, display:"flex", alignItems:"center", justifyContent:"center", transform:vis?"scale(1)":"scale(0.4)", opacity:vis?1:0, transition:"all 0.65s cubic-bezier(.34,1.56,.64,1)" }}>🌱</div>
      <div style={{ color:"#fff", fontSize:28, fontWeight:700, lineHeight:1.3, opacity:vis?1:0, transition:"opacity 0.5s 0.3s", fontFamily:"inherit" }}>{KM.appName}</div>
      <div style={{ color:"rgba(255,255,255,0.85)", fontSize:14, lineHeight:1.5, opacity:vis?1:0, transition:"opacity 0.5s 0.5s", fontFamily:"inherit" }}>{KM.tagline}</div>
    </div>
  );
}

// ── ONBOARDING ──────────────────────────────────────────────────
function Onboarding({ onDone }) {
  const [page, setPage] = useState(0);
  const p = KM.onboardPages[page];
  return (
    <div style={{ flex:1, display:"flex", flexDirection:"column", padding:"32px 24px 24px", background:AppColors.surface }}>
      <div style={{ flex:1, display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", gap:24, textAlign:"center" }}>
        <div style={{ fontSize:80, lineHeight:1, height:96, display:"flex", alignItems:"center", justifyContent:"center" }}>{p.icon}</div>
        <div style={{ fontSize:22, fontWeight:700, color:GR[800], lineHeight:1.4, maxWidth:280 }}>{p.title}</div>
        <div style={{ fontSize:14, color:GR[500], lineHeight:1.75, maxWidth:300 }}>{p.sub}</div>
      </div>
      <div style={{ display:"flex", justifyContent:"center", gap:8, marginBottom:24 }}>
        {KM.onboardPages.map((_,i) => (
          <div key={i} style={{ width:i===page?22:8, height:8, borderRadius:4, background:i===page?G[600]:GR[200], transition:"all .3s" }} />
        ))}
      </div>
      <button onClick={() => page < 2 ? setPage(p=>p+1) : onDone()}
        style={{ background:G[600], color:"#fff", border:"none", borderRadius:14, padding:"14px", fontSize:16, fontWeight:600, cursor:"pointer", fontFamily:"inherit" }}>
        {page < 2 ? KM.next : KM.getStarted}
      </button>
    </div>
  );
}

// ── FINANCE ─────────────────────────────────────────────────────
function Finance({ txns, setTxns, currency, setCurrency }) {
  const [filter, setFilter] = useState("all");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ type:"income", amount:"", category:"លក់", note:"" });

  const fmt = (n) => formatMoney(n, currency);
  const totalIncome  = txns.filter(t=>t.type==="income").reduce((s,t)=>s+t.amount,0);
  const totalExpense = txns.filter(t=>t.type==="expense").reduce((s,t)=>s+t.amount,0);
  const balance = totalIncome - totalExpense;
  const filtered = txns.filter(t => filter==="all" || t.type===filter);

  function addTxn() {
    if (!form.amount) return;
    setTxns(p => [...p, { id:Date.now(), type:form.type, amount:Number(form.amount), currency:"KHR", category:form.category, note:form.note || "—", date:new Date().toISOString().slice(0,10) }]);
    setShowForm(false);
    setForm({ type:"income", amount:"", category:"លក់", note:"" });
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], position:"relative" }}>
      {/* Balance card */}
      <div style={{ margin:"12px 12px 8px", background:AppColors.surface, borderRadius:16, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
        <div style={{ padding:"14px 16px 10px", borderBottom:`1px solid ${GR[100]}`, display:"flex", justifyContent:"space-between", alignItems:"center" }}>
          <div>
            <div style={{ fontSize:11, color:GR[400], marginBottom:3 }}>{KM.balance}</div>
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
          {[[G[600],KM.income,totalIncome],[DANGER.main,KM.expense,totalExpense]].map(([col,label,val],i) => (
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

      {/* Filter chips */}
      <div style={{ display:"flex", gap:6, padding:"0 12px 10px" }}>
        {[["all",KM.all],["income",KM.income],["expense",KM.expense]].map(([k,l]) => (
          <button key={k} onClick={()=>setFilter(k)}
            style={{ fontSize:12, padding:"5px 12px", borderRadius:20, border:`1px solid ${filter===k?G[600]:GR[200]}`, background:filter===k?G[100]:AppColors.surface, color:filter===k?G[700]:GR[600], cursor:"pointer", fontFamily:"inherit" }}>{l}</button>
        ))}
      </div>

      {/* Transaction list */}
      <div style={{ padding:"0 12px", display:"flex", flexDirection:"column", gap:8 }}>
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
        <BottomSheet onClose={()=>setShowForm(false)} title={KM.addTransaction}>
          <div style={{ display:"flex", gap:8 }}>
            {[["income",KM.income],["expense",KM.expense]].map(([k,l]) => (
              <button key={k} onClick={()=>setForm(f=>({...f,type:k}))}
                style={{ flex:1, padding:"9px", borderRadius:10, border:`1.5px solid ${form.type===k?G[600]:GR[200]}`, background:form.type===k?G[50]:AppColors.surface, color:form.type===k?G[700]:GR[600], fontSize:13, cursor:"pointer", fontFamily:"inherit", fontWeight:form.type===k?600:400 }}>{l}</button>
            ))}
          </div>
          <input placeholder={KM.amount} type="number" value={form.amount} onChange={e=>setForm(f=>({...f,amount:e.target.value}))} style={inputStyle} />
          <select value={form.category} onChange={e=>setForm(f=>({...f,category:e.target.value}))} style={selectStyle}>
            {KM.categories.map(c=><option key={c}>{c}</option>)}
          </select>
          <input placeholder={KM.note} value={form.note} onChange={e=>setForm(f=>({...f,note:e.target.value}))} style={inputStyle} />
          <ActionRow onCancel={()=>setShowForm(false)} onSave={addTxn} />
        </BottomSheet>
      )}

      <div style={{ height:80 }} />
      <FAB onClick={()=>setShowForm(true)} />
    </div>
  );
}

// ── CALENDAR ────────────────────────────────────────────────────
function CalendarTab({ activities, setActivities }) {
  const today = new Date();
  const [year]  = useState(today.getFullYear());
  const [month] = useState(today.getMonth());
  const [selected, setSelected] = useState(today.getDate());
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name:"", date:"", type:"ដំណាំ" });

  const daysInMonth = new Date(year, month+1, 0).getDate();
  const firstDay    = new Date(year, month, 1).getDay();
  const actDates    = new Set(activities.map(a => new Date(a.date).getDate()));
  const selStr      = `${year}-${String(month+1).padStart(2,"0")}-${String(selected).padStart(2,"0")}`;
  const selActs     = activities.filter(a => a.date === selStr);

  function addActivity() {
    if (!form.name || !form.date) return;
    setActivities(p => [...p, { id:Date.now(), ...form, done:false }]);
    setShowForm(false);
    setForm({ name:"", date:"", type:"ដំណាំ" });
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px", position:"relative" }}>
      {/* Calendar grid */}
      <div style={{ background:AppColors.surface, borderRadius:14, padding:"12px", border:`1px solid ${GR[200]}`, marginBottom:12 }}>
        <div style={{ textAlign:"center", fontWeight:700, color:GR[800], marginBottom:10, fontSize:14 }}>
          {KM.months[month]} {year}
        </div>
        <div style={{ display:"grid", gridTemplateColumns:"repeat(7,1fr)", gap:2, marginBottom:6 }}>
          {KM.days.map(d => <div key={d} style={{ textAlign:"center", fontSize:10, color:GR[400], fontWeight:600 }}>{d}</div>)}
        </div>
        <div style={{ display:"grid", gridTemplateColumns:"repeat(7,1fr)", gap:2 }}>
          {Array(firstDay).fill(null).map((_,i) => <div key={"e"+i} />)}
          {Array(daysInMonth).fill(null).map((_,i) => {
            const d = i+1;
            const isToday = d===today.getDate() && month===today.getMonth();
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

      <div style={{ fontWeight:600, color:GR[700], fontSize:13, marginBottom:8 }}>{KM.months[month]} {selected}</div>
      {selActs.length === 0 && <div style={{ color:GR[400], fontSize:13, textAlign:"center", padding:"20px 0" }}>{KM.noData}</div>}
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

      {showForm && (
        <BottomSheet onClose={()=>setShowForm(false)} title={KM.addActivity}>
          <input placeholder={KM.activityName} value={form.name} onChange={e=>setForm(f=>({...f,name:e.target.value}))} style={inputStyle} />
          <input type="date" value={form.date} onChange={e=>setForm(f=>({...f,date:e.target.value}))} style={inputStyle} />
          <select value={form.type} onChange={e=>setForm(f=>({...f,type:e.target.value}))} style={selectStyle}>
            {["ដំណាំ","ថែទាំ","ការពារ","ប្រមូល"].map(t=><option key={t}>{t}</option>)}
          </select>
          <ActionRow onCancel={()=>setShowForm(false)} onSave={addActivity} />
        </BottomSheet>
      )}

      <div style={{ height:80 }} />
      <FAB onClick={()=>setShowForm(true)} />
    </div>
  );
}

// ── GUIDE ───────────────────────────────────────────────────────
const pests = [
  { id:1, name:"ដង្កូវស្រូវ", cat:"insects",  symptoms:"ស្លឹកលឿង ជ្រួញ",         treatment:"បាញ់ថ្នាំ Chlorpyrifos", prevention:"សំអាតស្មៅ ជុំវិញចំការ" },
  { id:2, name:"ផ្សិតស្លឹក",  cat:"fungal",   symptoms:"មានប្រណំ ខ្មៅ នៅស្លឹក",    treatment:"បាញ់ថ្នាំ Mancozeb",     prevention:"មិនត្រូវស្រោចច្រើន" },
  { id:3, name:"រោគប្លែក",   cat:"bacterial", symptoms:"ស្លឹករោយ ស្លាប",           treatment:"កាប់ចោល ដុត",            prevention:"ប្រើពូជធន់" },
  { id:4, name:"ចៃស្រូវ",    cat:"insects",  symptoms:"ស្លឹកស្ងួត ឡើងក្រហម",     treatment:"ចូរដើរ ចាប់",             prevention:"ដុះស្មៅ គ្របដំណាំ" },
  { id:5, name:"ផ្សិតរាក",   cat:"fungal",   symptoms:"ដើមមាន ក្រហម ធ្លាក់",      treatment:"ព្យាបាលដី Trichoderma",   prevention:"ប្តូរដំណាំ" },
  { id:6, name:"ជំងឺខ្ចី",   cat:"bacterial", symptoms:"ផ្លែស្ងួត មិនធំ",          treatment:"ដករំលោភ ដុត",             prevention:"ស្ទូចត្រឹមត្រូវ" },
  { id:7, name:"ក្រញ៉ូន",    cat:"insects",  symptoms:"ស្លឹកដូច ត្របក",           treatment:"បាញ់ Imidacloprid",       prevention:"ហ្វឹកហាត់ ស្ទូច" },
  { id:8, name:"ផ្សិតស្រូវ", cat:"fungal",   symptoms:"គ្រាប់ស្រូវ ខ្មៅ",         treatment:"ព្យាបាលពូជ ថ្នាំ",         prevention:"ប្រើពូជ ស្អាត" },
];

function Guide() {
  const [cat, setCat]       = useState("all");
  const [query, setQuery]   = useState("");
  const [selected, setSelected] = useState(null);
  const [expanded, setExpanded] = useState({});

  const cats = [["all",KM.all],["insects",KM.insects],["fungal",KM.fungal],["bacterial",KM.bacterial]];
  const filtered = pests.filter(p => (cat==="all"||p.cat===cat) && (!query||p.name.includes(query)));

  if (selected) {
    const sections = [["symptoms",KM.symptoms,selected.symptoms],["treatment",KM.treatment,selected.treatment],["prevention",KM.prevention,selected.prevention]];
    return (
      <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px" }}>
        <button onClick={()=>{ setSelected(null); setExpanded({}); }}
          style={{ background:"none", border:"none", color:G[600], fontSize:14, cursor:"pointer", marginBottom:10, padding:0, fontFamily:"inherit", display:"flex", alignItems:"center", gap:4 }}>← ត្រឡប់</button>
        <div style={{ background:AppColors.surface, borderRadius:14, padding:"16px", border:`1px solid ${GR[200]}`, marginBottom:12 }}>
          <div style={{ fontSize:20, fontWeight:700, color:GR[800], marginBottom:8 }}>{selected.name}</div>
          <span style={{ fontSize:11, background:SEC[50], color:SEC[700], padding:"3px 10px", borderRadius:20 }}>
            {cats.find(c=>c[0]===selected.cat)?.[1]}
          </span>
        </div>
        {sections.map(([key,label,text]) => (
          <div key={key} style={{ background:AppColors.surface, borderRadius:12, marginBottom:8, border:`1px solid ${GR[200]}`, overflow:"hidden" }}>
            <button onClick={()=>setExpanded(e=>({...e,[key]:!e[key]}))}
              style={{ width:"100%", padding:"12px 14px", background:"none", border:"none", display:"flex", justifyContent:"space-between", cursor:"pointer", fontSize:13, fontWeight:600, color:GR[800], fontFamily:"inherit" }}>
              {label} <span style={{ color:GR[400], fontSize:12 }}>{expanded[key]?"▲":"▼"}</span>
            </button>
            {expanded[key] && <div style={{ padding:"0 14px 12px", fontSize:13, color:GR[700], lineHeight:1.7 }}>{text}</div>}
          </div>
        ))}
      </div>
    );
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px" }}>
      <input value={query} onChange={e=>setQuery(e.target.value)} placeholder={KM.search}
        style={{ ...inputStyle, marginBottom:10 }} />
      <div style={{ display:"flex", gap:6, marginBottom:12, overflowX:"auto", paddingBottom:2 }}>
        {cats.map(([k,l]) => (
          <button key={k} onClick={()=>setCat(k)}
            style={{ whiteSpace:"nowrap", fontSize:12, padding:"5px 12px", borderRadius:20, border:`1px solid ${cat===k?G[600]:GR[200]}`, background:cat===k?G[100]:AppColors.surface, color:cat===k?G[700]:GR[600], cursor:"pointer", fontFamily:"inherit" }}>{l}</button>
        ))}
      </div>
      <div style={{ display:"flex", flexDirection:"column", gap:8 }}>
        {filtered.map(p => (
          <div key={p.id} onClick={()=>setSelected(p)}
            style={{ background:AppColors.surface, borderRadius:12, padding:"12px 14px", display:"flex", justifyContent:"space-between", alignItems:"center", border:`1px solid ${GR[200]}`, cursor:"pointer" }}>
            <div>
              <div style={{ fontSize:13, fontWeight:600, color:GR[800] }}>{p.name}</div>
              <div style={{ fontSize:11, color:GR[400], marginTop:2 }}>{cats.find(c=>c[0]===p.cat)?.[1]}</div>
            </div>
            <span style={{ color:GR[400], fontSize:18 }}>›</span>
          </div>
        ))}
      </div>
    </div>
  );
}

// ── JOURNAL ─────────────────────────────────────────────────────
function Journal({ entries, setEntries }) {
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ content:"", weather:"☀️" });
  const weathers = ["☀️","🌧️","⛅","💨"];

  function addEntry() {
    if (!form.content) return;
    setEntries(p => [{ id:Date.now(), date:new Date().toISOString().slice(0,10), ...form }, ...p]);
    setShowForm(false);
    setForm({ content:"", weather:"☀️" });
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px", position:"relative" }}>
      <div style={{ display:"flex", flexDirection:"column", gap:10 }}>
        {entries.map(e => (
          <div key={e.id} style={{ background:AppColors.surface, borderRadius:14, padding:"14px", border:`1px solid ${GR[200]}` }}>
            <div style={{ display:"flex", justifyContent:"space-between", alignItems:"center", marginBottom:8 }}>
              <div style={{ display:"flex", alignItems:"center", gap:8 }}>
                <span style={{ fontSize:22 }}>{e.weather}</span>
                <span style={{ fontSize:12, color:GR[400] }}>{e.date}</span>
              </div>
              <button onClick={()=>setEntries(p=>p.filter(x=>x.id!==e.id))}
                style={{ fontSize:13, color:DANGER.main, background:"none", border:"none", cursor:"pointer" }}>✕</button>
            </div>
            <div style={{ fontSize:13, color:GR[700], lineHeight:1.75 }}>{e.content}</div>
          </div>
        ))}
      </div>

      {showForm && (
        <BottomSheet onClose={()=>setShowForm(false)} title={KM.addEntry}>
          <div>
            <div style={{ fontSize:12, color:GR[600], marginBottom:8 }}>{KM.weather}</div>
            <div style={{ display:"flex", gap:8 }}>
              {weathers.map(w => (
                <button key={w} onClick={()=>setForm(f=>({...f,weather:w}))}
                  style={{ fontSize:22, background:form.weather===w?G[50]:AppColors.surface, border:`1.5px solid ${form.weather===w?G[500]:GR[200]}`, borderRadius:10, padding:"6px 10px", cursor:"pointer", lineHeight:1 }}>{w}</button>
              ))}
            </div>
          </div>
          <textarea placeholder={KM.content} value={form.content} onChange={e=>setForm(f=>({...f,content:e.target.value}))}
            rows={4} style={textareaStyle} />
          <ActionRow onCancel={()=>setShowForm(false)} onSave={addEntry} />
        </BottomSheet>
      )}

      <div style={{ height:80 }} />
      <FAB onClick={()=>setShowForm(true)} />
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

function Settings({ txns, activities, entries, setTxns, setActivities, setEntries, theme, setTheme }) {
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
      txns, activities, entries,
    };
    downloadFile(`smartfarm-backup-${new Date().toISOString().slice(0,10)}.json`, JSON.stringify(data, null, 2), "application/json");
  }

  function onRestoreFile(e) {
    const file = e.target.files?.[0];
    e.target.value = "";
    if (!file) return;
    if (!window.confirm(KM.restoreConfirm)) return;
    const reader = new FileReader();
    reader.onload = (ev) => {
      try {
        const d = JSON.parse(ev.target.result);
        if (Array.isArray(d.txns)) setTxns(d.txns);
        if (Array.isArray(d.activities)) setActivities(d.activities);
        if (Array.isArray(d.entries)) setEntries(d.entries);
        window.alert(KM.restoreSuccess);
      } catch {
        window.alert(KM.restoreFail);
      }
    };
    reader.readAsText(file);
  }

  function clearAll() {
    if (!window.confirm(KM.clearConfirm)) return;
    setTxns([]);
    setActivities([]);
    setEntries([]);
  }

  return (
    <div style={{ flex:1, overflowY:"auto", background:GR[50], padding:"12px" }}>
      {/* Preferences — theme */}
      <SettingsCard icon="🎨" title={KM.preferences}>
        <div style={{ fontSize:11, color:GR[600], fontWeight:600, marginBottom:8 }}>{KM.theme}</div>
        <div style={{ display:"flex", gap:8 }}>
          {[["light",KM.themeLight,"☀️"],["dark",KM.themeDark,"🌙"]].map(([k,l,ic]) => (
            <button key={k} onClick={()=>setTheme(k)}
              style={{
                flex:1, padding:"10px", borderRadius:10,
                border:`1.5px solid ${theme===k?G[600]:GR[200]}`,
                background:theme===k?G[50]:AppColors.surface,
                color:theme===k?G[700]:GR[600],
                fontSize:13, fontWeight:theme===k?700:500,
                cursor:"pointer", fontFamily:"inherit",
                display:"flex", alignItems:"center", justifyContent:"center", gap:6,
              }}>
              <span style={{ fontSize:14 }}>{ic}</span>{l}
            </button>
          ))}
        </div>
      </SettingsCard>

      {/* Data stats */}
      <SettingsCard icon="📦" title={KM.dataStats}>
        <div style={{ display:"flex", gap:8 }}>
          {[
            [txns.length, KM.transactions],
            [activities.length, KM.activities],
            [entries.length, KM.journalEntries],
          ].map(([n, l], i) => (
            <div key={i} style={{ flex:1, padding:"10px", background:GR[50], borderRadius:10, textAlign:"center" }}>
              <div style={{ fontSize:18, fontWeight:700, color:G[700] }}>{n}</div>
              <div style={{ fontSize:10, color:GR[500], marginTop:2 }}>{l}</div>
            </div>
          ))}
        </div>
      </SettingsCard>

      {/* Export */}
      <SettingsCard icon="📤" title={KM.dataExport}>
        <SettingsBtn onClick={exportTxnsCSV} icon="📄" label={KM.exportTxnsCSV} />
      </SettingsCard>

      {/* Backup & Restore */}
      <SettingsCard icon="💾" title={KM.backupRestore}>
        <SettingsBtn onClick={exportBackup} icon="⬇️" label={KM.backupAll} />
        <label style={{
          width:"100%", padding:"11px 12px", borderRadius:10,
          border:`1px solid ${GR[200]}`, background:AppColors.surface, color:GR[700],
          fontSize:13, fontWeight:500, cursor:"pointer", fontFamily:"inherit",
          marginBottom:8, textAlign:"left", display:"flex", alignItems:"center", gap:8,
          boxSizing:"border-box",
        }}>
          <span style={{ fontSize:15 }}>⬆️</span>{KM.restoreFromFile}
          <input type="file" accept="application/json,.json" onChange={onRestoreFile} style={{ display:"none" }} />
        </label>
        <SettingsBtn onClick={clearAll} icon="🗑️" label={KM.clearAll} danger />
      </SettingsCard>

      <div style={{ height:20 }} />
    </div>
  );
}

// ── ROOT ────────────────────────────────────────────────────────
export default function App() {
  const [screen, setScreen] = useState("splash");
  const [tab, setTab] = useState(0);
  const [txns, setTxns] = usePersistedState("sf_txns", INITIAL_TXNS);
  const [activities, setActivities] = usePersistedState("sf_acts", INITIAL_ACTS);
  const [entries, setEntries] = usePersistedState("sf_entries", INITIAL_ENTRIES);
  const [currency, setCurrency] = usePersistedState("sf_currency", "KHR");
  const [theme, setTheme] = usePersistedState("sf_theme", "light");

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
  }, [theme]);

  const tabs = [
    { label:KM.tabFinance,  icon:"💰", comp:<Finance txns={txns} setTxns={setTxns} currency={currency} setCurrency={setCurrency} /> },
    { label:KM.tabCalendar, icon:"📅", comp:<CalendarTab activities={activities} setActivities={setActivities} /> },
    { label:KM.tabGuide,    icon:"📖", comp:<Guide /> },
    { label:KM.tabJournal,  icon:"📓", comp:<Journal entries={entries} setEntries={setEntries} /> },
    { label:KM.tabSettings, icon:"⚙️", comp:<Settings txns={txns} activities={activities} entries={entries} setTxns={setTxns} setActivities={setActivities} setEntries={setEntries} theme={theme} setTheme={setTheme} /> },
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

        {screen==="splash"  && <Splash onDone={()=>setScreen("onboard")} />}
        {screen==="onboard" && <Onboarding onDone={()=>setScreen("main")} />}

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
