package com.barathym.controllers;

import com.barathym.dtos.LigneCommandeResponseDTO;
import com.barathym.services.LigneCommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/lignes-commande", produces = "application/json")
@RequiredArgsConstructor
public class LigneCommandeController {

    private final LigneCommandeService ligneCommandeService;

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<LigneCommandeResponseDTO>> getLignesByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(ligneCommandeService.findByCommande(commandeId));
    }

    @PatchMapping("/{id}/avancer")
    public ResponseEntity<String> avancerStatut(@PathVariable Long id) {
        ligneCommandeService.avancerStatut(id);
        return ResponseEntity.ok("{\"message\": \"Étape avancée avec succès\"}");
    }
}
