package game;

import java.io.Serializable;

public class Territoire implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1339536055196507524L;
	private int nombreDes = 1;
	private Joueur joueur;
	private int ID = 1;
	private static int counter = 1;
	private int row;
	private int col;

	public Territoire(int row, int col) {
		super();
		this.ID = counter++;
		this.row = row;
		this.col = col;
	}
		
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getID() {
		return ID;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	
	public int getNombreDes() {
		return nombreDes;
	}
	
	public void setNombreDes(int nombreDes) {
		if(nombreDes <= 8)
			this.nombreDes = nombreDes;
	}
	
	public void addDes(int nombreDes) {
		if(this.nombreDes + nombreDes <= 8)
			this.nombreDes += nombreDes;
	}
	
	public Joueur getJoueur() {
		return joueur;
	}
	
	public int[][] recupererVoisins() {
		return new int[][] {
			new int[] {this.getRow() - 1, this.getCol() -1},
			new int[] {this.getRow() - 1, this.getCol()},
			new int[] {this.getRow() - 1, this.getCol() + 1},
			new int[] {this.getRow(), this.getCol() - 1},
			new int[] {this.getRow(), this.getCol() + 1},
			new int[] {this.getRow() + 1, this.getCol() - 1},
			new int[] {this.getRow() + 1, this.getCol()},
			new int[] {this.getRow() + 1, this.getCol() + 1},
		};
	}
	
	public int lancerDes() {
		int somme =0;
		for(int i =0; i<nombreDes; i++) {
			int randomNbDes = Jeu.getRandomNumberInRange(1, 6);
			somme += randomNbDes;
		} 
		return somme;
	}
	@Override
	public String toString() {
		return "Territoire n°" + ID + " / " + nombreDes + " dés / " + joueur;
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
		Territoire other = (Territoire) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}
