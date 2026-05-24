package model;

import java.awt.*;

public class Ball {

    private double x, y;
    private boolean isSolid;
    private double radius;
    private int number;
    private double vx, vy;

    public Ball(double x, double y, double radius, int number, boolean isSolid) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.number = number;
        this.isSolid = isSolid;
        this.vx = 0;
        this.vy = 0;
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getVx() { return vx; }
    public void setVx(double vx) { this.vx = vx; }

    public double getVy() { return vy; }
    public void setVy(double vy) { this.vy = vy; }

    public void move() {
        this.x += vx;
        this.y += vy;
    }

    public boolean isCollidingWith(Ball other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distanceSquared = dx * dx + dy * dy;
        double radiusSum = this.radius + other.radius;
        return distanceSquared <= (radiusSum * radiusSum);
    }

}
