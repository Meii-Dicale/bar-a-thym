package com.barathym.controllers;

import com.barathym.dtos.LigneCommandeResponseDTO;
import com.barathym.entites.LigneCommande;
import com.barathym.services.LigneCommandeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LigneCommandeControllerTest {

    @Mock
    private LigneCommandeService ligneCommandeService;

    @InjectMocks
    private LigneCommandeController controller;

    private final LigneCommandeResponseDTO dto =
            new LigneCommandeResponseDTO(1L, 1L, 1L, "Mojito", 1L, null, null, null, LigneCommande.Statut.PREPARATION);

    @Test
    void getLignesByCommande_shouldReturnList() {
        when(ligneCommandeService.findByCommande(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<LigneCommandeResponseDTO>> response = controller.getLignesByCommande(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void avancerStatut_shouldDelegateToService() {
        ResponseEntity<String> response = controller.avancerStatut(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ligneCommandeService).avancerStatut(1L);
    }
}
