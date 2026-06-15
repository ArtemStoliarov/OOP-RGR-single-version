package uilogic;

public class ControlButton extends Button {
    public ControlButton(int x, int y, int width, int height, String text, Runnable action) {
        super(x, y, width, height, text, action);
    }

    public static ControlButton createResign(int x, int y, Runnable action) {
        return new ControlButton(x, y, 200, 50, "ЗДАТИСЯ", action);
    }

    public static ControlButton createRestart(int x, int y, Runnable action) {
        return new ControlButton(x, y, 200, 50, "РЕСТАРТ", action);
    }
}
