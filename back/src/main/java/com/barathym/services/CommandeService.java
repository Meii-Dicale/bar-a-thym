package com.barathym.services;

import com.barathym.dtos.CommandeRequestDTO;
import com.barathym.dtos.CommandeResponseDTO;
import com.barathym.entites.*;
import com.barathym.exceptions.ResourceNotFoundException;
import com.barathym.mappers.CommandeMapper;
import com.barathym.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CocktailRepository cocktailRepository;
    private final TaillePrixRepository taillePrixRepository;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final CommandeMapper commandeMapper;

    public List<CommandeResponseDTO> findAll() {
        return commandeMapper.toDTO(commandeRepository.findAll());
    }

    public List<CommandeResponseDTO> findByClient(Long clientId) {
        return commandeMapper.toDTO(commandeRepository.findByClientId(clientId));
    }

    public List<CommandeResponseDTO> findEnAttente() {
        return commandeMapper.toDTO(commandeRepository.findByStatut(Commande.Statut.COMMANDEE));
    }

    public CommandeResponseDTO find(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + id));
        return commandeMapper.toDTO(commande);
    }

    public void save(CommandeRequestDTO dto) {
        Commande commande = new Commande();
        commande.setStatut(Commande.Statut.COMMANDEE);
        commande.setCreeLe(LocalDateTime.now());

        if (utilisateurRepository.findById(dto.clientId()).isPresent()) {
            commande.setClient(utilisateurRepository.findById(dto.clientId()).get());
        }

        BigDecimal total = BigDecimal.ZERO;
        List<LigneCommande> lignes = new ArrayList<>();

        for (var ligneDto : dto.lignes()) {
            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(commande);
            ligne.setNote(ligneDto.note());
            ligne.setStatut(LigneCommande.Statut.PREPARATION);

            if (cocktailRepository.findById(ligneDto.cocktailId()).isPresent()) {
                ligne.setCocktail(cocktailRepository.findById(ligneDto.cocktailId()).get());
            }
            if (taillePrixRepository.findById(ligneDto.taillePrixId()).isPresent()) {
                TaillePrix tp = taillePrixRepository.findById(ligneDto.taillePrixId()).get();
                ligne.setTaillePrix(tp);
                total = total.add(tp.getPrix());
            }
            lignes.add(ligne);
        }

        commande.setTotal(total);
        commande.setLignesCommande(lignes);
        commandeRepository.save(commande);
    }

    public void prendreEnCharge(Long commandeId, Long barmakerId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + commandeId));
        commande.setStatut(Commande.Statut.EN_COURS);
        utilisateurRepository.findById(barmakerId).ifPresent(commande::setBarmaker);
        commandeRepository.save(commande);
    }

    public void verifierEtTerminer(Long commandeId) {
        if (commandeRepository.findById(commandeId).isPresent()) {
            Commande commande = commandeRepository.findById(commandeId).get();
            boolean toutesTerminees = commande.getLignesCommande().stream()
                .allMatch(l -> l.getStatut() == LigneCommande.Statut.TERMINEE);
            if (toutesTerminees) {
                commande.setStatut(Commande.Statut.TERMINEE);
                commandeRepository.save(commande);
            }
        }
    }

    public void annuler(Long id) {
        commandeRepository.findById(id).ifPresent(commande -> {
            commande.setStatut(Commande.Statut.ANNULEE);
            commandeRepository.save(commande);
        });
    }

    public void remove(Long id) {
        commandeRepository.deleteById(id);
    }
}
