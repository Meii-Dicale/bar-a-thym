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
            <div class="d-flex justify-space-between align-center mb-1">
              <div>
                <span class="font-weight-semibold" style="color: #1F2421;">
                  {{ ligne.cocktailNom }}
                </span>
                <v-chip size="x-small" variant="tonal" color="secondary" class="ml-2">
                  {{ ligne.taille }}
                </v-chip>
              </div>
              <v-chip size="x-small" :color="couleurEtape(ligne.statut)" variant="tonal">
                {{ STATUT_LIGNE_LABELS[ligne.statut] }}
              </v-chip>
            </div>
            <p v-if="ligne.note" style="font-size: 12px; color: rgba(31,36,33,0.55); margin-bottom: 8px; font-style: italic;">
              « {{ ligne.note }} »
            </p>

            <v-progress-linear
              :model-value="progressionLigne(ligne.statut)"
              color="primary"
              height="3"
              rounded
              class="mt-2"
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

      <v-btn
        v-if="store.commandeSelectionnee.statut === 'TERMINEE'"
        color="primary"
        rounded="lg"
        block
        class="mt-4"
        prepend-icon="mdi-download"
        @click="telechargerRecu"
      >
        Télécharger mon reçu
      </v-btn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { jsPDF } from 'jspdf'
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
    TERMINEE:  'success',
    ANNULEE:   'error'
  }
  return map[statut]
}

onMounted(() => {
  store.fetchDetail(Number(route.params.id))
})

function telechargerRecu() {
  const commande = store.commandeSelectionnee
  if (!commande) return

  const largeur = 80
  const marge = 6
  const hauteur = 60 + commande.lignes.length * 16 + 30
  const doc = new jsPDF({ unit: 'mm', format: [largeur, hauteur] })
  const centre = largeur / 2
  let y = 10

  doc.setFont('courier', 'bold')
  doc.setFontSize(13)
  doc.text('Bar à Thym', centre, y, { align: 'center' })
  y += 6

  doc.setFont('courier', 'normal')
  doc.setFontSize(9)
  doc.text(`Commande n°${commande.id}`, centre, y, { align: 'center' })
  y += 5
  doc.text(new Date(commande.creeLe).toLocaleString('fr-FR'), centre, y, { align: 'center' })
  y += 7

  doc.text('-'.repeat(34), centre, y, { align: 'center' })
  y += 6

  for (const ligne of commande.lignes) {
    doc.text(`${ligne.cocktailNom} (${ligne.taille})`, marge, y)
    y += 5
    if (ligne.note) {
      doc.setFontSize(7)
      doc.text(`« ${ligne.note} »`, marge + 2, y)
      doc.setFontSize(9)
      y += 4
    }
    doc.text(`${ligne.prix.toFixed(2)} €`, largeur - marge, y - 5, { align: 'right' })
    y += 4
  }

  y += 2
  doc.text('-'.repeat(34), centre, y, { align: 'center' })
  y += 7

  doc.setFont('courier', 'bold')
  doc.setFontSize(11)
  doc.text('TOTAL', marge, y)
  doc.text(`${commande.total.toFixed(2)} €`, largeur - marge, y, { align: 'right' })
  y += 8

  doc.setFont('courier', 'normal')
  doc.setFontSize(8)
  doc.text('Merci de votre visite !', centre, y, { align: 'center' })

  doc.save(`recu-commande-${commande.id}.pdf`)
}
</script>

<style scoped>
</style>
