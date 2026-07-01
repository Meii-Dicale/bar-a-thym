package com.barathym.dtos;

import java.util.List;

public record CocktailResponseDTO(
    Long id,
    String apiId,
    String nom,
    String imageUrl,
    String categorie,
    Boolean actif,
    String instructions,
    List<IngredientCocktailDTO> ingredients,
    Boolean aPrix
) {}
