import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { ArticlePanier } from '@/types'

export const usePanierStore = defineStore('panier', () => {
  const articles = ref<ArticlePanier[]>([])

  const total = computed(() =>
    articles.value.reduce((sum, a) => sum + a.prix, 0)
  )

  const nombreArticles = computed(() => articles.value.length)

  function ajouterArticle(article: ArticlePanier) {
    articles.value.push(article)
  }

  function supprimerArticle(index: number) {
    articles.value.splice(index, 1)
  }

  function vider() {
    articles.value = []
  }

  return { articles, total, nombreArticles, ajouterArticle, supprimerArticle, vider }
})
