package uilogic;

public class InfoButton extends Button {
    public InfoButton(int x, int y, int width, int height, String text, Runnable action) {
        super(x, y, width, height, text, action);
    }

    public static InfoButton createInfo(int x, int y, Runnable action) {
        return new InfoButton(x, y, 200, 50, "ІНФО", action);
    }

    // Кнопки для вкладок всередині оверлею
    public static InfoButton createTabRules(int x, int y, Runnable action) {
        return new InfoButton(x, y, 150, 40, "ПРАВИЛА", action);
    }

    public static InfoButton createTabHistory(int x, int y, Runnable action) {
        return new InfoButton(x, y, 150, 40, "ІСТОРІЯ", action);
    }
}
