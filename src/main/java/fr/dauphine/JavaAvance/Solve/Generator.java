package fr.dauphine.JavaAvance.Solve;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.GUI.GridFiles;
import fr.dauphine.JavaAvance.GUI.DisplayUnicode;
import fr.dauphine.JavaAvance.GUI.GUI;

/**
 * Solvable grid generator
 */
public class Generator {
	// Shouldn't be instanciated
	private Generator() {
	}

	private static boolean verbose = false;
	private static GUI gui = null;

	/**
	 * Controller for generate option. Generates a random solvable grid and save it to a file
	 *
	 * @param width the width of the grid
	 * @param height the height of the grid
	 * @param nbcc number of connected components/regions (-1 to ignore, positive and < width*length/2 otherwise)
	 * @param connectivity higher for more connexions, between 0 and 1
	 * @param outputFilePath the path of the file where the grid will be saved
	 * @param verbose true if should print more info in console
	 * @param display true if should display result and progress in GUI
	 */
	public static void generateLevel(int width, int height, int nbcc, double connectivity, String outputFilePath, boolean verbose, boolean display) {
		Generator.verbose = verbose;

		// Create a grid
		Grid grid = new Grid(width, height);

		// Create display
		if(display) {
			gui = new GUI(grid);
			gui.start();
		}

		// Fill it with random pieces in a valid pattern
		if(nbcc < 0) {
			fillRandomPieces(grid, connectivity);
			if(verbose){System.out.println("Generated solved grid\n");grid.print();}
		}
		else {
			int currentNbcc = fillRandomPiecesWithConnectedComponents(grid, nbcc, connectivity);
			if(verbose){System.out.println("Generated solved grid with "+currentNbcc+" connected components\n");grid.print();}
		}

		// Shuffle the pieces
		shufflePiecesOrientation(grid);
		if(verbose){System.out.println("Generated shuffled solvable grid\n");grid.print();}

		try {
			// Save
      String fileFormatStringForGrid = GridFiles.serializeGrid(grid, outputFilePath);
			//if(verbose){System.out.println("Printed following serialization for grid to file\n"+fileFormatStringForGrid);}
		} catch (IOException e) {
			System.err.println("Error saving file : " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Generate for an interactive interface. Generates a random solvable grid
	 *
	 * @param gui the interface that should be updated
	 * @param width the width of the grid
	 * @param height the height of the grid
	 * @param nbcc number of connected components/regions (-1 to ignore, positive and < width*length/2 otherwise)
	 * @param connectivity higher for more connexions, between 0 and 1
	 */
	public static void generateLevelForGui(GUI gui, int width, int height, int nbcc, double connectivity) {
		verbose = false;
		Generator.gui = gui;

		// Create a grid
		Grid grid = new Grid(width, height);
		gui.setGrid(grid);

		// Fill it with random pieces in a valid pattern
		if(nbcc < 0) {
			fillRandomPieces(grid, connectivity);
		}
		else {
			int currentNbcc = fillRandomPiecesWithConnectedComponents(grid, nbcc, connectivity);
		}

		// Shuffle the pieces
		shufflePiecesOrientation(grid);

		// Reactivate the GUI
		gui.activateAll(true);
	}

	/**
	 * Fills a grid with random pieces, in a solved pattern (all pieces' connectors connected to neighbourgs' connectors). All previous pieces in the grid will be replaced.
	 *
	 * @param grid the grid to fill
	 * @return the grid filled (input grid modified by side effects returned for practicity)
	 */
	public static Grid fillRandomPieces(Grid grid, double connectivity) {
		Random rand = new Random();
		for(int i=0; i<grid.getHeight(); i++) {
			for(int j=0; j<grid.getWidth(); j++) {
				boolean hasTopConnector = false;
				if (i>0) {
					Piece topPiece = grid.getPiece(i-1,j);
					hasTopConnector = topPiece.hasBottomConnector();
				}

				boolean hasLeftConnector = false;
				if (j>0) {
					Piece leftPiece = grid.getPiece(i,j-1);
					hasLeftConnector = leftPiece.hasRightConnector();
				}

				boolean hasBottomConnector = false;
				if (i<grid.getHeight()-1)
				{
					if(rand.nextDouble() < connectivity) {
						hasBottomConnector=true;
					}
				}

				boolean hasRightConnector = false;
				if (j<grid.getWidth()-1)
				{
					if(rand.nextDouble() < connectivity) {
						hasRightConnector=true;
					}
				}
				Piece piece = new Piece(i,j, hasTopConnector, hasRightConnector, hasBottomConnector, hasLeftConnector);
				if(gui != null) {
					gui.getGridMutex().lock();
				}
				grid.setPiece(i,j, piece);
				if(gui != null) {
					gui.getGridMutex().unlock();
					gui.refreshGrid();
				}
				if(verbose){System.out.println("Created piece "+i+","+j+" "+hasTopConnector+" "+hasLeftConnector+" "+hasBottomConnector+" "+hasRightConnector+" "+piece.getType()+" "+piece.getOrientation()+" "+DisplayUnicode.getUnicodeOfPiece(piece.getType(), piece.getOrientation()));grid.print();}
			}
		}
		return grid;
	}

	/**
	 * Fills a grid with random pieces, in a solved pattern (all pieces' connectors connected to neighbourgs' connectors). All previous pieces in the grid will be replaced.
	 *
	 * @param grid the grid to fill
	 * @return the grid filled (input grid modified by side effects returned for practicity)
	 */
	public static int fillRandomPiecesWithConnectedComponents(Grid grid, int nbcc, double connectivity) {
		Random rand = new Random();
		//For each piece, null if void or not determined yet, otherwise id of the connected component
		Integer[][] connectedComponents = new Integer[grid.getHeight()][grid.getWidth()];
		//Current number of connected components, should be equal to nbcc at the end
		int currentnbcc = 0;
		//Ids of components, always increased
		int idcc = 0;
		//number of pieces in the grid
		int pieceNumber = grid.getHeight()*grid.getWidth();
		//Number of pieces left to determine;
		int piecesLeftToDetermine = pieceNumber;

		for(int i=0; i<grid.getHeight(); i++) {
			for(int j=0; j<grid.getWidth(); j++) {
				//Builds the piece by determining if it has connectors in all directions

				//Top and left connectors are straighforward : they depend on the pieces already built
				boolean hasTopConnector = false;
				if (i>0) {
					Piece topPiece = grid.getPiece(i-1,j);
					hasTopConnector = topPiece.hasBottomConnector();
				}
				boolean hasLeftConnector = false;
				if (j>0) {
					Piece leftPiece = grid.getPiece(i,j-1);
					hasLeftConnector = leftPiece.hasRightConnector();
				}

				//Determine the goal and urgency
				boolean wantToIncreaseNbcc = currentnbcc < nbcc;
				boolean wantToKeepSame = currentnbcc >= nbcc;
				boolean urgent = false;
				double urgency = 1-((piecesLeftToDetermine/pieceNumber) /2); //between 0.5 and 1
				if(
				   wantToIncreaseNbcc && (nbcc - currentnbcc + 1 >=piecesLeftToDetermine/2 - grid.getWidth()) //Have to deal with increasing at least one line sooner
				|| wantToKeepSame && (2 >= piecesLeftToDetermine/grid.getWidth()) //Stop messing around if we're at the number during the two last lines
				) {
					urgent = true;
				}

				//Determine bottom connector
				boolean hasBottomConnector = false;
				//Can have a bottom connector only if not on the last row
				if (i<grid.getHeight()-1)
				{
					boolean urgentBottom = urgent;
					//Determine what we want to do towards the goal using the component of the piece
					boolean wantToConnect = false;
					Integer connectedComponentForPiece = connectedComponents[i][j];

					//Void piece
					if(connectedComponentForPiece == null) {
						//Wanting to increase, create a connection to create a component
						if(wantToIncreaseNbcc) {
							wantToConnect=true;
						}
						//Wanting to keep same, don't create a connection to let void
						else { //if(wantToKeepSame) {
							//wantToConnect=false;
							urgentBottom = true;
						}
					}
					//Piece in a component,
					else { //if(connectedComponentForPiece != null) {
						//Wanting to increase, don't create a connection to let the neighbor be in a different component
						if(wantToIncreaseNbcc) {
							//wantToConnect=false;
						}
						//Wanting to keep same, create a connection to not increase the number of component
						else { //if (wantToKeepSame) {
							wantToConnect=true;
						}
					}

					//Determine if we create the connection
					//If urgent just do what we want to do
					if(urgentBottom) {
						hasBottomConnector=wantToConnect;
					}
					//If not urgent, draw by taking connectivity and urgency into account
					else {
						double likelinessOfConnection = 0;
						if(wantToConnect) {
							double forceOfConnection = connectivity*urgency;
							likelinessOfConnection = forceOfConnection*0.5+0.5;
						}
						else {
							double forceOfDisconnection = (1-connectivity)*urgency;
							likelinessOfConnection = 1-(forceOfDisconnection*0.5+0.5);
						}
						if(rand.nextDouble() < likelinessOfConnection) {
							hasBottomConnector=true;
						}
					}

					//Update the connected components
					if(hasBottomConnector) {
						//We created a component
						if(connectedComponentForPiece == null) {
							connectedComponents[i][j] = idcc;
							connectedComponents[i+1][j] = idcc;
							if(verbose){System.out.println("Created vertical domino component "+currentnbcc+" joining "+i+","+j+" and "+(i+1)+","+j);}
							currentnbcc++;
							idcc++;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
						//We joined bottom neighbor to piece's component
						else {
							connectedComponents[i+1][j] = connectedComponentForPiece;
							if(verbose){System.out.println("Joined component "+connectedComponentForPiece+" from top joining "+i+","+j+" and "+(i+1)+","+j);}
						}
					}
				}

				//Determine right connector
				boolean hasRightConnector = false;
				//Can have a right connector only if not on the last column
				if (j<grid.getWidth()-1)
				{
					//Determine what we want to do towards the goal using the component of the piece and its right neighbor
					boolean wantToConnect=false;
					boolean doesntmatter=false;
					Integer connectedComponentForPiece = connectedComponents[i][j];
					Integer connectedComponentForRightNeighbor = connectedComponents[i][j+1];

					//Void piece
					if(connectedComponentForPiece == null) {
						//Void neighbor
						if(connectedComponentForRightNeighbor == null) {
							//Wanting to increase, create a connection, connecting the two void to create a component
							if(wantToIncreaseNbcc) {
								wantToConnect = true;
							}
							//Wanting to keep same, don't create a connection to not create a component
							else { //if(wantToKeepSame) {
								//wantToConnect = false;
								urgent = true;
							}
						}
						//Neighbor in a component, doesn't matter
						else { //if(connectedComponentForRightNeighbor != null)
							doesntmatter = true;
						}
					}
					//Piece in a component
					else {
						//Void neighbor
						if(connectedComponentForRightNeighbor == null) {
							//Wanting to increase, don't create a connection to let the piece be in a different component
							if(wantToIncreaseNbcc) {
								//wantToConnect=false;
							}
							//Wanting to keep same, create a connection to not increase the number of component
							else { //if (wantToKeepSame) {
								wantToConnect=true;
							}
						}
						//Neighbor in the same component, doesn't matter
						else if(connectedComponentForPiece == connectedComponentForRightNeighbor) {
							doesntmatter = true;
						}
						//Neighbor in a different component
						else {
							//Wanting to increase or keep same, don't create a connection to not decrease the number of component
							//if (wantToIncreaseNbcc || wantToKeepSame) {
								//wantToConnect=false;
						}
					}

					//If it doesn't matter, draw by taking only connectivity into account
					if(doesntmatter) {
						//Draw randomly with connectivity
						if(rand.nextDouble() < connectivity) {
							hasRightConnector=true;
						}
					}
					//If urgent just do what we want to do
					else if(urgent) {
						hasRightConnector = wantToConnect;
					}
					//If not urgent, draw by taking connectivity and urgency into account
					else {
						double likelinessOfConnection = 0;
						if(wantToConnect) {
							double forceOfConnection = connectivity*urgency;
							likelinessOfConnection = forceOfConnection*0.5+0.5;
						}
						else {
							double forceOfDisconnection = (1-connectivity)*urgency;
							likelinessOfConnection = 1-(forceOfDisconnection*0.5+0.5);
						}
						if(rand.nextDouble() < likelinessOfConnection) {
							hasRightConnector=true;
						}
					}

					//Update the connected components
					if(hasRightConnector) {
						//We created a component
						if(connectedComponentForPiece == null && connectedComponentForRightNeighbor == null) {
							connectedComponents[i][j] = idcc;
							connectedComponents[i][j+1] = idcc;
							if(verbose){System.out.println("Created horizontal domino component "+currentnbcc+" joining "+i+","+j+" and "+i+","+(j+1));}
							currentnbcc++;
							idcc++;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
						//We joined right neighbor to piece's component
						else if (connectedComponentForRightNeighbor == null) {
							connectedComponents[i][j+1] = connectedComponentForPiece;
							if(verbose){System.out.println("Joined component "+connectedComponentForPiece+" from left joining "+i+","+j+" and "+i+","+(j+1));}
						}
						//We joined piece to right neighbor's component
						else if (connectedComponentForPiece == null) {
							connectedComponents[i][j] = connectedComponentForRightNeighbor;
							if(verbose){System.out.println("Joined component "+connectedComponentForRightNeighbor+" from right joining "+i+","+j+" and "+i+","+(j+1));}
						}
						//We merged two components
						else if (connectedComponentForPiece != connectedComponentForRightNeighbor) {
							for(int ip = 0; ip<grid.getHeight(); ip++) {
								for(int jp = 0; jp<grid.getWidth(); jp++) {
									if(connectedComponents[ip][jp] == connectedComponentForRightNeighbor) {
										connectedComponents[ip][jp] = connectedComponentForPiece;
									}
								}
							}
							if(verbose){System.out.println("Merged components "+connectedComponentForPiece+" and "+connectedComponentForRightNeighbor+" joining "+i+","+j+" and "+i+","+(j+1));}
							currentnbcc--;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
					}
				}

				//Everything is determined, create piece
				Piece piece = new Piece(i,j, hasTopConnector, hasRightConnector, hasBottomConnector, hasLeftConnector);
				if(gui != null) {
					gui.getGridMutex().lock();
				}
				grid.setPiece(i,j, piece);
				if(gui != null) {
					gui.getGridMutex().unlock();
					gui.refreshGrid();
				}
				piecesLeftToDetermine--;
				if(verbose){System.out.println("Created piece "+i+","+j+" "+hasTopConnector+" "+hasLeftConnector+" "+hasBottomConnector+" "+hasRightConnector+" "+piece.getType()+" "+piece.getOrientation()+" "+DisplayUnicode.getUnicodeOfPiece(piece.getType(), piece.getOrientation()));grid.print();}

				/*//DEBUG Displays connectedComponents
				System.out.println("test "+currentnbcc);
				for(int ip=0; ip<grid.getHeight(); ip++) {
					for(int jp=0; jp<grid.getWidth(); jp++) {
						if(connectedComponents[ip][jp] != null) {
							System.out.print(connectedComponents[ip][jp]);
						} else {
							System.out.print(" ");
						}
					}
					System.out.println();
				}*/
			}
		}
		return currentnbcc;
	}

	/**
	 * Shuffles a grid: picks randomly all its pieces' orientation
	 *
	 * @param grid the grid to shuffle
	 * @return the grid shuffled (input grid modified by side effects returned for practicity)
	 */
	public static Grid shufflePiecesOrientation(Grid grid) {
		Random rand = new Random();
		for(int i=0; i<grid.getHeight(); i++) {
			for(int j=0; j<grid.getWidth(); j++) {
				Piece piece = grid.getPiece(i,j);
				int numberRotations = rand.nextInt(4);
				if(gui != null) {
					gui.getGridMutex().lock();
				}
				for (int r=0; r<=numberRotations; r++)
				{
					piece.turn();
				}
				grid.setPiece(i,j, piece);
				if(gui != null) {
					gui.getGridMutex().unlock();
					gui.refreshGrid();
				}
			}
		}
		return grid;
	}

	/**
	 * Gets the set of the connected components of a grid, a connected component is a set of piece. Kept only to be used by unit tests
	 *
	 * @param grid the grid
	 * @return the connected components
	 */
	public static ArrayList<ArrayList<Piece>> getConnectedComponents(Grid grid) {
		Boolean[][] isInAComponent = new Boolean[grid.getHeight()][grid.getWidth()];
		ArrayList<ArrayList<Piece>> connectedComponents = new ArrayList<ArrayList<Piece>>();

		for(int i=0; i<grid.getHeight(); i++) {
			for(int j=0; j<grid.getWidth(); j++) {
				if(isInAComponent[i][j] == null) {
					Piece piece = grid.getPiece(i,j);
					if(piece.getType() != PieceType.VOID) {
						ArrayList<Piece> connectComponentOfPiece = new ArrayList<Piece>();
						addPieceToConnectedComponent(piece, connectComponentOfPiece, isInAComponent, grid);
						connectedComponents.add(connectComponentOfPiece);
					}
				}
			}
		}
		return connectedComponents;
	}

	/**
	 * Used by getConnectedComponents
	 */
	private static void addPieceToConnectedComponent(Piece piece, ArrayList<Piece> connectedComponent, Boolean[][] isInAComponent, Grid grid) {
		connectedComponent.add(piece);
		isInAComponent[piece.getPosY()][piece.getPosX()] = true;

		for (Piece neighbor : grid.listOfNeighbours(piece)) {
			if(isInAComponent[neighbor.getPosY()][neighbor.getPosX()] == null) {
				addPieceToConnectedComponent(neighbor, connectedComponent, isInAComponent, grid);
			}
		}
	}
}

	/* HISTORY.

	/**
	 * This was in the generateLevel method to use several tries to generate with the buggy algorithm below
	 *
		// Fill it with random pieces in a valid pattern
		if(nbcc < 0) {
			grid = new Grid(width, height);
			fillRandomPieces(grid, connectivity);
		}
		else {
			int bestNbcc=-1;
			Grid bestGrid = null;
			int numberOfTries = 0;
			int maxNumberOfTries = (10000*(verbose?1:100)/(width*height))+1;

			while(bestNbcc != nbcc && numberOfTries<maxNumberOfTries) {
				numberOfTries++;
				Grid currentGrid = new Grid(width, height);

				int currentNbcc = fillRandomPiecesWithConnectedComponents(currentGrid, nbcc, connectivity);

				if(!verbose){System.out.println("Generated grid with "+currentNbcc+" connected components, try n°"+numberOfTries+"\n");currentGrid.print();}

				if(bestNbcc==-1 || Math.abs(currentNbcc - nbcc) < Math.abs(bestNbcc - nbcc)) {
					bestGrid = currentGrid;
					bestNbcc = currentNbcc;
				}
			}
			grid = bestGrid;
			if(!verbose){System.out.println("Generated a grid with "+bestNbcc+" components in "+numberOfTries+" tries, off by "+Math.abs(bestNbcc-nbcc));}
		}

	/**
	 * This is a previous version of fillRandomPiecesWithConnectedComponents, with the ability to overshoot the nbcc and trying to go down after
	 *
	public static int fillRandomPiecesWithConnectedComponents(Grid grid, int nbcc, double connectivity) {
		Random rand = new Random();
		//For each piece, null if void or not determined yet, otherwise id of the connected component
		Integer[][] connectedComponents = new Integer[grid.getHeight()][grid.getWidth()];
		//Current number of connected components, should be equal to nbcc at the end
		int currentnbcc = 0;
		//Ids of components, always increased
		int idcc = 0;
		//number of pieces in the grid
		int pieceNumber = grid.getHeight()*grid.getWidth();
		//Number of pieces left to determine;
		int piecesLeftToDetermine = pieceNumber;

		for(int i=0; i<grid.getHeight(); i++) {
			for(int j=0; j<grid.getWidth(); j++) {
				//Builds the piece by determining if it has connectors in all directions

				//Top and left connectors are straighforward : they depend on the pieces already built
				boolean hasTopConnector = false;
				if (i>0) {
					Piece topPiece = grid.getPiece(i-1,j);
					hasTopConnector = topPiece.hasBottomConnector();
				}
				boolean hasLeftConnector = false;
				if (j>0) {
					Piece leftPiece = grid.getPiece(i,j-1);
					hasLeftConnector = leftPiece.hasRightConnector();
				}

				//Determine the goal and urgency
				boolean wantToReduceNbcc = currentnbcc > nbcc;
				boolean wantToIncreaseNbcc = currentnbcc < nbcc;
				boolean wantToKeepSameNbcc = currentnbcc == nbcc;
				boolean urgent = false;
				double urgency = 1-((piecesLeftToDetermine/pieceNumber) /2); //between 0.5 and 1
				if(
				   wantToIncreaseNbcc && (nbcc - currentnbcc + 1 >=piecesLeftToDetermine - grid.getWidth()) //Have to deal with increasing at least one line sooner
				|| wantToReduceNbcc && (currentnbcc - nbcc + 1 >=piecesLeftToDetermine/grid.getWidth()) //Really urgent to reduce when we reach the last lines
				|| wantToKeepSameNbcc && (2 >=piecesLeftToDetermine/grid.getWidth()) //Stop messing around if we're at the number during the two last lines
				) {
					urgent = true;
				}

				//Determine bottom connector
				boolean hasBottomConnector = false;
				//Can have a bottom connector only if not on the last row
				if (i<grid.getHeight()-1)
				{
					//Determine what we want to do towards the goal using the component of the piece
					boolean wantToConnect = false;
					Integer connectedComponentForPiece = connectedComponents[i][j];

					//Void piece
					if(connectedComponentForPiece == null) {
						//Wanting to increase, create a connection to create a component
						if(wantToIncreaseNbcc) {
							wantToConnect=true;
						}
						//Wanting to reduce or keep same, don't create a connection to let void
						else { //if(wantToReduceNbcc || wantToKeepSameNbcc) {
							//wantToConnect=false;
						}
					}
					//Piece in a component,
					else { //if(connectedComponentForPiece != null) {
						//Wanting to increase, don't create a connection to let the neighbor be in a different component
						if(wantToIncreaseNbcc) {
							//wantToConnect=false;
						}
						//Wanting to reduce or keep same, create a connection to not increase the number of component
						else { //if (wantToReduceNbcc || wantToKeepSameNbcc) {
							wantToConnect=true;
						}
					}

					//Determine if we create the connection
					//If urgent just do what we want to do
					if(urgent) {
						hasBottomConnector=wantToConnect;
					}
					//If not urgent, draw by taking connectivity and urgency into account
					else {
						double likelinessOfConnection = 0;
						if(wantToConnect) {
							double forceOfConnection = connectivity*urgency;
							likelinessOfConnection = forceOfConnection*0.5+0.5;
						}
						else {
							double forceOfDisconnection = (1-connectivity)*urgency;
							likelinessOfConnection = 1-(forceOfDisconnection*0.5+0.5);
						}
						if(rand.nextDouble() < likelinessOfConnection) {
							hasBottomConnector=true;
						}
					}

					//Update the connected components
					if(hasBottomConnector) {
						//We created a component
						if(connectedComponentForPiece == null) {
							connectedComponents[i][j] = idcc;
							connectedComponents[i+1][j] = idcc;
							if(verbose){System.out.println("Created vertical domino component "+currentnbcc+" joining "+i+","+j+" and "+(i+1)+","+j);}
							currentnbcc++;
							idcc++;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
						//We joined bottom neighbor to piece's component
						else {
							connectedComponents[i+1][j] = connectedComponentForPiece;
							if(verbose){System.out.println("Joined component "+connectedComponentForPiece+" from top joining "+i+","+j+" and "+(i+1)+","+j);}
						}
					}
				}

				//Determine right connector
				boolean hasRightConnector = false;
				//Can have a right connector only if not on the last column
				if (j<grid.getWidth()-1)
				{
					//Determine what we want to do towards the goal using the component of the piece and its right neighbor
					boolean wantToConnect=false;
					boolean doesntmatter=false;
					Integer connectedComponentForPiece = connectedComponents[i][j];
					Integer connectedComponentForRightNeighbor = connectedComponents[i][j+1];

					//Void piece
					if(connectedComponentForPiece == null) {
						//Void neighbor
						if(connectedComponentForRightNeighbor == null) {
							//Wanting to increase, create a connection, connecting the two void to create a component
							if(wantToIncreaseNbcc) {
								wantToConnect = true;
							}
							//Wanting to reduce or keep same, don't create a connection to not create a component
							else { //if(wantToReduceNbcc || wantToKeepSameNbcc) {
								//wantToConnect = false;
							}
						}
						//Neighbor in a component, doesn't matter
						else { //if(connectedComponentForRightNeighbor != null)
							doesntmatter = true;
						}
					}
					//Piece in a component
					else {
						//Void neighbor
						if(connectedComponentForRightNeighbor == null) {
							//Wanting to increase, don't create a connection to let the piece be in a different component
							if(wantToIncreaseNbcc) {
								//wantToConnect=false;
							}
							//Wanting to reduce or keep same, create a connection to not increase the number of component
							else { //if (wantToReduceNbcc || wantToKeepSameNbcc) {
								wantToConnect=true;
							}
						}
						//Neighbor in the same component, doesn't matter
						else if(connectedComponentForPiece == connectedComponentForRightNeighbor) {
							doesntmatter = true;
						}
						//Neighbor in a different component
						else {
							//Wanting to reduce, create a connection to merge the two components
							if(wantToReduceNbcc) {
								wantToConnect=true;
								urgent = true; //Not a lot of opportunities to merge, deal with it right now
							}
							//Wanting to increase or keep same, don't create a connection to not decrease the number of component
							else { //if (wantToIncreaseNbcc) || wantToKeepSameNbcc) {
								//wantToConnect=false;
							}
						}
					}

					//If it doesn't matter, draw by taking only connectivity into account
					if(doesntmatter) {
						//Draw randomly with connectivity
						if(rand.nextDouble() < connectivity) {
							hasRightConnector=true;
						}
					}
					//If urgent just do what we want to do
					else if(urgent) {
						hasRightConnector = wantToConnect;
					}
					//If not urgent, draw by taking connectivity and urgency into account
					else {
						double likelinessOfConnection = 0;
						if(wantToConnect) {
							double forceOfConnection = connectivity*urgency;
							likelinessOfConnection = forceOfConnection*0.5+0.5;
						}
						else {
							double forceOfDisconnection = (1-connectivity)*urgency;
							likelinessOfConnection = 1-(forceOfDisconnection*0.5+0.5);
						}
						if(rand.nextDouble() < likelinessOfConnection) {
							hasRightConnector=true;
						}
					}

					//Update the connected components
					if(hasRightConnector) {
						//We created a component
						if(connectedComponentForPiece == null && connectedComponentForRightNeighbor == null) {
							connectedComponents[i][j] = idcc;
							connectedComponents[i][j+1] = idcc;
							if(verbose){System.out.println("Created horizontal domino component "+currentnbcc+" joining "+i+","+j+" and "+i+","+(j+1));}
							currentnbcc++;
							idcc++;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
						//We joined right neighbor to piece's component
						else if (connectedComponentForRightNeighbor == null) {
							connectedComponents[i][j+1] = connectedComponentForPiece;
							if(verbose){System.out.println("Joined component "+connectedComponentForPiece+" from left joining "+i+","+j+" and "+i+","+(j+1));}
						}
						//We joined piece to right neighbor's component
						else if (connectedComponentForPiece == null) {
							connectedComponents[i][j] = connectedComponentForRightNeighbor;
							if(verbose){System.out.println("Joined component "+connectedComponentForRightNeighbor+" from right joining "+i+","+j+" and "+i+","+(j+1));}
						}
						//We merged two components
						else if (connectedComponentForPiece != connectedComponentForRightNeighbor) {
							for(int ip = 0; ip<grid.getHeight(); ip++) {
								for(int jp = 0; jp<grid.getWidth(); jp++) {
									if(connectedComponents[ip][jp] == connectedComponentForRightNeighbor) {
										connectedComponents[ip][jp] = connectedComponentForPiece;
									}
								}
							}
							if(verbose){System.out.println("Merged components "+connectedComponentForPiece+" and "+connectedComponentForRightNeighbor+" joining "+i+","+j+" and "+i+","+(j+1));}
							currentnbcc--;
							if(verbose){System.out.println("Number of components changed to "+currentnbcc);}
						}
					}
				}

				//Everything is determined, create piece
				Piece piece = new Piece(i,j, hasTopConnector, hasRightConnector, hasBottomConnector, hasLeftConnector);
				grid.setPiece(i,j, piece);
				piecesLeftToDetermine--;
				if(verbose){System.out.println("Created piece "+i+","+j+" "+hasTopConnector+" "+hasLeftConnector+" "+hasBottomConnector+" "+hasRightConnector+" "+piece.getType()+" "+piece.getOrientation()+" "+DisplayUnicode.getUnicodeOfPiece(piece.getType(), piece.getOrientation()));grid.print();}

				/* //DEBUG Displays connectedComponents
				System.out.println("test "+currentnbcc);
				for(int ip=0; ip<grid.getHeight(); ip++) {
					for(int jp=0; jp<grid.getWidth(); jp++) {
						if(connectedComponents[ip][jp] != null) {
							System.out.print(connectedComponents[ip][jp]);
						} else {
							System.out.print(" ");
						}
					}
					System.out.println();
				}*
			}
		}
		return currentnbcc;
	}

	/**
	 * This is an abandonned idea for solving the nbcc problem : after the grid is generated solved without nbcc, modify it until it has the right nbcc
	 *

	public static Grid modifyConnectedComponents(Grid grid, int nbcc) {
		boolean finished = false;
		Random rand = new Random();
		while(!finished) {
			ArrayList<ArrayList<Piece>> connectedComponents = getConnectedComponents();
			int currentNbcc = connectedComponents.size()
			if(currentNbcc == nbcc) {
				finished = true;
			}
			else if(currentNbcc > nbcc) {
				// Reduce number of connected component...
				int connectedComponentToExpandId = rand.nextInt(currentNbcc);
				ArrayList<Piece> connectedComponentToExpand = connectedComponents.get(connectedComponentToExpandId);
				if(connectedComponentToExpand.size() > 3) {
					//...by expanding a big one until it connects to a neighbor one
					ArrayList<Piece> piecesToExpand = ArrayList<Piece>(connectedComponentToExpand);
					boolean expanded = false;
					while(!expanded) {
						int pieceToExpandId = rand.nextInt(piecesToExpand.size());
						Piece pieceToExpand = piecesToExpand.get(pieceToExpandId);
						Orientation[] directionsToExpand = Orientation.values();
						while(!expanded) {
							int directionToExpandId = rand.nextInt(piecesToExpand.size());
							Orientation directionToExpand = directionsToExpand.get(directionToExpandId);
							Piece neighbor = grid.getNeighborFromDirection(piece,d);
							if() {//neighbor appartient à une autre région
								//expand
								expanded = true;
							}
							else {
								directionsToExpand.remove(directionToExpand);
							}
						}
						if(!expanded) {
							piecesToExpand.remove(pieceToExpand);
						}
					}
				}
				else
				{
					//by changing a small one to void
				}
			}
			else if(currentNbcc < nbcc) {
				// Increase number of connected components
			}
		}
		return grid;
	}
	*/
