package gamelogic;

import java.awt.Color;

/**
 * Factory for creating Piece objects.
 * Responsibility: Creating piece instances based on type.
 * This respects the Open/Closed Principle - adding new piece types
 * only requires changes here, not in Board or other classes.
 */
public class PieceFactory {

    public static Piece createMan(Color color) {
        return new Man(color);
    }

    public static Piece createKing(Color color) {
        return new King(color);
    }

    public static Piece createPiece(Piece original) {
        if (original.isKing()) {
            return createKing(original.color);
        }
        return createMan(original.color);
    }
}

