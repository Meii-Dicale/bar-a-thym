import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Utilisateur } from '@/types'
import { authService } from '@/services/authService'

export const useAuthStore = defineStore('auth', () => {
  const utilisateur = ref<Utilisateur | null>(
    JSON.parse(localStorage.getItem('client_user') ?? 'null')
  )

  const estConnecte = computed(() => utilisateur.value !== null)

  async function login(email: string, motDePasse: string): Promise<boolean> {
    const user = await authService.login(email, motDePasse)
    if (user) {
      utilisateur.value = user
      localStorage.setItem('client_user', JSON.stringify(user))
      return true
    }
    return false
  }

  function logout() {
    utilisateur.value = null
    localStorage.removeItem('client_user')
  }

  return { utilisateur, estConnecte, login, logout }
})
