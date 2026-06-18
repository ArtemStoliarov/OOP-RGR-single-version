package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import uilogic.SideBarComponentFactory;
import uilogic.SideBarComponentFactory.SideBarButtons;
import uilogic.HistoryConsole;
import uilogic.GameInterface;

public class SideBarComponentFactoryTest {

    static class StubGame implements GameInterface {
        public void resign() {}
        public void reset() {}
        public void toggleInfo() {}
        public void setInfoTab(int tab) {}
        public boolean isShowInfo() { return false; }
        public int getInfoTab() { return 0; }
        public boolean isGameOver() { return false; }
        public int getHistorySize() { return 0; }
    }

    @Test
    public void testFactoryCreatesButtons() {
        StubGame game = new StubGame();
        HistoryConsole console = new HistoryConsole();

        SideBarButtons btns = SideBarComponentFactory.createAllSideBarComponents(game, console, null);
        assertNotNull(btns);
        assertNotNull(btns.allButtons);
        assertEquals(8, btns.allButtons.size());
    }
}

