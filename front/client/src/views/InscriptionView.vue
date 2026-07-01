<template>
  <v-app style="background: #F9F5EE;">
    <v-main>
      <div
        class="d-flex flex-column align-center justify-center pa-6"
        style="min-height: 100vh;"
      >
        <div class="text-center mb-8">
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
            Créer un compte client
          </p>
        </div>

        <v-card width="100%" max-width="360" rounded="xl" elevation="2" class="pa-2">
          <v-card-text class="pa-6">
            <v-alert v-if="erreur" type="error" variant="tonal" rounded="lg" class="mb-5">
              {{ erreur }}
            </v-alert>

            <v-text-field
              v-model="form.prenom"
              label="Prénom"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-3"
            />

            <v-text-field
              v-model="form.nom"
              label="Nom"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-3"
            />

            <v-text-field
              v-model="form.email"
              label="Adresse email"
              type="email"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-3"
            />

            <v-text-field
              v-model="form.motDePasse"
              label="Mot de passe"
              type="password"
              variant="outlined"
              rounded="lg"
              color="primary"
              class="mb-5"
              @keyup.enter="inscrire"
            />

            <v-btn
              block
              color="primary"
              size="large"
              rounded="lg"
              :loading="chargement"
              @click="inscrire"
            >
              Créer mon compte
            </v-btn>

            <div class="text-center mt-5">
              <span style="font-size: 13px; color: rgba(31,36,33,0.6);">
                Déjà un compte ?
              </span>
              <v-btn variant="text" color="primary" size="small" to="/login">
                Se connecter
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </div>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/authService'

const router = useRouter()

const form = reactive({
  prenom: '',
  nom: '',
  email: '',
  motDePasse: ''
})
const erreur = ref('')
const chargement = ref(false)

async function inscrire() {
  erreur.value = ''
  if (!form.prenom || !form.nom || !form.email || !form.motDePasse) {
    erreur.value = 'Tous les champs sont obligatoires.'
    return
  }
  chargement.value = true
  try {
    await authService.inscription(form)
    router.push({ path: '/login', query: { inscrit: '1' } })
  } catch {
    erreur.value = 'Une erreur est survenue. Cet email est peut-être déjà utilisé.'
  } finally {
    chargement.value = false
  }
}
</script>

<style scoped>
</style>
