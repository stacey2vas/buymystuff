package fr.eni.buymystuff;

import fr.eni.buymystuff.bo.Adresse;
import fr.eni.buymystuff.bo.Utilisateurs;
import fr.eni.buymystuff.dao.IDAOAuth;
import fr.eni.buymystuff.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BuymystuffApplicationTests {

	@Autowired
	IDAOAuth auth;

	@Test
	void contextLoads() {
		Utilisateurs util = new Utilisateurs();
		util.setId(1);
		util.setNom("Terrieur");
		util.setPrenom("Alain");
		util.setPseudo("toto");
		util.setEmail("aterrieur@eni.fr");
		util.setTelephone("0606060606");
		util.setMotDePasse("toto");
		util.setAdministrateur(false);
		util.setCredit(100);
		util.setActif(true);
		Adresse add = new Adresse();
		add.setId(1);
		util.setAdresse(add);

		auth.insert(util);

	}

}
