package com.barathym.dtos;

import com.barathym.entites.LigneCommande.Statut;

public record LigneCommandeResponseDTO(
    Long id,
    Long commandeId,
    Long cocktailId,
    String cocktailNom,
    Long taillePrixId,
    String note,
    Statut statut
) {}
