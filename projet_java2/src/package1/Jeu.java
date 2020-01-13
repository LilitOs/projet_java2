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
		this.joueurs = joueurs;
		Territoire[][] territoires = this.carte.getTerritoires();
		List<Joueur> joueursAttribues = new ArrayList<Joueur>();
	    //for (Joueur item : joueurs) joueursAttribues.add((Joueur) item);
	    //joueursAttribues.remove(2);
		int territoiresCount = 0;
		// TODO : check si ma condition d'attribution afin que tous les joueurs ait au moins un territoire fonctionne
		// Répartition des territoires pour les joueurs
		while(joueursAttribues.size() != joueurs.size()) {
			for(Territoire[] list : territoires) {
				for(Territoire territoire: list) {
					Joueur joueur = getRandomJoueur(joueurs);
					if(territoire != null) {
						territoire.setJoueur(joueur);
						territoiresCount++;
						if(!joueursAttribues.contains(joueur))
							joueursAttribues.add(joueur);
					}
				}
			}
		}
		int numberDices = 20;
		// Répartion des dés dans les territoires de chaque joueur
		for(Joueur joueur: joueurs) {
			System.out.println(joueur);
			// int numberDices = (territoiresCount * 3) / joueurs.size();
			// int dicesRemaining = 3
			int dicesRemaining = numberDices;
			System.out.println("Il y a " + numberDices + " dés par joueurs");
			List<Territoire> territoiresJoueur = new ArrayList<Territoire>();
			for(Territoire[] list : territoires) {
				for(Territoire territoire: list) {
					if(territoire != null && territoire.getJoueur().getID() == joueur.getID()) {
						territoiresJoueur.add(territoire);
					}
				}
			}
			/*
			//while(dicesRemaining != 0) {	
				for(Territoire territoire : territoiresJoueur) {
					if(dicesRemaining > 0) {
						int dicesTerritoire = getRandomNumberInRange(1, dicesRemaining < 8 ? (8 - dicesRemaining) : 8);
						dicesRemaining -= dicesTerritoire;
						territoire.setNombreDes(dicesTerritoire);
						System.out.println("Le territoire " + territoire.getID() + " du joueur " + joueur.getID() + " a " + territoire.getNombreDes() + " dés");
					}
				}
			//}
			 */
			while(dicesRemaining != 0) {
				Territoire randomT = getRandomTerritoire(territoiresJoueur);
				System.out.println("dice remaining " + dicesRemaining);
				System.out.println("dice remaining possible " + (dicesRemaining < 8 ? (8 - dicesRemaining) : 8));
				int diceNumber = dicesRemaining < 8 ? (8 - dicesRemaining) : 8;
				int dicesTerritoire = getRandomNumberInRange(1, diceNumber);
				System.out.println("dice choisi " + dicesTerritoire);
				if(randomT.getNombreDes() + dicesTerritoire <= 8)
					dicesRemaining -= diceNumber;
					randomT.addDes(dicesTerritoire);
			}
			System.out.println(territoiresJoueur);
		}
		
		System.out.println(territoires);

	}
    private static Joueur getRandomJoueur(List<Joueur> list) 
    { 
        return list.get(new Random().nextInt(list.size()));
    }
    
    private static Territoire getRandomTerritoire(List<Territoire> list) 
    { 
        return list.get(new Random().nextInt(list.size()));
    }
    
    private static int getRandomNumberInRange(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}
}
