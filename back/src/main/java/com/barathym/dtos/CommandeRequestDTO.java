package com.barathym.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CommandeRequestDTO(
    @NotNull Long clientId,
    @NotEmpty List<LigneCommandeRequestDTO> lignes
) {}
