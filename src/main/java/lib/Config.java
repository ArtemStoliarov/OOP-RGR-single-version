package lib;

import java.awt.Color;
import java.awt.Font;

public class Config {
    private Config() {}

    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;
    public static final int SIDEBAR_WIDTH = 250;
    public static final int WINDOW_WIDTH = BOARD_WIDTH + SIDEBAR_WIDTH;
    public static final int WINDOW_HEIGHT = BOARD_HEIGHT;

    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int SQUARE_SIZE = BOARD_WIDTH / COLS;

    public static final int PADDING = 15;
    public static final int OUTLINE = 3;

    public static final Color RED = new Color(200, 33, 0);
    public static final Color WHITE = new Color(240, 240, 240);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GREEN = new Color(84, 136, 30);
    public static final Color GREY = new Color(50, 50, 50);
    public static final Color BUTTON_COLOR = new Color(70, 70, 70);
    public static final Color BUTTON_HOVER = new Color(100, 100, 100);
    public static final Color LAST_MOVE_COLOR = new Color(255, 255, 0, 100);
    public static final Color CREAM = new Color(240, 230, 140);
    public static final Color GOLD = new Color(220, 200, 55);

    public static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 30);
    public static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 22);
    public static final Font RULES_FONT = new Font("Arial", Font.PLAIN, 20);
}