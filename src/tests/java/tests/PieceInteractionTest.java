package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.Board;
import gamelogic.CreateBoard;
import gamelogic.Man;
import gamelogic.King;
import gamelogic.Piece;
import lib.Config;

import java.awt.Point;
import java.util.List;
import java.util.Map;

/**
 * Тестування логіки ходів, побиття, блокування фігур та перетворень.
 */
public class PieceInteractionTest {

    @Test
    public void testSimpleMove() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(5, 2, new Man(Config.WHITE));

        Map<Point, List<Point>> moves = board.getPiece(5, 2).getValidMoves(board, 5, 2);

        assertTrue(moves.containsKey(new Point(4, 1)), "Доступний хід вліво-вгору");
        assertTrue(moves.containsKey(new Point(4, 3)), "Доступний хід вправо-вгору");
        assertFalse(moves.containsKey(new Point(6, 1)), "Звичайна шашка не може ходити назад без биття");
    }

    @Test
    public void testSingleCapture() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(4, 3, new Man(Config.RED));
        board.setPiece(5, 4, new Man(Config.WHITE));

        Map<Point, List<Point>> moves = board.getPiece(4, 3).getValidMoves(board, 4, 3);
        Point landing = new Point(6, 5);

        assertTrue(moves.containsKey(landing), "Має бути можливість побити");
        assertEquals(1, moves.get(landing).size(), "Жертва має бути одна");
        assertEquals(new Point(5, 4), moves.get(landing).get(0), "Правильна координата жертви");
    }

    @Test
    public void testMultiCapture() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(2, 1, new Man(Config.RED));
        board.setPiece(3, 2, new Man(Config.WHITE));
        board.setPiece(5, 4, new Man(Config.WHITE));

        Map<Point, List<Point>> moves = board.getPiece(2, 1).getValidMoves(board, 2, 1);
        Point landing = new Point(6, 5);

        assertTrue(moves.containsKey(landing), "Має існувати маршрут подвійного биття");
        assertEquals(2, moves.get(landing).size(), "Кількість жертв у списку - 2");
    }

    @Test
    public void testBlockedPiece() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(0, 0, new Man(Config.WHITE));
        board.setPiece(1, 1, new Man(Config.RED));
        board.setPiece(2, 2, new Man(Config.RED));

        Map<Point, List<Point>> moves = board.getPiece(0, 0).getValidMoves(board, 0, 0);

        assertTrue(moves.isEmpty(), "Заблокована шашка не має жодних ходів");
    }

    @Test
    public void testPromotionToKing() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(1, 2, new Man(Config.WHITE));
        board.move(1, 2, 0, 3);

        Piece finalPiece = board.getPiece(0, 3);
        assertNotNull(finalPiece, "Фігура має переміститися");
        assertTrue(finalPiece.isKing(), "Шашка має стати дамкою після досягнення кінця дошки");
    }

    @Test
    public void testPromotionDuringMultiCapture() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(4, 1, new Man(Config.WHITE));
        board.setPiece(3, 2, new Man(Config.RED));
        board.setPiece(1, 4, new Man(Config.RED));

        Map<Point, List<Point>> moves = board.getPiece(4, 1).getValidMoves(board, 4, 1);
        Point finalLanding = new Point(0, 5);

        assertTrue(moves.containsKey(finalLanding), "Має бути знайдено шлях подвійного биття на останню лінію");
        assertEquals(2, moves.get(finalLanding).size(), "Кількість жертв має дорівнювати 2");

        board.move(4, 1, 0, 5);
        board.remove(moves.get(finalLanding));

        Piece finalPiece = board.getPiece(0, 5);
        assertTrue(finalPiece.isKing(), "Після завершення серії биття на останній лінії шашка стає дамкою");
        assertEquals(0, board.redLeft, "Побиті червоні шашки мають бути видалені");
    }

    @Test
    public void testKingMultiCapture() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(5, 0, new King(Config.WHITE));
        board.setPiece(3, 2, new Man(Config.RED));
        board.setPiece(1, 6, new Man(Config.RED));

        Map<Point, List<Point>> moves = board.getPiece(5, 0).getValidMoves(board, 5, 0);
        Point finalLanding = new Point(2, 7);

        assertTrue(moves.containsKey(finalLanding), "Дамка має правильно обирати проміжні точки приземлення");
        assertEquals(2, moves.get(finalLanding).size(), "Дамка має знайти шлях, щоб побити обидві фігури");

        List<Point> skipped = moves.get(finalLanding);
        assertTrue(skipped.contains(new Point(3, 2)), "Перша жертва має бути на 3,2");
        assertTrue(skipped.contains(new Point(1, 6)), "Друга жертва має бути на 1,6");
    }
}
