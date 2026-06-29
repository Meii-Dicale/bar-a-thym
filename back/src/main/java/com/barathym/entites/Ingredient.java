package com.barathym.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ING_ID")
    private Long id;

    @Column(name = "ING_ApiId", unique = true, nullable = false)
    private String apiId;

    @Column(name = "ING_Nom", nullable = false)
    private String nom;

    @Column(name = "ING_ImageUrl")
    private String imageUrl;

    @Column(name = "ING_Disponible", nullable = false)
    private Boolean disponible = false;
}
