import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'
import LoginView from '@/views/LoginView.vue'
import ClientLayout from '@/layouts/ClientLayout.vue'
import AccueilView from '@/views/AccueilView.vue'
import PanierView from '@/views/PanierView.vue'
import MesCommandesView from '@/views/MesCommandesView.vue'
import CommandeDetailView from '@/views/CommandeDetailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    {
      path: '/',
      component: ClientLayout,
      meta: { requiresAuth: true },
      children: [
        { path: '',          component: AccueilView      },
        { path: 'panier',   component: PanierView       },
        { path: 'commandes', component: MesCommandesView },
        { path: 'commandes/:id', component: CommandeDetailView }
      ]
    }
  ]
})

router.beforeEach(to => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.estConnecte) {
    return '/login'
  }
  if (to.path === '/login' && auth.estConnecte) {
    return '/'
  }
})

export default router
