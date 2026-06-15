package gamelogic;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Map;

public abstract class Piece {
    public Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public abstract boolean isKing();

    public abstract Map<Point, List<Point>> getValidMoves(Board board, int myRow, int myCol);
}
