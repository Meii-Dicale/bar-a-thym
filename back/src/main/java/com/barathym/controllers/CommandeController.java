package com.barathym.controllers;

import com.barathym.dtos.CommandeRequestDTO;
import com.barathym.dtos.CommandeResponseDTO;
import com.barathym.services.CommandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/api/commandes", produces = "application/json")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public ResponseEntity<List<CommandeResponseDTO>> getCommandes() {
        return ResponseEntity.ok(commandeService.findAll());
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<CommandeResponseDTO>> getCommandesEnAttente() {
        return ResponseEntity.ok(commandeService.findEnAttente());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeResponseDTO>> getCommandesByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.findByClient(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.find(id));
    }

    @PostMapping
    public ResponseEntity<String> addCommande(@Valid @RequestBody CommandeRequestDTO dto) {
        commandeService.save(dto);
        return ResponseEntity.ok("{\"message\": \"Commande passée avec succès\"}");
    }

    @PatchMapping("/{commandeId}/prendre-en-charge/{barmakerId}")
    public ResponseEntity<String> prendreEnCharge(@PathVariable Long commandeId, @PathVariable Long barmakerId) {
        commandeService.prendreEnCharge(commandeId, barmakerId);
        return ResponseEntity.ok("{\"message\": \"Commande prise en charge\"}");
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<String> annulerCommande(@PathVariable Long id) {
        commandeService.annuler(id);
        return ResponseEntity.ok("{\"message\": \"Commande annulée\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommande(@PathVariable Long id) {
        commandeService.remove(id);
        return ResponseEntity.ok("{\"message\": \"Commande supprimée avec succès\"}");
    }
}
