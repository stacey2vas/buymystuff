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

        user.setId(rs.getInt("no_utilisateur"));
        user.setPseudo(rs.getString("pseudo"));
        user.setNom(rs.getString("nom"));
        user.setEmail(rs.getString("email"));
        user.setTelephone(rs.getString("telephone"));
        user.setMotDePasse(rs.getString("mot_de_passe"));
        user.setCredit(rs.getInt("credit"));
        user.setAdministrateur(rs.getBoolean("administrateur"));

        int noAdresse = rs.getInt("no_adresse");
        if (!rs.wasNull()) {
            Adresse adresse = new Adresse();
            adresse.setId(noAdresse);
            user.setAdresse(adresse);
        }
        user.setArticles(null);
        user.setEncheres(null);
        return user;
    }
}
