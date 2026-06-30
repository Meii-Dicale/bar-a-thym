import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Commande } from '@/types'
import { commandeService } from '@/services/commandeService'
import { usePanierStore } from './usePanierStore'

export const useCommandeStore = defineStore('commandes', () => {
  const commandes = ref<Commande[]>([])
  const commandeSelectionnee = ref<Commande | null>(null)
  const loading = ref(false)

  async function passerCommande(clientId: number): Promise<Commande> {
    const panierStore = usePanierStore()
    const payload = {
      clientId,
      lignes: panierStore.articles.map(a => ({
        cocktailId: a.cocktailId,
        taillePrixId: a.taillePrixId,
        note: a.note
      }))
    }
    const commande = await commandeService.create(payload)
    commandes.value.unshift(commande)
    panierStore.vider()
    return commande
  }

  async function fetchMesCommandes(clientId: number) {
    loading.value = true
    commandes.value = await commandeService.findByClient(clientId)
    loading.value = false
  }

  async function fetchDetail(id: number) {
    commandeSelectionnee.value = await commandeService.findById(id)
  }

  return {
    commandes,
    commandeSelectionnee,
    loading,
    passerCommande,
    fetchMesCommandes,
    fetchDetail
  }
})
