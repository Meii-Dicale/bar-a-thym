package com.barathym.repositories;

import com.barathym.entites.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {
    Optional<Cocktail> findByApiId(String apiId);
    List<Cocktail> findByActifTrue();

    @Query("SELECT DISTINCT c FROM Cocktail c " +
           "WHERE EXISTS (SELECT ci FROM CocktailIngredient ci WHERE ci.cocktail = c) " +
           "AND NOT EXISTS (SELECT ci2 FROM CocktailIngredient ci2 WHERE ci2.cocktail = c AND ci2.ingredient.disponible = false)")
    List<Cocktail> findAvecTousIngredentsDisponibles();

    @Query("SELECT DISTINCT c FROM Cocktail c " +
           "WHERE c.actif = true " +
           "AND EXISTS (SELECT tp FROM TaillePrix tp WHERE tp.cocktail = c)")
    List<Cocktail> findActifsAvecPrix();
}
