package draw;

import gamelogic.Board;
import gamelogic.Game;
import gamelogic.Piece;
import lib.Config;
import uilogic.Button;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.List;

public class Artist extends JPanel {
    private Game game;

    public Artist(Game game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Board boardToDraw = game.getBoardToDraw();

        drawBoard(g2d, boardToDraw);
        drawPieces(g2d, boardToDraw);

        if (!game.isViewingPast()) {
            drawHighlight(g2d);
            drawValidMoves(g2d);
        }

        if (game.isGameOver()) drawGameOver(g2d);
        if (game.isShowInfo()) drawInfoOverlay(g2d);

        drawSidebar(g2d);
    }

    private void drawBoard(Graphics2D g2d, Board board) {
        g2d.setColor(Config.GREEN);
        g2d.fillRect(0, 0, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);

        int squareNum = 1;
        for (int row = 0; row < Config.ROWS; row++) {
            for (int col = 0; col < Config.COLS; col++) {
                squareNum = drawSquare(g2d, row, col, squareNum);
            }
        }
        drawLastMove(g2d, board);
    }

    private int drawSquare(Graphics2D g2d, int row, int col, int currentNum) {
        boolean isPlayable = (row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0);

        if (isPlayable) {
            g2d.setColor(Config.CREAM);
            g2d.fillRect(col * Config.SQUARE_SIZE, row * Config.SQUARE_SIZE, Config.SQUARE_SIZE, Config.SQUARE_SIZE);
            return currentNum;
        }

        g2d.setColor(new Color(150, 100, 50));
        g2d.setFont(Config.SMALL_FONT);
        g2d.drawString(String.valueOf(currentNum), col * Config.SQUARE_SIZE + 5, row * Config.SQUARE_SIZE + 20);
        return currentNum + 1;
    }

    private void drawLastMove(Graphics2D g2d, Board board) {
        int[] lastMove = board.getLastMove();
        if (lastMove == null) return;

        g2d.setColor(Config.LAST_MOVE_COLOR);
        g2d.fillRect(lastMove[1] * Config.SQUARE_SIZE, lastMove[0] * Config.SQUARE_SIZE, Config.SQUARE_SIZE, Config.SQUARE_SIZE);
        g2d.fillRect(lastMove[3] * Config.SQUARE_SIZE, lastMove[2] * Config.SQUARE_SIZE, Config.SQUARE_SIZE, Config.SQUARE_SIZE);
    }

    private void drawPieces(Graphics2D g2d, Board board) {
        for (int row = 0; row < Config.ROWS; row++) {
            for (int col = 0; col < Config.COLS; col++) {
                drawSinglePiece(g2d, board.getPiece(row, col), row, col);
            }
        }
    }

    private void drawSinglePiece(Graphics2D g2d, Piece p, int row, int col) {
        if (p == null) return;

        int px = Config.SQUARE_SIZE * col + Config.SQUARE_SIZE / 2;
        int py = Config.SQUARE_SIZE * row + Config.SQUARE_SIZE / 2;
        int radius = Config.SQUARE_SIZE / 2 - Config.PADDING;

        g2d.setColor(Config.BLACK);
        g2d.fillOval(px - radius - Config.OUTLINE, py - radius - Config.OUTLINE, (radius + Config.OUTLINE) * 2, (radius + Config.OUTLINE) * 2);

        g2d.setColor(p.color);
        g2d.fillOval(px - radius, py - radius, radius * 2, radius * 2);

        if (p.isKing()) {
            int crownRadius = (int)(radius / 2.5);
            g2d.setColor(Config.GOLD);
            g2d.fillOval(px - crownRadius, py - crownRadius, crownRadius * 2, crownRadius * 2);
        }
    }

    private void drawHighlight(Graphics2D g2d) {
        Point p = game.getSelectedPos();
        if (p == null) return;

        int px = Config.SQUARE_SIZE * p.y + Config.SQUARE_SIZE / 2;
        int py = Config.SQUARE_SIZE * p.x + Config.SQUARE_SIZE / 2;
        int radius = Config.SQUARE_SIZE / 2 - Config.PADDING + Config.OUTLINE;

        g2d.setColor(Config.GOLD);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawOval(px - radius, py - radius, radius * 2, radius * 2);
        g2d.setStroke(new BasicStroke(1));
    }

