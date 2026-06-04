package model;

import java.awt.*;

public class BasicCue extends Cue {

    public BasicCue() {
        super("Basic Wood", new Color(0x8B, 0x5A, 0x2B), 7);
    }

    @Override
    public void draw(Graphics2D g2d, double x, double y, double angle, double power, double radius) {

        g2d.setColor(this.getColor());
        g2d.setStroke(new BasicStroke(this.getThickness()));

        double startDist = radius + 7 + (power * 2);
        int x1 = (int) (x + Math.cos(angle) * startDist);
        int y1 = (int) (y + Math.sin(angle) * startDist);
        int x2 = (int) (x + Math.cos(angle) * (startDist + 170));
        int y2 = (int) (y + Math.sin(angle) * (startDist + 170));

        g2d.setColor(new Color(0, 0, 0, 115));
        g2d.setStroke(new BasicStroke(this.getThickness() + 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1 + 3, y1 + 3, x2 + 3, y2 + 3);
        g2d.setStroke(new BasicStroke(this.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(this.getColor());
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(0xB0, 0x8D, 0x57));
        g2d.drawLine(x1, y1, (int) (x + Math.cos(angle) * (startDist + 34)), (int) (y + Math.sin(angle) * (startDist + 34)));

    }
}
