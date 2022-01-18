package fr.dauphine.JavaAvance.Components;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.ArrayList;

public class PieceTypeTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_getOrientation() {
		assertEquals(Orientation.NORTH, PieceType.VOID.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.NORTH, PieceType.VOID.getOrientation(Orientation.EAST));
		assertEquals(Orientation.NORTH, PieceType.VOID.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.NORTH, PieceType.VOID.getOrientation(Orientation.WEST));

		assertEquals(Orientation.NORTH, PieceType.ONECONN.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.EAST, PieceType.ONECONN.getOrientation(Orientation.EAST));
		assertEquals(Orientation.SOUTH, PieceType.ONECONN.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.WEST, PieceType.ONECONN.getOrientation(Orientation.WEST));

		assertEquals(Orientation.NORTH, PieceType.BAR.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.EAST, PieceType.BAR.getOrientation(Orientation.EAST));
		assertEquals(Orientation.NORTH, PieceType.BAR.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.EAST, PieceType.BAR.getOrientation(Orientation.WEST));

		assertEquals(Orientation.NORTH, PieceType.TTYPE.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.EAST, PieceType.TTYPE.getOrientation(Orientation.EAST));
		assertEquals(Orientation.SOUTH, PieceType.TTYPE.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.WEST, PieceType.TTYPE.getOrientation(Orientation.WEST));

		assertEquals(Orientation.NORTH, PieceType.FOURCONN.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.NORTH, PieceType.FOURCONN.getOrientation(Orientation.EAST));
		assertEquals(Orientation.NORTH, PieceType.FOURCONN.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.NORTH, PieceType.FOURCONN.getOrientation(Orientation.WEST));

		assertEquals(Orientation.NORTH, PieceType.LTYPE.getOrientation(Orientation.NORTH));
		assertEquals(Orientation.EAST, PieceType.LTYPE.getOrientation(Orientation.EAST));
		assertEquals(Orientation.SOUTH, PieceType.LTYPE.getOrientation(Orientation.SOUTH));
		assertEquals(Orientation.WEST, PieceType.LTYPE.getOrientation(Orientation.WEST));

