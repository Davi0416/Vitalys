import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  resolve: {
    // Prioriza .ts/.tsx sobre .js/.jsx para que os arquivos TypeScript
    // sejam carregados mesmo que os antigos .jsx ainda existam
    extensions: ['.mts', '.ts', '.tsx', '.mjs', '.js', '.jsx', '.json'],
  },
});
