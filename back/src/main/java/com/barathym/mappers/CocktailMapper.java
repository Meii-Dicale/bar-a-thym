package com.barathym.mappers;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.entites.CocktailIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CocktailMapper {

    @Mapping(target = "ingredients", expression = "java(extractIngredients(cocktail))")
    CocktailResponseDTO toDTO(Cocktail cocktail);

    List<CocktailResponseDTO> toDTO(List<Cocktail> cocktails);

    Cocktail toEntity(CocktailRequestDTO dto);

    default List<String> extractIngredients(Cocktail cocktail) {
        if (cocktail.getCocktailIngredients() == null) return List.of();
        return cocktail.getCocktailIngredients().stream()
                .map(ci -> ci.getIngredient().getNom())
                .collect(Collectors.toList());
    }
}
