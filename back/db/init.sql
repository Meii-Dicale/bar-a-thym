-- ============================================================
--  Bar à Thym — Script d'initialisation de la base de données
-- ============================================================

CREATE TABLE utilisateurs (
    US_ID       SERIAL PRIMARY KEY,
    US_Nom      VARCHAR(255) NOT NULL,
    US_Prenom   VARCHAR(255) NOT NULL,
    US_Email    VARCHAR(255) NOT NULL UNIQUE,
    US_MotDePasse VARCHAR(255) NOT NULL,
    US_Role     VARCHAR(50)  NOT NULL
);

CREATE TABLE ingredients (
    ING_ID          SERIAL PRIMARY KEY,
    ING_ApiId       VARCHAR(255) NOT NULL UNIQUE,
    ING_Nom         VARCHAR(255) NOT NULL,
    ING_ImageUrl    VARCHAR(500),
    ING_Disponible  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE cocktails (
    CO_ID           SERIAL PRIMARY KEY,
    CO_ApiId        VARCHAR(255) NOT NULL UNIQUE,
    CO_Nom          VARCHAR(255) NOT NULL,
    CO_ImageUrl     VARCHAR(500),
    CO_Categorie    VARCHAR(255),
    CO_Actif        BOOLEAN NOT NULL DEFAULT FALSE,
    CO_Instructions TEXT
);

CREATE TABLE cocktail_ingredients (
    CI_ID       SERIAL PRIMARY KEY,
    CO_ID       INTEGER NOT NULL REFERENCES cocktails(CO_ID) ON DELETE CASCADE,
    ING_ID      INTEGER NOT NULL REFERENCES ingredients(ING_ID) ON DELETE CASCADE,
    CI_Quantite VARCHAR(100)
);

CREATE TABLE tailles_prix (
    TP_ID       SERIAL PRIMARY KEY,
    CO_ID       INTEGER      NOT NULL REFERENCES cocktails(CO_ID) ON DELETE CASCADE,
    TP_Taille   VARCHAR(10)  NOT NULL,
    TP_Prix     DECIMAL(5,2) NOT NULL
);

CREATE TABLE commandes (
    CMD_ID          SERIAL PRIMARY KEY,
    CMD_ClientId    INTEGER      NOT NULL REFERENCES utilisateurs(US_ID),
    CMD_BarMakerId  INTEGER      REFERENCES utilisateurs(US_ID),
    CMD_Statut      VARCHAR(50)  NOT NULL DEFAULT 'COMMANDEE',
    CMD_Total       DECIMAL(8,2) NOT NULL,
    CMD_CreeLe      TIMESTAMP    NOT NULL
);

CREATE TABLE lignes_commande (
    LC_ID       SERIAL PRIMARY KEY,
    CMD_ID      INTEGER     NOT NULL REFERENCES commandes(CMD_ID) ON DELETE CASCADE,
    CO_ID       INTEGER     NOT NULL REFERENCES cocktails(CO_ID),
    TP_ID       INTEGER     NOT NULL REFERENCES tailles_prix(TP_ID),
    LC_Note     TEXT,
    LC_Statut   VARCHAR(50) NOT NULL DEFAULT 'PREPARATION'
);

-- ============================================================
--  Données initiales
-- ============================================================

INSERT INTO utilisateurs (US_Nom, US_Prenom, US_Email, US_MotDePasse, US_Role) VALUES
    ('Admin', 'Bar', 'barmaker@barathym.fr', 'barmaker123', 'BARMAKER'),
    ('Dupont', 'Marie', 'marie@barathym.fr',  'client123',   'CLIENT');
