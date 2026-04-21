# Vitalys

> Sistema de gestão para clínicas multidisciplinares

---

## Sobre

O **Vitalys** é um sistema desenvolvido para centralizar o controle de pacientes, agendamentos, profissionais e eventos em clínicas multidisciplinares. O objetivo é tornar o fluxo de atendimento mais ágil, reduzir erros operacionais e dar mais visibilidade à equipe.

---

## Tecnologias

- **Backend:** Java com Spring Boot
- **Frontend:** a definir (desenvolvido por terceiros)

---

## Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| **Login seguro** | Autenticação de atendentes com validação de credenciais e reset em caso de acesso inválido |
| **Cadastro de pacientes** | Registro com nome, nascimento, endereço e CPF — com validação antes de salvar |
| **Agendamento** | Busca por nome/CPF, seleção de profissional, exibição de horários disponíveis e alerta de conflito |
| **Lista de atendimentos** | Visualização de todos os agendamentos com opções de deletar ou remarcar |
| **Cadastro de profissionais** | Registro com dados pessoais, profissão e disponibilidade — concessão de acesso ao painel |
| **Lista de profissionais** | Detalhes de atuação e opção de desligar acesso sem excluir a ficha |
| **Calendário de eventos** | Visualização de atendimentos, feriados e eventos — com opção de marcar, editar ou remover |

---

## Fluxo geral

```
Inicio
  └── Login (usuario + senha)
        ├── Credenciais invalidas → Resetar tela e avisar
        └── Credenciais validas → Menu principal
              ├── 1. Cadastrar paciente
              │     └── Preencher dados → Validar → Salvar BD → Confirmar
              ├── 2. Agendar atendimento
              │     └── Buscar paciente → Escolher profissional → Ver horarios
              │           ├── Disponivel → Registrar → BD → Confirmar
              │           └── Indisponivel → Avisar → Escolher outro horario
              ├── 3. Lista de atendimentos
              │     └── Listar → Deletar ou Remarcar
              ├── 4. Cadastrar profissional
              │     └── Preencher dados → Validar → Salvar BD → Conceder acesso
              ├── 5. Lista de profissionais
              │     └── Listar → Detalhes ou Desligar acesso
              └── 6. Calendario de eventos
                    └── Exibir → Marcar / Editar / Remover evento
```

---

## Banco de dados

| Modulo | Campos principais |
|---|---|
| `Paciente` | Nome, data de nascimento, endereco, CPF |
| `Atendimentos` | Paciente, profissional, data/hora, status |
| `Profissionais` | Nome, CPF, profissao, disponibilidade, status de acesso |
| `Calendario` | Tipo (atendimento/feriado/evento), data, descricao |

---

## Estrutura do projeto

O projeto é dividido em dois repositórios separados — um para cada parte da equipe.

**Backend (vitalys-backend):**
```
vitalys-backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/vitalys/
│       │       ├── controller/   # recebe as requisições (ex: AgendamentoController)
│       │       ├── service/      # regras de negócio (ex: AgendamentoService)
│       │       ├── model/        # entidades (Paciente, Profissional...)
│       │       └── repository/   # acesso ao banco de dados
│       └── resources/
│           └── application.properties  # configurações (banco, porta...)
├── database/
│   └── migrations/   # scripts SQL de criação das tabelas
└── README.md
```

**Frontend (vitalys-frontend):**
```
vitalys-frontend/
├── src/
│   ├── pages/        # telas (Login, Menu, Pacientes...)
│   ├── components/   # partes reutilizáveis (botões, tabelas...)
│   └── services/     # chamadas para a API do backend
└── README.md
```

---

## Observações

- O sistema é operado pelo perfil de **atendente** — acesso de profissionais ao painel é concedido via cadastro pelo atendente
- Desligar um profissional remove apenas o acesso, **preservando ficha e histórico** no banco de dados
- Conflitos de horário são detectados em tempo real, com retorno do nome da consulta existente

---

## Status do projeto

- [x] Especificação
- [ ] Backend (Java)
- [ ] Frontend
- [x] Banco de dados
- [ ] Testes

---

<p align="center">Vitalys &nbsp;•&nbsp; Sistema de gestão clínica &nbsp;•&nbsp; 2026</p>