    private void drawValidMoves(Graphics2D g2d) {
        if (game.getValidMoves() == null) return;

        g2d.setColor(Config.GOLD);
        for (Point p : game.getValidMoves().keySet()) {
            int cx = p.y * Config.SQUARE_SIZE + Config.SQUARE_SIZE / 2;
            int cy = p.x * Config.SQUARE_SIZE + Config.SQUARE_SIZE / 2;
            g2d.fillOval(cx - 15, cy - 15, 30, 30);
        }
    }

    private void drawSidebar(Graphics2D g2d) {
        g2d.setColor(Config.GREY);
        g2d.fillRect(Config.BOARD_WIDTH, 0, Config.SIDEBAR_WIDTH, Config.BOARD_HEIGHT);

        g2d.setColor(Config.WHITE);
        g2d.drawLine(Config.BOARD_WIDTH, 0, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);

        Board board = game.getBoard();
        int alignX = Config.BOARD_WIDTH + 15;

        g2d.setFont(Config.MAIN_FONT);
        g2d.setColor(Config.WHITE);
        g2d.drawString("ХІД: ", alignX, 50);

        int turnOffset = g2d.getFontMetrics().stringWidth("ХІД: ");
        if (game.getTurn().equals(Config.RED)) {
            g2d.setColor(Config.RED);
            g2d.drawString("ЧЕРВОНІ", alignX + turnOffset, 50);
        } else {
            g2d.setColor(Config.WHITE);
            g2d.drawString("БІЛІ", alignX + turnOffset, 50);
        }

        if (game.isViewingPast()) {
            g2d.setColor(Config.GOLD);
            Font smallViewFont = Config.SMALL_FONT.deriveFont(16f);
            g2d.setFont(smallViewFont);
            g2d.drawString("РЕЖИМ ПЕРЕГЛЯДУ", alignX, 80);
        }

        g2d.setFont(Config.SMALL_FONT);
        g2d.setColor(Config.RED);
        g2d.drawString("Червоні: " + board.redLeft, alignX, 130);
        g2d.setColor(Config.WHITE);
        g2d.drawString("Білі: " + board.whiteLeft, alignX, 160);

        for (Button btn : game.getSideBar().getButtons()) {
            if (btn == game.getSideBar().getBtnTabRules() || btn == game.getSideBar().getBtnTabHistory()) continue;
            drawButtonIfActive(g2d, btn);
        }

        drawMiniConsole(g2d);
    }

    private void drawButtonIfActive(Graphics2D g2d, Button btn) {
        if (!btn.isActive) return;

        g2d.setColor(btn.isHover ? Config.BUTTON_HOVER : Config.BUTTON_COLOR);
        g2d.fillRoundRect(btn.rect.x, btn.rect.y, btn.rect.width, btn.rect.height, 10, 10);

        g2d.setColor(Config.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(btn.rect.x, btn.rect.y, btn.rect.width, btn.rect.height, 10, 10);

        g2d.setFont(Config.MAIN_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = btn.rect.x + (btn.rect.width - fm.stringWidth(btn.text)) / 2;
        int textY = btn.rect.y + ((btn.rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(btn.text, textX, textY);
    }

    private void drawMiniConsole(Graphics2D g2d) {
        int startY = 340;
        int consoleHeight = 360;
        int consoleX = Config.BOARD_WIDTH + 15;

        g2d.setColor(Config.BLACK);
        g2d.fillRect(consoleX, startY, 200, consoleHeight);

        g2d.setFont(Config.RULES_FONT);

        List<String> history = game.getTextHistory();
        int activeIdx = game.getSideBar().getViewIndex() - 1;
        int maxRecords = 14;
        int startIndex = 0;

        if (history.size() > maxRecords) {
            if (game.isViewingPast() && activeIdx >= 0) {
                startIndex = Math.max(0, activeIdx - (maxRecords / 2));
                if (startIndex + maxRecords > history.size()) {
                    startIndex = history.size() - maxRecords;
                }
            } else {
                startIndex = history.size() - maxRecords;
            }
        }

        int currentY = startY + 25;
        int textPadding = consoleX + 10;

        for (int i = startIndex; i < Math.min(history.size(), startIndex + maxRecords); i++) {
            String line = history.get(i);

            String numPrefix = (i + 1) + ". ";
            g2d.setColor(Config.WHITE);
            g2d.drawString(numPrefix, textPadding, currentY);
            int numW = g2d.getFontMetrics().stringWidth(numPrefix);

            String colorChar = line.substring(0, 1);
            g2d.setColor(colorChar.equals("Ч") ? Config.RED : Config.WHITE);
            g2d.drawString(colorChar, textPadding + numW, currentY);
            int charW = g2d.getFontMetrics().stringWidth(colorChar);

            String rest = line.substring(1);
            g2d.setColor(Config.WHITE);
            g2d.drawString(rest, textPadding + numW + charW, currentY);

            if (activeIdx == i) {
                g2d.setColor(Config.GOLD);
                g2d.drawString("<", consoleX + 180, currentY);
            }

            currentY += 25;
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);

        g2d.setColor(Config.GOLD);
        g2d.setFont(Config.MAIN_FONT);
        FontMetrics fm = g2d.getFontMetrics();

        String[] words = game.getWinnerText().split("\\.");
        int yOffset = 0;
        for (String line : words) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            int x = (Config.BOARD_WIDTH - fm.stringWidth(trimmed)) / 2;
            int y = Config.BOARD_HEIGHT / 2 + yOffset;
            g2d.drawString(trimmed, x, y);
            yOffset += 50;
        }
    }

    private void drawInfoOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 230));
        g2d.fillRect(0, 0, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);

        Button tabRules = game.getSideBar().getBtnTabRules();
        Button tabHistory = game.getSideBar().getBtnTabHistory();

        g2d.setFont(Config.MAIN_FONT);
        FontMetrics fm = g2d.getFontMetrics();

        g2d.setColor(game.getInfoTab() == 0 ? Config.WHITE : Color.GRAY);
        g2d.drawString(tabRules.text, tabRules.rect.x, 50);

        g2d.setColor(game.getInfoTab() == 1 ? Config.WHITE : Color.GRAY);
        g2d.drawString(tabHistory.text, tabHistory.rect.x, 50);

        g2d.setColor(Config.GOLD);
        if (game.getInfoTab() == 0) {
            g2d.fillRect(tabRules.rect.x, 60, fm.stringWidth(tabRules.text), 4);
        } else {
            g2d.fillRect(tabHistory.rect.x, 60, fm.stringWidth(tabHistory.text), 4);
        }

        g2d.setColor(Config.WHITE);
        g2d.setFont(Config.RULES_FONT);
        if (game.getInfoTab() == 0) {
            drawRulesContent(g2d);
        } else {
            drawHistoryContent(g2d);
        }
    }

