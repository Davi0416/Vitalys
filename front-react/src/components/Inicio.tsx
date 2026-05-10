import { useEffect, useState, type ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { useData } from '../contexts/DataContext';
import { getNomeUsuario } from '../api';
import { DIAS_PT, MESES_PT } from '../utils';
import type { StatusAgendamento } from '../types';
import ModalAgendamento from './modals/ModalAgendamento';

export default function Inicio() {
  const {
    pacientes, profissionais, agendamentos,
    carregarPacientes, carregarProfissionais, carregarAgendamentos,
    nomePaciente, nomeProfissional,
  } = useData();
  const navigate = useNavigate();
  const [modalAgend, setModalAgend] = useState(false);

  useEffect(() => {
    const promises: Promise<void>[] = [];
    if (!agendamentos.length)  promises.push(carregarAgendamentos());
    if (!profissionais.length) promises.push(carregarProfissionais());
    if (!pacientes.length)     promises.push(carregarPacientes());
    void Promise.all(promises);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const agora    = new Date();
  const hora     = agora.getHours();
  const saudacao = hora < 12 ? 'Bom dia' : hora < 18 ? 'Boa tarde' : 'Boa noite';
  const nomeUsr  = getNomeUsuario();
  const dataFmt  = `${DIAS_PT[agora.getDay()]}, ${String(agora.getDate()).padStart(2, '0')} de ${MESES_PT[agora.getMonth()]} de ${agora.getFullYear()}`.toUpperCase();

  const hojeStr     = agora.toDateString();
  const agHoje      = agendamentos.filter(a => a.dataEHoraMarcadas && new Date(a.dataEHoraMarcadas).toDateString() === hojeStr);
  const confirmados = agHoje.filter(a => a.status === 'confirmado').length;
  const pendentes   = agHoje.filter(a => !a.status || a.status === 'pendente').length;
  const cancelados  = agHoje.filter(a => a.status === 'cancelado').length;

  const proximos = agHoje.slice().sort((a, b) =>
    new Date(a.dataEHoraMarcadas).getTime() - new Date(b.dataEHoraMarcadas).getTime()
  );

  const contagem: Record<string, number> = {};
  agHoje.forEach(a => {
    const nome = a.nomeProfissional ?? nomeProfissional(a.idProfissional);
    contagem[nome] = (contagem[nome] ?? 0) + 1;
  });
  const ocupacao = Object.entries(contagem).sort((a, b) => b[1] - a[1]).slice(0, 5);
  const maxVal   = ocupacao.length ? ocupacao[0][1] : 1;

  function badgeClass(status: StatusAgendamento | undefined): string {
    if (status === 'confirmado') return 'badge-ativo';
    if (status === 'cancelado')  return 'badge-desligado';
    return 'badge-pendente';
  }

  return (
    <div>
      <div className="inicio-topo">
        <div className="inicio-topo-esq">
          <span className="inicio-data">{dataFmt}</span>
          <h2 className="inicio-saudacao">
            {saudacao}{renderNome(nomeUsr)}
          </h2>
          <p className="inicio-subtitulo">Aqui está o resumo da clínica para hoje.</p>
        </div>
        <div className="inicio-topo-dir">
          <button className="btn-exportar">Exportar relatório</button>
          <button className="btn-novo" onClick={() => setModalAgend(true)}>+ Novo agendamento</button>
        </div>
      </div>

      <div className="inicio-cards">
        <Card
          label="Agendamentos hoje"
          valor={agHoje.length}
          sub={agHoje.length > 0
            ? `${confirmados} confirmados · ${pendentes} pendentes · ${cancelados} cancelados`
            : 'Nenhum agendamento hoje'}
          icon={<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>}
        />
        <Card
          label="Profissionais ativos"
          valor={profissionais.length}
          sub={`${profissionais.length} cadastrados no sistema`}
          icon={<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>}
        />
        <Card
          label="Pacientes cadastrados"
          valor={pacientes.length.toLocaleString('pt-BR')}
          sub={`${pacientes.length} registros ativos`}
          icon={<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><line x1="23" y1="11" x2="17" y2="11"/><line x1="20" y1="8" x2="20" y2="14"/></svg>}
        />
      </div>

      <div className="inicio-paineis">
        <div className="inicio-painel painel-atend">
          <div className="painel-header">
            <div>
              <h3 className="painel-titulo">Próximos atendimentos</h3>
              <span className="painel-sub">Atualizado agora</span>
            </div>
            <a
              href="#"
              className="painel-link"
              onClick={e => { e.preventDefault(); navigate('/agendamentos'); }}
            >
              Ver agenda completa
            </a>
          </div>
          {proximos.length === 0 ? (
            <p className="lista-vazia" style={{ padding: '24px 0' }}>Nenhum atendimento agendado para hoje.</p>
          ) : (
            proximos.slice(0, 6).map((a, i) => {
              const horaFmt  = new Date(a.dataEHoraMarcadas).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
              const status   = a.status ?? 'pendente';
              const barraExtra = status === 'confirmado' ? '' : ` ${status}`;
              const nomePac  = a.nomePaciente ?? nomePaciente(a.idPaciente);
              const nomeProf = a.nomeProfissional ?? nomeProfissional(a.idProfissional);
              return (
                <div key={i} className="proximo-atend">
                  <span className="proximo-atend-hora">{horaFmt}</span>
                  <div className={`proximo-atend-barra${barraExtra}`} />
                  <div className="proximo-atend-info">
                    <div className="proximo-atend-nome">{nomePac}</div>
                    <div className="proximo-atend-det">{nomeProf}</div>
                  </div>
                  <span className={`badge ${badgeClass(status)}`}>
                    {status.charAt(0).toUpperCase() + status.slice(1)}
                  </span>
                </div>
              );
            })
          )}
        </div>

        <div className="inicio-paineis-dir">
          <div className="inicio-painel painel-ocupacao">
            <h3 className="painel-titulo">Ocupação por profissional</h3>
            <span className="painel-sub">Agendamentos hoje por profissional</span>
            <div className="ocupacao-lista">
              {ocupacao.length === 0 ? (
                <p style={{ fontSize: '13px', color: 'var(--text-muted)', paddingTop: '12px' }}>Sem agendamentos hoje.</p>
              ) : (
                ocupacao.map(([nome, count]) => (
                  <div key={nome} className="ocupacao-item">
                    <div className="ocupacao-item-header">
                      <span className="ocupacao-item-nome">{nome}</span>
                      <span className="ocupacao-item-pct">{count} agend.</span>
                    </div>
                    <div className="ocupacao-barra-track">
                      <div className="ocupacao-barra-fill" style={{ width: `${Math.round((count / maxVal) * 100)}%` }} />
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>

      {modalAgend && (
        <ModalAgendamento
          onClose={() => setModalAgend(false)}
          onSalvo={() => { setModalAgend(false); void carregarAgendamentos(); }}
        />
      )}
    </div>
  );
}

interface CardProps {
  label: string;
  valor: number | string;
  sub: string;
  icon: ReactNode;
}

function Card({ label, valor, sub, icon }: CardProps) {
  return (
    <div className="inicio-card">
      <div className="inicio-card-top">
        <div className="inicio-card-icon">{icon}</div>
      </div>
      <span className="inicio-card-label">{label}</span>
      <span className="inicio-card-num">{valor === 0 || valor ? valor : '–'}</span>
      <span className="inicio-card-sub">{sub}</span>
    </div>
  );
}

function renderNome(nome: string): ReactNode {
  if (!nome) return '!';
  return <>, <span style={{ color: 'var(--color-primary)' }}>{nome}</span></>;
}
