package fr.dauphine.JavaAvance.GUI;

import java.util.ArrayList;
import java.util.Random;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;

/**
 * Grid handler and pieces functions which depends of the grid
 */
public class Grid {
	private int width; // j
	private int height; // i
	private int nbcc = -1;
	private Piece[][] pieces;

	// Constructor with specified number of connected component
	public Grid(int width, int height, int nbcc) {
		this.width = width;
		this.height = height;
		this.nbcc = nbcc;
		pieces = new Piece[height][width];
	}

	public Grid(int width, int height) {
		this(width, height, -1);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		//Should probably update pieces here but not used anywhere in the code
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		//TODO Should probably update pieces here but not used anywhere in the code
	}

	public Integer getNbcc() {
		return nbcc;
	}

	public void setNbcc(int nbcc) {
		this.nbcc = nbcc;
	}

	public Piece getPiece(int line, int column) {
		return this.pieces[line][column];
	}

	public void setPiece(int line, int column, Piece piece) {
		this.pieces[line][column] = piece;
		//TODO Should probably update the piece's coordinates
	}

	public Piece[][] getAllPieces() {
		return pieces;
	}

	/**
	 * Checks if a case is a corner
	 * 
	 * @param line line of the case
	 * @param column column of the case
	 * @return true if the case is a corner
	 */
	public boolean isCorner(int line, int column) {
		if (line == 0) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else if (line == this.getHeight() - 1) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Checks if a case is member of the first or the last line
	 * 
	 * @param line line of the case
	 * @param column column of the case
	 * @return true if the case is a corner
	 */
	public boolean isBorderLine(int line, int column) {
		if (line == 0 && column > 0 && column < this.getWidth() - 1) {
			return true;
		} else if (line == this.getHeight() - 1 && column > 0 && column < this.getWidth() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a case is member of the first or the last column
	 * 
	 * @param line line of the case
	 * @param column column of the case
	 * @return true if the case is a corner
	 */
	public boolean isBorderColumn(int line, int column) {
		if (column == 0 && line > 0 && line < this.getHeight() - 1) {
			return true;
		} else if (column == this.getWidth() - 1 && line > 0 && line < this.getHeight() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a piece has a  non-void neighbor in the direction of one of its connector in its current orientation
	 * 
	 * @param p the piece to check
	 * @return true if there is a non-void neighbor for all connectors
	 */
	public boolean hasNeighbour(Piece p) {
		for (Orientation c : p.getConnectors()) {
			int oppPieceY = c.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = c.getOpposedPieceCoordinates(p)[1];// j
			try {
				if (this.getPiece(oppPieceY, oppPieceX).getType() == PieceType.VOID) {
					return false;
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Checks if a piece has a fixed connected neighbor for each one of its connectors. (connected neighbor = neighbor in a direction of one of this piece's connectors, that also has a connector in the direction of this piece)
	 * 
	 * @param p the piece to check
	 * @return true if there is a fixed connected neighbor for each connector
	 */
	public boolean hasFixedNeighbour(Piece p) {
		boolean bool = false;
		for (Orientation c : p.getConnectors()) {
			bool = false;
			int oppPieceY = c.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = c.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.getType() == PieceType.VOID || !neigh.isFixed()) {
					return false;
				}
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (c == oriOppPiece.getOpposedOrientation()) {
							bool = true;
						}
					}
					if (!bool) {
						return false;
					}

				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return bool;
	}

	/**
	 * Checks if a piece has a at least one fixed connected neighbor
	 * 
	 * @param p the piece to check
	 * @return true if the piece has a at least one fixed connected neighbor
	 */
	public boolean hasAtLeast1FixedNeighbour(Piece p) {
		for (Orientation c : p.getConnectors()) {
			int oppPieceY = c.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = c.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (c == oriOppPiece.getOpposedOrientation()) {
							return true;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Gets the list of non-void neighbors of a pieces in the direction of its connectors
	 * 
	 * @param p the piece
	 * @return the list of neighbors
	 */
	public ArrayList<Piece> listOfNeighbours(Piece p) {
		ArrayList<Piece> lp = new ArrayList<>();
		for (Orientation c : p.getConnectors()) {
			int oppPieceY = c.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = c.getOpposedPieceCoordinates(p)[1];// j

			if (oppPieceY >= 0 && oppPieceY < this.getHeight() && oppPieceX >= 0 && oppPieceX < this.width) {
				if (this.getPiece(oppPieceY, oppPieceX).getType() != PieceType.VOID) {
					lp.add(this.getPiece(oppPieceY, oppPieceX));
				}
			}

		}
		return lp;
	}

	/**
	 * Returns the number of non-void neighbors of a piece in the direction of its connectors
	 * 
	 * @param p the piece
	 * @return the number of neighbors
	 */
	public int numberOfNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;
		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID)
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID)
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID)
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID)
			count++;
		return count;
	}

	/**
	 * Returns the number of fixed neighbors of a piece
	 * 
	 * @param p the piece
	 * @return the number of neighbors
	 */
	public int numberOfFixedNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;

		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID && getPiece(Y + 1, X).isFixed())
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID && getPiece(Y, X + 1).isFixed())
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID && getPiece(Y - 1, X).isFixed())
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID && getPiece(Y, X - 1).isFixed())
			count++;
		return count;
	}

	/**
	 * Checks if all pieces of the grid have at least one neighbor in the direction of one of their connectors
	 * 
	 * @return false if a piece has no neighbor in the direction of its connectors
	 */
	public boolean allPieceHaveNeighbour() {

		for (Piece[] ligne : this.getAllPieces()) {
			for (Piece p : ligne) {

				if (p.getType() != PieceType.VOID) {
					if (p.getType().getNbConnectors() > numberOfNeibours(p)) {
						return false;
					}
				}

			}
		}
		return true;

	}

	/**
	 * Returns the next piece of the current piece
	 * 
	 * @param p the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPiece(Piece p) {
		int i = p.getPosY();
		int j = p.getPosX();
		if (j < this.getWidth() - 1) {
			p = this.getPiece(i, j + 1);
		} else {
			if (i < this.getHeight() - 1) {
				p = this.getPiece(i + 1, 0);
			} else {
				return null;
			}

		}
		return p;
	}
	
	/**
	 * Returns the next piece of the current piece right2left and bottom2top
	 * 
	 * @param p the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPieceInv(Piece p) {

		int i = p.getPosY();
		int j = p.getPosX();
		if (j > 0) {
			p = this.getPiece(i, j - 1);
		} else {
			if (i > 0) {
				p = this.getPiece(i - 1, this.getWidth()-1);
			} else {
				return null;
			}

		}

		return p;

	}

	/**
	 * Checks if a piece is connected to its neighbor in one direction (supposing it has a connector in that direction)
	 * 
	 * @param p the piece to check
	 * @param d the direction to check for the connection
	 * @return true if a connector of a piece is connected
	 */
	public boolean isConnected(Piece p, Orientation d) {
		int oppPieceY = d.getOpposedPieceCoordinates(p)[0];// i
		int oppPieceX = d.getOpposedPieceCoordinates(p)[1];// j
		try {
			for (Orientation oppConnector : this.getPiece(oppPieceY, oppPieceX).getConnectors()) {
				if (oppConnector == d.getOpposedOrientation()) {
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	/**
	 * Checks if a piece is totally connected (in all directions). Should return the same as isValidOrientation.
	 * 
	 * @param p the piece to check
	 * @return true if a connector of the piece is connected
	 */
	public boolean isTotallyConnected(Piece p) {
		if (p.getType() != PieceType.VOID) {
			for (Orientation connector : p.getConnectors()) {
				if (!this.isConnected(p, connector)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * ADDED. Checks if all pieces in the grid are totally connected. Should return the same as allPiecesValid.
	 * 
	 * @return true if a connector of a piece is connected
	 */
	public boolean allPiecesTotallyConnected() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(!isTotallyConnected(getPiece(i,j))) {
					System.out.print("test "+i+","+j);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if a piece position is valid. Should return the same as isTotallyConnected.
	 * 
	 * @param line of the piece
	 * @param column of the piece
	 * @return true if a connector of a piece is connected
	 */
	public boolean isValidOrientation(int line, int column) {

		Piece tn = this.topNeighbor(this.getPiece(line, column));
		Piece ln = this.leftNeighbor(this.getPiece(line, column));
		Piece rn = this.rightNeighbor(this.getPiece(line, column));
		Piece bn = this.bottomNeighbor(this.getPiece(line, column));

		if (this.getPiece(line, column).getType() != PieceType.VOID) {
			if (line == 0) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;
				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}
				if (this.getPiece(line, column).hasTopConnector())
					return false;
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector())
					return false;
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector())
					return false;

			} else if (line > 0 && line < this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;

				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}

				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector())
					return false;
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector())
					return false;

			} else if (line == this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;
				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}
				if (this.getPiece(line, column).hasBottomConnector())
					return false;
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;

			}
			if (this.getPiece(line, column).hasLeftConnector() && ln == null)
				return false;
			if (this.getPiece(line, column).hasTopConnector() && tn == null)
				return false;
			if (this.getPiece(line, column).hasRightConnector() && rn == null)
				return false;
			if (this.getPiece(line, column).hasBottomConnector() && bn == null)
				return false;
		}

		return true;
	}

	/**
	 * ADDED. Checks if all pieces in the grid are valid. Should return the same as allPiecesTotallyConnected.
	 * 
	 * @return true if a connector of a piece is connected
	 */
	public boolean allPiecesValid() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(!isValidOrientation(i,j)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Find the left neighbor of a piece
	 * 
	 * @param p the piece
	 * @return the neighbor or null if no neighbor
	 */
	public Piece leftNeighbor(Piece p) {

		if (p.getPosX() > 0) {
			if (this.getPiece(p.getPosY(), p.getPosX() - 1).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY(), p.getPosX() - 1);
			}
		}
		return null;
	}

	/**
	 * Find the top neighbor
	 * 
	 * @param p the piece
	 * @return the neighbor or null if no neighbor
	 */
	public Piece topNeighbor(Piece p) {

		if (p.getPosY() > 0) {
			if (this.getPiece(p.getPosY() - 1, p.getPosX()).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY() - 1, p.getPosX());
			}
		}
		return null;
	}

	/**
	 * Find the right neighbor
	 * 
	 * @param p the piece
	 * @return the neighbor or null if no neighbor
	 */
	public Piece rightNeighbor(Piece p) {

		if (p.getPosX() < this.getWidth() - 1) {
			if (this.getPiece(p.getPosY(), p.getPosX() + 1).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY(), p.getPosX() + 1);
			}
		}
		return null;
	}

	/**
	 * Find the bottom neighbor
	 * 
	 * @param p the piece
	 * @return the neighbor or null if no neighbor
	 */
	public Piece bottomNeighbor(Piece p) {

		if (p.getPosY() < this.getHeight() - 1) {
			if (this.getPiece(p.getPosY() + 1, p.getPosX()).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY() + 1, p.getPosX());
			}
		}
		return null;
	}

