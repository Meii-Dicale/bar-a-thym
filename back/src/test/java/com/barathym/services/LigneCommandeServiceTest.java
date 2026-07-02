package com.barathym.services;

import com.barathym.dtos.LigneCommandeResponseDTO;
import com.barathym.entites.Commande;
import com.barathym.entites.LigneCommande;
import com.barathym.exceptions.ResourceNotFoundException;
import com.barathym.mappers.LigneCommandeMapper;
import com.barathym.repositories.LigneCommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LigneCommandeServiceTest {

    @Mock private LigneCommandeRepository ligneCommandeRepository;
    @Mock private LigneCommandeMapper ligneCommandeMapper;
    @Mock private CommandeService commandeService;

    @InjectMocks
    private LigneCommandeService ligneCommandeService;

    private LigneCommande ligne;
    private LigneCommandeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        Commande commande = new Commande();
        commande.setId(1L);

        ligne = new LigneCommande();
        ligne.setId(1L);
        ligne.setCommande(commande);
        ligne.setStatut(LigneCommande.Statut.PREPARATION);

        responseDTO = new LigneCommandeResponseDTO(1L, 1L, 1L, "Mojito", 1L, null, null, null, LigneCommande.Statut.PREPARATION);
    }

    @Test
    void findByCommande_shouldReturnLignes() {
        when(ligneCommandeRepository.findByCommandeId(1L)).thenReturn(List.of(ligne));
        when(ligneCommandeMapper.toDTO(List.of(ligne))).thenReturn(List.of(responseDTO));

        List<LigneCommandeResponseDTO> result = ligneCommandeService.findByCommande(1L);

        assertEquals(1, result.size());
        assertEquals(LigneCommande.Statut.PREPARATION, result.get(0).statut());
    }

    @Test
    void avancerStatut_fromPreparation_shouldSetAssemblage() {
        ligne.setStatut(LigneCommande.Statut.PREPARATION);
        when(ligneCommandeRepository.findById(1L)).thenReturn(Optional.of(ligne));

        ligneCommandeService.avancerStatut(1L);

        assertEquals(LigneCommande.Statut.ASSEMBLAGE, ligne.getStatut());
        verify(ligneCommandeRepository).save(ligne);
    }

    @Test
    void avancerStatut_fromAssemblage_shouldSetDressage() {
        ligne.setStatut(LigneCommande.Statut.ASSEMBLAGE);
        when(ligneCommandeRepository.findById(1L)).thenReturn(Optional.of(ligne));

        ligneCommandeService.avancerStatut(1L);

        assertEquals(LigneCommande.Statut.DRESSAGE, ligne.getStatut());
    }

    @Test
    void avancerStatut_fromDressage_shouldSetTermineeAndVerifierCommande() {
        ligne.setStatut(LigneCommande.Statut.DRESSAGE);
        when(ligneCommandeRepository.findById(1L)).thenReturn(Optional.of(ligne));

        ligneCommandeService.avancerStatut(1L);

        assertEquals(LigneCommande.Statut.TERMINEE, ligne.getStatut());
        verify(commandeService).verifierEtTerminer(1L);
    }

    @Test
    void avancerStatut_fromTerminee_shouldNotChange() {
        ligne.setStatut(LigneCommande.Statut.TERMINEE);
        when(ligneCommandeRepository.findById(1L)).thenReturn(Optional.of(ligne));

        ligneCommandeService.avancerStatut(1L);

        assertEquals(LigneCommande.Statut.TERMINEE, ligne.getStatut());
        verify(ligneCommandeRepository, never()).save(any());
    }

    @Test
    void avancerStatut_whenNotFound_shouldThrow() {
        when(ligneCommandeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ligneCommandeService.avancerStatut(99L));
        verify(ligneCommandeRepository, never()).save(any());
    }
}
