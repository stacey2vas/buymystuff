package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.DTO.EnchereDTO;
import fr.eni.buymystuff.bo.Articles;
import fr.eni.buymystuff.bo.Encheres;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.mapper.ArticleMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DAOEnchere implements IDAOEnchere{

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

            enchere.setId(rs.getInt("id_enchere"));

            Utilisateurs utilisateur = new Utilisateurs();
            utilisateur.setId(rs.getInt("no_utilisateur"));
            utilisateur.setPseudo(rs.getString("pseudo"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            enchere.setUtilisateur(utilisateur);

            enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));

            return enchere;
        });
    }

    @Override
    public void addEnchere(EnchereDTO enchere) throws IOException {
        String sql = "INSERT INTO encheres (no_utilisateur, no_article, date_enchere, montant_enchere) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                enchere.getUtilisateur().getId(),
                enchere.getArticle().getId(),
                LocalDateTime.now(),
                enchere.getMontantEnchere()
        );
    }
}
