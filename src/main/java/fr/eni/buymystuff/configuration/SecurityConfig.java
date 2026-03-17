package fr.eni.buymystuff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

    @Configuration
    public class SecurityConfig {

        @Bean
        UserDetailsService userDetailsService(DataSource dataSource) {
            JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(dataSource);

            jdbc.setUsersByUsernameQuery("select pseudo, password, from utilisateur where email=?");

            jdbc.setAuthoritiesByUsernameQuery("select pseudo, role from roles where pseudo=?");

            //pour gérer une seule table on change le nom de la table
            //jdbc.setAuthoritiesByUsernameQuery("select pseudo, role from utilisateur where pseudo=?");

            return jdbc;

        }
}
