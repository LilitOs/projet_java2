package game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Joueur implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 484936424360772723L;
	private int ID = 1;
	private static int counter = 1;
	private Color couleur;
	private List<Territoire> territoires = new ArrayList<Territoire>();
	private boolean IA = false;
	private int difficulteIA = 1;
	
	public int getDifficulteIA() {
		return difficulteIA;
	}

	public void setDifficulteIA(int difficulteIA) {
		this.difficulteIA = difficulteIA;
	}

	public Joueur(Color couleur) {
		super();
		this.ID = counter++;
		this.couleur = couleur;
	}

	@Override
	public String toString() {
		return "Joueur " + ID + " (" + (isIA() ? "IA" : "Humain") + ")";
	}

	public int getID() {
		return ID;
	}

	public void jouer() {
		
	}
	
	public boolean isEliminer() {
		return this.territoires.size() <= 0;
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
			Joueur attaquant = territoireAttaquant.getJoueur();
			Joueur attaque = territoireAttaquee.getJoueur();
			territoireAttaquee.setJoueur(attaquant);
			attaquant.addTerritoire(territoireAttaquee);
			attaque.removeTerritoire(territoireAttaquee);
			territoireAttaquant.setNombreDes(1);
			System.out.println("Nouveau joueur du territoire" + this + " " + territoireAttaquee.getJoueur());
		}else {
			// Défaite de l'attaquant
			System.out.printf("Défaite du joueur %d\n", this.ID);
			System.out.println("Le nombre de dés sur le territoire de l'attaquant passe à 1");
			System.out.println("Le territoire attaqué reste inchangé");
			territoireAttaquant.setNombreDes(1);
		}
		return new int[]{scoreAttaquant, scoreAttaque};
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

	
	public List<Territoire> getTerritoires() {
		return territoires;
	}
	
	
	//Liste des territoires ayant moins de 8 cases pour l'attribution de dés 
	public List<Territoire> getTerritoiresDisponibles(){
		List<Territoire> territoiresDisponibles = new ArrayList<Territoire>();
		for(Territoire territoire: territoires) {
			if(territoire.getNombreDes() < 8) {
				territoiresDisponibles.add(territoire);
			}
		}
		return territoiresDisponibles;
	}

	public void addTerritoire(Territoire territoire) {
		this.territoires.add(territoire);
	}
	
	public void removeTerritoire(Territoire territoire) {
		this.territoires.remove(territoire);	
	}
	
	public List<Territoire> cloneTerritoires() {
	    List<Territoire> clone = new ArrayList<Territoire>(this.territoires.size());
	    
	    for (Territoire item : this.territoires)
			try {
				clone.add((Territoire) item.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    return clone;
	}

	public boolean isIA() {
		return IA;
	}

	public void setIA(boolean iA) {
		IA = iA;
	}
}
