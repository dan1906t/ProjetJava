package fr.dauphine.JavaAvance.Components;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.ArrayList;

public class PieceTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_setType_pieceType() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());
		ArrayList<Orientation> al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		LinkedList<Orientation> ll = new LinkedList<Orientation>(); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST); ll.add(Orientation.EAST);
		assertEquals(ll, piece.getConnectors());

		piece.setType(PieceType.BAR);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.BAR, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());
		al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST);
		assertEquals(al, piece.getPossibleOrientations());
		ll = new LinkedList<Orientation>(); ll.add(Orientation.NORTH); ll.add(Orientation.SOUTH);
		assertEquals(ll, piece.getConnectors());
	}

	@Test
	public void test_setType_value() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());
		ArrayList<Orientation> al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		LinkedList<Orientation> ll = new LinkedList<Orientation>(); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST); ll.add(Orientation.EAST);
		assertEquals(ll, piece.getConnectors());

		piece.setType(2);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.BAR, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());
		al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST);
		assertEquals(al, piece.getPossibleOrientations());
		ll = new LinkedList<Orientation>(); ll.add(Orientation.NORTH); ll.add(Orientation.SOUTH);
		assertEquals(ll, piece.getConnectors());
	}

	@Test(expected = NullPointerException.class)
	public void test_setType_null() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);
		piece.setType(null); //! undefined behaviour, might actually change the type but not the rest TODO check for that and correct it in the code
	}

	@Test
	public void test_setOrientation_value() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());
		ArrayList<Orientation> al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		LinkedList<Orientation> ll = new LinkedList<Orientation>(); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST); ll.add(Orientation.EAST);
		assertEquals(ll, piece.getConnectors());

		piece.setOrientation(1);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.EAST, piece.getOrientation());
		al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		ll = new LinkedList<Orientation>(); ll.add(Orientation.EAST); ll.add(Orientation.SOUTH); ll.add(Orientation.NORTH);
		assertEquals(ll, piece.getConnectors());
	}

	@Test
	public void test_setTypeAndOrientationFromConnectors() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());
		ArrayList<Orientation> al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		LinkedList<Orientation> ll = new LinkedList<Orientation>(); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST); ll.add(Orientation.EAST);
		assertEquals(ll, piece.getConnectors());

		piece.setTypeAndOrientationFromConnectors(true, true, true, true);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.FOURCONN, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());
		al = new ArrayList<Orientation>(); al.add(Orientation.NORTH);
		assertEquals(al, piece.getPossibleOrientations());
		ll = new LinkedList<Orientation>(); ll.add(Orientation.NORTH); ll.add(Orientation.EAST); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST);
		assertEquals(ll, piece.getConnectors());

		piece.setTypeAndOrientationFromConnectors(true, true, true, false);
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.EAST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, true, false, true);
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, false, true, true);
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.WEST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, true, true, true);
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, true, false, false);
		assertEquals(PieceType.LTYPE, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, false, false, true);
		assertEquals(PieceType.LTYPE, piece.getType());
		assertEquals(Orientation.WEST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, false, true, true);
		assertEquals(PieceType.LTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, true, true, false);
		assertEquals(PieceType.LTYPE, piece.getType());
		assertEquals(Orientation.EAST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, false, true, false);
		assertEquals(PieceType.BAR, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, true, false, true);
		assertEquals(PieceType.BAR, piece.getType());
		assertEquals(Orientation.EAST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(true, false, false, false);
		assertEquals(PieceType.ONECONN, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, true, false, false);
		assertEquals(PieceType.ONECONN, piece.getType());
		assertEquals(Orientation.EAST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, false, true, false);
		assertEquals(PieceType.ONECONN, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, false, false, true);
		assertEquals(PieceType.ONECONN, piece.getType());
		assertEquals(Orientation.WEST, piece.getOrientation());

		piece.setTypeAndOrientationFromConnectors(false, false, false, false);
		assertEquals(PieceType.VOID, piece.getType());
		assertEquals(Orientation.NORTH, piece.getOrientation());
	}

	@Test
	public void test_constructor_connectors() {
		Piece piece = new Piece(5,3,false, false, false, true);
		assertEquals(PieceType.ONECONN, piece.getType());
		assertEquals(Orientation.WEST, piece.getOrientation());
	}

	@Test
	public void test_turn() {
		Piece piece = new Piece(5,3,PieceType.TTYPE,Orientation.SOUTH);

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.SOUTH, piece.getOrientation());
		ArrayList<Orientation> al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		LinkedList<Orientation> ll = new LinkedList<Orientation>(); ll.add(Orientation.SOUTH); ll.add(Orientation.WEST); ll.add(Orientation.EAST);
		assertEquals(ll, piece.getConnectors());

		piece.turn();

		assertEquals(5, piece.getPosY());
		assertEquals(3, piece.getPosX());
		assertEquals(PieceType.TTYPE, piece.getType());
		assertEquals(Orientation.WEST, piece.getOrientation());
		al = new ArrayList<Orientation>(); al.add(Orientation.NORTH); al.add(Orientation.EAST); al.add(Orientation.SOUTH); al.add(Orientation.WEST);
		assertEquals(al, piece.getPossibleOrientations());
		ll = new LinkedList<Orientation>(); ll.add(Orientation.WEST); ll.add(Orientation.NORTH); ll.add(Orientation.SOUTH);
		assertEquals(ll, piece.getConnectors());
	}
}
