<template>
  <div ref="conteneur" style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">

    <div class="d-flex align-center justify-space-between" style="flex-shrink: 0; margin-bottom: 16px;">
      <h1 class="font-fraunces" style="font-size: 36px; color: #1F2421;">
        Liste des ingrédients
      </h1>
      <v-btn
        :icon="rechercheOuverte ? 'mdi-close' : 'mdi-magnify'"
        variant="outlined"
        color="primary"
        rounded="circle"
        @click="toggleRecherche"
      />
    </div>

    <v-expand-transition>
      <div v-if="rechercheOuverte" style="flex-shrink: 0; margin-bottom: 16px;">
        <v-text-field
          v-model="recherche"
          placeholder="Rechercher un ingrédient…"
          variant="outlined"
          density="compact"
          color="primary"
          rounded="lg"
          hide-details
          clearable
          autofocus
          prepend-inner-icon="mdi-magnify"
        />
      </div>
    </v-expand-transition>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" style="flex-shrink: 0; margin-bottom: 12px;" />

    <div v-if="ingredientsFiltres.length === 0 && !store.loading" style="flex: 1; display: flex; align-items: center; justify-content: center;">
      <div class="text-center" style="color: #9E9E9E;">
        <v-icon icon="mdi-leaf-off" size="48" style="margin-bottom: 12px; display: block;" />
        <p>Aucun ingrédient trouvé pour « {{ recherche }} »</p>
      </div>
    </div>

    <div v-else style="flex: 1; min-height: 0; overflow: hidden;">
      <v-row style="margin: 0; height: 100%;">
        <v-col
          v-for="ingredient in ingredientsPage"
          :key="ingredient.id"
          cols="12" sm="6" md="4" lg="3"
          style="padding: 8px;"
        >
          <v-card rounded="xl" elevation="0" border>
            <v-img
              :src="ingredient.imageUrl ?? ''"
              height="120"
              cover
              class="rounded-t-xl"
            >
              <template #error>
                <div class="d-flex align-center justify-center h-100 bg-grey-lighten-3">
                  <v-icon icon="mdi-image-off" color="grey" />
                </div>
              </template>
            </v-img>
            <v-card-text class="d-flex align-center justify-space-between pa-3">
              <span class="font-weight-semibold" style="font-size: 14px; color: #1F2421;">
                {{ ingredient.nom }}
              </span>
              <v-switch
                :model-value="ingredient.disponible"
                color="primary"
                hide-details
                density="compact"
                @change="store.toggleDisponible(ingredient.id)"
              />
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
import { useIngredientStore } from '@/stores/useIngredientStore'
import AppPagination from '@/components/AppPagination.vue'
import { useSwipe } from '@/composables/useSwipe'

const PAR_PAGE = 12

const store = useIngredientStore()
const page = ref(1)
const conteneur = ref<HTMLElement | null>(null)

useSwipe(
  conteneur,
  () => { if (page.value < totalPages.value) page.value++ },
  () => { if (page.value > 1) page.value-- }
)
const recherche = ref('')
const rechercheOuverte = ref(false)

function toggleRecherche() {
  rechercheOuverte.value = !rechercheOuverte.value
  if (!rechercheOuverte.value) {
    recherche.value = ''
  }
}

const ingredientsFiltres = computed(() => {
  const terme = (recherche.value ?? '').trim().toLowerCase()
  if (!terme) return store.ingredients
  return store.ingredients.filter(i => i.nom.toLowerCase().includes(terme))
})

watch(recherche, () => { page.value = 1 })

const totalPages = computed(() => Math.ceil(ingredientsFiltres.value.length / PAR_PAGE))

const ingredientsPage = computed(() => {
  const debut = (page.value - 1) * PAR_PAGE
  return ingredientsFiltres.value.slice(debut, debut + PAR_PAGE)
})

onMounted(() => store.fetchAll())
</script>

<style scoped>
</style>
