package com.barathym.controllers;

import com.barathym.dtos.UtilisateurRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import com.barathym.services.UtilisateurService;
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
class UtilisateurControllerTest {

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private UtilisateurController controller;

    private final UtilisateurResponseDTO dto =
            new UtilisateurResponseDTO(1L, "Dupont", "Marie", "marie@barathym.fr", Utilisateur.Role.CLIENT);
    private final UtilisateurRequestDTO req =
            new UtilisateurRequestDTO("Dupont", "Marie", "marie@barathym.fr", "password", Utilisateur.Role.CLIENT);

    @Test
    void getUtilisateurs_shouldReturnList() {
        when(utilisateurService.findAll()).thenReturn(List.of(dto));

        ResponseEntity<List<UtilisateurResponseDTO>> response = controller.getUtilisateurs();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getUtilisateur_shouldReturnOne() {
        when(utilisateurService.find(1L)).thenReturn(dto);

        assertThat(controller.getUtilisateur(1L).getBody().email()).isEqualTo("marie@barathym.fr");
    }

    @Test
    void addUtilisateur_shouldDelegateToService() {
        ResponseEntity<String> response = controller.addUtilisateur(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(utilisateurService).save(req);
    }

    @Test
    void updateUtilisateur_shouldDelegateToService() {
        ResponseEntity<String> response = controller.updateUtilisateur(2L, req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(utilisateurService).update(2L, req);
    }

    @Test
    void deleteUtilisateur_shouldDelegateToService() {
        ResponseEntity<String> response = controller.deleteUtilisateur(3L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(utilisateurService).remove(3L);
    }
}
