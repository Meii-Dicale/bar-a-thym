package com.barathym.controllers;

import com.barathym.services.SyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncControllerTest {

    @Mock
    private SyncService syncService;

    @InjectMocks
    private SyncController controller;

    @Test
    void syncCocktails_shouldReturnTotal() {
        when(syncService.syncCocktails()).thenReturn(42);

        ResponseEntity<Map<String, Object>> response = controller.syncCocktails();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("cocktailsImportes", 42);
        assertThat(response.getBody()).containsEntry("message", "Synchronisation terminée");
    }
}
