package uilogic;

import lib.Config;
import java.util.ArrayList;
import java.util.List;

/**
 * SideBarComponentFactory creates all UI components for the sidebar.
 * Responsibility: Creating and initializing sidebar buttons.
 * This separates component creation from layout logic, following Single Responsibility Principle.
 */
public class SideBarComponentFactory {

    public static class SideBarButtons {
        public final ControlButton btnResign;
        public final ControlButton btnRestart;
        public final InfoButton btnInfo;
        public final InfoButton btnTabRules;
        public final InfoButton btnTabHistory;
        public final RewindButton btnPrevMove;
        public final RewindButton btnNextMove;
        public final RewindButton btnCurrentMove;
        public final List<Button> allButtons;

        public SideBarButtons(ControlButton resign, ControlButton restart, InfoButton info,
                            InfoButton tabRules, InfoButton tabHistory,
                            RewindButton prevMove, RewindButton nextMove, RewindButton currentMove) {
            this.btnResign = resign;
            this.btnRestart = restart;
            this.btnInfo = info;
            this.btnTabRules = tabRules;
            this.btnTabHistory = tabHistory;
            this.btnPrevMove = prevMove;
            this.btnNextMove = nextMove;
            this.btnCurrentMove = currentMove;

            // Combine all buttons in one list
            this.allButtons = new ArrayList<>();
            this.allButtons.add(resign);
            this.allButtons.add(restart);
            this.allButtons.add(info);
            this.allButtons.add(tabRules);
            this.allButtons.add(tabHistory);
            this.allButtons.add(prevMove);
            this.allButtons.add(nextMove);
            this.allButtons.add(currentMove);
        }
    }

    public static SideBarButtons createAllSideBarComponents(GameInterface game, HistoryConsole console, Runnable updateButtonStatesCallback) {
        int alignX = Config.BOARD_WIDTH + 15;

        // Control buttons
        ControlButton btnResign = ControlButton.createResign(alignX, 200, game::resign);
        ControlButton btnRestart = ControlButton.createRestart(alignX, 200, game::reset);

        // Info buttons
        InfoButton btnInfo = InfoButton.createInfo(alignX, 270, game::toggleInfo);

        // Info tabs
        InfoButton btnTabRules = InfoButton.createTabRules(50, 20, () -> {
            game.setInfoTab(0);
            console.resetHistoryPage();
        });
        InfoButton btnTabHistory = InfoButton.createTabHistory(220, 20, () -> {
            game.setInfoTab(1);
            console.resetHistoryPage();
        });

        // History navigation buttons
        RewindButton btnPrevMove = RewindButton.createPrevMove(alignX, 720, () -> {
            console.viewPrevious();
            if (updateButtonStatesCallback != null) updateButtonStatesCallback.run();
        });
        RewindButton btnNextMove = RewindButton.createNextMove(alignX + 75, 720, () -> {
            console.viewNext(game.getHistorySize());
            if (updateButtonStatesCallback != null) updateButtonStatesCallback.run();
        });
        RewindButton btnCurrentMove = RewindButton.createCurrentMove(alignX + 150, 720, () -> {
            console.viewCurrent(game.getHistorySize());
            if (updateButtonStatesCallback != null) updateButtonStatesCallback.run();
        });

        return new SideBarButtons(btnResign, btnRestart, btnInfo, btnTabRules, btnTabHistory,
                                  btnPrevMove, btnNextMove, btnCurrentMove);
    }
}


