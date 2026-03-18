package fr.eni.buymystuff.dao;

import fr.eni.buymystuff.bo.Utilisateurs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Component
public class DAOAuth  implements IDAOAuth {

    private final JdbcTemplate jdbcTemplate;
    private final UtilisateursRowMapper utilisateursRowMapper;

    private String INSERT_USER_SQL = "";

    public DAOAuth(JdbcTemplate jdbcTemplate, UtilisateursRowMapper utilisateursRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateursRowMapper = utilisateursRowMapper;
        loadSQlScript();

    }

    private void loadSQlScript() {
        // Essayer de charger le fichier SQL
        try {
            INSERT_USER_SQL = new ClassPathResource("sql/insert-user.sql")
                    .getContentAsString(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insert(Utilisateurs utilisateur) {

    }
}
