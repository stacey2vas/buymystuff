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

    private String FIND_APPUSER_BY_ID_SQL = "";

    public DAOAuth(JdbcTemplate jdbcTemplate, UtilisateursRowMapper utilisateursRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateursRowMapper = utilisateursRowMapper;
        loadSQlScript();

    }

    private void loadSQlScript() {
        // Essayer de charger le fichier SQL
        try {
            FIND_APPUSER_BY_ID_SQL = new ClassPathResource("sql/find_user_by_id.sql")
                    .getContentAsString(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Utilisateurs selectByEmailAndPassword(String email, String password) {
        Utilisateurs appUser = null;
        List<Utilisateurs> appUserList = jdbcTemplate.query(FIND_APPUSER_BY_ID_SQL, utilisateursRowMapper, email, password);

        if (appUserList.size() > 0 ){
            appUser = appUserList.get(0);
        }

        return appUser;
    }
}
