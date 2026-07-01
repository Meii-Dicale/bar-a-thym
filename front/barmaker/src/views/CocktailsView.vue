<template>
  <div ref="conteneur" style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">

    <div class="d-flex align-center justify-space-between" style="flex-shrink: 0; margin-bottom: 16px;">
      <h1 class="font-fraunces" style="font-size: 36px; color: #1F2421;">
        {{ vue === 'disponibles' ? 'Cocktails disponibles' : 'Cocktails en ligne' }}
      </h1>
      <div class="d-flex align-center" style="gap: 8px;">
        <v-btn-toggle v-model="filtreAlcool" mandatory density="compact" rounded="lg" color="secondary">
          <v-btn value="tout" size="small">Tout</v-btn>
          <v-btn value="avec" size="small">Avec alcool</v-btn>
          <v-btn value="sans" size="small">Sans alcool</v-btn>
        </v-btn-toggle>
        <v-btn-toggle v-model="vue" mandatory density="compact" rounded="lg" color="primary">
          <v-btn value="disponibles" size="small">Disponibles</v-btn>
          <v-btn value="en-ligne" size="small">En ligne</v-btn>
        </v-btn-toggle>
        <v-btn
          :icon="filtresOuverts ? 'mdi-close' : 'mdi-magnify'"
          variant="outlined"
          color="primary"
          rounded="circle"
          @click="toggleFiltres"
        />
      </div>
    </div>

    <v-expand-transition>
      <div v-if="filtresOuverts" style="flex-shrink: 0; margin-bottom: 16px; display: flex; gap: 12px;">
        <v-text-field
          v-model="recherche"
          placeholder="Rechercher un cocktail…"
          variant="outlined"
          density="compact"
          color="primary"
          rounded="lg"
          hide-details
          clearable
          autofocus
          prepend-inner-icon="mdi-magnify"
          style="flex: 1;"
        />
        <v-autocomplete
          v-model="ingredientSelectionne"
          :items="ingredientsActifs"
          placeholder="Filtrer par ingrédient"
          variant="outlined"
          density="compact"
          color="primary"
          rounded="lg"
          hide-details
          clearable
          prepend-inner-icon="mdi-leaf-circle-outline"
          style="flex: 1;"
        />
      </div>
    </v-expand-transition>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" style="flex-shrink: 0; margin-bottom: 12px;" />

    <div v-if="cocktailsFiltres.length === 0 && !store.loading" style="flex: 1; display: flex; align-items: center; justify-content: center;">
      <div class="text-center" style="color: #9E9E9E;">
        <v-icon icon="mdi-cup-off" size="48" style="margin-bottom: 12px; display: block;" />
        <p>Aucun cocktail trouvé</p>
      </div>
    </div>

    <div v-else style="flex: 1; min-height: 0; overflow: hidden;">
      <v-row style="margin: 0; height: 100%;">
        <v-col
          v-for="cocktail in cocktailsPage"
          :key="cocktail.id"
          cols="12" sm="6" md="4" lg="3"
          style="padding: 8px;"
        >
          <v-card rounded="xl" elevation="0" border>
            <div style="position: relative;">
              <v-img
                :src="cocktail.imageUrl ?? ''"
                height="120"
                cover
                class="rounded-t-xl"
              >
                <template #error>
                  <div class="d-flex align-center justify-center h-100 bg-grey-lighten-3">
                    <v-icon icon="mdi-cup" color="grey" />
                  </div>
                </template>
              </v-img>
              <div
                v-if="cocktail.actif && !cocktail.aPrix"
                style="position: absolute; top: 8px; right: 8px; background: #B00020; border-radius: 50%; width: 26px; height: 26px; display: flex; align-items: center; justify-content: center;"
              >
                <v-icon icon="mdi-currency-eur" color="white" size="15" />
              </div>
            </div>
            <v-card-text class="pa-3">
              <div class="d-flex align-center justify-space-between">
                <span class="font-weight-semibold" style="font-size: 14px; color: #1F2421; flex: 1; min-width: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                  {{ cocktail.nom }}
                </span>
                <div class="d-flex align-center" style="gap: 4px; flex-shrink: 0;">
                  <v-btn
                    icon="mdi-currency-eur"
                    size="x-small"
                    variant="text"
                    color="secondary"
                    @click.stop="ouvrirDialogPrix(cocktail)"
                  />
                  <v-switch
                    :model-value="cocktail.actif"
                    color="primary"
                    hide-details
                    density="compact"
                    @change="store.toggleActif(cocktail.id)"
                  />
                </div>
              </div>
              <v-btn
                variant="text"
                color="primary"
                size="small"
                class="mt-1 px-0"
                :to="`/cocktails/${cocktail.id}/recette`"
              >
                Voir la recette
              </v-btn>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </div>

    <div style="flex-shrink: 0;">
      <AppPagination v-model:page="page" :total-pages="totalPages" />
    </div>

  </div>

  <!-- Dialog prix -->
  <v-dialog v-model="dialogPrix" max-width="360">
    <v-card v-if="cocktailPrix" rounded="xl">
      <v-card-title class="font-fraunces pt-5 px-5" style="font-size: 20px; color: #1F2421;">
        Fixer les prix
      </v-card-title>
      <v-card-subtitle class="px-5 pb-0" style="font-size: 13px; color: rgba(31,36,33,0.55);">
        {{ cocktailPrix.nom }}
      </v-card-subtitle>
      <v-card-text class="px-5 pt-4">
        <div style="display: flex; flex-direction: column; gap: 12px;">
          <v-text-field
            v-model.number="prixS"
            label="Prix S"
            type="number"
            min="0"
            step="0.50"
            variant="outlined"
            density="compact"
            color="primary"
            rounded="lg"
            hide-details
            suffix="€"
          />
          <v-text-field
            v-model.number="prixM"
            label="Prix M"
            type="number"
            min="0"
            step="0.50"
            variant="outlined"
            density="compact"
            color="primary"
            rounded="lg"
            hide-details
            suffix="€"
          />
          <v-text-field
            v-model.number="prixL"
            label="Prix L"
            type="number"
            min="0"
            step="0.50"
            variant="outlined"
            density="compact"
            color="primary"
            rounded="lg"
            hide-details
            suffix="€"
          />
        </div>
      </v-card-text>
      <v-card-actions class="pa-5 pt-0">
        <v-btn variant="text" @click="dialogPrix = false">Annuler</v-btn>
        <v-spacer />
        <v-btn
          color="primary"
          rounded="lg"
          :disabled="!prixS || !prixM || !prixL || enregistrement"
          :loading="enregistrement"
          @click="sauvegarderPrix"
        >
          Enregistrer
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { useIngredientStore } from '@/stores/useIngredientStore'
import AppPagination from '@/components/AppPagination.vue'
import { useSwipe } from '@/composables/useSwipe'
import { cocktailService } from '@/services/cocktailService'
import type { Cocktail } from '@/types'

