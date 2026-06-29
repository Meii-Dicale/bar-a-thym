package com.barathym.dtos;

import com.barathym.entites.Commande.Statut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CommandeResponseDTO(
    Long id,
    Long clientId,
    Long barmakerId,
    Statut statut,
    BigDecimal total,
    LocalDateTime creeLe,
    List<LigneCommandeResponseDTO> lignes
) {}
