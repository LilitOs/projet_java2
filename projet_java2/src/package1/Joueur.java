package package1;

public class Joueur {
	private int ID = 1;
	private static int counter = 1;

	public Joueur() {
		super();
		this.ID = counter++;
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
	
	public void attaquer (int territoireAttaquant, int territoireAttaquée) {
		System.out.printf("Le joueur %d attaque avec son territoire %d le territoire %d\n", this.ID, territoireAttaquant, territoireAttaquée);
	}
}
