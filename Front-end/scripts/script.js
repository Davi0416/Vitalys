// ── TEMA ──
const btn = document.getElementById('theme-toggle');
const themeIcon = document.getElementById('theme-icon');
const html = document.documentElement;

const themes = [
  {
    name: 'light',
    svg: `<circle cx="12" cy="12" r="4"/><line x1="12" y1="2" x2="12" y2="5"/><line x1="12" y1="19" x2="12" y2="22"/><line x1="2" y1="12" x2="5" y2="12"/><line x1="19" y1="12" x2="22" y2="12"/><line x1="4.93" y1="4.93" x2="7.05" y2="7.05"/><line x1="16.95" y1="16.95" x2="19.07" y2="19.07"/><line x1="4.93" y1="19.07" x2="7.05" y2="16.95"/><line x1="16.95" y1="7.05" x2="19.07" y2="4.93"/>`
  },
  {
    name: 'system',
    svg: `<circle cx="12" cy="12" r="9"/><path d="M12 3v18"/><path d="M12 3a9 9 0 0 1 0 18z" fill="currentColor" stroke="none" opacity="0.4"/>`
  },
  {
    name: 'dark',
    svg: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>`
  }
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

// ── NAVEGAÇÃO ──
function mostrarTela(id, link) {
  document.querySelectorAll('.tela-conteudo').forEach(s => s.classList.remove('ativa'));
  document.getElementById(id).classList.add('ativa');
  document.querySelectorAll('.menuNavegacao a').forEach(a => a.classList.remove('active'));
  link.classList.add('active');
  if (id === 'pacientes') carregarPacientes();
}

// ── PACIENTES ──
const API = 'http://localhost:8080/vitalys/pacientes';

let pacientes = [];
let pacienteEditandoId = null;

async function carregarPacientes() {
  try {
    const res = await fetch(API);
    pacientes = await res.json();
    renderizarLista(pacientes);
  } catch (err) {
    document.getElementById('listaMensagem').textContent =
      'Não foi possível conectar ao servidor.';
  }
}

function renderizarLista(lista) {
  const container = document.getElementById('listaPacientes');
  const mensagem  = document.getElementById('listaMensagem');

  container.querySelectorAll('.paciente-item').forEach(el => el.remove());

  if (lista.length === 0) {
    mensagem.style.display = 'block';
    mensagem.textContent   = 'Nenhum paciente encontrado.';
    return;
  }

  mensagem.style.display = 'none';

  lista.forEach(p => {
    const item = document.createElement('div');
    item.className  = 'paciente-item';
    item.dataset.id = p.id;
    item.innerHTML  = `
      <div class="paciente-info">
        <span class="paciente-nome">${p.nome}</span>
        <span class="paciente-detalhe">
          CPF: ${p.cpf ?? '-'}
          &nbsp;|&nbsp;
          Nasc: ${formatarData(p.dataNascimento)}
          &nbsp;|&nbsp;
          ${p.email ?? '-'}
          &nbsp;|&nbsp;
          ${p.endereco ?? '-'}
        </span>
      </div>
      <div class="paciente-acoes">
        <button class="btn-editar" onclick="editarPaciente(${p.id})">Editar</button>
        <button class="btn-remover" onclick="removerPaciente(${p.id})">Remover</button>
      </div>
    `;
    container.appendChild(item);
  });
}

function filtrarPacientes() {
  const termo = document.getElementById('buscarPaciente').value.toLowerCase();
  const filtrados = pacientes.filter(p => p.nome.toLowerCase().includes(termo));
  renderizarLista(filtrados);
}

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

function abrirModal() {
  pacienteEditandoId = null;
  document.getElementById('modalTitulo').textContent = 'Novo Paciente';
  document.getElementById('btnSalvar').textContent   = 'Cadastrar';
  document.getElementById('formPaciente').reset();
  document.getElementById('modalPaciente').classList.add('aberto');
}

function editarPaciente(id) {
  const p = pacientes.find(p => p.id === id);
  if (!p) return;
  pacienteEditandoId = id;
  document.getElementById('modalTitulo').textContent  = 'Editar Paciente';
  document.getElementById('btnSalvar').textContent    = 'Salvar';
  document.getElementById('inputNome').value          = p.nome ?? '';
  document.getElementById('inputCpf').value           = p.cpf ?? '';
  document.getElementById('inputEmail').value         = p.email ?? '';
  document.getElementById('inputNascimento').value    = dataParaInput(p.dataNascimento);
  document.getElementById('inputEndereco').value      = p.endereco ?? '';
  document.getElementById('modalPaciente').classList.add('aberto');
}

function fecharModal() {
  document.getElementById('modalPaciente').classList.remove('aberto');
}

function fecharModalFora(event) {
  if (event.target === document.getElementById('modalPaciente')) fecharModal();
}

async function salvarPaciente(event) {
  event.preventDefault();

  const dados = {
    nome:           document.getElementById('inputNome').value.trim(),
    cpf:            document.getElementById('inputCpf').value.trim(),
    email:          document.getElementById('inputEmail').value.trim(),
    dataNascimento: dataParaBackend(document.getElementById('inputNascimento').value),
    endereco:       document.getElementById('inputEndereco').value.trim(),
  };

  try {
    let res;
    if (pacienteEditandoId) {
      res = await fetch(`${API}/${pacienteEditandoId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados),
      });
    } else {
      res = await fetch(API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados),
      });
    }

    if (res.ok) {
      fecharModal();
      carregarPacientes();
    } else {
      alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
  } catch (err) {
    alert('Não foi possível conectar ao servidor.');
  }
}

async function removerPaciente(id) {
  if (!confirm('Tem certeza que deseja remover este paciente?')) return;
  try {
    await fetch(`${API}/${id}`, { method: 'DELETE' });
    carregarPacientes();
  } catch (err) {
    alert('Não foi possível remover. Tente novamente.');
  }
}