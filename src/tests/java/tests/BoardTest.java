package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.Board;
import gamelogic.CreateBoard;
import gamelogic.Man;
import gamelogic.Piece;
import lib.Config;

/**
 * Тестування контейнера дошки.
 */
public class BoardTest {

    @Test
    public void testStandardBoardInitialization() {
        Board board = CreateBoard.createStandard();

        assertEquals(12, board.redLeft, "На старті має бути 12 червоних шашок");
        assertEquals(12, board.whiteLeft, "На старті має бути 12 білих шашок");

        assertNotNull(board.getPiece(0, 1), "На чорній клітинці має бути фігура");
        assertNull(board.getPiece(0, 0), "На білій клітинці не має бути фігур");
    }

    @Test
    public void testSetAndRemovePiece() {
        Board board = CreateBoard.createEmpty();

        board.setPiece(3, 4, new Man(Config.RED));
        assertEquals(1, board.redLeft, "Лічильник має збільшитися");
        assertNotNull(board.getPiece(3, 4), "Фігура має з'явитися");

        board.removePiece(3, 4);
        assertEquals(0, board.redLeft, "Лічильник має зменшитися");
        assertNull(board.getPiece(3, 4), "Фігура має зникнути");
    }

    @Test
    public void testPieceMovementUpdate() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(2, 3, new Man(Config.RED));

        board.move(2, 3, 3, 4);

        assertNull(board.getPiece(2, 3), "Стара клітинка має стати порожньою");
        assertNotNull(board.getPiece(3, 4), "Фігура має з'явитися на новій клітинці");
    }

    @Test
    public void testPromotionToKing() {
        Board board = CreateBoard.createEmpty();
        board.setPiece(1, 2, new Man(Config.WHITE));

        board.move(1, 2, 0, 3);

        Piece p = board.getPiece(0, 3);
        assertNotNull(p, "Фігура має переміститися");
        assertTrue(p.isKing(), "Шашка має стати дамкою");
    }
}
