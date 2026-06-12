/**
 * Load test — mede requisições em 10 segundos
 * Uso: node loadtest.js <BASE_URL> [TOKEN] [CONCORRENCIA]
 *
 * Exemplos:
 *   node loadtest.js http://localhost:8080
 *   node loadtest.js https://vitalys-gc27.onrender.com eyJhbGci... 20
 */

const https = require('https');
const http = require('http');

const BASE_URL  = process.argv[2] || 'http://localhost:8080';
const TOKEN     = process.argv[3] || '';
const CONCURR   = parseInt(process.argv[4] || '10', 10);
const DURATION  = 10_000; // ms

const ENDPOINT  = `${BASE_URL}/vitalys/pacientes`;
const url       = new URL(ENDPOINT);
const client    = url.protocol === 'https:' ? https : http;

const options = {
  hostname: url.hostname,
  port:     url.port || (url.protocol === 'https:' ? 443 : 80),
  path:     url.pathname,
  method:   'GET',
  headers: {
    'Content-Type': 'application/json',
    ...(TOKEN ? { Authorization: `Bearer ${TOKEN}` } : {}),
  },
};

let total   = 0;
let ok      = 0;
let errors  = 0;
let running = true;

const buckets = {}; // status code → count

function request() {
  if (!running) return;

  const req = client.request(options, (res) => {
    res.resume();
    total++;
    buckets[res.statusCode] = (buckets[res.statusCode] || 0) + 1;
    if (res.statusCode < 400) ok++;
    else errors++;
    request(); // próxima imediatamente
  });

  req.on('error', () => {
    total++;
    errors++;
    request();
  });

  req.end();
}

console.log(`\n🔥 Load test — ${DURATION / 1000}s | concorrência: ${CONCURR}`);
console.log(`   Endpoint: GET ${ENDPOINT}\n`);

const start = Date.now();

// Inicia N "workers" paralelos
for (let i = 0; i < CONCURR; i++) request();

setTimeout(() => {
  running = false;
  const elapsed = (Date.now() - start) / 1000;
  const rps = (total / elapsed).toFixed(1);

  console.log('─'.repeat(40));
  console.log(`Duração real  : ${elapsed.toFixed(2)}s`);
  console.log(`Total         : ${total} requisições`);
  console.log(`✅ Sucesso     : ${ok}`);
  console.log(`❌ Erro        : ${errors}`);
  console.log(`⚡ Throughput  : ${rps} req/s`);
  console.log('\nDistribuição de status:');
  Object.entries(buckets).sort().forEach(([code, n]) =>
    console.log(`  HTTP ${code}: ${n}`)
  );
  console.log('─'.repeat(40));
}, DURATION);
