<template>
  <v-app style="background: #F9F5EE; min-height: 100dvh;">
    <v-main
      :style="{
        background: `linear-gradient(rgba(249,245,238,0.78), rgba(249,245,238,0.78)), url(${woodBg}) center / cover fixed`
      }"
    >
      <div
        class="d-flex flex-column align-center justify-center pa-6"
        style="min-height: 100dvh;"
      >
        <div class="text-center mb-10">
          <v-avatar size="120" class="mb-6">
            <v-img :src="logo" alt="Bar à Thym" />
          </v-avatar>
        </div>

        <v-card width="100%" max-width="360" rounded="xl" elevation="2" class="pa-2">
          <v-card-text class="pa-6">
            <v-alert
              v-if="inscrit"
              type="success"
              variant="tonal"
              rounded="lg"
              class="mb-5"
            >
              Compte créé ! Connectez-vous.
            </v-alert>

            <v-alert
              v-if="erreur"
              type="error"
              variant="tonal"
              rounded="lg"
              class="mb-5"
            >
              {{ erreur }}
            </v-alert>

            <p class="mb-5" style="color: rgba(31,36,33,0.7); font-size: 14px;">
              Connetez-vous pour accéder à la carte.
            </p>

            <v-text-field
              v-model="email"
              label="Adresse email"
              type="email"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-3"
            />

            <v-text-field
              v-model="motDePasse"
              label="Mot de passe"
              type="password"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-4"
              @keyup.enter="connexion"
            />

            <v-btn
              block
              color="primary"
              size="large"
              rounded="lg"
              :loading="chargement"
              @click="connexion"
            >
              Accéder à la carte
            </v-btn>

            <div class="text-center mt-5">
              <span style="font-size: 13px; color: rgba(31,36,33,0.6);">
                Pas encore de compte ?
              </span>
              <v-btn variant="text" color="primary" size="small" to="/inscription">
                S'inscrire
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </div>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import logo from '@/assets/logo.png'
import woodBg from '@/assets/wood-bg.jpg'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const email = ref('')
const motDePasse = ref('')
const erreur = ref('')
const chargement = ref(false)
const inscrit = ref(route.query.inscrit === '1')

async function connexion() {
  erreur.value = ''
  chargement.value = true
  const ok = await authStore.login(email.value, motDePasse.value)
  chargement.value = false
  if (ok) {
    router.push('/')
  } else {
    erreur.value = 'Aucun compte client trouvé pour cet email.'
  }
}
</script>

<style scoped>
</style>
