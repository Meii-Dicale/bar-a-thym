<template>
  <div>
    <h1 class="font-fraunces mb-8" style="font-size: 40px; color: #1F2421;">
      Commandes entrantes
    </h1>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <p v-if="!store.loading && store.commandesEnAttente.length === 0" style="color: rgba(31,36,33,0.6);">
      Aucune commande en attente.
    </p>

    <v-row>
      <v-col
        v-for="commande in store.commandesEnAttente"
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

            <ul style="list-style: none; padding: 0; margin: 0 0 16px;">
              <li
                v-for="ligne in commande.lignes"
                :key="ligne.id"
                style="color: rgba(31,36,33,0.8); font-size: 14px; margin-bottom: 4px;"
              >
                • {{ ligne.cocktailNom }} x1
              </li>
            </ul>

            <div class="d-flex" style="gap: 8px;">
              <v-btn
                color="primary"
                rounded="lg"
                flex="1"
                style="flex: 1;"
                @click="prendreEnCharge(commande.id)"
              >
                Prendre en charge
              </v-btn>
              <v-btn variant="outlined" rounded="lg" @click="refuser(commande.id)">
                Refuser
              </v-btn>
            </div>
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

const store = useCommandeStore()
const authStore = useAuthStore()

onMounted(() => store.fetchEnAttente())

async function prendreEnCharge(commandeId: number) {
  if (!authStore.utilisateur) return
  await store.prendreEnCharge(commandeId, authStore.utilisateur.id)
}

function refuser(commandeId: number) {
  store.commandesEnAttente = store.commandesEnAttente.filter(c => c.id !== commandeId)
}
</script>

<style scoped>
</style>
