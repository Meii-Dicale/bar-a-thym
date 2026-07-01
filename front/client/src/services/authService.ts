import api from './api'
import type { Utilisateur } from '@/types'

export interface InscriptionPayload {
  nom: string
  prenom: string
  email: string
  motDePasse: string
}

export interface LoginResponse {
  token: string
  utilisateur: Utilisateur
}

export const authService = {
  async login(email: string, motDePasse: string): Promise<LoginResponse | null> {
    try {
      const { data } = await api.post<LoginResponse>('/auth/login', { email, motDePasse })
      return data.utilisateur.role === 'CLIENT' ? data : null
    } catch {
      return null
    }
  },

  async inscription(payload: InscriptionPayload): Promise<void> {
    await api.post('/utilisateurs', { ...payload, role: 'CLIENT' })
  }
}
