package fr.dauphine.JavaAvance.Components;

import java.util.HashMap;

/**
 * 
 * Orientation of the piece enum
 * 
 */
public enum Orientation {
	/* Implement all the possible orientations and 
	 *  required methods to rotate
	 */
	NORTH,
	WEST,
	EAST,
	SOUTH;
	
	public static Orientation getOrifromValue(int t) {
		return this;
	}
	
	public Orientation turn90() {
		return this;
	}

}
