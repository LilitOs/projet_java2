package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Partie {
	private static Scanner sc = new Scanner(System.in);
	private static Jeu jeu;
	private static String filepath = "./sauvegarde.json";

	public static void main(String[] args) {		
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
		boolean sauvegarde = false;

		// Si une sauvegarde existe, proposer de la reprendre
		if(new File("./sauvegarde.json").exists()) {
			System.out.println("Une sauvegarde a été trouvée\nSouhaitez-vous la reprendre ? Tapez Oui / Non");

			String reponse = null;
			do { 
				reponse = sc.nextLine(); 
			} while (!reponse.toLowerCase().equals("oui") && !reponse.toLowerCase().equals("non")); 

			sc.close();
			sauvegarde = reponse.toLowerCase().equals("oui") ? true : false;
		}

		Territoire[][] territoires;
		Carte carte = null;
		if(sauvegarde) {
			try {
				lireSauvegarde();
				carte = jeu.getCarte();
			} catch (ClassNotFoundException | IOException | ClassCastException e) {
				e.printStackTrace();
				System.out.println("Erreur de sauvegarde");
				supprimerSauvegarde();
				sauvegarde = false;
			}
		}

		if(!sauvegarde){
			List<Joueur> joueurs = genererJoueurs();
			territoires = genererMap(joueurs.size());
			carte = new Carte(territoires);
			jeu = new Jeu(carte, joueurs);
		}

		new GameBoard(jeu);
	}

	public static void sauvegarder() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(filepath);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(jeu);
		objectOut.close();
	}

	public static void lireSauvegarde() throws IOException, ClassNotFoundException, ClassCastException {
		FileInputStream fileIn = new FileInputStream(filepath);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		jeu = (Jeu) objectIn.readObject();
		objectIn.close();
	}

	public static void supprimerSauvegarde() {
		new File(filepath).delete();
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
	
	public static Territoire[][] genererMap(int nbJoueurs) {
		int nbTerritoires = Jeu.getRandomNumberInRange(nbJoueurs * 3 + 1, nbJoueurs * 6 + 1);
		int nbCasesVides = nbTerritoires / 3;

		int N = nbTerritoires + nbCasesVides;
		int nbRows = (int) Math.floor(Math.sqrt(N));
		int nbCols = (int) Math.ceil(N/nbRows);
		if(nbCols * nbRows != N)
		    nbCols += 1;
		
		Territoire territoires[][] = new Territoire[nbRows][0];

		int rowIdx = 0;
		int columnIdx = 0;
		for (int row = 0; row < nbRows; row++) {
			columnIdx = 0;
			territoires[rowIdx] = new Territoire[nbCols];
			for (int column = 0; column < nbCols; column++) {
				if (new Random().nextBoolean() && nbTerritoires > 0) {
					System.out.println("new Territoire");
					Territoire territoire = new Territoire(rowIdx, columnIdx);
					territoires[rowIdx][columnIdx] = territoire;
					nbTerritoires--;
				}
				columnIdx++;
			}
			rowIdx++;
		}

		// S'il y a encore des territoires non présents sur la map
		while(nbTerritoires > 0) {
			boolean condition = false;
			System.out.println("while begin");
			int randomStartRow = Jeu.getRandomNumberInRange(0, nbRows - 1);
			int randomStartCol = Jeu.getRandomNumberInRange(0, nbCols - 1);

			for(int row = randomStartRow; row < (randomStartRow + nbRows); row++ ) {
				for(int col = randomStartCol; col < (randomStartCol + nbCols); col++ ) {
					if(territoires[row - randomStartRow][col - randomStartCol] == null) {
						System.out.println("new territoire after");
						Territoire territoire = new Territoire(row - randomStartRow, col - randomStartCol);
						territoires[row - randomStartRow][col - randomStartCol] = territoire;
						nbTerritoires--;
						condition = true;
						break;
					}
				}
				if(condition == true){
					break;      
				}
			}
		}
		
		// TODO : vérification qu'il n'y a aucun territoire sans voisins

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
				sc.close();
		 */

		for (int i = 0; i < nombreJoueurs; i++) {
			int R = (int)(Math.random()*256);
			int G = (int)(Math.random()*256);
			int B = (int)(Math.random()*256);
			Color color = new Color(R, G, B);
			Joueur nouveauJoueur = new Joueur(color);
			joueurs.add(nouveauJoueur);
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
		
		// Vérification que le territoire attaque est un territoire voisin
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
