export function iniciais(nome) {
  if (!nome) return '?';
  return nome.trim().split(/\s+/).slice(0, 2).map(w => w[0]).join('').toUpperCase();
}

export function formatarData(data) {
  if (!data) return '-';
  const d = new Date(data);
  if (isNaN(d)) return '-';
  return d.toLocaleDateString('pt-BR');
}

export function formatarDataHora(timestamp) {
  if (!timestamp) return '-';
  const d = new Date(timestamp);
  if (isNaN(d)) return '-';
  return (
    d.toLocaleDateString('pt-BR') +
    ' às ' +
    d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
  );
}

export function dataParaBackend(dataStr) {
  if (!dataStr) return null;
  return new Date(dataStr + 'T00:00:00').getTime();
}

export function dataParaInput(data) {
  if (!data) return '';
  const d = new Date(data);
  if (isNaN(d)) return '';
  return d.toISOString().split('T')[0];
}

export function mascaraCpf(v) {
  return v
    .replace(/\D/g, '')
    .slice(0, 11)
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
}

export function mascaraTelefone(v) {
  return v
    .replace(/\D/g, '')
    .slice(0, 11)
    .replace(/(\d{2})(\d)/, '($1) $2')
    .replace(/(\d{5})(\d)/, '$1-$2');
}

export const HORARIOS = [
  '08:00','08:30','09:00','09:30','10:00','10:30',
  '11:00','11:30','13:00','13:30','14:00','14:30',
  '15:00','15:30','16:00','16:30',
];

export const MESES = [
  'Janeiro','Fevereiro','Março','Abril','Maio','Junho',
  'Julho','Agosto','Setembro','Outubro','Novembro','Dezembro',
];

export const DIAS_PT = [
  'domingo','segunda-feira','terça-feira','quarta-feira',
  'quinta-feira','sexta-feira','sábado',
];

export const MESES_PT = [
  'janeiro','fevereiro','março','abril','maio','junho',
  'julho','agosto','setembro','outubro','novembro','dezembro',
];
