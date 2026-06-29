package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tailles_prix")
public class TaillePrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_ID", nullable = false)
    private Cocktail cocktail;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_Taille", nullable = false)
    private Taille taille;

    @Column(name = "TP_Prix", nullable = false, precision = 5, scale = 2)
    private BigDecimal prix;

    public enum Taille {
        S, M, L
    }
}
