package com.barathym.services;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.mappers.CocktailMapper;
import com.barathym.repositories.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailService {

    private final CocktailRepository cocktailRepository;
    private final CocktailMapper cocktailMapper;

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findAll() {
        return cocktailMapper.toDTO(cocktailRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findActifs() {
        return cocktailMapper.toDTO(cocktailRepository.findByActifTrue());
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findCarte() {
        return cocktailMapper.toDTO(cocktailRepository.findActifsAvecPrix());
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findDisponibles() {
        return cocktailMapper.toDTO(cocktailRepository.findAvecTousIngredentsDisponibles());
    }

    @Transactional(readOnly = true)
    public CocktailResponseDTO find(Long id) {
        Cocktail cocktail = null;
        if (cocktailRepository.findById(id).isPresent()) {
            cocktail = cocktailRepository.findById(id).get();
        }
        return cocktailMapper.toDTO(cocktail);
    }

    public void save(CocktailRequestDTO dto) {
        cocktailRepository.save(cocktailMapper.toEntity(dto));
    }

    public void update(Long id, CocktailRequestDTO dto) {
        Cocktail cocktail = cocktailMapper.toEntity(dto);
        cocktail.setId(id);
        cocktailRepository.save(cocktail);
    }

    public void toggleActif(Long id) {
        if (cocktailRepository.findById(id).isPresent()) {
            Cocktail cocktail = cocktailRepository.findById(id).get();
            cocktail.setActif(!cocktail.getActif());
            cocktailRepository.save(cocktail);
        }
    }

    public void remove(Long id) {
        cocktailRepository.deleteById(id);
    }
}
