import { useEffect, useState } from 'react';
import { useData } from '../../contexts/DataContext';
import { apiFetch, API } from '../../api';
import { HORARIOS } from '../../utils';
import Modal from '../Modal';

export default function ModalAgendamento({ onClose, onSalvo }) {
  const { pacientes, profissionais, agendamentos, carregarPacientes, carregarProfissionais } = useData();
  const [buscaPac, setBuscaPac]   = useState('');
  const [pacSel, setPacSel]       = useState(null);
  const [autocomplete, setAuto]   = useState([]);
  const [profId, setProfId]       = useState('');
  const [data, setData]           = useState('');
  const [horario, setHorario]     = useState(null);
  const [status, setStatus]       = useState('pendente');

  useEffect(() => {
    if (!pacientes.length)     carregarPacientes();
    if (!profissionais.length) carregarProfissionais();
  }, []);

  function filtrarPac(v) {
    setBuscaPac(v);
    setPacSel(null);
    if (!v.trim()) { setAuto([]); return; }
    setAuto(pacientes.filter(p => p.nome.toLowerCase().includes(v.toLowerCase())).slice(0, 8));
  }

  function selecionarPac(p) {
    setBuscaPac(p.nome);
    setPacSel(p);
    setAuto([]);
  }

  const ocupados = (profId && data)
    ? agendamentos
        .filter(a => String(a.idProfissional) === String(profId) &&
          new Date(a.dataEHoraMarcadas).toISOString().split('T')[0] === data)
        .map(a => new Date(a.dataEHoraMarcadas).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }))
    : [];

  async function salvar(e) {
    e.preventDefault();
    if (!pacSel)   { alert('Selecione um paciente da lista.'); return; }
    if (!profId)   { alert('Selecione um profissional.'); return; }
    if (!data)     { alert('Selecione uma data.'); return; }
    if (!horario)  { alert('Selecione um horário.'); return; }

    const res = await apiFetch(`${API}/atendimentos`, {
      method: 'POST',
      body: JSON.stringify({
        idPaciente:        pacSel.id,
        idProfissional:    Number(profId),
        dataEHoraMarcadas: `${data}T${horario}:00`,
        status,
      }),
    });
    if (res?.ok) onSalvo();
    else alert('Erro ao salvar agendamento.');
  }

  return (
    <Modal titulo="Novo Agendamento" onClose={onClose}>
      <form onSubmit={salvar}>
        <div className="modal-corpo">
          {/* Paciente com autocomplete */}
          <div className="modal-campo">
            <label>Paciente</label>
            <div className="autocomplete-wrap">
              <input
                type="text" placeholder="Digite o nome do paciente..."
                value={buscaPac} onChange={e => filtrarPac(e.target.value)}
                autoComplete="off" required
              />
              {autocomplete.length > 0 && (
                <div className="autocomplete-lista" style={{ display: 'block' }}>
                  {autocomplete.map(p => (
                    <div key={p.id} className="autocomplete-item" onClick={() => selecionarPac(p)}>
                      {p.nome}
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>

          {/* Profissional */}
          <div className="modal-campo">
            <label>Profissional</label>
            <select value={profId} onChange={e => { setProfId(e.target.value); setHorario(null); }} required>
              <option value="">Selecione um profissional...</option>
              {profissionais.map(p => <option key={p.id} value={p.id}>{p.nome}</option>)}
            </select>
          </div>

          {/* Data */}
          <div className="modal-campo">
            <label>Data</label>
            <input type="date" value={data} onChange={e => { setData(e.target.value); setHorario(null); }} required />
          </div>

          {/* Horários */}
          <div className="modal-campo">
            <label>Horário disponível</label>
            <div className="horarios-grid">
              {(!profId || !data) ? (
                <p className="lista-vazia" style={{ padding: '12px 0', textAlign: 'left', fontSize: '13px' }}>
                  Selecione um profissional e uma data.
                </p>
              ) : HORARIOS.map(h => (
                <button
                  key={h} type="button"
                  className={`horario-btn${ocupados.includes(h) ? ' ocupado' : ''}${horario === h ? ' selecionado' : ''}`}
                  disabled={ocupados.includes(h)}
                  onClick={() => setHorario(h)}
                >
                  {h}
                </button>
              ))}
            </div>
          </div>

          {/* Status */}
          <div className="modal-campo">
            <label>Status</label>
            <select value={status} onChange={e => setStatus(e.target.value)}>
              <option value="pendente">Pendente</option>
              <option value="confirmado">Confirmado</option>
            </select>
          </div>
        </div>
        <div className="modal-acoes">
          <button type="button" className="btn-cancelar" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn-salvar">Agendar</button>
        </div>
      </form>
    </Modal>
  );
}
