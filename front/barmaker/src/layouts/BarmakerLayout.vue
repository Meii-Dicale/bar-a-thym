<template>
  <v-layout style="height: 100dvh; overflow: hidden;">

    <v-navigation-drawer permanent width="240" color="#F3EFE7" style="height: 100dvh;">
      <div style="height: 100%; display: flex; flex-direction: column; padding: 20px; overflow: hidden;">

        <div class="d-flex align-center" style="gap: 10px; flex-shrink: 0; margin-bottom: 32px;">
          <v-avatar size="44">
            <v-img :src="logo" alt="Bar à Thym" />
          </v-avatar>
          <span class="font-fraunces font-weight-bold font-italic" style="font-size: 17px; color: #1F2421; white-space: nowrap;">
            Bar à Thym
          </span>
        </div>

        <v-list nav density="compact" :lines="false" style="flex-shrink: 0; padding: 0;">
          <v-list-item
            v-for="item in navItems"
            :key="item.to"
            :to="item.to"
            :prepend-icon="item.icon"
            :title="item.label"
            active-color="primary"
            rounded="lg"
            style="white-space: nowrap;"
          />
        </v-list>

        <div style="flex: 1;" />

        <span
          v-if="authStore.utilisateur"
          style="font-size: 13px; color: #1F2421; opacity: 0.7; margin-bottom: 4px; white-space: nowrap;"
        >
          {{ authStore.utilisateur.prenom }}
        </span>

        <v-btn
          variant="text"
          color="primary"
          prepend-icon="mdi-logout"
          rounded="lg"
          class="mb-3"
          style="justify-content: flex-start;"
          @click="deconnexion"
        >
          Déconnexion
        </v-btn>

        <div class="pa-4 rounded-xl" style="background: #C98E5F; color: white; flex-shrink: 0;">
          <div style="font-size: 10px; font-weight: 600; text-transform: uppercase; opacity: 0.8;">
            Total cocktails
          </div>
          <div class="font-fraunces" style="font-size: 28px; font-weight: 700;">
            {{ cocktailStore.cocktails.length }}
          </div>
        </div>

      </div>
    </v-navigation-drawer>

    <v-main style="background: #F9F5EE; height: 100dvh; overflow: hidden; display: flex; flex-direction: column;">
      <div style="flex: 1; min-height: 0; overflow: hidden; padding: 32px; display: flex; flex-direction: column;">
        <router-view />
      </div>
    </v-main>

  </v-layout>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useCocktailStore } from '@/stores/useCocktailStore'
import { useAuthStore } from '@/stores/useAuthStore'
import logo from '@/assets/logo.png'

const router = useRouter()
const cocktailStore = useCocktailStore()
const authStore = useAuthStore()

function deconnexion() {
  authStore.logout()
  router.push('/login')
}

const navItems = [
  { to: '/ingredients',   icon: 'mdi-leaf-circle-outline', label: 'Ingrédients'   },
  { to: '/cocktails',     icon: 'mdi-cup',                 label: 'Cocktails'     },
  { to: '/commandes',     icon: 'mdi-clipboard-list',      label: 'Commandes'     },
  { to: '/mes-commandes', icon: 'mdi-circle-outline',      label: 'Mes commandes' }
]
</script>

<style scoped>
:deep(.v-navigation-drawer__content) {
  overflow: hidden !important;
}
</style>
