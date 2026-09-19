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
        } else if (piece.getPieceType() == PieceType.PAWN) {

            //White piece
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                //Standard Checks (i.e. forward 1, capture moves)
                ChessPosition forwardPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition farForwardPos = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                ChessPosition captureRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                ChessPosition captureLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);

                ChessMove move;

                //Forward Check
                if (forwardPos.InBounds() && board.getPiece(forwardPos) == null) {
                    if (myPosition.getRow() == 7) {
                        move = new ChessMove(myPosition, forwardPos, PieceType.BISHOP);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.QUEEN);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.KNIGHT);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.ROOK);
                        possibleMoves.add(new ChessMove(move));
                    } else if (myPosition.getRow() == 2 && farForwardPos.InBounds() && board.getPiece(farForwardPos) == null) {
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
                if (captureRight.InBounds() && board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (myPosition.getRow() == 7) {
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
                if (captureLeft.InBounds() && board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (myPosition.getRow() == 7) {
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
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                //Black Piece
                //Standard Checks (i.e. forward 1, capture moves)
                ChessPosition forwardPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition farForwardPos = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                ChessPosition captureRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                ChessPosition captureLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);

                ChessMove move;

                //Forward Check
                if (forwardPos.InBounds() && board.getPiece(forwardPos) == null) {
                    if (myPosition.getRow() == 2) {
                        move = new ChessMove(myPosition, forwardPos, PieceType.BISHOP);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.QUEEN);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.KNIGHT);
                        possibleMoves.add(new ChessMove(move));
                        move = new ChessMove(myPosition, forwardPos, PieceType.ROOK);
                        possibleMoves.add(new ChessMove(move));
                    } else if (myPosition.getRow() == 7 && farForwardPos.InBounds() && board.getPiece(farForwardPos) == null) {
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
                if (captureRight.InBounds() && board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (myPosition.getRow() == 2) {
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
                if (captureLeft.InBounds() && board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (myPosition.getRow() == 2) {
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
            }
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
        } else if (piece.getPieceType() == PieceType.BISHOP) {
            int i = 1;

            while (myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
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
            while (myPosition.getRow()+i >= 1 && myPosition.getColumn()+i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
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
            while (myPosition.getRow()+i <= 8 && myPosition.getColumn()-i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
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
            i = 1;
            while (myPosition.getRow()-i >= 1 && myPosition.getColumn()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
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
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            //Rook Logic
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

            //Bishop Logic
            i = 1;

            while (myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
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
            while (myPosition.getRow()+i >= 1 && myPosition.getColumn()+i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
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
            while (myPosition.getRow()+i <= 8 && myPosition.getColumn()-i >= 1) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
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
            i = 1;
            while (myPosition.getRow()-i >= 1 && myPosition.getColumn()+i <= 8) {
                ChessPosition testPos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
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
