package package1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Carte {
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

	@Override
	public String toString() {
		return "Carte [territoires=" + Arrays.deepToString(territoires) + "]";
	}
	
	public List<Territoire> getJoueurTerritoires(Joueur joueur){
		List<Territoire> joueurTerritoires = new ArrayList<Territoire>();
		for(Territoire[] territoireRow: this.territoires) {
			for(Territoire territoireColumn: territoireRow) {
				if(territoireColumn.getJoueur().equals(joueur))
					joueurTerritoires.add(territoireColumn);
			}
		}
		return joueurTerritoires;
	}
}
