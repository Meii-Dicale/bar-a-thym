package com.barathym.mappers;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.entites.Cocktail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CocktailMapper {
    CocktailResponseDTO toDTO(Cocktail cocktail);
    List<CocktailResponseDTO> toDTO(List<Cocktail> cocktails);
    Cocktail toEntity(CocktailRequestDTO dto);
}
