package com.barathym.controllers;

import com.barathym.dtos.LoginRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import com.barathym.mappers.UtilisateurMapper;
import com.barathym.repositories.UtilisateurRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    @PostMapping("/login")
    public ResponseEntity<UtilisateurResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return utilisateurRepository.findByEmailAndMotDePasse(dto.email(), dto.motDePasse())
                .map(u -> ResponseEntity.ok(utilisateurMapper.toResponse(u)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
