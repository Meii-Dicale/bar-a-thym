# Bar à Thym

Application de commande de cocktails pour le bar Bar à Thym.
Les clients passent commande depuis leur téléphone, les barmakers gèrent la préparation depuis leur poste.

---

## Architecture

```
bar-a-thym/
├── back/          API REST — Java 21 / Spring Boot 4
├── front/
│   ├── barmaker/  Interface barmaker — Vue 3 / Vuetify (port 3001)
│   └── client/    Interface client mobile — Vue 3 / Vuetify (port 3000)
└── docker-compose.yml
```

**Stack technique**

| Couche          | Technologies                                       |
|-----------------|----------------------------------------------------|
| Backend         | Java 21, Spring Boot 4, Spring Data JPA, MapStruct |
| Base de données | PostgreSQL 16                                      |
| Frontend        | Vue 3, TypeScript, Vuetify 3, Pinia, Vue Router    |
| Tests           | JUnit 5 + Mockito (back), Vitest (front)           |
| Déploiement     | Docker, Docker Compose, Nginx                      |

---

## Prérequis

- Docker & Docker Compose
- Java 21 + Maven 3.9 (développement local backend)
- Node.js 20 (développement local frontend)

---

## Lancement rapide avec Docker

```bash
# 1. Copier le fichier d'environnement
cp .env.example .env

# 2. Construire et démarrer tous les services
docker compose up --build
```

| Service  | URL                       |
|----------|---------------------------|
| Client   | http://localhost:3000      |
| Barmaker | http://localhost:3001      |
| API REST | http://localhost:8080/api  |

---

## Synchronisation des cocktails (TheCocktailDB)

Après le premier démarrage, importer le catalogue de cocktails :

```bash
curl -X POST http://localhost:8080/api/sync/cocktails
```

Cette opération récupère tous les cocktails disponibles via [TheCocktailDB](https://www.thecocktaildb.com/api.php),
crée les ingrédients correspondants (désactivés par défaut) et les associe à chaque cocktail.
Le barmaker active ensuite les ingrédients disponibles dans son bar depuis l'interface dédiée.
Les cocktails dont tous les ingrédients actifs sont disponibles peuvent alors être activés pour la carte.

---

## Développement local

### Backend

```bash
cd back
mvn spring-boot:run
# API disponible sur http://localhost:8080
```

> Nécessite une instance PostgreSQL locale. Paramètres dans `back/src/main/resources/application.properties`.

### Barmaker

```bash
cd front/barmaker
npm install
npm run dev
# http://localhost:3001
```

### Client

```bash
cd front/client
npm install
npm run dev
# http://localhost:3000
```

---

## Tests

```bash
# Backend (JUnit 5 + Mockito)
cd back && mvn test

# Frontend barmaker (Vitest)
cd front/barmaker && npm test

# Frontend client (Vitest)
cd front/client && npm test
```

---

## Principaux endpoints API

| Méthode | Endpoint                                            | Description                        |
|---------|-----------------------------------------------------|------------------------------------|
| GET     | `/api/ingredients`                                  | Liste tous les ingrédients         |
| PATCH   | `/api/ingredients/{id}/disponible`                  | Active/désactive un ingrédient     |
| GET     | `/api/cocktails`                                    | Liste tous les cocktails           |
| GET     | `/api/cocktails/actifs`                             | Liste les cocktails disponibles    |
| PATCH   | `/api/cocktails/{id}/actif`                         | Active/désactive un cocktail       |
| GET     | `/api/tailles-prix/cocktail/{cocktailId}`           | Prix S/M/L d'un cocktail          |
| POST    | `/api/commandes`                                    | Passer une commande                |
| GET     | `/api/commandes/en-attente`                         | Commandes en attente (barmaker)    |
| GET     | `/api/commandes/client/{clientId}`                  | Commandes d'un client              |
| PATCH   | `/api/commandes/{id}/prendre-en-charge/{barmakerId}`| Assigner une commande à un barmaker|
| PATCH   | `/api/lignes-commande/{id}/avancer`                 | Avancer l'étape d'une ligne       |
| POST    | `/api/sync/cocktails`                               | Synchroniser depuis TheCocktailDB  |

---

## Comptes de démonstration

| Rôle     | Email                |
|----------|----------------------|
| Barmaker | barmaker@barathym.fr |
| Client   | client@barathym.fr   |
