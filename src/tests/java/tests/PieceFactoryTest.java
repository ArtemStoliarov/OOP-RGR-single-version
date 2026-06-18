package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import gamelogic.PieceFactory;
import gamelogic.Man;
import gamelogic.King;
import lib.Config;

public class PieceFactoryTest {

    @Test
    public void testCreateManAndKing() {
        assertTrue(PieceFactory.createMan(Config.RED) instanceof Man);
        assertTrue(PieceFactory.createKing(Config.WHITE) instanceof King);
    }
}

