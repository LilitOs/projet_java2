package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6425504515677342495L;
	private Carte carte;
	private List<Joueur> joueurs = new ArrayList<Joueur>();
	private int joueurTour = 0;
	private boolean finie = false;

	public Jeu(Carte carte, List<Joueur> joueurs) {
		super();
		this.carte = carte;
		this.joueurs = joueurs;
		repartirTerritoires();
		repartirDes();
	}

	public int getJoueurTour() {
		return joueurTour;
	}

	public boolean isFinie() {
		return finie;
	}

	public void setFinie(boolean finie) {
		this.finie = finie;
	}

	public void passerTour() {
		// A la fin du tour, le joueur gagne autant de dés que le plus grand nombre de territoires contigus
		// sélection des territoires contigus
		List<Territoire> territoiresContigus = new ArrayList<Territoire>();
		Joueur joueurActuel = this.joueurs.get(joueurTour);
		for(Territoire territoire: joueurActuel.getTerritoires()) {
			int[][] voisins = territoire.recupererVoisins();
				for(int i = 0; i < voisins.length; i++) {
				Territoire voisin = this.carte.getTerritoireByCoords(voisins[i][0], voisins[i][1]);
				if(voisin != null && voisin.getJoueur().equals(joueurActuel) && !territoiresContigus.contains(voisin))
					territoiresContigus.add(voisin);
			}
		}
		
		
		// répartition des dés sur les territoires du joueur
		int dicesRemaining = territoiresContigus.size();
		while (dicesRemaining > 0) {
			if(joueurActuel.getTerritoiresDisponibles().size() <= 0) {
				dicesRemaining = 0;
				break;
			}
			Territoire randomTerritory = getRandomTerritoire(joueurActuel.getTerritoiresDisponibles());
			int diceNumber = dicesRemaining < 8 ? dicesRemaining : 8;
			int randomNumber = getRandomNumberInRange(1, diceNumber);
			// vérification si la case n'est pas remplie (max 8 dés par cases)
			if (randomTerritory.getNombreDes() + randomNumber <= 8) {
				dicesRemaining -= randomNumber;
				randomTerritory.addDes(randomNumber);
			}
		}
		do {
			this.joueurTour += 1;
			if(this.joueurTour >= this.joueurs.size()) {
				this.joueurTour = 0;
			}
		}while(this.joueurs.get(this.joueurTour).isEliminer());
	}

	public Carte getCarte() {
		return carte;
	}

	public void setCarte(Carte carte) {
		this.carte = carte;
	}

	private void repartirTerritoires() {
		Territoire[][] territoires = this.carte.getTerritoires();
		List<Joueur> joueursAttribues = new ArrayList<Joueur>();
		// Répartition des territoires pour les joueurs
		while (joueursAttribues.size() != this.joueurs.size()) {
			for (Territoire[] row : territoires) {
				for (Territoire territoire : row) {
					Joueur joueur = joueurTerritoireMin();
					if (territoire != null) {
						territoire.setJoueur(joueur);
						joueur.addTerritoire(territoire);
						if (!joueursAttribues.contains(joueur))
							joueursAttribues.add(joueur);
					}
				}
			}
		}
	}
	
	private Joueur joueurTerritoireMin() {
		Joueur joueurMin = this.joueurs.get(0);
		for(Joueur joueur : this.joueurs) {
			if(joueur.getTerritoires().size() < joueurMin.getTerritoires().size())
				joueurMin = joueur;
		}
		return joueurMin;
	}

	private void repartirDes() {
		// Définition du nombre de dés pour chaque joueur
		int numberDices = this.carte.getNombreTerritoires() / 2;
		// Répartion des dés dans les territoires de chaque joueur
		for (Joueur joueur : this.joueurs) {
			System.out.println("Il y a " + numberDices + " dés par joueurs");
			int dicesRemaining = numberDices;
			// tant qu'il y a des dés a attribués
			while (dicesRemaining > 0) {
				if(joueur.getTerritoiresDisponibles().size() == 0)
					dicesRemaining = 0;
				// sélection d'un territoire aléatoire qui a moins de 8 dés (max)
				Territoire randomTerritory = getRandomTerritoire(joueur.getTerritoiresDisponibles());
				int diceNumber = dicesRemaining < 8 ? dicesRemaining : 8;
				int randomNumber = getRandomNumberInRange(1, diceNumber);
				// vérification si la case n'est pas remplie (max 8 dés par cases)
				if (randomTerritory.getNombreDes() + randomNumber <= 8) {
					dicesRemaining -= randomNumber;
					randomTerritory.addDes(randomNumber);
				}
			}
		}
	}
	
	private static Territoire getRandomTerritoire(List<Territoire> territoires) {
		return territoires.get(new Random().nextInt(territoires.size()));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carte == null) ? 0 : carte.hashCode());
		result = prime * result + joueurTour;
		result = prime * result + ((joueurs == null) ? 0 : joueurs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jeu other = (Jeu) obj;
		if (carte == null) {
			if (other.carte != null)
				return false;
		} else if (!carte.equals(other.carte))
			return false;
		if (joueurTour != other.joueurTour)
			return false;
		if (joueurs == null) {
			if (other.joueurs != null)
				return false;
		} else if (!joueurs.equals(other.joueurs))
			return false;
		return true;
	}
}
