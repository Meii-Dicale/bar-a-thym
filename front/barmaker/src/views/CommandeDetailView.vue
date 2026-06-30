<template>
  <div>
    <v-btn
      variant="text"
      color="primary"
      prepend-icon="mdi-arrow-left"
      class="mb-6 px-0"
      to="/commandes"
    >
      Retour aux commandes
    </v-btn>

    <h1 class="font-fraunces mb-8" style="font-size: 40px; color: #1F2421;">
      Détail commande n°{{ route.params.id }}
    </h1>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <div v-if="store.commandeSelectionnee" style="display: flex; flex-direction: column; gap: 16px;">
      <v-card
        v-for="ligne in store.commandeSelectionnee.lignes"
        :key="ligne.id"
        rounded="xl"
        elevation="0"
        border
      >
        <v-card-text class="pa-6">
          <div class="d-flex align-center justify-space-between mb-4">
            <div>
              <h3 class="font-weight-bold" style="font-size: 18px; color: #1F2421;">
                {{ ligne.cocktailNom }}
              </h3>
              <div class="d-flex" style="gap: 8px; margin-top: 6px; flex-wrap: wrap;">
                <v-chip size="x-small" variant="tonal" color="primary">
                  {{ ligne.taillePrixId }}
                </v-chip>
              </div>
            </div>
            <v-btn
              v-if="ligne.statut !== 'TERMINEE'"
              color="primary"
              rounded="lg"
              size="small"
              @click="avancer(ligne.id)"
            >
              Étape suivante
            </v-btn>
            <v-chip v-else color="success" variant="tonal" size="small">
              Terminé ✓
            </v-chip>
          </div>

          <div class="d-flex align-center" style="gap: 0;">
            <template v-for="(etape, index) in etapes" :key="etape.key">
              <div class="d-flex flex-column align-center" style="flex: 1;">
                <div
                  :style="{
                    width: '12px', height: '12px', borderRadius: '50%',
                    background: estAtteinte(ligne.statut, etape.key) ? '#7A9078' : '#E0E0E0'
                  }"
                />
                <span style="font-size: 11px; color: rgba(31,36,33,0.5); margin-top: 4px; text-align: center;">
                  {{ etape.label }}
                </span>
              </div>
              <div
                v-if="index < etapes.length - 1"
                :style="{
                  flex: 2, height: '2px', background: estAtteinte(ligne.statut, etapes[index + 1].key) ? '#7A9078' : '#E0E0E0',
                  marginBottom: '14px'
                }"
              />
            </template>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCommandeStore } from '@/stores/useCommandeStore'
import type { LigneCommande } from '@/types'

const route = useRoute()
const store = useCommandeStore()

const etapes: { key: LigneCommande['statut']; label: string }[] = [
  { key: 'PREPARATION', label: 'Ingrédients préparés' },
  { key: 'ASSEMBLAGE',  label: 'Assemblage'           },
  { key: 'DRESSAGE',    label: 'Dressage final'       },
  { key: 'TERMINEE',    label: 'Terminée'             }
]

const ordre = ['PREPARATION', 'ASSEMBLAGE', 'DRESSAGE', 'TERMINEE']

function estAtteinte(statut: LigneCommande['statut'], etape: LigneCommande['statut']): boolean {
  return ordre.indexOf(statut) >= ordre.indexOf(etape)
}

async function avancer(ligneId: number) {
  await store.avancerLigne(ligneId)
}

onMounted(() => {
  store.fetchDetail(Number(route.params.id))
})
</script>

<style scoped>
</style>
