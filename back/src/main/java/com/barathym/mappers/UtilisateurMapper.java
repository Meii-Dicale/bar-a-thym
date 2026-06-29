package com.barathym.mappers;

import com.barathym.dtos.UtilisateurRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    UtilisateurResponseDTO toDTO(Utilisateur utilisateur);
    List<UtilisateurResponseDTO> toDTO(List<Utilisateur> utilisateurs);
    Utilisateur toEntity(UtilisateurRequestDTO dto);
}
