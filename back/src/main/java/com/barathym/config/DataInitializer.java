package com.barathym.config;

import com.barathym.entites.Utilisateur;
import com.barathym.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        creerSiAbsent("Admin",  "Bar",    "barmaker@barathym.fr", "barmaker123", Utilisateur.Role.BARMAKER);
        creerSiAbsent("Test",   "Barman", "barman@test.fr",       "test123!",    Utilisateur.Role.BARMAKER);
        creerSiAbsent("Dupont", "Marie",  "marie@barathym.fr",    "client123",   Utilisateur.Role.CLIENT);
    }

    private void creerSiAbsent(String nom, String prenom, String email, String mdp, Utilisateur.Role role) {
        if (utilisateurRepository.findByEmail(email).isEmpty()) {
            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setEmail(email);
            u.setMotDePasse(passwordEncoder.encode(mdp));
            u.setRole(role);
            utilisateurRepository.save(u);
        }
    }
}
