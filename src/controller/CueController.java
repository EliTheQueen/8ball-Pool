package controller;

import model.*;
import java.awt.*;
import java.awt.event.*;

public class CueController extends MouseAdapter {
    private final GameState gameState;
    private Point mousePoint = new Point();
    private boolean isDragging = false;
    private double power = 0, angle = 0;

    public CueController(GameState gameState) { this.gameState = gameState; }

    @Override public void mousePressed(MouseEvent e) {
        if (gameState.isGameOver()) return;
        if (gameState.isBallInHand()) {
            Rectangle a = gameState.getTable().playArea();
            if (a.contains(e.getPoint())) { gameState.getCueBall().setX(e.getX()); gameState.getCueBall().setY(e.getY()); gameState.clearBallInHand(); }
            return;
        }
        int pocket = gameState.getTable().pocketAt(e.getPoint());
        if (pocket >= 0) { gameState.setSelectedPocket(pocket); return; }
        if (gameState.getSelectedPocket() >= 0 && gameState.isEverythingStopped()) { mousePoint = e.getPoint(); calculateAngleAndPower(); isDragging = true; }
    }
    @Override public void mouseReleased(MouseEvent e) { if (isDragging) { mousePoint = e.getPoint(); calculateAngleAndPower(); hitBall(); isDragging = false; power = 0; } }
    @Override public void mouseDragged(MouseEvent e) { if (isDragging) { mousePoint = e.getPoint(); calculateAngleAndPower(); } }

    private void calculateAngleAndPower() {
        Ball cueBall = gameState.getCueBall();
        double dx = mousePoint.x - cueBall.getX(), dy = mousePoint.y - cueBall.getY();
        angle = Math.atan2(dy, dx); power = Math.min(Math.hypot(dx, dy) / 7.0, 24);
    }
    private void hitBall() {
        if (power < 1) return;
        Ball cueBall = gameState.getCueBall();
        cueBall.setVx(-Math.cos(angle) * power * 0.55); cueBall.setVy(-Math.sin(angle) * power * 0.55);
        gameState.startShot();
    }
    public double getAngle() { return angle; }
    public double getPower() { return power; }
    public boolean isDragging() { return isDragging; }
}
