package controller;

import model.*;
import  view.GamePanel;
import java.awt.*;
import java.awt.event.*;

public class CueController extends MouseAdapter {
    private final GameState gameState;
    private Point mousePoint = new Point();
    private boolean isDragging = false;
    private double power = 0, angle = 0;

    public CueController(GameState gameState) { this.gameState = gameState; }

    private Point toTablePoint(MouseEvent e) {
        if (e.getComponent() instanceof GamePanel panel) {
            return new Point(e.getX() - panel.getTableOffsetX(), e.getY() - panel.getTableOffsetY());
        }
        return e.getPoint();
    }

    @Override public void mousePressed(MouseEvent e) {
        if (gameState.isGameOver()) return;
        Point tablePoint = toTablePoint(e);
        if (gameState.isBallInHand()) {
            Rectangle a = gameState.getTable().playArea();
            Ball cue = gameState.getCueBall();
            double x = tablePoint.getX();
            double y = tablePoint.getY();
            double r = cue.getRadius();
            x = Math.max(a.x + r, Math.min(220, x));
            y = Math.max(a.y + r, Math.min(a.y + a.height - r, y));

            if (a.contains(tablePoint) && tablePoint.x <= 220) {
                cue.setX(x);
                cue.setY(y);
                gameState.clearBallInHand();
            }
            return;
        }
        int pocket = gameState.getTable().pocketAt(toTablePoint(e));
        if (pocket >= 0) {
            gameState.setSelectedPocket(pocket);
            return;
        }

        boolean pocketReady = gameState.isBreakShot() || gameState.getSelectedPocket() >= 0;
        if (pocketReady && gameState.isEverythingStopped()) {
            mousePoint = tablePoint;
            calculateAngleAndPower();
            isDragging = true;
        }
    }
    @Override public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            mousePoint = toTablePoint(e);
            calculateAngleAndPower();
            hitBall();
            isDragging = false;
            power = 0;
        }
    }

    @Override public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            mousePoint = toTablePoint(e);
            calculateAngleAndPower();
        }
    }

    private void calculateAngleAndPower() {
        Ball cueBall = gameState.getCueBall();
        double dx = mousePoint.x - cueBall.getX(), dy = mousePoint.y - cueBall.getY();
        angle = Math.atan2(dy, dx);
        power = Math.min(Math.hypot(dx, dy) / 7.0, 42);
    }

    private void hitBall() {
        if (power < 1) return;
        Ball cueBall = gameState.getCueBall();
        cueBall.setVx(-Math.cos(angle) * power * 0.72);
        cueBall.setVy(-Math.sin(angle) * power * 0.72);
        SoundManager.playCueStrike(power);
        gameState.startShot();
    }
    public double getAngle() { return angle; }
    public double getPower() { return power; }
    public boolean isDragging() { return isDragging; }
}
