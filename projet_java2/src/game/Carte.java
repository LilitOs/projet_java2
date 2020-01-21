package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Carte implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1589142986449785114L;
	private Territoire[][] territoires;

	public Carte(Territoire[][] territoires) {
		super();
		this.territoires = territoires;
	}

	public Territoire[][] getTerritoires() {
		return territoires;
	}

	public void setTerritoires(Territoire[][] territoires) {
		this.territoires = territoires;
	}
	
	public int getNombreTerritoires() {
		int counter = 0;
		for(Territoire[] row: this.territoires) {
			counter += row.length;
		}
		return counter;
	}
	
	public int getNombreTerritoiresValides() {
		int counter = 0;
		for(Territoire[] row: this.territoires) {
			for(Territoire col: row) {
				if(col != null) {
					counter+= 1;
				}
			}
		}
		return counter;
	}
	
	
	public Territoire getTerritoireById(int id){
		Territoire territoire = null;
		for(Territoire[] territoireRow: this.territoires) {
			for(Territoire territoireColumn: territoireRow) {
				if(territoireColumn != null && territoireColumn.getID() == id)
					territoire = territoireColumn;
			}
		}
		return territoire;
	}
	
	public Territoire getTerritoireByCoords(int row, int col) {
		Territoire territoire = null;
		if(row >= 0 && col >= 0 && row < territoires.length && col < territoires[0].length) {
			territoire = territoires[row][col];
		}
		return territoire;
	}

	@Override
	public String toString() {
		return "Carte [territoires=" + Arrays.deepToString(territoires) + "]";
	}
	
	public List<Territoire> getJoueurTerritoires(Joueur joueur){
		List<Territoire> joueurTerritoires = new ArrayList<Territoire>();
		for(Territoire[] territoireRow: this.territoires) {
			for(Territoire territoireColumn: territoireRow) {
				if(territoireColumn != null && territoireColumn.getJoueur().equals(joueur))
					joueurTerritoires.add(territoireColumn);
			}
		}
		return joueurTerritoires;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(territoires);
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
		Carte other = (Carte) obj;
		if (!Arrays.deepEquals(territoires, other.territoires))
			return false;
		return true;
	}

}
