package com.barathym.dtos;

import com.barathym.entites.LigneCommande.Statut;
import com.barathym.entites.TaillePrix.Taille;

import java.math.BigDecimal;

public record LigneCommandeResponseDTO(
    Long id,
    Long commandeId,
    Long cocktailId,
    String cocktailNom,
    Long taillePrixId,
    Taille taille,
    BigDecimal prix,
    String note,
    Statut statut
) {}
