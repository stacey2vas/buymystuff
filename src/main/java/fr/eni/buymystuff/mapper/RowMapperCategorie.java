package fr.eni.buymystuff.mapper;

import fr.eni.buymystuff.bo.Categories;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperCategorie implements RowMapper<Categories> {

    @Override
    public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
        Categories categorie = new Categories();
        categorie.setId(rs.getLong("no_categorie"));
        categorie.setLibelle(rs.getString("libelle"));
        return categorie;
    }
}

