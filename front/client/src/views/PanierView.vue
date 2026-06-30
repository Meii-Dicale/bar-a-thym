<template>
  <div class="pa-4">
    <h2 class="font-fraunces mb-6" style="font-size: 28px; color: #1F2421;">
      Mon panier
    </h2>

    <div v-if="panierStore.articles.length === 0" class="text-center py-12">
      <v-icon icon="mdi-cart-outline" size="64" color="grey-lighten-1" class="mb-4" />
      <p style="color: rgba(31,36,33,0.5);">Votre panier est vide</p>
      <v-btn color="primary" variant="text" to="/" class="mt-2">
        Voir la carte
      </v-btn>
    </div>

    <template v-else>
      <div style="display: flex; flex-direction: column; gap: 10px; margin-bottom: 24px;">
        <v-card
          v-for="(article, index) in panierStore.articles"
          :key="index"
          rounded="xl"
          elevation="0"
          border
        >
          <v-card-text class="d-flex align-center pa-4">
            <div style="flex: 1;">
              <div class="font-weight-semibold" style="color: #1F2421;">
                {{ article.cocktailNom }}
              </div>
              <div style="font-size: 13px; color: rgba(31,36,33,0.6); margin-top: 2px;">
                Taille {{ article.taille }}
                <span v-if="article.note"> · {{ article.note }}</span>
              </div>
            </div>
            <span class="font-weight-bold mr-3" style="color: #C98E5F;">
              {{ article.prix }} €
            </span>
            <v-btn
              icon="mdi-close"
              variant="text"
              size="small"
              color="error"
              @click="panierStore.supprimerArticle(index)"
            />
          </v-card-text>
        </v-card>
      </div>

      <v-card rounded="xl" elevation="0" color="primary" class="mb-6">
        <v-card-text class="d-flex justify-space-between align-center pa-5">
          <span style="color: white; font-size: 16px;">Total</span>
          <span class="font-fraunces" style="color: white; font-size: 28px; font-weight: 700;">
            {{ panierStore.total.toFixed(2) }} €
          </span>
        </v-card-text>
      </v-card>

      <v-alert v-if="erreur" type="error" variant="tonal" rounded="lg" class="mb-4">
        {{ erreur }}
      </v-alert>

      <v-btn
        block
        color="secondary"
        size="x-large"
        rounded="xl"
        :loading="chargement"
        @click="commander"
      >
        Commander
      </v-btn>
    </template>

    <v-snackbar v-model="snackbar" color="primary" timeout="3000" rounded="lg">
      Commande passée avec succès !
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePanierStore } from '@/stores/usePanierStore'
import { useCommandeStore } from '@/stores/useCommandeStore'
import { useAuthStore } from '@/stores/useAuthStore'

const panierStore = usePanierStore()
const commandeStore = useCommandeStore()
const authStore = useAuthStore()
const router = useRouter()

const chargement = ref(false)
const erreur = ref('')
const snackbar = ref(false)

async function commander() {
  if (!authStore.utilisateur) return
  erreur.value = ''
  chargement.value = true
  try {
    await commandeStore.passerCommande(authStore.utilisateur.id)
    snackbar.value = true
    setTimeout(() => router.push('/commandes'), 1500)
  } catch {
    erreur.value = 'Une erreur est survenue, veuillez réessayer.'
  } finally {
    chargement.value = false
  }
}
</script>

<style scoped>
</style>
