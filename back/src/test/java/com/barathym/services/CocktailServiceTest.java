package com.barathym.services;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.mappers.CocktailMapper;
import com.barathym.repositories.CocktailRepository;
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
class CocktailServiceTest {

    @Mock
    private CocktailRepository cocktailRepository;

    @Mock
    private CocktailMapper cocktailMapper;

    @InjectMocks
    private CocktailService cocktailService;

    private Cocktail cocktail;
    private CocktailResponseDTO responseDTO;
    private CocktailRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cocktail = new Cocktail();
        cocktail.setId(1L);
        cocktail.setApiId("co_001");
        cocktail.setNom("Mojito");
        cocktail.setActif(true);

        responseDTO = new CocktailResponseDTO(1L, "co_001", "Mojito", null, "Cocktail", true, null);
        requestDTO = new CocktailRequestDTO("co_001", "Mojito", null, "Cocktail", true, null);
    }

    @Test
    void findAll_shouldReturnAllCocktails() {
        when(cocktailRepository.findAll()).thenReturn(List.of(cocktail));
        when(cocktailMapper.toDTO(List.of(cocktail))).thenReturn(List.of(responseDTO));

        List<CocktailResponseDTO> result = cocktailService.findAll();

        assertEquals(1, result.size());
        assertEquals("Mojito", result.get(0).nom());
    }

    @Test
    void findActifs_shouldReturnOnlyActifs() {
        when(cocktailRepository.findByActifTrue()).thenReturn(List.of(cocktail));
        when(cocktailMapper.toDTO(List.of(cocktail))).thenReturn(List.of(responseDTO));

        List<CocktailResponseDTO> result = cocktailService.findActifs();

        assertEquals(1, result.size());
        assertTrue(result.get(0).actif());
    }

    @Test
    void find_whenExists_shouldReturnDTO() {
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));
        when(cocktailMapper.toDTO(cocktail)).thenReturn(responseDTO);

        CocktailResponseDTO result = cocktailService.find(1L);

        assertNotNull(result);
        assertEquals("Mojito", result.nom());
    }

    @Test
    void find_whenNotExists_shouldReturnNull() {
        when(cocktailRepository.findById(99L)).thenReturn(Optional.empty());
        when(cocktailMapper.toDTO((Cocktail) null)).thenReturn(null);

        assertNull(cocktailService.find(99L));
    }

    @Test
    void save_shouldPersistCocktail() {
        when(cocktailMapper.toEntity(requestDTO)).thenReturn(cocktail);

        cocktailService.save(requestDTO);

        verify(cocktailRepository).save(cocktail);
    }

    @Test
    void update_shouldSetIdAndSave() {
        when(cocktailMapper.toEntity(requestDTO)).thenReturn(cocktail);

        cocktailService.update(3L, requestDTO);

        assertEquals(3L, cocktail.getId());
        verify(cocktailRepository).save(cocktail);
    }

    @Test
    void toggleActif_whenTrue_shouldSetFalse() {
        cocktail.setActif(true);
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));

        cocktailService.toggleActif(1L);

        assertFalse(cocktail.getActif());
        verify(cocktailRepository).save(cocktail);
    }

    @Test
    void toggleActif_whenFalse_shouldSetTrue() {
        cocktail.setActif(false);
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));

        cocktailService.toggleActif(1L);

        assertTrue(cocktail.getActif());
    }

    @Test
    void toggleActif_whenNotExists_shouldDoNothing() {
        when(cocktailRepository.findById(99L)).thenReturn(Optional.empty());

        cocktailService.toggleActif(99L);

        verify(cocktailRepository, never()).save(any());
    }

    @Test
    void remove_shouldCallDeleteById() {
        cocktailService.remove(1L);

        verify(cocktailRepository).deleteById(1L);
    }
}
