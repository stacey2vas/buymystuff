package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.jdbc.IDAOCategories;
import fr.eni.buymystuff.mapper.RowMapperCategorie;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class DAOCategoriesJDBC implements IDAOCategories {

    private final JdbcTemplate jdbcTemplate;

    private String FIND_CATEGORIE_BY_ID_SQL = "";
    private String SAVE_CATEGORIES_SQL = "";
    private String UPDATE_CATEGORIES_SQL = "";
    private String SELECT_ALL_CATEGORIES_SQL = "";
    private String SELECT_CATEGORIE_BY_LIBELLE_SQL = "";

    public DAOCategoriesJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        loadSQlScript();
    }

    private void loadSQlScript() {

        try {
            SAVE_CATEGORIES_SQL = new ClassPathResource("sql/save_categories.sql")
                    .getContentAsString(StandardCharsets.UTF_8);
            FIND_CATEGORIE_BY_ID_SQL = new ClassPathResource("sql/find_categorie_by_id.sql")
                    .getContentAsString(StandardCharsets.UTF_8);
            UPDATE_CATEGORIES_SQL = new ClassPathResource("sql/update_categories.sql")
                    .getContentAsString(StandardCharsets.UTF_8);
            SELECT_ALL_CATEGORIES_SQL = new ClassPathResource("sql/select_all_categories.sql")
                    .getContentAsString(StandardCharsets.UTF_8);
            SELECT_CATEGORIE_BY_LIBELLE_SQL = new ClassPathResource("sql/select_categorie_by_libelle.sql")
                    .getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categories> selectAllCategories() {
        return jdbcTemplate.query(
                SELECT_ALL_CATEGORIES_SQL,
                new RowMapperCategorie()
        );
    }

    @Override
    public Categories save(Categories categorie) {
        if(categorie.getId() == null){
            return create(categorie);
        } else {
            return update(categorie);
        }
    }

    @Override
    public Categories selectByLibelle(String libelle) {
        List<Categories> result = jdbcTemplate.query(
                SELECT_CATEGORIE_BY_LIBELLE_SQL,
                new RowMapperCategorie(),
                libelle
        );
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public Categories selectById(Long id) {
        List<Categories> categorie = jdbcTemplate.query(
                    FIND_CATEGORIE_BY_ID_SQL,
                    new RowMapperCategorie(),
                    id
            );
        return categorie.isEmpty() ? null : categorie.getFirst();
    }

    @Override
    public Categories update(Categories categories) {
        jdbcTemplate.update(UPDATE_CATEGORIES_SQL,
                categories.getLibelle(),
                categories.getId()
        );

        return categories;
    }

    private Categories create(Categories categories) {
        jdbcTemplate.update(SAVE_CATEGORIES_SQL,
                categories.getLibelle()
        );

        return categories;
    }
}
