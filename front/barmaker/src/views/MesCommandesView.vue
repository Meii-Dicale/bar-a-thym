<template>
  <div>
    <h1 class="font-fraunces mb-8" style="font-size: 40px; color: #1F2421;">
      Mes commandes en cours
    </h1>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <p v-if="!store.loading && store.commandesEnCours.length === 0" style="color: rgba(31,36,33,0.6);">
      Aucune commande en cours.
    </p>

    <v-row>
      <v-col
        v-for="commande in store.commandesEnCours"
        :key="commande.id"
        cols="12" md="6"
      >
        <v-card rounded="xl" elevation="0" border class="pa-2">
          <v-card-text>
            <div class="d-flex justify-space-between align-center mb-3">
              <span class="font-weight-bold" style="font-size: 18px; color: #1F2421;">
                Commande n°{{ commande.id }}
              </span>
              <v-chip size="small" variant="tonal" color="primary">
                {{ commande.lignes.length }} verre{{ commande.lignes.length > 1 ? 's' : '' }}
              </v-chip>
            </div>

            <ul style="list-style: none; padding: 0; margin: 0 0 12px;">
              <li
                v-for="ligne in commande.lignes"
                :key="ligne.id"
                style="color: rgba(31,36,33,0.8); font-size: 14px; margin-bottom: 4px;"
              >
                • {{ ligne.cocktailNom }} x1
              </li>
            </ul>

            <v-progress-linear
              :model-value="progression(commande)"
              color="primary"
              height="4"
              rounded
              class="mb-2"
            />
            <div class="d-flex justify-space-between" style="font-size: 11px; color: rgba(31,36,33,0.5);">
              <span>Ingrédients</span>
              <span>Assemblage</span>
              <span>Service</span>
            </div>

            <v-btn
              variant="outlined"
              color="primary"
              rounded="lg"
              block
              class="mt-4"
              :to="`/commandes/${commande.id}`"
            >
              Gérer la commande
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { useAuthStore } from '@/stores/useAuthStore'
import type { Commande } from '@/types'

const store = useCommandeStore()
const authStore = useAuthStore()

onMounted(() => {
  if (authStore.utilisateur) {
    store.fetchMesCommandes(authStore.utilisateur.id)
  }
})

function progression(commande: Commande): number {
  if (!commande.lignes.length) return 0
  const poids = { PREPARATION: 25, ASSEMBLAGE: 50, DRESSAGE: 75, TERMINEE: 100 }
  const total = commande.lignes.reduce((sum, l) => sum + poids[l.statut], 0)
  return total / commande.lignes.length
}
</script>

<style scoped>
</style>
