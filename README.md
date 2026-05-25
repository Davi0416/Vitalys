# Vitalys — Sistema de Gestão Clínica

> Projeto desenvolvido com foco em aprendizado e prática para atuação como desenvolvedor.

Sistema completo para gerenciamento de clínicas multidisciplinares. Permite cadastrar pacientes, profissionais e usuários, realizar agendamentos e visualizar eventos no calendário.

## Demo

| Serviço | URL |
|---|---|
| Frontend | https://vitalys0416.netlify.app |
| Backend (API) | https://vitalys-gc27.onrender.com |

**Acesso demo:** login `admin` / senha `admin123`

> O backend está hospedado no plano gratuito do Render e pode demorar até 50 segundos para responder após um período de inatividade.

---

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Executando o Projeto](#executando-o-projeto)
- [Padrões de Projeto](#padrões-de-projeto)
- [Endpoints](#endpoints)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Roadmap](#roadmap)

---

## Tecnologias

### Backend

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.5 | Framework base da aplicação |
| Spring Data JPA | (gerenciada) | Camada de persistência com Hibernate |
| Spring Security | (gerenciada) | Autenticação e autorização |
| Auth0 Java JWT | 4.4.0 | Geração e validação de tokens JWT |
| Lombok | (gerenciada) | Redução de boilerplate (@Builder, @Getter...) |
| MySQL | 9.6 | Banco de dados relacional |
| Maven | (wrapper incluso) | Gerenciamento de dependências e build |
| Docker | — | Container do banco de dados |

### Frontend

| Tecnologia | Versão | Descrição |
|---|---|---|
| React | 18 | Biblioteca de UI |
| TypeScript | 5.6 | Tipagem estática |
| Vite | 5.4 | Bundler e servidor de desenvolvimento |
| React Router | 6 | Roteamento SPA |

---

## Pré-requisitos

- **Java 21+** — [Download](https://adoptium.net/)
- **Node.js 20+** — [Download](https://nodejs.org/)
- **Docker** — [Download](https://www.docker.com/)
- **Git**

---

## Configuração do Ambiente

### 1. Clone o repositório

```bash
git clone https://github.com/Davi0416/Vitalys.git
cd Vitalys
```

### 2. Backend — suba o banco de dados com Docker

```bash
cd vitalys-backend
docker compose up -d
```

Isso sobe um container MySQL na porta `1416` com o banco `base_de_dados_vitalys` já configurado.

### 3. Backend — configure o arquivo de propriedades

Crie o arquivo `vitalys-backend/src/main/resources/application.properties`:

```properties
spring.application.name=vitalys-backend
spring.datasource.url=jdbc:mysql://localhost:1416/base_de_dados_vitalys?serverTimezone=America/Sao_Paulo&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true
spring.datasource.username=REDACTED
spring.datasource.password=REDACTED
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
api.security.token.secret=seu-secret-jwt-aqui
```

> **Atenção:** `application.properties` está no `.gitignore`. Crie-o manualmente após clonar.

### 4. Backend — execute o schema

Com o container rodando, execute `vitalys-backend/database/schema.sql` para criar as tabelas.

### 5. Frontend — instale as dependências

```bash
cd front-react
cp .env.example .env   # ajuste VITE_API_URL se necessário
npm install
```

---

## Executando o Projeto

### Backend

**IntelliJ IDEA:** abra o projeto e execute `VitalysBackendApplication`.

**Maven Wrapper:**

```bash
# Windows
cd vitalys-backend && mvnw.cmd spring-boot:run

# Linux / macOS
cd vitalys-backend && ./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`.

### Frontend

```bash
cd front-react
npm run dev        # desenvolvimento em http://localhost:5173
npm run build      # gera dist/ para produção
```

---

## Padrões de Projeto

O backend aplica os seguintes design patterns:

### Facade — Services
Controllers são finos: apenas recebem HTTP e delegam para os services. Toda lógica de negócio vive nos `*Service.java`.

### Builder — Criação de entidades
Todas as entidades usam `@Builder` do Lombok. Os services criam objetos com o padrão fluente:

```java
Paciente p = Paciente.builder()
    .nome(dto.nome())
    .cpf(dto.cpf())
    .email(dto.email())
    .build();
```

### Proxy — Transações
Métodos de escrita são anotados com `@Transactional` (rollback automático em falha) e leituras com `@Transactional(readOnly = true)` (otimização de conexão).

### Strategy — Tipos de atendimento
`AgendamentoStrategy` é uma interface com implementações `ConsultaStrategy` e `RetornoStrategy`, ambas anotadas com `@Component`. O `AtendimentoService` injeta `Map<String, AgendamentoStrategy>` e delega sem `if/else`:

```java
AgendamentoStrategy strategy = strategies.get(dto.tipo()); // "consulta" ou "retorno"
strategy.validar(dto);
```

### Observer — Evento de atendimento criado
Após persistir um atendimento, o service publica `AtendimentoCriadoEvent` via `ApplicationEventPublisher`. O `AtendimentoEventListener` escuta com `@EventListener` e loga o evento, desacoplando side-effects do fluxo principal.

---

## Autenticação

A API usa **JWT** para autenticação. Todos os endpoints, exceto `/vitalys/auth/login`, exigem o header:

```
Authorization: Bearer <token>
```

### Login

```http
POST /vitalys/auth/login
Content-Type: application/json

{
  "login": "admin",
  "senha": "123456"
}
```

**Response `200 OK`:** retorna o token JWT como texto simples.

---

## Endpoints

Todos os endpoints são prefixados com `/vitalys`.

### Pacientes — `/vitalys/pacientes`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/pacientes` | Lista todos os pacientes |
| `POST` | `/vitalys/pacientes` | Cadastra um novo paciente |
| `PUT` | `/vitalys/pacientes/{id}` | Atualiza dados de um paciente |
| `DELETE` | `/vitalys/pacientes/{id}` | Remove um paciente |

**Exemplo — POST `/vitalys/pacientes`**

```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao@email.com",
  "telefone": "21999999999",
  "dataNascimento": "1990-05-10",
  "endereco": "Rua A, 123"
}
```

---

### Profissionais — `/vitalys/profissionais`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/profissionais` | Lista todos os profissionais |
| `POST` | `/vitalys/profissionais` | Cadastra um novo profissional |
| `PUT` | `/vitalys/profissionais/{id}` | Atualiza dados de um profissional |
| `DELETE` | `/vitalys/profissionais/{id}` | Remove um profissional |

---

### Atendimentos — `/vitalys/atendimentos`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/atendimentos` | Lista todos os atendimentos |
| `POST` | `/vitalys/atendimentos` | Agenda um novo atendimento |
| `PUT` | `/vitalys/atendimentos/{id}` | Atualiza um atendimento |
| `DELETE` | `/vitalys/atendimentos/{id}` | Cancela um atendimento |

**Exemplo — POST `/vitalys/atendimentos`**

```json
{
  "idPaciente": 1,
  "idProfissional": 1,
  "dataEHoraMarcadas": "2026-04-25T14:00:00",
  "tipo": "consulta"
}
```

> O campo `tipo` determina a strategy de validação. Valores aceitos: `consulta`, `retorno`.

---

### Cargos — `/vitalys/cargos`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/cargos` | Lista todos os cargos |
| `POST` | `/vitalys/cargos` | Cadastra um novo cargo |
| `PUT` | `/vitalys/cargos/{id}` | Atualiza um cargo |
| `DELETE` | `/vitalys/cargos/{id}` | Remove um cargo |

---

### Usuários — `/vitalys/usuarios`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/usuarios` | Lista todos os usuários |
| `POST` | `/vitalys/usuarios` | Cadastra um novo usuário |
| `PUT` | `/vitalys/usuarios/{id}` | Atualiza um usuário |
| `DELETE` | `/vitalys/usuarios/{id}` | Remove um usuário |

---

### Calendário — `/vitalys/calendario`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/calendario` | Lista todos os eventos |
| `POST` | `/vitalys/calendario` | Cadastra um novo evento |
| `PUT` | `/vitalys/calendario/{id}` | Atualiza um evento |
| `DELETE` | `/vitalys/calendario/{id}` | Remove um evento |

---

## Estrutura do Projeto

```
Vitalys/
├── front-react/                        # Frontend React + TypeScript
│   ├── public/
│   ├── src/
│   │   ├── api/                        # apiFetch, token helpers, getNomeUsuario
│   │   ├── components/
│   │   │   ├── modals/
│   │   │   │   └── ModalAgendamento.tsx
│   │   │   ├── Agendamentos.tsx
│   │   │   ├── Calendario.tsx
│   │   │   ├── ErrorBoundary.tsx
│   │   │   ├── Header.tsx
│   │   │   ├── Inicio.tsx
│   │   │   ├── Modal.tsx
│   │   │   ├── Pacientes.tsx
│   │   │   └── Profissionais.tsx
│   │   ├── contexts/
│   │   │   ├── AuthContext.tsx
│   │   │   └── DataContext.tsx
│   │   ├── pages/
│   │   │   ├── Dashboard.tsx
│   │   │   └── Login.tsx
│   │   ├── types/
│   │   │   └── index.ts                # Interfaces Paciente, Profissional, Agendamento
│   │   ├── App.tsx
│   │   ├── constants.ts                # TOKEN_KEY, THEME_KEY, API
│   │   ├── main.tsx
│   │   └── utils.ts
│   ├── .env.example
│   ├── tsconfig.json
│   └── vite.config.js
│
└── vitalys-backend/                    # Backend Spring Boot
    ├── database/
    │   └── schema.sql
    ├── src/main/java/com/vitalys/backend/
    │   ├── controller/
    │   ├── dto/
    │   ├── exception/
    │   │   ├── BusinessException.java
    │   │   ├── ConflictException.java
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── ResourceNotFoundException.java
    │   ├── infra/
    │   │   ├── event/
    │   │   │   ├── AtendimentoCriadoEvent.java
    │   │   │   └── AtendimentoEventListener.java
    │   │   └── security/
    │   │       ├── SecurityConfig.java
    │   │       ├── SecurityFilter.java
    │   │       └── TokenService.java
    │   ├── model/
    │   ├── repository/
    │   ├── service/
    │   │   ├── strategy/
    │   │   │   ├── AgendamentoStrategy.java
    │   │   │   ├── ConsultaStrategy.java
    │   │   │   └── RetornoStrategy.java
    │   │   ├── AtendimentoService.java
    │   │   ├── AuthorizationService.java
    │   │   ├── CalendarioService.java
    │   │   ├── CargoService.java
    │   │   ├── PacienteService.java
    │   │   ├── ProfissionalService.java
    │   │   └── UsuariosService.java
    │   ├── CorsConfig.java
    │   └── VitalysBackendApplication.java
    ├── docker-compose.yml
    └── pom.xml
```

---

## Banco de Dados

### Tabelas

| Tabela | Descrição |
|---|---|
| `paciente` | Dados dos pacientes da clínica |
| `profissionais` | Dados dos profissionais de saúde |
| `cargos` | Tipos de cargo e nível de acesso |
| `usuarios` | Credenciais de acesso ao sistema |
| `atendimento` | Agendamentos entre paciente e profissional |
| `calendario` | Eventos e atendimentos no calendário |

### Cargos padrão

| Cargo | Nível de acesso |
|---|---|
| Atendente | ADMIN |
| Médico | PROFISSIONAL |
| Fisioterapeuta | PROFISSIONAL |
| Enfermeiro | PROFISSIONAL |
| Psicólogo | PROFISSIONAL |

---

## Roadmap

- [x] CRUD completo: pacientes, profissionais, cargos, usuários, atendimentos, calendário
- [x] Autenticação com Spring Security e JWT
- [x] Validação de dados com Jakarta Validation
- [x] Tratamento centralizado de exceções com `@RestControllerAdvice`
- [x] DTOs de entrada e saída como Java Records
- [x] Configuração de CORS para integração com frontend
- [x] Deploy em produção (Render + Neon + Netlify)
- [x] Migração do frontend para React 18 + Vite
- [x] Migração do frontend para TypeScript
- [x] Variáveis de ambiente no frontend (`VITE_API_URL`)
- [x] ErrorBoundary no frontend
- [x] Padrão Builder nas entidades com Lombok
- [x] Padrão Strategy para tipos de atendimento
- [x] Padrão Observer com eventos Spring
- [x] Transações declarativas com `@Transactional`

---

<p align="center">Vitalys &nbsp;•&nbsp; Sistema de gestão clínica &nbsp;•&nbsp; 2026</p>
