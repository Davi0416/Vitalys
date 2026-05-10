export const API = 'https://vitalys-gc27.onrender.com/vitalys';

export function getToken() {
  return localStorage.getItem('vitalys-token');
}

export function setToken(token) {
  localStorage.setItem('vitalys-token', token);
}

export function removeToken() {
  localStorage.removeItem('vitalys-token');
}

export async function apiFetch(url, options = {}) {
  const token = getToken();
  const res = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
  });
  if (res.status === 401 || res.status === 403) {
    removeToken();
    window.location.href = '/login';
    return null;
  }
  return res;
}

export function getNomeUsuario() {
  try {
    const token = getToken();
    if (!token) return '';
    const payload = JSON.parse(atob(token.split('.')[1]));
    const login = payload.sub || payload.name || payload.login || '';
    const primeiro = login.split(/[._@\s]/)[0];
    return primeiro.charAt(0).toUpperCase() + primeiro.slice(1).toLowerCase();
  } catch {
    return '';
  }
}
