package uilogic;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class Button {
    public Rectangle rect;
    public String text;
    public Runnable action;
    public boolean isHover;
    public boolean isActive;

    public Button(int x, int y, int width, int height, String text, Runnable action) {
        this.rect = new Rectangle(x, y, width, height);
        this.text = text;
        this.action = action;
        this.isHover = false;
        this.isActive = true;
    }

    public void checkHover(Point pos) {
        if (!isActive) {
            this.isHover = false;
            return;
        }
        this.isHover = rect.contains(pos);
    }

    public boolean click(Point pos) {
        if (!isActive) return false;

        if (rect.contains(pos) && action != null) {
            action.run();
            return true; // Кнопка сама каже "Я обробила клік!"
        }
        return false;
    }
}