  /**
	 * ADDED. Finds the neighbor in the specified direction
	 * 
	 * @param p the piece
	 * @param d the direction
	 * @return the neighbor or null if no neighbor
	 */
	public Piece getNeighborFromDirection(Piece p, Orientation d) {
		switch (d) {
			case NORTH:
				return topNeighbor(p);
			case EAST:
				return rightNeighbor(p);
			case SOUTH:
				return bottomNeighbor(p);
			case WEST:
				return leftNeighbor(p);
		}
		return null;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				s += DisplayUnicode.getUnicodeOfPiece(pieces[i][j].getType(), pieces[i][j].getOrientation());
			}
			s += "\n";
		}
		return s;
	}

  /**
	 * ADDED. Displays the current grid in console. Useful for really big grids : toString appends to a really big string and is really slow
	 * 
	 * @param color true if should print special characters to color fixed pieces
	 */
	public void print(boolean color) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(pieces[i][j] != null) {
					if(color && pieces[i][j].isFixed()) {
						System.out.print("\u001B[34m");
					}
					System.out.print(DisplayUnicode.getUnicodeOfPiece(pieces[i][j].getType(), pieces[i][j].getOrientation()));
					if(color && pieces[i][j].isFixed()) {
						System.out.print("\u001B[0m");
					}
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

  /**
	 * ADDED. Displays the current grid in console. Useful for really big grids : toString appends to a really big string and is really slow
	 */
	public void print() {
		print(false);
	}
}
