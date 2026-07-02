<template>
  <div class="pa-4">
    <h2 class="font-fraunces mb-1" style="font-size: 28px; color: #1F2421;">
      Notre carte
    </h2>
    <p class="mb-4" style="color: rgba(31,36,33,0.55); font-size: 14px;">
      {{ cocktailsFiltres.length }} cocktails disponibles
    </p>

    <v-btn-toggle v-model="filtreAlcool" mandatory density="compact" rounded="lg" color="secondary" class="mb-6">
      <v-btn value="tout" size="small">Tout</v-btn>
      <v-btn value="avec" size="small">Avec alcool</v-btn>
      <v-btn value="sans" size="small">Sans alcool</v-btn>
    </v-btn-toggle>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <div style="display: flex; flex-direction: column; gap: 12px;">
      <v-card
        v-for="cocktail in cocktailsFiltres"
        :key="cocktail.id"
        rounded="xl"
        elevation="0"
        border
        @click="ouvrirDetail(cocktail)"
      >
        <div class="d-flex">
          <v-img
            :src="cocktail.imageUrl ?? ''"
            width="100"
            height="100"
            cover
            class="rounded-s-xl flex-shrink-0"
          >
            <template #error>
              <div class="d-flex align-center justify-center h-100 bg-grey-lighten-3">
                <v-icon icon="mdi-cup" color="grey" />
              </div>
            </template>
          </v-img>
          <div class="pa-4 d-flex flex-column justify-center" style="flex: 1;">
            <span class="font-weight-semibold" style="font-size: 15px; color: #1F2421;">
              {{ cocktail.nom }}
            </span>
            <span v-if="cocktail.categorie" style="font-size: 12px; color: rgba(31,36,33,0.5); margin-top: 2px;">
              {{ cocktail.categorie }}
            </span>
          </div>
          <div class="d-flex align-center pr-4">
            <v-icon icon="mdi-chevron-right" color="primary" />
          </div>
        </div>
      </v-card>
    </div>

    <v-dialog v-model="dialogOuvert" max-width="400">
      <v-card v-if="cocktailSelectionne" rounded="xl">
        <v-img
          :src="cocktailSelectionne.imageUrl ?? ''"
          height="200"
          cover
          class="rounded-t-xl"
        />
        <v-card-title class="font-fraunces pt-4" style="font-size: 22px; color: #1F2421;">
          {{ cocktailSelectionne.nom }}
        </v-card-title>
        <v-card-text>
          <p class="mb-4" style="font-size: 14px; color: rgba(31,36,33,0.7);">
            Choisissez votre taille :
          </p>
          <div class="d-flex" style="gap: 8px;">
            <v-btn
              v-for="tp in taillesPrix"
              :key="tp.id"
              :variant="taillePrixSelectionnee?.id === tp.id ? 'flat' : 'outlined'"
              color="primary"
              rounded="lg"
              style="flex: 1;"
              @click="taillePrixSelectionnee = tp"
            >
              <div class="d-flex flex-column">
                <span style="font-size: 16px; font-weight: 700;">{{ tp.taille }}</span>
                <span style="font-size: 11px;">{{ tp.prix }} €</span>
              </div>
            </v-btn>
          </div>
          <v-textarea
            v-model="note"
            label="Note (facultatif)"
            variant="outlined"
            rounded="lg"
            color="primary"
            rows="2"
            class="mt-4"
            hide-details
          />
        </v-card-text>
        <v-card-actions class="pa-4 pt-0">
          <v-btn variant="text" @click="dialogOuvert = false">Annuler</v-btn>
          <v-spacer />
          <v-btn
            color="primary"
            rounded="lg"
            :disabled="!taillePrixSelectionnee"
            @click="ajouterAuPanier"
          >
            Ajouter au panier
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { usePanierStore } from '@/stores/usePanierStore'
import { cocktailService } from '@/services/cocktailService'
import type { Cocktail, TaillePrix } from '@/types'

const store = useCocktailStore()
const panierStore = usePanierStore()

const dialogOuvert = ref(false)
const cocktailSelectionne = ref<Cocktail | null>(null)
const taillesPrix = ref<TaillePrix[]>([])
const taillePrixSelectionnee = ref<TaillePrix | null>(null)
const note = ref('')
const filtreAlcool = ref<'tout' | 'avec' | 'sans'>('tout')

const cocktailsFiltres = computed(() => {
  if (filtreAlcool.value === 'avec') return store.cocktails.filter(c => c.alcoolise === true)
  if (filtreAlcool.value === 'sans') return store.cocktails.filter(c => c.alcoolise === false)
  return store.cocktails
})

onMounted(() => store.fetchActifs())

async function ouvrirDetail(cocktail: Cocktail) {
  cocktailSelectionne.value = cocktail
  taillePrixSelectionnee.value = null
  note.value = ''
  taillesPrix.value = await cocktailService.getTaillesPrix(cocktail.id)
  dialogOuvert.value = true
}

function ajouterAuPanier() {
  if (!cocktailSelectionne.value || !taillePrixSelectionnee.value) return
  panierStore.ajouterArticle({
    cocktailId:     cocktailSelectionne.value.id,
    cocktailNom:    cocktailSelectionne.value.nom,
    cocktailImageUrl: cocktailSelectionne.value.imageUrl,
    taillePrixId:   taillePrixSelectionnee.value.id,
    taille:         taillePrixSelectionnee.value.taille,
    prix:           taillePrixSelectionnee.value.prix,
    note:           note.value
  })
  dialogOuvert.value = false
}
</script>

<style scoped>
</style>
