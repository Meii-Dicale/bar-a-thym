package com.barathym.dtos;

import com.barathym.entites.TaillePrix.Taille;

import java.math.BigDecimal;

public record TaillePrixResponseDTO(
    Long id,
    Long cocktailId,
    Taille taille,
    BigDecimal prix
) {}
