package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getColumn()-1][position.getRow()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (board[position.getColumn()-1][position.getRow()-1] == null) {
            return null;
        } else {
            return board[position.getColumn()-1][position.getRow()-1];
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];

        //adds the white pawns
        for (int i = 1; i <= 8; i++) {
            ChessPosition tempPosition = new ChessPosition(2, i);
            ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(tempPosition, tempPiece);
        }
        //adds the white back row
        for (int i = 1; i <= 8; i++) {
            ChessPosition tempPosition = new ChessPosition(1, i);
            ChessPiece tempPiece;
            if (i == 1 || i == 8) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            } else if (i == 2 || i == 7) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            } else if (i == 3 || i == 6) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            } else if (i == 4) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
            } else {
                tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
            }
            addPiece(tempPosition, tempPiece);
        }
        //adds the black pawns
        for (int i = 1; i <= 8; i++) {
            ChessPosition tempPosition = new ChessPosition(7, i);
            ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(tempPosition, tempPiece);
        }
        //adds the black back row
        for (int i = 1; i <= 8; i++) {
            ChessPosition tempPosition = new ChessPosition(8, i);
            ChessPiece tempPiece;
            if (i == 1 || i == 8) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            } else if (i == 2 || i == 7) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            } else if (i == 3 || i == 6) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            } else if (i == 4) {
                tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            } else {
                tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
            }
            addPiece(tempPosition, tempPiece);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        for (int i = 1; i<=8; i++) {
            for (int j = 1; j<=8; j++) {
                ChessPosition temp = new ChessPosition(i, j);
                System.out.print(board.getPiece(temp) + "|");
            }
            System.out.println();
        }
    }
}
