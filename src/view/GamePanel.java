package view;

import javax.swing.JPanel;
import java.awt.*;

import controller.CueController;
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
    private CueController cueController;


    public GamePanel(GameState gameState, PhysicsEngine physicsEngine) {
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;

        this.gameState = gameState;
        this.physicsEngine = physicsEngine;

        Ball whiteBall = gameState.getBalls().get(0);

        this.cueController = new CueController(whiteBall);

        // ۳. متصل کردن کنترلر به این پنل (بسیار مهم)
        this.addMouseListener(cueController);
        this.addMouseMotionListener(cueController);

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

        drawCue(g2d, gameState.getBalls().get(0), cueController);

    }

    private void drawCue(Graphics2D g2, Ball cueBall, CueController controller) {
        if (!controller.isDragging()) return;

        double angle = controller.getAngle();
        double power = controller.getPower();

        // شروع چوب با کمی فاصله از توپ
        double startDist = cueBall.getRadius() + 5 + (power * 2);
        double cueLength = 150; // طول چوب

        // محاسبه نقاط ابتدا و انتهای چوب با استفاده از سینوس و کسینوس
        int x1 = (int) (cueBall.getX() + Math.cos(angle) * startDist);
        int y1 = (int) (cueBall.getY() + Math.sin(angle) * startDist);

        int x2 = (int) (cueBall.getX() + Math.cos(angle) * (startDist + cueLength));
        int y2 = (int) (cueBall.getY() + Math.sin(angle) * (startDist + cueLength));

        g2.setColor(gameState.getCurrentCue().getColor());
        g2.setStroke(new BasicStroke(gameState.getCurrentCue().getThickness()));
        g2.drawLine(x1, y1, x2, y2);
    }

}
