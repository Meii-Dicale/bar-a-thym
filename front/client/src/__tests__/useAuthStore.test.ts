import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '@/stores/useAuthStore'
import { authService } from '@/services/authService'

vi.mock('@/services/authService')

const mockClient = {
  id: 2, nom: 'Dupont', prenom: 'Marie',
  email: 'marie@client.fr', role: 'CLIENT' as const
}

describe('useAuthStore (client)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('login réussi retourne true', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockClient)
    const store = useAuthStore()
    const ok = await store.login('marie@client.fr')
    expect(ok).toBe(true)
    expect(store.estConnecte).toBe(true)
    expect(store.utilisateur?.email).toBe('marie@client.fr')
  })

  it('login échoué retourne false', async () => {
    vi.mocked(authService.login).mockResolvedValue(null)
    const store = useAuthStore()
    const ok = await store.login('nope@nope.fr')
    expect(ok).toBe(false)
    expect(store.utilisateur).toBeNull()
  })

  it('logout efface tout', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockClient)
    const store = useAuthStore()
    await store.login('marie@client.fr')
    store.logout()
    expect(store.estConnecte).toBe(false)
    expect(store.utilisateur).toBeNull()
    expect(localStorage.getItem('client_user')).toBeNull()
  })

  it('persist dans localStorage', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockClient)
    const store = useAuthStore()
    await store.login('marie@client.fr')
    const stored = JSON.parse(localStorage.getItem('client_user') ?? 'null')
    expect(stored?.role).toBe('CLIENT')
  })
})
