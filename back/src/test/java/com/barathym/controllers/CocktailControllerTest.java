package com.barathym.controllers;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.services.CocktailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CocktailControllerTest {

    @Mock
    private CocktailService cocktailService;

    @InjectMocks
    private CocktailController controller;

    private final CocktailResponseDTO dto =
            new CocktailResponseDTO(1L, "co_001", "Mojito", null, "Cocktail", true, null, true, List.of(), false);
    private final CocktailRequestDTO req =
            new CocktailRequestDTO("co_001", "Mojito", null, "Cocktail", true, null);

    @Test
    void getCocktails_shouldReturnList() {
        when(cocktailService.findAll()).thenReturn(List.of(dto));

        ResponseEntity<List<CocktailResponseDTO>> response = controller.getCocktails();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getCocktailsActifs_shouldReturnList() {
        when(cocktailService.findActifs()).thenReturn(List.of(dto));

        assertThat(controller.getCocktailsActifs().getBody()).hasSize(1);
    }

    @Test
    void getCocktailsDisponibles_shouldReturnList() {
        when(cocktailService.findDisponibles()).thenReturn(List.of(dto));

        assertThat(controller.getCocktailsDisponibles().getBody()).hasSize(1);
    }

    @Test
    void getCocktailsCarte_shouldReturnList() {
        when(cocktailService.findCarte()).thenReturn(List.of(dto));

        assertThat(controller.getCocktailsCarte().getBody()).hasSize(1);
    }

    @Test
    void getCocktail_shouldReturnOne() {
        when(cocktailService.find(1L)).thenReturn(dto);

        ResponseEntity<CocktailResponseDTO> response = controller.getCocktail(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nom()).isEqualTo("Mojito");
    }

    @Test
    void addCocktail_shouldDelegateToService() {
        ResponseEntity<String> response = controller.addCocktail(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cocktailService).save(req);
    }

    @Test
    void updateCocktail_shouldDelegateToService() {
        ResponseEntity<String> response = controller.updateCocktail(5L, req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cocktailService).update(5L, req);
    }

    @Test
    void toggleActif_shouldDelegateToService() {
        ResponseEntity<String> response = controller.toggleActif(3L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cocktailService).toggleActif(3L);
    }

    @Test
    void deleteCocktail_shouldDelegateToService() {
        ResponseEntity<String> response = controller.deleteCocktail(7L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cocktailService).remove(7L);
    }
}
