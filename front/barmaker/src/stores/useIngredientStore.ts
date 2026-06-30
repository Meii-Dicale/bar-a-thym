import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Ingredient } from '@/types'
import { ingredientService } from '@/services/ingredientService'

export const useIngredientStore = defineStore('ingredients', () => {
  const ingredients = ref<Ingredient[]>([])
  const loading = ref(false)

  async function fetchAll() {
    loading.value = true
    ingredients.value = await ingredientService.findAll()
    loading.value = false
  }

  async function toggleDisponible(id: number) {
    await ingredientService.toggleDisponible(id)
    const ing = ingredients.value.find(i => i.id === id)
    if (ing) ing.disponible = !ing.disponible
  }

  return { ingredients, loading, fetchAll, toggleDisponible }
})
