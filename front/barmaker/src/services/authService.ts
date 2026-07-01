import api from './api'
import type { Utilisateur } from '@/types'

export interface LoginResponse {
  token: string
  utilisateur: Utilisateur
}

export const authService = {
  async login(email: string, motDePasse: string): Promise<LoginResponse | null> {
    try {
      const { data } = await api.post<LoginResponse>('/auth/login', { email, motDePasse })
      return data.utilisateur.role === 'BARMAKER' ? data : null
    } catch {
      return null
    }
  }
}
