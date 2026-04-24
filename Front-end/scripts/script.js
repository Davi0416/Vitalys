// ════════════════════════════
//  TEMA
// ════════════════════════════
const btn = document.getElementById('theme-toggle');
const themeIcon = document.getElementById('theme-icon');
const html = document.documentElement;

const themes = [
  { name: 'light', svg: `<circle cx="12" cy="12" r="4"/><line x1="12" y1="2" x2="12" y2="5"/><line x1="12" y1="19" x2="12" y2="22"/><line x1="2" y1="12" x2="5" y2="12"/><line x1="19" y1="12" x2="22" y2="12"/><line x1="4.93" y1="4.93" x2="7.05" y2="7.05"/><line x1="16.95" y1="16.95" x2="19.07" y2="19.07"/><line x1="4.93" y1="19.07" x2="7.05" y2="16.95"/><line x1="16.95" y1="7.05" x2="19.07" y2="4.93"/>` },
  { name: 'system', svg: `<circle cx="12" cy="12" r="9"/><path d="M12 3v18"/><path d="M12 3a9 9 0 0 1 0 18z" fill="currentColor" stroke="none" opacity="0.4"/>` },
  { name: 'dark', svg: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>` }
];

let currentIndex = 0;
const saved = localStorage.getItem('vitalys-theme-preference');
if (saved) {
  const savedIndex = themes.findIndex(t => t.name === saved);
  if (savedIndex !== -1) currentIndex = savedIndex;
}
applyTheme(themes[currentIndex]);

btn.addEventListener('click', () => {
  currentIndex = (currentIndex + 1) % themes.length;
  applyTheme(themes[currentIndex]);
});

function applyTheme(theme) {
  if (theme.name === 'system') {
    html.removeAttribute('data-theme');
  } else {
    html.setAttribute('data-theme', theme.name);
  }
  themeIcon.innerHTML = theme.svg;
  localStorage.setItem('vitalys-theme-preference', theme.name);
}

// ════════════════════════════
//  NAVEGAÇÃO
// ════════════════════════════
function mostrarTela(id, link) {
  document.querySelectorAll('.tela-conteudo').forEach(s => s.classList.remove('ativa'));
  document.getElementById(id).classList.add('ativa');
  document.querySelectorAll('.menuNavegacao a').forEach(a => a.classList.remove('active'));
  link.classList.add('active');

  if (id === 'pacientes')     carregarPacientes();
  if (id === 'agendamentos')  carregarAgendamentos();
  if (id === 'profissionais') carregarProfissionais();
  if (id === 'calendario')    renderizarCalendario();
  if (id === 'inicio')        atualizarResumoInicio();
}

// ════════════════════════════
//  MODAL — utilitários
// ════════════════════════════
function abrirModal(id) {
  document.getElementById(id).classList.add('aberto');
}

function fecharModal(id) {
  document.getElementById(id).classList.remove('aberto');
}

function fecharModalFora(event, id) {
  if (event.target === document.getElementById(id)) fecharModal(id);
}

// ════════════════════════════
//  UTILITÁRIOS
// ════════════════════════════
function formatarData(data) {
  if (!data) return '-';
  const d = new Date(data);
  if (isNaN(d)) return '-';
  return d.toLocaleDateString('pt-BR');
}

function dataParaBackend(dataStr) {
  if (!dataStr) return null;
  return new Date(dataStr + 'T00:00:00').getTime();
}

function dataParaInput(data) {
  if (!data) return '';
  const d = new Date(data);
  if (isNaN(d)) return '';
  return d.toISOString().split('T')[0];
}

function mascaraCpf(input) {
  let v = input.value.replace(/\D/g, '').slice(0, 11);
  v = v.replace(/(\d{3})(\d)/, '$1.$2');
  v = v.replace(/(\d{3})(\d)/, '$1.$2');
  v = v.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  input.value = v;
}

function mascaraTelefone(input) {
  let v = input.value.replace(/\D/g, '').slice(0, 11);
  v = v.replace(/(\d{2})(\d)/, '($1) $2');
  v = v.replace(/(\d{5})(\d)/, '$1-$2');
  input.value = v;
}

// ════════════════════════════
//  PACIENTES
// ════════════════════════════
const API_PACIENTES = 'http://localhost:8080/vitalys/pacientes';
let pacientes = [];
let pacienteEditandoId = null;

async function carregarPacientes() {
  try {
    const res = await fetch(API_PACIENTES);
    pacientes = await res.json();
    renderizarPacientes(pacientes);
  } catch {
    document.getElementById('msgPacientes').textContent =
      'Não foi possível conectar ao servidor.';
  }
}

function renderizarPacientes(lista) {
  const container = document.getElementById('listaPacientes');
  const msg = document.getElementById('msgPacientes');
  container.querySelectorAll('.item').forEach(el => el.remove());

  if (lista.length === 0) {
    msg.style.display = 'block';
    msg.textContent = 'Nenhum paciente encontrado.';
    return;
  }
  msg.style.display = 'none';

  lista.forEach(p => {
    const item = document.createElement('div');
    item.className = 'item';
    item.innerHTML = `
      <div class="item-info">
        <span class="item-nome">${p.nome}</span>
        <span class="item-detalhe">
          CPF: ${p.cpf ?? '-'} &nbsp;|&nbsp;
          Nasc: ${formatarData(p.dataNascimento)} &nbsp;|&nbsp;
          ${p.email ?? '-'} &nbsp;|&nbsp;
          ${p.endereco ?? '-'}
        </span>
      </div>
      <div class="item-acoes">
        <button class="btn-editar" onclick="editarPaciente(${p.id})">Editar</button>
        <button class="btn-remover" onclick="removerPaciente(${p.id})">Remover</button>
      </div>
    `;
    container.appendChild(item);
  });
}

function filtrarPacientes() {
  const t = document.getElementById('buscarPaciente').value.toLowerCase();
  renderizarPacientes(pacientes.filter(p => p.nome.toLowerCase().includes(t)));
}

function editarPaciente(id) {
  const p = pacientes.find(p => p.id === id);
  if (!p) return;
  pacienteEditandoId = id;
  document.getElementById('tituloPaciente').textContent = 'Editar Paciente';
  document.getElementById('btnSalvarPaciente').textContent = 'Salvar';
  document.getElementById('inputNome').value = p.nome ?? '';
  document.getElementById('inputCpf').value = p.cpf ?? '';
  document.getElementById('inputEmail').value = p.email ?? '';
  document.getElementById('inputNascimento').value = dataParaInput(p.dataNascimento);
  document.getElementById('inputEndereco').value = p.endereco ?? '';
  abrirModal('modalPaciente');
}

async function salvarPaciente(event) {
  event.preventDefault();
  const dados = {
    nome: document.getElementById('inputNome').value.trim(),
    cpf: document.getElementById('inputCpf').value.trim(),
    email: document.getElementById('inputEmail').value.trim(),
    dataNascimento: dataParaBackend(document.getElementById('inputNascimento').value),
    endereco: document.getElementById('inputEndereco').value.trim(),
  };
  try {
    const res = pacienteEditandoId
      ? await fetch(`${API_PACIENTES}/${pacienteEditandoId}`, {
          method: 'PUT', headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(dados)
        })
      : await fetch(API_PACIENTES, {
          method: 'POST', headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(dados)
        });
    if (res.ok) { fecharModal('modalPaciente'); carregarPacientes(); }
    else alert('Erro ao salvar paciente.');
  } catch { alert('Não foi possível conectar ao servidor.'); }
}

async function removerPaciente(id) {
  if (!confirm('Remover este paciente?')) return;
  try {
    await fetch(`${API_PACIENTES}/${id}`, { method: 'DELETE' });
    carregarPacientes();
  } catch { alert('Erro ao remover.'); }
}

document.querySelector('#pacientes .btn-novo').onclick = () => {
  pacienteEditandoId = null;
  document.getElementById('tituloPaciente').textContent = 'Novo Paciente';
  document.getElementById('btnSalvarPaciente').textContent = 'Cadastrar';
  document.getElementById('formPaciente').reset();
  abrirModal('modalPaciente');
};

// ════════════════════════════
//  PROFISSIONAIS
// ════════════════════════════
let profissionais = [];
let especialidades = ['Cardiologia', 'Dermatologia', 'Clínica Geral', 'Pediatria', 'Ortopedia'];
let profEditandoId = null;

function carregarProfissionais() {
  renderizarProfissionais(profissionais);
  atualizarSelectEspecialidades();
}

function renderizarProfissionais(lista) {
  const container = document.getElementById('listaProfissionais');
  const msg = document.getElementById('msgProfissionais');
  container.querySelectorAll('.item').forEach(el => el.remove());

  if (lista.length === 0) {
    msg.style.display = 'block';
    msg.textContent = 'Nenhum profissional cadastrado.';
    return;
  }
  msg.style.display = 'none';

  lista.forEach(p => {
    const item = document.createElement('div');
    item.className = 'item';
    const ativo = p.status !== 'desligado';
    item.innerHTML = `
      <div class="item-info">
        <span class="item-nome">
          ${p.nome}
          <span class="badge ${ativo ? 'badge-ativo' : 'badge-desligado'}">
            ${ativo ? 'Ativo' : 'Desligado'}
          </span>
        </span>
        <span class="item-detalhe">
          ${p.especialidade ?? '-'} &nbsp;|&nbsp; ${p.telefone ?? '-'}
        </span>
      </div>
      <div class="item-acoes">
        <button class="btn-editar" onclick="editarProfissional(${p.id})">Editar</button>
        ${ativo
          ? `<button class="btn-desligar" onclick="desligarProfissional(${p.id})">Desligar</button>`
          : `<button class="btn-reativar" onclick="reativarProfissional(${p.id})">Reativar</button>`
        }
      </div>
    `;
    container.appendChild(item);
  });
}

function filtrarProfissionais() {
  const t = document.getElementById('buscarProfissional').value.toLowerCase();
  renderizarProfissionais(profissionais.filter(p => p.nome.toLowerCase().includes(t)));
}

function atualizarSelectEspecialidades() {
  const sel = document.getElementById('selectEspecialidade');
  const valorAtual = sel.value;
  sel.innerHTML = '<option value="">Selecione ou crie uma especialidade...</option>';
  especialidades.forEach(e => {
    const opt = document.createElement('option');
    opt.value = e; opt.textContent = e;
    sel.appendChild(opt);
  });
  const nova = document.createElement('option');
  nova.value = '__nova__'; nova.textContent = '+ Criar nova especialidade';
  sel.appendChild(nova);
  sel.value = valorAtual;
}

function toggleNovaEspecialidade() {
  const sel = document.getElementById('selectEspecialidade');
  const input = document.getElementById('inputNovaEspecialidade');
  input.style.display = sel.value === '__nova__' ? 'block' : 'none';
  if (sel.value === '__nova__') input.focus();
}

function editarProfissional(id) {
  const p = profissionais.find(p => p.id === id);
  if (!p) return;
  profEditandoId = id;
  document.getElementById('tituloProfissional').textContent = 'Editar Profissional';
  document.getElementById('btnSalvarProfissional').textContent = 'Salvar';
  document.getElementById('inputNomeProfissional').value = p.nome ?? '';
  document.getElementById('inputTelefone').value = p.telefone ?? '';
  document.getElementById('inputNascimentoProf').value = dataParaInput(p.dataNascimento);
  atualizarSelectEspecialidades();
  document.getElementById('selectEspecialidade').value = p.especialidade ?? '';
  document.getElementById('inputNovaEspecialidade').style.display = 'none';
  abrirModal('modalProfissional');
}

function desligarProfissional(id) {
  if (!confirm('Desligar este profissional? Os dados serão mantidos.')) return;
  const p = profissionais.find(p => p.id === id);
  if (p) { p.status = 'desligado'; renderizarProfissionais(profissionais); }
}

function reativarProfissional(id) {
  const p = profissionais.find(p => p.id === id);
  if (p) { p.status = 'ativo'; renderizarProfissionais(profissionais); }
}

function salvarProfissional(event) {
  event.preventDefault();
  const sel = document.getElementById('selectEspecialidade');
  let especialidade = sel.value;

  if (especialidade === '__nova__') {
    const nova = document.getElementById('inputNovaEspecialidade').value.trim();
    if (!nova) { alert('Digite o nome da nova especialidade.'); return; }
    if (!especialidades.includes(nova)) especialidades.push(nova);
    especialidade = nova;
  }

  if (!especialidade) { alert('Selecione ou crie uma especialidade.'); return; }

  const dados = {
    id: profEditandoId ?? Date.now(),
    nome: document.getElementById('inputNomeProfissional').value.trim(),
    telefone: document.getElementById('inputTelefone').value.trim(),
    dataNascimento: document.getElementById('inputNascimentoProf').value,
    especialidade,
    status: 'ativo',
  };

  if (profEditandoId) {
    const idx = profissionais.findIndex(p => p.id === profEditandoId);
    if (idx !== -1) profissionais[idx] = { ...profissionais[idx], ...dados };
  } else {
    profissionais.push(dados);
  }

  fecharModal('modalProfissional');
  profEditandoId = null;
  document.getElementById('formProfissional').reset();
  document.getElementById('inputNovaEspecialidade').style.display = 'none';
  renderizarProfissionais(profissionais);
  atualizarSelectProfissionaisAgendamento();
}

document.querySelector('#profissionais .btn-novo').onclick = () => {
  profEditandoId = null;
  document.getElementById('tituloProfissional').textContent = 'Novo Profissional';
  document.getElementById('btnSalvarProfissional').textContent = 'Cadastrar';
  document.getElementById('formProfissional').reset();
  atualizarSelectEspecialidades();
  document.getElementById('inputNovaEspecialidade').style.display = 'none';
  abrirModal('modalProfissional');
};

// ════════════════════════════
//  AGENDAMENTOS
// ════════════════════════════
let agendamentos = [];
let horarioSelecionado = null;

const HORARIOS_DISPONIVEIS = [
  '08:00', '08:30', '09:00', '09:30', '10:00', '10:30',
  '11:00', '11:30', '13:00', '13:30', '14:00', '14:30',
  '15:00', '15:30', '16:00', '16:30'
];

function carregarAgendamentos() {
  renderizarAgendamentos(agendamentos);
  atualizarSelectProfissionaisAgendamento();
}

function atualizarSelectProfissionaisAgendamento() {
  const sel = document.getElementById('selectProfissional');
  const valorAtual = sel.value;
  sel.innerHTML = '<option value="">Selecione um profissional...</option>';
  profissionais.filter(p => p.status !== 'desligado').forEach(p => {
    const opt = document.createElement('option');
    opt.value = p.id; opt.textContent = `${p.nome} — ${p.especialidade}`;
    sel.appendChild(opt);
  });
  sel.value = valorAtual;
}

function renderizarAgendamentos(lista) {
  const container = document.getElementById('listaAgendamentos');
  const msg = document.getElementById('msgAgendamentos');
  container.querySelectorAll('.item').forEach(el => el.remove());

  if (lista.length === 0) {
    msg.style.display = 'block';
    msg.textContent = 'Nenhum agendamento encontrado.';
    return;
  }
  msg.style.display = 'none';

  lista.forEach(a => {
    const item = document.createElement('div');
    item.className = 'item';
    const confirmado = a.status === 'confirmado';
    item.innerHTML = `
      <div class="item-info">
        <span class="item-nome">
          ${a.pacienteNome}
          <span class="badge ${confirmado ? 'badge-ativo' : 'badge-pendente'}">
            ${confirmado ? 'Confirmado' : 'Pendente'}
          </span>
        </span>
        <span class="item-detalhe">
          ${a.profissionalNome} &nbsp;|&nbsp;
          ${a.especialidade ?? '-'} &nbsp;|&nbsp;
          ${a.data} às ${a.hora}
        </span>
      </div>
      <div class="item-acoes">
        <button class="btn-remover" onclick="cancelarAgendamento(${a.id})">Desmarcar</button>
      </div>
    `;
    container.appendChild(item);
  });
}

function filtrarAgendamentos() {
  const t = document.getElementById('buscarAgendamento').value.toLowerCase();
  renderizarAgendamentos(
    agendamentos.filter(a => a.pacienteNome.toLowerCase().includes(t))
  );
}

function autocompletePaciente() {
  const input = document.getElementById('inputPacienteAgend');
  const lista = document.getElementById('autocompletePaciente');
  const termo = input.value.toLowerCase();
  lista.innerHTML = '';
  document.getElementById('inputPacienteId').value = '';

  if (!termo) { lista.style.display = 'none'; return; }

  const filtrados = pacientes.filter(p => p.nome.toLowerCase().includes(termo));
  if (filtrados.length === 0) { lista.style.display = 'none'; return; }

  lista.style.display = 'block';
  filtrados.forEach(p => {
    const item = document.createElement('div');
    item.className = 'autocomplete-item';
    item.textContent = p.nome;
    item.onclick = () => {
      input.value = p.nome;
      document.getElementById('inputPacienteId').value = p.id;
      lista.style.display = 'none';
    };
    lista.appendChild(item);
  });
}

document.addEventListener('click', e => {
  const wrap = document.getElementById('autocompletePaciente');
  if (wrap && !wrap.contains(e.target) && e.target.id !== 'inputPacienteAgend') {
    wrap.style.display = 'none';
  }
});

function carregarHorarios() {
  const profId = document.getElementById('selectProfissional').value;
  const data   = document.getElementById('inputDataAgend').value;
  const grid   = document.getElementById('horariosGrid');
  horarioSelecionado = null;
  grid.innerHTML = '';

  if (!profId || !data) {
    grid.innerHTML = '<p class="lista-vazia" style="padding:12px 0;text-align:left;">Selecione um profissional e uma data.</p>';
    return;
  }

  const ocupados = agendamentos
    .filter(a => String(a.profissionalId) === String(profId) && a.data === data)
    .map(a => a.hora);

  HORARIOS_DISPONIVEIS.forEach(h => {
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'horario-btn' + (ocupados.includes(h) ? ' ocupado' : '');
    btn.textContent = h;
    btn.disabled = ocupados.includes(h);
    btn.onclick = () => {
      grid.querySelectorAll('.horario-btn').forEach(b => b.classList.remove('selecionado'));
      btn.classList.add('selecionado');
      horarioSelecionado = h;
    };
    grid.appendChild(btn);
  });
}

function salvarAgendamento(event) {
  event.preventDefault();
  const pacienteId   = document.getElementById('inputPacienteId').value;
  const pacienteNome = document.getElementById('inputPacienteAgend').value.trim();
  const profId       = document.getElementById('selectProfissional').value;
  const data         = document.getElementById('inputDataAgend').value;
  const status       = document.getElementById('selectStatus').value;

  if (!pacienteId)         { alert('Selecione um paciente da lista.'); return; }
  if (!profId)             { alert('Selecione um profissional.'); return; }
  if (!data)               { alert('Selecione uma data.'); return; }
  if (!horarioSelecionado) { alert('Selecione um horário.'); return; }

  const prof = profissionais.find(p => String(p.id) === String(profId));

  agendamentos.push({
    id: Date.now(),
    pacienteId,
    pacienteNome,
    profissionalId: profId,
    profissionalNome: prof?.nome ?? '-',
    especialidade: prof?.especialidade ?? '-',
    data: new Date(data + 'T00:00:00').toLocaleDateString('pt-BR'),
    hora: horarioSelecionado,
    status,
  });

  fecharModal('modalAgendamento');
  document.getElementById('formAgendamento').reset();
  document.getElementById('horariosGrid').innerHTML = '';
  horarioSelecionado = null;
  renderizarAgendamentos(agendamentos);
}

function cancelarAgendamento(id) {
  if (!confirm('Desmarcar este agendamento?')) return;
  agendamentos = agendamentos.filter(a => a.id !== id);
  renderizarAgendamentos(agendamentos);
}

// ════════════════════════════
//  INÍCIO — resumo
// ════════════════════════════
function atualizarResumoInicio() {
  const hoje = new Date().toLocaleDateString('pt-BR');
  const agHoje = agendamentos.filter(a => a.data === hoje).length;
  const profAtivos = profissionais.filter(p => p.status !== 'desligado').length;

  const elAgHoje = document.getElementById('totalAgendamentosHoje');
  const elProf   = document.getElementById('totalProfissionais');
  const elPac    = document.getElementById('totalPacientes');

  if (elAgHoje) elAgHoje.textContent = agHoje;
  if (elProf)   elProf.textContent   = profAtivos;
  if (elPac)    elPac.textContent    = pacientes.length;
}

// ════════════════════════════
//  CALENDÁRIO
// ════════════════════════════
let calAno  = new Date().getFullYear();
let calMes  = new Date().getMonth();
let calDiaSelecionado = null;

const MESES = [
  'Janeiro','Fevereiro','Março','Abril','Maio','Junho',
  'Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'
];

function mudarMes(dir) {
  calMes += dir;
  if (calMes > 11) { calMes = 0; calAno++; }
  if (calMes < 0)  { calMes = 11; calAno--; }
  renderizarCalendario();
}

function renderizarCalendario() {
  document.getElementById('calTitulo').textContent = `${MESES[calMes]} ${calAno}`;

  const container = document.getElementById('calDias');
  container.innerHTML = '';

  const hoje     = new Date();
  const primeiro = new Date(calAno, calMes, 1).getDay();
  const total    = new Date(calAno, calMes + 1, 0).getDate();

  const diasComAtend = new Set(
    agendamentos
      .map(a => {
        const partes = a.data.split('/');
        if (partes.length === 3) {
          const d = parseInt(partes[0]);
          const m = parseInt(partes[1]) - 1;
          const y = parseInt(partes[2]);
          if (m === calMes && y === calAno) return d;
        }
        return null;
      })
      .filter(Boolean)
  );

  for (let i = 0; i < primeiro; i++) {
    container.appendChild(document.createElement('div'));
  }

  for (let d = 1; d <= total; d++) {
    const div = document.createElement('div');
    div.className = 'cal-dia';

    const isHoje = d === hoje.getDate() &&
                   calMes === hoje.getMonth() &&
                   calAno === hoje.getFullYear();

    const isSelecionado = calDiaSelecionado &&
                          d === calDiaSelecionado.d &&
                          calMes === calDiaSelecionado.m &&
                          calAno === calDiaSelecionado.y;

    if (isHoje)        div.classList.add('hoje');
    if (isSelecionado) div.classList.add('selecionado');
    if (diasComAtend.has(d)) div.classList.add('tem-atend');

    div.innerHTML = `${d}${diasComAtend.has(d) ? '<div class="cal-dot"></div>' : ''}`;

    div.onclick = () => {
      calDiaSelecionado = { d, m: calMes, y: calAno };
      renderizarCalendario();
      renderizarPainelDia(d, calMes, calAno);
    };

    container.appendChild(div);
  }
}

function renderizarPainelDia(d, m, y) {
  const painel = document.getElementById('calDireita');
  const dataStr = `${String(d).padStart(2,'0')}/${String(m+1).padStart(2,'0')}/${y}`;

  const idsComAgend = new Set(
    agendamentos
      .filter(a => a.data === dataStr)
      .map(a => String(a.profissionalId))
  );

  const lista = profissionais.filter(p => p.status !== 'desligado');

  if (lista.length === 0) {
    painel.innerHTML = `
      <p class="cal-direita-titulo">${d} de ${MESES[m]}</p>
      <p class="cal-direita-vazia">Nenhum profissional ativo cadastrado.</p>
    `;
    return;
  }

  let html = `<p class="cal-direita-titulo">${d} de ${MESES[m]}</p>`;

  lista.forEach(p => {
    const temAgenda = idsComAgend.has(String(p.id));
    const iniciais  = p.nome.split(' ').slice(0,2).map(n => n[0]).join('').toUpperCase();
    html += `
      <div class="prof-card" style="${!temAgenda ? 'opacity:0.45;' : ''}">
        <div class="prof-avatar-cal">${iniciais}</div>
        <div class="prof-card-info">
          <span class="prof-card-nome">${p.nome}</span>
          <span class="prof-card-esp">${p.especialidade ?? '-'}</span>
        </div>
        ${temAgenda
          ? `<span class="prof-card-horario">Com agenda</span>`
          : `<span class="prof-card-sem-agenda">Sem agenda</span>`
        }
      </div>
    `;
  });

  painel.innerHTML = html;
}