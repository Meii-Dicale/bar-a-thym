package com.barathym.dtos;

import com.barathym.entites.Utilisateur.Role;

public record UtilisateurResponseDTO(
    Long id,
    String nom,
    String prenom,
    String email,
    Role role
) {}
