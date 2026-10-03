package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard boardInternal;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        boardInternal = new ChessBoard();
        boardInternal.resetBoard();
    }

    public ChessGame(ChessBoard original, TeamColor turn) {
        teamTurn = turn;
        boardInternal = new ChessBoard(original);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (boardInternal.getPiece(startPosition) == null) {
            return null;
        } else {
            TeamColor currentTurn = boardInternal.getPiece(startPosition).getTeamColor();
            Collection<ChessMove> possibleMoves = boardInternal.getPiece(startPosition).pieceMoves(boardInternal, startPosition);
            for (ChessMove move : possibleMoves) {
                if ((boardInternal.getPiece(move.getEndPosition()) == null) || (boardInternal.getPiece(move.getStartPosition()).getTeamColor() != boardInternal.getPiece(move.getEndPosition()).getTeamColor())) {
                    ChessGame testGame = new ChessGame(boardInternal, boardInternal.getPiece(move.getStartPosition()).getTeamColor());
                    try {
                        testGame.makeMove(move);
                        if (!testGame.isInCheck(currentTurn)) {
                            validMoves.add(new ChessMove(move));
                        }
                    } catch (InvalidMoveException e) {}
                    catch (Exception e) {
                        System.out.print(e.toString());
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!move.getStartPosition().InBounds() || !move.getEndPosition().InBounds() || boardInternal.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid move");
        }
        if (boardInternal.getPiece(move.getEndPosition()) == null) {
            if (move.getPromotionPiece() == null) {
                boardInternal.addPiece(move.getEndPosition(), boardInternal.getPiece(move.getStartPosition()));
                boardInternal.addPiece(move.getStartPosition(), null);
            } else {
                boardInternal.addPiece(move.getEndPosition(), new ChessPiece(boardInternal.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
                boardInternal.addPiece(move.getStartPosition(), null);
            }
        } else if (boardInternal.getPiece(move.getEndPosition()).getTeamColor() != boardInternal.getPiece(move.getStartPosition()).getTeamColor()) {
            if (move.getPromotionPiece() == null) {
                boardInternal.addPiece(move.getEndPosition(), boardInternal.getPiece(move.getStartPosition()));
                boardInternal.addPiece(move.getStartPosition(), null);
            } else {
                boardInternal.addPiece(move.getEndPosition(), new ChessPiece(boardInternal.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
                boardInternal.addPiece(move.getStartPosition(), null);
            }
        } else {
            throw new InvalidMoveException("Cannot capture piece of same color");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Finds the king for the team
        ChessPosition kingPos = new ChessPosition(-1, -1);
        for (int i=0; i<8; i++) {
            boolean kingFound = false;
            for (int j=0; j<8; j++) {
                ChessPosition pos = new ChessPosition(j+1, i+1);
                if (boardInternal.getPiece(pos) != null) {
                    ChessPiece testPiece = new ChessPiece(boardInternal.getPiece(pos));
                    if (testPiece.getTeamColor() == teamColor && testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPos = new ChessPosition(pos);
                        kingFound = true;
                        break;
                    }
                }
            }
            if (kingFound) {
                break;
            }
        }
        if (kingPos.InBounds()) {
            //Loops through every other piece to see if they can capture the king
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    ChessPosition pos = new ChessPosition(j+1, i+1);
                    if (boardInternal.getPiece(pos) != null && boardInternal.getPiece(pos).getTeamColor() != teamColor) {
                        Collection<ChessMove> possibleMoves = boardInternal.getPiece(pos).pieceMoves(boardInternal, pos);
                        for (ChessMove move : possibleMoves) {
                            if (move.getEndPosition().equals(kingPos)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Unable to find King");
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                ChessPosition pos = new ChessPosition(i+1, j+1);
                if (board.getPiece(pos) != null) {
                    boardInternal.addPiece(pos, new ChessPiece(board.getPiece(pos)));
                } else {
                    boardInternal.addPiece(pos, null);
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return new ChessBoard(boardInternal);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(boardInternal, chessGame.boardInternal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, boardInternal);
    }

    @Override
    public String toString() {
        return boardInternal.toString() + "\nTeam Turn: " + teamTurn;
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        System.out.print(game.toString());
        System.out.print(game.isInCheck(TeamColor.WHITE));
    }
}
