package gamelogic;

import lib.Config;
import playerinput.Controller;
import uilogic.GameInterface;
import uilogic.SideBar;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Map;

/**
 * Game orchestrates the overall game flow and delegates responsibilities to specialized services.
 * Responsibility: Coordinating game state, delegating logic to GameRulesEngine and UI state to GameUIState.
 * This follows Single Responsibility Principle by separating concerns.
 */
public class Game implements GameInterface, Controller {
    private GameRulesEngine rulesEngine;
    private GameUIState uiState;
    private Board board;
    private History history;
    private SideBar sideBar;

    public Game() {
        this.board = CreateBoard.createStandard();
        this.history = new History();
        this.rulesEngine = new GameRulesEngine(board, history);
        this.uiState = new GameUIState();
        this.sideBar = new SideBar(this);
        initializeGame();
    }

    private void initializeGame() {
        history.reset(board);
        sideBar.resetConsole();
        sideBar.updateButtonStates();
    }

    @Override
    public void reset() {
        board = CreateBoard.createStandard();
        rulesEngine.setBoard(board);
        rulesEngine.resetTurn();
        uiState.reset();
        history.reset(board);
        sideBar.resetConsole();
        sideBar.updateButtonStates();
    }

    /**
     * Handles a piece selection and attempts to move it.
     * This follows the OOP principle of "objects do things" - the selection action
     * initiates a series of delegated responsibilities.
     */
    @Override
    public boolean handleMouseClick(Point pos) {
        int row = pos.y / Config.SQUARE_SIZE;
        int col = pos.x / Config.SQUARE_SIZE;

        attemptPieceInteraction(row, col);
        return true;
    }

    // Compatibility method for tests and older callers that used grid coordinates
    public void select(int row, int col) {
        attemptPieceInteraction(row, col);
    }

    /**
     * Attempts to interact with a piece at the given position.
     * If a piece is already selected, this tries to move it.
     * Otherwise, it selects a piece if it's valid.
     */
    private void attemptPieceInteraction(int row, int col) {
        if (uiState.isGameOver() || isViewingPastMoves()) return;

        // Try to move selected piece to this location
        if (uiState.hasPieceSelected() && tryMovePiece(row, col)) {
            sideBar.syncWithNewMove(history.getSize());
            sideBar.updateButtonStates();
            return;
        }

        // Otherwise, try to select a piece at this location
        selectPieceAt(row, col);
    }

    /**
     * Attempts to move the selected piece to the given position.
     * Returns true if the move was successful.
     */
    private boolean tryMovePiece(int row, int col) {
        Point selectedPos = uiState.getSelectedPosition();
        if (selectedPos == null) return false;

        // Capture current turn to detect whether a consecutive jump occurred
        java.awt.Color beforeTurn = rulesEngine.getCurrentTurn();

        boolean moveMade = rulesEngine.attemptMovePiece(selectedPos.x, selectedPos.y, row, col);

        if (moveMade) {
            // If the turn stayed the same the player may continue jumping -> keep selection and update moves
            if (beforeTurn.equals(rulesEngine.getCurrentTurn())) {
                uiState.selectPiece(row, col);
                uiState.setValidMoves(rulesEngine.getAvailableMovesForPiece(row, col));
            } else {
                // Turn changed -> clear selection and valid moves immediately so highlight disappears
                uiState.deselectPiece();
            }

            checkGameState();
            return true;
        }

        uiState.deselectPiece();
        return false;
    }

    /**
     * Selects a piece at the given position if it's valid.
     */
    private void selectPieceAt(int row, int col) {
        Piece piece = board.getPiece(row, col);

        if (!rulesEngine.canSelectPiece(piece)) {
            uiState.deselectPiece();
            return;
        }

        Map<Point, List<Point>> moves = rulesEngine.getAvailableMovesForPiece(row, col);

        if (moves.isEmpty()) {
            uiState.deselectPiece();
            return;
        }

        uiState.selectPiece(row, col);
        uiState.setValidMoves(moves);
    }

    /**
     * Checks the current game state and updates accordingly.
     */
    private void checkGameState() {
        String winner = rulesEngine.checkWinCondition();
        if (winner != null) {
            endGame("ПЕРЕМІГ: " + winner + "!");
        }
    }

    /**
     * Ends the game with the given winner message.
     */
    private void endGame(String message) {
        uiState.endGame(message);
        sideBar.updateButtonStates();
    }

    /**
     * Checks if we're currently viewing past game states in the history.
     */
    private boolean isViewingPastMoves() {
        return sideBar.isViewingPast(history.getSize());
    }

    /**
     * Public version for other classes to check if we're viewing past moves.
     */
    public boolean isViewingPast() {
        return isViewingPastMoves();
    }

    @Override
    public void handleMouseMove(Point pos) {}

    @Override
    public void resign() {
        if (uiState.isGameOver()) return;

        Color loser = rulesEngine.getCurrentTurn();
        String loserName = loser.equals(Config.RED) ? "ЧЕРВОНІ" : "БІЛІ";
        String winnerName = loser.equals(Config.RED) ? "БІЛІ" : "ЧЕРВОНІ";

        endGame(loserName + " здалися. " + winnerName + " виграли!");
    }

    @Override
    public void toggleInfo() {
        uiState.toggleInfo();
        sideBar.updateButtonStates();
    }

    @Override
    public void setInfoTab(int tab) {
        uiState.setInfoTab(tab);
    }

    @Override
    public boolean isShowInfo() {
        return uiState.isShowInfo();
    }

    @Override
    public int getInfoTab() {
        return uiState.getInfoTab();
    }

    @Override
    public int getHistorySize() {
        return history.getSize();
    }

    @Override
    public boolean isGameOver() {
        return uiState.isGameOver();
    }

    // Getters for drawing and UI
    public Board getBoardToDraw() {
        if (isViewingPastMoves()) {
            return history.getBoardAt(sideBar.getViewIndex());
        }
        return board;
    }

    public Board getBoard() {
        return board;
    }

    public History getHistory() {
        return history;
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    public List<String> getTextHistory() {
        return history.getTextHistory();
    }

    public Color getTurn() {
        return rulesEngine.getCurrentTurn();
    }

    public String getWinnerText() {
        return uiState.getWinnerText();
    }

    public Point getSelectedPos() {
        return uiState.getSelectedPosition();
    }

    public Piece getSelectedPiece() {
        Point selectedPos = uiState.getSelectedPosition();
        if (selectedPos == null) return null;
        return board.getPiece(selectedPos.x, selectedPos.y);
    }

    public Map<Point, List<Point>> getValidMoves() {
        return uiState.getValidMoves();
    }
}
