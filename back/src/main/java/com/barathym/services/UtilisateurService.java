package com.barathym.services;

import com.barathym.dtos.UtilisateurRequestDTO;
import com.barathym.dtos.UtilisateurResponseDTO;
import com.barathym.entites.Utilisateur;
import com.barathym.mappers.UtilisateurMapper;
import com.barathym.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    public List<UtilisateurResponseDTO> findAll() {
        return utilisateurMapper.toDTO(utilisateurRepository.findAll());
    }

    public UtilisateurResponseDTO find(Long id) {
        Utilisateur utilisateur = null;
        if (utilisateurRepository.findById(id).isPresent()) {
            utilisateur = utilisateurRepository.findById(id).get();
        }
        return utilisateurMapper.toDTO(utilisateur);
    }

    public void save(UtilisateurRequestDTO dto) {
        utilisateurRepository.save(utilisateurMapper.toEntity(dto));
    }

    public void update(Long id, UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        utilisateur.setId(id);
        utilisateurRepository.save(utilisateur);
    }

    public void remove(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
