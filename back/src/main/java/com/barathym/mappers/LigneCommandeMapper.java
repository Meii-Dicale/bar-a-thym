package com.barathym.mappers;

import com.barathym.dtos.LigneCommandeRequestDTO;
import com.barathym.dtos.LigneCommandeResponseDTO;
import com.barathym.entites.LigneCommande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LigneCommandeMapper {
    @Mapping(source = "commande.id", target = "commandeId")
    @Mapping(source = "cocktail.id", target = "cocktailId")
    @Mapping(source = "cocktail.nom", target = "cocktailNom")
    @Mapping(source = "taillePrix.id", target = "taillePrixId")
    @Mapping(source = "taillePrix.taille", target = "taille")
    @Mapping(source = "taillePrix.prix", target = "prix")
    LigneCommandeResponseDTO toDTO(LigneCommande ligneCommande);
    List<LigneCommandeResponseDTO> toDTO(List<LigneCommande> lignes);
    LigneCommande toEntity(LigneCommandeRequestDTO dto);
}
