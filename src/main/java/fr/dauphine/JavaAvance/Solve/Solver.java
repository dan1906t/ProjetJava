package fr.dauphine.JavaAvance.Solve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.lang.IllegalStateException;
import java.text.DecimalFormat;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Pair;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.GUI.GridFiles;
import fr.dauphine.JavaAvance.GUI.GUI;

/**
 * Grid solver
 */
public class Solver {
	// Shouldn't be instanciated
	private Solver() {
	}

	private static boolean verbose = false;
	private static GUI gui = null;
	private static int strategy = 0;

	//All search tree exploration strategies to pick a piece to make a guess with
	public static final int STRATEGY_PICKONE = 0; //Picks the first piece in piecesLeftToFix
	public static final int STRATEGY_PICKLOWESTLIBERTY = 1; //Picks a piece with the lowest valid orientations
	public static final int STRATEGY_PICKSECONDDEGREELIBERTY = 2; //Picks a piece with a two degree liberty (two valid orientations)
	//public static final int STRATEGY_PICKRANDOM = 3; //Picks a piece at random
	//public static final int STRATEGY_PICKHIGHESTFIXEDNEIGHBORS = 4; //Picks a piece with the lowest number of fixed neighbors

	//The grid to solve
	protected static Grid grid = null;

	//Both following variables represent the pace to explore in the search algorithm
	//A two-dimensions array matching the grid, each case containing a liberty, meaning a set of orientations that a piece can potentially take
	private static ArrayList<Orientation>[][] libertiesLeft = null;
	//A set of the pieces left to fix, meaning to chose an orientation for
	private static ArrayDeque<Piece> piecesLeftToFix = null;

	//Rollback stack in the search algorithm, used to simulate recursion
	//Each state is a pair of a piece and a liberty, and represents either a deduction, or a guess.
	//A deduction means the liberty of a piece changed or an orientation have been set for a piece with no liberty (only one valid orientation),
	//A guess means an orientation have been chosen for a piece amoung all the possible orientations in the liberty,
	//To indicate a guess, the liberty is set to null. In case of a deduction, the liberty stored is the one before deduction.
	//When rolling back, the history is rolled back up to the last guess.
	private static ArrayDeque<Pair<Piece, ArrayList<Orientation>>> stateHistory = null;

