package package1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Partie {
	public static void main(String[] args) {
		List<Joueur> joueurs = genererJoueurs();
		
		Territoire[][] territoires = readFile();
		
		Carte carte = new Carte(territoires);
		Jeu jeu = new Jeu(carte, joueurs);
	}
	
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
	
	public static List<Joueur> genererJoueurs(){
		Scanner sc = new Scanner(System.in);
		
		int nombreJoueurs = sc.nextInt();
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		for(int i = 0; i < nombreJoueurs; i++) {
			Joueur nouveauJoueur = new Joueur();
			joueurs.add(nouveauJoueur);
		}
		return joueurs;
	}
}
