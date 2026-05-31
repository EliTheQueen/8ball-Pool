package controller;

import model.*;
import java.awt.*;
import java.util.ArrayList;

public class PhysicsEngine {
    private static final double FRICTION = 0.992;
    private static final double STOP_THRESHOLD = 0.08;
    private static final double RESTITUTION = 0.96;
    private GameState gameState;

    public PhysicsEngine(GameState gameState) {
        this.gameState = gameState;
    }

    public void updatePhysics(GameState state) {
        Rectangle area = state.getTable().playArea();
        ArrayList<Ball> balls = state.getBalls();
        for (Ball b : balls) {
            if (b.isPotted()) continue;
            applyFriction(b); moveBall(b); checkWallCollision(b, area); checkPocket(state, b);
        }
        handleBallCollisions(balls, state);
        state.finishShotIfStopped();
    }

    private void applyFriction(Ball b) {
        b.setVx(b.getVx() * FRICTION); b.setVy(b.getVy() * FRICTION);
        if (Math.abs(b.getVx()) < STOP_THRESHOLD) b.setVx(0);
        if (Math.abs(b.getVy()) < STOP_THRESHOLD) b.setVy(0);
    }
    private void moveBall(Ball b) { b.setX(b.getX() + b.getVx()); b.setY(b.getY() + b.getVy()); }

    private void checkWallCollision(Ball b, Rectangle a) {
        double r = b.getRadius();
        if (b.getX() - r < a.x) { b.setX(a.x + r); b.setVx(-b.getVx() * RESTITUTION); }
        if (b.getX() + r > a.x + a.width) { b.setX(a.x + a.width - r); b.setVx(-b.getVx() * RESTITUTION); }
        if (b.getY() - r < a.y) { b.setY(a.y + r); b.setVy(-b.getVy() * RESTITUTION); }
        if (b.getY() + r > a.y + a.height) { b.setY(a.y + a.height - r); b.setVy(-b.getVy() * RESTITUTION); }
    }

    private void checkPocket(GameState state, Ball b) {
        for (Point p : state.getTable().pockets()) if (p.distance(b.getX(), b.getY()) < Table.POCKET_RADIUS) { state.potBall(b); return; }
    }

    private void handleBallCollisions(ArrayList<Ball> balls, GameState state) {
        for (int i = 0; i < balls.size(); i++) for (int j = i + 1; j < balls.size(); j++) {
            Ball b1 = balls.get(i), b2 = balls.get(j);
            if (!b1.isPotted() && !b2.isPotted()) {

                if (isColliding(b1, b2)) {

                    if (b1.getGroup() == Ball.Group.CUE &&
                            b2.getGroup() != Ball.Group.CUE) {

                        gameState.getShotResult().setFirstHitBall(b2);

                    } else if (b2.getGroup() == Ball.Group.CUE &&
                            b1.getGroup() != Ball.Group.CUE) {

                        gameState.getShotResult().setFirstHitBall(b1);
                    }

                    resolveCollision(b1, b2, state);
                }
            }
        }
    }

    private void resolveCollision(Ball b1, Ball b2, GameState state) {
        double dx = b2.getX() - b1.getX(), dy = b2.getY() - b1.getY();
        double dist = Math.hypot(dx, dy), minDist = b1.getRadius() + b2.getRadius();
        if (dist <= 0 || dist > minDist) return;
        double nx = dx / dist, ny = dy / dist;
        double rvx = b2.getVx() - b1.getVx(), rvy = b2.getVy() - b1.getVy();
        double velAlongNormal = rvx * nx + rvy * ny;
        double overlap = minDist - dist + 0.2;
        b1.setX(b1.getX() - nx * overlap / 2); b1.setY(b1.getY() - ny * overlap / 2);
        b2.setX(b2.getX() + nx * overlap / 2); b2.setY(b2.getY() + ny * overlap / 2);
        if (velAlongNormal > 0) return;
        double impulse = -(1 + RESTITUTION) * velAlongNormal / 2.0;
        b1.setVx(b1.getVx() - impulse * nx); b1.setVy(b1.getVy() - impulse * ny);
        b2.setVx(b2.getVx() + impulse * nx); b2.setVy(b2.getVy() + impulse * ny);
        if (b1.getGroup() == Ball.Group.CUE || b2.getGroup() == Ball.Group.CUE) applySpin(state, b1.getGroup() == Ball.Group.CUE ? b1 : b2, nx, ny);
    }

    private void applySpin(GameState state, Ball cue, double nx, double ny) {
        double s = 0.45;
        switch (state.getSpin()) {
            case TOP -> { cue.setVx(cue.getVx() + nx * s); cue.setVy(cue.getVy() + ny * s); }
            case BACK -> { cue.setVx(cue.getVx() - nx * s); cue.setVy(cue.getVy() - ny * s); }
            case LEFT -> { cue.setVx(cue.getVx() - ny * s); cue.setVy(cue.getVy() + nx * s); }
            case RIGHT -> { cue.setVx(cue.getVx() + ny * s); cue.setVy(cue.getVy() - nx * s); }
        }
    }

    private boolean isColliding(Ball b1, Ball b2) {
        double dx = b2.getX() - b1.getX();
        double dy = b2.getY() - b1.getY();

        double distance = Math.hypot(dx, dy);

        return distance <= b1.getRadius() + b2.getRadius();
    }
}
