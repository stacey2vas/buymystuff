package fr.eni.buymystuff.services;


import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.pattern.Util;

@Service
public class AuthService {

    private final IDAOAuth daoAuth;
    public AuthService(IDAOAuth daoAuth) {
        this.daoAuth = daoAuth;
    }

    @Transactional
    public ServiceResponse<Utilisateurs> ajouterUtilisateur(Utilisateurs utilisateurs) {

        // Insertion de l'adresse
        Adresse insertedAdresse = daoAuth.insertAdresse(utilisateurs.getAdresse());

        // Si ça ne marche pas
        if (insertedAdresse == null) {
            return new ServiceResponse<Utilisateurs>("7024", "L'insertion de l'adresse à échouée");
        }

         utilisateurs.setAdresse(insertedAdresse);
        // insertion user
        Utilisateurs insertUser = daoAuth.insert(utilisateurs);

        // Si ça marche pas
        if (insertUser == null)
         {
           return new ServiceResponse<Utilisateurs>("7025", "L'insertion à échouée");
        }

        // Success
        return new ServiceResponse<Utilisateurs>("2002", "Insertion réussie", insertUser);}

        //Pour recuperer les details de l'utilisateur connecté
        public ServiceResponse<Utilisateurs> getUtilisateurDetail(int id){
            Utilisateurs utilisateurConnecte = daoAuth.selectById(id);

            return ServiceResponse.buildResponse("202","Movie récupéré avec succès", utilisateurConnecte);
        }
        public ServiceResponse<Integer> getIdUserByPseudo(String pseudo) {
       int idUser = daoAuth.findIdByPseudo(pseudo);
       return new ServiceResponse<Integer>("4000", "Film trouvé", idUser);
    }
    public ServiceResponse<Utilisateurs> getUserByPseudo(String pseudo) {
       Utilisateurs user = daoAuth.selectByPseudo(pseudo);
       return new ServiceResponse<Utilisateurs>("4000", "Film trouvé", user);
    }
}