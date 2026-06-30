import api from './api'
import type { Ingredient } from '@/types'

export const ingredientService = {
  findAll: () => api.get<Ingredient[]>('/ingredients').then(r => r.data),

  toggleDisponible: (id: number) =>
    api.patch<void>(`/ingredients/${id}/disponible`)
}
