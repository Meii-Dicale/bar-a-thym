<template>
  <div>
    <v-btn
      variant="text"
      color="primary"
      prepend-icon="mdi-arrow-left"
      class="mb-6 px-0"
      to="/cocktails"
    >
      Retour aux cocktails
    </v-btn>

    <v-progress-linear v-if="chargement" indeterminate color="primary" class="mb-4" />

    <template v-if="cocktail">
      <div class="d-flex align-center" style="gap: 16px; margin-bottom: 8px;">
        <h1 class="font-fraunces" style="font-size: 40px; color: #1F2421;">
          Recette : {{ cocktail.nom }}
        </h1>
        <v-avatar color="primary" size="36" rounded="lg">
          <v-icon icon="mdi-leaf" color="white" size="18" />
        </v-avatar>
      </div>

      <v-row class="mt-6">
        <v-col cols="12" md="5">
          <h2 class="font-fraunces mb-4" style="font-size: 22px; color: #1F2421;">
            Ingrédients
          </h2>
          <v-table density="compact">
            <tbody>
              <tr v-for="ing in cocktail.ingredients" :key="ing.nom">
                <td style="color: #1F2421; font-weight: 500;">{{ ing.nom }}</td>
                <td style="color: #C98E5F; font-weight: 600; text-align: right;">
                  {{ ing.quantite ?? '' }}
                </td>
              </tr>
            </tbody>
          </v-table>

          <h2 class="font-fraunces mt-8 mb-3" style="font-size: 18px; color: #1F2421;">
            Tarifs
          </h2>
          <v-table density="compact">
            <tbody>
              <tr v-for="tp in taillesPrix" :key="tp.id">
                <td style="color: #1F2421;">Taille {{ tp.taille }}</td>
                <td style="color: #C98E5F; font-weight: 600; text-align: right;">
                  {{ tp.prix }} €
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-col>

        <v-col cols="12" md="7">
          <h2 class="font-fraunces mb-4" style="font-size: 22px; color: #1F2421;">
            Préparation
          </h2>
          <p v-if="cocktail.instructions" style="color: rgba(31,36,33,0.8); line-height: 1.6;">
            {{ cocktail.instructions }}
          </p>
          <p v-else style="color: rgba(31,36,33,0.4); font-style: italic;">
            Aucune instruction disponible.
          </p>
        </v-col>
      </v-row>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { cocktailService } from '@/services/cocktailService'
import type { Cocktail, TaillePrix } from '@/types'

const route = useRoute()
const cocktail = ref<Cocktail | null>(null)
const taillesPrix = ref<TaillePrix[]>([])
const chargement = ref(false)

onMounted(async () => {
  chargement.value = true
  const id = Number(route.params.id)
  cocktail.value = await cocktailService.findById(id)
  taillesPrix.value = await cocktailService.getTaillesPrix(id)
  chargement.value = false
})
</script>

<style scoped>
</style>
