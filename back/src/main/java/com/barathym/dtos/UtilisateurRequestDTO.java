package com.barathym.dtos;

import com.barathym.entites.Utilisateur.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UtilisateurRequestDTO(
    @NotBlank String nom,
    @NotBlank String prenom,
    @NotBlank @Email String email,
    @NotBlank String motDePasse,
    @NotNull Role role
) {}
