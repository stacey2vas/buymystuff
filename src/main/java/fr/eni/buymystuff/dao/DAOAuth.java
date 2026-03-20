package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class DAOAuth  implements IDAOAuth {

    private final JdbcTemplate jdbcTemplate;;
    private final PasswordEncoder passwordEncoder;


    public DAOAuth(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Adresse insertAdresse(Adresse adresse) {
        System.out.println("Adresse dans insert adresse :" + adresse);

        String sql = "INSERT INTO adresses (rue, code_postal, ville) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, adresse.getRue());
            ps.setString(2, adresse.getCodePostal());
            ps.setString(3, adresse.getVille());
            return ps;
        }, keyHolder);

        // Récupération de l'ID généré
        adresse.setId(keyHolder.getKey().longValue());

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
                utilisateur.isAdministrateur(), utilisateur.getAdresse().getId(), 1
                );

        return utilisateur;
    }

    @Override
    public Utilisateurs selectById(int id) {
        String sql = """
                SELECT pseudo,nom,prenom,email,telephone,rue, code_postal,ville
                FROM utilisateurs
                INNER JOIN adresses ON utilisateurs.no_adresse = adresses.no_adresse
                WHERE no_utilisateur = ?
                """;
        jdbcTemplate.query(sql, new Object[]{id}, rs -> {;
            if (rs.next()) {
                Utilisateurs utilisateur = new Utilisateurs();
                utilisateur.setPseudo(rs.getString("pseudo"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setTelephone(rs.getString("telephone"));

                Adresse adresse = new Adresse();
                adresse.setRue(rs.getString("rue"));
                adresse.setCodePostal(rs.getString("code_postal"));
                adresse.setVille(rs.getString("ville"));

                utilisateur.setAdresse(adresse);

                return utilisateur;
            }
            return null;
        });
        return null ;
    }
}
