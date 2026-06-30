<template>
  <div>
    <div class="d-flex align-center justify-space-between mb-8">
      <h1 class="font-fraunces" style="font-size: 40px; color: #1F2421;">
        Liste des ingrédients
      </h1>
      <div class="d-flex" style="gap: 8px;">
        <v-btn icon="mdi-magnify" variant="outlined" color="primary" rounded="circle" />
        <v-btn icon="mdi-tune" variant="outlined" color="primary" rounded="circle" />
      </div>
    </div>

    <v-progress-linear v-if="store.loading" indeterminate color="primary" class="mb-4" />

    <v-row>
      <v-col
        v-for="ingredient in store.ingredients"
        :key="ingredient.id"
        cols="12" sm="6" md="4" lg="3"
      >
        <v-card rounded="xl" elevation="0" border>
          <v-img
            :src="ingredient.imageUrl ?? ''"
            height="160"
            cover
            class="rounded-t-xl"
          >
            <template #error>
              <div class="d-flex align-center justify-center h-100 bg-grey-lighten-3">
                <v-icon icon="mdi-image-off" color="grey" />
              </div>
            </template>
          </v-img>
          <v-card-text class="d-flex align-center justify-space-between pa-4">
            <span class="font-weight-semibold" style="font-size: 15px; color: #1F2421;">
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
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useIngredientStore } from '@/stores/useIngredientStore'

const store = useIngredientStore()

onMounted(() => store.fetchAll())
</script>

<style scoped>
</style>
