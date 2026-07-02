package com.barathym.controllers;

import com.barathym.dtos.DefinirPrixDTO;
import com.barathym.dtos.TaillePrixRequestDTO;
import com.barathym.dtos.TaillePrixResponseDTO;
import com.barathym.entites.TaillePrix;
import com.barathym.services.TaillePrixService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaillePrixControllerTest {

    @Mock
    private TaillePrixService taillePrixService;

    @InjectMocks
    private TaillePrixController controller;

    private final TaillePrixResponseDTO dto =
            new TaillePrixResponseDTO(1L, 1L, TaillePrix.Taille.S, new BigDecimal("6.50"));
    private final TaillePrixRequestDTO req =
            new TaillePrixRequestDTO(1L, TaillePrix.Taille.S, new BigDecimal("6.50"));
    private final DefinirPrixDTO prix =
            new DefinirPrixDTO(new BigDecimal("6.50"), new BigDecimal("8.00"), new BigDecimal("9.50"));

    @Test
    void getTaillesPrixByCocktail_shouldReturnList() {
        when(taillePrixService.findByCocktail(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<TaillePrixResponseDTO>> response = controller.getTaillesPrixByCocktail(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void definirPrix_shouldDelegateToService() {
        ResponseEntity<String> response = controller.definirPrix(1L, prix);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(taillePrixService).definirPrix(1L, prix);
    }

    @Test
    void addTaillePrix_shouldDelegateToService() {
        ResponseEntity<String> response = controller.addTaillePrix(req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(taillePrixService).save(req);
    }

    @Test
    void updateTaillePrix_shouldDelegateToService() {
        ResponseEntity<String> response = controller.updateTaillePrix(5L, req);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(taillePrixService).update(5L, req);
    }

    @Test
    void deleteTaillePrix_shouldDelegateToService() {
        ResponseEntity<String> response = controller.deleteTaillePrix(7L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(taillePrixService).remove(7L);
    }
}
