<template>
  <v-layout>
    <v-app-bar flat color="#F9F5EE" border="b">
      <v-app-bar-title>
        <span class="font-fraunces font-italic font-weight-bold" style="font-size: 20px; color: #1F2421;">
          Bar à Thym
        </span>
      </v-app-bar-title>
      <template #append>
        <v-btn icon="mdi-logout" variant="text" color="primary" size="small" @click="deconnexion" />
      </template>
    </v-app-bar>

    <v-main style="background: #F9F5EE; padding-bottom: 80px;">
      <router-view />
    </v-main>

    <v-bottom-navigation
      :model-value="routeActive"
      color="primary"
      bg-color="white"
      elevation="8"
      grow
    >
      <v-btn value="/" to="/" icon="mdi-cup-outline">
        <v-icon>mdi-cup-outline</v-icon>
        <span style="font-size: 11px;">Carte</span>
      </v-btn>

      <v-btn value="/panier" to="/panier">
        <v-badge
          v-if="panierStore.nombreArticles > 0"
          :content="panierStore.nombreArticles"
          color="secondary"
        >
          <v-icon>mdi-cart-outline</v-icon>
        </v-badge>
        <v-icon v-else>mdi-cart-outline</v-icon>
        <span style="font-size: 11px;">Panier</span>
      </v-btn>

      <v-btn value="/commandes" to="/commandes">
        <v-icon>mdi-clipboard-list-outline</v-icon>
        <span style="font-size: 11px;">Commandes</span>
      </v-btn>
    </v-bottom-navigation>
  </v-layout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import { usePanierStore } from '@/stores/usePanierStore'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const panierStore = usePanierStore()

const routeActive = computed(() => route.path)

function deconnexion() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
</style>
