package com.barathym.dtos;

public record IngredientResponseDTO(
    Long id,
    String apiId,
    String nom,
    String imageUrl,
    Boolean disponible
) {}
