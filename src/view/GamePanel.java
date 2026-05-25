package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import model.Ball;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GamePanel extends JPanel {
    private Ball ball;
    private Timer timer;

    public GamePanel() {

        ball = new Ball(100, 100, 20, 1, true);

        ball.setVx(5);
        ball.setVy(3);

        int delayMs = 16;
        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball.move();
                ball.checkWallCollision(getWidth(), getHeight());
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


        g2d.setColor(Color.PINK);

       int r = 20;
        g2d.fillOval((int)(ball.getX() - r), (int)(ball.getY() - r), 2 * r, 2 * r);
    }
}
