package com.barathym.controllers;

import com.barathym.dtos.UtilisateurRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.services.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/utilisateurs", produces = "application/json")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDTO>> getUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> getUtilisateur(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.find(id));
    }

    @PostMapping
    public ResponseEntity<String> addUtilisateur(@Valid @RequestBody UtilisateurRequestDTO dto) {
        utilisateurService.save(dto);
        return ResponseEntity.ok("{\"message\": \"Utilisateur créé avec succès\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUtilisateur(@PathVariable Long id, @Valid @RequestBody UtilisateurRequestDTO dto) {
        utilisateurService.update(id, dto);
        return ResponseEntity.ok("{\"message\": \"Utilisateur mis à jour avec succès\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.remove(id);
        return ResponseEntity.ok("{\"message\": \"Utilisateur supprimé avec succès\"}");
    }
}
