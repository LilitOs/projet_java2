package package1;

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
}
