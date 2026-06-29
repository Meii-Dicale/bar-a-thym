package com.barathym.mappers;

import com.barathym.dtos.TaillePrixRequestDTO;
import com.barathym.dtos.TaillePrixResponseDTO;
import com.barathym.entites.TaillePrix;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaillePrixMapper {
    @Mapping(source = "cocktail.id", target = "cocktailId")
    TaillePrixResponseDTO toDTO(TaillePrix taillePrix);
    List<TaillePrixResponseDTO> toDTO(List<TaillePrix> taillesPrix);
    TaillePrix toEntity(TaillePrixRequestDTO dto);
}
