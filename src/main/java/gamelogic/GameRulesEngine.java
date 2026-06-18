package gamelogic;

import lib.Config;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameRulesEngine handles the core game logic and rules.
 * Responsibility: Managing piece movements, captures, turn changes, and win conditions.
 * This separates game logic from UI state management, following Single Responsibility Principle.
 */
public class GameRulesEngine {
    private Board board;
    private Color currentTurn;
    private History history;

    public GameRulesEngine(Board board, History history) {
        this.board = board;
        this.history = history;
        this.currentTurn = Config.WHITE;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Attempts to move a piece from one position to another.
     * Returns true if move was successful.
     */
    public boolean attemptMovePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.color.equals(currentTurn)) {
            return false;
        }

        Point targetPoint = new Point(toRow, toCol);
        Map<Point, List<Point>> validMoves = piece.getValidMoves(board, fromRow, fromCol);

        if (!validMoves.containsKey(targetPoint)) {
            return false;
        }

        List<Point> skippedCoords = validMoves.get(targetPoint);
        boolean isCapture = (skippedCoords != null && !skippedCoords.isEmpty());

        executePieceMovement(fromRow, fromCol, toRow, toCol, skippedCoords, isCapture);
        return true;
    }

    private void executePieceMovement(int fromRow, int fromCol, int toRow, int toCol,
                                     List<Point> skippedCoords, boolean isCapture) {
        board.move(fromRow, fromCol, toRow, toCol);

        if (isCapture) {
            board.removeCapturedPieces(skippedCoords);
        }

        history.recordMove(fromRow, fromCol, toRow, toCol, isCapture, currentTurn, board);

        if (isCapture && hasMoreJumpsAvailable(toRow, toCol)) {
            // Player can continue jumping
            return;
        }

        changeTurn();
    }

    private boolean hasMoreJumpsAvailable(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece == null) return false;

        Map<Point, List<Point>> nextMoves = piece.getValidMoves(board, row, col);
        return filterOnlyCaptures(nextMoves).size() > 0;
    }

    /**
     * Changes the turn to the other player and checks for win condition.
     */
    public void changeTurn() {
        currentTurn = currentTurn.equals(Config.WHITE) ? Config.RED : Config.WHITE;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public void resetTurn() {
        currentTurn = Config.WHITE;
    }

    /**
     * Checks if a piece can be selected (belongs to current player).
     */
    public boolean canSelectPiece(Piece piece) {
        return piece != null && piece.color.equals(currentTurn);
    }

    /**
     * Gets valid moves for a piece and enforces mandatory jump rules.
     */
    public Map<Point, List<Point>> getAvailableMovesForPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece == null) return new HashMap<>();

        Map<Point, List<Point>> moves = piece.getValidMoves(board, row, col);

        // Enforce mandatory jumps if any jumps are available
        if (hasAvailableJumps(currentTurn)) {
            return filterOnlyCaptures(moves);
        }

        return moves;
    }

    /**
     * Checks if the player with given color has any available jump moves.
     */
    public boolean hasAvailableJumps(Color color) {
        for (int r = 0; r < Config.ROWS; r++) {
            for (int c = 0; c < Config.COLS; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null && piece.color.equals(color)) {
                    Map<Point, List<Point>> moves = piece.getValidMoves(board, r, c);
                    for (List<Point> skipped : moves.values()) {
                        if (!skipped.isEmpty()) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a player has any valid moves available.
     */
    public boolean hasValidMoves(Color color) {
        for (int r = 0; r < Config.ROWS; r++) {
            for (int c = 0; c < Config.COLS; c++) {
                Piece piece = board.getPiece(r, c);
                if (piece != null && piece.color.equals(color)) {
                    if (!piece.getValidMoves(board, r, c).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the game is over and returns the winner.
     * Returns null if game is not over, otherwise returns winner color as string.
     */
    public String checkWinCondition() {
        if (board.redLeft <= 0) return "БІЛІ";
        if (board.whiteLeft <= 0) return "ЧЕРВОНІ";
        if (!hasValidMoves(currentTurn)) {
            return currentTurn.equals(Config.RED) ? "БІЛІ" : "ЧЕРВОНІ";
        }
        return null;
    }

    private Map<Point, List<Point>> filterOnlyCaptures(Map<Point, List<Point>> moves) {
        Map<Point, List<Point>> captures = new HashMap<>();
        for (Map.Entry<Point, List<Point>> entry : moves.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                captures.put(entry.getKey(), entry.getValue());
            }
        }
        return captures;
    }

    public Board getBoard() {
        return board;
    }

    public History getHistory() {
        return history;
    }
}

