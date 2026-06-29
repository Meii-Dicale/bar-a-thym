package com.barathym.controllers;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.services.CocktailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/cocktails", produces = "application/json")
@RequiredArgsConstructor
public class CocktailController {

    private final CocktailService cocktailService;

    @GetMapping
    public ResponseEntity<List<CocktailResponseDTO>> getCocktails() {
        return ResponseEntity.ok(cocktailService.findAll());
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<CocktailResponseDTO>> getCocktailsActifs() {
        return ResponseEntity.ok(cocktailService.findActifs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CocktailResponseDTO> getCocktail(@PathVariable Long id) {
        return ResponseEntity.ok(cocktailService.find(id));
    }

    @PostMapping
    public ResponseEntity<String> addCocktail(@Valid @RequestBody CocktailRequestDTO dto) {
        cocktailService.save(dto);
        return ResponseEntity.ok("{\"message\": \"Cocktail créé avec succès\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCocktail(@PathVariable Long id, @Valid @RequestBody CocktailRequestDTO dto) {
        cocktailService.update(id, dto);
        return ResponseEntity.ok("{\"message\": \"Cocktail mis à jour avec succès\"}");
    }

    @PatchMapping("/{id}/actif")
    public ResponseEntity<String> toggleActif(@PathVariable Long id) {
        cocktailService.toggleActif(id);
        return ResponseEntity.ok("{\"message\": \"Statut du cocktail mis à jour\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCocktail(@PathVariable Long id) {
        cocktailService.remove(id);
        return ResponseEntity.ok("{\"message\": \"Cocktail supprimé avec succès\"}");
    }
}