	/**
	 * Controller for solve option. Loads a grid from a file, attempt to solve it, prints the outcome in console, and if the attempt is successful, saves the solved grid in a file
	 *
	 * @param inputFilePath the path of the file representing the grid to solve
	 * @param outputFilePath the path of the file where the solution grid will be saved
	 * @param strategy the strategy to use for solving, see this class's constants
	 * @param performances true if should print performances infos in console
	 * @param verbose true if should print more info in console
	 * @param display true if should display result and progress in GUI
	 */
	public static void solveLevel(String inputFilePath, String outputFilePath, int strategy, boolean performances, boolean verbose, boolean display) {
		Solver.verbose = verbose;
		Solver.strategy = strategy;

		try {
			//Get grid from file
			grid = GridFiles.unserializeGrid(inputFilePath);
		} catch (IOException e) {
			System.err.println("Error parsing file : " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		if(verbose){System.out.println("Read grid\n");grid.print(true);}
		// Create display
		if(display) {
			gui = new GUI(grid);
			gui.start();
		}

		try {
			//Solve
			long startTime=0, endTime=0;
			if(performances) startTime = System.currentTimeMillis();
			solveGridDepthFirstSearch(grid);
			if(performances) endTime = System.currentTimeMillis();

			//Solved
			if(verbose){System.out.println("Solved Grid\n");grid.print();}
			if(performances) System.out.println("Solving time: "+timeToString(startTime,endTime));
			if(gui != null) gui.displayInfo("Solved!");
			System.out.println("SOLVED: true");

			try {
				//Save solution to file
		  	GridFiles.serializeGrid(grid, outputFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IllegalStateException e) {
			//Not solved
			if(verbose){System.out.println("Grid not solved, here's its last state\n");grid.print(true);}
			if(gui != null) gui.displayInfo("Not solvable.");
			System.out.println("SOLVED: false");
		}
	}

	/**
	 * Solve for an interactive interface. Attempt to solve the grid.
	 *
	 * @param gui the interface that should be updated
	 * @param grid the grid to solve
	 * @param strategy the strategy to use for solving, see this class's constants
	 */
	public static void solveLevelForGUI(GUI gui, Grid grid, int strategy) {
		verbose = false;
		Solver.grid = grid;
		Solver.gui = gui;

		try {
			//Solve
			solveGridDepthFirstSearch(grid);

			//Solved
			gui.displayInfo("Solved!");
		} catch (IllegalStateException e) {
			//Not solved
			gui.displayInfo("Not solvable.");
		}

		// Reactivate the GUI
		gui.activateAll(true);
	}

	/**
	 * Solves the grid, using a depth first search algorithm.
	 *
	 * @param grid the grid
	 * @return the grid, after solving it
	 * @throw IllegalStateException if the grid couldn't be solved
	 */
	protected static Grid solveGridDepthFirstSearch (Grid grid) throws IllegalStateException {
		//See class variables comments for context

		Solver.grid = grid;

		//Initialize libertiesLeft, piecesLeftToFix, and fix easy pieces
		getLibertiesForAllNonFixedPieces();

		if(verbose) {System.out.println("\n\nDone initializing, starting to explore\n");grid.print(true);printLibertiesLeft();}

		//Initialize stateHistory
		stateHistory = new ArrayDeque<Pair<Piece, ArrayList<Orientation>>>();

		while (piecesLeftToFix.size() > 0)  {
			//As long as there are pieces left to fix, chose one
			if(verbose){System.out.println("\n\nChoosing a piece to fix\n");grid.print(true);printLibertiesLeft();}

			Piece pieceToFix = choosePieceLeftToFix();
			ArrayList<Orientation> liberty = getLibertyLeft(pieceToFix);

			//Chose an orientation from its liberties
			Orientation orientationChosen = liberty.get(0);

			//If it still has liberty (several valid orientations)
			if(liberty.size() > 1) {
				//Remember the state before guessing in rollback history
				stateHistory.addFirst(new Pair<Piece, ArrayList<Orientation>>(pieceToFix, new ArrayList<Orientation>(liberty)));
				//As long as all guesses for this piece are not rolled back, the guessed orientation for this piece cannot be picked again
				liberty.remove(orientationChosen);
				//Put the guess in rollback history
				stateHistory.addFirst(new Pair<Piece, ArrayList<Orientation>>(pieceToFix, null));
			}

			try {
				fixPiece (orientationChosen, pieceToFix);
			} catch (IllegalStateException e) {
				boolean shouldRollbackContinue = true;
				while(shouldRollbackContinue) {
					shouldRollbackContinue = rollBack();
				}
				if(gui != null) {
					gui.refreshGrid();
				}
			}
		}

		return grid;
	}

	/**
	 * Initializes libertiesLeft, piecesLeftToFix, and fixes easy pieces
	 *
	 * @throw IllegalStateException if the grid couldn't be solved
	 */
	private static void getLibertiesForAllNonFixedPieces() throws IllegalStateException {
		stateHistory = null;
		libertiesLeft = new ArrayList[grid.getHeight()][grid.getWidth()];
		piecesLeftToFix = new ArrayDeque<Piece>();
		int i=0;
		for (Piece[] line : grid.getAllPieces()) {
			int j=0;
			for (Piece piece : line) {
				if(!piece.isFixed()) {
					//For each non-fixed piece in the grid
					//Get its liberty
					ArrayList<Orientation> validOrientations = getLibertyForPiece(piece);
					if(verbose){System.out.println("Initialize piece "+piece.getPosY()+","+piece.getPosX()+" with liberties "+validOrientations.toString());}
					//Check its liberty
					if(checkLibertyForPiece(validOrientations, piece)) {
						//Build libertiesLeft and piecesLeftToFix
						libertiesLeft[i][j] = new ArrayList<Orientation>(validOrientations);
						piecesLeftToFix.addLast(piece);
					}
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * Calculates the liberty for a piece, the set of potentially valid orientations for a piece based on its constraints (its fixed neighbors or if it touches a border)
	 *
	 * @param piece the piece
	 * @return the liberty
	 */
	protected static ArrayList<Orientation> getLibertyForPiece(Piece piece) {
		// Starts will all orientations that the piece type allows
		ArrayList<Orientation> liberty = piece.getType().getListOfPossibleOri();
		// Reduce the set for each constraints in each direction
		for(Orientation d : Orientation.values()) {
			reduceLibertyBasedOnConstraint(d, liberty, piece);
		}
		return liberty;
	}

	/**
	 * Checks the constraint of a piece in one direction, if it has a border or a fixed neighbor in that direction, and takes it into account to potentially reduce the liberty of that piece
	 *
	 * @param d the direction
	 * @param liberty the liberty to reduce, must represent the known liberty of the piece without that constraint
	 * @param piece the piece
	 * @return true if the liberty have been reduced, false if unchanged
	 */
	protected static boolean reduceLibertyBasedOnConstraint(Orientation d, ArrayList<Orientation> liberty, Piece piece) {
		boolean libertyHaveChanged = false;
		// Get the neighbor in that direction
		Piece neighbor = grid.getNeighborFromDirection(piece, d);
		// If the neighbor is a border or it is fixed, there is a constraint in that direction
		if (neighbor == null || neighbor.isFixed())
		{
			// Check if the constraint is for the piece to present a connection in that direction (the neighbor shows a connexion) or not (border neightbor or the neighbor shows no connection)
			boolean shouldPresentConnection = true;
			if (neighbor == null || !neighbor.hasConnectorInDirection(d.getOpposedOrientation())) {
				shouldPresentConnection = false;
			}

			// Get valid orientations under the constraint
			ArrayList<Orientation> libertyForCurrentConstraint = piece.getType().getListOfPossibleOrientationsUnderConstraint(d, shouldPresentConnection);

			ArrayList<Orientation> libertyCopy = new ArrayList<Orientation>(liberty);

			// Remove invalid orientations for constraint
			libertyHaveChanged = liberty.retainAll(libertyForCurrentConstraint);

			// Deduction: Store the previous state in rollback history if it has changed
			if(libertyHaveChanged && stateHistory != null) {
				if(verbose){System.out.println("Liberty for piece "+piece.getPosY()+","+piece.getPosX()+" was reduced from "+libertyCopy+" to "+liberty);}
				stateHistory.addFirst(new Pair(piece, libertyCopy));
			}
		}

		return libertyHaveChanged;
	}

	/**
	 * Checks the liberty of a piece, fixes it if it has no liberty and returns false, throws an exception if it has no valid orientation, and returns true if it still has liberty
	 *
	 * @param liberty the liberty to check, must represent the liberty of the piece
	 * @param piece the piece
	 * @return true if the piece still have liberty
	 * @throws IllegalStateException if the piece have no valid orientation (invalid state)
	 */
	private static boolean checkLibertyForPiece (ArrayList<Orientation> liberty, Piece piece) throws IllegalStateException {
		if (liberty.size() == 1) {
			// This piece has no liberty : it has only one valid orientation, fix it
			if(verbose){System.out.println("Fixing a piece with no liberty");}
			fixPiece (liberty.get(0), piece);
			return false;
		}
		else if (liberty.size() < 1) {
			// This piece has no valid orientation
			if(verbose){System.out.println("Piece "+piece.getPosY()+","+piece.getPosX()+" has no valid orientation, rolling back\n");grid.print(true);}
			throw new IllegalStateException();
		}
		else {
			return true;
		}
	}

	/**
	 * Fixes a piece in a specified orientation and checks its neighbors for the new constraint it represents
	 *
	 * @param o the orientation
	 * @param piece the piece
	 * @throws IllegalStateException if the consequences of that call leads to an invalid state
	 */
	private static void fixPiece (Orientation o, Piece piece) throws IllegalStateException {
		if(verbose){System.out.println("Fixing piece "+piece.getPosY()+","+piece.getPosX()+" "+piece.getType()+" "+o);}

		//Fix the piece in the orientation and stop searching for it
		if(gui != null) {
			gui.getGridMutex().lock();
		}
		piece.setOrientation(o);
		piece.setFixed(true);
		if(gui != null) {
			gui.getGridMutex().unlock();
			gui.refreshGrid();
		}
		piecesLeftToFix.remove(piece);

		//Deduction: Put the state in rollback history to remember to unfix the piece
		if(stateHistory != null) {
			stateHistory.addFirst(new Pair<Piece, ArrayList<Orientation>>(piece, new ArrayList<Orientation>(getLibertyLeft(piece))));
		}

		// Update the constraints of the neighbors
		for (Orientation d : Orientation.values()) {
			Piece neighbor = grid.getNeighborFromDirection(piece,d);
			if(neighbor != null && !neighbor.isFixed()) {
				ArrayList<Orientation> liberty = getLibertyLeft(neighbor);
				if (liberty != null) {
					if(verbose){System.out.println("Checking "+d+" neighbor "+neighbor.getPosY()+","+neighbor.getPosX()+" of "+piece.getPosY()+","+piece.getPosX());}
					reduceLibertyBasedOnConstraint (d.getOpposedOrientation(), liberty, neighbor);
				}
			}
		}
		// Checks the constraints of the neighbors
		// Note : this can not be done at the same time as above, I don't remember the rare case where it matters but it does, especially for big grids
		for (Orientation d : Orientation.values()) {
			Piece neighbor = grid.getNeighborFromDirection(piece,d);
			if(neighbor != null && !neighbor.isFixed()) {
				ArrayList<Orientation> liberty = getLibertyLeft(neighbor);
				if (liberty != null) {
					checkLibertyForPiece (liberty, neighbor);
				}
			}
		}
	}


	/**
	 * Chooses a piece left to fix for next guess and removes it from piecesLeftToFix
	 *
	 * @return the piece chosen
	 */
	private static Piece choosePieceLeftToFix() {
		Piece pieceToFix = null;

		if(strategy == STRATEGY_PICKLOWESTLIBERTY) {
			int lowestLiberty = 5;
			for(Piece p: piecesLeftToFix) {
				ArrayList<Orientation> liberty = getLibertyLeft(p);
				if(liberty.size()<lowestLiberty) {
					lowestLiberty = liberty.size();
					pieceToFix = p;
					if(lowestLiberty == 1) {
						break;
					}
				}
			}
			piecesLeftToFix.remove(pieceToFix);
		}
		else if(strategy == STRATEGY_PICKSECONDDEGREELIBERTY) {
			int lowestLiberty = 5;
			for(Piece p: piecesLeftToFix) {
				ArrayList<Orientation> liberty = getLibertyLeft(p);
				if(liberty.size()<lowestLiberty) {
					lowestLiberty = liberty.size();
					pieceToFix = p;
					if(lowestLiberty == 2) {
						break;
					}
				}
			}
			piecesLeftToFix.remove(pieceToFix);
		}
		//else if(strategy == STRATEGY_PICKRANDOM) {} //Would not be optimal with an ArrayDeque for piecesLeftToFix : implement a way for pieceToFix to change type depending on strategy ?
		else { //if(strategy == STRATEGY_PICKONE) {
			pieceToFix = piecesLeftToFix.removeFirst();
		}

		return pieceToFix;
	}

	/**
	 * Rolls back the history.
	 *
	 * @return true if rollback should continue
	 * @throws IllegalStateException if the consequences of that call leads to an invalid state
	 */
	private static boolean rollBack() throws IllegalStateException {
		boolean rollbackShouldContinue = false;
		Pair<Piece, ArrayList<Orientation>> state = null;
		try {
			//Get last state from history
			state = stateHistory.removeFirst();
		} catch (NoSuchElementException e) {
			throw new IllegalStateException();
		}

		Piece piece = state.getKey();
		ArrayList<Orientation> previousLibertyState = state.getValue();

		//If the piece is fixed, unfix it, it means the deduction we're rolling back was just before fixing the piece
		if(piece.isFixed()) {
			if(gui != null) {
				gui.getGridMutex().lock();
			}
			piece.setFixed(false);
			if(gui != null) {
				gui.getGridMutex().unlock();
			}
			piecesLeftToFix.addFirst(piece);
		}


		//If the state is a deduction, reverts the liberty back to its previous state, then continue to rollback until guess state
		if(previousLibertyState != null) {
			if(verbose){System.out.println("Rollback, reverting liberty for piece "+piece.getPosY()+","+piece.getPosX()+" to "+previousLibertyState);}
			setLibertyLeft(piece, previousLibertyState);
			rollbackShouldContinue = true;
		}
		//If the state is a guess, stop rolling back, we'll eventually continue to guess for that piece
		return rollbackShouldContinue;
	}

	/**
	 * Get liberty left from libertiesLeft for piece
	 *
	 * @param piece the piece
	 * @return the liberty
	 */
	private static ArrayList<Orientation> getLibertyLeft(Piece piece) {
		return libertiesLeft[piece.getPosY()][piece.getPosX()];
	}

	/**
	 * Set liberty left from libertiesLeft for piece
	 *
	 * @param piece the piece
	 * @param liberty the liberty
	 */
	private static void setLibertyLeft(Piece piece, ArrayList<Orientation> liberty) {
		libertiesLeft[piece.getPosY()][piece.getPosX()] = liberty;
	}

	/**
	 * Prints all liberties left to console
	 */
	private static void printLibertiesLeft() {
		int i=0;
		for (Piece[] line : grid.getAllPieces()) {
			int j=0;
			for (Piece piece : line) {
				if(!piece.isFixed()) {
					System.out.println("Piece "+piece.getPosY()+","+piece.getPosX()+" have liberty "+getLibertyLeft(piece));
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * Gets a string representing the time elapsed between a starting time and ending time
	 *
	 * @param startTime starting time in ms
	 * @param endTime ending time in ms
	 */
	protected static String timeToString(long startTime, long endTime) {
		long time = endTime-startTime;
		if(time < 1000) {
			return time+"ms";
		}
		else {
			time = time/1000;
			if(time < 60) {
				return (new DecimalFormat("#.#").format(time))+"s";
			}
			else {
				time = time/60;
				return (new DecimalFormat("#.#").format(time))+"min";
			}
		}
	}
}
