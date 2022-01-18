package fr.dauphine.JavaAvance.Solve;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.GUI.Grid;

public class GeneratorTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_getConnectedComponents_allConnected() {
		/* test TTYPE, LTYPE, FOURCONN, all connections
			┌┬┐
			├┼┤
			└┴┘
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.LTYPE,Orientation.EAST));  grid.setPiece(0,1, new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST));  grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.TTYPE,Orientation.WEST));
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.TTYPE,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		ArrayList<ArrayList<Piece>> cc = Generator.getConnectedComponents(grid);
		assertEquals(1, cc.size());
		assertEquals(9, cc.get(0).size());
	}

	@Test
	public void test_getConnectedComponents_void() {
		/* test VOID, no connections
		*/
		Grid grid = new Grid(3,3);
		grid.setPiece(0,0, new Piece(0,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,1, new Piece(1,1,PieceType.VOID,Orientation.NORTH)); grid.setPiece(1,2, new Piece(1,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.VOID,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.VOID,Orientation.NORTH));  grid.setPiece(2,2, new Piece(2,2,PieceType.VOID,Orientation.NORTH));
		
		ArrayList<ArrayList<Piece>> cc = Generator.getConnectedComponents(grid);
		assertEquals(0, cc.size());
	}

	@Test
	public void test_getConnectedComponents_twoBarsSquare() {
		/* test ONECONN, BAR, three connected components
			╻  
			│┌┐
			╹└┘
			╺─╸
		*/
		Grid grid = new Grid(3,4);
		grid.setPiece(0,0, new Piece(0,0,PieceType.ONECONN,Orientation.SOUTH));  grid.setPiece(0,1, new Piece(0,1,PieceType.VOID,Orientation.NORTH));    grid.setPiece(0,2, new Piece(0,2,PieceType.VOID,Orientation.NORTH));
		grid.setPiece(1,0, new Piece(1,0,PieceType.BAR,Orientation.NORTH));  grid.setPiece(1,1, new Piece(1,1,PieceType.LTYPE,Orientation.EAST)); grid.setPiece(1,2, new Piece(1,2,PieceType.LTYPE,Orientation.SOUTH));
		grid.setPiece(2,0, new Piece(2,0,PieceType.ONECONN,Orientation.NORTH)); grid.setPiece(2,1, new Piece(2,1,PieceType.LTYPE,Orientation.NORTH));    grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		grid.setPiece(3,0, new Piece(3,0,PieceType.ONECONN,Orientation.EAST)); grid.setPiece(3,1, new Piece(3,1,PieceType.BAR,Orientation.EAST));    grid.setPiece(3,2, new Piece(3,2,PieceType.BAR,Orientation.WEST));

		ArrayList<ArrayList<Piece>> cc = Generator.getConnectedComponents(grid);
		assertEquals(3, cc.size());
		assertEquals(3, cc.get(0).size());
		assertEquals(4, cc.get(1).size());
		assertEquals(3, cc.get(2).size());
	}

// Note : due to the random nature of the following functions, the following tests will have a statistics, and can technically fail, or pass where they should fail
// TODO be able to manipulate the random seed to have consistent tests ? But would remove the possibility of finding a bug by chance ?

	@Test
	public void test_fillRandomPieces() {
		Grid grid = new Grid(100,100);
		Generator.fillRandomPieces(grid, 0.5);

		assertEquals(true, grid.allPiecesTotallyConnected());

		int[] numberOfType = {0,0,0,0,0,0};
		int[] numberOfOrientation = {0,0,0,0};
		double numberOfPieces = 10000;
		double tolerance = 0.1; //the lower the most tolerance
		for(int i=0; i<100; i++) {
			for(int j=0; j<100; j++) {
				Piece piece = grid.getPiece(i,j);
				numberOfType[piece.getType().getValue()]++;
				numberOfOrientation[piece.getOrientation().getValue()]++;
			}
		}
		//The following could technically fail even with the high tolerance
		assertTrue(""+numberOfType[0],numberOfType[0]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[1],numberOfType[1]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[2],numberOfType[2]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[3],numberOfType[3]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[4],numberOfType[4]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[5],numberOfType[5]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfOrientation[0],numberOfOrientation[0]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[1],numberOfOrientation[1]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[2],numberOfOrientation[2]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[3],numberOfOrientation[3]/numberOfPieces > tolerance/4);
	}

	@Test
	public void test_fillRandomPiecesWithConnectedComponents() {
		Grid grid = new Grid(30,30);
		Generator.fillRandomPiecesWithConnectedComponents(grid, 15, 0.5);

		assertEquals(true, grid.allPiecesTotallyConnected());
		int nbcc = Generator.getConnectedComponents(grid).size();
		assertEquals(15, nbcc);

		int[] numberOfType = {0,0,0,0,0,0};
		int[] numberOfOrientation = {0,0,0,0};
		double numberOfPieces = 900;
		double tolerance = 0.1; //the lower the most tolerance
		for(int i=0; i<30; i++) {
			for(int j=0; j<30; j++) {
				Piece piece = grid.getPiece(i,j);
				numberOfType[piece.getType().getValue()]++;
				numberOfOrientation[piece.getOrientation().getValue()]++;
			}
		}
		//The following could technically fail even with the high tolerance
		assertTrue(""+numberOfType[0],numberOfType[0]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[1],numberOfType[1]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[2],numberOfType[2]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[3],numberOfType[3]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[4],numberOfType[4]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfType[5],numberOfType[5]/numberOfPieces > tolerance/6);
		assertTrue(""+numberOfOrientation[0],numberOfOrientation[0]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[1],numberOfOrientation[1]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[2],numberOfOrientation[2]/numberOfPieces > tolerance/4);
		assertTrue(""+numberOfOrientation[3],numberOfOrientation[3]/numberOfPieces > tolerance/4);
	}

	@Test
	public void test_shufflePiecesOrientation() {
		Grid grid = new Grid(100,100);
		Generator.fillRandomPieces(grid, 0.5);
		//Grid gridCopy = new Grid(grid); TODO implement deep copy
		Generator.shufflePiecesOrientation(grid);

		assertEquals(false, grid.allPiecesTotallyConnected()); //This could technically fail, it is very very unlikely
		//TODO test a percent of the pieces changed orientation, and a lower percent kept orientation
	}
}
