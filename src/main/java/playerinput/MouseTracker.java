package playerinput;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseTracker extends MouseAdapter {
    private ClickHandler handler;
    private JPanel view; // Твій клас Artist

    public MouseTracker(ClickHandler handler, JPanel view) {
        this.handler = handler;
        this.view = view;

        // Автоматично підключаємо "слухача" до графічної панелі
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Передаємо логіку в Handler
        handler.handleMouseClick(e.getPoint());
        // Змушуємо Artist оновити екран
        view.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        handler.handleMouseMove(e.getPoint());
        view.repaint();
    }
}
