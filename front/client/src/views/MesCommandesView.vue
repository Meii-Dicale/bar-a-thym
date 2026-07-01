<template>
  <div class="pa-4">
    <h2 class="font-fraunces mb-6" style="font-size: 28px; color: #1F2421;">
      Mes commandes
    </h2>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <div v-if="!store.loading && store.commandes.length === 0" class="text-center py-12">
      <v-icon icon="mdi-clipboard-list-outline" size="64" color="grey-lighten-1" class="mb-4" />
      <p style="color: rgba(31,36,33,0.5);">Aucune commande pour le moment</p>
    </div>

    <div style="display: flex; flex-direction: column; gap: 10px;">
      <v-card
        v-for="commande in store.commandes"
        :key="commande.id"
        rounded="xl"
        elevation="0"
        border
        :to="`/commandes/${commande.id}`"
      >
        <v-card-text class="pa-4">
          <div class="d-flex justify-space-between align-center mb-2">
            <span class="font-weight-bold" style="color: #1F2421;">
              Commande n°{{ commande.id }}
            </span>
            <v-chip
              :color="couleurStatut(commande.statut)"
              size="small"
              variant="tonal"
            >
              {{ STATUT_COMMANDE_LABELS[commande.statut] }}
            </v-chip>
          </div>
          <div class="d-flex justify-space-between" style="font-size: 13px; color: rgba(31,36,33,0.6);">
            <span>{{ commande.lignes.length }} cocktail{{ commande.lignes.length > 1 ? 's' : '' }}</span>
            <span class="font-weight-semibold" style="color: #C98E5F;">{{ commande.total.toFixed(2) }} €</span>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { useAuthStore } from '@/stores/useAuthStore'
import { STATUT_COMMANDE_LABELS } from '@/types'
import type { Commande } from '@/types'

const store = useCommandeStore()
const authStore = useAuthStore()

onMounted(() => {
  if (authStore.utilisateur) {
    store.fetchMesCommandes(authStore.utilisateur.id)
  }
})

function couleurStatut(statut: Commande['statut']): string {
  const map: Record<Commande['statut'], string> = {
    COMMANDEE: 'warning',
    EN_COURS:  'primary',
    TERMINEE:  'success',
    ANNULEE:   'error'
  }
  return map[statut]
}
</script>

<style scoped>
</style>
