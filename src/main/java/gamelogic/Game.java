package gamelogic;

import lib.Config;
import playerinput.Controller;
import uilogic.GameInterface;
import uilogic.SideBar;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements GameInterface, Controller {
    private Board board;
    private History history;
    private SideBar sideBar;
    private Color turn;

    private Point selectedPos;
    private Map<Point, List<Point>> validMoves;

    private boolean gameOver;
    private String winnerText;
    private boolean showInfo;
    private int infoTab;

    public Game() {
        history = new History();
        sideBar = new SideBar(this);
        reset();
    }

    @Override
    public void reset() {
        board = CreateBoard.createStandard();
        turn = Config.WHITE;
        validMoves = new HashMap<>();
        selectedPos = null;
        gameOver = false;
        winnerText = "";
        showInfo = false;
        infoTab = 0;

        history.reset(board);
        sideBar.resetConsole();
        sideBar.updateButtonStates();
    }

    public void select(int row, int col) {
        if (gameOver || isViewingPast()) return;

        if (tryMoveSelectedPiece(row, col)) return;

        Piece piece = board.getPiece(row, col);
        if (!isValidSelection(piece)) return;

        Map<Point, List<Point>> moves = piece.getValidMoves(board, row, col);
        moves = enforceMandatoryJumps(moves);

        if (moves.isEmpty()) return;

        selectedPos = new Point(row, col);
        validMoves = moves;
    }

    private boolean tryMoveSelectedPiece(int row, int col) {
        if (selectedPos == null) return false;
        if (movePiece(row, col)) return true;
        selectedPos = null;
        return false;
    }

    private boolean isValidSelection(Piece piece) {
        return piece != null && piece.color.equals(turn);
    }

    private Map<Point, List<Point>> enforceMandatoryJumps(Map<Point, List<Point>> moves) {
        if (!hasAvailableJumps(turn)) return moves;
        return filterOnlyCaptures(moves);
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

    private boolean movePiece(int row, int col) {
        Point targetPoint = new Point(row, col);

        if (board.getPiece(row, col) != null) return false;
        if (!validMoves.containsKey(targetPoint)) return false;

        List<Point> skippedCoords = validMoves.get(targetPoint);
        boolean isCapture = (skippedCoords != null && !skippedCoords.isEmpty());

        int oldX = selectedPos.x;
        int oldY = selectedPos.y;

        board.move(oldX, oldY, row, col);

        if (isCapture) {
            board.remove(skippedCoords);
        }

        history.recordMove(oldX, oldY, row, col, isCapture, turn, board);
        sideBar.syncWithNewMove(history.getSize());

        selectedPos = new Point(row, col);

        if (isCapture) {
            return handleConsecutiveJumps();
        }

        changeTurn();
        return true;
    }

    private boolean handleConsecutiveJumps() {
        Piece piece = board.getPiece(selectedPos.x, selectedPos.y);
        Map<Point, List<Point>> nextMoves = piece.getValidMoves(board, selectedPos.x, selectedPos.y);
        Map<Point, List<Point>> nextCaptures = filterOnlyCaptures(nextMoves);

        if (!nextCaptures.isEmpty()) {
            validMoves = nextCaptures;
            return true;
        }

        changeTurn();
        return true;
    }

    private void changeTurn() {
        validMoves.clear();
        selectedPos = null;
        turn = turn.equals(Config.WHITE) ? Config.RED : Config.WHITE;
        checkWinCondition();
    }

    private void checkWinCondition() {
        String winner = determineWinner();
        if (winner == null) return;

        gameOver = true;
        winnerText = "ПЕРЕМІГ: " + winner + "!";
        sideBar.updateButtonStates();
    }

    private String determineWinner() {
        if (board.redLeft <= 0) return "БІЛІ";
        if (board.whiteLeft <= 0) return "ЧЕРВОНІ";
        if (!hasValidMoves(turn)) return turn.equals(Config.RED) ? "БІЛІ" : "ЧЕРВОНІ";
        return null;
    }

    private boolean hasAvailableJumps(Color color) {
        for (int r = 0; r < Config.ROWS; r++) {
            for (int c = 0; c < Config.COLS; c++) {
                if (canPieceJump(board.getPiece(r, c), r, c, color)) return true;
            }
        }
        return false;
    }

    private boolean canPieceJump(Piece p, int r, int c, Color color) {
        if (p == null || !p.color.equals(color)) return false;
        for (List<Point> skipped : p.getValidMoves(board, r, c).values()) {
            if (!skipped.isEmpty()) return true;
        }
        return false;
    }

    private boolean hasValidMoves(Color color) {
        for (int r = 0; r < Config.ROWS; r++) {
            for (int c = 0; c < Config.COLS; c++) {
                if (canPieceMove(board.getPiece(r, c), r, c, color)) return true;
            }
        }
        return false;
    }

    private boolean canPieceMove(Piece p, int r, int c, Color color) {
        if (p == null || !p.color.equals(color)) return false;
        return !p.getValidMoves(board, r, c).isEmpty();
    }

    @Override
    public boolean handleMouseClick(Point pos) {
        int row = pos.y / Config.SQUARE_SIZE;
        int col = pos.x / Config.SQUARE_SIZE;
        select(row, col);
        return true;
    }

    @Override
    public void handleMouseMove(Point pos) {}

    @Override
    public void resign() {
        if (gameOver) return;
        gameOver = true;
        showInfo = false;
        String loser = turn.equals(Config.RED) ? "ЧЕРВОНІ" : "БІЛІ";
        String winner = turn.equals(Config.RED) ? "БІЛІ" : "ЧЕРВОНІ";
        winnerText = loser + " здалися. " + winner + " виграли!";
        sideBar.updateButtonStates();
    }

    @Override
    public void toggleInfo() {
        showInfo = !showInfo;
        sideBar.updateButtonStates();
    }

    @Override
    public void setInfoTab(int tab) {
        infoTab = tab;
    }

    @Override
    public boolean isShowInfo() { return this.showInfo; }

    @Override
    public int getInfoTab() { return this.infoTab; }

    @Override
    public int getHistorySize() {
        return history.getSize();
    }

    @Override
    public boolean isGameOver() { return this.gameOver; }

    public boolean isViewingPast() {
        return sideBar.isViewingPast(history.getSize());
    }

    public Board getBoardToDraw() {
        if (isViewingPast()) {
            return history.getBoardAt(sideBar.getViewIndex());
        }
        return board;
    }

    public Board getBoard() { return this.board; }
    public History getHistory() { return this.history; }
    public SideBar getSideBar() { return this.sideBar; }
    public List<String> getTextHistory() { return history.getTextHistory(); }

    public Color getTurn() { return this.turn; }
    public String getWinnerText() { return this.winnerText; }

    public Point getSelectedPos() { return this.selectedPos; }

    public Piece getSelectedPiece() {
        if (selectedPos == null) return null;
        return board.getPiece(selectedPos.x, selectedPos.y);
    }

    public Map<Point, List<Point>> getValidMoves() { return this.validMoves; }
}
