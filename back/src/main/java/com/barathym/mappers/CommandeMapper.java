package com.barathym.mappers;

import com.barathym.dtos.CommandeRequestDTO;
import com.barathym.dtos.CommandeResponseDTO;
import com.barathym.entites.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LigneCommandeMapper.class})
public interface CommandeMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "barmaker.id", target = "barmakerId")
    @Mapping(source = "lignesCommande", target = "lignes")
    CommandeResponseDTO toDTO(Commande commande);
    List<CommandeResponseDTO> toDTO(List<Commande> commandes);
    Commande toEntity(CommandeRequestDTO dto);
}
