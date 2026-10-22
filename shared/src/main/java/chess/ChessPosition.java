package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int rowVal;
    private int colVal;

    public ChessPosition(int row, int col) {
        rowVal = row;
        colVal = col;
    }

    public ChessPosition(ChessPosition origionalPos) {
        rowVal = origionalPos.rowVal;
        colVal = origionalPos.colVal;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return rowVal;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return colVal;
    }

    public boolean inBounds() {
        if (colVal > 8 || colVal < 1) {
            return false;
        } else if (rowVal > 8 || rowVal < 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return rowVal == that.rowVal && colVal == that.colVal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowVal, colVal);
    }

    @Override
    public String toString() {
        return "ChessPosition{" + rowVal + "," + colVal + '}';
    }
}
