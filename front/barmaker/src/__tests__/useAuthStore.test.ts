import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '@/stores/useAuthStore'
import { authService } from '@/services/authService'

vi.mock('@/services/authService')

const mockBarmaker = {
  id: 1,
  nom: 'Martin',
  prenom: 'Jean',
  email: 'jean@bar.fr',
  role: 'BARMAKER' as const
}

describe('useAuthStore (barmaker)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('login réussi retourne true et stocke l\'utilisateur', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockBarmaker)
    const store = useAuthStore()
    const ok = await store.login('jean@bar.fr')
    expect(ok).toBe(true)
    expect(store.utilisateur).toEqual(mockBarmaker)
    expect(store.estConnecte).toBe(true)
  })

  it('login échoué retourne false', async () => {
    vi.mocked(authService.login).mockResolvedValue(null)
    const store = useAuthStore()
    const ok = await store.login('inconnu@bar.fr')
    expect(ok).toBe(false)
    expect(store.utilisateur).toBeNull()
  })

  it('logout vide l\'utilisateur et le localStorage', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockBarmaker)
    const store = useAuthStore()
    await store.login('jean@bar.fr')
    store.logout()
    expect(store.utilisateur).toBeNull()
    expect(store.estConnecte).toBe(false)
    expect(localStorage.getItem('barmaker_user')).toBeNull()
  })

  it('persist en localStorage lors du login', async () => {
    vi.mocked(authService.login).mockResolvedValue(mockBarmaker)
    const store = useAuthStore()
    await store.login('jean@bar.fr')
    const stored = JSON.parse(localStorage.getItem('barmaker_user') ?? 'null')
    expect(stored?.id).toBe(1)
  })
})
