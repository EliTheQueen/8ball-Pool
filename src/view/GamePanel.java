package view;

import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameState gameState;
    private final PhysicsEngine physicsEngine;
    private final CueController cueController;
    private int tableOffsetX = 0;
    private int tableOffsetY = 0;

    public GamePanel(GameState gameState, PhysicsEngine physicsEngine) {
        this.gameState = gameState; this.physicsEngine = physicsEngine;
        setPreferredSize(new Dimension(Table.WIDTH, Table.HEIGHT + 70));
        setFocusable(true);
        cueController = new CueController(gameState);
        addMouseListener(cueController); addMouseMotionListener(cueController);
        new Timer(16, e -> { physicsEngine.updatePhysics(gameState); repaint(); }).start();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTable(g2d); drawBalls(g2d); drawCue(g2d); drawHud(g2d);
    }

    public int getTableOffsetX() { return tableOffsetX; }
    public int getTableOffsetY() { return tableOffsetY; }

    private void drawTable(Graphics2D g) {
        setBackground(new Color(30, 30, 30));
        g.setColor(new Color(110, 65, 25)); g.fillRoundRect(10, 10, Table.WIDTH - 20, Table.HEIGHT - 20, 35, 35);
        Rectangle a = gameState.getTable().playArea();
        g.setColor(new Color(20, 115, 50)); g.fillRect(a.x, a.y, a.width, a.height);
        g.setColor(new Color(0,0,0));
        for (int i = 0; i < gameState.getTable().pockets().size(); i++) {
            Point p = gameState.getTable().pockets().get(i);
            if (i == gameState.getSelectedPocket()) { g.setColor(Color.YELLOW); g.fillOval(p.x - 34, p.y - 34, 68, 68); g.setColor(Color.BLACK); }
            g.fillOval(p.x - Table.POCKET_RADIUS, p.y - Table.POCKET_RADIUS, Table.POCKET_RADIUS*2, Table.POCKET_RADIUS*2);
        }
        g.setColor(new Color(255,255,255,90)); g.drawLine(220, a.y, 220, a.y + a.height);
    }

    private void drawBalls(Graphics2D g) {
        for (Ball b : gameState.getBalls()) {
            if (b.isPotted()) continue;
            int x = (int)(b.getX() - b.getRadius()), y = (int)(b.getY() - b.getRadius()), d = (int)(2*b.getRadius());
            g.setColor(new Color(0,0,0,70)); g.fillOval(x+3, y+4, d, d);
            g.setColor(b.getColor()); g.fillOval(x, y, d, d);
            g.setColor(Color.BLACK); g.drawOval(x, y, d, d);
            if (b.getGroup() == Ball.Group.STRIPE) { g.setColor(Color.WHITE); g.fillRect(x+3, y+d/3, d-6, d/3); g.setColor(b.getColor()); g.drawRect(x+3, y+d/3, d-6, d/3); }
            if (b.getNumber() > 0) { g.setColor(Color.WHITE); g.fillOval((int)b.getX()-7,(int)b.getY()-7,14,14); g.setColor(Color.BLACK); g.setFont(new Font("Arial", Font.BOLD, 10)); g.drawString(String.valueOf(b.getNumber()), (int)b.getX()-4, (int)b.getY()+4); }
        }
    }

    private void drawCue(Graphics2D g) {
        if (gameState.isEverythingStopped() && cueController.isDragging()) {
            Ball cue = gameState.getCueBall();
            gameState.getCurrentCue().draw(g, cue.getX(), cue.getY(), cueController.getAngle(), cueController.getPower(), cue.getRadius());

        }
    }

    private void drawHud(Graphics2D g) {
        int y = Table.HEIGHT + 8;
        g.setColor(new Color(20,20,20)); g.fillRect(0, Table.HEIGHT, getWidth(), 70);
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 14));
        Player p1 = gameState.getPlayers()[0], p2 = gameState.getPlayers()[1];
        g.drawString("Turn: " + gameState.getCurrentPlayer().getName() + " | Cue: " + gameState.getCurrentCue().getName() + " | Spin: " + gameState.getSpin(), 20, y + 18);
        g.drawString(p1.getName()+" ["+p1.groupText()+"] score: "+p1.getScore()+"     "+p2.getName()+" ["+p2.groupText()+"] score: "+p2.getScore(), 20, y + 38);
        g.setColor(gameState.isGameOver() ? Color.ORANGE : Color.CYAN); g.drawString(gameState.getMessage(), 20, y + 58);
    }
}
