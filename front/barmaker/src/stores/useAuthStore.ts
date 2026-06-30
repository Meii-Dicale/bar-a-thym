import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Utilisateur } from '@/types'
import { authService } from '@/services/authService'

export const useAuthStore = defineStore('auth', () => {
  const utilisateur = ref<Utilisateur | null>(
    JSON.parse(localStorage.getItem('barmaker_user') ?? 'null')
  )

  const isAuthenticated = ref(utilisateur.value !== null)

  async function login(email: string): Promise<boolean> {
    const user = await authService.login(email)
    if (user) {
      utilisateur.value = user
      isAuthenticated.value = true
      localStorage.setItem('barmaker_user', JSON.stringify(user))
      return true
    }
    return false
  }

  function logout() {
    utilisateur.value = null
    isAuthenticated.value = false
    localStorage.removeItem('barmaker_user')
  }

  return { utilisateur, isAuthenticated, login, logout }
})