		assertEquals(null, PieceType.LTYPE.getOrientation(null)); //! undefined behaviour
	}

	@Test
	public void test_setConnectorsList() {
		LinkedList<Orientation> ll;

		ll = new LinkedList<Orientation>();
		assertEquals(ll, PieceType.VOID.setConnectorsList(null)); //!

		assertEquals(ll, PieceType.VOID.setConnectorsList(Orientation.NORTH));
		assertEquals(ll, PieceType.VOID.setConnectorsList(Orientation.EAST));
		assertEquals(ll, PieceType.VOID.setConnectorsList(Orientation.SOUTH)); //!
		assertEquals(ll, PieceType.VOID.setConnectorsList(Orientation.WEST)); //!


		ll = new LinkedList<Orientation>();ll.add(Orientation.NORTH);
		assertEquals(ll, PieceType.ONECONN.setConnectorsList(Orientation.NORTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.EAST);
		assertEquals(ll, PieceType.ONECONN.setConnectorsList(Orientation.EAST));
		ll = new LinkedList<Orientation>();ll.add(Orientation.SOUTH);
		assertEquals(ll, PieceType.ONECONN.setConnectorsList(Orientation.SOUTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.WEST);
		assertEquals(ll, PieceType.ONECONN.setConnectorsList(Orientation.WEST));

		ll = new LinkedList<Orientation>();ll.add(Orientation.NORTH);ll.add(Orientation.SOUTH);
		assertEquals(ll, PieceType.BAR.setConnectorsList(Orientation.NORTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.EAST);ll.add(Orientation.WEST);
		assertEquals(ll, PieceType.BAR.setConnectorsList(Orientation.EAST));
		ll = new LinkedList<Orientation>();ll.add(Orientation.SOUTH);ll.add(Orientation.NORTH);
		assertEquals(ll, PieceType.BAR.setConnectorsList(Orientation.SOUTH)); //!
		ll = new LinkedList<Orientation>();ll.add(Orientation.WEST);ll.add(Orientation.EAST);
		assertEquals(ll, PieceType.BAR.setConnectorsList(Orientation.WEST)); //!

		ll = new LinkedList<Orientation>();ll.add(Orientation.NORTH);ll.add(Orientation.EAST);ll.add(Orientation.WEST);
		assertEquals(ll, PieceType.TTYPE.setConnectorsList(Orientation.NORTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.EAST);ll.add(Orientation.SOUTH);ll.add(Orientation.NORTH);
		assertEquals(ll, PieceType.TTYPE.setConnectorsList(Orientation.EAST));
		ll = new LinkedList<Orientation>();ll.add(Orientation.SOUTH);ll.add(Orientation.WEST);ll.add(Orientation.EAST);
		assertEquals(ll, PieceType.TTYPE.setConnectorsList(Orientation.SOUTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.WEST);ll.add(Orientation.NORTH);ll.add(Orientation.SOUTH);
		assertEquals(ll, PieceType.TTYPE.setConnectorsList(Orientation.WEST));

		ll = new LinkedList<Orientation>();ll.add(Orientation.NORTH);ll.add(Orientation.EAST);ll.add(Orientation.SOUTH);ll.add(Orientation.WEST);
		assertEquals(ll, PieceType.FOURCONN.setConnectorsList(Orientation.NORTH));
		assertEquals(ll, PieceType.FOURCONN.setConnectorsList(Orientation.EAST)); //!
		assertEquals(ll, PieceType.FOURCONN.setConnectorsList(Orientation.SOUTH)); //!
		assertEquals(ll, PieceType.FOURCONN.setConnectorsList(Orientation.WEST)); //!

		ll = new LinkedList<Orientation>();ll.add(Orientation.NORTH);ll.add(Orientation.EAST);
		assertEquals(ll, PieceType.LTYPE.setConnectorsList(Orientation.NORTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.EAST);ll.add(Orientation.SOUTH);
		assertEquals(ll, PieceType.LTYPE.setConnectorsList(Orientation.EAST));
		ll = new LinkedList<Orientation>();ll.add(Orientation.SOUTH);ll.add(Orientation.WEST);
		assertEquals(ll, PieceType.LTYPE.setConnectorsList(Orientation.SOUTH));
		ll = new LinkedList<Orientation>();ll.add(Orientation.WEST);ll.add(Orientation.NORTH);
		assertEquals(ll, PieceType.LTYPE.setConnectorsList(Orientation.WEST));
	}

	@Test
	public void test_getNbConnectors() {
		assertEquals(0, PieceType.VOID.getNbConnectors());
		assertEquals(1, PieceType.ONECONN.getNbConnectors());
		assertEquals(2, PieceType.BAR.getNbConnectors());
		assertEquals(3, PieceType.TTYPE.getNbConnectors());
		assertEquals(4, PieceType.FOURCONN.getNbConnectors());
		assertEquals(2, PieceType.LTYPE.getNbConnectors());
	}

	@Test
	public void test_getListOfPossibleOri() {
		ArrayList<Orientation> al;

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.VOID.getListOfPossibleOri());

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);al.add(Orientation.SOUTH);al.add(Orientation.WEST);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOri());

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);
		assertEquals(al, PieceType.BAR.getListOfPossibleOri());

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);al.add(Orientation.SOUTH);al.add(Orientation.WEST);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOri());

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOri());

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);al.add(Orientation.SOUTH);al.add(Orientation.WEST);
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOri());
	}

	@Test
	public void test_getListOfPossibleOrientationsUnderConstraint() {
		ArrayList<Orientation> al;

		al = new ArrayList<Orientation>();
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(null, true)); //!
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(null, true)); //!

		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));
		assertEquals(al, PieceType.VOID.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		al = new ArrayList<Orientation>();al.add(Orientation.EAST);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		al = new ArrayList<Orientation>();al.add(Orientation.SOUTH);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		al = new ArrayList<Orientation>();al.add(Orientation.WEST);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));

		al = new ArrayList<Orientation>();al.add(Orientation.EAST);al.add(Orientation.SOUTH);al.add(Orientation.WEST);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.SOUTH);al.add(Orientation.WEST);al.add(Orientation.NORTH);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
		al = new ArrayList<Orientation>();al.add(Orientation.WEST);al.add(Orientation.NORTH);al.add(Orientation.EAST);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);al.add(Orientation.SOUTH);
		assertEquals(al, PieceType.ONECONN.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));
		al = new ArrayList<Orientation>();al.add(Orientation.EAST);
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		assertEquals(al, PieceType.BAR.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.EAST);al.add(Orientation.WEST);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		al = new ArrayList<Orientation>();al.add(Orientation.EAST);al.add(Orientation.SOUTH);al.add(Orientation.NORTH);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		al = new ArrayList<Orientation>();al.add(Orientation.SOUTH);al.add(Orientation.WEST);al.add(Orientation.EAST);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		al = new ArrayList<Orientation>();al.add(Orientation.WEST);al.add(Orientation.NORTH);al.add(Orientation.SOUTH);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));

		al = new ArrayList<Orientation>();al.add(Orientation.SOUTH);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.WEST);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.EAST);
		assertEquals(al, PieceType.TTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));

		al = new ArrayList<Orientation>();
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));
		assertEquals(al, PieceType.FOURCONN.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));

		al = new ArrayList<Orientation>();al.add(Orientation.NORTH);al.add(Orientation.WEST);
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, true));
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.EAST);al.add(Orientation.NORTH);
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, true));
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, false));
		al = new ArrayList<Orientation>();al.add(Orientation.SOUTH);al.add(Orientation.EAST);
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.SOUTH, true));
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.NORTH, false));
		al = new ArrayList<Orientation>();al.add(Orientation.WEST);al.add(Orientation.SOUTH);
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.WEST, true));
		assertEquals(al, PieceType.LTYPE.getListOfPossibleOrientationsUnderConstraint(Orientation.EAST, false));
	}

	@Test
	public void test_getValuefromType_pieceType() {
		assertEquals(0, PieceType.getValuefromType(PieceType.VOID));
		assertEquals(1, PieceType.getValuefromType(PieceType.ONECONN));
		assertEquals(2, PieceType.getValuefromType(PieceType.BAR));
		assertEquals(3, PieceType.getValuefromType(PieceType.TTYPE));
		assertEquals(4, PieceType.getValuefromType(PieceType.FOURCONN));
		assertEquals(5, PieceType.getValuefromType(PieceType.LTYPE));
	}

	@Test(expected = NullPointerException.class)
	public void test_getValuefromType_null() {
		PieceType.getValuefromType(null); //!
	}

	@Test
	public void test_getValue() {
		assertEquals(0, PieceType.VOID.getValue());
		assertEquals(1, PieceType.ONECONN.getValue());
		assertEquals(2, PieceType.BAR.getValue());
		assertEquals(3, PieceType.TTYPE.getValue());
		assertEquals(4, PieceType.FOURCONN.getValue());
		assertEquals(5, PieceType.LTYPE.getValue());
	}

	@Test
	public void test_getTypefromValue() {
		assertEquals(PieceType.VOID, PieceType.getTypefromValue(0));
		assertEquals(PieceType.ONECONN, PieceType.getTypefromValue(1));
		assertEquals(PieceType.BAR, PieceType.getTypefromValue(2));
		assertEquals(PieceType.TTYPE, PieceType.getTypefromValue(3));
		assertEquals(PieceType.FOURCONN, PieceType.getTypefromValue(4));
		assertEquals(PieceType.LTYPE, PieceType.getTypefromValue(5));
		assertEquals(null, PieceType.getTypefromValue(6));
		assertEquals(null, PieceType.getTypefromValue(-1));
	}

	@Test
	public void test_getTypeMinValue() {
		assertEquals(0, PieceType.getTypeMinValue());
	}

	@Test
	public void test_getTypeMaxValuee() {
		assertEquals(5, PieceType.getTypeMaxValue());
	}


	/**
	 * Gets the minimum integer value to represent a piece type when serializing
	 * 
	 * @return the minimum integer value to represent an piece type when serializing
	 *
	public static int getTypeMinValue() {
		return 0;
	}

	/**
	 * Gets the maximum integer value to represent a piece type when serializing
	 * 
	 * @return the maximum integer value to represent an piece type when serializing
	 *
	public static int getTypeMaxValue() {
		return 5;
	}*/
}
