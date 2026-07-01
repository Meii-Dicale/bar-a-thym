package com.barathym.services;

import com.barathym.dtos.cocktailapi.CocktailApiItemDTO;
import com.barathym.dtos.cocktailapi.CocktailApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailApiClient {

    private final RestClient restClient;

    @Value("${cocktaildb.base-url}")
    private String baseUrl;

    public List<CocktailApiItemDTO> rechercherParLettre(char lettre) {
        String url = baseUrl + "/search.php?f=" + lettre;
        CocktailApiResponseDTO response = restClient.get()
                .uri(url)
                .retrieve()
                .body(CocktailApiResponseDTO.class);
        if (response == null || response.drinks() == null) {
            return Collections.emptyList();
        }
        return response.drinks();
    }
}
