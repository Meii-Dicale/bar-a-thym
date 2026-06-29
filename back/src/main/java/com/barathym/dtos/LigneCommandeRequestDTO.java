package com.barathym.dtos;

import jakarta.validation.constraints.NotNull;

public record LigneCommandeRequestDTO(
    @NotNull Long cocktailId,
    @NotNull Long taillePrixId,
    String note
) {}
