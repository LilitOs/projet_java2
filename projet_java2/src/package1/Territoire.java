package package1;

public class Territoire {
	private int nombreDes = 1;
	private Joueur joueur;
	private int ID = 1;
	private static int counter = 1;


	public Territoire() {
		super();
		this.ID = counter++;
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
	
	public void recupererVoisins() {
		
	}
	
	public void modifierJoueur() {
		
	}
	@Override
	public String toString() {
		return "Territoire n°" + ID + " / " + nombreDes + " dés / " + joueur;
	}
}
