import { createContext, useContext, useState, ReactNode } from 'react';
import { getToken, setToken, removeToken } from '../api';

interface AuthContextValue {
  token: string | null;
  login: (t: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setTokenState] = useState<string | null>(getToken);

  function login(t: string) {
    setToken(t);
    setTokenState(t);
  }

  function logout() {
    removeToken();
    setTokenState(null);
  }

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
