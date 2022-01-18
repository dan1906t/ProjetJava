package fr.dauphine.JavaAvance.Components;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Handling of pieces with general functions
 */
public class Piece {
	private int posX;// j
	private int posY;// i
	private PieceType type;
	private Orientation orientation;
	private LinkedList<Orientation> connectors;
	private ArrayList<Orientation> possibleOrientations;

	private boolean isFixed;

	public Piece(int posY, int posX, PieceType type, Orientation orientation) {
		this.posX = posX;
		this.posY = posY;
		this.type = type;
		this.orientation = type.getOrientation(orientation);
		this.connectors = type.setConnectorsList(orientation);
		this.possibleOrientations = type.getListOfPossibleOri();
		this.isFixed = false;
	}

	public Piece(int posY, int posX) {
		this(posY, posX, PieceType.VOID, Orientation.NORTH);
	}

	public Piece(int posY, int posX, int typeValue, int orientationValue) {
		this(posY, posX, PieceType.getTypefromValue(typeValue), Orientation.getOrifromValue(orientationValue));
	}

	/**
	 * ADDED. Constructor. Instanciates a piece knowing its coordinates and which directions should be connected
	 *
	 * @params posY ordinate of the piece in a grid
	 * @params posX abscissa of the piece in a grid
	 * @params hasTopConnector true if the piece should have a top connector
	 * @params hasRightConnector true if the piece should have a right connector
	 * @params hasBottomConnector true if the piece should have a bottom connector
	 * @params hasLeftConnector true if the piece should have a left connector
	 */
	public Piece(int posY, int posX, boolean hasTopConnector, boolean hasRightConnector, boolean hasBottomConnector, boolean hasLeftConnector) {
		this.posX = posX;
		this.posY = posY;
		this.setTypeAndOrientationFromConnectors (hasTopConnector, hasRightConnector, hasBottomConnector, hasLeftConnector);
		this.isFixed = false;
	}

	public void setPossibleOrientations(ArrayList<Orientation> possibleOrientations) {
		this.possibleOrientations = possibleOrientations;
	}

	public ArrayList<Orientation> getPossibleOrientations() {
		return this.possibleOrientations;
	}

	public LinkedList<Orientation> getInvPossibleOrientation() {
		LinkedList<Orientation> invPossibleOrientations = new LinkedList<>();
		for (Orientation ori : this.getPossibleOrientations()) {
			invPossibleOrientations.addFirst(ori);
		}
		return invPossibleOrientations;
	}

