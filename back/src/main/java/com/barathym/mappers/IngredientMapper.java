package com.barathym.mappers;

import com.barathym.dtos.IngredientRequestDTO;
import com.barathym.dtos.IngredientResponseDTO;
import com.barathym.entites.Ingredient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponseDTO toDTO(Ingredient ingredient);
    List<IngredientResponseDTO> toDTO(List<Ingredient> ingredients);
    Ingredient toEntity(IngredientRequestDTO dto);
}
