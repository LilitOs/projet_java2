package package1;

import java.util.List;

public class Territoire {
	private int nombreDes;
	private List<Joueur> joueurs;
	
	public int getNombreDes() {
		return nombreDes;
	}
	public void setNombreDes(int nombreDes) {
		this.nombreDes = nombreDes;
	}
	public List<Joueur> getJoueurs() {
		return joueurs;
	}
	
	public void recupererVoisins() {
		
	}
	
	public void modifierJoueur() {
		
	}
	@Override
	public String toString() {
		return "Territoire [nombreDes=" + nombreDes + ", joueurs=" + joueurs + "]";
	}
}