    private void drawRulesContent(Graphics2D g2d) {
        String[] rules = {
                "- Дошка 8x8, клітинки 1-32",
                "- Білі ходять першими",
                "- Шашки ходять вперед по діагоналі",
                "- Шашка, що дійшла до кінця, стає дамкою",
                "- Дамки ходять на всю діагональ",
                "- Бити обов'язково (в тому числі назад)",
                "",
                "Натисніть 'ІНФО' щоб закрити вікно"
        };
        int startY = 150;
        for (String line : rules) {
            g2d.drawString(line, 50, startY);
            startY += 40;
        }
    }

    private void drawHistoryContent(Graphics2D g2d) {
        List<String> history = game.getTextHistory();

        int movesPerPage = 76;
        int totalPages = (int) Math.ceil((double) Math.max(1, history.size()) / movesPerPage);
        int currentPage = game.getSideBar().getHistoryPage();

        if (currentPage >= totalPages) currentPage = Math.max(0, totalPages - 1);

        int startIdx = currentPage * movesPerPage;
        int endIdx = Math.min(startIdx + movesPerPage, history.size());

        int x = 50;
        int y = 140;

        for (int i = startIdx; i < endIdx; i++) {
            g2d.drawString((i + 1) + ". " + history.get(i), x, y);
            y += 30;
            if (y > Config.BOARD_HEIGHT - 80) {
                y = 140;
                x += 180;
            }
        }

        if (totalPages > 1) {
            int tabWidth = 80;
            int startX = 50;
            int tabY = Config.BOARD_HEIGHT - 50;
            int tabHeight = 30;

            for (int p = 0; p < totalPages; p++) {
                int tx = startX + p * (tabWidth + 5);

                if (p == currentPage) {
                    g2d.setColor(Config.GREY);
                    g2d.fillRect(tx, tabY, tabWidth, tabHeight);
                    g2d.setColor(Config.GOLD);
                    g2d.drawRect(tx, tabY, tabWidth, tabHeight);
                } else {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRect(tx, tabY, tabWidth, tabHeight);
                    g2d.setColor(Config.WHITE);
                    g2d.drawRect(tx, tabY, tabWidth, tabHeight);
                }

                g2d.setColor(Config.WHITE);
                g2d.drawString("Стор " + (p + 1), tx + 10, tabY + 20);
            }
        }
    }
}
