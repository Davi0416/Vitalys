import { useEffect, useState, type ReactNode } from 'react';
import { useData } from '../contexts/DataContext';
import { apiFetch, API } from '../api';
import { iniciais, formatarData, dataParaBackend, dataParaInput, mascaraCpf, mascaraTelefone } from '../utils';
import type { Paciente } from '../types';
import Modal from './Modal';

export default function Pacientes() {
  const { pacientes, carregarPacientes } = useData();
  const [busca, setBusca]     = useState('');
  const [modal, setModal]     = useState(false);
  const [editando, setEditando] = useState<Paciente | null>(null);

  useEffect(() => { void carregarPacientes(); }, []);

  const lista = pacientes.filter(p => p.nome.toLowerCase().includes(busca.toLowerCase()));

  async function remover(id: number) {
    if (!confirm('Remover este paciente?')) return;
    await apiFetch(`${API}/pacientes/${id}`, { method: 'DELETE' });
    void carregarPacientes();
  }

  function abrirNovo()          { setEditando(null); setModal(true); }
  function abrirEditar(p: Paciente) { setEditando(p); setModal(true); }

  return (
    <div>
      <div className="toolbar">
        <div className="busca">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"
            strokeLinecap="round" strokeLinejoin="round" className="icone-busca">
            <circle cx="11" cy="11" r="8" /><line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input
            type="text" placeholder="Buscar paciente pelo nome..."
            value={busca} onChange={e => setBusca(e.target.value)}
          />
        </div>
        <button className="btn-novo" onClick={abrirNovo}>+ Novo</button>
      </div>

      <div className="lista">
        {lista.length === 0 ? (
          <p className="lista-vazia">Nenhum paciente encontrado.</p>
        ) : lista.map(p => (
          <div key={p.id} className="item">
            <div className="item-avatar">{iniciais(p.nome)}</div>
            <div className="item-info">
              <span className="item-nome">{p.nome}</span>
              <span className="item-detalhe">
                CPF: {p.cpf ?? '-'} &nbsp;|&nbsp; Tel: {p.telefone ?? '-'} &nbsp;|&nbsp; Nasc: {formatarData(p.dataNascimento)}
              </span>
            </div>
            <div className="item-acoes">
              <button className="btn-editar" onClick={() => abrirEditar(p)}>Editar</button>
              <button className="btn-remover" onClick={() => remover(p.id)}>Remover</button>
            </div>
          </div>
        ))}
      </div>

      {modal && (
        <ModalPaciente
          paciente={editando}
          onClose={() => setModal(false)}
          onSalvo={() => { setModal(false); void carregarPacientes(); }}
        />
      )}
    </div>
  );
}

interface ModalPacienteProps {
  paciente: Paciente | null;
  onClose: () => void;
  onSalvo: () => void;
}

interface PacienteForm {
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  dataNascimento: string;
  endereco: string;
}

function ModalPaciente({ paciente, onClose, onSalvo }: ModalPacienteProps) {
  const [form, setForm] = useState<PacienteForm>({
    nome:           paciente?.nome ?? '',
    cpf:            paciente?.cpf ?? '',
    email:          paciente?.email ?? '',
    telefone:       paciente?.telefone ?? '',
    dataNascimento: paciente ? dataParaInput(paciente.dataNascimento) : '',
    endereco:       paciente?.endereco ?? '',
  });

  function set(field: keyof PacienteForm) {
    return (e: React.ChangeEvent<HTMLInputElement>) =>
      setForm(f => ({ ...f, [field]: e.target.value }));
  }

  async function salvar(e: React.FormEvent) {
    e.preventDefault();
    const dados = {
      ...form,
      dataNascimento: dataParaBackend(form.dataNascimento),
      cpf: form.cpf.replace(/\D/g, ''),
      telefone: form.telefone.replace(/\D/g, ''),
    };
    const res = paciente
      ? await apiFetch(`${API}/pacientes/${paciente.id}`, { method: 'PUT', body: JSON.stringify(dados) })
      : await apiFetch(`${API}/pacientes`, { method: 'POST', body: JSON.stringify(dados) });
    if (res?.ok) onSalvo();
    else alert('Erro ao salvar paciente.');
  }

  return (
    <Modal titulo={paciente ? 'Editar Paciente' : 'Novo Paciente'} onClose={onClose}>
      <form onSubmit={salvar}>
        <div className="modal-corpo">
          <Campo label="Nome completo"><input type="text" placeholder="Ex: Maria Silva" required value={form.nome} onChange={set('nome')} /></Campo>
          <Campo label="CPF"><input type="text" placeholder="000.000.000-00" maxLength={14} required value={form.cpf} onChange={e => setForm(f => ({ ...f, cpf: mascaraCpf(e.target.value) }))} /></Campo>
          <Campo label="Email"><input type="email" placeholder="Ex: maria@email.com" value={form.email} onChange={set('email')} /></Campo>
          <Campo label="Telefone"><input type="text" placeholder="(00) 00000-0000" maxLength={15} value={form.telefone} onChange={e => setForm(f => ({ ...f, telefone: mascaraTelefone(e.target.value) }))} /></Campo>
          <Campo label="Data de nascimento"><input type="date" required value={form.dataNascimento} onChange={set('dataNascimento')} /></Campo>
          <Campo label="Endereço"><input type="text" placeholder="Ex: Rua das Flores, 123 - SP" required value={form.endereco} onChange={set('endereco')} /></Campo>
        </div>
        <div className="modal-acoes">
          <button type="button" className="btn-cancelar" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn-salvar">{paciente ? 'Salvar' : 'Cadastrar'}</button>
        </div>
      </form>
    </Modal>
  );
}

function Campo({ label, children }: { label: string; children: ReactNode }) {
  return (
    <div className="modal-campo">
      <label>{label}</label>
      {children}
    </div>
  );
}
