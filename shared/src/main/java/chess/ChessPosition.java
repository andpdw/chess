package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row_val;
    private int col_val;

    public ChessPosition(int row, int col) {
        row_val = row;
        col_val = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row_val;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col_val;
    }

    public boolean InBounds() {
        if (col_val > 7 || col_val < 0) {
            return false;
        } else if (row_val > 7 || row_val < 0) {
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
        return row_val == that.row_val && col_val == that.col_val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row_val, col_val);
    }
}
