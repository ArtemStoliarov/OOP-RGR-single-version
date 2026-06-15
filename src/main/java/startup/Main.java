package startup;

import lib.Config;
import gamelogic.Game;
import draw.Artist;
import playerinput.ClickHandler;
import playerinput.MouseTracker;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Шашки");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT + 35);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            Game game = new Game();

            Artist artist = new Artist(game);

            ClickHandler clickHandler = new ClickHandler(game);

            new MouseTracker(clickHandler, artist);

            frame.add(artist);
            frame.setVisible(true);
        });
    }
}