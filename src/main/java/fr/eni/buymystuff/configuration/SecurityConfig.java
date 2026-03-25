package fr.eni.buymystuff.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(dataSource);
        jdbc.setUsersByUsernameQuery(
                "select pseudo, password, actif from utilisateurs where pseudo=?"
        );
        jdbc.setAuthoritiesByUsernameQuery(
                "select pseudo, role from roles where pseudo=?"
        );
        return jdbc;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        // Désactivation CSRF uniquement pour les API REST
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/accueil", "/register", "/public/**", "/login", "/formulaire-test").permitAll()
                       .requestMatchers("/css/**", "/js/**", "/images/**", "/*.*").permitAll()
                               .requestMatchers(HttpMethod.GET, "/add-article").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/enchere/{id}").hasAnyRole("USER", "ADMIN")
                       .requestMatchers("/api/**").permitAll() // Autorisé l'accès aux routes d'API (ex: /api/films) sans authentification
                        .requestMatchers("/api/articles","/api/creditUser").permitAll()
                        .requestMatchers("/accueil").authenticated()
                                //.anyRequest().authenticated() // ICI ON DECOMMENTERA A LA FIN POUR LES TESTS ON PERMIT ALL pour donner accès à tous les fichiers
                                .anyRequest().permitAll()
                )
                .formLogin( form -> {
                            //donne l'accès à la page de login à tous
                            form.loginPage("/login")
                                    .usernameParameter("pseudo") // <--- ici on dit que le champ pseudo correspond à username
                                    .passwordParameter("password")
                            .permitAll();
                            //redirige après le login sur la page d'accueil
                        form.defaultSuccessUrl("/", true);                        }
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")             // URL qui déclenche le logout
                        .logoutSuccessUrl("/") // où aller après déconnexion
                        .invalidateHttpSession(true)      // invalide la session
                        .deleteCookies("JSESSIONID")      // supprime le cookie de session
                        .permitAll() // Permet à tous d'accéder à l'URL de logout
                )
                 .httpBasic(AbstractHttpConfigurer::disable) // Enlève cette saleté de popup de login basique du navigateur pour les API REST
                .rememberMe(remember -> remember
                .key("uniqueAndSecret") // Clé secrète pour le token de remember-me
                .tokenValiditySeconds(7 * 24 * 60 * 60)); // Durée de validité du token (ex: 7 jours)

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();}
}

