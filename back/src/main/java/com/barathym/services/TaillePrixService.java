package com.barathym.services;

import com.barathym.dtos.DefinirPrixDTO;
import com.barathym.dtos.TaillePrixRequestDTO;
import com.barathym.dtos.TaillePrixResponseDTO;
import com.barathym.entites.Cocktail;
import com.barathym.entites.TaillePrix;
import com.barathym.mappers.TaillePrixMapper;
import com.barathym.repositories.CocktailRepository;
import com.barathym.repositories.TaillePrixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaillePrixService {

    private final TaillePrixRepository taillePrixRepository;
    private final CocktailRepository cocktailRepository;
    private final TaillePrixMapper taillePrixMapper;

    public List<TaillePrixResponseDTO> findByCocktail(Long cocktailId) {
        return taillePrixMapper.toDTO(taillePrixRepository.findByCocktailId(cocktailId));
    }

    public void save(TaillePrixRequestDTO dto) {
        TaillePrix taillePrix = taillePrixMapper.toEntity(dto);
        if (cocktailRepository.findById(dto.cocktailId()).isPresent()) {
            Cocktail cocktail = cocktailRepository.findById(dto.cocktailId()).get();
            taillePrix.setCocktail(cocktail);
        }
        taillePrixRepository.save(taillePrix);
    }

    public void update(Long id, TaillePrixRequestDTO dto) {
        TaillePrix taillePrix = taillePrixMapper.toEntity(dto);
        taillePrix.setId(id);
        if (cocktailRepository.findById(dto.cocktailId()).isPresent()) {
            Cocktail cocktail = cocktailRepository.findById(dto.cocktailId()).get();
            taillePrix.setCocktail(cocktail);
        }
        taillePrixRepository.save(taillePrix);
    }

    public void remove(Long id) {
        taillePrixRepository.deleteById(id);
    }

    @Transactional
    public void definirPrix(Long cocktailId, DefinirPrixDTO dto) {
        Cocktail cocktail = cocktailRepository.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail non trouvé"));
        taillePrixRepository.deleteByCocktailId(cocktailId);
        creer(cocktail, TaillePrix.Taille.S, dto.prixS());
        creer(cocktail, TaillePrix.Taille.M, dto.prixM());
        creer(cocktail, TaillePrix.Taille.L, dto.prixL());
    }

    private void creer(Cocktail cocktail, TaillePrix.Taille taille, java.math.BigDecimal prix) {
        TaillePrix tp = new TaillePrix();
        tp.setCocktail(cocktail);
        tp.setTaille(taille);
        tp.setPrix(prix);
        taillePrixRepository.save(tp);
    }
}
