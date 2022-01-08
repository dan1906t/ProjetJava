package fr.dauphine.JavaAvance.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * Type of the piece enum
 * 
 */
public enum PieceType {
	// Each Type has a number of connectors and a specific value
	VOID;
	
	public Orientation getOrientation(Orientation o) {return o;};
	
	public LinkedList<Orientation> setConnectorsList(Orientation O) {
		LinkedList<Orientation> l = new LinkedList<Orientation>();
		return l;
	};
	
	public ArrayList<Orientation> getListOfPossibleOri(){
		return new ArrayList<Orientation>();
	}
	
	public static PieceType getTypefromValue(int value) {
		return this;
	}
}