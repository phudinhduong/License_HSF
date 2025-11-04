const PAGE_SIZE = 10;

const subTitle = document.getElementById('subTitle');
const tbody    = document.getElementById('tbody');
const pageInfo = document.getElementById('pageInfo');
const debugEl  = document.getElementById('debug');

const btnFirst = document.getElementById('btnFirst');
const btnPrev  = document.getElementById('btnPrev');
const btnNext  = document.getElementById('btnNext');
const btnLast  = document.getElementById('btnLast');

const state = {
  productId: null,
  productCode: null,
  productName: null,
  page: 0, size: PAGE_SIZE,
  totalPages: 0, totalElements: 0
};

function escapeHtml(s) {
  return String(s ?? "")
    .replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;")
    .replaceAll('"',"&quot;").replaceAll("'","&#039;");
}
function fmtDate(v) { try { return v ? new Date(v).toLocaleString() : ""; } catch { return String(v||""); } }
function fmtInt(n) { const x = Number(n); return Number.isFinite(x) ? x.toLocaleString() : (n ?? ""); }
function humanDuration(days) {
  if (!days || Number(days) === 0) return "Perpetual";
  const d = Number(days);
  if (d % 365 === 0) return (d/365) + " year(s)";
  if (d % 30  === 0) return (d/30)  + " month(s)";
  if (d % 7   === 0) return (d/7)   + " week(s)";
  return d + " day(s)";
}
function pushUrl() {
  const usp = new URLSearchParams();
  if (state.productId)   usp.set('productId', state.productId);
  if (state.productCode) usp.set('productCode', state.productCode);
  if (state.productName) usp.set('productName', state.productName);
  if (state.page)        usp.set('page', String(state.page));
  history.replaceState({}, '', location.pathname + (usp.toString() ? ('?' + usp.toString()) : ''));
}
function setSubtitle() {
  const parts = [];
  if (state.productCode) parts.push(state.productCode);
  if (state.productName) parts.push(state.productName);
  if (state.productId)   parts.push(`(${state.productId})`);
  subTitle.textContent = parts.join(' — ');
}

async function fetchPlans() {
  const params = new URLSearchParams();
  // yêu cầu productId từ FE
  if (state.productId) params.set("productId", state.productId);
  params.set("page", state.page);
  params.set("size", state.size);

  const base = (typeof API_BASE !== 'undefined' ? API_BASE : '');
  const url = base + "/api/plans?" + params.toString();

  const headers = {};
  if (typeof getAccess === 'function') {
    const acc = getAccess();
    if (acc) headers["Authorization"] = acc.header;
  }

  tbody.innerHTML = `<tr><td colspan="11" class="muted" style="padding:12px;">Đang tải…</td></tr>`;
  try {
    const resp = await fetch(url, { headers });
    if (!resp.ok) throw new Error(resp.status + ' ' + resp.statusText);
    const data = await resp.json();
    renderTable(data);
    renderPager(data);
    pushUrl();
    debugEl.textContent = "";
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="11" style="padding:12px; color:#9b2c2c;">Lỗi tải dữ liệu: ${escapeHtml(e?.message || e)}</td></tr>`;
  }
}

function renderTable(pageData) {
  const list = pageData?.content || [];
  if (!list.length) {
    tbody.innerHTML = `<tr><td colspan="11" class="muted" style="padding:12px;">Không có dữ liệu.</td></tr>`;
    return;
  }
  tbody.innerHTML = list.map((p, idx) => `
    <tr>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${state.page * state.size + idx + 1}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(p.code ?? "")}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(p.name ?? "")}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(p.billingType ?? "")}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${fmtInt(p.priceCredits)}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(p.currency ?? "USD")}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(humanDuration(p.durationDays))}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${fmtInt(p.seats)}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${fmtInt(p.concurrentLimitPerAccount)}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${fmtInt(p.deviceLimitPerAccount)}</td>
      <td style="padding:10px; border-bottom:1px solid #e5e7eb;">${escapeHtml(fmtDate(p.createdAt))}</td>
    </tr>
  `).join("");
}

function renderPager(pageData) {
  const number = Number(pageData?.number ?? 0);
  const totalPages = Number(pageData?.totalPages ?? 0);
  const totalElements = Number(pageData?.totalElements ?? 0);

  state.page = number;
  state.totalPages = totalPages;
  state.totalElements = totalElements;

  pageInfo.textContent = totalPages
    ? `Trang ${number + 1} / ${totalPages} (Tổng ${totalElements})`
    : `0 / 0`;

  btnFirst.disabled = number <= 0;
  btnPrev.disabled  = number <= 0;
  btnNext.disabled  = number >= totalPages - 1;
  btnLast.disabled  = number >= totalPages - 1;
}

// Pagination events
btnFirst.addEventListener('click', () => { state.page = 0; fetchPlans(); });
btnPrev .addEventListener('click', () => { if (state.page > 0) { state.page--; fetchPlans(); }});
btnNext .addEventListener('click', () => { if (state.page < state.totalPages - 1) { state.page++; fetchPlans(); }});
btnLast .addEventListener('click', () => { if (state.totalPages > 0) { state.page = state.totalPages - 1; fetchPlans(); }});

// Init after partials loaded
window.partialsReady.then(() => {
  const usp = new URLSearchParams(location.search);
  state.productId   = usp.get('productId');
  state.productCode = usp.get('productCode');
  state.productName = usp.get('productName');

  setSubtitle();

  if (!state.productId) {
    // Nếu BE bắt buộc productId, báo lỗi và dừng
    tbody.innerHTML = `<tr><td colspan="11" style="padding:12px; color:#9b2c2c;">
      Thiếu <code>productId</code>. Vui lòng mở từ trang Sản phẩm (Xem chi tiết).
    </td></tr>`;
    return;
  }

  fetchPlans(); // gọi API backend để load dữ liệu ngay
});
