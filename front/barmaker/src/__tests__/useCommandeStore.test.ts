import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { commandeService } from '@/services/commandeService'

vi.mock('@/services/commandeService')

const mockLigne = {
  id: 1, commandeId: 10, cocktailId: 1, cocktailNom: 'Mojito',
  taillePrixId: 1, note: null, statut: 'PREPARATION' as const
}
const mockCommande = {
  id: 10, clientId: 2, barmakerId: null,
  statut: 'COMMANDEE' as const, total: 12, creeLe: '2026-06-30T10:00:00',
  lignes: [mockLigne]
}

describe('useCommandeStore (barmaker)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('fetchEnAttente charge les commandes en attente', async () => {
    vi.mocked(commandeService.findEnAttente).mockResolvedValue([mockCommande])
    const store = useCommandeStore()
    await store.fetchEnAttente()
    expect(store.commandesEnAttente).toHaveLength(1)
    expect(store.commandesEnAttente[0].id).toBe(10)
  })

  it('prendreEnCharge retire la commande des commandes en attente', async () => {
    vi.mocked(commandeService.findEnAttente).mockResolvedValue([mockCommande])
    vi.mocked(commandeService.prendreEnCharge).mockResolvedValue(undefined as any)
    const store = useCommandeStore()
    await store.fetchEnAttente()
    await store.prendreEnCharge(10, 1)
    expect(store.commandesEnAttente).toHaveLength(0)
  })

  it('fetchDetail charge la commande sélectionnée', async () => {
    vi.mocked(commandeService.findById).mockResolvedValue(mockCommande)
    const store = useCommandeStore()
    await store.fetchDetail(10)
    expect(store.commandeSelectionnee?.id).toBe(10)
  })

  it('avancerLigne rafraîchit la commande sélectionnée', async () => {
    vi.mocked(commandeService.findById).mockResolvedValue(mockCommande)
    vi.mocked(commandeService.avancerLigne).mockResolvedValue(undefined as any)
    const store = useCommandeStore()
    store.commandeSelectionnee = mockCommande
    await store.avancerLigne(1)
    expect(commandeService.findById).toHaveBeenCalledWith(10)
  })

  it('fetchMesCommandes filtre les commandes en cours du barmaker', async () => {
    const enCours = { ...mockCommande, statut: 'EN_COURS' as const, barmakerId: 5 }
    const autre   = { ...mockCommande, id: 11, barmakerId: 3 }
    vi.mocked(commandeService.findAll).mockResolvedValue([enCours, autre])
    const store = useCommandeStore()
    await store.fetchMesCommandes(5)
    expect(store.commandesEnCours).toHaveLength(1)
    expect(store.commandesEnCours[0].id).toBe(10)
  })
})
