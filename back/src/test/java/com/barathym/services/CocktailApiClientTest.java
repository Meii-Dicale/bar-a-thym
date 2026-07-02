package com.barathym.services;

import com.barathym.dtos.cocktailapi.CocktailApiItemDTO;
import com.barathym.dtos.cocktailapi.CocktailApiResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CocktailApiClientTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private CocktailApiClient client;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(client, "baseUrl", "http://api.test");
    }

    private void stubChain(CocktailApiResponseDTO response) {
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri(anyString());
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(response).when(responseSpec).body(eq(CocktailApiResponseDTO.class));
    }

    @Test
    void rechercherParLettre_whenDrinksPresent_shouldReturnList() {
        stubChain(new CocktailApiResponseDTO(Collections.singletonList((CocktailApiItemDTO) null)));

        List<CocktailApiItemDTO> result = client.rechercherParLettre('a');

        assertThat(result).hasSize(1);
    }

    @Test
    void rechercherParLettre_whenResponseNull_shouldReturnEmpty() {
        stubChain(null);

        assertThat(client.rechercherParLettre('b')).isEmpty();
    }

    @Test
    void rechercherParLettre_whenDrinksNull_shouldReturnEmpty() {
        stubChain(new CocktailApiResponseDTO(null));

        assertThat(client.rechercherParLettre('c')).isEmpty();
    }
}
