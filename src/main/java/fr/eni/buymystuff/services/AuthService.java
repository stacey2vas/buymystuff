package fr.eni.buymystuff.services;


import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOAuth;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    private final IDAOAuth daoAuth;

    public AuthService(IDAOAuth daoAuth) {
        this.daoAuth = daoAuth;
    }

    public ServiceResponse<Utilisateurs> login(String email, String password){
        // essayer de retrouver user
        Utilisateurs loggedUser = daoAuth.selectByEmailAndPassword(email, password);

        // Si je trouve pas => erreur
        if (loggedUser == null)
        {
            return new ServiceResponse<Utilisateurs>("7025", "Email ou mot de passe invalide");
        }

        // Success
        return new ServiceResponse<Utilisateurs>("2002", "Authentification réussie", loggedUser);
    }
}