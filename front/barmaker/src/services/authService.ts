import api from './api'
import type { Utilisateur } from '@/types'

export const authService = {
  login: async (email: string): Promise<Utilisateur | null> => {
    const utilisateurs = await api.get<Utilisateur[]>('/utilisateurs').then(r => r.data)
    return utilisateurs.find(u => u.email === email && u.role === 'BARMAKER') ?? null
  }
}
