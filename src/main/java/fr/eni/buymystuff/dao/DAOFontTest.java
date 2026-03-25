package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DAOFontTest implements IDAOFontTest {

    private final JdbcTemplate jdbcTemplate;

    public DAOFontTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Encheres> selectEncheresByUserId(Long userId) {
        String sql = """
                SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.no_utilisateur AS vendeur_id, a.no_adresse, a.etat_vente, a.image,
                e_last.*, u.pseudo AS pseudoVendeur, u.nom, u.prenom
                FROM articles_vendus a
                INNER JOIN (
                SELECT no_article, MAX(date_enchere) AS derniere_date
                FROM encheres
                GROUP BY no_article
                ) last_e
                ON a.no_article = last_e.no_article
                INNER JOIN encheres e_last
                ON e_last.no_article = last_e.no_article
                AND e_last.date_enchere = last_e.derniere_date
                INNER JOIN utilisateurs u ON u.no_utilisateur = a.no_utilisateur
                WHERE a.etat_vente = 0
                AND EXISTS (
                SELECT 1
                FROM encheres e_user
                WHERE e_user.no_article = a.no_article
                AND e_user.no_utilisateur = ?
                );
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToEnchere(rs), userId);
    }

    private Encheres mapRowToEnchere(java.sql.ResultSet rs) throws java.sql.SQLException {
        Encheres enchere = new Encheres();
        enchere.setId(rs.getInt("id_enchere"));

        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setId(rs.getInt("no_utilisateur"));
        utilisateur.setPseudo(rs.getString("pseudoVendeur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setId(rs.getInt("vendeur_id"));

        Articles art = new Articles();
        art.setId(rs.getLong("no_article"));
        art.setUtilisateur(utilisateur);
        art.setImage(rs.getString("image"));
        enchere.setArticle(art);

        var timestamp = rs.getTimestamp("date_enchere");
        if (timestamp != null) {
            enchere.setDateEnchere(timestamp.toLocalDateTime());
        }

        enchere.setMontantEnchere(rs.getInt("montant_enchere"));

        return enchere;
    }
}