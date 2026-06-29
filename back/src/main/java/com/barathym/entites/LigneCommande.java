package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lignes_commande")
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LC_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMD_ID", nullable = false)
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CO_ID", nullable = false)
    private Cocktail cocktail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TP_ID", nullable = false)
    private TaillePrix taillePrix;

    @Column(name = "LC_Note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "LC_Statut", nullable = false)
    private Statut statut = Statut.PREPARATION;

    public enum Statut {
        PREPARATION,
        ASSEMBLAGE,
        DRESSAGE,
        TERMINEE
    }
}
