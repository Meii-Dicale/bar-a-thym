<template>
  <v-layout style="height: 100vh; overflow: hidden;">

    <v-navigation-drawer permanent width="240" color="#F3EFE7" style="height: 100vh;">
      <div style="height: 100%; display: flex; flex-direction: column; padding: 20px; overflow: hidden;">

        <div class="d-flex align-center" style="gap: 10px; flex-shrink: 0; margin-bottom: 32px;">
          <v-avatar color="primary" size="44" rounded="lg">
            <v-icon icon="mdi-leaf" color="white" />
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

    <v-main style="background: #F9F5EE; height: 100vh; overflow: hidden; display: flex; flex-direction: column;">
      <div style="flex: 1; min-height: 0; overflow: hidden; padding: 32px; display: flex; flex-direction: column;">
        <router-view />
      </div>
    </v-main>

  </v-layout>
</template>

<script setup lang="ts">
import { useCocktailStore } from '@/stores/useCocktailStore'

const cocktailStore = useCocktailStore()

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
