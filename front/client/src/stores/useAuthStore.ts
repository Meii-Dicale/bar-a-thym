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
    const result = await authService.login(email, motDePasse)
    if (result) {
      utilisateur.value = result.utilisateur
      localStorage.setItem('client_user', JSON.stringify(result.utilisateur))
      localStorage.setItem('client_token', result.token)
      return true
    }
    return false
  }

  function logout() {
    utilisateur.value = null
    localStorage.removeItem('client_user')
    localStorage.removeItem('client_token')
  }

  return { utilisateur, estConnecte, login, logout }
})
