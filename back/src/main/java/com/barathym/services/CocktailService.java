package com.barathym.services;

import com.barathym.dtos.CocktailRequestDTO;
import com.barathym.dtos.CocktailResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.mappers.CocktailMapper;
import com.barathym.repositories.CocktailRepository;
import com.barathym.repositories.TaillePrixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CocktailService {

    private final CocktailRepository cocktailRepository;
    private final CocktailMapper cocktailMapper;
    private final TaillePrixRepository taillePrixRepository;

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findAll() {
        return enrichir(cocktailMapper.toDTO(cocktailRepository.findAll()));
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findActifs() {
        return enrichir(cocktailMapper.toDTO(cocktailRepository.findByActifTrue()));
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findCarte() {
        return enrichir(cocktailMapper.toDTO(cocktailRepository.findActifsAvecPrix()));
    }

    @Transactional(readOnly = true)
    public List<CocktailResponseDTO> findDisponibles() {
        return enrichir(cocktailMapper.toDTO(cocktailRepository.findAvecTousIngredentsDisponibles()));
    }

    @Transactional(readOnly = true)
    public CocktailResponseDTO find(Long id) {
        return cocktailRepository.findById(id)
                .map(c -> {
                    CocktailResponseDTO dto = cocktailMapper.toDTO(c);
                    boolean aPrix = taillePrixRepository.findByCocktailId(id).isEmpty() == false;
                    return new CocktailResponseDTO(dto.id(), dto.apiId(), dto.nom(), dto.imageUrl(),
                            dto.categorie(), dto.actif(), dto.instructions(), dto.ingredients(), aPrix);
                })
                .orElse(null);
    }

    private List<CocktailResponseDTO> enrichir(List<CocktailResponseDTO> dtos) {
        Set<Long> avecPrix = taillePrixRepository.findAllCocktailIdsAvecPrix();
        return dtos.stream()
                .map(dto -> new CocktailResponseDTO(
                        dto.id(), dto.apiId(), dto.nom(), dto.imageUrl(),
                        dto.categorie(), dto.actif(), dto.instructions(), dto.ingredients(),
                        avecPrix.contains(dto.id())))
                .collect(Collectors.toList());
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
