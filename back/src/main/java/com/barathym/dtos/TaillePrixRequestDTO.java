package com.barathym.dtos;

import com.barathym.entites.TaillePrix.Taille;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TaillePrixRequestDTO(
    @NotNull Long cocktailId,
    @NotNull Taille taille,
    @NotNull @Positive BigDecimal prix
) {}
