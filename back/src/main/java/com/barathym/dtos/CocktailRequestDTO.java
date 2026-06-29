package com.barathym.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CocktailRequestDTO(
    @NotBlank String apiId,
    @NotBlank String nom,
    String imageUrl,
    String categorie,
    @NotNull Boolean actif,
    String instructions
) {}
