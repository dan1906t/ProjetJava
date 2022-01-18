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
		// Create display
		if(display) {
			GUI gui = new GUI(grid);
			gui.start();
		}

		boolean solved = Checker.isSolved(grid);
		System.out.println("SOLVED: " + solved);
	}

	public static Grid buildGrid(String inputFilePath) throws IOException {
		return GridFiles.unserializeGrid(inputFilePath);
	}

	public static boolean isSolved(Grid grid) {
		return grid.allPiecesTotallyConnected();
	}
}
