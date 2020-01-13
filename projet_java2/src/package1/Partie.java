package package1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Partie {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		List<Joueur> joueurs = genererJoueurs();
		
		Territoire[][] territoires = readFile();
		System.out.println(Arrays.deepToString(territoires));
		for(Territoire[] a: territoires) {
			for(Territoire x: a) {
				System.out.println(x);
			}
		}
		Carte carte = new Carte(territoires);
		Jeu jeu = new Jeu(carte, joueurs);
		System.out.println("Début de la partie : " + joueurs.size() + " joueurs");
		System.out.println(carte.toString());
		for(Joueur joueur: joueurs) {
			jouerTour(carte, joueur);
		}
		sc.close();
	}
	
	// Lecture du fichier des territoires et création de la matrice des territoires
	public static Territoire[][] readFile() {
		List<String[]> lines = new ArrayList<String[]>();
		int firstIdx = 0;
		int secondIdx = 0;
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
		for(String[] a: array) {
			secondIdx = 0;
			territoires[firstIdx] = new Territoire[a.length];
			for(String b: a) {
				if(b.equals("1")) {
					Territoire territoire = new Territoire();
					territoires[firstIdx][secondIdx] = territoire;
				}
				secondIdx++;
			}
			firstIdx++;
		}
		return territoires;
	}
	
	// Génération des joueurs à partir du chiffre entré dans le scanner
	public static List<Joueur> genererJoueurs(){
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		int max = 7;
		int nombreJoueurs;
		System.out.println("Entrez le nombre de joueurs : (" + max + " maximum)");
		do {
		    while (!sc.hasNextInt()) sc.next();
		    System.out.println("Maximum " + max + " joueurs. Réessayez :");
		    nombreJoueurs = sc.nextInt();
		} while (nombreJoueurs > max);
		for(int i = 0; i < nombreJoueurs; i++) {
			Joueur nouveauJoueur = new Joueur();
			joueurs.add(nouveauJoueur);
		}
		return joueurs;
	}
	
	public static void jouerTour(Carte carte, Joueur joueur) {
		System.out.println("Début du tour du joueur " + joueur.getID());
		System.out.println("Liste de vos territoires " + carte.getJoueurTerritoires(joueur));
		System.out.println("Attaquez : (territoire attaquant territoire attaqué)");
		sc.nextLine();
		String attaque = sc.nextLine();
	    attaque = attaque.replaceAll("[^0-9]", " "); 
	    List<String> territoiresAttaque = Arrays.asList(attaque.trim().split(" "));
	    joueur.attaquer(Integer.parseInt(territoiresAttaque.get(0)), Integer.parseInt(territoiresAttaque.get(1)));
	}
}
