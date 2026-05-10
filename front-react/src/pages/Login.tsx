import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { API } from '../api';
import '../login.css';

interface Theme {
  name: string;
  svg: string;
}

const THEMES: Theme[] = [
  { name: 'light', svg: `<circle cx="12" cy="12" r="4"/><line x1="12" y1="2" x2="12" y2="5"/><line x1="12" y1="19" x2="12" y2="22"/><line x1="2" y1="12" x2="5" y2="12"/><line x1="19" y1="12" x2="22" y2="12"/><line x1="4.93" y1="4.93" x2="7.05" y2="7.05"/><line x1="16.95" y1="16.95" x2="19.07" y2="19.07"/><line x1="4.93" y1="19.07" x2="7.05" y2="16.95"/><line x1="16.95" y1="7.05" x2="19.07" y2="4.93"/>` },
  { name: 'system', svg: `<circle cx="12" cy="12" r="9"/><path d="M12 3v18"/><path d="M12 3a9 9 0 0 1 0 18z" fill="currentColor" stroke="none" opacity="0.4"/>` },
  { name: 'dark', svg: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>` },
];

export default function Login() {
  const { login, token } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ login: '', password: '' });
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);
  const [themeIdx, setThemeIdx] = useState(() => {
    const saved = localStorage.getItem('vitalys-theme-preference');
    const i = THEMES.findIndex(t => t.name === saved);
    return i !== -1 ? i : 0;
  });

  useEffect(() => {
    document.body.classList.add('login-page');
    return () => document.body.classList.remove('login-page');
  }, []);

  useEffect(() => {
    if (token) navigate('/', { replace: true });
  }, [token, navigate]);

  useEffect(() => {
    const theme = THEMES[themeIdx];
    if (theme.name === 'system') document.documentElement.removeAttribute('data-theme');
    else document.documentElement.setAttribute('data-theme', theme.name);
    localStorage.setItem('vitalys-theme-preference', theme.name);
  }, [themeIdx]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro('');
    setLoading(true);
    try {
      const res = await fetch(`${API}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login: form.login, senha: form.password }),
      });
      if (res.ok) {
        const tokenStr = await res.text();
        login(tokenStr);
        navigate('/', { replace: true });
      } else {
        setErro('Usuário ou senha incorretos.');
      }
    } catch {
      setErro('Não foi possível conectar ao servidor.');
    } finally {
      setLoading(false);
    }
  }

  const theme = THEMES[themeIdx];

  return (
    <>
      <div className="login-brand-strip">
        <img src="/img/logo-vitalys-light.png" className="login-logo-img" alt="Vitalys" />
        <span className="login-brand-desc">Portal interno · clínicas multidisciplinares</span>
        <button
          className="botaoTema"
          title="Alternar tema"
          onClick={() => setThemeIdx(i => (i + 1) % THEMES.length)}
        >
          <svg
            id="theme-icon"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
            dangerouslySetInnerHTML={{ __html: theme.svg }}
          />
        </button>
      </div>

      <main>
        <section className="login">
          <div className="login-brand-icon">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
              <path d="M3 12h4l2-5 3 10 2-5h7" stroke="white" strokeWidth="2.4"
                strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </div>

          <h1>Bem-vindo de volta</h1>
          <span className="login-subtitle">Entre com suas credenciais para acessar o portal.</span>

          <form onSubmit={handleSubmit}>
            <div className="field-group">
              <label htmlFor="login">Usuário</label>
              <div className="field-wrap">
                <svg className="field-icon" width="16" height="16" viewBox="0 0 24 24" fill="none"
                  stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <circle cx="12" cy="8" r="4" /><path d="M4 21c0-4 4-7 8-7s8 3 8 7" />
                </svg>
                <input
                  type="text" id="login" placeholder="seu.usuario" required
                  autoComplete="username"
                  value={form.login}
                  onChange={e => setForm(f => ({ ...f, login: e.target.value }))}
                />
              </div>
            </div>

            <div className="field-group">
              <div className="field-label-row">
                <label htmlFor="password">Senha</label>
                <a href="#" className="link-forgot">Esqueci minha senha</a>
              </div>
              <div className="field-wrap">
                <svg className="field-icon" width="16" height="16" viewBox="0 0 24 24" fill="none"
                  stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="4" y="10" width="16" height="11" rx="2" /><path d="M8 10V7a4 4 0 0 1 8 0v3" />
                </svg>
                <input
                  type="password" id="password" placeholder="••••••••" required
                  autoComplete="current-password"
                  value={form.password}
                  onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
                />
              </div>
            </div>

            <p id="msgErroLogin">{erro}</p>

            <button type="submit" className="botaoEntrar" disabled={loading}>
              {loading ? 'Entrando...' : 'Entrar no portal'}
              {!loading && (
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none"
                  stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M5 12h14M13 5l7 7-7 7" />
                </svg>
              )}
            </button>
          </form>

          <div className="login-card-footer">
            Problemas para acessar? <a href="#">Fale com o suporte</a>
          </div>
        </section>
      </main>

      <div className="login-copyright">
        © 2026 Vitalys · Dados protegidos por criptografia ponta a ponta
      </div>
    </>
  );
}
