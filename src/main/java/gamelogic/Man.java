package gamelogic;

import lib.Config;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Man extends Piece {

    public Man(Color color) {
        super(color);
    }

    @Override
    public boolean isKing() { return false; }

    @Override
    public Map<Point, List<Point>> getValidMoves(Board board, int myRow, int myCol) {
        Map<Point, List<Point>> moves = new HashMap<>();
        addSimpleMoves(board, moves, myRow, myCol);
        moves.putAll(calculateCaptures(board, myRow, myCol, new ArrayList<>()));
        return moves;
    }

    private void addSimpleMoves(Board board, Map<Point, List<Point>> moves, int myRow, int myCol) {
        int[] moveRows = this.color.equals(Config.RED) ? new int[]{1} : new int[]{-1};
        for (int rStep : moveRows) {
            for (int cStep : new int[]{-1, 1}) {
                checkAndAddSimpleMove(board, moves, myRow + rStep, myCol + cStep);
            }
        }
    }

    private void checkAndAddSimpleMove(Board board, Map<Point, List<Point>> moves, int newR, int newC) {
        if (!isValidCoordinate(newR, newC)) return;

        if (board.getPiece(newR, newC) == null) {
            moves.put(new Point(newR, newC), new ArrayList<>());
        }
    }

    private Map<Point, List<Point>> calculateCaptures(Board board, int curRow, int curCol, List<Point> skippedBase) {
        Map<Point, List<Point>> moves = new HashMap<>();
        int[][] dirs = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : dirs) {
            processCaptureDirection(board, curRow, curCol, dir[0], dir[1], skippedBase, moves);
        }
        return moves;
    }

    private void processCaptureDirection(Board board, int curR, int curC, int dr, int dc, List<Point> skippedBase, Map<Point, List<Point>> moves) {
        int midR = curR + dr, midC = curC + dc;
        int endR = curR + 2 * dr, endC = curC + 2 * dc;

        if (!isValidCoordinate(endR, endC)) return;

        Piece midPiece = board.getPiece(midR, midC);
        Piece endPiece = board.getPiece(endR, endC);
        Point victimPos = new Point(midR, midC); // Запам'ятовуємо координату жертви!

        if (!isValidCapture(midPiece, endPiece, victimPos, skippedBase)) return;

        List<Point> newSkipped = new ArrayList<>(skippedBase);
        newSkipped.add(victimPos);
        Point landingPoint = new Point(endR, endC);

        updateBestMove(moves, landingPoint, newSkipped);

        Map<Point, List<Point>> furtherCaptures = calculateCaptures(board, endR, endC, newSkipped);
        mergeMoves(moves, furtherCaptures);
    }

    private boolean isValidCapture(Piece mid, Piece end, Point victimPos, List<Point> skipped) {
        if (mid == null || end != null) return false;
        if (mid.color.equals(this.color)) return false;
        if (skipped.contains(victimPos)) return false;
        return true;
    }

    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < Config.ROWS && c >= 0 && c < Config.COLS;
    }

    private void updateBestMove(Map<Point, List<Point>> moves, Point p, List<Point> newSkipped) {
        if (!moves.containsKey(p) || newSkipped.size() > moves.get(p).size()) {
            moves.put(p, newSkipped);
        }
    }

    private void mergeMoves(Map<Point, List<Point>> dest, Map<Point, List<Point>> src) {
        for (Map.Entry<Point, List<Point>> entry : src.entrySet()) {
            updateBestMove(dest, entry.getKey(), entry.getValue());
        }
    }
}
