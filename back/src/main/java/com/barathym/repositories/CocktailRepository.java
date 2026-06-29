package com.barathym.repositories;

import com.barathym.entites.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {
    Optional<Cocktail> findByApiId(String apiId);
    List<Cocktail> findByActifTrue();
}
