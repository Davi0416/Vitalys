-- Schema PostgreSQL para Neon
-- Execute este arquivo no SQL Editor do Neon

CREATE TABLE IF NOT EXISTS cargos (
    id SERIAL PRIMARY KEY,
    cargo VARCHAR(100) NOT NULL,
    nivel_acesso VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS paciente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE NOT NULL,
    endereco TEXT NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS profissionais (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE NOT NULL,
    id_cargo INTEGER REFERENCES cargos(id),
    cpf VARCHAR(14) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS atendimento (
    id SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL REFERENCES paciente(id),
    id_profissional INTEGER NOT NULL REFERENCES profissionais(id),
    data_e_hora_marcadas TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS calendario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data TIMESTAMP NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    id_atendimento INTEGER REFERENCES atendimento(id)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    id_cargo INTEGER REFERENCES cargos(id),
    id_profissional INTEGER REFERENCES profissionais(id),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Dados iniciais de cargos
INSERT INTO cargos (cargo, nivel_acesso) VALUES
    ('Atendente', 'ADMIN'),
    ('Médico', 'PROFISSIONAL'),
    ('Fisioterapeuta', 'PROFISSIONAL'),
    ('Enfermeiro', 'PROFISSIONAL'),
    ('Psicólogo', 'PROFISSIONAL')
ON CONFLICT DO NOTHING;
