package playerinput;

import java.awt.Point;

public interface Controller {
    boolean handleMouseClick(Point pos);
    void handleMouseMove(Point pos);
}
