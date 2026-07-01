import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Utilisateur } from '@/types'
import { authService } from '@/services/authService'

export const useAuthStore = defineStore('auth', () => {
  const utilisateur = ref<Utilisateur | null>(
    JSON.parse(localStorage.getItem('barmaker_user') ?? 'null')
  )

  const isAuthenticated = ref(utilisateur.value !== null)

  async function login(email: string, motDePasse: string): Promise<boolean> {
    const result = await authService.login(email, motDePasse)
    if (result) {
      utilisateur.value = result.utilisateur
      isAuthenticated.value = true
      localStorage.setItem('barmaker_user', JSON.stringify(result.utilisateur))
      localStorage.setItem('barmaker_token', result.token)
      return true
    }
    return false
  }

  function logout() {
    utilisateur.value = null
    isAuthenticated.value = false
    localStorage.removeItem('barmaker_user')
    localStorage.removeItem('barmaker_token')
  }

  return { utilisateur, isAuthenticated, login, logout }
})
