package com.barathym.services;

import com.barathym.dtos.IngredientRequestDTO;
import com.barathym.dtos.IngredientResponseDTO;
import com.barathym.entites.Ingredient;
import com.barathym.exceptions.ResourceNotFoundException;
import com.barathym.mappers.IngredientMapper;
import com.barathym.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient ingredient;
    private IngredientResponseDTO responseDTO;
    private IngredientRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setApiId("ing_001");
        ingredient.setNom("Rhum blanc");
        ingredient.setDisponible(false);

        responseDTO = new IngredientResponseDTO(1L, "ing_001", "Rhum blanc", null, false);
        requestDTO = new IngredientRequestDTO("ing_001", "Rhum blanc", null, false);
    }

    @Test
    void findAll_shouldReturnAllIngredients() {
        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));
        when(ingredientMapper.toDTO(List.of(ingredient))).thenReturn(List.of(responseDTO));

        List<IngredientResponseDTO> result = ingredientService.findAll();

        assertEquals(1, result.size());
        assertEquals("Rhum blanc", result.get(0).nom());
    }

    @Test
    void find_whenExists_shouldReturnDTO() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDTO(ingredient)).thenReturn(responseDTO);

        IngredientResponseDTO result = ingredientService.find(1L);

        assertNotNull(result);
        assertEquals("ing_001", result.apiId());
    }

    @Test
    void find_whenNotExists_shouldThrow() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ingredientService.find(99L));
    }

    @Test
    void save_shouldPersistIngredient() {
        when(ingredientMapper.toEntity(requestDTO)).thenReturn(ingredient);

        ingredientService.save(requestDTO);

        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void update_shouldSetIdAndSave() {
        when(ingredientMapper.toEntity(requestDTO)).thenReturn(ingredient);

        ingredientService.update(5L, requestDTO);

        assertEquals(5L, ingredient.getId());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void toggleDisponible_whenExists_shouldInvertValue() {
        ingredient.setDisponible(false);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        ingredientService.toggleDisponible(1L);

        assertTrue(ingredient.getDisponible());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void toggleDisponible_whenTrue_shouldSetFalse() {
        ingredient.setDisponible(true);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        ingredientService.toggleDisponible(1L);

        assertFalse(ingredient.getDisponible());
    }

    @Test
    void toggleDisponible_whenNotExists_shouldThrow() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ingredientService.toggleDisponible(99L));
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void remove_shouldCallDeleteById() {
        ingredientService.remove(1L);

        verify(ingredientRepository).deleteById(1L);
    }
}
