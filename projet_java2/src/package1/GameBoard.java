package package1;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class GameBoard {

	private Jeu jeu = null;
	private Territoire territoireAttaquant = null;
	private Territoire territoireAttaque = null;
	private Cell[][] cells;
	private JTextArea scoreLabel;
	private JLabel tourLabel;
	private JButton finTourBouton;
	
	public static void main(String[] args, Jeu jeu) {
		new GameBoard(jeu);
	}

	public GameBoard(Jeu jeu) {
		this.jeu = jeu;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
				}

				JFrame frame = new JFrame("Dice Wars");
				
				
			    JTextArea scoreLabel = new JTextArea("Score");
			    scoreLabel.setWrapStyleWord(true);
			    scoreLabel.setLineWrap(true);
			    scoreLabel.setEditable(false);
			    scoreLabel.setVisible(true);
			    scoreLabel.setSize(200, 30);

				tourLabel = new JLabel(jeu.getJoueurs().get(jeu.getJoueurTour()).displayTour());
				tourLabel.setForeground(jeu.getJoueurs().get(jeu.getJoueurTour()).getCouleur());


				finTourBouton = new JButton("Fin du tour");
				//finTourBouton.setHorizontalAlignment(SwingConstants.CENTER);
				//finTourBouton.setVerticalAlignment(SwingConstants.TOP);
				finTourBouton.setSize(200, 50);
				finTourBouton.setVisible(true);
				// finTourBouton.setLayout(null);
				// finTourBouton.setBounds(50, 250, 100, 50);
		        finTourBouton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jeu.passerTour();
						tourLabel.setText(jeu.getJoueurs().get(jeu.getJoueurTour()).displayTour());
						tourLabel.setForeground(jeu.getJoueurs().get(jeu.getJoueurTour()).getCouleur());
					}
				});
		        
			    JPanel textpanel = new JPanel(new GridLayout(3,1));
			    textpanel.add(scoreLabel);
			    textpanel.add(tourLabel);
			    textpanel.add(finTourBouton);
			    textpanel.setSize(200,300);
			    textpanel.setVisible(true);
			    
			    frame.add(textpanel);
			    
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				/*
				scoreLabel = new JLabel("Score");
				scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
				scoreLabel.setVerticalAlignment(SwingConstants.BOTTOM);
				scoreLabel.setSize(300, 100);
				scoreLabel.setVisible(true);
				frame.add(scoreLabel);
				
				tourLabel = new JLabel(jeu.getJoueurs().get(jeu.getJoueurTour()).displayTour());
				tourLabel.setForeground(jeu.getJoueurs().get(jeu.getJoueurTour()).getCouleur());
				tourLabel.setHorizontalAlignment(SwingConstants.CENTER);
				tourLabel.setVerticalAlignment(SwingConstants.TOP);
				tourLabel.setSize(300, 50);
				tourLabel.setVisible(true);
				frame.add(tourLabel);
				 * 
				 */
				
				
				//frame.add(finTourBouton);
				
				frame.add(new GameBoard.GamePanel());
				frame.setBackground(Color.WHITE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				/*
				while(Partie.verificationFinPartie() != true) {
					System.out.println("jouer tour");
					
				}
				*/
			}
		});
	}

	public class GamePanel extends JPanel {

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
				public void actionPerformed(ActionEvent e) {
			    	System.out.println("terroite attaque " + territoire);
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
						System.out.println("condition terr " + !territoire.getJoueur().equals(jeu.getJoueurs().get(jeu.getJoueurTour())));
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
						System.out.println("TERROITRE ATTAQUE 1" + territoireAttaque);

						territoireAttaque = territoire;
						System.out.println("TERROITRE ATTAQUE 2" + territoireAttaque);
						try {
							Partie.verificationAttaque(territoireAttaquant, territoireAttaque, jeu.getJoueurs().get(jeu.getJoueurTour()));
					        setBackground(Color.GREEN);			    		
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
						
						int[] scores = territoire.getJoueur().attaquer(territoireAttaquant, territoireAttaque);
						System.out.println("scores " + scores[0] + " " + scores[1]);
												
						Cell cellAttaque = cells[territoireAttaque.getRow()][territoireAttaque.getCol()];
						cellAttaque.setText(String.valueOf(territoireAttaque.getNombreDes()));
						cellAttaque.setBackground(territoireAttaque.getJoueur().getCouleur());
						cellAttaque.setBorder(new LineBorder(Color.BLACK, 1));
						
						Cell cellAttaquant = cells[territoireAttaquant.getRow()][territoireAttaquant.getCol()];
						cellAttaquant.setText(String.valueOf(territoireAttaquant.getNombreDes()));
						cellAttaquant.setBackground(territoireAttaquant.getJoueur().getCouleur());
						cellAttaquant.setBorder(new LineBorder(Color.BLACK, 1));
						
						scoreLabel.setText(displayScore(scores));
						
						territoireAttaquant = null;
						territoireAttaque = null;
					}
				}
			});
			
			this.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    	if(territoire != null && territoireAttaquant != null && !territoire.equals(territoireAttaquant)) {
			    		try {
							Partie.verificationAttaque(territoireAttaquant, territoire, jeu.getJoueurs().get(jeu.getJoueurTour()));
					        setBorder(new LineBorder(Color.BLACK, 3));
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	}
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
				    if(territoire != null && territoireAttaquant != null && !territoire.equals(territoireAttaquant))
			    		setBorder(new LineBorder(Color.BLACK, 1));
			    }
			});
		}
		
		public String displayScore(int[] scores) {
			return "Scores \nAttaquant : " + String.valueOf(scores[0]) +
					"\nAttaqué : " + 
					String.valueOf(scores[1]) +
					"\nVictoire de " + (scores[0] > scores[1] ? "l'attaquant" : "l'attaqué");
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(WIDTH, LENGTH);
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
				System.out.println(prefSize);
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