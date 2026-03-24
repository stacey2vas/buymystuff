package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DAOEnchere implements IDAOEnchere {

    private final JdbcTemplate jdbcTemplate;

    public DAOEnchere(JdbcTemplate jdbcTemplate, ArticleMapper articleMapper) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Encheres> selectEncheresByArticleId(Long id) {
        String sql = """
                    SELECT e.id_enchere,
                           e.no_utilisateur,
                           u.pseudo,
                           u.nom,
                           u.prenom,
                           e.no_article,
                           e.date_enchere,
                           e.montant_enchere
                    FROM encheres e
                    JOIN utilisateurs u ON e.no_utilisateur = u.no_utilisateur
                    WHERE e.no_article = ?
                    ORDER BY e.date_enchere DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Encheres enchere = new Encheres();

            // Id de l'enchère
            enchere.setId(rs.getInt("id_enchere"));

            // Utilisateur minimal
            Utilisateurs utilisateur = new Utilisateurs();
            utilisateur.setId(rs.getInt("no_utilisateur"));
            utilisateur.setPseudo(rs.getString("pseudo"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            enchere.setUtilisateur(utilisateur);

            // Date et montant
            enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));

            return enchere;
        });
    }

    @Transactional
    @Override
    public void addEnchere(EnchereDTO enchere) throws IOException {
        Encheres lastEnchere = this.selectLastEnchereByArticleId(enchere.getArticle().getId());
        if (lastEnchere != null) {
            String sqlUpdateCreditLastEnchere = "UPDATE utilisateurs SET credit = credit + ? WHERE no_utilisateur = ?";
            jdbcTemplate.update(sqlUpdateCreditLastEnchere, lastEnchere.getMontantEnchere(), lastEnchere.getUtilisateur().getId());
        }
        String sql = "INSERT INTO encheres (no_utilisateur, no_article, date_enchere, montant_enchere) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                enchere.getUtilisateur().getId(),
                enchere.getArticle().getId(),
                LocalDateTime.now(),
                enchere.getMontantEnchere()
        );
        String sqlUpdatePrixVente = "UPDATE articles_vendus SET prix_vente = ? WHERE no_article = ?";
        jdbcTemplate.update(sqlUpdatePrixVente, enchere.getMontantEnchere(), enchere.getArticle().getId());

        String sqlUpdateCreditNewEnchere = "UPDATE utilisateurs SET credit = credit - ? WHERE no_utilisateur = ?";
        jdbcTemplate.update(sqlUpdateCreditNewEnchere, enchere.getMontantEnchere(), enchere.getUtilisateur().getId());
    }

    @Override
    public Encheres selectLastEnchereByArticleId(Long id) {
        String sql = """
                    SELECT e.no_utilisateur, e.montant_enchere, e.date_enchere
                    FROM encheres e
                    WHERE e.no_article = ?
                    ORDER BY e.date_enchere DESC
                    LIMIT 1
                """;

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Encheres e = new Encheres();
            Utilisateurs user = new Utilisateurs();
            user.setId(rs.getInt("no_utilisateur"));
            e.setUtilisateur(user);
            e.setMontantEnchere(rs.getInt("montant_enchere"));
            e.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
            return e;
        });
    }
}
