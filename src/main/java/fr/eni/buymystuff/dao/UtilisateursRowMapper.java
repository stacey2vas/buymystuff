package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UtilisateursRowMapper implements RowMapper<Utilisateurs> {

    @Override
    public Utilisateurs mapRow(ResultSet rs, int rowNum) throws SQLException {
        Utilisateurs user = new Utilisateurs();

        // Champs simples de l'utilisateur
        user.setId(rs.getInt("no_utilisateur"));
        user.setPseudo(rs.getString("pseudo"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom")); // ajout du prénom
        user.setEmail(rs.getString("email"));
        user.setTelephone(rs.getString("telephone"));
        user.setMotDePasse(rs.getString("mot_de_passe"));
        user.setCredit(rs.getInt("credit"));
        user.setAdministrateur(rs.getBoolean("administrateur"));

        // Adresse
        int noAdresse = rs.getInt("no_adresse");
        if (!rs.wasNull()) {
            Adresse adresse = new Adresse();
            adresse.setId(noAdresse);
            adresse.setRue(rs.getString("rue"));             // récupère la rue
            adresse.setVille(rs.getString("ville"));         // récupère la ville
            adresse.setCodePostal(rs.getString("code_postal")); // récupère le code postal
            user.setAdresse(adresse);
        }

        // Articles et Enchères (laissés à null, à remplir si nécessaire)
        user.setArticles(null);
        user.setEncheres(null);

        return user;
    }
}