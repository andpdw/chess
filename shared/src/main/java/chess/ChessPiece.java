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

    public ChessPiece(ChessPiece pieceOrigin) {
        color = pieceOrigin.getTeamColor();
        pieceType = pieceOrigin.getPieceType();
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
        ChessPiece piece = board.getPiece(myPosition);

        if (piece.getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition, piece.getTeamColor());

        } else if (piece.getPieceType() == PieceType.KING) {
            int[][] kingMoves = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
            return checkPositionMoves(board, myPosition, kingMoves);

        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            int[][] knightMoves = {{2, 1}, {2, -1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {1, -2}, {-1, -2}};
            return checkPositionMoves(board, myPosition, knightMoves);

        } else if (piece.getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);

        } else if (piece.getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);

        } else if (piece.getPieceType() == PieceType.QUEEN) {
            Collection<ChessMove> queenMoves = new ArrayList<>();
            queenMoves.addAll(rookMoves(board, myPosition));
            queenMoves.addAll(bishopMoves(board, myPosition));
            return queenMoves;
        } else {
            return null;
        }
    }

    public Collection<ChessMove> checkPositionMoves(ChessBoard board, ChessPosition myPosition, int[][] moves) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        for (int[] m : moves) {
            ChessPosition testPos = new ChessPosition(myPosition.getRow()+m[0], myPosition.getColumn()+m[1]);
            if (testPos.inBounds() && (board.getPiece(testPos) == null || board.getPiece(testPos).getTeamColor() != piece.getTeamColor())) {
                ChessMove move = new ChessMove(myPosition, testPos, null);
                possibleMoves.add(new ChessMove(move));
            }
        }
        return possibleMoves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int dir = 1;
        int endRow = 7;
        int startRow = 2;
        if (color == ChessGame.TeamColor.BLACK) {
            dir = -1;
            endRow = 2;
            startRow = 7;
        }

        ChessPosition forwardPos = new ChessPosition(myPosition.getRow()+dir, myPosition.getColumn());
        ChessPosition farForwardPos = new ChessPosition(myPosition.getRow()+(2*dir), myPosition.getColumn());
        ChessPosition captureRight = new ChessPosition(myPosition.getRow()+dir, myPosition.getColumn()+1);
        ChessPosition captureLeft = new ChessPosition(myPosition.getRow()+dir, myPosition.getColumn()-1);

        ChessMove move;

        //Forward Check
        if (forwardPos.inBounds() && board.getPiece(forwardPos) == null) {
            if (myPosition.getRow() == endRow) {
                move = new ChessMove(myPosition, forwardPos, PieceType.BISHOP);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, forwardPos, PieceType.QUEEN);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, forwardPos, PieceType.KNIGHT);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, forwardPos, PieceType.ROOK);
                possibleMoves.add(new ChessMove(move));
            } else if (myPosition.getRow() == startRow && farForwardPos.inBounds() && board.getPiece(farForwardPos) == null) {
                move = new ChessMove(myPosition, forwardPos, null);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, farForwardPos, null);
                possibleMoves.add(new ChessMove(move));
            } else {
                move = new ChessMove(myPosition, forwardPos, null);
                possibleMoves.add(new ChessMove(move));
            }
        }
        //Capture Checks
        if (captureRight.inBounds() && board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() != color) {
            if (myPosition.getRow() == endRow) {
                move = new ChessMove(myPosition, captureRight, PieceType.BISHOP);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureRight, PieceType.QUEEN);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureRight, PieceType.KNIGHT);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureRight, PieceType.ROOK);
                possibleMoves.add(new ChessMove(move));
            } else {
                move = new ChessMove(myPosition, captureRight, null);
                possibleMoves.add(new ChessMove(move));
            }
        }
        if (captureLeft.inBounds() && board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() != color) {
            if (myPosition.getRow() == endRow) {
                move = new ChessMove(myPosition, captureLeft, PieceType.BISHOP);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureLeft, PieceType.QUEEN);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureLeft, PieceType.KNIGHT);
                possibleMoves.add(new ChessMove(move));
                move = new ChessMove(myPosition, captureLeft, PieceType.ROOK);
                possibleMoves.add(new ChessMove(move));
            } else {
                move = new ChessMove(myPosition, captureLeft, null);
                possibleMoves.add(new ChessMove(move));
            }
        }
        return possibleMoves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] dirVals = {{1, 0, 1, 0}, {-1, 0, -1, 0}, {0, 1, 0, 1}, {0, -1, 0, -1}};
        return lineMoveTests(board, myPosition, dirVals);
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] dirVals = {{1, 1, 1, 1}, {-1, 1, -1, 1}, {1, -1, 1, -1}, {-1, -1, -1, -1}};
        return lineMoveTests(board, myPosition, dirVals);
    }

    public Collection<ChessMove> lineMoveTests(ChessBoard board, ChessPosition myPosition, int[][] dirVals) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        for (int[] val : dirVals) {
            while (myPosition.getRow() + val[0] <= 8 && myPosition.getColumn() + val[1] <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow() + val[0], myPosition.getColumn() + val[1]);
                if (testPos.inBounds()) {
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
                } else {
                    break;
                }
                val[0] = val[0] + val[2];
                val[1] = val[1] + val[3];
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

    @Override
    public String toString() {
        if (color == ChessGame.TeamColor.WHITE) {
            if (pieceType == PieceType.KING) {
                return "K";
            } else if (pieceType == PieceType.QUEEN) {
                return "Q";
            } else if (pieceType == PieceType.ROOK) {
                return "R";
            } else if (pieceType == PieceType.KNIGHT) {
                return "N";
            } else if (pieceType == PieceType.BISHOP) {
                return "B";
            } else {
                return "P";
            }
        } else if (color == ChessGame.TeamColor.BLACK) {
            if (pieceType == PieceType.KING) {
                return "k";
            } else if (pieceType == PieceType.QUEEN) {
                return "q";
            } else if (pieceType == PieceType.ROOK) {
                return "r";
            } else if (pieceType == PieceType.KNIGHT) {
                return "n";
            } else if (pieceType == PieceType.BISHOP) {
                return "b";
            } else {
                return "p";
            }
        }
        return "Error";
    }
}
