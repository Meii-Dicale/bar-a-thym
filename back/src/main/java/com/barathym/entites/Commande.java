package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CMD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMD_ClientId", nullable = false)
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMD_BarMakerId")
    private Utilisateur barmaker;

    @Enumerated(EnumType.STRING)
    @Column(name = "CMD_Statut", nullable = false)
    private Statut statut = Statut.COMMANDEE;

    @Column(name = "CMD_Total", nullable = false, precision = 8, scale = 2)
    private BigDecimal total;

    @Column(name = "CMD_CreeLe", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignesCommande;

    public enum Statut {
        COMMANDEE,
        EN_COURS,
        TERMINEE
    }
}
