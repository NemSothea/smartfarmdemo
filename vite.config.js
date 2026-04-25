import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  // For GitHub Pages (project pages), set `VITE_BASE=/<repo>/` at build time.
  // Defaults to `/` for local dev and other hosts.
  base: process.env.VITE_BASE ?? '/',
  plugins: [react()],
})
