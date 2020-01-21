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
	private static int nbRows;
	private static int nbCols;

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

			sauvegarde = reponse.toLowerCase().equals("oui") ? true : false;
		}

		Territoire[][] territoires;
		Carte carte = null;
		// lire la sauvegarde
		if(sauvegarde) {
			try {
				lireSauvegarde();
				carte = jeu.getCarte();
				if(jeu.isFinie()) {
					System.out.println("La partie est terminée\nSouhaitez-vous la reprendre ? Tapez Oui / Non");

					String reponse = null;
					do { 
						reponse = sc.nextLine(); 
					} while (!reponse.toLowerCase().equals("oui") && !reponse.toLowerCase().equals("non")); 

					sauvegarde = reponse.toLowerCase().equals("oui") ? true : false;
				}
			} catch (ClassNotFoundException | IOException | ClassCastException e) {
				e.printStackTrace();
				System.out.println("Erreur de sauvegarde");
				supprimerSauvegarde();
				sauvegarde = false;
			}
		}

		// s'il ne souhaite pas reprendre la sauvegarde ou qu'il n'y en a pas : générer des joueurs et une map
		if(!sauvegarde){
			boolean partieContreIA = false;
			String reponse = null;
			System.out.println("Souhaitez-vous jouer contre l'IA ? Tapez Oui / Non");
			do { 
				reponse = sc.nextLine(); 
			} while (!reponse.toLowerCase().equals("oui") && !reponse.toLowerCase().equals("non")); 
			partieContreIA = reponse.toLowerCase().equals("oui") ? true : false;

			List<Joueur> joueurs = genererJoueurs(partieContreIA);
			territoires = genererMap(joueurs.size());

			boolean connexe = false;
			do {
				territoires = genererMap(joueurs.size());
				connexe = verificationConnexes(territoires);
			}while(!connexe);
			carte = new Carte(territoires);
			jeu = new Jeu(carte, joueurs);
		}
		
		sc.close();
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
	
	public static Territoire[][] genererMap(int nbJoueurs) {
		int nbTerritoires = Jeu.getRandomNumberInRange(nbJoueurs * 3 + 1, nbJoueurs * 6 + 1);
		int nbCasesVides = nbTerritoires / 3;

		int N = nbTerritoires + nbCasesVides;
		
		nbRows = (int) Math.floor(Math.sqrt(N));
		nbCols = (int) Math.ceil(N/nbRows);
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
			int randomStartRow = Jeu.getRandomNumberInRange(0, nbRows - 1);
			int randomStartCol = Jeu.getRandomNumberInRange(0, nbCols - 1);

			for(int row = randomStartRow; row < (randomStartRow + nbRows); row++ ) {
				for(int col = randomStartCol; col < (randomStartCol + nbCols); col++ ) {
					if(territoires[row - randomStartRow][col - randomStartCol] == null) {
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

		return territoires;
	}
	
	public static boolean verificationConnexes(Territoire[][] territoires) {
        boolean[][] visited = new boolean[nbRows][nbCols]; 
  
        int count = 0; 
        for (int i = 0; i < nbRows; ++i) {
        	for (int j = 0; j < nbCols; ++j) {
        		if (territoires[i][j] != null && !visited[i][j]){
        			casesConnexes(territoires, i, j, visited); 
        			count++; 
        		} 
        	}
        }        
        return count == 1; 
	}

	public static void casesConnexes(Territoire territoires[][], int row, int col, boolean visited[][]) 
    { 
        int rowVoisines[] = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 }; 
        int colVoisines[] = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 }; 

        visited[row][col] = true; 

        for (int voisin = 0; voisin < 8; voisin++) {
        	if (voisinValide(territoires, row + rowVoisines[voisin], col + colVoisines[voisin], visited)) {
        		casesConnexes(territoires, row + rowVoisines[voisin], col + colVoisines[voisin], visited); 
        	}
        }
    }
	
    public static boolean voisinValide(Territoire territoires[][], int row, int col, 
                   boolean visited[][]) 
    { 
        return (row >= 0) && (row < nbRows) && (col >= 0) && (col < nbCols) && (territoires[row][col] != null && !visited[row][col]); 
    } 

    // Génération des joueurs à partir du chiffre entré dans le scanner
	public static List<Joueur> genererJoueurs(boolean partieContreIA) {
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		int max = 7;
		int min = 2;
		int nombreJoueurs = 5;
		/*
		 * int nombreJoueurs; System.out.println("Entrez le nombre de joueurs : (" + max
		 * + " maximum)"); do { while (!sc.hasNextInt()) sc.next();
		 * System.out.println("Maximum " + max + " joueurs. Réessayez :"); nombreJoueurs
		 * = sc.nextInt(); } while (nombreJoueurs > max && nombreJoueurs < min); sc.nextLine();
				sc.close();
		 */

		for (int i = 0; i < nombreJoueurs; i++) {
			Random random = new Random();
			float hue = random.nextFloat();
			float saturation = (random.nextInt(2000) + 1000) / 10000f;
			float luminance = 0.8f;
			Color color = Color.getHSBColor(hue, saturation, luminance);
			Joueur nouveauJoueur = new Joueur(color);
			if(partieContreIA && i != 0)
				nouveauJoueur.setIA(true);
				
			joueurs.add(nouveauJoueur);
		}
		System.out.println(joueurs);
		return joueurs;
	}

	public static void jouerTour(Carte carte, Joueur joueur) {
		System.out.println("Début du tour du joueur " + joueur.getID());
		System.out.println("Liste de vos territoires " + joueur.getTerritoires());
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
		System.out.printf("Le joueur %d souhaite attaquer avec le territoire %s le territoire %s \n", joueur.getID(), territoireAttaquant, territoireAttaque);
		if(territoireAttaquant.getNombreDes() == 1) {
			throw new Exception("Le territoire ne peut pas attaquer avec 1 dé");
		}
		
		if(!territoireAttaquant.getJoueur().equals(joueur)) {
			throw new Exception("Le territoire attaquant n'appartient pas à l'attaquant, il appartient à " + territoireAttaquant.getJoueur());
		}
		
		if(territoireAttaque.getJoueur().equals(territoireAttaquant.getJoueur())){
			throw new Exception("Le territoire attaqué appartient à l'attaquant");
		}
		
		// Vérification que le territoire attaque est un territoire voisin
		int[][] voisins = territoireAttaque.recupererVoisins();
		
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
	
	public static Joueur verificationFinPartie() {
		Joueur gagnant = null;
		for(Joueur joueur: jeu.getJoueurs()) {
			if(joueur.getTerritoires().size() == jeu.getCarte().getNombreTerritoiresValides()) {
				gagnant = joueur;
				jeu.setFinie(true);
			}
		}
		return gagnant;
	}
}
