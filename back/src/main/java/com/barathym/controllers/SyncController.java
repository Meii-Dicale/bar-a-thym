package com.barathym.controllers;

import com.barathym.services.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/sync", produces = "application/json")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping("/cocktails")
    public ResponseEntity<Map<String, Object>> syncCocktails() {
        int total = syncService.syncCocktails();
        return ResponseEntity.ok(Map.of(
            "message", "Synchronisation terminée",
            "cocktailsImportes", total
        ));
    }
}
