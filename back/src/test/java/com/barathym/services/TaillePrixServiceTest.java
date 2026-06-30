package com.barathym.services;

import com.barathym.dtos.TaillePrixRequestDTO;
import com.barathym.dtos.TaillePrixResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.entites.TaillePrix;
import com.barathym.mappers.TaillePrixMapper;
import com.barathym.repositories.CocktailRepository;
import com.barathym.repositories.TaillePrixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaillePrixServiceTest {

    @Mock private TaillePrixRepository taillePrixRepository;
    @Mock private CocktailRepository cocktailRepository;
    @Mock private TaillePrixMapper taillePrixMapper;

    @InjectMocks
    private TaillePrixService taillePrixService;

    private Cocktail cocktail;
    private TaillePrix taillePrix;
    private TaillePrixResponseDTO responseDTO;
    private TaillePrixRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cocktail = new Cocktail();
        cocktail.setId(1L);

        taillePrix = new TaillePrix();
        taillePrix.setId(1L);
        taillePrix.setCocktail(cocktail);
        taillePrix.setTaille(TaillePrix.Taille.M);
        taillePrix.setPrix(new BigDecimal("7.00"));

        responseDTO = new TaillePrixResponseDTO(1L, 1L, TaillePrix.Taille.M, new BigDecimal("7.00"));
        requestDTO = new TaillePrixRequestDTO(1L, TaillePrix.Taille.M, new BigDecimal("7.00"));
    }

    @Test
    void findByCocktail_shouldReturnTaillesPrix() {
        when(taillePrixRepository.findByCocktailId(1L)).thenReturn(List.of(taillePrix));
        when(taillePrixMapper.toDTO(List.of(taillePrix))).thenReturn(List.of(responseDTO));

        List<TaillePrixResponseDTO> result = taillePrixService.findByCocktail(1L);

        assertEquals(1, result.size());
        assertEquals(TaillePrix.Taille.M, result.get(0).taille());
    }

    @Test
    void save_shouldAssociateCocktailAndPersist() {
        when(taillePrixMapper.toEntity(requestDTO)).thenReturn(taillePrix);
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));

        taillePrixService.save(requestDTO);

        assertEquals(cocktail, taillePrix.getCocktail());
        verify(taillePrixRepository).save(taillePrix);
    }

    @Test
    void save_whenCocktailNotFound_shouldStillSave() {
        when(taillePrixMapper.toEntity(requestDTO)).thenReturn(taillePrix);
        when(cocktailRepository.findById(1L)).thenReturn(Optional.empty());

        taillePrixService.save(requestDTO);

        verify(taillePrixRepository).save(taillePrix);
    }

    @Test
    void update_shouldSetIdCocktailAndSave() {
        when(taillePrixMapper.toEntity(requestDTO)).thenReturn(taillePrix);
        when(cocktailRepository.findById(1L)).thenReturn(Optional.of(cocktail));

        taillePrixService.update(5L, requestDTO);

        assertEquals(5L, taillePrix.getId());
        verify(taillePrixRepository).save(taillePrix);
    }

    @Test
    void remove_shouldCallDeleteById() {
        taillePrixService.remove(1L);

        verify(taillePrixRepository).deleteById(1L);
    }
}
