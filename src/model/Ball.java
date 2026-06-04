package model;

import java.awt.*;

public class Ball {

    public enum Group { CUE, SOLID, STRIPE, EIGHT }

    private double x, y;
    private double radius;
    private int number;
    private double vx, vy;
    private final Color color;
    private final Group group;
    private boolean potted;
    private long pocketAnimationStartMillis = 0L;
    private double spinX, spinY;

    public Ball(double x, double y, double radius, int number, Group group, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.number = number;
        this.group = group;
        this.color = color;
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getVx() { return vx; }
    public void setVx(double vx) { this.vx = vx; }

    public double getVy() { return vy; }
    public void setVy(double vy) { this.vy = vy; }

    public double getRadius() { return radius; }

    public int getNumber() { return number; }

    public Group getGroup() { return group; }
    public Color getColor() { return color; }
    public boolean isPotted() { return potted; }
    public void setPotted(boolean potted) { this.potted = potted; }
    public void beginPocketAnimation() { pocketAnimationStartMillis = System.currentTimeMillis(); }
    public boolean isPocketAnimationActive() { return potted && pocketAnimationProgress() < 1.0; }
    public double pocketAnimationProgress() {
        if (pocketAnimationStartMillis == 0L) return 1.0;
        return Math.min(1.0, (System.currentTimeMillis() - pocketAnimationStartMillis) / 520.0);
    }

    public double getSpinX() { return spinX; }
    public void setSpinX(double spinX) { this.spinX = spinX; }

    public double getSpinY() { return spinY; }
    public void setSpinY(double spinY) { this.spinY = spinY; }

    public void clearSpin() {
        spinX = 0;
        spinY = 0;
    }

    public boolean isMoving() { return Math.hypot(vx, vy) > 0.15; }
    public void stop() { vx = 0; vy = 0; }



}
