package package1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Partie {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		List<Joueur> joueurs = genererJoueurs();

		Territoire[][] territoires = readFile();
		System.out.println(Arrays.deepToString(territoires));
		for (Territoire[] a : territoires) {
			for (Territoire x : a) {
				System.out.println(x);
			}
		}
		Carte carte = new Carte(territoires);
		Jeu jeu = new Jeu(carte, joueurs);
		System.out.println("Début de la partie : " + joueurs.size() + " joueurs");

		System.out.println(carte);

		new GameBoard(jeu);
		/*
		 * 
		 * JFrame frame = new JFrame("Test");
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.add(new
		 * CartePanel()); frame.pack(); frame.setLocationRelativeTo(null);
		 * frame.setVisible(true);
		 * 
		 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 * System.out.println(screenSize.getHeight() + " " + screenSize.getWidth());
		 * 
		 * System.out.println(carte.toString());
		 */
		/*
		 * for(Joueur joueur: joueurs) { jouerTour(carte, joueur); }
		 */
		sc.close();
	}

	// Lecture du fichier des territoires et création de la matrice des territoires
	public static Territoire[][] readFile() {
		List<String[]> lines = new ArrayList<String[]>();
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader("./territoires.csv"));
			String row;
			while ((row = csvReader.readLine()) != null) {
				lines.add(row.split(";"));
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[][] array = new String[lines.size()][0];
		Territoire territoires[][] = new Territoire[lines.size()][0];
		lines.toArray(array);

		int rowIdx = 0;
		int columnIdx = 0;
		for (String[] row : array) {
			columnIdx = 0;
			territoires[rowIdx] = new Territoire[row.length];
			for (String column : row) {
				if (column.equals("1")) {
					Territoire territoire = new Territoire(rowIdx, columnIdx);
					territoires[rowIdx][columnIdx] = territoire;
				}
				columnIdx++;
			}
			rowIdx++;
		}
		return territoires;
	}

	// Génération des joueurs à partir du chiffre entré dans le scanner
	public static List<Joueur> genererJoueurs() {
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		int max = 7;
		int nombreJoueurs = 5;
		/*
		 * int nombreJoueurs; System.out.println("Entrez le nombre de joueurs : (" + max
		 * + " maximum)"); do { while (!sc.hasNextInt()) sc.next();
		 * System.out.println("Maximum " + max + " joueurs. Réessayez :"); nombreJoueurs
		 * = sc.nextInt(); } while (nombreJoueurs > max); sc.nextLine();
		 */
		List<Color> colorsList = new ArrayList<Color>(List.of(Color.BLUE, Color.CYAN, Color.GRAY,
				Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK));
		
		for (int i = 0; i < nombreJoueurs; i++) {
			Color color = colorsList.get(new Random().nextInt(colorsList.size()));
			Joueur nouveauJoueur = new Joueur(color);
			joueurs.add(nouveauJoueur);
			colorsList.remove(color);
		}
		return joueurs;
	}

	public static void jouerTour(Carte carte, Joueur joueur) {
		System.out.println("Début du tour du joueur " + joueur.getID());
		System.out.println("Liste de vos territoires " + carte.getJoueurTerritoires(joueur));
		System.out.println("Attaquez : (territoire attaquant territoire attaqué)");
		// Vérif si les actions de l'utilisateur ne sont pas interdites
		boolean verification = false;
		do {
			String attaque = sc.nextLine();
			attaque = attaque.replaceAll("[^0-9]", " ");
			List<String> territoiresAttaque = Arrays.asList(attaque.trim().split(" "));
			// Récupération des territoires à partir des chiffres rentrés par l'utilisateur
			Territoire territoireAttaquant = carte.getTerritoireById(Integer.parseInt(territoiresAttaque.get(0)));
			Territoire territoireAttaque = carte.getTerritoireById(Integer.parseInt(territoiresAttaque.get(1)));

			try {
				verificationAttaque(territoireAttaquant, territoireAttaque, joueur);
				// Attaquer
				joueur.attaquer(territoireAttaquant, territoireAttaque);
				verification = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Réessayez :");
			}
		} while (verification != true);
	}

	// Vérification des actions interdites
	public static void verificationAttaque(Territoire territoireAttaquant, Territoire territoireAttaque, Joueur joueur) throws Exception {
		if(!territoireAttaquant.getJoueur().equals(joueur)) {
			throw new Exception("Le territoire attaquant n'appartient pas à l'attaquant");
		}
		
		if(territoireAttaque.getJoueur().equals(territoireAttaquant.getJoueur())){
			throw new Exception("Le territoire attaque appartient à l'attaquant");
		}
				
		int[][] voisins = new int[][] {
			new int[] {territoireAttaque.getRow() - 1, territoireAttaque.getCol() -1},
			new int[] {territoireAttaque.getRow() - 1, territoireAttaque.getCol()},
			new int[] {territoireAttaque.getRow() - 1, territoireAttaque.getCol() + 1},
			new int[] {territoireAttaque.getRow(), territoireAttaque.getCol() - 1},
			new int[] {territoireAttaque.getRow(), territoireAttaque.getCol() + 1},
			new int[] {territoireAttaque.getRow() + 1, territoireAttaque.getCol() - 1},
			new int[] {territoireAttaque.getRow() + 1, territoireAttaque.getCol()},
			new int[] {territoireAttaque.getRow() + 1, territoireAttaque.getCol() + 1},
		};
		
		boolean voisin = false;
		int [] coordsAttaque = new int[] {territoireAttaquant.getRow(), territoireAttaquant.getCol()};
		for(int i = 0; i < voisins.length; i++) {
			if(voisins[i][0] == coordsAttaque[0] && voisins[i][1] == coordsAttaque[1]) {
				voisin = true;
			}
		}
		if(voisin == false) {
			throw new Exception("Le territoire attaqué n'est pas un voisin");
		}
	}
	
	public static boolean verificationFinPartie() {
		return false;
	}
}
