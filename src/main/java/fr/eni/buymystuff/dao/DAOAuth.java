package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Component
public class DAOAuth  implements IDAOAuth {

    private final JdbcTemplate jdbcTemplate;
    private final UtilisateursRowMapper utilisateursRowMapper;
    private final PasswordEncoder passwordEncoder;


    public DAOAuth(JdbcTemplate jdbcTemplate, UtilisateursRowMapper utilisateursRowMapper, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateursRowMapper = utilisateursRowMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Adresse insertAdresse(Adresse adresse) {
        String sql = """
            INSERT INTO adresses (rue, code_postal, ville)
            VALUES (?, ?, ?)
        """;

        jdbcTemplate.update(sql, adresse.getRue(), adresse.getCodePostal(), adresse.getVille());

        return adresse;
    }

    @Override
    public Utilisateurs insert(Utilisateurs utilisateur) {
        String sql = """
            INSERT INTO utilisateurs (nom, prenom, email, pseudo,
            password, telephone,credit, administrateur, no_adresse, actif)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        //ici il faut traduire
        //bcrypt
        String bcrypt = passwordEncoder.encode(utilisateur.getMotDePasse());
        System.out.println("Mot de passe avant encode : " + utilisateur.getMotDePasse());
        System.out.println("Mot de passe après encode : " + bcrypt);

        jdbcTemplate.update(sql, utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(),
                utilisateur.getPseudo(), bcrypt, utilisateur.getTelephone(), utilisateur.getCredit(),
                 utilisateur.isAdministrateur(), utilisateur.getAdresse().getId(), utilisateur.isActif()
                );

        return utilisateur;
    }
}
