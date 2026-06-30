import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { usePanierStore } from '@/stores/usePanierStore'
import type { ArticlePanier } from '@/types'

const article: ArticlePanier = {
  cocktailId: 1, cocktailNom: 'Mojito', cocktailImageUrl: null,
  taillePrixId: 2, taille: 'M', prix: 9.5, note: ''
}

describe('usePanierStore', () => {
  beforeEach(() => setActivePinia(createPinia()))

  it('démarre vide', () => {
    const store = usePanierStore()
    expect(store.articles).toHaveLength(0)
    expect(store.total).toBe(0)
    expect(store.nombreArticles).toBe(0)
  })

  it('ajoute un article', () => {
    const store = usePanierStore()
    store.ajouterArticle(article)
    expect(store.articles).toHaveLength(1)
    expect(store.total).toBe(9.5)
    expect(store.nombreArticles).toBe(1)
  })

  it('ajoute plusieurs articles et calcule le total', () => {
    const store = usePanierStore()
    store.ajouterArticle(article)
    store.ajouterArticle({ ...article, prix: 11.5 })
    expect(store.total).toBe(21)
  })

  it('supprime un article par index', () => {
    const store = usePanierStore()
    store.ajouterArticle(article)
    store.ajouterArticle({ ...article, cocktailNom: 'Cosmo' })
    store.supprimerArticle(0)
    expect(store.articles).toHaveLength(1)
    expect(store.articles[0].cocktailNom).toBe('Cosmo')
  })

  it('vide le panier', () => {
    const store = usePanierStore()
    store.ajouterArticle(article)
    store.vider()
    expect(store.articles).toHaveLength(0)
    expect(store.total).toBe(0)
  })
})
