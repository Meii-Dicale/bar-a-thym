package com.barathym.repositories;

import com.barathym.entites.TaillePrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaillePrixRepository extends JpaRepository<TaillePrix, Long> {
    List<TaillePrix> findByCocktailId(Long cocktailId);
    void deleteByCocktailId(Long cocktailId);
}
