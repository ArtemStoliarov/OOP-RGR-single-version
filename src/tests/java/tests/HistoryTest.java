package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.Game;
import gamelogic.Man;
import lib.Config;

import java.util.List;

/**
 * Тестування системи запису історії ходів.
 */
public class HistoryTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testMoveRecording() {
        game.select(5, 2);
        game.select(4, 3); // 22 - 18

        List<String> hist = game.getTextHistory();

        assertEquals(1, hist.size(), "Історія має зберегти 1 запис");
        assertEquals("Б: 22 - 18", hist.get(0), "Текст має відповідати шашковій нотації");
    }

    @Test
    public void testCaptureRecording() {
        game.getBoard().setPiece(4, 3, new Man(Config.RED));

        game.select(5, 2);
        game.select(3, 4); // 22 x 15

        List<String> hist = game.getTextHistory();

        assertEquals(1, hist.size());
        assertEquals("Б: 22 x 15", hist.get(0), "Биття має позначатися 'x'");
    }
}
