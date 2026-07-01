package com.barathym.controllers;

import com.barathym.dtos.LoginRequestDTO;
import com.barathym.dtos.LoginResponseDTO;
import com.barathym.mappers.UtilisateurMapper;
import com.barathym.repositories.UtilisateurRepository;
import com.barathym.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return utilisateurRepository.findByEmail(dto.email())
                .filter(u -> passwordEncoder.matches(dto.motDePasse(), u.getMotDePasse()))
                .map(u -> {
                    String token = jwtService.generer(u.getEmail(), u.getRole().name());
                    return ResponseEntity.ok(new LoginResponseDTO(token, utilisateurMapper.toDTO(u)));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
