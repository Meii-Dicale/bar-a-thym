import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Commande } from '@/types'
import { commandeService } from '@/services/commandeService'

export const useCommandeStore = defineStore('commandes', () => {
  const commandesEnAttente = ref<Commande[]>([])
  const commandesEnCours = ref<Commande[]>([])
  const commandeSelectionnee = ref<Commande | null>(null)
  const loading = ref(false)

  async function fetchEnAttente() {
    loading.value = true
    commandesEnAttente.value = await commandeService.findEnAttente()
    loading.value = false
  }

  async function fetchMesCommandes(barmakerId: number) {
    loading.value = true
    const toutes = await commandeService.findAll()
    commandesEnCours.value = toutes.filter(
      c => c.barmakerId === barmakerId && c.statut === 'EN_COURS'
    )
    loading.value = false
  }

  async function fetchDetail(id: number) {
    commandeSelectionnee.value = await commandeService.findById(id)
  }

  async function prendreEnCharge(commandeId: number, barmakerId: number) {
    await commandeService.prendreEnCharge(commandeId, barmakerId)
    commandesEnAttente.value = commandesEnAttente.value.filter(c => c.id !== commandeId)
  }

  async function avancerLigne(ligneId: number) {
    await commandeService.avancerLigne(ligneId)
    if (commandeSelectionnee.value) {
      await fetchDetail(commandeSelectionnee.value.id)
    }
  }

  return {
    commandesEnAttente,
    commandesEnCours,
    commandeSelectionnee,
    loading,
    fetchEnAttente,
    fetchMesCommandes,
    fetchDetail,
    prendreEnCharge,
    avancerLigne
  }
})
