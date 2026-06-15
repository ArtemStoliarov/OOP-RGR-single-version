package uilogic;

import lib.Config;
import playerinput.Controller;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class SideBar implements Controller {
    private GameInterface game;
    private HistoryConsole console; // SideBar сам володіє консоллю!
    private List<Button> buttons;

    private ControlButton btnResign;
    private ControlButton btnRestart;
    private InfoButton btnInfo;
    private InfoButton btnTabRules;
    private InfoButton btnTabHistory;
    private HistoryButton btnPrevMove;
    private HistoryButton btnNextMove;
    private HistoryButton btnCurrentMove;

    public SideBar(GameInterface game) {
        this.game = game;
        this.console = new HistoryConsole();
        this.buttons = new ArrayList<>();

        int alignX = Config.BOARD_WIDTH + 15;

        btnResign = ControlButton.createResign(alignX, 200, game::resign);
        btnRestart = ControlButton.createRestart(alignX, 200, game::reset);
        btnInfo = InfoButton.createInfo(alignX, 270, game::toggleInfo);

        btnTabRules = InfoButton.createTabRules(50, 20, () -> { game.setInfoTab(0); console.resetHistoryPage(); });
        btnTabHistory = InfoButton.createTabHistory(220, 20, () -> { game.setInfoTab(1); console.resetHistoryPage(); });

        btnPrevMove = HistoryButton.createPrevMove(alignX, 720, () -> { console.viewPrevious(); updateButtonStates(); });
        btnNextMove = HistoryButton.createNextMove(alignX + 75, 720, () -> { console.viewNext(game.getHistorySize()); updateButtonStates(); });
        btnCurrentMove = HistoryButton.createCurrentMove(alignX + 150, 720, () -> { console.viewCurrent(game.getHistorySize()); updateButtonStates(); });

        buttons.add(btnResign);
        buttons.add(btnRestart);
        buttons.add(btnInfo);
        buttons.add(btnTabRules);
        buttons.add(btnTabHistory);
        buttons.add(btnPrevMove);
        buttons.add(btnNextMove);
        buttons.add(btnCurrentMove);
    }

    public void updateButtonStates() {
        boolean info = game.isShowInfo();
        boolean over = game.isGameOver();

        buttons.forEach(b -> b.isActive = false);

        btnResign.isActive = !over;
        btnRestart.isActive = over;

        if (info) {
            btnInfo.isActive = true;
            btnTabRules.isActive = true;
            btnTabHistory.isActive = true;
            return;
        }

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

    public int getViewIndex() { return console.getViewIndex(); }
    public int getHistoryPage() { return console.getHistoryPage(); }

    public List<Button> getButtons() { return buttons; }
    public Button getBtnTabRules() { return btnTabRules; }
    public Button getBtnTabHistory() { return btnTabHistory; }
    public ControlButton getBtnResign() { return btnResign; }
    public ControlButton getBtnRestart() { return btnRestart; }
    public InfoButton getBtnInfo() { return btnInfo; }
    public HistoryButton getBtnPrevMove() { return btnPrevMove; }
    public HistoryButton getBtnNextMove() { return btnNextMove; }
}
