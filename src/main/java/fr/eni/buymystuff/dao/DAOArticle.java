package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Categories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DAOArticle implements IDAOArticle {

    private final JdbcTemplate jdbcTemplate;

    public DAOArticle(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveArticle(ArticleFormDTO article){
        // On retourne l'id du nouvel article ou celui existant
        Long articleId = saveOrUpdateArticle(article);
        // On parcout les categories id avant d'insert en BDD
        List<Long> categoryIds = article.getCategories().stream()
                .map(Categories::getId)
                .toList();
        // On adapte les catégories existantes liées au produit ( delete / insert)
        syncArticleCategories(articleId, categoryIds);
    }

    @Override
    public Articles findArticleById(Long id) {
        String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = ?";

        Articles article = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {
                    Articles a = new Articles();
                    a.setId(rs.getLong("no_article"));
                    a.setNomArticle(rs.getString("nom_article"));
                    a.setDescription(rs.getString("description"));
                    a.setImage(rs.getString("image"));
                    if (rs.getTimestamp("date_debut_encheres") != null) {
                        a.setDateDebut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
                    }
                    if (rs.getTimestamp("date_fin_encheres") != null) {
                        a.setDateFin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
                    }

                    a.setPrixInitial(rs.getInt("prix_initial"));
                    a.setPrixVente(rs.getInt("prix_vente"));
                    a.setEtatVente(rs.getInt("etat_vente") == 1);

                    // récupérer l'adresse
                    Long adresseId = rs.getLong("no_adresse");
                    if(adresseId != null && adresseId > 0){
                        a.setAdresseProprietaire(findAdresseById(adresseId));
                    }

                    return a;
                },
                id
        );

        // récupérer les catégories
        article.setCategories(findCategoriesByArticleId(id));

        return article;
    }
    private Adresse findAdresseById(Long adresseId) {
        String sql = "SELECT * FROM adresses WHERE no_adresse = ?";
        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    Adresse adresse = new Adresse();
                    adresse.setId(rs.getLong("no_adresse"));
                    adresse.setRue(rs.getString("rue"));
                    adresse.setCodePostal(rs.getString("code_postal"));
                    adresse.setVille(rs.getString("ville"));
                    return adresse;
                },
                adresseId
        );
    }
    private List<Categories> findCategoriesByArticleId(Long articleId) {
        String sql = "SELECT c.no_categorie, c.libelle " +
                "FROM categories c " +
                "JOIN ARTICLES_CATEGORIES ac ON c.no_categorie = ac.no_categorie " +
                "WHERE ac.no_article = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Categories cat = new Categories();
                    cat.setId(rs.getLong("no_categorie"));
                    cat.setLibelle(rs.getString("libelle"));
                    return cat;
                },
                articleId
        );
    }
    @Override
    public List<Articles> findAllArticlesByUserId(int id) {
        String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur = ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Articles article = new Articles();
                    article.setId(rs.getLong("no_article"));
                    article.setNomArticle(rs.getString("nom_article"));
                    article.setDescription(rs.getString("description"));

                    if (rs.getTimestamp("date_debut_encheres") != null) {
                        article.setDateDebut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
                    }
                    if (rs.getTimestamp("date_fin_encheres") != null) {
                        article.setDateFin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
                    }

                    article.setPrixInitial(rs.getInt("prix_initial"));
                    article.setPrixVente(rs.getInt("prix_vente"));
                    article.setEtatVente(rs.getInt("etat_vente") == 1);

                    return article;
                },
                id
        );
    }

    public Long saveOrUpdateArticle(ArticleFormDTO article) {
        if (article.getId() == null) {
            // INSERT
            String insertSql = "INSERT INTO ARTICLES_VENDUS " +
                    "(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_adresse, etat_vente) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, article.getNomArticle());
                ps.setString(2, article.getDescription());
                ps.setObject(3, article.getDateDebut());
                ps.setObject(4, article.getDateFin());
                ps.setDouble(5, article.getPrixInitial());
                ps.setLong(6, 1); // à remplacer par l'utilisateur connecté
                ps.setLong(7, 1); // adresse
                ps.setInt(8, 0);
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            article.setId(generatedId);

        } else {
            // UPDATE
            String updateSql = "UPDATE ARTICLES_VENDUS SET nom_article=?, description=?, date_debut_encheres=?, date_fin_encheres=?, prix_initial=?, etat_vente=? WHERE no_article=?";
            jdbcTemplate.update(updateSql,
                    article.getNomArticle(),
                    article.getDescription(),
                    article.getDateDebut(),
                    article.getDateFin(),
                    article.getPrixInitial(),
                    0,
                    article.getId()
            );
        }
        return article.getId();
    }

    // Vérifie si la catégorie existe
    private boolean categoryExists(Long categoryId){
        String sql = "SELECT COUNT(*) FROM CATEGORIES WHERE no_categorie = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }
    // Synchronisation des catégories
    private void syncArticleCategories(Long articleId, List<Long> newCategoryIds){
        // Récupérer les catégories actuelles
        String selectSql = "SELECT no_categorie FROM ARTICLES_CATEGORIES WHERE no_article = ?";
        List<Long> existingCategoryIds = jdbcTemplate.queryForList(selectSql, Long.class, articleId);

        // Catégories à ajouter
        List<Long> toAdd = newCategoryIds.stream()
                .filter(id -> !existingCategoryIds.contains(id))
                .toList();

        // Catégories à supprimer
        List<Long> toRemove = existingCategoryIds.stream()
                .filter(id -> !newCategoryIds.contains(id))
                .toList();
        // INSERT nouvelles catégories
        String insertSql = "INSERT INTO ARTICLES_CATEGORIES(no_article, no_categorie) VALUES (?, ?)";
        for(Long catId : toAdd){
            if(categoryExists(catId)) {
                jdbcTemplate.update(insertSql, articleId, catId);
            }
        }

        // DELETE catégories supprimées
        String deleteSql = "DELETE FROM ARTICLES_CATEGORIES WHERE no_article=? AND no_categorie=?";
        for(Long catId : toRemove){
            jdbcTemplate.update(deleteSql, articleId, catId);
        }
    }
}