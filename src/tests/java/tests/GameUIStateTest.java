package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.GameUIState;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameUIStateTest {

    @Test
    public void testSelectAndDeselect() {
        GameUIState ui = new GameUIState();
        assertFalse(ui.hasPieceSelected());
        ui.selectPiece(2, 3);
        assertTrue(ui.hasPieceSelected());
        assertEquals(new Point(2,3), ui.getSelectedPosition());

        ui.deselectPiece();
        assertFalse(ui.hasPieceSelected());
    }

    @Test
    public void testValidMovesSetClear() {
        GameUIState ui = new GameUIState();
        Map<Point, List<Point>> m = new HashMap<>();
        ui.setValidMoves(m);
        assertNotNull(ui.getValidMoves());
        ui.clearValidMoves();
        assertTrue(ui.getValidMoves().isEmpty());
    }
}

