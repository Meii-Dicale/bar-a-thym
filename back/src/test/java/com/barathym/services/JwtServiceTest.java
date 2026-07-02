package com.barathym.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Clé Base64 d'au moins 32 octets, requise par HMAC-SHA256
        String secret = Base64.getEncoder()
                .encodeToString("0123456789012345678901234567890123456789".getBytes());
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 3_600_000L);
    }

    @Test
    void generer_shouldProduceNonBlankToken() {
        String token = jwtService.generer("marie@barathym.fr", "CLIENT");

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void valider_whenTokenGeneratedByService_shouldReturnTrue() {
        String token = jwtService.generer("marie@barathym.fr", "BARMAKER");

        assertThat(jwtService.valider(token)).isTrue();
    }

    @Test
    void valider_whenTokenIsInvalid_shouldReturnFalse() {
        assertThat(jwtService.valider("ceci.nest.pas-un-token")).isFalse();
    }
}
