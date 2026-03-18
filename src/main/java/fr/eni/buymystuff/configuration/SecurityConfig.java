package fr.eni.buymystuff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(dataSource);

        jdbc.setUsersByUsernameQuery("SELECT email, password, true FROM utilisateurs WHERE email=?");

        jdbc.setAuthoritiesByUsernameQuery(  "SELECT email, CONCAT('ROLE_', role) FROM utilisateurs WHERE email=?"  );
        //pour gérer une seule table on change le nom de la table
        //jdbc.setAuthoritiesByUsernameQuery("select pseudo, role from utilisateur where pseudo=?");

        return jdbc;

    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth ->
                        auth

                                /*************************ICI rajouter les routes ***************************/

                                /* donnée l'accès à la page de la liste uniquement au utilisateur connectés */
                                /* avec une saule table
                            .requestMatchers(HttpMethod.GET, "/magic").hasAnyRole("EMPLOYE", "ADMIN")*/
                                .requestMatchers("/", "/register", "/public/**", "/login").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/*.*").permitAll()   // Accès au CSS, JS, images depuis static
                                .requestMatchers(HttpMethod.GET, "/magic").hasRole("EMPLOYE")
                                .requestMatchers(HttpMethod.GET, "/magic/ajout").hasRole("ADMIN")
                                .anyRequest().denyAll()

        );
        http.formLogin(form -> form
                        .loginPage("/login") // URL GET pour afficher le formulaire
                        .loginProcessingUrl("/login") // URL POST pour la soumission du formulaire
                        .usernameParameter("email")   // 👈 on dit à Spring d'utiliser "email"
                        .passwordParameter("password") // password en 2ème param de connexion
                        .defaultSuccessUrl("/accueil", true)) // Si c'est validé on retourne a la page principale
                 ;

        http.logout( logout -> {
            //supprimer la session du côté du serveur d'application
            logout.invalidateHttpSession(true)
                    .clearAuthentication(true)
                    //suppression du cookie sur le serveur web
                    .deleteCookies("JSESSIONID")
                    //déterminer la page à utiliser pour le logout
                    .logoutUrl("/logout")
                    //redirige après le logout sur la page d'accueil
                    .logoutSuccessUrl("/accueil")
                    .permitAll();
        });


        return http.build();
    }

}
