package fr.eni.buymystuff.services;


import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOAuth;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IDAOAuth daoAuth;
    public AuthService(IDAOAuth daoAuth) {
        this.daoAuth = daoAuth;
    }

    public ServiceResponse<Utilisateurs> ajouterUtilisateur(String nom, String prenom, String email, String pseudo, String password, String telephone,
                                                            int credit, boolean administrateur, Adresse adresse,
                                                            boolean actif, String adresseRue, String adresseCodePostal, String adresseVille) {
        // Gestion de l'adresse
        Adresse add = new Adresse();
        add.setRue(adresseRue);
        add.setCodePostal(adresseCodePostal);
        add.setVille(adresseVille);

        // Insertion de l'adresse
        daoAuth.insertAdresse(add);

        // Creation d'un objet User
        Utilisateurs util = new Utilisateurs();
        util.setNom(nom);
        util.setPrenom(prenom);
        util.setPseudo(pseudo);
        util.setEmail(email);
        util.setTelephone(telephone);
        util.setMotDePasse(password);
        util.setAdministrateur(administrateur);
        util.setCredit(credit);
        util.setActif(actif);
        util.setAdresse(add);


        // insertion user
        Utilisateurs insertUser = daoAuth.insert(new Utilisateurs());

        // Si je trouve pas => erreur
        // if (loggedUser == null)
        // {
          //  return new ServiceResponse<Utilisateurs>("7025", "Email ou mot de passe invalide");
        // }

        // Success
        return new ServiceResponse<Utilisateurs>("2002", "Insertion réussie", insertUser);}
}