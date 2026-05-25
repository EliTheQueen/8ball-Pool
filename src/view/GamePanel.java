package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import model.Ball;

public class GamePanel extends JPanel {
    private Ball ball;

    public GamePanel() {

        ball = new Ball(100, 100, 20, 1, true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(Color.WHITE);

       int r = 20;
        g2d.fillOval((int)(ball.getX() - r), (int)(ball.getY() - r), 2 * r, 2 * r);
    }
}
