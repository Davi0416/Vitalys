export interface Paciente {
  id: number;
  nome: string;
  cpf?: string | null;
  email?: string | null;
  telefone?: string | null;
  dataNascimento?: number | null;
  endereco?: string | null;
}

export interface Profissional {
  id: number;
  nome: string;
  cpf?: string | null;
  email?: string | null;
  telefone?: string | null;
  dataNascimento?: number | null;
}

export type StatusAgendamento = 'pendente' | 'confirmado' | 'cancelado';

export interface Agendamento {
  id: number;
  idPaciente: number;
  idProfissional: number;
  dataEHoraMarcadas: number | string;
  status?: StatusAgendamento;
  nomePaciente?: string;
  nomeProfissional?: string;
}
