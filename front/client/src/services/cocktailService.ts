import api from './api'
import type { Cocktail, TaillePrix } from '@/types'

export const cocktailService = {
  async findActifs(): Promise<Cocktail[]> {
    const { data } = await api.get<Cocktail[]>('/cocktails/actifs')
    return data
  },

  async findById(id: number): Promise<Cocktail> {
    const { data } = await api.get<Cocktail>(`/cocktails/${id}`)
    return data
  },

  async getTaillesPrix(cocktailId: number): Promise<TaillePrix[]> {
    const { data } = await api.get<TaillePrix[]>(`/tailles-prix/cocktail/${cocktailId}`)
    return data
  }
}
