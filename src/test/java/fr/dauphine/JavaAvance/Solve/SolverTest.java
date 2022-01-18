package fr.dauphine.JavaAvance.Solve;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

public class SolverTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_solveGridDepthFirstSearch_solvable() throws IllegalStateException {
		Grid grid = new Grid(100,100);
		Generator.fillRandomPieces(grid, 0.5);
		Generator.shufflePiecesOrientation(grid);

		Solver.solveGridDepthFirstSearch(grid);

		assertEquals(true, grid.allPiecesTotallyConnected());
	}

	@Test(expected = IllegalStateException.class)
	public void test_solveGridDepthFirstSearch_notSolvable() throws IllegalStateException {
		Grid grid = new Grid(100,100);
		Generator.fillRandomPieces(grid, 0.5);
		Generator.shufflePiecesOrientation(grid);

		grid.setPiece(99,99, new Piece(99,99,PieceType.FOURCONN,Orientation.NORTH));

		Solver.solveGridDepthFirstSearch(grid);
	}

	@Test
	public void test_getLibertyForPiece() {
		ArrayList<Orientation> al;

		/* test TTYPE, LTYPE, FOURCONN, all connections
			┌┬╸
			├┼┐
			└┼┘
		*/
		Grid grid = new Grid(3,3);
		Solver.grid = grid;
		Piece p00 = new Piece(0,0,PieceType.LTYPE,Orientation.EAST);
		Piece p01 = new Piece(0,1,PieceType.TTYPE,Orientation.SOUTH);
		Piece p02 = new Piece(0,2,PieceType.ONECONN,Orientation.WEST);
		Piece p12 = new Piece(1,2,PieceType.LTYPE,Orientation.SOUTH);
		Piece p21 = new Piece(2,1,PieceType.FOURCONN,Orientation.NORTH);
		grid.setPiece(0,0, p00); grid.setPiece(0,1, p01); grid.setPiece(0,2, p02);
		grid.setPiece(1,0, new Piece(1,0,PieceType.TTYPE,Orientation.EAST)); grid.setPiece(1,1, new Piece(1,1,PieceType.FOURCONN,Orientation.NORTH)); grid.setPiece(1,2, p12);
		grid.setPiece(2,0, new Piece(2,0,PieceType.LTYPE,Orientation.NORTH)); grid.setPiece(2,1, p21); grid.setPiece(2,2, new Piece(2,2,PieceType.LTYPE,Orientation.WEST));
		
		al = new ArrayList<Orientation>(); al.add(Orientation.EAST);
		assertEquals(al, Solver.getLibertyForPiece(p00));

		al = new ArrayList<Orientation>();
		assertEquals(al, Solver.getLibertyForPiece(p21));

		al = new ArrayList<Orientation>(); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, Solver.getLibertyForPiece(p02));

		p01.setFixed(true);
		al = new ArrayList<Orientation>(); al.add(Orientation.WEST);
		assertEquals(al, Solver.getLibertyForPiece(p02));

		p01.setFixed(false);
		p12.setFixed(true);
		al = new ArrayList<Orientation>(); al.add(Orientation.WEST);
		assertEquals(al, Solver.getLibertyForPiece(p02));
		
		p01.setOrientation(Orientation.WEST);
		p01.setFixed(true);
		al = new ArrayList<Orientation>();
		assertEquals(al, Solver.getLibertyForPiece(p02));

		p12.setOrientation(Orientation.WEST);
		p01.setOrientation(Orientation.SOUTH);
		al = new ArrayList<Orientation>();
		assertEquals(al, Solver.getLibertyForPiece(p02));
	}
	
	//TODO test_reduceLibertyBasedOnConstraint : covered by test_getLibertyForPiece ?
	//TODO test_timeToString
}
