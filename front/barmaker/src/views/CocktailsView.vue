<template>
  <div style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">

    <div class="d-flex align-center justify-space-between" style="flex-shrink: 0; margin-bottom: 24px;">
      <h1 class="font-fraunces" style="font-size: 36px; color: #1F2421;">
        Cocktails disponibles
      </h1>
      <div class="d-flex" style="gap: 8px;">
        <v-btn icon="mdi-magnify" variant="outlined" color="primary" rounded="circle" />
        <v-btn icon="mdi-tune" variant="outlined" color="primary" rounded="circle" />
      </div>
    </div>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" style="flex-shrink: 0; margin-bottom: 12px;" />

    <div style="flex: 1; min-height: 0; overflow: hidden;">
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
import { ref, computed, onMounted } from 'vue'
import { useCocktailStore } from '@/stores/useCocktailStore'
import AppPagination from '@/components/AppPagination.vue'

const PAR_PAGE = 8

const store = useCocktailStore()
const page = ref(1)

const totalPages = computed(() => Math.ceil(store.cocktails.length / PAR_PAGE))

const cocktailsPage = computed(() => {
  const debut = (page.value - 1) * PAR_PAGE
  return store.cocktails.slice(debut, debut + PAR_PAGE)
})

onMounted(() => store.fetchDisponibles())
</script>

<style scoped>
</style>
