package fr.dauphine.JavaAvance.GUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;

/**
 * Grid Files handler
 */
public class GridFiles {
	// Shouldn't be instanciated
	private GridFiles() {
	}

  /**
	 * Builds a file-formated string representing a grid
	 * 
	 * @param the grid
	 * @return the file-formated string
	 */
	public static String getFileFormatStringFromGrid(Grid grid) {
		String s = grid.getWidth()+"\n"+grid.getHeight()+"\n";
		for (int i = 0; i < grid.getHeight(); i++) {
			for (int j = 0; j < grid.getWidth(); j++) {
				Piece piece = grid.getPiece(i,j);
				s += PieceType.getValuefromType(piece.getType()) +" "+ Orientation.getValuefromOri(piece.getOrientation())+"\n";
			}
		}
		return s;
	}

  /**
	 * Saves a file representing a grid
	 * 
	 * @param the grid
	 * @param outputFilePath the path of the file
	 * @return the file-formated string representing the grid
	 * @throws IOException if the file could not be writen
	 */
	public static String serializeGrid(Grid grid, String outputFilePath) throws IOException {
		//Note : appending to a string for big grids is slow, this could be optimised by writing into the file directly, but kept that way for clarity and lack of time
		String fileFormatStringForGrid = getFileFormatStringFromGrid(grid);
		Files.writeString(Paths.get(outputFilePath), fileFormatStringForGrid);
		return fileFormatStringForGrid;
	}

  /**
	 * Builds a grid based on a file formated string representing it
	 * 
	 * @param fileFormatString the file-formated string
	 * @return the grid
	 * @throws IOException if the file format is invalid
	 */
	public static Grid getGridFromFileFormatString(String fileFormatString) throws IOException {
		String[] fileFormatLines = fileFormatString.split("\n");

		if (fileFormatLines.length < 3) {
			throw new IOException("Input file should have at least three lines, starting with one for width and one for height");
		}

		int width = 0;
		int height = 0;
		boolean validParsingWH = true;
		try {
			width = Integer.parseInt(fileFormatLines[0]);
			height = Integer.parseInt(fileFormatLines[1]);
		}
		catch (NumberFormatException e) {
			validParsingWH = false;
		}

		if (!validParsingWH || width<=0 || height<=0) {
			throw new IOException("Input file should start with one line for width and one line for height, both strictly positive integers");
		}
		if (fileFormatLines.length != width*height+2) {
			throw new IOException("Input file should start with one line for width and one line for height, then exactly width*height lines for each piece"); //TODO accept blank lines
		}

		Grid grid = new Grid(width, height);

		int l=2;
    for (int i=0; i<height; i++) {
    	for (int j=0; j<width; j++) { //l=2+i*width+j
				String[] fileFormatLine = fileFormatLines[l].split(" ");

				if (fileFormatLine.length != 2) {
					throw new IOException("Input file, line "+l+" piece "+(l-2)+": each piece should be described as type and orientation, seperated by a space");
				}

				int type = PieceType.getTypeMaxValue() + 1;
				int orientation = Orientation.getOrientationMaxValue() + 1;
				boolean validParsingTO = true;
				try {
					type = Integer.parseInt(fileFormatLine[0]);
					orientation = Integer.parseInt(fileFormatLine[1]);
				}
				catch (NumberFormatException e) {
					validParsingTO = false;
				}

				if (!validParsingTO || type<PieceType.getTypeMinValue() || type>PieceType.getTypeMaxValue() || orientation<Orientation.getOrientationMinValue() || orientation>Orientation.getOrientationMaxValue()) {
					throw new IOException("Input file, line "+l+" piece "+(l-2)+": each piece should be described as type and orientation, as integers in range ["+PieceType.getTypeMinValue()+","+PieceType.getTypeMaxValue()+"] and ["+Orientation.getOrientationMinValue()+","+Orientation.getOrientationMaxValue()+"]");
				}

				grid.setPiece(i,j, new Piece(i,j, PieceType.getTypefromValue(type), Orientation.getOrifromValue(orientation)));

				l++;
			}
		}

		return grid;
	}

  /**
	 * Loads a grid based on a file containing a file formated string representing it
	 * 
	 * @param inputFilePath the path of the file
	 * @return the grid
	 * @throws IOException if the file could not be read or the file format is invalid
	 */
	public static Grid unserializeGrid(String inputFilePath) throws IOException {
		return getGridFromFileFormatString(Files.readString(Paths.get(inputFilePath)));
	}
}
