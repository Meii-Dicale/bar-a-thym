<template>
  <v-app style="background: #F9F5EE;">
    <v-main>
      <div
        class="d-flex flex-column align-center justify-center pa-6"
        style="min-height: 100vh;"
      >
        <div class="text-center mb-10">
          <v-avatar color="primary" size="80" rounded="xl" class="mb-6">
            <v-icon icon="mdi-leaf" color="white" size="40" />
          </v-avatar>
          <h1
            class="font-fraunces font-italic"
            style="font-size: 36px; color: #1F2421; font-weight: 700;"
          >
            Bar à Thym
          </h1>
          <p style="color: rgba(31,36,33,0.55); margin-top: 6px; font-size: 15px;">
            Cocktails artisanaux
          </p>
        </div>

        <v-card width="100%" max-width="360" rounded="xl" elevation="2" class="pa-2">
          <v-card-text class="pa-6">
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
              Entrez votre adresse email pour accéder à la carte.
            </p>

            <v-text-field
              v-model="email"
              label="Adresse email"
              type="email"
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
          </v-card-text>
        </v-card>
      </div>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const erreur = ref('')
const chargement = ref(false)

async function connexion() {
  erreur.value = ''
  chargement.value = true
  const ok = await authStore.login(email.value)
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
