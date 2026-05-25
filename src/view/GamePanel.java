package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

import controller.PhysicsEngine;
import model.Ball;
import model.GameState;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GamePanel extends JPanel {

    private Timer timer;
    private GameState gameState;
    private PhysicsEngine physicsEngine;


    public GamePanel() {

        this.gameState = new GameState();
        this.physicsEngine = new PhysicsEngine();

        int delayMs = 16;
        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (getWidth() <= 0) return;

                physicsEngine.updatePhysics(gameState.getBalls(), getWidth(), getHeight());
                repaint();
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(new Color(20, 100, 20));

        for (Ball b : gameState.getBalls()) {
            g2d.setColor(b.getColor());

            int drawX = (int) (b.getX() - b.getRadius());
            int drawY = (int) (b.getY() - b.getRadius());
            int diameter = (int) (2 * b.getRadius());

            g2d.fillOval(drawX, drawY, diameter, diameter);
        }

    }
}
