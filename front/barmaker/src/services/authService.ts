import api from './api'
import type { Utilisateur } from '@/types'

export const authService = {
  async login(email: string, motDePasse: string): Promise<Utilisateur | null> {
    try {
      const { data } = await api.post<Utilisateur>('/auth/login', { email, motDePasse })
      return data.role === 'BARMAKER' ? data : null
    } catch {
      return null
    }
  }
}
