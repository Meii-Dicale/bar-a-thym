package com.barathym.dtos;

public record CocktailResponseDTO(
    Long id,
    String apiId,
    String nom,
    String imageUrl,
    String categorie,
    Boolean actif,
    String instructions
) {}
