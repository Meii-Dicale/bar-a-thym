<template>
  <div>
    <div class="d-flex align-center justify-space-between mb-8">
      <h1 class="font-fraunces" style="font-size: 40px; color: #1F2421;">
        Liste des cocktails
      </h1>
      <div class="d-flex" style="gap: 8px;">
        <v-btn icon="mdi-magnify" variant="outlined" color="primary" rounded="circle" />
        <v-btn icon="mdi-tune" variant="outlined" color="primary" rounded="circle" />
      </div>
    </div>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <v-row>
      <v-col
        v-for="cocktail in store.cocktails"
        :key="cocktail.id"
        cols="12" sm="6" md="4" lg="3"
      >
        <v-card rounded="xl" elevation="0" border>
          <v-img
            :src="cocktail.imageUrl ?? ''"
            height="160"
            cover
            class="rounded-t-xl"
          >
            <template #error>
              <div class="d-flex align-center justify-center h-100 bg-grey-lighten-3">
                <v-icon icon="mdi-cup" color="grey" />
              </div>
            </template>
          </v-img>
          <v-card-text class="pa-4">
            <div class="d-flex align-center justify-space-between">
              <span class="font-weight-semibold" style="font-size: 15px; color: #1F2421;">
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
            <div v-if="!cocktail.actif" class="d-flex align-center mt-1" style="gap: 4px;">
              <v-icon icon="mdi-alert-outline" color="error" size="14" />
              <span style="font-size: 12px; color: #B00020;">Manquant</span>
            </div>
            <v-btn
              variant="text"
              color="primary"
              size="small"
              class="mt-2 px-0"
              :to="`/cocktails/${cocktail.id}/recette`"
            >
              Voir la recette
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCocktailStore } from '@/stores/useCocktailStore'

const store = useCocktailStore()

onMounted(() => store.fetchAll())
</script>

<style scoped>
</style>
