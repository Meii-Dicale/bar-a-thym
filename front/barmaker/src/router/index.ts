import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/',
      component: () => import('@/layouts/BarmakerLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/ingredients'
        },
        {
          path: 'ingredients',
          name: 'ingredients',
          component: () => import('@/views/IngredientsView.vue')
        },
        {
          path: 'cocktails',
          name: 'cocktails',
          component: () => import('@/views/CocktailsView.vue')
        },
        {
          path: 'cocktails/:id/recette',
          name: 'recette',
          component: () => import('@/views/RecetteView.vue')
        },
        {
          path: 'commandes',
          name: 'commandes',
          component: () => import('@/views/CommandesView.vue')
        },
        {
          path: 'commandes/:id',
          name: 'commande-detail',
          component: () => import('@/views/CommandeDetailView.vue')
        },
        {
          path: 'mes-commandes',
          name: 'mes-commandes',
          component: () => import('@/views/MesCommandesView.vue')
        }
      ]
    },
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login' }
  }
  if (to.name === 'login' && auth.isAuthenticated) {
    return { name: 'ingredients' }
  }
})

export default router
