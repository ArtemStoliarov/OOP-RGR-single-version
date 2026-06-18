package gamelogic;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameUIState manages all UI-related state.
 * Responsibility: Maintaining UI state (selected position, valid moves display, info tabs, etc.)
 * This separates UI concerns from game logic, following Single Responsibility Principle.
 */
public class GameUIState {
    private Point selectedPos;
    private Map<Point, List<Point>> validMoves;
    private boolean gameOver;
    private String winnerText;
    private boolean showInfo;
    private int infoTab;

    public GameUIState() {
        this.selectedPos = null;
        this.validMoves = new HashMap<>();
        this.gameOver = false;
        this.winnerText = "";
        this.showInfo = false;
        this.infoTab = 0;
    }

    public void reset() {
        selectedPos = null;
        validMoves = new HashMap<>();
        gameOver = false;
        winnerText = "";
        showInfo = false;
        infoTab = 0;
    }

    // Piece Selection
    public void selectPiece(int row, int col) {
        selectedPos = new Point(row, col);
    }

    public void deselectPiece() {
        selectedPos = null;
        validMoves.clear();
    }

    public Point getSelectedPosition() {
        return selectedPos;
    }

    public boolean hasPieceSelected() {
        return selectedPos != null;
    }

    // Valid Moves Display
    public void setValidMoves(Map<Point, List<Point>> moves) {
        validMoves = new HashMap<>(moves);
    }

    public Map<Point, List<Point>> getValidMoves() {
        return validMoves;
    }

    public void clearValidMoves() {
        validMoves.clear();
    }

    // Game Over State
    public void endGame(String winner) {
        gameOver = true;
        winnerText = winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinnerText() {
        return winnerText;
    }

    // Info Panel State
    public void toggleInfo() {
        showInfo = !showInfo;
    }

    public void setShowInfo(boolean show) {
        showInfo = show;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setInfoTab(int tab) {
        infoTab = tab;
    }

    public int getInfoTab() {
        return infoTab;
    }
}

