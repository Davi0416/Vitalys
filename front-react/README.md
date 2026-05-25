# Vitalys — Frontend

Interface web do sistema de gestão clínica Vitalys, desenvolvida com **React 18 + TypeScript + Vite**.

## Tecnologias

| Tecnologia | Versão |
|---|---|
| React | 18 |
| TypeScript | 5.6 |
| Vite | 5.4 |
| React Router | 6 |

## Instalação

```bash
npm install
cp .env.example .env
```

Configure `.env`:

```env
VITE_API_URL=https://vitalys-gc27.onrender.com/vitalys
```

## Scripts

```bash
npm run dev      # servidor de desenvolvimento (http://localhost:5173)
npm run build    # build de produção → dist/
npm run preview  # pré-visualiza o build
```

## Deploy (Netlify)

O deploy é feito automaticamente via `netlify.toml` na raiz de `front-react/`. Variável de ambiente `VITE_API_URL` deve ser configurada em **Site settings → Environment variables** no painel do Netlify.

## Estrutura

```
src/
├── api/            # apiFetch, token helpers, getNomeUsuario
├── components/     # Header, Modal, Inicio, Pacientes, Profissionais,
│   │               # Agendamentos, Calendario, ErrorBoundary
│   └── modals/     # ModalAgendamento
├── contexts/       # AuthContext, DataContext
├── pages/          # Login, Dashboard
├── types/          # Interfaces TypeScript (Paciente, Profissional, Agendamento)
├── constants.ts    # TOKEN_KEY, THEME_KEY, API
└── utils.ts        # Formatação de datas, máscaras, helpers
```

Para documentação completa do projeto, consulte o [README principal](../README.md).
