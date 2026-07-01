import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Cocktail } from '@/types'
import { cocktailService } from '@/services/cocktailService'
import { useIngredientStore } from './useIngredientStore'

export const useCocktailStore = defineStore('cocktails', () => {
  const cocktails = ref<Cocktail[]>([])
  const loading = ref(false)

  const cocktailsAvecStatut = computed(() => {
    const ingredientStore = useIngredientStore()
    return cocktails.value.map(c => ({
      ...c,
      manquant: !c.actif && ingredientStore.ingredients.some(i => !i.disponible)
    }))
  })

  async function fetchAll() {
    loading.value = true
    cocktails.value = await cocktailService.findAll()
    loading.value = false
  }

  async function fetchDisponibles() {
    loading.value = true
    cocktails.value = await cocktailService.findDisponibles()
    loading.value = false
  }

  async function fetchActifs() {
    loading.value = true
    cocktails.value = await cocktailService.findActifs()
    loading.value = false
  }

  async function toggleActif(id: number) {
    await cocktailService.toggleActif(id)
    const c = cocktails.value.find(c => c.id === id)
    if (c) c.actif = !c.actif
  }

  return { cocktails, cocktailsAvecStatut, loading, fetchAll, fetchDisponibles, fetchActifs, toggleActif }
})
