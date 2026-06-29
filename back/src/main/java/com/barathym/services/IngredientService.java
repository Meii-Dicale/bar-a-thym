package com.barathym.services;

import com.barathym.dtos.IngredientRequestDTO;
import com.barathym.dtos.IngredientResponseDTO;
import com.barathym.entites.Ingredient;
import com.barathym.mappers.IngredientMapper;
import com.barathym.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public List<IngredientResponseDTO> findAll() {
        return ingredientMapper.toDTO(ingredientRepository.findAll());
    }

    public IngredientResponseDTO find(Long id) {
        Ingredient ingredient = null;
        if (ingredientRepository.findById(id).isPresent()) {
            ingredient = ingredientRepository.findById(id).get();
        }
        return ingredientMapper.toDTO(ingredient);
    }

    public void save(IngredientRequestDTO dto) {
        ingredientRepository.save(ingredientMapper.toEntity(dto));
    }

    public void update(Long id, IngredientRequestDTO dto) {
        Ingredient ingredient = ingredientMapper.toEntity(dto);
        ingredient.setId(id);
        ingredientRepository.save(ingredient);
    }

    public void toggleDisponible(Long id) {
        if (ingredientRepository.findById(id).isPresent()) {
            Ingredient ingredient = ingredientRepository.findById(id).get();
            ingredient.setDisponible(!ingredient.getDisponible());
            ingredientRepository.save(ingredient);
        }
    }

    public void remove(Long id) {
        ingredientRepository.deleteById(id);
    }
}
