package view;

import javax.swing.JPanel;
import java.awt.*;

import controller.CueController;
import controller.PhysicsEngine;
import model.Ball;
import model.Cue;
import model.GameState;
import model.Table;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GamePanel extends JPanel {

    private Timer timer;
    private GameState gameState;
    private PhysicsEngine physicsEngine;
    private CueController cueController;
    private ArrayList<Cue> cueList;
    private Cue currentCue;


    public GamePanel(GameState gameState, PhysicsEngine physicsEngine) {
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;

        this.gameState = gameState;
        this.physicsEngine = physicsEngine;

        Ball whiteBall = gameState.getBalls().get(0);

        this.cueController = new CueController(whiteBall, gameState);

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

            g2d.setColor(new Color(0, 0, 0, 150)); // پس‌زمینه مشکی نیمه‌شفاف برای متن
            g2d.fillRect(10, 50, 150, 25);
            g2d.setColor(Color.CYAN);
            g2d.drawString("Active Cue: " + gameState.getCurrentCue().getName(), 15, 67);

        }

        if (gameState.isEverythingStopped() && cueController.isDragging()) {
            Ball cueBall = gameState.getBalls().get(0);

            gameState.getCurrentCue().draw(
                    g2d,
                    cueBall.getX(),
                    cueBall.getY(),
                    cueController.getAngle(),
                    cueController.getPower(),
                    cueBall.getRadius()
            );
        }


    }

}

private void checkPocket(GameState state, Ball b) {
    for (Point p : state.getTable().pockets()) if (p.distance(b.getX(), b.getY()) < Table.POCKET_RADIUS) { state.potBall(b); return; }
}
