import api from './api'
import type { Commande } from '@/types'

export interface LigneCommandeRequest {
  cocktailId: number
  taillePrixId: number
  note: string
}

export interface CommandeRequest {
  clientId: number
  lignes: LigneCommandeRequest[]
}

export const commandeService = {
  async create(payload: CommandeRequest): Promise<Commande> {
    const { data } = await api.post<Commande>('/commandes', payload)
    return data
  },

  async findByClient(clientId: number): Promise<Commande[]> {
    const { data } = await api.get<Commande[]>(`/commandes/client/${clientId}`)
    return data
  },

  async findById(id: number): Promise<Commande> {
    const { data } = await api.get<Commande>(`/commandes/${id}`)
    return data
  }
}
