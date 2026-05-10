import { createContext, useContext, useState } from 'react';
import { getToken, setToken, removeToken } from '../api';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setTokenState] = useState(getToken);

  function login(t) {
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

export function useAuth() {
  return useContext(AuthContext);
}
