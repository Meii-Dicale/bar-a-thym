package com.barathym.dtos;

import com.barathym.entites.LigneCommande.Statut;
import com.barathym.entites.TaillePrix.Taille;

public record LigneCommandeResponseDTO(
    Long id,
    Long commandeId,
    Long cocktailId,
    String cocktailNom,
    Long taillePrixId,
    Taille taille,
    String note,
    Statut statut
) {}
