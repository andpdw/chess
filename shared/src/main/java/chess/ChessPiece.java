package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null) {
            return null;
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            return null;
        } else if (piece.getPieceType() == PieceType.KING) {
            int[][] kingMoves = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};

            for (int i = 0; i<8; i++) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+kingMoves[i][0], myPosition.getColumn()+kingMoves[i][1]);
                if (testPos.InBounds() && (board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() != piece.getTeamColor())) {
                    ChessMove move = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(new ChessMove(move));
                }
            }
        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            int[][] knightMoves = {{2, 1}, {2, -1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {1, -2}, {-1, -2}};

            for (int[] m : knightMoves) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+ m[0], myPosition.getColumn()+ m[1]);
                if (testPos.InBounds() && (board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() != piece.getTeamColor())) {
                    ChessMove move = new ChessMove(myPosition, testPos, null);
                    possibleMoves.add(new ChessMove(move));
                }
            }
        } else if (piece.getPieceType() == PieceType.ROOK) {
            int i = 1;

            while (myPosition.getRow()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+ i, myPosition.getColumn());
                if (testPos.InBounds()) {
                    if (board.getPiece(testPos) == null) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                    } else if (board.getPiece(testPos).getTeamColor() != piece.getTeamColor()) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                        break;
                    } else {
                        break;
                    }
                }
                i++;
            }
            i = -1;
            while (myPosition.getRow()+i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+ i, myPosition.getColumn());
                if (testPos.InBounds()) {
                    if (board.getPiece(testPos) == null) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                    } else if (board.getPiece(testPos).getTeamColor() != piece.getTeamColor()) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                        break;
                    } else {
                        break;
                    }
                }
                i--;
            }
            i = 1;
            while (myPosition.getColumn()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i);
                if (testPos.InBounds()) {
                    if (board.getPiece(testPos) == null) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                    } else if (board.getPiece(testPos).getTeamColor() != piece.getTeamColor()) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                        break;
                    } else {
                        break;
                    }
                }
                i++;
            }
            i = -1;
            while (myPosition.getColumn()+i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i);
                if (testPos.InBounds()) {
                    if (board.getPiece(testPos) == null) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                    } else if (board.getPiece(testPos).getTeamColor() != piece.getTeamColor()) {
                        ChessMove move = new ChessMove(myPosition, testPos, null);
                        possibleMoves.add(new ChessMove(move));
                        break;
                    } else {
                        break;
                    }
                }
                i--;
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }
}
