import { useEffect, useState } from 'react';
import { useData } from '../contexts/DataContext';
import { apiFetch, API } from '../api';
import { iniciais, formatarDataHora } from '../utils';
import type { StatusAgendamento } from '../types';
import ModalAgendamento from './modals/ModalAgendamento';

export default function Agendamentos() {
  const { agendamentos, carregarAgendamentos, nomePaciente, nomeProfissional } = useData();
  const [busca, setBusca] = useState('');
  const [modal, setModal] = useState(false);

  useEffect(() => { void carregarAgendamentos(); }, []);

  const lista = agendamentos.filter(a => {
    const nomePac = a.nomePaciente ?? nomePaciente(a.idPaciente);
    return nomePac.toLowerCase().includes(busca.toLowerCase());
  });

  async function cancelar(id: number) {
    if (!confirm('Desmarcar este agendamento?')) return;
    await apiFetch(`${API}/atendimentos/${id}`, { method: 'DELETE' });
    void carregarAgendamentos();
  }

  function badgeClass(status: StatusAgendamento | undefined): string {
    if (status === 'confirmado') return 'badge-ativo';
    if (status === 'cancelado')  return 'badge-desligado';
    return 'badge-pendente';
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
            type="text" placeholder="Buscar agendamento pelo nome do paciente..."
            value={busca} onChange={e => setBusca(e.target.value)}
          />
        </div>
        <button className="btn-novo" onClick={() => setModal(true)}>+ Novo</button>
      </div>

      <div className="lista">
        {lista.length === 0 ? (
          <p className="lista-vazia">Nenhum agendamento encontrado.</p>
        ) : lista.map(a => {
          const status   = a.status ?? 'pendente';
          const nomePac  = a.nomePaciente ?? nomePaciente(a.idPaciente);
          const nomeProf = a.nomeProfissional ?? nomeProfissional(a.idProfissional);
          return (
            <div key={a.id} className="item">
              <div className="item-avatar">{iniciais(nomePac)}</div>
              <div className="item-info">
                <span className="item-nome">
                  {nomePac}
                  <span className={`badge ${badgeClass(status)}`}>
                    {status.charAt(0).toUpperCase() + status.slice(1)}
                  </span>
                </span>
                <span className="item-detalhe">
                  {nomeProf} &nbsp;|&nbsp; {formatarDataHora(a.dataEHoraMarcadas)}
                </span>
              </div>
              <div className="item-acoes">
                <button className="btn-remover" onClick={() => cancelar(a.id)}>Desmarcar</button>
              </div>
            </div>
          );
        })}
      </div>

      {modal && (
        <ModalAgendamento
          onClose={() => setModal(false)}
          onSalvo={() => { setModal(false); void carregarAgendamentos(); }}
        />
      )}
    </div>
  );
}
