<template>
  <v-app style="background: #F9F5EE;">
    <v-main>
      <v-container class="d-flex align-center justify-center" style="min-height: 100vh;">
        <v-card width="400" rounded="xl" elevation="2">
          <v-card-text class="pa-8">

            <div class="text-center mb-8">
              <v-avatar size="120" class="mb-4">
                <v-img :src="logo" alt="Bar à Thym" />
              </v-avatar>
              <p style="color: rgba(31,36,33,0.6); margin-top: 4px;">Espace Barmaker</p>
            </div>

            <v-alert v-if="erreur" type="error" variant="tonal" class="mb-4" rounded="lg">
              {{ erreur }}
            </v-alert>

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
              Se connecter
            </v-btn>

          </v-card-text>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import logo from '@/assets/logo.png'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const motDePasse = ref('')
const erreur = ref('')
const chargement = ref(false)

async function connexion() {
  erreur.value = ''
  chargement.value = true
  const ok = await authStore.login(email.value, motDePasse.value)
  chargement.value = false
  if (ok) {
    router.push('/')
  } else {
    erreur.value = 'Email ou mot de passe incorrect.'
  }
}
</script>

<style scoped>
</style>
