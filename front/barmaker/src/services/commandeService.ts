import api from './api'
import type { Commande } from '@/types'

export const commandeService = {
  findEnAttente: () =>
    api.get<Commande[]>('/commandes/en-attente').then(r => r.data),

  findAll: () => api.get<Commande[]>('/commandes').then(r => r.data),

  findById: (id: number) =>
    api.get<Commande>(`/commandes/${id}`).then(r => r.data),

  prendreEnCharge: (commandeId: number, barmakerId: number) =>
    api.patch(`/commandes/${commandeId}/prendre-en-charge/${barmakerId}`),

  avancerLigne: (ligneId: number) =>
    api.patch(`/lignes-commande/${ligneId}/avancer`),

  annuler: (commandeId: number) =>
    api.patch(`/commandes/${commandeId}/annuler`)
}
