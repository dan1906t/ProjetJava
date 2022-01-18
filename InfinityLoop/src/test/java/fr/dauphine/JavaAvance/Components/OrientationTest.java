package fr.dauphine.JavaAvance.Components;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Orientation of a piece (also used for directions from a piece, or its connectors) enum
 */
public class OrientationTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_turn90Right() {
		assertEquals(Orientation.EAST, Orientation.NORTH.turn90Right());
		assertEquals(Orientation.SOUTH, Orientation.EAST.turn90Right());
		assertEquals(Orientation.WEST, Orientation.SOUTH.turn90Right());
		assertEquals(Orientation.NORTH, Orientation.WEST.turn90Right());
	}

	@Test
	public void test_getOpposedOrientation() {
		assertEquals(Orientation.SOUTH, Orientation.NORTH.getOpposedOrientation());
		assertEquals(Orientation.WEST, Orientation.EAST.getOpposedOrientation());
		assertEquals(Orientation.NORTH, Orientation.SOUTH.getOpposedOrientation());
		assertEquals(Orientation.EAST, Orientation.WEST.getOpposedOrientation());
	}

	@Test
	public void test_turn90Left() {
		assertEquals(Orientation.WEST, Orientation.NORTH.turn90Left());
		assertEquals(Orientation.NORTH, Orientation.EAST.turn90Left());
		assertEquals(Orientation.EAST, Orientation.SOUTH.turn90Left());
		assertEquals(Orientation.SOUTH, Orientation.WEST.turn90Left());
	}

	@Test
	public void test_getOpposedPieceCoordinates_pieceObject() {
		assertEquals(2, Orientation.NORTH.getOpposedPieceCoordinates(new Piece(2,3)).length);
		assertEquals(1, Orientation.NORTH.getOpposedPieceCoordinates(new Piece(2,3))[0]);
		assertEquals(3, Orientation.NORTH.getOpposedPieceCoordinates(new Piece(2,3))[1]);
		assertEquals(2, Orientation.SOUTH.getOpposedPieceCoordinates(new Piece(0,5,PieceType.LTYPE,Orientation.NORTH)).length);
		assertEquals(1, Orientation.SOUTH.getOpposedPieceCoordinates(new Piece(0,5,PieceType.LTYPE,Orientation.NORTH))[0]);
		assertEquals(5, Orientation.SOUTH.getOpposedPieceCoordinates(new Piece(0,5,PieceType.LTYPE,Orientation.NORTH))[1]);
		assertEquals(2, Orientation.EAST.getOpposedPieceCoordinates(new Piece(25,25,2,3)).length);
		assertEquals(25, Orientation.EAST.getOpposedPieceCoordinates(new Piece(25,25,2,3))[0]);
		assertEquals(26, Orientation.EAST.getOpposedPieceCoordinates(new Piece(25,25,2,3))[1]);
		assertEquals(2, Orientation.WEST.getOpposedPieceCoordinates(new Piece(0,0,true,false,true,false)).length);
		assertEquals(0, Orientation.WEST.getOpposedPieceCoordinates(new Piece(0,0,true,false,true,false))[0]);
		assertEquals(-1, Orientation.WEST.getOpposedPieceCoordinates(new Piece(0,0,true,false,true,false))[1]);
	}

	@Test(expected = NullPointerException.class)
	public void test_getOpposedPieceCoordinates_null() {
		Orientation.WEST.getOpposedPieceCoordinates(null); //! Undefined behaviour
	}

	@Test
	public void test_getValuefromOri_orientation()  {
		assertEquals(0, Orientation.getValuefromOri(Orientation.NORTH));
		assertEquals(1, Orientation.getValuefromOri(Orientation.EAST));
		assertEquals(2, Orientation.getValuefromOri(Orientation.SOUTH));
		assertEquals(3, Orientation.getValuefromOri(Orientation.WEST));
	}

	@Test(expected = NullPointerException.class)
	public void test_getValuefromOri_null() {
		Orientation.getValuefromOri(null); //!
	}

	@Test
	public void test_getValue() {
		assertEquals(0, Orientation.NORTH.getValue());
		assertEquals(1, Orientation.EAST.getValue());
		assertEquals(2, Orientation.SOUTH.getValue());
		assertEquals(3, Orientation.WEST.getValue());
	}

	@Test
	public void test_getOrifromValue() {
		assertEquals(Orientation.NORTH, Orientation.getOrifromValue(0));
		assertEquals(Orientation.EAST, Orientation.getOrifromValue(1));
		assertEquals(Orientation.SOUTH, Orientation.getOrifromValue(2));
		assertEquals(Orientation.WEST, Orientation.getOrifromValue(3));
		assertEquals(null, Orientation.getOrifromValue(4));
		assertEquals(null, Orientation.getOrifromValue(-1));
	}

	@Test
	public void test_getOrientationMinValue() {
		assertEquals(0, Orientation.getOrientationMinValue());
	}

	@Test
	public void test_getOrientationMaxValue() {
		assertEquals(3, Orientation.getOrientationMaxValue());
	}
}
