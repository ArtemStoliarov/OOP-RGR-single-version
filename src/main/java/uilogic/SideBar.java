package uilogic;

import lib.Config;
import playerinput.Controller;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 * SideBar manages the UI sidebar and handles user interactions with sidebar elements.
 * Responsibility: Managing sidebar layout, button states, and handling clicks.
 * Button creation is delegated to SideBarComponentFactory.
 */
public class SideBar implements Controller {
    private GameInterface game;
    private HistoryConsole console;
    private List<Button> buttons;

    private ControlButton btnResign;
    private ControlButton btnRestart;
    private InfoButton btnInfo;
    private InfoButton btnTabRules;
    private InfoButton btnTabHistory;
    private RewindButton btnPrevMove;
    private RewindButton btnNextMove;
    private RewindButton btnCurrentMove;

    public SideBar(GameInterface game) {
        this.game = game;
        this.console = new HistoryConsole();

        // Delegate button creation to factory and provide update callback
        SideBarComponentFactory.SideBarButtons componentSet =
            SideBarComponentFactory.createAllSideBarComponents(game, console, this::updateButtonStates);

        this.buttons = componentSet.allButtons;
        this.btnResign = componentSet.btnResign;
        this.btnRestart = componentSet.btnRestart;
        this.btnInfo = componentSet.btnInfo;
        this.btnTabRules = componentSet.btnTabRules;
        this.btnTabHistory = componentSet.btnTabHistory;
        this.btnPrevMove = componentSet.btnPrevMove;
        this.btnNextMove = componentSet.btnNextMove;
        this.btnCurrentMove = componentSet.btnCurrentMove;
    }

    public void updateButtonStates() {
        boolean info = game.isShowInfo();
        boolean over = game.isGameOver();

        // Disable all buttons first
        buttons.forEach(b -> b.isActive = false);

        // Set resign/restart buttons
        btnResign.isActive = !over;
        btnRestart.isActive = over;

        if (info) {
            // When info is open, only show info-related buttons
            btnInfo.isActive = true;
            btnTabRules.isActive = true;
            btnTabHistory.isActive = true;
            return;
        }

        // When info is closed, show main controls and history buttons
        btnInfo.isActive = true;
        btnPrevMove.isActive = (console.getViewIndex() > 0);
        btnNextMove.isActive = (console.getViewIndex() >= 0 && console.getViewIndex() < game.getHistorySize() - 1);
        btnCurrentMove.isActive = (console.getViewIndex() >= 0 && console.getViewIndex() < game.getHistorySize() - 1);
    }

    @Override
    public boolean handleMouseClick(Point pos) {
        if (handlePaginationClick(pos)) return true;

        for (Button btn : buttons) {
            if (btn.click(pos)) return true;
        }
        return false;
    }

    private boolean handlePaginationClick(Point pos) {
        if (!game.isShowInfo() || game.getInfoTab() != 1) return false;

        int movesPerPage = 76;
        int totalPages = (int) Math.ceil((double) game.getHistorySize() / movesPerPage);
        if (totalPages <= 1) return false;

        int tabWidth = 80;
        int startX = 50;
        int tabY = Config.BOARD_HEIGHT - 50;
        int tabHeight = 30;

        for (int p = 0; p < totalPages; p++) {
            Rectangle rect = new Rectangle(startX + p * (tabWidth + 5), tabY, tabWidth, tabHeight);
            if (rect.contains(pos)) {
                console.setHistoryPage(p);
                updateButtonStates();
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleMouseMove(Point pos) {
        for (Button btn : buttons) {
            btn.checkHover(pos);
        }
    }

    public void syncWithNewMove(int historySize) {
        console.syncWithNewMove(historySize);
        updateButtonStates();
    }

    public void resetConsole() {
        console.reset();
    }

    public boolean isViewingPast(int historySize) {
        return console.isViewingPast(historySize);
    }

    public int getViewIndex() {
        return console.getViewIndex();
    }

    public int getHistoryPage() {
        return console.getHistoryPage();
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public Button getBtnTabRules() {
        return btnTabRules;
    }

    public Button getBtnTabHistory() {
        return btnTabHistory;
    }

    public ControlButton getBtnResign() {
        return btnResign;
    }

    public ControlButton getBtnRestart() {
        return btnRestart;
    }

    public InfoButton getBtnInfo() {
        return btnInfo;
    }

    public RewindButton getBtnPrevMove() {
        return btnPrevMove;
    }

    public RewindButton getBtnNextMove() {
        return btnNextMove;
    }

    public RewindButton getBtnCurrentMove() {
        return btnCurrentMove;
    }
}
