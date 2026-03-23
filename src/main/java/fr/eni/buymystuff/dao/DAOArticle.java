package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.mapper.ArticleMapper;

import jdk.jshell.execution.Util;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import fr.eni.buymystuff.DTO.ArticleFormDTO;
import fr.eni.buymystuff.bo.Articles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DAOArticle implements IDAOArticle {

    private final JdbcTemplate jdbcTemplate;
    private final ArticleMapper articleMapper;
     public DAOArticle(JdbcTemplate jdbcTemplate, ArticleMapper articleMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.articleMapper = articleMapper;
    }

    @Override
    public void saveArticle(ArticleFormDTO article, Long idUser) throws IOException {
        // On retourne l'id du nouvel article ou celui existant
        Long articleId = saveOrUpdateArticle(article, idUser);
        // On parcout les categories id avant d'insert en BDD
        List<Long> categoryIds = article.getCategoriesIds() != null ? article.getCategoriesIds() : List.of();

        // On adapte les catégories existantes liées au produit ( delete / insert)
        syncArticleCategories(articleId, categoryIds);
    }

    @Override
    public Articles findArticleById(Long id) {
        String sql = """
                    SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres,
                           a.prix_initial,a.prix_vente, a.image, a.etat_vente,
                           ad.rue, ad.code_postal, ad.ville,
                           GROUP_CONCAT(c.libelle SEPARATOR ',') AS categories_string,
                           GROUP_CONCAT(c.no_categorie SEPARATOR ',') AS categories_ids,
                           u.no_utilisateur, u.pseudo, u.nom, u.prenom
                    FROM articles_vendus a
                    LEFT JOIN articles_categories ac ON a.no_article = ac.no_article
                    LEFT JOIN categories c ON c.no_categorie = ac.no_categorie
                    LEFT JOIN adresses ad ON ad.no_adresse = a.no_adresse
                    LEFT JOIN utilisateurs u ON u.no_utilisateur = a.no_utilisateur
                    WHERE a.no_article = ?
                    GROUP BY a.no_article, ad.rue, ad.code_postal, ad.ville
                """;

        Articles article = jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {
                    Articles a = new Articles();
                    a.setId(rs.getLong("no_article"));
                    a.setNomArticle(rs.getString("nom_article"));
                    a.setDescription(rs.getString("description"));
                    if (rs.getTimestamp("date_debut_encheres") != null) {
                        a.setDateDebut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
                    }
                    if (rs.getTimestamp("date_fin_encheres") != null) {
                        a.setDateFin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
                    }
                    a.setPrixInitial(rs.getInt("prix_initial"));
                    a.setPrixVente(rs.getInt("prix_vente"));
                    a.setImage(rs.getString("image"));
                    a.setEtatVente(rs.getBoolean("etat_vente"));
                     // 📍 Adresse
                    Adresse adresse = new Adresse();
                    adresse.setRue(rs.getString("rue"));
                    adresse.setCodePostal(rs.getString("code_postal"));
                    adresse.setVille(rs.getString("ville"));
                    a.setAdresseProprietaire(adresse);

                    Utilisateurs proprietaire = new Utilisateurs();
                    proprietaire.setId(rs.getInt("no_utilisateur"));
                    proprietaire.setPseudo(rs.getString("pseudo"));
                    proprietaire.setNom(rs.getString("nom"));
                    proprietaire.setPrenom(rs.getString("prenom"));
                    a.setUtilisateur(proprietaire);
                    // 🏷️ Catégories
                    String categoriesIdsStr = rs.getString("categories_ids");
                    String categoriesNamesStr = rs.getString("categories_string");
                    if (categoriesIdsStr != null && !categoriesIdsStr.isEmpty()) {
                        String[] ids = categoriesIdsStr.split(",");
                        String[] noms = categoriesNamesStr.split(",");
                        List<Categories> categories = new ArrayList<>();
                        for (int i = 0; i < ids.length; i++) {
                            Categories cat = new Categories();
                            cat.setId(Long.parseLong(ids[i]));
                            cat.setLibelle(noms[i]);
                            categories.add(cat);
                        }
                        a.setCategories(categories);
                    }

                    return a;
                },
                id
        );

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
        String sql = "SELECT c.no_categorie, c.libelle  FROM categories c JOIN ARTICLES_CATEGORIES ac ON c.no_categorie = ac.no_categorie WHERE ac.no_article = ?";
        List<Categories> categories = jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    Categories cat = new Categories();
                    cat.setId(rs.getLong("no_categorie"));
                    cat.setLibelle(rs.getString("libelle"));
                    return cat;
                },
                articleId
        );
        return categories;
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

    public Long saveOrUpdateArticle(ArticleFormDTO article, Long idUser) throws IOException {
        MultipartFile file = article.getImageFile();
        String imageName;
        int idInsertAdresse;

        if (file != null && !file.isEmpty()) {
            // Nouveau fichier choisi → on le stocke et on met à jour le DTO
            imageName = storeFile(file); // méthode qui copie le fichier sur le serveur et retourne le nom
        } else {
            // Aucun fichier → on garde l'image existante
            imageName = article.getImage();
        }
        article.setImage(imageName);

        if (article.getAdresseString() != null) {
            idInsertAdresse = insertAdresse(article.getAdresseString());
        } else {
            idInsertAdresse = 0;
        }

        if (article.getId() == null) {
            // INSERT
            String insertSql = "INSERT INTO ARTICLES_VENDUS " +
                    "(nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_adresse, etat_vente, image) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, article.getNomArticle());
                ps.setString(2, article.getDescription());
                ps.setObject(3, article.getDateDebut());
                ps.setObject(4, article.getDateFin());
                ps.setDouble(5, article.getPrixInitial());
                ps.setLong(6, idUser); // TODO: remplacer par l'utilisateur connecté
                ps.setLong(7, idInsertAdresse); // TODO: adresse
                ps.setInt(8, 0);  // état vente
                ps.setString(9, imageName); // image
                return ps;
            }, keyHolder);
            Long generatedId = keyHolder.getKey().longValue();
            article.setId(generatedId);
        } else {
            // UPDATE
            String updateSql = "UPDATE ARTICLES_VENDUS SET nom_article=?, description=?, date_debut_encheres=?, date_fin_encheres=?, prix_initial=?, etat_vente=?, image=? WHERE no_article=?";
            jdbcTemplate.update(updateSql,
                    article.getNomArticle(),
                    article.getDescription(),
                    article.getDateDebut(),
                    article.getDateFin(),
                    article.getPrixInitial(),
                    0,              // état vente
                    imageName,
                    article.getId()
            );
        }
        List<Long> categoryIds = article.getCategoriesIds() != null ?
                article.getCategoriesIds() :
                article.getCategories() != null ?
                        article.getCategories().stream().map(Categories::getId).toList() :
                        List.of();

        syncArticleCategories(article.getId(), categoryIds);

        return article.getId();
    }

    private boolean categoryExists(Long categoryId) {
        String sql = "SELECT COUNT(*) FROM CATEGORIES WHERE no_categorie = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }

    // Synchronisation des catégories
    private void syncArticleCategories(Long articleId, List<Long> newCategoryIds) {
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
        for (Long catId : toAdd) {
            if (categoryExists(catId)) {
                jdbcTemplate.update(insertSql, articleId, catId);
            }
        }

        // DELETE catégories supprimées
        String deleteSql = "DELETE FROM ARTICLES_CATEGORIES WHERE no_article=? AND no_categorie=?";
        for (Long catId : toRemove) {
            jdbcTemplate.update(deleteSql, articleId, catId);
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        Path uploadDir = Paths.get("src/main/resources/static/images").toAbsolutePath();
        Files.createDirectories(uploadDir);

        Path targetLocation = uploadDir.resolve(originalFilename);

        if (Files.exists(targetLocation)) {
            // Fichier déjà présent → on utilise le nom existant
            return originalFilename;
        } else {
            // Fichier absent → on crée un nom unique pour éviter collisions
            String newFileName = System.currentTimeMillis() + "_" + originalFilename;
            Path newTarget = uploadDir.resolve(newFileName);
            Files.copy(file.getInputStream(), newTarget, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        }
    }

    public int insertAdresse(String adresseString) {

        if (adresseString == null || adresseString.isBlank()) {
            throw new IllegalArgumentException("Adresse invalide");
        }

        // Split
        String[] parts = adresseString.split(",");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Format attendu : rue, code postal, ville");
        }

        String rue = parts[0].trim();
        String codePostal = parts[1].trim();
        String ville = parts[2].trim();

        String sql = "INSERT INTO adresses (rue, code_postal, ville) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, rue);
            ps.setString(2, codePostal);
            ps.setString(3, ville);
            return ps;
        }, keyHolder);

        // Récupération de l'id généré
        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue();
        } else {
            throw new RuntimeException("Erreur lors de la récupération de l'ID");
        }
    }

    @Override
    public List<Articles> getAllArticles() {
        String sql = """
                    SELECT 
                        a.no_article,
                        a.nom_article,
                        a.description,
                        a.date_debut_encheres,
                        a.date_fin_encheres,
                        a.prix_initial,
                        a.image,
                        a.etat_vente,
                        ad.rue,
                        ad.code_postal,
                        ad.ville,
                        GROUP_CONCAT(c.libelle SEPARATOR ',') AS categories_string,
                        GROUP_CONCAT(c.no_categorie SEPARATOR ',') AS categories_ids
                    FROM articles_vendus a
                    LEFT JOIN articles_categories ac ON a.no_article = ac.no_article
                    LEFT JOIN categories c ON c.no_categorie = ac.no_categorie
                    LEFT JOIN adresses ad ON ad.no_adresse = a.no_adresse
                    GROUP BY a.no_article;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Articles article = new Articles();
            article.setId(rs.getLong("no_article"));
            article.setNomArticle(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDateDebut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
            article.setDateFin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.setPrixInitial(rs.getInt("prix_initial"));
            article.setImage(rs.getString("image"));
            article.setEtatVente(rs.getBoolean("etat_vente"));
            Adresse adresse = new Adresse();
            adresse.setRue(rs.getString("rue"));
            adresse.setCodePostal(rs.getString("code_postal"));
            adresse.setVille(rs.getString("ville"));
            article.setAdresseProprietaire(adresse);

            // Mapping des catégories
            String categoriesIdsStr = rs.getString("categories_ids");
            String categoriesNamesStr = rs.getString("categories_string");

            if (categoriesIdsStr != null && !categoriesIdsStr.isEmpty()) {
                String[] ids = categoriesIdsStr.split(",");
                String[] noms = categoriesNamesStr.split(",");

                List<Categories> categories = new ArrayList<>();
                for (int i = 0; i < ids.length; i++) {
                    Categories cat = new Categories();
                    cat.setId(Long.parseLong(ids[i]));
                    cat.setLibelle(noms[i]);
                    categories.add(cat);
                }
                article.setCategories(categories);
            }
            return article;
        });
    }

    @Override
    public List<ArticleFormDTO> findBySearch(String nomArticle, String categorie,
                                             Integer prixMin, Integer prixMax,
                                             LocalDateTime dateStart, LocalDateTime dateEnd) {

        StringBuilder sql = new StringBuilder("""
                    SELECT 
                        a.no_article,
                        a.nom_article,
                        a.description,
                        a.date_debut_encheres,
                        a.date_fin_encheres,
                        a.prix_initial,
                        a.image,
                        a.etat_vente,
                        ad.rue,
                        ad.code_postal,
                        ad.ville,
                        GROUP_CONCAT(c.libelle SEPARATOR ',') AS categories_string,
                        GROUP_CONCAT(c.no_categorie SEPARATOR ',') AS categories_ids
                    FROM articles_vendus a
                    LEFT JOIN articles_categories ac ON a.no_article = ac.no_article
                    LEFT JOIN categories c ON c.no_categorie = ac.no_categorie
                    LEFT JOIN adresses ad ON ad.no_adresse = a.no_adresse
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        // 🔎 Filtre nom
        if (nomArticle != null && !nomArticle.isBlank()) {
            sql.append(" AND LOWER(a.nom_article) LIKE LOWER(?)");
            params.add("%" + nomArticle + "%");
        }

        // 🔎 Filtre catégorie (CORRIGÉ avec EXISTS)
        if (categorie != null && !categorie.isBlank()) {
            sql.append("""
                        AND EXISTS (
                            SELECT 1 FROM articles_categories ac2
                            WHERE ac2.no_article = a.no_article
                            AND ac2.no_categorie = ?
                        )
                    """);
            params.add(Long.parseLong(categorie));
        }

        // 🔎 Filtre prix min
        if (prixMin != null) {
            sql.append(" AND a.prix_initial >= ?");
            params.add(prixMin);
        }

        // 🔎 Filtre prix max
        if (prixMax != null) {
            sql.append(" AND a.prix_initial <= ?");
            params.add(prixMax);
        }

        // 🔎 Filtre date début
        if (dateStart != null) {
            sql.append(" AND a.date_debut_encheres >= ?");
            params.add(java.sql.Timestamp.valueOf(dateStart));
        }

        // 🔎 Filtre date fin
        if (dateEnd != null) {
            sql.append(" AND a.date_fin_encheres <= ?");
            params.add(java.sql.Timestamp.valueOf(dateEnd));
        }

        // 🔥 IMPORTANT : group by pour éviter doublons
        sql.append(" GROUP BY a.no_article");

        // 🔥 Execution
        List<Articles> articles = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {

            Articles article = new Articles();
            article.setId(rs.getLong("no_article"));
            article.setNomArticle(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDateDebut(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
            article.setDateFin(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.setPrixInitial(rs.getInt("prix_initial"));
            article.setImage(rs.getString("image"));
            article.setEtatVente(rs.getBoolean("etat_vente"));

            // 📍 Adresse
            Adresse adresse = new Adresse();
            adresse.setRue(rs.getString("rue"));
            adresse.setCodePostal(rs.getString("code_postal"));
            adresse.setVille(rs.getString("ville"));
            article.setAdresseProprietaire(adresse);

            // 🏷️ Catégories
            String categoriesIdsStr = rs.getString("categories_ids");
            String categoriesNamesStr = rs.getString("categories_string");

            if (categoriesIdsStr != null && !categoriesIdsStr.isEmpty()) {
                String[] ids = categoriesIdsStr.split(",");
                String[] noms = categoriesNamesStr.split(",");

                List<Categories> categories = new ArrayList<>();

                for (int i = 0; i < ids.length; i++) {
                    Categories cat = new Categories();
                    cat.setId(Long.parseLong(ids[i]));
                    cat.setLibelle(noms[i]);
                    categories.add(cat);
                }

                article.setCategories(categories);
            }

            return article;

        }, params.toArray());

        // 🔁 Mapping vers DTO
        return articles.stream()
                .map(articleMapper::toFormDTO)
                .toList();
    }


    @Override
    public Utilisateurs selectUserById(Long id) {
        String sql = """
        SELECT pseudo, nom, prenom, email, telephone
        FROM utilisateurs
        WHERE no_utilisateur = ?
    """;

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Utilisateurs utilisateur = new Utilisateurs();
            utilisateur.setPseudo(rs.getString("pseudo"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTelephone(rs.getString("telephone"));
            return utilisateur;
        });
    }
}