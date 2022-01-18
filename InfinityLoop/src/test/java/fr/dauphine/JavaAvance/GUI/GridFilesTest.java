package fr.dauphine.JavaAvance.GUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import fr.dauphine.JavaAvance.GUI.Grid;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;

public class GridFilesTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_getFileFormatStringFromGrid_allConnections() {
		/* test TTYPE, LTYPE, FOURCONN
			┌┬┐
			├┼┤
			└┴┘
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.TTYPE,Orientation.WEST));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.TTYPE,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		String s =
		  "3\n"
		+ "3\n"
		+ "5 1\n"
		+ "3 2\n"
		+ "5 2\n"
		+ "3 1\n"
		+ "4 0\n"
		+ "3 3\n"
		+ "5 0\n"
		+ "3 0\n"
		+ "5 3\n";
		assertEquals(s, GridFiles.getFileFormatStringFromGrid(grid));
	}

	@Test
	public void test_getFileFormatStringFromGrid_twoBars() {
		/* test VOID, ONECONN, BAR
			╻  
			│  
			╹  
			╺─╸
		*/
		Grid grid = new Grid(3,4);
		grid.setPiece(0,0, new Piece(0,0,PieceType.ONECONN,Orientation.SOUTH));  grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.BAR,Orientation.NORTH));  grid.setPiece(1,1, new Piece(1,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.ONECONN,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(3,0, new Piece(3,0,PieceType.ONECONN,Orientation.EAST)); grid.setPiece(3,1, new Piece(3,1,PieceType.BAR,Orientation.EAST));    grid.setPiece(3,2, new Piece(3,2,PieceType.ONECONN,Orientation.WEST));
		
		String s =
		  "3\n"
		+ "4\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 1\n"
		+ "2 1\n"
		+ "1 3\n";
		assertEquals(s, GridFiles.getFileFormatStringFromGrid(grid));
	}

	@Test(expected = NullPointerException.class)
	public void test_getFileFormatStringFromGrid_notInitializedCompletely() {
		Grid grid = new Grid(3,4);
		grid.setPiece(0,0, new Piece(0,0,PieceType.ONECONN,Orientation.SOUTH));  grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.BAR,Orientation.NORTH));  grid.setPiece(1,1, new Piece(1,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.ONECONN,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(3,0, new Piece(3,0,PieceType.ONECONN,Orientation.EAST)); grid.setPiece(3,1, new Piece(3,1,PieceType.BAR,Orientation.EAST));    grid.setPiece(3,2, new Piece(3,2,PieceType.ONECONN,Orientation.WEST));
		GridFiles.getFileFormatStringFromGrid(grid);
	}

	@Test(expected = NullPointerException.class)
	public void test_getFileFormatStringFromGrid_notInitialized() {
		Grid grid = new Grid(3,4);
		GridFiles.getFileFormatStringFromGrid(grid);
	}

	@Test(expected = NullPointerException.class)
	public void test_getFileFormatStringFromGrid_null() {
		GridFiles.getFileFormatStringFromGrid(null);
	}

	@Test
	public void test_getGridFromFileFormatString_allConnections() throws IOException {
		/* test TTYPE, LTYPE, FOURCONN
			┌┬┐
			├┼┤
			└┴┘
		*/
		String s =
		  "3\n"
		+ "3\n"
		+ "5 1\n"
		+ "3 2\n"
		+ "5 2\n"
		+ "3 1\n"
		+ "4 0\n"
		+ "3 3\n"
		+ "5 0\n"
		+ "3 0\n"
		+ "5 3\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);

		assertEquals(3, grid.getWidth());
		assertEquals(3, grid.getHeight());

		assertEquals(PieceType.LTYPE, grid.getPiece(0,0).getType());
		assertEquals(Orientation.EAST, grid.getPiece(0,0).getOrientation());
		assertEquals(PieceType.TTYPE, grid.getPiece(0,1).getType());
		assertEquals(Orientation.SOUTH, grid.getPiece(0,1).getOrientation());
		assertEquals(PieceType.LTYPE, grid.getPiece(0,2).getType());
		assertEquals(Orientation.SOUTH, grid.getPiece(0,2).getOrientation());
		assertEquals(PieceType.TTYPE, grid.getPiece(1,0).getType());
		assertEquals(Orientation.EAST, grid.getPiece(1,0).getOrientation());
		assertEquals(PieceType.FOURCONN, grid.getPiece(1,1).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(1,1).getOrientation());
		assertEquals(PieceType.TTYPE, grid.getPiece(1,2).getType());
		assertEquals(Orientation.WEST, grid.getPiece(1,2).getOrientation());
		assertEquals(PieceType.LTYPE, grid.getPiece(2,0).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(2,0).getOrientation());
		assertEquals(PieceType.TTYPE, grid.getPiece(2,1).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(2,1).getOrientation());
		assertEquals(PieceType.LTYPE, grid.getPiece(2,2).getType());
		assertEquals(Orientation.WEST, grid.getPiece(2,2).getOrientation());
	}

	@Test
	public void test_getGridFromFileFormatString_twoBars() throws IOException {
		/* test VOID, ONECONN, BAR
			╻  
			│  
			╹  
			╺─╸
		*/
		String s =
		  "3\n"
		+ "4\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 1\n"
		+ "2 1\n"
		+ "1 3\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);

		assertEquals(3, grid.getWidth());
		assertEquals(4, grid.getHeight());

		assertEquals(PieceType.ONECONN, grid.getPiece(0,0).getType());
		assertEquals(Orientation.SOUTH, grid.getPiece(0,0).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(0,1).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(0,1).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(0,2).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(0,2).getOrientation());
		assertEquals(PieceType.BAR, grid.getPiece(1,0).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(1,0).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(1,1).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(1,1).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(1,2).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(1,2).getOrientation());
		assertEquals(PieceType.ONECONN, grid.getPiece(2,0).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(2,0).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(2,1).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(2,1).getOrientation());
		assertEquals(PieceType.VOID, grid.getPiece(2,2).getType());
		assertEquals(Orientation.NORTH, grid.getPiece(2,2).getOrientation());
		assertEquals(PieceType.ONECONN, grid.getPiece(3,0).getType());
		assertEquals(Orientation.EAST, grid.getPiece(3,0).getOrientation());
		assertEquals(PieceType.BAR, grid.getPiece(3,1).getType());
		assertEquals(Orientation.EAST, grid.getPiece(3,1).getOrientation());
		assertEquals(PieceType.ONECONN, grid.getPiece(3,2).getType());
		assertEquals(Orientation.WEST, grid.getPiece(3,2).getOrientation());
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_missingOneLine() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_oneLineMore() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_missingOneLineForDimensions() throws IOException {
		String s =
		  "3\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_unknownCharacter() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "1 2 a\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidOrientation1() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "1 4\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidOrientation2() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "1 -1\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidType1() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "6 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidType2() throws IOException {
		String s =
		  "3\n"
		+ "3\n"
		+ "-1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidWidth() throws IOException {
		String s =
		  "0\n"
		+ "3\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	@Test(expected = IOException.class)
	public void test_getFileFormatStringFromGrid_invalidHeight() throws IOException {
		String s =
		  "3\n"
		+ "0\n"
		+ "1 2\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "2 0\n"
		+ "0 0\n"
		+ "0 0\n"
		+ "1 0\n"
		+ "1 0\n"
		+ "0 0\n";

		Grid grid = GridFiles.getGridFromFileFormatString(s);
	}

	//TODO test_serializeGrid and test_unserializeGrid : test for file writing/reading ?
}
