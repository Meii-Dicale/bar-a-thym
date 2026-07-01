package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cocktails")
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CO_ID")
    private Long id;

    @Column(name = "CO_ApiId", unique = true, nullable = false)
    private String apiId;

    @Column(name = "CO_Nom", nullable = false)
    private String nom;

    @Column(name = "CO_ImageUrl")
    private String imageUrl;

    @Column(name = "CO_Categorie")
    private String categorie;

    @Column(name = "CO_Actif", nullable = false)
    private Boolean actif = false;

    @Column(name = "CO_Instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "CO_Alcoolise")
    private Boolean alcoolise;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailIngredient> cocktailIngredients;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaillePrix> taillesPrix;
}
