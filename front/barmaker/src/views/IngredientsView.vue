<template>
  <div style="display: flex; flex-direction: column; height: 100%; overflow: hidden;">

    <div class="d-flex align-center justify-space-between" style="flex-shrink: 0; margin-bottom: 24px;">
      <h1 class="font-fraunces" style="font-size: 36px; color: #1F2421;">
        Liste des ingrédients
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
import { ref, computed, onMounted } from 'vue'
import { useIngredientStore } from '@/stores/useIngredientStore'
import AppPagination from '@/components/AppPagination.vue'

const PAR_PAGE = 12

const store = useIngredientStore()
const page = ref(1)

const totalPages = computed(() => Math.ceil(store.ingredients.length / PAR_PAGE))

const ingredientsPage = computed(() => {
  const debut = (page.value - 1) * PAR_PAGE
  return store.ingredients.slice(debut, debut + PAR_PAGE)
})

onMounted(() => store.fetchAll())
</script>

<style scoped>
</style>
