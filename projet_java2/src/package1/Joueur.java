package package1;

public class Joueur {
	private static int ID = 0;
	
	public int getID() {
		return ID;
	}

	public void jouer() {
		
	}

	public Joueur() {
		super();
		this.incrementID();
	}
	
	public void incrementID() {
		ID += 1;
	}

	@Override
	public String toString() {
		return "Joueur " + ID;
	}
}
