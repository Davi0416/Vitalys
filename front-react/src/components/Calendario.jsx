import { useEffect, useState } from 'react';
import { useData } from '../contexts/DataContext';
import { MESES, iniciais } from '../utils';

export default function Calendario() {
  const { agendamentos, profissionais, carregarAgendamentos, carregarProfissionais } = useData();
  const hoje = new Date();
  const [ano, setAno]   = useState(hoje.getFullYear());
  const [mes, setMes]   = useState(hoje.getMonth());
  const [diaSel, setDiaSel] = useState(null);

  useEffect(() => {
    if (!agendamentos.length)  carregarAgendamentos();
    if (!profissionais.length) carregarProfissionais();
  }, []);

  function mudarMes(dir) {
    setMes(m => {
      const nm = m + dir;
      if (nm > 11) { setAno(a => a + 1); return 0; }
      if (nm < 0)  { setAno(a => a - 1); return 11; }
      return nm;
    });
    setDiaSel(null);
  }

  const primeiro = new Date(ano, mes, 1).getDay();
  const total    = new Date(ano, mes + 1, 0).getDate();

  const diasComAtend = new Set(
    agendamentos
      .filter(a => a.dataEHoraMarcadas)
      .map(a => { const d = new Date(a.dataEHoraMarcadas); return d.getMonth() === mes && d.getFullYear() === ano ? d.getDate() : null; })
      .filter(Boolean)
  );

  const agendDia = diaSel
    ? agendamentos.filter(a => {
        if (!a.dataEHoraMarcadas) return false;
        const d = new Date(a.dataEHoraMarcadas);
        return d.getDate() === diaSel && d.getMonth() === mes && d.getFullYear() === ano;
      })
    : [];

  const idsComAgend = new Set(agendDia.map(a => String(a.idProfissional)));

  return (
    <div className="cal-container">
      <div className="cal-esquerda">
        <div className="cal-header">
          <button className="cal-nav" onClick={() => mudarMes(-1)}>‹</button>
          <span className="cal-mes-titulo">{MESES[mes]} {ano}</span>
          <button className="cal-nav" onClick={() => mudarMes(1)}>›</button>
        </div>

        <div className="cal-grid">
          {['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'].map(d => (
            <div key={d} className="cal-dia-nome">{d}</div>
          ))}
        </div>

        <div className="cal-dias">
          {Array.from({ length: primeiro }, (_, i) => <div key={`e${i}`} />)}
          {Array.from({ length: total }, (_, i) => {
            const d = i + 1;
            const isHoje = d === hoje.getDate() && mes === hoje.getMonth() && ano === hoje.getFullYear();
            const isSel  = diaSel === d;
            const temAtend = diasComAtend.has(d);
            return (
              <div
                key={d}
                className={`cal-dia${isHoje ? ' hoje' : ''}${isSel ? ' selecionado' : ''}${temAtend ? ' tem-atend' : ''}`}
                onClick={() => setDiaSel(d)}
              >
                {d}
                {temAtend && <div className="cal-dot" />}
              </div>
            );
          })}
        </div>
      </div>

      <div className="cal-direita" id="calDireita">
        {!diaSel ? (
          <p className="cal-direita-vazia">Clique em um dia para ver os profissionais.</p>
        ) : (
          <>
            <p className="cal-direita-titulo">{diaSel} de {MESES[mes]}</p>
            {profissionais.length === 0 ? (
              <p className="cal-direita-vazia">Nenhum profissional cadastrado.</p>
            ) : profissionais.map(p => {
              const temAgenda = idsComAgend.has(String(p.id));
              return (
                <div key={p.id} className="prof-card" style={!temAgenda ? { opacity: 0.45 } : {}}>
                  <div className="prof-avatar-cal">{iniciais(p.nome)}</div>
                  <div className="prof-card-info">
                    <span className="prof-card-nome">{p.nome}</span>
                    <span className="prof-card-esp">{p.email ?? '-'}</span>
                  </div>
                  {temAgenda
                    ? <span className="prof-card-horario">Com agenda</span>
                    : <span className="prof-card-sem-agenda">Sem agenda</span>
                  }
                </div>
              );
            })}
          </>
        )}
      </div>
    </div>
  );
}
