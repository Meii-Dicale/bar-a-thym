import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { cocktailService } from '@/services/cocktailService'

vi.mock('@/services/cocktailService')

const mockActifs = [
  { id: 1, apiId: 'c1', nom: 'Mojito', imageUrl: null, categorie: 'Classic', actif: true, instructions: null },
  { id: 2, apiId: 'c2', nom: 'Caipirinha', imageUrl: null, categorie: 'Classic', actif: true, instructions: null }
]

describe('useCocktailStore (client)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('charge uniquement les cocktails actifs', async () => {
    vi.mocked(cocktailService.findActifs).mockResolvedValue(mockActifs)
    const store = useCocktailStore()
    await store.fetchActifs()
    expect(store.cocktails).toHaveLength(2)
    expect(store.cocktails.every(c => c.actif)).toBe(true)
  })

  it('loading est true pendant le chargement', async () => {
    let resolve!: (v: typeof mockActifs) => void
    vi.mocked(cocktailService.findActifs).mockReturnValue(new Promise(r => { resolve = r }))
    const store = useCocktailStore()
    const p = store.fetchActifs()
    expect(store.loading).toBe(true)
    resolve(mockActifs)
    await p
    expect(store.loading).toBe(false)
  })

  it('liste vide si aucun cocktail actif', async () => {
    vi.mocked(cocktailService.findActifs).mockResolvedValue([])
    const store = useCocktailStore()
    await store.fetchActifs()
    expect(store.cocktails).toHaveLength(0)
  })
})
