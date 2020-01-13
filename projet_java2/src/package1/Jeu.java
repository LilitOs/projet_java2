package package1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeu {
	private Carte carte;
	private List<Joueur> joueurs = new ArrayList<Joueur>();

	public Jeu(Carte carte, List<Joueur> joueurs) {
		super();
		this.carte = carte;
		this.setJoueurs(joueurs);
		repartirTerritoires();
		repartirDes();
		System.out.println(this.carte.getTerritoires());
	}
	
	private void repartirTerritoires() {
		Territoire[][] territoires = this.carte.getTerritoires();
		List<Joueur> joueursAttribues = new ArrayList<Joueur>();
		// TODO : check si ma condition d'attribution afin que tous les joueurs ait au moins un territoire fonctionne
		// Répartition des territoires pour les joueurs
		while(joueursAttribues.size() != this.joueurs.size()) {
			for(Territoire[] list : territoires) {
				for(Territoire territoire: list) {
					Joueur joueur = getRandomJoueur(this.joueurs);
					if(territoire != null) {
						territoire.setJoueur(joueur);
						if(!joueursAttribues.contains(joueur))
							joueursAttribues.add(joueur);
					}
				}
			}
		}
	}
	
	private void repartirDes() {
		// TODO : génerer le nombre de dés en fonction du nombre de territoires et joueurs
		int numberDices = 20;
		// Répartion des dés dans les territoires de chaque joueur
		for(Joueur joueur: this.joueurs) {
			System.out.println(joueur);
			System.out.println("Il y a " + numberDices + " dés par joueurs");
			List<Territoire> territoiresJoueur = new ArrayList<Territoire>();
			for(Territoire[] list : this.carte.getTerritoires()) {
				for(Territoire territoire: list) {
					if(territoire != null && territoire.getJoueur().getID() == joueur.getID()) {
						territoiresJoueur.add(territoire);
					}
				}
			}
			int dicesRemaining = numberDices - territoiresJoueur.size();
			while(dicesRemaining > 0) {
				Territoire randomTerritory = getRandomTerritoire(territoiresJoueur);
				int diceNumber = dicesRemaining < 8 ? dicesRemaining : 8;
				int randomNumber = getRandomNumberInRange(1, diceNumber);
				// vérification si la case est pas remplie (max 8 dés par cases)
				if(randomTerritory.getNombreDes() + randomNumber <= 8) {
					dicesRemaining -= randomNumber;
					randomTerritory.addDes(randomNumber);
				}
			}
			System.out.println(territoiresJoueur);
		}
	}
	
    private static Joueur getRandomJoueur(List<Joueur> list) 
    { 
        return list.get(new Random().nextInt(list.size()));
    }
    
    private static Territoire getRandomTerritoire(List<Territoire> list) 
    { 
        return list.get(new Random().nextInt(list.size()));
    }
    
    public static int getRandomNumberInRange(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}
	public List<Joueur> getJoueurs() {
		return joueurs;
	}
	public void setJoueurs(List<Joueur> joueurs) {
		this.joueurs = joueurs;
	}
}
