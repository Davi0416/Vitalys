import { useEffect, useState } from 'react';
import { useData } from '../contexts/DataContext';
import { apiFetch, API } from '../api';
import { iniciais, formatarData, dataParaBackend, dataParaInput, mascaraCpf, mascaraTelefone } from '../utils';
import Modal from './Modal';

export default function Profissionais() {
  const { profissionais, carregarProfissionais } = useData();
  const [busca, setBusca] = useState('');
  const [modal, setModal] = useState(false);
  const [editando, setEditando] = useState(null);

  useEffect(() => { carregarProfissionais(); }, []);

  const lista = profissionais.filter(p => p.nome.toLowerCase().includes(busca.toLowerCase()));

  async function remover(id) {
    if (!confirm('Remover este profissional?')) return;
    await apiFetch(`${API}/profissionais/${id}`, { method: 'DELETE' });
    carregarProfissionais();
  }

  return (
    <div>
      <div className="toolbar">
        <div className="busca">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"
            strokeLinecap="round" strokeLinejoin="round" className="icone-busca">
            <circle cx="11" cy="11" r="8" /><line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input
            type="text" placeholder="Buscar profissional pelo nome..."
            value={busca} onChange={e => setBusca(e.target.value)}
          />
        </div>
        <button className="btn-novo" onClick={() => { setEditando(null); setModal(true); }}>+ Novo</button>
      </div>

      <div className="lista">
        {lista.length === 0 ? (
          <p className="lista-vazia">Nenhum profissional encontrado.</p>
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
              <button className="btn-editar" onClick={() => { setEditando(p); setModal(true); }}>Editar</button>
              <button className="btn-remover" onClick={() => remover(p.id)}>Remover</button>
            </div>
          </div>
        ))}
      </div>

      {modal && (
        <ModalProfissional
          profissional={editando}
          onClose={() => setModal(false)}
          onSalvo={() => { setModal(false); carregarProfissionais(); }}
        />
      )}
    </div>
  );
}

function ModalProfissional({ profissional, onClose, onSalvo }) {
  const [form, setForm] = useState({
    nome:           profissional?.nome ?? '',
    cpf:            profissional?.cpf ?? '',
    email:          profissional?.email ?? '',
    telefone:       profissional?.telefone ?? '',
    dataNascimento: profissional ? dataParaInput(profissional.dataNascimento) : '',
  });

  function set(field) { return e => setForm(f => ({ ...f, [field]: e.target.value })); }

  async function salvar(e) {
    e.preventDefault();
    const dados = { ...form, dataNascimento: dataParaBackend(form.dataNascimento) };
    const res = profissional
      ? await apiFetch(`${API}/profissionais/${profissional.id}`, { method: 'PUT', body: JSON.stringify(dados) })
      : await apiFetch(`${API}/profissionais`, { method: 'POST', body: JSON.stringify(dados) });
    if (res?.ok) onSalvo();
    else alert('Erro ao salvar profissional.');
  }

  return (
    <Modal titulo={profissional ? 'Editar Profissional' : 'Novo Profissional'} onClose={onClose}>
      <form onSubmit={salvar}>
        <div className="modal-corpo">
          <div className="modal-campo"><label>Nome completo</label><input type="text" placeholder="Ex: Dr. João Silva" required value={form.nome} onChange={set('nome')} /></div>
          <div className="modal-campo"><label>Telefone</label><input type="text" placeholder="(00) 00000-0000" maxLength={15} required value={form.telefone} onChange={e => setForm(f => ({ ...f, telefone: mascaraTelefone(e.target.value) }))} /></div>
          <div className="modal-campo"><label>Data de nascimento</label><input type="date" required value={form.dataNascimento} onChange={set('dataNascimento')} /></div>
          <div className="modal-campo"><label>CPF</label><input type="text" placeholder="000.000.000-00" maxLength={14} required value={form.cpf} onChange={e => setForm(f => ({ ...f, cpf: mascaraCpf(e.target.value) }))} /></div>
          <div className="modal-campo"><label>Email</label><input type="email" placeholder="Ex: joao@vitalys.com" value={form.email} onChange={set('email')} /></div>
        </div>
        <div className="modal-acoes">
          <button type="button" className="btn-cancelar" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn-salvar">{profissional ? 'Salvar' : 'Cadastrar'}</button>
        </div>
      </form>
    </Modal>
  );
}
