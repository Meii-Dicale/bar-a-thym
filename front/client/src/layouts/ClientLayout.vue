<template>
  <v-layout style="height: 100dvh; overflow: hidden;">

    <v-app-bar flat color="#F9F5EE" border="b" style="flex-shrink: 0;">
      <template #prepend>
        <v-avatar size="36" class="ml-2">
          <v-img :src="logo" alt="Bar à Thym" />
        </v-avatar>
      </template>
      <v-app-bar-title>
        <span class="font-fraunces font-italic font-weight-bold" style="font-size: 20px; color: #1F2421;">
          Bar à Thym
        </span>
      </v-app-bar-title>
      <template #append>
        <span v-if="authStore.utilisateur" style="font-size: 13px; color: #1F2421; margin-right: 4px;">
          {{ authStore.utilisateur.prenom }}
        </span>
        <v-btn icon="mdi-logout" variant="text" color="primary" size="small" @click="deconnexion" />
      </template>
    </v-app-bar>

    <v-main
      :style="{
        background: `linear-gradient(rgba(249,245,238,0.78), rgba(249,245,238,0.78)), url(${woodBg}) center / cover fixed`,
        overflowY: 'auto',
        WebkitOverflowScrolling: 'touch'
      }"
    >
      <router-view />
    </v-main>

    <v-bottom-navigation
      :model-value="routeActive"
      color="primary"
      bg-color="white"
      elevation="8"
      grow
      style="flex-shrink: 0;"
    >
      <v-btn value="/" @click="router.push('/')">
        <v-icon>mdi-cup-outline</v-icon>
        <span style="font-size: 11px;">Carte</span>
      </v-btn>

      <v-btn value="/panier" @click="router.push('/panier')">
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

      <v-btn value="/commandes" @click="router.push('/commandes')">
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
import logo from '@/assets/logo.png'
import woodBg from '@/assets/wood-bg.jpg'

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
:deep(.v-main__wrap) {
  height: 100%;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}
</style>
