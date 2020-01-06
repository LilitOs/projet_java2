package package1;

import java.util.ArrayList;
import java.util.List;

public class Jeu {
	private Carte carte;
	private List<Joueur> joueurs = new ArrayList<Joueur>();

	public Jeu(Carte carte, List<Joueur> joueurs) {
		super();
		this.carte = carte;
		this.joueurs = joueurs;
	}
}