const PAR_PAGE = 12

const store = useCocktailStore()
const ingredientStore = useIngredientStore()
const page = ref(1)
const recherche = ref('')
const ingredientSelectionne = ref<string | null>(null)
const filtresOuverts = ref(false)
const conteneur = ref<HTMLElement | null>(null)
const vue = ref<'disponibles' | 'en-ligne'>('disponibles')
const filtreAlcool = ref<'tout' | 'avec' | 'sans'>('tout')

const dialogPrix = ref(false)
const cocktailPrix = ref<Cocktail | null>(null)
const prixS = ref<number | null>(null)
const prixM = ref<number | null>(null)
const prixL = ref<number | null>(null)
const enregistrement = ref(false)

useSwipe(
  conteneur,
  () => { if (page.value < totalPages.value) page.value++ },
  () => { if (page.value > 1) page.value-- }
)

function toggleFiltres() {
  filtresOuverts.value = !filtresOuverts.value
  if (!filtresOuverts.value) {
    recherche.value = ''
    ingredientSelectionne.value = null
  }
}

async function ouvrirDialogPrix(cocktail: Cocktail) {
  cocktailPrix.value = cocktail
  prixS.value = null
  prixM.value = null
  prixL.value = null
  const existants = await cocktailService.getTaillesPrix(cocktail.id)
  existants.forEach(tp => {
    if (tp.taille === 'S') prixS.value = Number(tp.prix)
    if (tp.taille === 'M') prixM.value = Number(tp.prix)
    if (tp.taille === 'L') prixL.value = Number(tp.prix)
  })
  dialogPrix.value = true
}

async function sauvegarderPrix() {
  if (!cocktailPrix.value || !prixS.value || !prixM.value || !prixL.value) return
  enregistrement.value = true
  await cocktailService.definirPrix(cocktailPrix.value.id, prixS.value, prixM.value, prixL.value)
  enregistrement.value = false
  dialogPrix.value = false
}

const ingredientsActifs = computed(() =>
  ingredientStore.ingredients
    .filter(i => i.disponible)
    .map(i => i.nom)
    .sort((a, b) => a.localeCompare(b))
)

const cocktailsFiltres = computed(() => {
  let liste = store.cocktails
  const terme = (recherche.value ?? '').trim().toLowerCase()
  if (terme) {
    liste = liste.filter(c => c.nom.toLowerCase().includes(terme))
  }
  if (ingredientSelectionne.value) {
    liste = liste.filter(c => c.ingredients?.some(i => i.nom === ingredientSelectionne.value))
  }
  if (filtreAlcool.value === 'avec') {
    liste = liste.filter(c => c.alcoolise === true)
  } else if (filtreAlcool.value === 'sans') {
    liste = liste.filter(c => c.alcoolise === false)
  }
  return liste
})

watch(vue, (v) => {
  page.value = 1
  if (v === 'en-ligne') store.fetchActifs()
  else store.fetchDisponibles()
})

watch([recherche, ingredientSelectionne, filtreAlcool], () => { page.value = 1 })

const totalPages = computed(() => Math.ceil(cocktailsFiltres.value.length / PAR_PAGE))

const cocktailsPage = computed(() => {
  const debut = (page.value - 1) * PAR_PAGE
  return cocktailsFiltres.value.slice(debut, debut + PAR_PAGE)
})

onMounted(() => {
  store.fetchDisponibles()
  ingredientStore.fetchAll()
})
</script>

<style scoped>
</style>
