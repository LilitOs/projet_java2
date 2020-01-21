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
}
