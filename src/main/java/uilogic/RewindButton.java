package uilogic;

public class RewindButton extends Button {
    public RewindButton(int x, int y, int width, int height, String text, Runnable action) {
        super(x, y, width, height, text, action);
    }

    public static RewindButton createPrevMove(int x, int y, Runnable action) {
        return new RewindButton(x, y, 65, 40, "<", action);
    }

    public static RewindButton createNextMove(int x, int y, Runnable action) {
        return new RewindButton(x, y, 65, 40, ">", action);
    }

    public static RewindButton createCurrentMove(int x, int y, Runnable action) {
        return new RewindButton(x, y, 40, 40, "X", action);
    }
}

