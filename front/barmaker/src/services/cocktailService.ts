import api from './api'
import type { Cocktail, TaillePrix } from '@/types'

export const cocktailService = {
  findAll: () => api.get<Cocktail[]>('/cocktails').then(r => r.data),

  findDisponibles: () => api.get<Cocktail[]>('/cocktails/disponibles').then(r => r.data),

  findActifs: () => api.get<Cocktail[]>('/cocktails/actifs').then(r => r.data),

  findById: (id: number) => api.get<Cocktail>(`/cocktails/${id}`).then(r => r.data),

  toggleActif: (id: number) => api.patch<void>(`/cocktails/${id}/actif`),

  getTaillesPrix: (cocktailId: number) =>
    api.get<TaillePrix[]>(`/tailles-prix/cocktail/${cocktailId}`).then(r => r.data),

  definirPrix: (cocktailId: number, prixS: number, prixM: number, prixL: number) =>
    api.put(`/tailles-prix/cocktail/${cocktailId}`, { prixS, prixM, prixL })
}
