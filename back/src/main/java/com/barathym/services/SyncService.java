package com.barathym.services;

import com.barathym.dtos.cocktailapi.CocktailApiItemDTO;
import com.barathym.entites.Cocktail;
import com.barathym.entites.CocktailIngredient;
import com.barathym.entites.Ingredient;
import com.barathym.repositories.CocktailIngredientRepository;
import com.barathym.repositories.CocktailRepository;
import com.barathym.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final CocktailApiClient cocktailApiClient;
    private final CocktailRepository cocktailRepository;
    private final IngredientRepository ingredientRepository;
    private final CocktailIngredientRepository cocktailIngredientRepository;

    @Transactional
    public int syncCocktails() {
        int total = 0;
        for (char lettre = 'a'; lettre <= 'z'; lettre++) {
            List<CocktailApiItemDTO> items = cocktailApiClient.rechercherParLettre(lettre);
            for (CocktailApiItemDTO item : items) {
                syncCocktail(item);
                total++;
            }
        }
        return total;
    }

    private void syncCocktail(CocktailApiItemDTO item) {
        Cocktail cocktail = cocktailRepository.findByApiId(item.getIdDrink())
                .orElseGet(() -> {
                    Cocktail c = new Cocktail();
                    c.setApiId(item.getIdDrink());
                    c.setActif(false);
                    return c;
                });

        cocktail.setNom(item.getStrDrink());
        cocktail.setImageUrl(item.getStrDrinkThumb());
        cocktail.setCategorie(item.getStrCategory());
        cocktail.setInstructions(item.getStrInstructionsFR());
        cocktail = cocktailRepository.save(cocktail);

        for (CocktailApiItemDTO.IngredientMesure im : item.getIngredientsMesures()) {
            Ingredient ingredient = syncIngredient(im.nom());
            if (!cocktailIngredientRepository.existsByCocktailAndIngredient(cocktail, ingredient)) {
                CocktailIngredient ci = new CocktailIngredient();
                ci.setCocktail(cocktail);
                ci.setIngredient(ingredient);
                ci.setQuantite(im.mesure());
                cocktailIngredientRepository.save(ci);
            }
        }
    }

    private Ingredient syncIngredient(String nom) {
        return ingredientRepository.findByNomIgnoreCase(nom)
                .orElseGet(() -> {
                    Ingredient i = new Ingredient();
                    i.setApiId(nom.toLowerCase().replace(" ", "_"));
                    i.setNom(nom);
                    i.setImageUrl("https://www.thecocktaildb.com/images/ingredients/" +
                                  nom.replace(" ", "%20") + "-Small.png");
                    i.setDisponible(false);
                    return ingredientRepository.save(i);
                });
    }
}
