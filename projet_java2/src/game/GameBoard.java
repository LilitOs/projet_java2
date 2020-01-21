package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

public class GameBoard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8939960145473623355L;
	private Jeu jeu = null;
	private Territoire territoireAttaquant = null;
	private Territoire territoireAttaque = null;
	private Cell[][] cells;
	private JLabel scoreLabel;
	private JLabel tourLabel;
	private JButton finTourBouton;
	
	public static void main(String[] args, Jeu jeu) {
		new GameBoard(jeu);
	}
 
	public GameBoard(Jeu jeu) {
		this.jeu = jeu;
		
		setTitle("Dice Wars");

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				initUI();
			}
		});
	}
	
	private void initUI() {

	    this.scoreLabel = new JLabel("Score");
	    this.scoreLabel.setVisible(true);
	    this.scoreLabel.setSize(200, 30);

	    this.tourLabel = new JLabel(jeu.getJoueurs().get(jeu.getJoueurTour()).displayTour());
	    this.tourLabel.setForeground(jeu.getJoueurs().get(jeu.getJoueurTour()).getCouleur());

	    this.finTourBouton = new JButton("Fin du tour");
		
	    this.finTourBouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initUIFinTour();
			}
        });
        
	    JPanel textpanel = new JPanel(new GridLayout(3,1));
	    textpanel.add(scoreLabel);
	    textpanel.add(tourLabel);
	    textpanel.add(finTourBouton);
	    textpanel.setSize(300,300);
	    textpanel.setVisible(true);
	    			    
	    add(textpanel);
	    
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(new GameBoard.GamePanel());
		setBackground(Color.WHITE);
		pack();
		
		// confirmation pour sauvegarde avant fermeture
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String[] buttonLabels = new String[] {"Yes", "No", "Cancel"};
		        String defaultOption = buttonLabels[0];
		        Icon icon = null;
		         
		        int answer = JOptionPane.showOptionDialog(new Frame(),
		                "Souhaitez-vous sauvegarder la partie avant de quitter ?",
		                "Warning",
		                JOptionPane.YES_NO_CANCEL_OPTION,
		                JOptionPane.WARNING_MESSAGE,
		                icon,
		                buttonLabels,
		                defaultOption);  
		        
				switch (answer) {
				case JOptionPane.YES_OPTION:
					System.out.println("Save and Quit");
					try {
						Partie.sauvegarder();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
					break;

				case JOptionPane.NO_OPTION:
					System.out.println("Don't Save and Quit");
					Partie.supprimerSauvegarde();
					System.exit(0);
					break;

				case JOptionPane.CANCEL_OPTION:
					System.out.println("Don't Quit");
					break;
				}
			}
        });
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void initUIFinTour() {
		Joueur joueurFinTour = jeu.getJoueurs().get(jeu.getJoueurTour());
		jeu.passerTour();
		for(Territoire territoire: joueurFinTour.getTerritoires())
			updateCell(territoire);
		Joueur joueurNouveauTour = jeu.getJoueurs().get(jeu.getJoueurTour());
		tourLabel.setText(joueurNouveauTour.displayTour());
		tourLabel.setForeground(joueurNouveauTour.getCouleur());
		if(joueurNouveauTour.isIA()) {
			jouerTourIA(joueurNouveauTour);
		}
	}

	public void jouerTourIA(Joueur joueurNouveauTour) {	
		List<Territoire> territoiresJoueurs = jeu.getCarte().getJoueurTerritoires(joueurNouveauTour);
		
		// Si IA niveau normal, faire un tri des dés pour commencer à attaquer avec les meilleurs territoires
		if(joueurNouveauTour.getDifficulteIA() == Joueur.NIVEAU_NORMAL) {
			Collections.sort(territoiresJoueurs, new Comparator<Territoire>(){
			    public int compare(Territoire t1, Territoire t2) {
			        return t2.getNombreDes() - t1.getNombreDes();
			    }
			});
		}
		
		// compteur du nombre de territoires du joueur
		int nbTerritoires = territoiresJoueurs.size();
		// boucle sur tous les territoires du joueur
		for(int i = 0; i < nbTerritoires; i++) {
			Territoire territoireJoueur = jeu.getCarte().getJoueurTerritoires(joueurNouveauTour).get(i);
			// n'attaquer que si le territoire a plus d'1 dé
			if(territoireJoueur.getNombreDes() > 1) {
				Territoire territoireAttaque = null;
				int probabiliteAttaque = 0;
				if(joueurNouveauTour.getDifficulteIA() == Joueur.NIVEAU_NORMAL) {
					// boucle sur les territoires voisins
					for(int[] coordsVoisin : territoireJoueur.recupererVoisins()) {
						Territoire territoireVoisin = jeu.getCarte().getTerritoireByCoords(coordsVoisin[0], coordsVoisin[1]);
						if(territoireVoisin != null && !territoireVoisin.getJoueur().equals(joueurNouveauTour)) {
							// calcul probabilité
							int victoire = territoireJoueur.calculerProbabilitesAttaque(territoireVoisin);
							// comparaison de la nouvelle probabilité avec l'ancienne
							if(victoire > probabiliteAttaque && victoire > 20) {
								territoireAttaque = territoireVoisin;
								probabiliteAttaque = victoire;
							}
							// si la probabilité est de 100, on peut directement attaquer
							if(victoire == 100)
								break;
						}
					}
					try {
						Partie.verificationAttaque(territoireJoueur, territoireAttaque, joueurNouveauTour);
						// Attaquer
						int[] scores = territoireJoueur.getJoueur().attaquer(territoireJoueur, territoireAttaque);

						if(scores[0] > scores[1]) {
							// ajoute 1 au compteur du nombre de territoires du joueur car a il gagné
							nbTerritoires++;
						}

						updateUIFinTour(territoireJoueur, territoireAttaque, scores);
					} catch (Exception ex) {
						System.out.println(ex);
					}

				}else {
					// boucle sur les territoires voisins
					for(int[] coordsVoisin : territoireJoueur.recupererVoisins()) {
						Territoire territoireVoisin = jeu.getCarte().getTerritoireByCoords(coordsVoisin[0], coordsVoisin[1]);
						if(territoireVoisin != null) {
							try {
								Partie.verificationAttaque(territoireJoueur, territoireVoisin, joueurNouveauTour);
								// Attaquer
								int[] scores = territoireJoueur.getJoueur().attaquer(territoireJoueur, territoireVoisin);

								if(scores[0] > scores[1]) {
									// ajoute 1 au compteur du nombre de territoires du joueur car a il gagné
									nbTerritoires++;
								}

								updateUIFinTour(territoireJoueur, territoireVoisin, scores);
							} catch (Exception ex) {
								System.out.println(ex);
							}
						}
					}
				}
			}
		}

		Joueur gagnant = Partie.verificationFinPartie();
		if(gagnant != null) {
			updateUIFinJeu(gagnant);
		}

		finTourBouton.doClick();
	}

	public void updateUIFinTour(Territoire territoireAttaquant, Territoire territoireAttaque, int[] scores) {

		// mise à jour de la case attaquant
		updateCell(territoireAttaquant);

		// mise à jour de la case attaquée
		updateCell(territoireAttaque);

		this.scoreLabel.setText(displayScore(scores));
		Joueur gagnant = Partie.verificationFinPartie();
		if(gagnant != null) {
			updateUIFinJeu(gagnant);
		}

		this.territoireAttaquant = null;
		this.territoireAttaque = null;
	}
	
	public void updateUIFinJeu(Joueur gagnant) {
		tourLabel.setText("Victoire du " + gagnant);
		finTourBouton.setEnabled(false);
		for(Cell[] row: cells) {
			for(Cell col: row) {
				col.setEnabled(false);
			}
		}
	}
	
	public void updateCell(Territoire territoire) {
		Cell cell = cells[territoire.getRow()][territoire.getCol()];
		cell.setText(String.valueOf(territoire.getNombreDes()));
		cell.setBackground(territoire.getJoueur().getCouleur());
		cell.setBorder(new LineBorder(Color.BLACK, 1));
	}

	public String displayScore(int[] scores) {
		return "<html>Scores <br>Attaquant : " + String.valueOf(scores[0]) +
				"<br>Attaqué : " + 
				String.valueOf(scores[1]) +
				"<br>Victoire de " + (scores[0] > scores[1] ? "l'attaquant" : "l'attaqué</html>");
	}

	public class GamePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -161324795144823890L;

		public GamePanel() {
			setLayout(new ChessBoardLayoutManager());
			int row = 0;
			cells = new Cell[jeu.getCarte().getTerritoires().length][0];
			for (Territoire[] territoireRow : jeu.getCarte().getTerritoires()) {
				int col = 0;
				cells[row] = new Cell[territoireRow.length];

				for (Territoire territoireColumn : territoireRow) {
					Cell cell = new GameBoard.Cell(row, col, territoireColumn);
					add(cell, new Point(col, row));
					cells[row][col] = cell;
					col++;
				}
				row++;
			}
		}
	}

	public class Cell extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1118848311880753370L;
		private static final int SIDE_LENGTH = 50;
		private static final int WIDTH = 25;
		private static final int LENGTH = 25;

		private int row = 0;
		private int column = 0;
		private Color background = null;
		private int nombreDes = 0;

		public Cell(int row, int column, Territoire territoire) {
			this.row = row;
			this.column = column;
			if (territoire != null) {
				this.background = territoire.getJoueur().getCouleur();
				this.nombreDes = territoire.getNombreDes();
			}

			setContentAreaFilled(false);
			setBorder(new LineBorder(Color.BLACK, 1));
			setBackground(background);
			setOpaque(background != null);
			if (this.nombreDes > 0)
				setText(String.valueOf(this.nombreDes));
			setForeground(Color.WHITE);

			// Au clique sur un territoire
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			    	initUITerritoire(territoire);
				}
			});
			
			this.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	if(territoire != null && territoireAttaquant != null && !territoire.equals(territoireAttaquant)) {
			    		try {
							Partie.verificationAttaque(territoireAttaquant, territoire, jeu.getJoueurs().get(jeu.getJoueurTour()));
					        setBorder(new LineBorder(Color.BLACK, 3));
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
			    	}
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
				    if(territoire != null && territoireAttaquant != null && !territoire.equals(territoireAttaquant))
			    		setBorder(new LineBorder(Color.BLACK, 1));
			    }
			});
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(WIDTH, LENGTH);
		}
		
		public void initUITerritoire(Territoire territoire) {
			if(territoire == null) {
				return;
			}
			
			// Si clique sur le même territoire que le territoire attaquant, le déselectionner
			if(territoire == territoireAttaquant) {
				territoireAttaquant = null;
				setBorder(new LineBorder(Color.BLACK, 1));
				return;
			}
			
			// Si 1er clique : sélection du territoire attaquant
			if(territoireAttaquant == null) {
				// Ne rien faire si le territoire n'est pas celui du joueur du tour courant
				if(!territoire.getJoueur().equals(jeu.getJoueurs().get(jeu.getJoueurTour()))) {
					return;
				}
				
				// Un territoire à un dé ne peut pas attaquer
				if(territoire.getNombreDes() == 1)
					return;
				territoireAttaquant = territoire;
				setBorder(new LineBorder(Color.BLACK, 5));
			}else {
				// Sinon : sélection du territoire à attaquer
				territoireAttaque = territoire;
				try {
					Partie.verificationAttaque(territoireAttaquant, territoireAttaque, jeu.getJoueurs().get(jeu.getJoueurTour()));
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					return;
				}
				
				int[] scores = territoire.getJoueur().attaquer(territoireAttaquant, territoireAttaque);
							
				updateUIFinTour(territoireAttaquant, territoireAttaque, scores);
				
				Joueur gagnant = Partie.verificationFinPartie();
				if(gagnant != null) {
					updateUIFinJeu(gagnant);
				}
			}
		}

		/*
		 * @Override public void paintComponent(Graphics g) { super.paintComponent(g);
		 * Polygon hex = new Polygon(); for (int i = 0; i < 6; i++) { hex.addPoint((int)
		 * (50 + SIDE_LENGTH * Math.cos(i * 2 * Math.PI / 6)), (int) (50 + SIDE_LENGTH *
		 * Math.sin(i * 2 * Math.PI / 6))); }
		 * 
		 * g.fillPolygon(hex);
		 * 
		 * //g.setColor(new Color(0xFF, 0xFF, 0xFF)); //g.fillRect(0,0,1000,1000);
		 * g.setColor(color == null ? null : color);
		 * 
		 * if(this.nombreDes > 0) g.drawString(String.valueOf(this.nombreDes), 1,1); }
		 */
	}

	public class ChessBoardLayoutManager implements LayoutManager2 {

		private Map<Point, Component> mapComps;

		public ChessBoardLayoutManager() {
			mapComps = new HashMap<>(25);
		}

		@Override
		public void addLayoutComponent(Component comp, Object constraints) {
			if (constraints instanceof Point) {

				mapComps.put((Point) constraints, comp);

			} else {

				throw new IllegalArgumentException("ChessBoard constraints must be a Point");

			}
		}

		@Override
		public Dimension maximumLayoutSize(Container target) {
			return preferredLayoutSize(target);
		}

		@Override
		public float getLayoutAlignmentX(Container target) {
			return 0.5f;
		}

		@Override
		public float getLayoutAlignmentY(Container target) {
			return 0.5f;
		}

		@Override
		public void invalidateLayout(Container target) {
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			Point[] keys = mapComps.keySet().toArray(new Point[mapComps.size()]);
			for (Point p : keys) {
				if (mapComps.get(p).equals(comp)) {
					mapComps.remove(p);
					break;
				}
			}
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new CellGrid(mapComps).getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		@Override
		public void layoutContainer(Container parent) {
			int width = parent.getWidth();
			int height = parent.getHeight();

			int gridSize = Math.min(width, height);

			CellGrid grid = new CellGrid(mapComps);
			int rowCount = grid.getRowCount();
			int columnCount = grid.getColumnCount();

			int cellSize = gridSize / Math.max(rowCount, columnCount);

			int xOffset = (width - (cellSize * columnCount)) / 2;
			int yOffset = (height - (cellSize * rowCount)) / 2;

			Map<Integer, List<CellGrid.Cell>> cellRows = grid.getCellRows();
			for (Integer row : cellRows.keySet()) {
				List<CellGrid.Cell> rows = cellRows.get(row);
				for (CellGrid.Cell cell : rows) {
					Point p = cell.getPoint();
					Component comp = cell.getComponent();

					int x = xOffset + (p.x * cellSize);
					int y = yOffset + (p.y * cellSize);

					comp.setLocation(x, y);
					comp.setSize(cellSize, cellSize);

				}
			}

		}

		public class CellGrid {

			private Dimension prefSize;
			private int cellWidth;
			private int cellHeight;

			private Map<Integer, List<Cell>> mapRows;
			private Map<Integer, List<Cell>> mapCols;

			public CellGrid(Map<Point, Component> mapComps) {
				mapRows = new HashMap<>(25);
				mapCols = new HashMap<>(25);
				for (Point p : mapComps.keySet()) {
					int row = p.y;
					int col = p.x;
					List<Cell> rows = mapRows.get(row);
					List<Cell> cols = mapCols.get(col);
					if (rows == null) {
						rows = new ArrayList<>(25);
						mapRows.put(row, rows);
					}
					if (cols == null) {
						cols = new ArrayList<>(25);
						mapCols.put(col, cols);
					}
					Cell cell = new Cell(p, mapComps.get(p));
					rows.add(cell);
					cols.add(cell);
				}

				int rowCount = mapRows.size();
				int colCount = mapCols.size();

				cellWidth = 0;
				cellHeight = 0;

				for (List<Cell> comps : mapRows.values()) {
					for (Cell cell : comps) {
						Component comp = cell.getComponent();
						cellWidth = Math.max(cellWidth, comp.getPreferredSize().width);
						cellHeight = Math.max(cellHeight, comp.getPreferredSize().height);
					}
				}

				int cellSize = Math.max(cellHeight, cellWidth);

				prefSize = new Dimension(cellSize * colCount, cellSize * rowCount);
			}

			public int getRowCount() {
				return getCellRows().size();
			}

			public int getColumnCount() {
				return getCellColumns().size();
			}

			public Map<Integer, List<Cell>> getCellColumns() {
				return mapCols;
			}

			public Map<Integer, List<Cell>> getCellRows() {
				return mapRows;
			}

			public Dimension getPreferredSize() {
				return prefSize;
			}

			public int getCellHeight() {
				return cellHeight;
			}

			public int getCellWidth() {
				return cellWidth;
			}

			public class Cell {

				private Point point;
				private Component component;

				public Cell(Point p, Component comp) {
					this.point = p;
					this.component = comp;
				}

				public Point getPoint() {
					return point;
				}

				public Component getComponent() {
					return component;
				}

			}

		}
	}
}