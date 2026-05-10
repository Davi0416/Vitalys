import { createContext, useContext, useState, useCallback } from 'react';
import { apiFetch, API } from '../api';

const DataContext = createContext();

export function DataProvider({ children }) {
  const [pacientes, setPacientes]         = useState([]);
  const [profissionais, setProfissionais] = useState([]);
  const [agendamentos, setAgendamentos]   = useState([]);

  const carregarPacientes = useCallback(async () => {
    const res = await apiFetch(`${API}/pacientes`);
    if (res?.ok) setPacientes(await res.json());
  }, []);

  const carregarProfissionais = useCallback(async () => {
    const res = await apiFetch(`${API}/profissionais`);
    if (res?.ok) setProfissionais(await res.json());
  }, []);

  const carregarAgendamentos = useCallback(async () => {
    const res = await apiFetch(`${API}/atendimentos`);
    if (res?.ok) setAgendamentos(await res.json());
  }, []);

  const nomePaciente = (id) => {
    const p = pacientes.find(p => String(p.id) === String(id));
    return p ? p.nome : `Paciente #${id}`;
  };

  const nomeProfissional = (id) => {
    const p = profissionais.find(p => String(p.id) === String(id));
    return p ? p.nome : `Profissional #${id}`;
  };

  return (
    <DataContext.Provider value={{
      pacientes, profissionais, agendamentos,
      carregarPacientes, carregarProfissionais, carregarAgendamentos,
      nomePaciente, nomeProfissional,
    }}>
      {children}
    </DataContext.Provider>
  );
}

export function useData() {
  return useContext(DataContext);
}
