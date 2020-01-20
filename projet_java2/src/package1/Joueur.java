package package1;

import java.awt.Color;

public class Joueur {
	private int ID = 1;
	private static int counter = 1;
	private Color couleur;

	public Joueur(Color couleur) {
		super();
		this.ID = counter++;
		this.couleur = couleur;
	}

	@Override
	public String toString() {
		return "Joueur " + ID;
	}

	public int getID() {
		return ID;
	}

	public void jouer() {
		
	}
	
	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public int[] attaquer (Territoire territoireAttaquant, Territoire territoireAttaquee) {
		System.out.printf("Le joueur %d attaque avec son territoire %d le territoire %d\n", this.ID, territoireAttaquant.getID(), territoireAttaquee.getID());
		// Lancer les dés pour les 2 territoires
		int scoreAttaquant = territoireAttaquant.lancerDes();
		int scoreAttaque = territoireAttaquee.lancerDes();
		System.out.printf("Score de l'attaquant : %d\n", scoreAttaquant);
		System.out.printf("Score de l'attaqué : %d\n", scoreAttaque);
		System.out.println(territoireAttaquant.getNombreDes());
		System.out.println(territoireAttaquee.getNombreDes());
		if(scoreAttaquant > scoreAttaque) {
			// Victoire de l'attaquant
			System.out.printf("Victoire du joueur %d\n", this.ID);
			int nombreDes = territoireAttaquant.getNombreDes() - 1;
			System.out.printf("Le joueur remporte le territoire attaqué et le nombre de dés passe à %d\n", nombreDes);
			System.out.println("Le nombre de dés sur le territoire attaquant passe à 1");
			territoireAttaquee.setNombreDes(nombreDes);
			territoireAttaquee.setJoueur(territoireAttaquant.getJoueur());
			territoireAttaquant.setNombreDes(1);
			System.out.println("Nouveau joueur du territoire" + this + " " + territoireAttaquee.getJoueur());
		}else {
			// Défaite de l'attaquant
			System.out.printf("Défaite du joueur %d\n", this.ID);
			System.out.println("Le nombre de dés sur le territoire de l'attaquant passe à 1");
			System.out.println("Le territoire attaqué reste inchangé");
			territoireAttaquant.setNombreDes(1);
		}
		int[] scores = new int[2];
		scores[0] = scoreAttaquant;
		scores[1] = scoreAttaque;
		return scores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
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
		Joueur other = (Joueur) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	public String displayTour() {
		return "C'est au tour du " + this;
	}
}
