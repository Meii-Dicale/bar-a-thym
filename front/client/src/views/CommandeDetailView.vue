<template>
  <div class="pa-4">
    <v-btn
      variant="text"
      color="primary"
      prepend-icon="mdi-arrow-left"
      class="mb-4 px-0"
      to="/commandes"
    >
      Retour
    </v-btn>

    <div v-if="store.commandeSelectionnee">
      <div class="d-flex justify-space-between align-center mb-6">
        <h2 class="font-fraunces" style="font-size: 24px; color: #1F2421;">
          Commande n°{{ store.commandeSelectionnee.id }}
        </h2>
        <v-chip
          :color="couleurStatut(store.commandeSelectionnee.statut)"
          variant="tonal"
        >
          {{ STATUT_COMMANDE_LABELS[store.commandeSelectionnee.statut] }}
        </v-chip>
      </div>

      <div style="display: flex; flex-direction: column; gap: 10px; margin-bottom: 20px;">
        <v-card
          v-for="ligne in store.commandeSelectionnee.lignes"
          :key="ligne.id"
          rounded="xl"
          elevation="0"
          border
        >
          <v-card-text class="pa-4">
            <div class="d-flex justify-space-between align-center mb-3">
              <span class="font-weight-semibold" style="color: #1F2421;">
                {{ ligne.cocktailNom }}
              </span>
              <v-chip size="x-small" :color="couleurEtape(ligne.statut)" variant="tonal">
                {{ STATUT_LIGNE_LABELS[ligne.statut] }}
              </v-chip>
            </div>

            <v-progress-linear
              :model-value="progressionLigne(ligne.statut)"
              color="primary"
              height="3"
              rounded
            />
          </v-card-text>
        </v-card>
      </div>

      <v-card rounded="xl" elevation="0" border>
        <v-card-text class="d-flex justify-space-between pa-5">
          <span style="color: rgba(31,36,33,0.7);">Total</span>
          <span class="font-fraunces font-weight-bold" style="font-size: 22px; color: #C98E5F;">
            {{ store.commandeSelectionnee.total.toFixed(2) }} €
          </span>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { STATUT_COMMANDE_LABELS, STATUT_LIGNE_LABELS } from '@/types'
import type { LigneCommande, Commande } from '@/types'

const route = useRoute()
const store = useCommandeStore()

const ordreEtapes = ['PREPARATION', 'ASSEMBLAGE', 'DRESSAGE', 'TERMINEE']

function progressionLigne(statut: LigneCommande['statut']): number {
  const idx = ordreEtapes.indexOf(statut)
  return ((idx + 1) / ordreEtapes.length) * 100
}

function couleurEtape(statut: LigneCommande['statut']): string {
  if (statut === 'TERMINEE') return 'success'
  if (statut === 'DRESSAGE') return 'primary'
  return 'warning'
}

function couleurStatut(statut: Commande['statut']): string {
  const map: Record<Commande['statut'], string> = {
    COMMANDEE: 'warning',
    EN_COURS:  'primary',
    TERMINEE:  'success'
  }
  return map[statut]
}

onMounted(() => {
  store.fetchDetail(Number(route.params.id))
})
</script>

<style scoped>
</style>
