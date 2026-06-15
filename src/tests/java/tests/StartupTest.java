package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import startup.Main;
import gamelogic.Game;
import draw.Artist;
import playerinput.ClickHandler;

/**
 * Тестування запуску та ініціалізації системи.
 */
public class StartupTest {

    @Test
    public void testMainStartup() {
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Критична помилка: Запуск програми завершився викликом виключення");
    }

    @Test
    public void testDependenciesWiring() {
        assertDoesNotThrow(() -> {
            Game game = new Game();
            Artist artist = new Artist(game);
            ClickHandler handler = new ClickHandler(game);

            assertNotNull(game.getBoard(), "Дошка не створилася");
            assertNotNull(game.getSideBar(), "Бічна панель не ініціалізувалася");
        }, "Помилка зв'язування архітектури MVC");
    }
}
