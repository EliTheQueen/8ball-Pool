package controller;

import model.Ball;
import model.GameState;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CueController extends MouseAdapter {

    private Ball cueBall;
    private Point mousePoint = new Point();
    private boolean isDragging = false;
    private double power = 0;
    private double angle = 0;
    GameState gameState;

    public CueController(Ball ball, GameState gameState) {
        this.cueBall = ball;
        this.gameState = gameState;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState.isEverythingStopped()) {
            isDragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(gameState.isEverythingStopped()) {
            if (isDragging) {
                hitBall();
                isDragging = false;
                power = 0;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePoint = e.getPoint();
        calculateAngleAndPower();
    }

    private void calculateAngleAndPower() {
        double dx = mousePoint.x - cueBall.getX();
        double dy = mousePoint.y - cueBall.getY();

        angle = Math.atan2(dy, dx);

        double distance = Math.sqrt(dx * dx + dy * dy);
        power = Math.min(distance / 5, 20);
    }

    private void hitBall() {
        double dx = mousePoint.x - cueBall.getX();
        double dy = mousePoint.y - cueBall.getY();

        double angle = Math.atan2(dy, dx);

        cueBall.setVx(-Math.cos(angle) * power * 0.65);
        cueBall.setVy(-Math.sin(angle) * power * 0.65);
    }


    public double getAngle() { return angle; }
    public double getPower() { return power; }
    public boolean isDragging() { return isDragging; }


}
