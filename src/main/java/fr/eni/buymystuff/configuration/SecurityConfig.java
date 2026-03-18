package fr.eni.buymystuff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(dataSource);

        jdbc.setUsersByUsernameQuery("select pseudo, password, actif from utilisateur where pseudo=?");

        jdbc.setAuthoritiesByUsernameQuery("select pseudo, role from roles where pseudo=?");

        //pour gérer une seule table on change le nom de la table
        //jdbc.setAuthoritiesByUsernameQuery("select pseudo, role from utilisateur where pseudo=?");

        return jdbc;

    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(
//                auth ->
//                        auth
//
//                                /*************************ICI rajouter les routes ***************************/
//
//                                /* donnée l'accès à la page de la liste uniquement au utilisateur connectés */
//                                /* avec une saule table
//                            .requestMatchers(HttpMethod.GET, "/magic").hasAnyRole("EMPLOYE", "ADMIN")*/
//                                .requestMatchers("/", "/register", "/public/**", "/login").permitAll()
//                                .requestMatchers("/css/**", "/js/**", "/images/**", "/*.*").permitAll()   // Accès au CSS, JS, images depuis static
//                                .requestMatchers(HttpMethod.GET, "/magic").hasRole("EMPLOYE")
//                                .requestMatchers(HttpMethod.GET, "/magic/ajout").hasRole("ADMIN")
//                                .anyRequest().denyAll()
//
//        );
//        http.formLogin( form -> {
//                    //donne l'accès à la page de login à tous
//            form.loginPage("/login"); // URL GET pour afficher le formulaire
//            form.defaultSuccessUrl("/");                    //redirige après le login sur la page d'accueil
//
//                }
//        );
//
//        http.logout( logout -> {
//            //supprimer la session du côté du serveur d'application
//            logout.invalidateHttpSession(true)
//                    .clearAuthentication(true)
//                    //suppression du cookie sur le serveur web
//                    .deleteCookies("JSESSIONID")
//                    //déterminer la page à utiliser pour le logout
//                    .logoutUrl("/logout")
//                    //redirige après le logout sur la page d'accueil
//                    .logoutSuccessUrl("/")
//                    .permitAll();
//        });
        // Commentez tout ce qu'il y a en haut pour désactiver spring sécurity
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
