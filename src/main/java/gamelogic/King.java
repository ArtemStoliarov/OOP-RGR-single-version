package gamelogic;

import lib.Config;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public boolean isKing() { return true; }

    @Override
    public Map<Point, List<Point>> getValidMoves(Board board, int myRow, int myCol) {
        Map<Point, List<Point>> moves = new HashMap<>();
        addSimpleMoves(board, moves, myRow, myCol);
        moves.putAll(calculateCaptures(board, myRow, myCol, new ArrayList<>()));
        return moves;
    }

    private void addSimpleMoves(Board board, Map<Point, List<Point>> moves, int myRow, int myCol) {
        int[][] dirs = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : dirs) {
            exploreSimpleDirection(board, moves, myRow, myCol, dir[0], dir[1]);
        }
    }

    private void exploreSimpleDirection(Board board, Map<Point, List<Point>> moves, int startR, int startC, int dr, int dc) {
        int curR = startR + dr;
        int curC = startC + dc;

        while (isValidCoordinate(curR, curC)) {
            if (board.getPiece(curR, curC) != null) break;

            moves.put(new Point(curR, curC), new ArrayList<>());
            curR += dr;
            curC += dc;
        }
    }

    private Map<Point, List<Point>> calculateCaptures(Board board, int startR, int startC, List<Point> skippedBase) {
        Map<Point, List<Point>> moves = new HashMap<>();
        int[][] dirs = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : dirs) {
            exploreCaptureDirection(board, startR, startC, dir[0], dir[1], skippedBase, moves);
        }
        return moves;
    }

    private void exploreCaptureDirection(Board board, int startR, int startC, int dr, int dc, List<Point> skippedBase, Map<Point, List<Point>> moves) {
        int curR = startR + dr;
        int curC = startC + dc;
        Point skippedInDir = null; // Тепер ми зберігаємо КООРДИНАТУ потенційної жертви

        while (isValidCoordinate(curR, curC)) {
            Piece target = board.getPiece(curR, curC);
            Point currentPos = new Point(curR, curC);

            if (target != null) {
                if (target.color.equals(this.color) || skippedBase.contains(currentPos) || skippedInDir != null) break;

                skippedInDir = currentPos;
            }
            else if (skippedInDir != null) {
                registerKingCapture(board, curR, curC, skippedInDir, skippedBase, moves);
            }

            curR += dr;
            curC += dc;
        }
    }

    private void registerKingCapture(Board board, int r, int c, Point victimPos, List<Point> skippedBase, Map<Point, List<Point>> moves) {
        List<Point> newSkipped = new ArrayList<>(skippedBase);
        newSkipped.add(victimPos);
        Point landingPoint = new Point(r, c);

        updateBestMove(moves, landingPoint, newSkipped);

        Map<Point, List<Point>> furtherCaptures = calculateCaptures(board, r, c, newSkipped);
        mergeMoves(moves, furtherCaptures);
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