	public void deleteFromPossibleOrientation(Orientation ori) {
		if (this.possibleOrientations.contains(ori)) {
			this.possibleOrientations.remove(ori);
		}
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	public boolean isFixed() {
		return isFixed;
	}

	public int getPosX() { // get j
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() { // get i
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public PieceType getType() {
		return type;
	}

	/**
	 * MODIFIED. Sets the type for the current piece, changes its orientation if needed, and refreshes its possible orientations list and connector list
	 */
	public void setType(PieceType type) {
		this.type = type;
		this.possibleOrientations = type.getListOfPossibleOri();
		if(this.orientation != null) {
			setOrientation(this.orientation);
		}
	}

	/**
	 * Sets the orientation for the current piece, and refreshes its connectors list
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = type.getOrientation(orientation);
		this.connectors = type.setConnectorsList(this.orientation);
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public LinkedList<Orientation> getConnectors() {
		return connectors;
	}

	/**
	 * ADDED. Sets the current piece's type from the integer representing its serialization
	 *
	 * @params orientationValue the integer representing a piece type
	 */
	public void setType(int typeValue) {
		setType(PieceType.getTypefromValue(typeValue));
	}

	/**
	 * ADDED. Sets the current piece's orientation from the integer representing its serialization
	 *
	 * @params orientationValue the integer representing an orientation
	 */
	public void setOrientation(int orientationValue) {
		setOrientation(Orientation.getOrifromValue(orientationValue));
	}

	/**
	 * ADDED. Sets both the type and orientation for the current piece, knowing which directions should be connected
	 *
	 * @params hasTopConnector true if the current piece should have a top connector
	 * @params hasRightConnector true if the current piece should have a right connector
	 * @params hasBottomConnector true if the current piece should have a bottom connector
	 * @params hasLeftConnector true if the current piece should have a left connector
	 */
	public Piece setTypeAndOrientationFromConnectors (boolean hasTopConnector, boolean hasRightConnector, boolean hasBottomConnector, boolean hasLeftConnector) {
		if (hasTopConnector) {
			if (hasRightConnector) {
				if (hasBottomConnector) {
					if (hasLeftConnector) {
						setType(PieceType.FOURCONN);
						setOrientation(Orientation.NORTH);
					}
					else {
						setType(PieceType.TTYPE);
						setOrientation(Orientation.EAST);
					}
				}
				else { //top-right-!bottom
					if (hasLeftConnector) {
						setType(PieceType.TTYPE);
						setOrientation(Orientation.NORTH);
					}
					else {
						setType(PieceType.LTYPE);
						setOrientation(Orientation.NORTH);
					}
				}
			}
			else { //top-!right
				if (hasBottomConnector) {
					if (hasLeftConnector) {
						setType(PieceType.TTYPE);
						setOrientation(Orientation.WEST);
					}
					else {
						setType(PieceType.BAR);
						setOrientation(Orientation.NORTH);
					}
				}
				else { //top-!right-!bottom
					if (hasLeftConnector) {
						setType(PieceType.LTYPE);
						setOrientation(Orientation.WEST);
					}
					else {
						setType(PieceType.ONECONN);
						setOrientation(Orientation.NORTH);
					}
				}
			}
		}
		else { //!top
			if (hasRightConnector) {
				if (hasBottomConnector) {
					if (hasLeftConnector) {
						setType(PieceType.TTYPE);
						setOrientation(Orientation.SOUTH);
					}
					else {
						setType(PieceType.LTYPE);
						setOrientation(Orientation.EAST);
					}
				}
				else { //!top-right-!bottom
					if (hasLeftConnector) {
						setType(PieceType.BAR);
						setOrientation(Orientation.EAST);
					}
					else {
						setType(PieceType.ONECONN);
						setOrientation(Orientation.EAST);
					}
				}
			}
			else { //!top-!right
				if (hasBottomConnector) {
					if (hasLeftConnector) {
						setType(PieceType.LTYPE);
						setOrientation(Orientation.SOUTH);
					}
					else {
						setType(PieceType.ONECONN);
						setOrientation(Orientation.SOUTH);
					}
				}
				else { //!top-!right-!bottom
					if (hasLeftConnector) {
						setType(PieceType.ONECONN);
						setOrientation(Orientation.WEST);
					}
					else {
						setType(PieceType.VOID);
						setOrientation(Orientation.NORTH);
					}
				}
			}
		}
		return this;
	}

	/**
	 * Checks if the current piece has a connector in the north direction
	 * 
	 * @return true if the current piece has a connector in the north direction
	 */
	public boolean hasTopConnector() {
		for (Orientation c : this.getConnectors()) {
			if (c == Orientation.NORTH) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the current piece has a connector in the east direction
	 * 
	 * @return true if the current piece has a connector in the east direction
	 */
	public boolean hasRightConnector() {
		for (Orientation c : this.getConnectors()) {
			if (c == Orientation.EAST) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the current piece has a connector in the south direction
	 * 
	 * @return true if the current piece has a connector in the south direction
	 */
	public boolean hasBottomConnector() {
		for (Orientation c : this.getConnectors()) {
			if (c == Orientation.SOUTH) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the current piece has a connector in the west direction
	 * 
	 * @return true if the current piece has a connector in the west direction
	 */
	public boolean hasLeftConnector() {
		for (Orientation c : this.getConnectors()) {
			if (c == Orientation.WEST) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the current piece has a connector in the specified direction
	 * 
	 * @params d the direction
	 * @return true if the current piece has a connector in the specified direction
	 */
	public boolean hasConnectorInDirection(Orientation d) {
		return this.getConnectors().contains(d);
	}

	/**
	 * MODIFIED. Turns the piece 90° clockwise
	 */
	public void turn() {
		setOrientation(type.getOrientation(orientation.turn90Right()));
	}

	@Override
	public String toString() {
		String s = "[" + this.getPosY() + ", " + this.getPosX() + "] " + this.getType() + " ";
		for (Orientation ori : this.getConnectors()) {
			s += " " + ori.toString();
		}
		s += " Orientation / " + this.getOrientation();
		return s;
	}

}
