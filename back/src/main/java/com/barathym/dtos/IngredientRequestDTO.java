package com.barathym.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequestDTO(
    @NotBlank String apiId,
    @NotBlank String nom,
    String imageUrl,
    @NotNull Boolean disponible
) {}
