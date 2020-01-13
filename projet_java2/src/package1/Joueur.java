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
}
