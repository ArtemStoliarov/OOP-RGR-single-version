package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.GameRulesEngine;
import gamelogic.CreateBoard;
import gamelogic.Board;
import gamelogic.History;
import lib.Config;
import gamelogic.Man;

import java.awt.Point;
import java.util.List;

public class GameRulesEngineTest {

    @Test
    public void testSimpleMoveAllowsTurnChange() {
        Board board = CreateBoard.createEmpty();
        History history = new History();
        GameRulesEngine engine = new GameRulesEngine(board, history);

        engine.resetTurn();
        board.setPiece(5, 2, new Man(Config.WHITE));

        boolean moved = engine.attemptMovePiece(5, 2, 4, 3);
        assertTrue(moved, "Move should succeed");
        // After a simple move turn should change
        assertEquals(Config.RED, engine.getCurrentTurn(), "Turn should have changed to RED");
    }

    @Test
    public void testCaptureRecordedInHistory() {
        Board board = CreateBoard.createEmpty();
        History history = new History();
        GameRulesEngine engine = new GameRulesEngine(board, history);

        engine.resetTurn();
        board.setPiece(4, 3, new Man(Config.RED));
        board.setPiece(5, 2, new Man(Config.WHITE));

        boolean moved = engine.attemptMovePiece(5, 2, 3, 4);
        assertTrue(moved);
        assertEquals(1, history.getSize(), "History should have one board snapshot after move");
    }
}

