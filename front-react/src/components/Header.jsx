import { useState, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { getNomeUsuario } from '../api';

const THEMES = [
  { name: 'light', svg: `<circle cx="12" cy="12" r="4"/><line x1="12" y1="2" x2="12" y2="5"/><line x1="12" y1="19" x2="12" y2="22"/><line x1="2" y1="12" x2="5" y2="12"/><line x1="19" y1="12" x2="22" y2="12"/><line x1="4.93" y1="4.93" x2="7.05" y2="7.05"/><line x1="16.95" y1="16.95" x2="19.07" y2="19.07"/><line x1="4.93" y1="19.07" x2="7.05" y2="16.95"/><line x1="16.95" y1="7.05" x2="19.07" y2="4.93"/>` },
  { name: 'system', svg: `<circle cx="12" cy="12" r="9"/><path d="M12 3v18"/><path d="M12 3a9 9 0 0 1 0 18z" fill="currentColor" stroke="none" opacity="0.4"/>` },
  { name: 'dark', svg: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>` },
];

const NAV_LINKS = [
  { to: '/',              label: 'Início',        end: true },
  { to: '/pacientes',     label: 'Pacientes' },
  { to: '/agendamentos',  label: 'Agendamentos' },
  { to: '/profissionais', label: 'Profissionais' },
  { to: '/calendario',    label: 'Calendário' },
];

export default function Header() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [menuAberto, setMenuAberto] = useState(false);
  const [themeIdx, setThemeIdx] = useState(() => {
    const saved = localStorage.getItem('vitalys-theme-preference');
    const i = THEMES.findIndex(t => t.name === saved);
    return i !== -1 ? i : 0;
  });

  useEffect(() => {
    const theme = THEMES[themeIdx];
    if (theme.name === 'system') document.documentElement.removeAttribute('data-theme');
    else document.documentElement.setAttribute('data-theme', theme.name);
    localStorage.setItem('vitalys-theme-preference', theme.name);
  }, [themeIdx]);

  const nomeUsuario = getNomeUsuario();
  const avatarLetra = nomeUsuario ? nomeUsuario[0].toUpperCase() : 'V';
  const theme = THEMES[themeIdx];

  return (
    <header>
      <img src="/img/logo-vitalys-light.png" className="logoClinica" alt="Logo Vitalys" />

      <nav className={`menuNavegacao${menuAberto ? ' aberto' : ''}`}>
        {NAV_LINKS.map(({ to, label, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) => isActive ? 'active' : ''}
            onClick={() => setMenuAberto(false)}
          >
            {label}
          </NavLink>
        ))}
      </nav>

      <div className="header-right">
        <button
          className="botaoTema"
          title="Alternar tema"
          onClick={() => setThemeIdx(i => (i + 1) % THEMES.length)}
        >
          <svg
            viewBox="0 0 24 24" fill="none" stroke="currentColor"
            strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
            dangerouslySetInnerHTML={{ __html: theme.svg }}
          />
        </button>

        <div
          className="user-avatar"
          title={nomeUsuario || 'Usuário'}
          onClick={() => { logout(); navigate('/login'); }}
          style={{ cursor: 'pointer' }}
        >
          {avatarLetra}
        </div>

        <button
          className={`btn-hamburger${menuAberto ? ' aberto' : ''}`}
          onClick={() => setMenuAberto(v => !v)}
        >
          <span /><span /><span />
        </button>
      </div>
    </header>
  );
}
