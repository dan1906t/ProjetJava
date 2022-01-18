package fr.dauphine.JavaAvance.Components;

import java.util.HashMap;

/**
 * Orientation of a piece (also used for directions from a piece, or its connectors) enum
 */
public enum Orientation {
	NORTH,
	EAST,
	SOUTH,
	WEST;

	/**
	 * Gets the orientation corresponding to the current one turned 90° clockwise
	 * 
	 * @return turned orientation
	 */	
	public Orientation turn90Right() {
		switch (this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
		}
		return this;	
	}

	/**
	 * Gets the orientation opposed to the current one, as the result of a 180° turn
	 * 
	 * @return opposed/turned orientation
	 */
	public Orientation getOpposedOrientation() {
		switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
		}
		return this;
	}

	/**
	 * Gets the orientation corresponding to the current one turned 90° anti-clockwise
	 * 
	 * @return turned orientation
	 */
	public Orientation turn90Left() {
		return this.turn90Right().getOpposedOrientation();	
	}

	/**
	 * Gets the coordinates of the neighbour piece of a piece in one direction (the one corresponding to this orientation). Doesn't check if out of the grid.
	 * 
	 * @param p the piece
	 * @return an integer array with y and x coordinates (in that order) of the neighbour of the piece
	 */
	public int[] getOpposedPieceCoordinates(Piece p) {
		int oppPieceX = p.getPosX();
		int oppPieceY = p.getPosY();
		
		switch (this) {
			case NORTH:
				oppPieceY -= 1;
				break;
			case EAST:
				oppPieceX += 1;
				break;
			case SOUTH:
				oppPieceY += 1;
				break;
			case WEST:
				oppPieceX -= 1;
				break;
		}
		return new int[] {oppPieceY, oppPieceX};
	}

	/**
	 * Serializes an orientation
	 * 
	 * @param orientation an orientation
	 * @return the corresponding integer value representing the orientation
	 */
	public static int getValuefromOri(Orientation orientation) {
		// This could be done cleaner with an enum constructor and private variable but with so little elements a switch case will do the job
		switch (orientation)
		{
				case NORTH:
					return 0;
				case EAST:
					return 1;
				case SOUTH:
					return 2;
				case WEST:
					return 3;
		}
		return 4;	
	}

	/**
	 * Serializes the current orientation
	 * 
	 * @return the corresponding integer value representing the orientation
	 */
	public int getValue() {
		return getValuefromOri(this);
	}

	/**
	 * Deserializes an orientation
	 * 
	 * @param value integer value representing an orientation
	 * @return the corresponding orientation, or null if not a valid value
	 */
	public static Orientation getOrifromValue(int value) {
		switch (value)
		{
				case 0:
					return NORTH;
				case 1:
					return EAST;
				case 2:
					return SOUTH;
				case 3:
					return WEST;
		}
		return null;	
	}

	/**
	 * Gets the minimum integer value to represent an orientation when serializing
	 * 
	 * @return the minimum integer value to represent an orientation when serializing
	 */
	public static int getOrientationMinValue() {
		return 0;	
	}

	/**
	 * Gets the maximum integer value to represent an orientation when serializing
	 * 
	 * @return the maximum integer value to represent an orientation when serializing
	 */
	public static int getOrientationMaxValue() {
		return 3;	
	}
}
