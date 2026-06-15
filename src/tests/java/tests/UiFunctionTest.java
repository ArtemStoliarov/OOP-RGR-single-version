package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.Game;
import uilogic.SideBar;
import uilogic.Button;
import lib.Config;

import java.awt.Point;
import java.awt.Color;

/**
 * Тестування кнопок, вкладок оверлею та пагінації.
 */
public class UiFunctionTest {
    private Game game;
    private SideBar sideBar;

    @BeforeEach
    public void setUp() {
        game = new Game();
        sideBar = game.getSideBar();
    }

    @Test
    public void testResignButton() {
        Button resignBtn = sideBar.getBtnResign();

        game.toggleInfo();

        sideBar.handleMouseClick(new Point(resignBtn.rect.x + 5, resignBtn.rect.y + 5));

        assertTrue(game.isGameOver(), "Гра має завершитися після натискання 'Здатися'");
        assertFalse(resignBtn.isActive, "Кнопка 'Здатися' вимикається");
        assertFalse(game.isShowInfo(), "Оверлей ІНФО має автоматично закритися");
    }

    @Test
    public void testInfoOverlayTabs() {
        Button infoBtn = sideBar.getBtnInfo();

        sideBar.handleMouseClick(new Point(infoBtn.rect.x + 5, infoBtn.rect.y + 5));
        assertTrue(game.isShowInfo(), "Оверлей відкрито");
        assertEquals(0, game.getInfoTab(), "Стандартна вкладка має бути ПРАВИЛА (0)");

        Button tabHistoryBtn = sideBar.getBtnTabHistory();
        sideBar.handleMouseClick(new Point(tabHistoryBtn.rect.x + 5, tabHistoryBtn.rect.y + 5));

        assertEquals(1, game.getInfoTab(), "Має перемкнутися на ІСТОРІЮ (1)");
        // ВИПРАВЛЕНО: Тепер звертаємось до sideBar, а не до game
        assertEquals(0, sideBar.getHistoryPage(), "При перемиканні вкладок сторінка історії скидається на 0");
    }

    @Test
    public void testHistoryPlayerNavigation() {
        for (int i = 0; i < 3; i++) {
            game.getHistory().recordMove(0, 0, 1, 1, false, Color.WHITE, game.getBoard());
            sideBar.syncWithNewMove(game.getHistory().getSize());
        }

        Button prevBtn = sideBar.getBtnPrevMove();
        Button nextBtn = sideBar.getBtnNextMove();

        sideBar.handleMouseClick(new Point(prevBtn.rect.x + 5, prevBtn.rect.y + 5));
        assertTrue(game.isViewingPast(), "Режим перегляду має бути активовано");

        sideBar.handleMouseClick(new Point(nextBtn.rect.x + 5, nextBtn.rect.y + 5));
        assertFalse(game.isViewingPast(), "Має відбутися повернення у теперішній час");
    }

    @Test
    public void testExcelPagination() {
        for (int i = 0; i < 80; i++) {
            game.getHistory().recordMove(0, 0, 1, 1, false, Color.WHITE, game.getBoard());
            sideBar.syncWithNewMove(game.getHistory().getSize());
        }

        game.toggleInfo();
        game.setInfoTab(1);

        assertEquals(0, sideBar.getHistoryPage(), "Початкова сторінка має бути 0");

        int clickX = 170;
        int clickY = Config.BOARD_HEIGHT - 35;
        sideBar.handleMouseClick(new Point(clickX, clickY));

        assertEquals(1, sideBar.getHistoryPage(), "Сторінка історії має перемкнутися на 1 (Стор 2)");
    }
}
