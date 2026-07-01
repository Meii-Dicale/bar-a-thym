<template>
  <div style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">
    <h1 class="font-fraunces mb-8" style="font-size: 40px; color: #1F2421; flex-shrink: 0;">
      Commandes entrantes
    </h1>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" style="flex-shrink: 0;" />

    <p v-if="!store.loading && store.commandesEnAttente.length === 0" style="color: rgba(31,36,33,0.6);">
      Aucune commande en attente.
    </p>

    <div style="flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch;">
      <v-row>
        <v-col
          v-for="commande in store.commandesEnAttente"
          :key="commande.id"
          cols="12" md="6"
        >
          <v-card rounded="xl" elevation="0" border class="pa-2">
            <v-card-text>
              <div class="d-flex justify-space-between align-center mb-1">
                <span class="font-weight-bold" style="font-size: 18px; color: #1F2421;">
                  Commande n°{{ commande.id }}
                </span>
                <v-chip size="small" variant="tonal" color="primary">
                  {{ commande.lignes.length }} verre{{ commande.lignes.length > 1 ? 's' : '' }}
                </v-chip>
              </div>
              <p style="font-size: 12px; color: rgba(31,36,33,0.45); margin-bottom: 12px;">
                {{ formatDate(commande.creeLe) }}
              </p>

              <ul style="list-style: none; padding: 0; margin: 0 0 16px;">
                <li
                  v-for="ligne in commande.lignes"
                  :key="ligne.id"
                  style="color: rgba(31,36,33,0.8); font-size: 14px; margin-bottom: 6px;"
                >
                  <div class="d-flex align-center" style="gap: 6px;">
                    <span>• {{ ligne.cocktailNom }}</span>
                    <v-chip size="x-small" variant="tonal" color="secondary">{{ ligne.taille }}</v-chip>
                  </div>
                  <p v-if="ligne.note" style="font-size: 12px; color: rgba(31,36,33,0.55); font-style: italic; margin: 2px 0 0 12px;">
                    « {{ ligne.note }} »
                  </p>
                </li>
              </ul>

              <div class="d-flex" style="gap: 8px;">
                <v-btn
                  color="primary"
                  rounded="lg"
                  style="flex: 1;"
                  @click="prendreEnCharge(commande.id)"
                >
                  Prendre en charge
                </v-btn>
                <v-btn variant="outlined" color="error" rounded="lg" @click="ouvrirConfirmRefus(commande.id)">
                  Refuser
                </v-btn>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </div>
  </div>

  <!-- Dialog confirmation refus -->
  <v-dialog v-model="dialogRefus" max-width="400">
    <v-card rounded="xl">
      <v-card-title class="font-fraunces pt-5 px-5" style="font-size: 20px; color: #1F2421;">
        Refuser la commande ?
      </v-card-title>
      <v-card-text class="px-5 pb-4" style="color: rgba(31,36,33,0.7);">
        Cette commande sera annulée. Le client sera automatiquement remboursé du montant total.
      </v-card-text>
      <v-card-actions class="pa-5 pt-0">
        <v-btn variant="text" @click="dialogRefus = false">Annuler</v-btn>
        <v-spacer />
        <v-btn color="error" rounded="lg" :loading="annulation" @click="confirmerRefus">
          Confirmer le refus
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { useAuthStore } from '@/stores/useAuthStore'

const store = useCommandeStore()
const authStore = useAuthStore()
const router = useRouter()

const dialogRefus = ref(false)
const commandeARefuser = ref<number | null>(null)
const annulation = ref(false)

onMounted(() => store.fetchEnAttente())

async function prendreEnCharge(commandeId: number) {
  if (!authStore.utilisateur) return
  await store.prendreEnCharge(commandeId, authStore.utilisateur.id)
  router.push(`/commandes/${commandeId}`)
}

function ouvrirConfirmRefus(commandeId: number) {
  commandeARefuser.value = commandeId
  dialogRefus.value = true
}

async function confirmerRefus() {
  if (!commandeARefuser.value) return
  annulation.value = true
  await store.annulerCommande(commandeARefuser.value)
  annulation.value = false
  dialogRefus.value = false
  commandeARefuser.value = null
}

function formatDate(iso: string): string {
  return new Date(iso).toLocaleString('fr-FR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  })
}
</script>

<style scoped>
</style>
