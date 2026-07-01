package com.barathym.repositories;

import com.barathym.entites.Cocktail;
import com.barathym.entites.CocktailIngredient;
import com.barathym.entites.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocktailIngredientRepository extends JpaRepository<CocktailIngredient, Long> {
    List<CocktailIngredient> findByCocktailId(Long cocktailId);
    boolean existsByCocktailAndIngredient(Cocktail cocktail, Ingredient ingredient);
}
