package com.barathym.services;

import com.barathym.dtos.UtilisateurRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import com.barathym.mappers.UtilisateurMapper;
import com.barathym.repositories.UtilisateurRepository;
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
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur;
    private UtilisateurResponseDTO responseDTO;
    private UtilisateurRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNom("Dupont");
        utilisateur.setPrenom("Marie");
        utilisateur.setEmail("marie@barathym.fr");
        utilisateur.setRole(Utilisateur.Role.CLIENT);

        responseDTO = new UtilisateurResponseDTO(1L, "Dupont", "Marie", "marie@barathym.fr", Utilisateur.Role.CLIENT);
        requestDTO = new UtilisateurRequestDTO("Dupont", "Marie", "marie@barathym.fr", "password", Utilisateur.Role.CLIENT);
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        when(utilisateurRepository.findAll()).thenReturn(List.of(utilisateur));
        when(utilisateurMapper.toDTO(List.of(utilisateur))).thenReturn(List.of(responseDTO));

        List<UtilisateurResponseDTO> result = utilisateurService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).nom());
        verify(utilisateurRepository).findAll();
    }

    @Test
    void find_whenExists_shouldReturnDTO() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(responseDTO);

        UtilisateurResponseDTO result = utilisateurService.find(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("marie@barathym.fr", result.email());
    }

    @Test
    void find_whenNotExists_shouldReturnNull() {
        when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());
        when(utilisateurMapper.toDTO((Utilisateur) null)).thenReturn(null);

        UtilisateurResponseDTO result = utilisateurService.find(99L);

        assertNull(result);
    }

    @Test
    void save_shouldMapAndPersist() {
        when(utilisateurMapper.toEntity(requestDTO)).thenReturn(utilisateur);

        utilisateurService.save(requestDTO);

        verify(utilisateurMapper).toEntity(requestDTO);
        verify(utilisateurRepository).save(utilisateur);
    }

    @Test
    void update_shouldSetIdAndSave() {
        when(utilisateurMapper.toEntity(requestDTO)).thenReturn(utilisateur);

        utilisateurService.update(2L, requestDTO);

        assertEquals(2L, utilisateur.getId());
        verify(utilisateurRepository).save(utilisateur);
    }

    @Test
    void remove_shouldCallDeleteById() {
        utilisateurService.remove(1L);

        verify(utilisateurRepository).deleteById(1L);
    }
}
