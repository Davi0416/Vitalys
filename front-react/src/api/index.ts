export const API = 'https://vitalys-gc27.onrender.com/vitalys';

export function getToken(): string | null {
  return localStorage.getItem('vitalys-token');
}

export function setToken(token: string): void {
  localStorage.setItem('vitalys-token', token);
}

export function removeToken(): void {
  localStorage.removeItem('vitalys-token');
}

export async function apiFetch(
  url: string,
  options: RequestInit = {}
): Promise<Response | null> {
  const token = getToken();
  const res = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers as Record<string, string> | undefined ?? {}),
    },
  });
  if (res.status === 401 || res.status === 403) {
    removeToken();
    window.location.href = '/login';
    return null;
  }
  return res;
}

export function getNomeUsuario(): string {
  try {
    const token = getToken();
    if (!token) return '';
    const payload = JSON.parse(atob(token.split('.')[1])) as Record<string, unknown>;
    const login = String(payload.sub ?? payload.name ?? payload.login ?? '');
    const primeiro = login.split(/[._@\s]/)[0];
    return primeiro.charAt(0).toUpperCase() + primeiro.slice(1).toLowerCase();
  } catch {
    return '';
  }
}
