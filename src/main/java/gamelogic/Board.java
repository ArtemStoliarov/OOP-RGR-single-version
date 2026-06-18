package gamelogic;

import lib.Config;
import java.awt.Point;
import java.util.List;

public class Board {
    private Piece[][] grid;
    public int redLeft = 0;
    public int whiteLeft = 0;
    private int[] lastMove = null;

    public Board() {
        grid = new Piece[Config.ROWS][Config.COLS];
    }

    // Створюємо незалежну копію дошки для історії
    public Board deepCopy() {
        Board copy = CreateBoard.createEmpty();
        copy.redLeft = this.redLeft;
        copy.whiteLeft = this.whiteLeft;

        if (this.lastMove != null) {
            copy.lastMove = this.lastMove.clone();
        }

        for (int r = 0; r < Config.ROWS; r++) {
            for (int c = 0; c < Config.COLS; c++) {
                Piece p = this.grid[r][c];
                if (p != null) {
                    // Using PieceFactory respects Open/Closed Principle
                    Piece clonePiece = PieceFactory.createPiece(p);
                    copy.setPiece(r, c, clonePiece);
                }
            }
        }
        return copy;
    }

    public void setPiece(int row, int col, Piece piece) {
        grid[row][col] = piece;
        if (piece != null) {
            if (piece.color.equals(Config.RED)) redLeft++;
            else whiteLeft++;
        }
    }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= Config.ROWS || col < 0 || col >= Config.COLS) return null;
        return grid[row][col];
    }

    // Тепер видалення відбувається суто за координатами
    public void removePiece(int row, int col) {
        Piece p = grid[row][col];
        if (p != null) {
            grid[row][col] = null;
            if (p.color.equals(Config.RED)) redLeft--;
            else whiteLeft--;
        }
    }

    // Видалення серії шашок після ланцюгового биття
    public void removeCapturedPieces(List<Point> positions) {
        for (Point pos : positions) {
            removePiece(pos.x, pos.y);
        }
    }

    // Keep for backward compatibility
    public void remove(List<Point> positions) {
        removeCapturedPieces(positions);
    }

    // Рух тепер виконується суто з точки А в точку Б
    public void move(int fromR, int fromC, int toR, int toC) {
        Piece piece = getPiece(fromR, fromC);
        lastMove = new int[]{fromR, fromC, toR, toC};

        removePiece(fromR, fromC);
        setPiece(toR, toC, piece);

        promoteIfReachedEnd(piece, toR, toC);
    }

    private void promoteIfReachedEnd(Piece piece, int row, int col) {
        if (piece == null || piece.isKing()) return;

        boolean whitePromotes = (row == 0 && piece.color.equals(Config.WHITE));
        boolean redPromotes = (row == Config.ROWS - 1 && piece.color.equals(Config.RED));

        if (whitePromotes || redPromotes) {
            removePiece(row, col);
            // Using PieceFactory respects Open/Closed Principle
            setPiece(row, col, PieceFactory.createKing(piece.color));
        }
    }

    public int[] getLastMove() { return lastMove; }
}
