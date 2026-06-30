import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useIngredientStore } from '@/stores/useIngredientStore'
import { ingredientService } from '@/services/ingredientService'

vi.mock('@/services/ingredientService')

const mockIngredients = [
  { id: 1, apiId: 'vodka', nom: 'Vodka', imageUrl: null, disponible: true },
  { id: 2, apiId: 'citron', nom: 'Citron', imageUrl: null, disponible: false }
]

describe('useIngredientStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('charge la liste des ingrédients', async () => {
    vi.mocked(ingredientService.findAll).mockResolvedValue(mockIngredients)
    const store = useIngredientStore()
    await store.fetchAll()
    expect(store.ingredients).toHaveLength(2)
    expect(store.ingredients[0].nom).toBe('Vodka')
  })

  it('est en loading pendant le chargement', async () => {
    let resolve!: (v: typeof mockIngredients) => void
    vi.mocked(ingredientService.findAll).mockReturnValue(new Promise(r => { resolve = r }))
    const store = useIngredientStore()
    const promise = store.fetchAll()
    expect(store.loading).toBe(true)
    resolve(mockIngredients)
    await promise
    expect(store.loading).toBe(false)
  })

  it('toggle disponible de false à true (optimiste)', async () => {
    vi.mocked(ingredientService.findAll).mockResolvedValue(mockIngredients)
    vi.mocked(ingredientService.toggleDisponible).mockResolvedValue(
      { ...mockIngredients[1], disponible: true }
    )
    const store = useIngredientStore()
    await store.fetchAll()
    expect(store.ingredients[1].disponible).toBe(false)
    store.toggleDisponible(2)
    expect(store.ingredients[1].disponible).toBe(true)
  })

  it('toggle disponible de true à false (optimiste)', async () => {
    vi.mocked(ingredientService.findAll).mockResolvedValue(mockIngredients)
    vi.mocked(ingredientService.toggleDisponible).mockResolvedValue(
      { ...mockIngredients[0], disponible: false }
    )
    const store = useIngredientStore()
    await store.fetchAll()
    store.toggleDisponible(1)
    expect(store.ingredients[0].disponible).toBe(false)
  })

  it('restaure la valeur si le toggle échoue', async () => {
    vi.mocked(ingredientService.findAll).mockResolvedValue(mockIngredients)
    vi.mocked(ingredientService.toggleDisponible).mockRejectedValue(new Error('erreur'))
    const store = useIngredientStore()
    await store.fetchAll()
    await store.toggleDisponible(2)
    expect(store.ingredients[1].disponible).toBe(false)
  })
})
