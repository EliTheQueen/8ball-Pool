package model;

import java.awt.*;

public class Ball {

    private double x, y;
    private boolean isSolid;
    private double radius;
    private int number;
    private double vx, vy;
    private Color color;

    public Ball(double x, double y, double radius, int number, boolean isSolid, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.number = number;
        this.isSolid = isSolid;
        this.color = color;
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

    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }


    public boolean isCollidingWith(Ball other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distanceSquared = dx * dx + dy * dy;
        double radiusSum = this.radius + other.radius;
        return distanceSquared <= (radiusSum * radiusSum);
    }

    public void checkWallCollision(double panelWidth, double panelHeight) {

        if (x - radius < 0) {
            x = radius;
            vx = -vx;
        } else if (x + radius > panelWidth) {
            x = panelWidth - radius;
            vx = -vx;
        }

        if (y - radius < 0) {
            y = radius;
            vy = -vy;
        } else if (y + radius > panelHeight) {
            y = panelHeight - radius;
            vy = -vy;
        }
    }

}
