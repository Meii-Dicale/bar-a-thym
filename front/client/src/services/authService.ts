import api from './api'
import type { Utilisateur } from '@/types'

export interface InscriptionPayload {
  nom: string
  prenom: string
  email: string
  motDePasse: string
}

export const authService = {
  async login(email: string): Promise<Utilisateur | null> {
    const { data } = await api.get<Utilisateur[]>('/utilisateurs')
    return data.find(u => u.email === email && u.role === 'CLIENT') ?? null
  },

  async inscription(payload: InscriptionPayload): Promise<void> {
    await api.post('/utilisateurs', { ...payload, role: 'CLIENT' })
  }
}
