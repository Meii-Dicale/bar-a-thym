package com.barathym.controllers;

import com.barathym.dtos.IngredientRequestDTO;
import com.barathym.dtos.IngredientResponseDTO;
import com.barathym.services.IngredientService;
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
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController controller;

    private final IngredientResponseDTO dto =
            new IngredientResponseDTO(1L, "in_001", "Menthe", null, true);
    private final IngredientRequestDTO req =
            new IngredientRequestDTO("in_001", "Menthe", null, true);

    @Test
    void getIngredients_shouldReturnList() {
        when(ingredientService.findAll()).thenReturn(List.of(dto));

        ResponseEntity<List<IngredientResponseDTO>> response = controller.getIngredients();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getIngredient_shouldReturnOne() {
        when(ingredientService.find(1L)).thenReturn(dto);

        assertThat(controller.getIngredient(1L).getBody().nom()).isEqualTo("Menthe");
    }

    @Test
    void addIngredient_shouldDelegateToService() {
        ResponseEntity<String> response = controller.addIngredient(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ingredientService).save(req);
    }

    @Test
    void updateIngredient_shouldDelegateToService() {
        ResponseEntity<String> response = controller.updateIngredient(4L, req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ingredientService).update(4L, req);
    }

    @Test
    void toggleDisponible_shouldDelegateToService() {
        ResponseEntity<String> response = controller.toggleDisponible(2L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ingredientService).toggleDisponible(2L);
    }

    @Test
    void deleteIngredient_shouldDelegateToService() {
        ResponseEntity<String> response = controller.deleteIngredient(6L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ingredientService).remove(6L);
    }
}
