package gamelogic;

import lib.Config;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class History {
    private List<String> textHistory;
    private List<Board> boardHistory;

    public History() {
        textHistory = new ArrayList<>();
        boardHistory = new ArrayList<>();
    }

    public void reset(Board initialBoard) {
        textHistory.clear();
        boardHistory.clear();
        boardHistory.add(initialBoard.deepCopy()); // Хід 0
    }

    public void recordMove(int fromRow, int fromCol, int toRow, int toCol, boolean isCapture, Color turn, Board currentBoard) {
        int fromNum = getSquareNum(fromRow, fromCol);
        int toNum = getSquareNum(toRow, toCol);

        String actionSymbol = isCapture ? " x " : " - ";
        String playerColor = turn.equals(Config.RED) ? "Ч" : "Б";

        textHistory.add(playerColor + ": " + fromNum + actionSymbol + toNum);
        boardHistory.add(currentBoard.deepCopy());
    }

    private int getSquareNum(int r, int c) {
        return (r * 4) + (c / 2) + 1;
    }

    public Board getBoardAt(int index) {
        if (index >= 0 && index < boardHistory.size()) return boardHistory.get(index);
        return null;
    }

    public List<String> getTextHistory() { return textHistory; }
    public int getSize() { return boardHistory.size(); }
}
