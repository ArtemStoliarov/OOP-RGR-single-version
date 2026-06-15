package gamelogic;

import lib.Config;

public class CreateBoard {

    public static Board createStandard() {
        Board board = new Board();

        for (int row = 0; row < Config.ROWS; row++) {
            for (int col = 0; col < Config.COLS; col++) {
                placeStartingPieceIfPlayable(board, row, col);
            }
        }
        return board;
    }

    private static void placeStartingPieceIfPlayable(Board board, int row, int col) {
        if (!isPlayableSquare(row, col)) return;

        if (row < 3) {
            board.setPiece(row, col, new Man(Config.RED));
        }

        else if (row > 4) {
            board.setPiece(row, col, new Man(Config.WHITE));
        }
    }

    private static boolean isPlayableSquare(int row, int col) {
        return col % 2 == ((row + 1) % 2);
    }

    public static Board createEmpty() {
        return new Board();
    }
}
