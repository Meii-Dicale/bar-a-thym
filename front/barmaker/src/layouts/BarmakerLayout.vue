<template>
  <v-layout>
    <v-navigation-drawer permanent width="200" color="#F3EFE7">
      <div class="pa-6 d-flex flex-column" style="gap: 48px; height: 100%;">

        <div class="d-flex align-center" style="gap: 10px;">
          <v-avatar color="primary" size="48" rounded="lg">
            <v-icon icon="mdi-leaf" color="white" />
          </v-avatar>
          <span class="font-fraunces font-weight-bold font-italic" style="font-size: 18px; color: #1F2421;">
            Bar à Thym
          </span>
        </div>

        <v-list nav density="compact" :lines="false">
          <v-list-item
            v-for="item in navItems"
            :key="item.to"
            :to="item.to"
            :prepend-icon="item.icon"
            :title="item.label"
            active-color="primary"
            rounded="lg"
          />
        </v-list>

        <div class="mt-auto">
          <div
            class="pa-4 rounded-xl"
            style="background: #C98E5F; color: white;"
          >
            <div style="font-size: 10px; font-weight: 600; text-transform: uppercase; opacity: 0.8;">
              Total cocktails
            </div>
            <div class="font-fraunces" style="font-size: 28px; font-weight: 700;">
              {{ cocktailStore.cocktails.length }}
            </div>
          </div>
        </div>

      </div>
    </v-navigation-drawer>

    <v-main style="background: #F9F5EE; min-height: 100vh;">
      <div class="pa-8">
        <router-view />
      </div>
    </v-main>
  </v-layout>
</template>

<script setup lang="ts">
import { useCocktailStore } from '@/stores/useCocktailStore'

const cocktailStore = useCocktailStore()

const navItems = [
  { to: '/ingredients',   icon: 'mdi-leaf-circle-outline', label: 'Ingredients'    },
  { to: '/cocktails',     icon: 'mdi-cup',                 label: 'Cocktails'      },
  { to: '/commandes',     icon: 'mdi-clipboard-list',      label: 'Commandes'      },
  { to: '/mes-commandes', icon: 'mdi-circle-outline',      label: 'Mes commandes'  }
]
</script>

<style scoped>
</style>
