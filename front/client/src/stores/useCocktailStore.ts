import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Cocktail } from '@/types'
import { cocktailService } from '@/services/cocktailService'

export const useCocktailStore = defineStore('cocktails', () => {
  const cocktails = ref<Cocktail[]>([])
  const loading = ref(false)

  async function fetchActifs() {
    loading.value = true
    cocktails.value = await cocktailService.findActifs()
    loading.value = false
  }

  return { cocktails, loading, fetchActifs }
})
