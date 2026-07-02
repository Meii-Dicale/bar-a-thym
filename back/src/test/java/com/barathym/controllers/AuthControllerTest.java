package com.barathym.controllers;

import com.barathym.dtos.LoginRequestDTO;
import com.barathym.dtos.LoginResponseDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import com.barathym.mappers.UtilisateurMapper;
import com.barathym.repositories.UtilisateurRepository;
import com.barathym.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController controller;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("marie@barathym.fr");
        utilisateur.setMotDePasse("hash");
        utilisateur.setRole(Utilisateur.Role.CLIENT);
    }

    @Test
    void login_whenValidCredentials_shouldReturnToken() {
        LoginRequestDTO dto = new LoginRequestDTO("marie@barathym.fr", "password");
        when(utilisateurRepository.findByEmail("marie@barathym.fr")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("password", "hash")).thenReturn(true);
        when(jwtService.generer("marie@barathym.fr", "CLIENT")).thenReturn("token-123");
        when(utilisateurMapper.toDTO(utilisateur))
                .thenReturn(new UtilisateurResponseDTO(1L, "Dupont", "Marie", "marie@barathym.fr", Utilisateur.Role.CLIENT));

        ResponseEntity<LoginResponseDTO> response = controller.login(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().token()).isEqualTo("token-123");
        assertThat(response.getBody().utilisateur().email()).isEqualTo("marie@barathym.fr");
    }

    @Test
    void login_whenWrongPassword_shouldReturnUnauthorized() {
        LoginRequestDTO dto = new LoginRequestDTO("marie@barathym.fr", "mauvais");
        when(utilisateurRepository.findByEmail("marie@barathym.fr")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais", "hash")).thenReturn(false);

        ResponseEntity<LoginResponseDTO> response = controller.login(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_whenUnknownEmail_shouldReturnUnauthorized() {
        LoginRequestDTO dto = new LoginRequestDTO("inconnu@barathym.fr", "password");
        when(utilisateurRepository.findByEmail("inconnu@barathym.fr")).thenReturn(Optional.empty());

        ResponseEntity<LoginResponseDTO> response = controller.login(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
