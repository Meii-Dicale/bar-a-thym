package com.barathym.controllers;

import com.barathym.dtos.IngredientRequestDTO;
import com.barathym.dtos.IngredientResponseDTO;
import com.barathym.services.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/ingredients", produces = "application/json")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<IngredientResponseDTO>> getIngredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> getIngredient(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.find(id));
    }

    @PostMapping
    public ResponseEntity<String> addIngredient(@Valid @RequestBody IngredientRequestDTO dto) {
        ingredientService.save(dto);
        return ResponseEntity.ok("{\"message\": \"Ingrédient créé avec succès\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientRequestDTO dto) {
        ingredientService.update(id, dto);
        return ResponseEntity.ok("{\"message\": \"Ingrédient mis à jour avec succès\"}");
    }

    @PatchMapping("/{id}/disponible")
    public ResponseEntity<String> toggleDisponible(@PathVariable Long id) {
        ingredientService.toggleDisponible(id);
        return ResponseEntity.ok("{\"message\": \"Disponibilité mise à jour\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long id) {
        ingredientService.remove(id);
        return ResponseEntity.ok("{\"message\": \"Ingrédient supprimé avec succès\"}");
    }
}
