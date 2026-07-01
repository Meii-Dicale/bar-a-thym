export interface Utilisateur {
  id: number
  nom: string
  prenom: string
  email: string
  role: 'CLIENT' | 'BARMAKER'
}

export interface Ingredient {
  id: number
  apiId: string
  nom: string
  imageUrl: string | null
  disponible: boolean
}

export interface TaillePrix {
  id: number
  cocktailId: number
  taille: 'S' | 'M' | 'L'
  prix: number
}

export interface Cocktail {
  id: number
  apiId: string
  nom: string
  imageUrl: string | null
  categorie: string | null
  actif: boolean
  instructions: string | null
}

export interface LigneCommande {
  id: number
  commandeId: number
  cocktailId: number
  cocktailNom: string
  taillePrixId: number
  taille: 'S' | 'M' | 'L'
  note: string | null
  statut: 'PREPARATION' | 'ASSEMBLAGE' | 'DRESSAGE' | 'TERMINEE'
}

export interface Commande {
  id: number
  clientId: number
  barmakerId: number | null
  statut: 'COMMANDEE' | 'EN_COURS' | 'TERMINEE'
  total: number
  creeLe: string
  lignes: LigneCommande[]
}

export interface ArticlePanier {
  cocktailId: number
  cocktailNom: string
  cocktailImageUrl: string | null
  taillePrixId: number
  taille: 'S' | 'M' | 'L'
  prix: number
  note: string
}

export const STATUT_LIGNE_LABELS: Record<LigneCommande['statut'], string> = {
  PREPARATION: 'Préparation',
  ASSEMBLAGE:  'Assemblage',
  DRESSAGE:    'Dressage',
  TERMINEE:    'Terminée'
}

export const STATUT_COMMANDE_LABELS: Record<Commande['statut'], string> = {
  COMMANDEE: 'En attente',
  EN_COURS:  'En cours',
  TERMINEE:  'Prête'
}
