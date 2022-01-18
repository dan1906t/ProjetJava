package fr.dauphine.JavaAvance.GUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;

public class GridTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_allPiecesTotallyConnected_trueAllConnections() {
		/* test TTYPE, LTYPE, FOURCONN, all connections
			┌┬┐
			├┼┤
			└┴┘
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.TTYPE,Orientation.WEST));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.TTYPE,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		assertEquals(true, grid.allPiecesTotallyConnected());
		assertEquals(true, grid.allPiecesValid());
	}

	@Test
	public void test_allPiecesTotallyConnected_trueVoid() {
		// test VOID, no connections
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,1, new Piece(1,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.VOID,Orientation.NORTH));  grid.setPiece(2,2, new Piece(2,2,PieceType.VOID,Orientation.NORTH));
		
		assertEquals(true, grid.allPiecesTotallyConnected());
		assertEquals(true, grid.allPiecesValid());
	}

	public void test_allPiecesTotallyConnected_trueTwoBars() {
		/* test ONECONN, BAR, two connected components
			╻  
			│  
			╹  
			╺─╸
		*/
		Grid grid = new Grid(3,4);
		grid.setPiece(0,0, new Piece(0,0,PieceType.ONECONN,Orientation.SOUTH));  grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.BAR,Orientation.NORTH));  grid.setPiece(1,1, new Piece(1,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.ONECONN,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(3,0, new Piece(3,0,PieceType.ONECONN,Orientation.EAST)); grid.setPiece(3,1, new Piece(3,1,PieceType.BAR,Orientation.EAST));    grid.setPiece(3,2, new Piece(3,2,PieceType.BAR,Orientation.WEST));
		
		assertEquals(true, grid.allPiecesTotallyConnected());
		assertEquals(true, grid.allPiecesValid());
	}

	@Test
	public void test_allPiecesTotallyConnected_falseOneInside() {
		/* test one connection broken inside
			┌┬┐
			├┼│
			└┴┘
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.BAR,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.TTYPE,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		assertEquals(false, grid.allPiecesTotallyConnected());
		assertEquals(false, grid.allPiecesValid());
	}

	@Test
	public void test_allPiecesTotallyConnected_falseOneOutside() {
		/* test one connection broken outside
			┌┬┐
			├┼┤
			└┼┘
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.TTYPE,Orientation.WEST));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.FOURCONN,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		assertEquals(false, grid.allPiecesTotallyConnected());
		assertEquals(false, grid.allPiecesValid());
	}

	//TODO test_allPiecesTotallyConnected_false : test for every piece disconnection ?

	@Test(expected = NullPointerException.class)
	public void test_allPiecesTotallyConnected_notInitializedCompletely() {
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.FOURCONN,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		grid.allPiecesTotallyConnected();
	}

	@Test(expected = NullPointerException.class)
	public void test_allPiecesValid_notInitializedCompletely() {
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.FOURCONN,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		grid.allPiecesValid();
	}

	@Test(expected = NullPointerException.class)
	public void test_allPiecesTotallyConnected_notInitialized() {
		Grid grid = new Grid(3,3);
		
		grid.allPiecesTotallyConnected();
	}

	@Test(expected = NullPointerException.class)
	public void test_allPiecesValid_notInitialized() {
		Grid grid = new Grid(3,3);
		
		grid.allPiecesValid();
	}

	//TODO test_print ? https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
}
