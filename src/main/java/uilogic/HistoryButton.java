package uilogic;

public class HistoryButton extends Button {
    public HistoryButton(int x, int y, int width, int height, String text, Runnable action) {
        super(x, y, width, height, text, action);
    }

    public static HistoryButton createPrevMove(int x, int y, Runnable action) {
        return new HistoryButton(x, y, 65, 40, "<", action);
    }

    public static HistoryButton createNextMove(int x, int y, Runnable action) {
        return new HistoryButton(x, y, 65, 40, ">", action);
    }

    public static HistoryButton createCurrentMove(int x, int y, Runnable action) {
        return new HistoryButton(x, y, 40, 40, "X", action);
    }
}
