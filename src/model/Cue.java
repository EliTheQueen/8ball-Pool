package model;

import java.awt.*;

public class Cue {

    private String name;
    private Color color;
    private int thickness;

    public Cue(String name, Color color, int thickness) {
        this.name = name;
        this.color = color;
        this.thickness = thickness;
    }

    public Color getColor() { return color; }
    public int getThickness() { return thickness; }

    public void draw(Graphics2D g2d, double x, double y, double angle, double power, double radius) {

    }

}
