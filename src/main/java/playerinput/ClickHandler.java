package playerinput;

import gamelogic.Game;
import uilogic.SideBar;
import lib.Config;
import java.awt.Point;

public class ClickHandler {
    private Game game;
    private SideBar sideBar;

    public ClickHandler(Game game) {
        this.game = game;
        this.sideBar = game.getSideBar();
    }

    public void handleMouseClick(Point pos) {
        Controller target = routeClick(pos);
        if (target != null) {
            target.handleMouseClick(pos);
        }
    }

    public void handleMouseMove(Point pos) {
        Controller target = routeMove(pos);
        if (target != null) {
            target.handleMouseMove(pos);
        }
    }

    private Controller routeClick(Point pos) {
        if (game.isShowInfo()) return sideBar;

        if (pos.x > Config.BOARD_WIDTH) return sideBar;

        if (game.isGameOver() || game.isViewingPast()) return null;

        return game;
    }

    private Controller routeMove(Point pos) {
        return sideBar;
    }
}
