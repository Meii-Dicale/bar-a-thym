package com.barathym.controllers;

import com.barathym.dtos.TaillePrixRequestDTO;
import com.barathym.dtos.TaillePrixResponseDTO;
import com.barathym.services.TaillePrixService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/tailles-prix", produces = "application/json")
@RequiredArgsConstructor
public class TaillePrixController {

    private final TaillePrixService taillePrixService;

    @GetMapping("/cocktail/{cocktailId}")
    public ResponseEntity<List<TaillePrixResponseDTO>> getTaillesPrixByCocktail(@PathVariable Long cocktailId) {
        return ResponseEntity.ok(taillePrixService.findByCocktail(cocktailId));
    }

    @PostMapping
    public ResponseEntity<String> addTaillePrix(@Valid @RequestBody TaillePrixRequestDTO dto) {
        taillePrixService.save(dto);
        return ResponseEntity.ok("{\"message\": \"Taille/prix créé avec succès\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTaillePrix(@PathVariable Long id, @Valid @RequestBody TaillePrixRequestDTO dto) {
        taillePrixService.update(id, dto);
        return ResponseEntity.ok("{\"message\": \"Taille/prix mis à jour avec succès\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaillePrix(@PathVariable Long id) {
        taillePrixService.remove(id);
        return ResponseEntity.ok("{\"message\": \"Taille/prix supprimé avec succès\"}");
    }
}
