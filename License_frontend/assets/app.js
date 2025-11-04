// ============ CONFIG ============
const API_BASE = "http://localhost:8080"; // đổi nếu FE/BE khác origin
const PROFILE_ENDPOINTS = ["/api/auth/me", "/api/users/me", "/api/me", "/api/profile"];

// ============ Helpers chung ============
function getAccess() {
  const token = localStorage.getItem('accessToken');
  const type  = localStorage.getItem('tokenType') || 'Bearer';
  return token ? { header: `${type} ${token}`, token } : null;
}
async function apiGet(url, headers = {}) {
  const r = await fetch(API_BASE + url, { headers });
  if (!r.ok) throw new Error(`${r.status} ${r.statusText}`);
  return r.json().catch(() => ({}));
}
async function tryGetProfile() {
  const access = getAccess();
  if (!access) return null;
  for (const ep of PROFILE_ENDPOINTS) {
    try {
      const data = await apiGet(ep, { 'Authorization': access.header });
      const m = mapProfile(data);
      if (m) return m;
    } catch (_) {}
  }
  return null;
}
function mapProfile(data) {
  if (!data || typeof data !== 'object') return null;
  const name = data.name || data.fullName || data.username || data.displayName || data.email || null;
  const email = data.email || data.mail || null;
  const avatarUrl = data.avatarUrl || data.avatar || null;
  if (!name && !email && !avatarUrl) return null;
  return { name, email, avatarUrl };
}
function initialsFrom(text) {
  if (!text) return "U";
  const parts = String(text).trim().split(/\s+/);
  if (parts.length === 1) return parts[0].slice(0,2).toUpperCase();
  return (parts[0][0] + parts[parts.length-1][0]).toUpperCase();
}
function logout() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('tokenType');
  localStorage.removeItem('accessExpAt');
  window.location.href = '/login.html';
}

// ============ Khởi tạo TOPBAR trong phạm vi node ============
function initTopbar(root) {
  const btnLogin  = root.querySelector('#btnLogin');
  const userMenu  = root.querySelector('#userMenu');
  const dropdown  = root.querySelector('#dropdown');
  const ddProfile = root.querySelector('#ddProfile');
  const ddLogout  = root.querySelector('#ddLogout');
  const avatarBox = root.querySelector('#avatarBox');
  const userLabel = root.querySelector('#userLabel');

  function setAvatar({ name, email, avatarUrl } = {}) {
    if (!avatarBox || !userLabel) return;
    avatarBox.innerHTML = "";
    if (avatarUrl) {
      const img = document.createElement('img');
      img.src = avatarUrl; img.alt = name || email || "avatar";
      avatarBox.appendChild(img);
    } else {
      avatarBox.textContent = initialsFrom(name || email || "U");
    }
    userLabel.textContent = name || email || "Tài khoản";
  }
  function showLoggedOut() {
    btnLogin && (btnLogin.style.display = "inline-flex");
    userMenu && (userMenu.style.display = "none");
    dropdown && dropdown.classList.remove('open');
  }
  function showLoggedIn(profile) {
    btnLogin && (btnLogin.style.display = "none");
    userMenu && (userMenu.style.display = "flex");
    setAvatar(profile || {});
  }

  // Events
  btnLogin?.addEventListener('click', () => { window.location.href = '/login.html'; });
  userMenu?.addEventListener('click', (e) => {
    e.stopPropagation();
    dropdown?.classList.toggle('open');
    dropdown?.setAttribute('aria-hidden', dropdown.classList.contains('open') ? 'false' : 'true');
  });
  document.addEventListener('click', () => dropdown?.classList.remove('open'));
  ddProfile?.addEventListener('click', () => { window.location.href = '/profile.html'; });
  ddLogout?.addEventListener('click', logout);

  // Init state
  const access = getAccess();
  if (!access) { showLoggedOut(); return; }
  tryGetProfile().then(p => showLoggedIn(p)).catch(() => showLoggedOut());
}

// ============ Khởi tạo SIDEBAR trong phạm vi node ============
function initSidebar(root) {
  // Đánh dấu link active theo pathname
  const path = location.pathname.replace(/\/+$/, "") || "/";
  root.querySelectorAll('a.nav-link[href]').forEach(a => {
    const href = (a.getAttribute('href') || "").replace(/\/+$/, "") || "/";
    if (href === path || (href !== "/" && path.endsWith(href))) {
      a.classList.add('active');
      // mở group nếu có toggle
      const group = a.closest('.group');
      if (group) {
        group.classList.add('open');
        const toggle = group.querySelector('.menu-toggle');
        toggle && toggle.setAttribute('aria-expanded', 'true');
      }
    }
  });

  // Hỗ trợ toggle nhóm (nếu có)
  root.querySelectorAll('.menu-toggle').forEach(btn => {
    btn.addEventListener('click', () => {
      const li = btn.closest('.group');
      const expanded = btn.getAttribute('aria-expanded') === 'true';
      btn.setAttribute('aria-expanded', String(!expanded));
      li && li.classList.toggle('open', !expanded);
    });
  });
}

// ============ Nạp partials ============
async function includePartials() {
  const nodes = document.querySelectorAll('[data-include]');
  for (const node of nodes) {
    const url = node.getAttribute('data-include');
    if (!url) continue;
    const res = await fetch(url);
    node.innerHTML = await res.text();

    if (node.classList.contains('topbar'))  initTopbar(node);
    if (node.classList.contains('sidebar')) initSidebar(node);
  }
}

// Promise để các trang con có thể chờ partials sẵn sàng
window.partialsReady = new Promise(resolve => {
  document.addEventListener('DOMContentLoaded', async () => {
    await includePartials();
    resolve();
  });
});
