package com.barathym.services;

import com.barathym.dtos.CommandeRequestDTO;
import com.barathym.dtos.CommandeResponseDTO;
import com.barathym.dtos.LigneCommandeRequestDTO;
import com.barathym.entites.*;
import com.barathym.exceptions.ResourceNotFoundException;
import com.barathym.mappers.CommandeMapper;
import com.barathym.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock private CommandeRepository commandeRepository;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private CocktailRepository cocktailRepository;
    @Mock private TaillePrixRepository taillePrixRepository;
    @Mock private LigneCommandeRepository ligneCommandeRepository;
    @Mock private CommandeMapper commandeMapper;

    @InjectMocks
    private CommandeService commandeService;

    private Utilisateur client;
    private Utilisateur barmaker;
    private Cocktail cocktail;
    private TaillePrix taillePrix;
    private Commande commande;
    private CommandeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        client = new Utilisateur();
        client.setId(1L);
        client.setRole(Utilisateur.Role.CLIENT);

        barmaker = new Utilisateur();
        barmaker.setId(2L);
        barmaker.setRole(Utilisateur.Role.BARMAKER);

        cocktail = new Cocktail();
        cocktail.setId(1L);
        cocktail.setNom("Mojito");

        taillePrix = new TaillePrix();
        taillePrix.setId(1L);
        taillePrix.setPrix(new BigDecimal("7.00"));

        commande = new Commande();
        commande.setId(1L);
        commande.setClient(client);
        commande.setStatut(Commande.Statut.COMMANDEE);
        commande.setTotal(new BigDecimal("7.00"));
        commande.setCreeLe(LocalDateTime.now());
        commande.setLignesCommande(new ArrayList<>());

        responseDTO = new CommandeResponseDTO(1L, 1L, null, Commande.Statut.COMMANDEE, new BigDecimal("7.00"), LocalDateTime.now(), List.of());
    }

    @Test
    void findAll_shouldReturnAllCommandes() {
        when(commandeRepository.findAll()).thenReturn(List.of(commande));
        when(commandeMapper.toDTO(List.of(commande))).thenReturn(List.of(responseDTO));

        List<CommandeResponseDTO> result = commandeService.findAll();

        assertEquals(1, result.size());
        assertEquals(Commande.Statut.COMMANDEE, result.get(0).statut());
    }

    @Test
    void findByClient_shouldFilterByClientId() {
        when(commandeRepository.findByClientId(1L)).thenReturn(List.of(commande));
        when(commandeMapper.toDTO(List.of(commande))).thenReturn(List.of(responseDTO));

        List<CommandeResponseDTO> result = commandeService.findByClient(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findEnAttente_shouldReturnCommandees() {
        when(commandeRepository.findByStatut(Commande.Statut.COMMANDEE)).thenReturn(List.of(commande));
        when(commandeMapper.toDTO(List.of(commande))).thenReturn(List.of(responseDTO));

        List<CommandeResponseDTO> result = commandeService.findEnAttente();

        assertEquals(1, result.size());
    }

    @Test
    void find_whenExists_shouldReturnDTO() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeMapper.toDTO(commande)).thenReturn(responseDTO);

        CommandeResponseDTO result = commandeService.find(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void find_whenNotExists_shouldThrow() {
        when(commandeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commandeService.find(99L));
    }

    @Test
    void save_shouldCreateCommandeWithLignes() {
        LigneCommandeRequestDTO ligneDto = new LigneCommandeRequestDTO(1L, 1L, "sans sucre");
        CommandeRequestDTO dto = new CommandeRequestDTO(1L, List.of(ligneDto));

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(client));
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
        when(taillePrixRepository.findById(1L)).thenReturn(Optional.of(taillePrix));

        commandeService.save(dto);

        verify(commandeRepository).save(any(Commande.class));
    }

    @Test
    void save_whenClientNotFound_shouldStillSave() {
        LigneCommandeRequestDTO ligneDto = new LigneCommandeRequestDTO(1L, 1L, null);
        CommandeRequestDTO dto = new CommandeRequestDTO(99L, List.of(ligneDto));

        when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());
        when(cocktailRepository.findById(1L)).thenReturn(Optional.empty());
        when(taillePrixRepository.findById(1L)).thenReturn(Optional.empty());

        commandeService.save(dto);

        verify(commandeRepository).save(any(Commande.class));
    }

    @Test
    void prendreEnCharge_shouldSetStatutAndBarmaker() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(barmaker));

        commandeService.prendreEnCharge(1L, 2L);

        assertEquals(Commande.Statut.EN_COURS, commande.getStatut());
        assertEquals(barmaker, commande.getBarmaker());
        verify(commandeRepository).save(commande);
    }

    @Test
    void prendreEnCharge_whenNotFound_shouldThrow() {
        when(commandeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commandeService.prendreEnCharge(99L, 2L));
        verify(commandeRepository, never()).save(any());
    }

    @Test
    void verifierEtTerminer_whenAllTerminees_shouldSetCommandeTerminee() {
        LigneCommande ligne = new LigneCommande();
        ligne.setStatut(LigneCommande.Statut.TERMINEE);
        commande.setLignesCommande(List.of(ligne));
        commande.setStatut(Commande.Statut.EN_COURS);

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        commandeService.verifierEtTerminer(1L);

        assertEquals(Commande.Statut.TERMINEE, commande.getStatut());
        verify(commandeRepository).save(commande);
    }

    @Test
    void verifierEtTerminer_whenNotAllTerminees_shouldNotChange() {
        LigneCommande ligne1 = new LigneCommande();
        ligne1.setStatut(LigneCommande.Statut.TERMINEE);
        LigneCommande ligne2 = new LigneCommande();
        ligne2.setStatut(LigneCommande.Statut.ASSEMBLAGE);
        commande.setLignesCommande(List.of(ligne1, ligne2));

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        commandeService.verifierEtTerminer(1L);

        assertEquals(Commande.Statut.COMMANDEE, commande.getStatut());
        verify(commandeRepository, never()).save(any());
    }

    @Test
    void remove_shouldCallDeleteById() {
        commandeService.remove(1L);

        verify(commandeRepository).deleteById(1L);
    }
}
