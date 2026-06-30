import api from './api'
import type { Utilisateur } from '@/types'

export const authService = {
  async login(email: string): Promise<Utilisateur | null> {
    const { data } = await api.get<Utilisateur[]>('/utilisateurs')
    return data.find(u => u.email === email && u.role === 'CLIENT') ?? null
  }
}
