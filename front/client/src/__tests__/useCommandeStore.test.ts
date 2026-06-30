import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { usePanierStore } from '@/stores/usePanierStore'
import { commandeService } from '@/services/commandeService'
import type { ArticlePanier } from '@/types'

vi.mock('@/services/commandeService')

const article: ArticlePanier = {
  cocktailId: 1, cocktailNom: 'Mojito', cocktailImageUrl: null,
  taillePrixId: 2, taille: 'M', prix: 9.5, note: 'sans glaçons'
}

const mockCommande = {
  id: 10, clientId: 3, barmakerId: null,
  statut: 'COMMANDEE' as const, total: 9.5, creeLe: '2026-06-30T10:00:00',
  lignes: []
}

describe('useCommandeStore (client)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('passerCommande crée la commande et vide le panier', async () => {
    vi.mocked(commandeService.create).mockResolvedValue(mockCommande)
    const commandeStore = useCommandeStore()
    const panierStore = usePanierStore()
    panierStore.ajouterArticle(article)
    expect(panierStore.articles).toHaveLength(1)

    await commandeStore.passerCommande(3)

    expect(panierStore.articles).toHaveLength(0)
    expect(commandeStore.commandes).toHaveLength(1)
    expect(commandeStore.commandes[0].id).toBe(10)
  })

  it('fetchMesCommandes charge les commandes du client', async () => {
    vi.mocked(commandeService.findByClient).mockResolvedValue([mockCommande])
    const store = useCommandeStore()
    await store.fetchMesCommandes(3)
    expect(store.commandes).toHaveLength(1)
    expect(store.loading).toBe(false)
  })

  it('fetchDetail charge la commande sélectionnée', async () => {
    vi.mocked(commandeService.findById).mockResolvedValue(mockCommande)
    const store = useCommandeStore()
    await store.fetchDetail(10)
    expect(store.commandeSelectionnee?.id).toBe(10)
  })

  it('passerCommande envoie les bonnes données', async () => {
    vi.mocked(commandeService.create).mockResolvedValue(mockCommande)
    const commandeStore = useCommandeStore()
    const panierStore = usePanierStore()
    panierStore.ajouterArticle(article)

    await commandeStore.passerCommande(3)

    expect(commandeService.create).toHaveBeenCalledWith({
      clientId: 3,
      lignes: [{ cocktailId: 1, taillePrixId: 2, note: 'sans glaçons' }]
    })
  })
})
