package com.barathym.controllers;

import com.barathym.dtos.CommandeRequestDTO;
import com.barathym.dtos.CommandeResponseDTO;
import com.barathym.entites.Commande;
import com.barathym.services.CommandeService;
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
class CommandeControllerTest {

    @Mock
    private CommandeService commandeService;

    @InjectMocks
    private CommandeController controller;

    private final CommandeResponseDTO dto =
            new CommandeResponseDTO(1L, 2L, null, Commande.Statut.COMMANDEE, null, null, List.of());
    private final CommandeRequestDTO req =
            new CommandeRequestDTO(2L, List.of());

    @Test
    void getCommandes_shouldReturnList() {
        when(commandeService.findAll()).thenReturn(List.of(dto));

        ResponseEntity<List<CommandeResponseDTO>> response = controller.getCommandes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getCommandesEnAttente_shouldReturnList() {
        when(commandeService.findEnAttente()).thenReturn(List.of(dto));

        assertThat(controller.getCommandesEnAttente().getBody()).hasSize(1);
    }

    @Test
    void getCommandesByClient_shouldReturnList() {
        when(commandeService.findByClient(2L)).thenReturn(List.of(dto));

        assertThat(controller.getCommandesByClient(2L).getBody()).hasSize(1);
    }

    @Test
    void getCommande_shouldReturnOne() {
        when(commandeService.find(1L)).thenReturn(dto);

        assertThat(controller.getCommande(1L).getBody().id()).isEqualTo(1L);
    }

    @Test
    void addCommande_shouldDelegateToService() {
        ResponseEntity<String> response = controller.addCommande(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commandeService).save(req);
    }

    @Test
    void prendreEnCharge_shouldDelegateToService() {
        ResponseEntity<String> response = controller.prendreEnCharge(1L, 9L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commandeService).prendreEnCharge(1L, 9L);
    }

    @Test
    void annulerCommande_shouldDelegateToService() {
        ResponseEntity<String> response = controller.annulerCommande(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commandeService).annuler(1L);
    }

    @Test
    void deleteCommande_shouldDelegateToService() {
        ResponseEntity<String> response = controller.deleteCommande(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commandeService).remove(1L);
    }
}
