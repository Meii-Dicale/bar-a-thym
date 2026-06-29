package com.barathym.services;

import com.barathym.dtos.LigneCommandeResponseDTO;
import com.barathym.entites.LigneCommande;
import com.barathym.mappers.LigneCommandeMapper;
import com.barathym.repositories.LigneCommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final LigneCommandeMapper ligneCommandeMapper;
    private final CommandeService commandeService;

    public List<LigneCommandeResponseDTO> findByCommande(Long commandeId) {
        return ligneCommandeMapper.toDTO(ligneCommandeRepository.findByCommandeId(commandeId));
    }

    public void avancerStatut(Long id) {
        if (ligneCommandeRepository.findById(id).isPresent()) {
            LigneCommande ligne = ligneCommandeRepository.findById(id).get();
            LigneCommande.Statut prochain = prochainStatut(ligne.getStatut());
            if (prochain != null) {
                ligne.setStatut(prochain);
                ligneCommandeRepository.save(ligne);
                if (prochain == LigneCommande.Statut.TERMINEE) {
                    commandeService.verifierEtTerminer(ligne.getCommande().getId());
                }
            }
        }
    }

    private LigneCommande.Statut prochainStatut(LigneCommande.Statut statut) {
        return switch (statut) {
            case PREPARATION -> LigneCommande.Statut.ASSEMBLAGE;
            case ASSEMBLAGE  -> LigneCommande.Statut.DRESSAGE;
            case DRESSAGE    -> LigneCommande.Statut.TERMINEE;
            case TERMINEE    -> null;
        };
    }
}
