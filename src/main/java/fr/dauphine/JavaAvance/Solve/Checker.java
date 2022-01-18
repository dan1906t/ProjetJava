package fr.dauphine.JavaAvance.Solve;

import java.io.IOException;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.GUI.GridFiles;
import fr.dauphine.JavaAvance.GUI.GUI;
import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.PieceType;

/**
 * Grid solvability checker
 */
public class Checker {
	// Shouldn't be instanciated
	private Checker() {
	}

  /**
	 * Controller for check option. Loads a grid from a file, checks if it is solved and prints it in console.
	 * 
	 * @param inputFilePath the path of the file representing the grid
	 * @param verbose true if should print more info in console
	 * @param display true if should display result in GUI
	 */
	public static void checkLevel(String inputFilePath, boolean verbose, boolean display) {
		Grid grid = null;

		try {
			grid = buildGrid(inputFilePath);
		} catch (IOException e) {
			System.err.println("Error parsing file : " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		if(verbose){System.out.println("Read grid\n");grid.print();}

		boolean solved = Checker.isSolved(grid);
		System.out.println("SOLVED: " + solved);

		// Create display
		if(display) {
			GUI gui = new GUI(grid);
			gui.start();
			if(solved) {
				gui.displayInfo("Solved!");
			}
			else {
				gui.displayInfo("Not solved.");
			}
		}
	}

  /**
	 * Check for the user interface. Checks if the grid is solved
	 * 
	 * @param gui the interface that should be updated
	 * @param grid the grid to check
	 */
	public static void checkLevelForGUI(GUI gui, Grid grid) {
		boolean solved = Checker.isSolved(grid);

		if(solved) {
			gui.displayInfo("Solved!");
		}
		else {
			gui.displayInfo("Not solved.");
		}

		// Reactivate the GUI
		gui.activateAll(true);
	}

	public static Grid buildGrid(String inputFilePath) throws IOException {
		return GridFiles.unserializeGrid(inputFilePath);
	}

	public static boolean isSolved(Grid grid) {
		return grid.allPiecesTotallyConnected();
	}
}
