package fr.dauphine.JavaAvance.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Type of a piece enum
 */
public enum PieceType {
	VOID,
	ONECONN,
	BAR,
	TTYPE,
	FOURCONN,
	LTYPE;

	/**
	 * Currates an orientation for the current piece type depending on its symetries. If, for the current piece type, the orientation is similar to a previous (following that order : NORTH, EAST, SOUTH, WEST) orientation, the first orientation is returned instead.
	 * 
	 * @params o an uncurrated orientation
	 * @return a currated orientation
	 */
	public Orientation getOrientation(Orientation o) {
		switch (this) {

			case VOID:
			case FOURCONN:
				return Orientation.NORTH;

			case BAR:
				switch(o) {
					case NORTH:
					case SOUTH:
						return Orientation.NORTH;

					case EAST:
					case WEST:
						return Orientation.EAST;
				}
		}
		return o;
	};

	/**
	 * Gets the list of connectors for the current piece type in the specified orientation. Called set because will be used to set said list in class Piece
	 * 
	 * @params o the orientation
	 * @return a list of orientations representing the directions of the current piece's connectors
	 */
	public LinkedList<Orientation> setConnectorsList(Orientation o) {
		LinkedList<Orientation> l = new LinkedList<Orientation>();
		
		switch (this) {

			case VOID:
				break;

			case ONECONN:
				l.add(o);
				break;

			case BAR:
				l.add(o);
				l.add(o.getOpposedOrientation());
				break;

			case TTYPE:
				l.add(o);
				l.add(o.turn90Right());
				l.add(o.turn90Left());
				break;

			case FOURCONN:
				for (Orientation or : Orientation.values())
				{
					l.add(or);
				}
				break;

			case LTYPE:
				l.add(o);
				l.add(o.turn90Right());
				break;
		}

		return l;
	}

	/**
	 * Gets the number of connectors for the current piece type
	 * 
	 * @return the number of connectors for the current piece type
	 */
	public int getNbConnectors()
	{
		return setConnectorsList(Orientation.NORTH).size();
	}

	/**
	 * Gets the list of all possible (unique) orientations for the current piece type
	 * 
	 * @return the list of all possible (unique) orientations for the current piece type
	 */
	public ArrayList<Orientation> getListOfPossibleOri(){
		ArrayList<Orientation> l = new ArrayList<Orientation>();
		switch (this) {

			case VOID:
			case FOURCONN:
				l.add(Orientation.NORTH);
				break;

			case BAR:
				l.add(Orientation.NORTH);
				l.add(Orientation.EAST);
				break;

			case ONECONN:
			case TTYPE:
			case LTYPE:
				for (Orientation o : Orientation.values())
				{
					l.add(o);
				}
				break;
		}
		return l;
	}

	/**
	 * Gets the list of all valid orientations for the current piece type under a single constraint (the piece should or should not be connected in one direction)
	 * 
	 * @params d direction of the constraint
	 * @params shouldConnect true if the piece should connect in that direction
	 * @return the list of all possible (unique) orientations for the current piece type
	 */
	public ArrayList<Orientation> getListOfPossibleOrientationsUnderConstraint(Orientation d, boolean shouldConnect) {
		ArrayList<Orientation> l = new ArrayList<Orientation>();
		switch (this) {

			case VOID:
				if (!shouldConnect) {
					l.add(Orientation.NORTH);
				}
				break;

			case ONECONN:
				if (shouldConnect) {
					l.add(d);
				} else {
					l.add(d.turn90Right());
					l.add(d.getOpposedOrientation());
					l.add(d.turn90Left());
				}
				break;

			case BAR:
				if (shouldConnect) {
					l.add(this.getOrientation(d));
				} else {
					l.add(this.getOrientation(d.turn90Right()));
				}
				break;

			case TTYPE:
				if (shouldConnect) {
					l.add(d);
					l.add(d.turn90Right());
					l.add(d.turn90Left());
				} else {
					l.add(d.getOpposedOrientation());
				}
				break;

			case FOURCONN:
				if (shouldConnect) {
					l.add(Orientation.NORTH);
				}
				break;

			case LTYPE:
				if (shouldConnect) {
					l.add(d);
					l.add(d.turn90Left());
				} else {
					l.add(d.getOpposedOrientation());
					l.add(d.turn90Right());
				}
				break;
		}
		return l;
	}

	/**
	 * Serializes a piece type
	 * 
	 * @param type a piece type
	 * @return the corresponding integer value representing the piece type
	 */
	public static int getValuefromType(PieceType type) {
		// This could be done cleaner with an enum constructor and private variable but with so little elements a switch case will do the job
		switch (type) {
			case VOID:
				return 0;
			case ONECONN:
				return 1;
			case BAR:
				return 2;
			case TTYPE:
				return 3;
			case FOURCONN:
				return 4;
			case LTYPE:
				return 5;
		}
		return 6;
	}

	/**
	 * Serializes the current piece type
	 * 
	 * @return the corresponding integer value representing the piece type
	 */
	public int getValue() {
		return getValuefromType(this);
	}

	/**
	 * Deserializes a piece type
	 * 
	 * @param type integer value representing a piece type
	 * @return the corresponding piece type, or null if not a valid value
	 */
	public static PieceType getTypefromValue(int value) {
		switch (value) {
			case 0:
				return VOID;
			case 1:
				return ONECONN;
			case 2:
				return BAR;
			case 3:
				return TTYPE;
			case 4:
				return FOURCONN;
			case 5:
				return LTYPE;
		}
		return null;
	}

	/**
	 * Gets the minimum integer value to represent a piece type when serializing
	 * 
	 * @return the minimum integer value to represent an piece type when serializing
	 */
	public static int getTypeMinValue() {
		return 0;
	}

	/**
	 * Gets the maximum integer value to represent a piece type when serializing
	 * 
	 * @return the maximum integer value to represent an piece type when serializing
	 */
	public static int getTypeMaxValue() {
		return 5;
	}
}
