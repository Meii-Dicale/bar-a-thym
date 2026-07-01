package com.barathym.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DefinirPrixDTO(
    @NotNull @Positive BigDecimal prixS,
    @NotNull @Positive BigDecimal prixM,
    @NotNull @Positive BigDecimal prixL
) {}
