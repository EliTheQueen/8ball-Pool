package model;

import java.awt.*;

public class BasicCue extends Cue {

    public BasicCue() {
        super("Basic Wood", new Color(139, 69, 19), 6);
    }

    @Override
    public void draw(Graphics2D g2d, double x, double y, double angle, double power, double radius) {

        g2d.setColor(this.getColor());
        g2d.setStroke(new BasicStroke(this.getThickness()));

        double startDist = radius + 5 + (power * 2);
        int x1 = (int) (x + Math.cos(angle) * startDist);
        int y1 = (int) (y + Math.sin(angle) * startDist);
        int x2 = (int) (x + Math.cos(angle) * (startDist + 150));
        int y2 = (int) (y + Math.sin(angle) * (startDist + 150));

        g2d.drawLine(x1, y1, x2, y2);
    }
}
