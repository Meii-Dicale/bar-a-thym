package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "US_ID")
    private Long id;

    @Column(name = "US_Nom", nullable = false)
    private String nom;

    @Column(name = "US_Prenom", nullable = false)
    private String prenom;

    @Column(name = "US_Email", nullable = false, unique = true)
    private String email;

    @Column(name = "US_MotDePasse", nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "US_Role", nullable = false)
    private Role role;

    public enum Role {
        CLIENT,
        BARMAKER
    }
}
