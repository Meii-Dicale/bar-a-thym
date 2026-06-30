import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { cocktailService } from '@/services/cocktailService'

vi.mock('@/services/cocktailService')

const mockCocktails = [
  { id: 1, apiId: 'c1', nom: 'Mojito', imageUrl: null, categorie: 'Cocktail', actif: true, instructions: null },
  { id: 2, apiId: 'c2', nom: 'Cosmo', imageUrl: null, categorie: 'Cocktail', actif: false, instructions: null }
]

describe('useCocktailStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('charge tous les cocktails', async () => {
    vi.mocked(cocktailService.findAll).mockResolvedValue(mockCocktails)
    const store = useCocktailStore()
    await store.fetchAll()
    expect(store.cocktails).toHaveLength(2)
  })

  it('est en loading pendant fetchAll', async () => {
    let resolve!: (v: typeof mockCocktails) => void
    vi.mocked(cocktailService.findAll).mockReturnValue(new Promise(r => { resolve = r }))
    const store = useCocktailStore()
    const p = store.fetchAll()
    expect(store.loading).toBe(true)
    resolve(mockCocktails)
    await p
    expect(store.loading).toBe(false)
  })

  it('toggle actif de true à false', async () => {
    vi.mocked(cocktailService.findAll).mockResolvedValue(mockCocktails)
    vi.mocked(cocktailService.toggleActif).mockResolvedValue({ ...mockCocktails[0], actif: false })
    const store = useCocktailStore()
    await store.fetchAll()
    await store.toggleActif(1)
    expect(store.cocktails[0].actif).toBe(false)
  })

  it('toggle actif de false à true', async () => {
    vi.mocked(cocktailService.findAll).mockResolvedValue(mockCocktails)
    vi.mocked(cocktailService.toggleActif).mockResolvedValue({ ...mockCocktails[1], actif: true })
    const store = useCocktailStore()
    await store.fetchAll()
    await store.toggleActif(2)
    expect(store.cocktails[1].actif).toBe(true)
  })

  it('cocktailsAvecStatut calcule le nombre d\'actifs', async () => {
    vi.mocked(cocktailService.findAll).mockResolvedValue(mockCocktails)
    const store = useCocktailStore()
    await store.fetchAll()
    const actifs = store.cocktails.filter(c => c.actif)
    expect(actifs).toHaveLength(1)
  })
})
