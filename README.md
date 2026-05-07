Projeto desenvolvido com foco em aprendizado e prática para atuação como desenvolvedor backend.

# Vitalys — API de Gestão Clínica

API REST para gerenciamento de clínicas multidisciplinares, desenvolvida com **Spring Boot 3.5.13** e **Java 21**.

Permite cadastrar pacientes, profissionais e usuários, realizar agendamentos, gerenciar cargos e visualizar eventos no calendário.

---

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Executando o Projeto](#executando-o-projeto)
- [Endpoints](#endpoints)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Roadmap](#roadmap)

---

## Tecnologias

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.5.13 | Framework base da aplicação |
| Spring Data JPA | (gerenciada) | Camada de persistência com Hibernate |
| Spring Security | (gerenciada) | Autenticação e autorização |
| Auth0 Java JWT | 4.4.0 | Geração e validação de tokens JWT |
| MySQL | 9.6 | Banco de dados relacional |
| Maven | (wrapper incluso) | Gerenciamento de dependências e build |
| Docker | — | Container do banco de dados |

---

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 21+** — [Download](https://adoptium.net/)
- **Docker** — [Download](https://www.docker.com/)
- **Git**

---

## Configuração do Ambiente

### 1. Clone o repositório

```bash
git clone https://github.com/Davi0416/Vitalys.git
cd Vitalys/vitalys-backend
```

### 2. Suba o banco de dados com Docker

```bash
docker compose up -d
```

Isso sobe um container MySQL na porta `1416` com o banco `base_de_dados_vitalys` já configurado.

### 3. Configure o arquivo de propriedades

Crie o arquivo `src/main/resources/application.properties` com o seguinte conteúdo:

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

> **Atenção:** o arquivo `application.properties` está no `.gitignore` para proteger as credenciais. Crie-o manualmente após clonar o repositório.

### 4. Execute o schema do banco

Com o container rodando, execute o arquivo `database/schema.sql` no banco para criar as tabelas e inserir os dados iniciais de cargos.

---

## Executando o Projeto

### Pelo IntelliJ IDEA

Abra o projeto e execute a classe `VitalysBackendApplication`.

### Pelo Maven Wrapper

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

Após a inicialização, a aplicação estará disponível em:

```
http://localhost:8080
```

---

## Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação. Todos os endpoints, exceto `/vitalys/auth/login`, exigem um token válido no header:

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

#### Exemplo — Cadastrar paciente

**Request:**
```http
POST /vitalys/pacientes
Content-Type: application/json
Authorization: Bearer <token>

{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao@email.com",
  "telefone": "21999999999",
  "dataNascimento": "1990-05-10",
  "endereco": "Rua A, 123"
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
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

#### Exemplo — Cadastrar profissional

**Request:**
```http
POST /vitalys/profissionais
Content-Type: application/json
Authorization: Bearer <token>

{
  "nome": "Dr. Carlos Lima",
  "cpf": "987.654.321-00",
  "email": "carlos@vitalys.com",
  "telefone": "21988888888",
  "dataNascimento": "1985-03-22",
  "idCargo": 2
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "nome": "Dr. Carlos Lima",
  "cpf": "987.654.321-00",
  "email": "carlos@vitalys.com",
  "telefone": "21988888888",
  "dataNascimento": "1985-03-22",
  "idCargo": 2
}
```

---

### Atendimentos — `/vitalys/atendimentos`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/atendimentos` | Lista todos os atendimentos |
| `POST` | `/vitalys/atendimentos` | Agenda um novo atendimento |
| `PUT` | `/vitalys/atendimentos/{id}` | Atualiza um atendimento |
| `DELETE` | `/vitalys/atendimentos/{id}` | Cancela um atendimento |

#### Exemplo — Agendar atendimento

**Request:**
```http
POST /vitalys/atendimentos
Content-Type: application/json
Authorization: Bearer <token>

{
  "idPaciente": 1,
  "idProfissional": 1,
  "dataEHoraMarcadas": "2026-04-25T14:00:00"
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "nomePaciente": "João Silva",
  "nomeProfissional": "Dr. Carlos Lima",
  "dataEHoraMarcadas": "2026-04-25T14:00:00"
}
```

---

### Cargos — `/vitalys/cargos`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/cargos` | Lista todos os cargos |
| `POST` | `/vitalys/cargos` | Cadastra um novo cargo |
| `PUT` | `/vitalys/cargos/{id}` | Atualiza um cargo |
| `DELETE` | `/vitalys/cargos/{id}` | Remove um cargo |

#### Exemplo — Listar cargos

**Response `200 OK`:**
```json
[
  { "id": 1, "cargo": "Atendente", "nivelAcesso": "ADMIN" },
  { "id": 2, "cargo": "Médico", "nivelAcesso": "PROFISSIONAL" },
  { "id": 3, "cargo": "Fisioterapeuta", "nivelAcesso": "PROFISSIONAL" }
]
```

---

### Usuários — `/vitalys/usuarios`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/usuarios` | Lista todos os usuários |
| `POST` | `/vitalys/usuarios` | Cadastra um novo usuário |

---

### Calendário — `/vitalys/calendario`

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/vitalys/calendario` | Lista todos os eventos |
| `POST` | `/vitalys/calendario` | Cadastra um novo evento |

#### Exemplo — Cadastrar evento

**Request:**
```http
POST /vitalys/calendario
Content-Type: application/json
Authorization: Bearer <token>

{
  "nome": "Consulta Dr. Carlos",
  "data": "2026-04-25T10:00:00",
  "tipo": "ATENDIMENTO"
}
```

---

## Estrutura do Projeto

```
vitalys-backend/
├── database/
│   └── schema.sql                          # Script de criação das tabelas
├── src/
│   └── main/
│       ├── java/com/vitalys/backend/
│       │   ├── controller/
│       │   │   ├── AtendimentoController.java
│       │   │   ├── AuthController.java
│       │   │   ├── CalendarioController.java
│       │   │   ├── CargoController.java
│       │   │   ├── PacienteController.java
│       │   │   ├── ProfissionalController.java
│       │   │   └── UsuariosController.java
│       │   ├── dto/
│       │   │   ├── AtendimentoRequestDTO.java
│       │   │   ├── AtendimentoResponseDTO.java
│       │   │   ├── CalendarioRequestDTO.java
│       │   │   ├── CalendarioResponseDTO.java
│       │   │   ├── CargoRequestDTO.java
│       │   │   ├── CargoResponseDTO.java
│       │   │   ├── ErrorResponseDTO.java
│       │   │   ├── LoginRequestDTO.java
│       │   │   ├── PacienteRequestDTO.java
│       │   │   ├── PacienteResponseDTO.java
│       │   │   ├── ProfissionalRequestDTO.java
│       │   │   ├── ProfissionalResponseDTO.java
│       │   │   ├── UsuariosRequestDTO.java
│       │   │   └── UsuariosResponseDTO.java
│       │   ├── exception/
│       │   │   ├── BusinessException.java
│       │   │   ├── ConflictException.java
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── ResourceNotFoundException.java
│       │   ├── infra/security/
│       │   │   ├── SecurityConfig.java
│       │   │   ├── SecurityFilter.java
│       │   │   └── TokenService.java
│       │   ├── model/
│       │   │   ├── Atendimento.java
│       │   │   ├── Calendario.java
│       │   │   ├── Cargo.java
│       │   │   ├── Paciente.java
│       │   │   ├── Profissional.java
│       │   │   └── Usuarios.java
│       │   ├── repository/
│       │   │   ├── AtendimentoRepository.java
│       │   │   ├── CalendarioRepository.java
│       │   │   ├── CargoRepository.java
│       │   │   ├── PacienteRepository.java
│       │   │   ├── ProfissionalRepository.java
│       │   │   └── UsuariosRepository.java
│       │   ├── service/
│       │   │   ├── AtendimentoService.java
│       │   │   ├── AuthorizationService.java
│       │   │   ├── CalendarioService.java
│       │   │   ├── CargoService.java
│       │   │   ├── PacienteService.java
│       │   │   ├── ProfissionalService.java
│       │   │   └── UsuariosService.java
│       │   ├── CorsConfig.java
│       │   └── VitalysBackendApplication.java
│       └── resources/
│           └── application.properties      # Credenciais locais (não versionado)
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
| `calendario` | Eventos, feriados e atendimentos no calendário |

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

- [x] Estrutura base do projeto com Spring Boot
- [x] Banco de dados MySQL via Docker
- [x] CRUD de pacientes
- [x] CRUD de profissionais
- [x] Endpoint de agendamento com retorno de nomes
- [x] Gestão de cargos
- [x] Gestão de usuários
- [x] Calendário de eventos
- [x] Configuração de CORS para integração com frontend
- [x] DTOs de entrada e saída como Java Records
- [x] Autenticação com Spring Security e JWT
- [x] Validação de dados com Jakarta Validation
- [x] Tratamento centralizado de exceções
- [ ] Deploy em produção

---

<p align="center">Vitalys &nbsp;•&nbsp; Sistema de gestão clínica &nbsp;•&nbsp; 2026</p>
