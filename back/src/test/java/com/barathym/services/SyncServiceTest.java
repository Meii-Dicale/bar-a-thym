package com.barathym.services;

import com.barathym.dtos.cocktailapi.CocktailApiItemDTO;
import com.barathym.entites.Cocktail;
import com.barathym.entites.CocktailIngredient;
import com.barathym.entites.Ingredient;
import com.barathym.repositories.CocktailIngredientRepository;
import com.barathym.repositories.CocktailRepository;
import com.barathym.repositories.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncServiceTest {

    @Mock private CocktailApiClient cocktailApiClient;
    @Mock private CocktailRepository cocktailRepository;
    @Mock private IngredientRepository ingredientRepository;
    @Mock private CocktailIngredientRepository cocktailIngredientRepository;

    @InjectMocks private SyncService syncService;

    private CocktailApiItemDTO buildItem(String id, String nom) {
        try {
            var ctor = CocktailApiItemDTO.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            var item = ctor.newInstance();
            setField(item, "idDrink", id);
            setField(item, "strDrink", nom);
            setField(item, "strCategory", "Cocktail");
            setField(item, "strDrinkThumb", "https://img/" + id);
            setField(item, "strInstructionsFR", "Mélanger.");
            setField(item, "strIngredient1", "Vodka");
            setField(item, "strMeasure1", "5 cl");
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object obj, String name, String value) throws Exception {
        var f = CocktailApiItemDTO.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, value);
    }

    @Test
    void syncCocktails_retourneLeTotalImporteeSurToutLalphabet() {
        CocktailApiItemDTO item = buildItem("1", "Mojito");
        when(cocktailApiClient.rechercherParLettre(anyChar()))
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(item));

        Cocktail savedCocktail = new Cocktail();
        savedCocktail.setId(10L);
        when(cocktailRepository.findByApiId("1")).thenReturn(Optional.empty());
        when(cocktailRepository.save(any())).thenReturn(savedCocktail);

        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(1L);
        when(ingredientRepository.findByNomIgnoreCase("Vodka")).thenReturn(Optional.empty());
        when(ingredientRepository.save(any())).thenReturn(savedIngredient);

        when(cocktailIngredientRepository.existsByCocktailAndIngredient(any(), any())).thenReturn(false);
        when(cocktailIngredientRepository.save(any())).thenReturn(new CocktailIngredient());

        int result = syncService.syncCocktails();
        assertThat(result).isEqualTo(1);
    }

    @Test
    void syncCocktails_sauteLesLettresSansResultat() {
        when(cocktailApiClient.rechercherParLettre(anyChar())).thenReturn(Collections.emptyList());
        int result = syncService.syncCocktails();
        assertThat(result).isZero();
        verify(cocktailRepository, never()).save(any());
    }

    @Test
    void syncCocktails_miseAJourCocktailExistant() {
        CocktailApiItemDTO item = buildItem("42", "Cosmopolitan");
        when(cocktailApiClient.rechercherParLettre(anyChar()))
                .thenReturn(List.of(item))
                .thenReturn(Collections.emptyList());

        Cocktail existant = new Cocktail();
        existant.setId(5L);
        existant.setApiId("42");
        existant.setActif(true);
        when(cocktailRepository.findByApiId("42")).thenReturn(Optional.of(existant));
        when(cocktailRepository.save(any())).thenReturn(existant);

        Ingredient ing = new Ingredient();
        ing.setId(2L);
        when(ingredientRepository.findByNomIgnoreCase("Vodka")).thenReturn(Optional.of(ing));
        when(cocktailIngredientRepository.existsByCocktailAndIngredient(any(), any())).thenReturn(true);

        syncService.syncCocktails();

        verify(cocktailRepository, atLeastOnce()).save(any());
        verify(cocktailIngredientRepository, never()).save(any());
    }

    @Test
    void syncCocktails_ingredientExistantReutilise() {
        CocktailApiItemDTO item = buildItem("7", "Gimlet");
        when(cocktailApiClient.rechercherParLettre(anyChar()))
                .thenReturn(List.of(item))
                .thenReturn(Collections.emptyList());

        Cocktail cocktail = new Cocktail(); cocktail.setId(7L);
        when(cocktailRepository.findByApiId("7")).thenReturn(Optional.empty());
        when(cocktailRepository.save(any())).thenReturn(cocktail);

        Ingredient existant = new Ingredient(); existant.setId(3L);
        when(ingredientRepository.findByNomIgnoreCase("Vodka")).thenReturn(Optional.of(existant));
        when(cocktailIngredientRepository.existsByCocktailAndIngredient(any(), any())).thenReturn(false);
        when(cocktailIngredientRepository.save(any())).thenReturn(new CocktailIngredient());

        syncService.syncCocktails();

        verify(ingredientRepository, never()).save(any());
        verify(cocktailIngredientRepository).save(any());
    }
}
