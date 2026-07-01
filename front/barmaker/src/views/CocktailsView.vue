<template>
  <div ref="conteneur" style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">

    <div class="d-flex align-center justify-space-between" style="flex-shrink: 0; margin-bottom: 16px;">
      <h1 class="font-fraunces" style="font-size: 36px; color: #1F2421;">
        Cocktails disponibles
      </h1>
      <v-btn
        :icon="filtresOuverts ? 'mdi-close' : 'mdi-magnify'"
        variant="outlined"
        color="primary"
        rounded="circle"
        @click="toggleFiltres"
      />
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
            <v-card-text class="pa-3">
              <div class="d-flex align-center justify-space-between">
                <span class="font-weight-semibold" style="font-size: 14px; color: #1F2421;">
                  {{ cocktail.nom }}
                </span>
                <v-switch
                  :model-value="cocktail.actif"
                  color="primary"
                  hide-details
                  density="compact"
                  @change="store.toggleActif(cocktail.id)"
                />
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
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { useIngredientStore } from '@/stores/useIngredientStore'
import AppPagination from '@/components/AppPagination.vue'
import { useSwipe } from '@/composables/useSwipe'

const PAR_PAGE = 12

const store = useCocktailStore()
const ingredientStore = useIngredientStore()
const page = ref(1)
const conteneur = ref<HTMLElement | null>(null)

useSwipe(
  conteneur,
  () => { if (page.value < totalPages.value) page.value++ },
  () => { if (page.value > 1) page.value-- }
)
const recherche = ref('')
const ingredientSelectionne = ref<string | null>(null)
const filtresOuverts = ref(false)

function toggleFiltres() {
  filtresOuverts.value = !filtresOuverts.value
  if (!filtresOuverts.value) {
    recherche.value = ''
    ingredientSelectionne.value = null
  }
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
    liste = liste.filter(c => c.ingredients?.includes(ingredientSelectionne.value!))
  }
  return liste
})

watch([recherche, ingredientSelectionne], () => { page.value = 1 })

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
