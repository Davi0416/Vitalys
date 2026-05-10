import { createContext, useContext, useState, useCallback, ReactNode } from 'react';
import { apiFetch, API } from '../api';
import type { Paciente, Profissional, Agendamento } from '../types';

interface DataContextValue {
  pacientes: Paciente[];
  profissionais: Profissional[];
  agendamentos: Agendamento[];
  carregarPacientes: () => Promise<void>;
  carregarProfissionais: () => Promise<void>;
  carregarAgendamentos: () => Promise<void>;
  nomePaciente: (id: number | string) => string;
  nomeProfissional: (id: number | string) => string;
}

const DataContext = createContext<DataContextValue | undefined>(undefined);

export function DataProvider({ children }: { children: ReactNode }) {
  const [pacientes, setPacientes]           = useState<Paciente[]>([]);
  const [profissionais, setProfissionais]   = useState<Profissional[]>([]);
  const [agendamentos, setAgendamentos]     = useState<Agendamento[]>([]);

  const carregarPacientes = useCallback(async () => {
    const res = await apiFetch(`${API}/pacientes`);
    if (res?.ok) setPacientes(await res.json() as Paciente[]);
  }, []);

  const carregarProfissionais = useCallback(async () => {
    const res = await apiFetch(`${API}/profissionais`);
    if (res?.ok) setProfissionais(await res.json() as Profissional[]);
  }, []);

  const carregarAgendamentos = useCallback(async () => {
    const res = await apiFetch(`${API}/atendimentos`);
    if (res?.ok) setAgendamentos(await res.json() as Agendamento[]);
  }, []);

  const nomePaciente = (id: number | string): string => {
    const p = pacientes.find(p => String(p.id) === String(id));
    return p ? p.nome : `Paciente #${id}`;
  };

  const nomeProfissional = (id: number | string): string => {
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

export function useData(): DataContextValue {
  const ctx = useContext(DataContext);
  if (!ctx) throw new Error('useData must be used within DataProvider');
  return ctx;
}
