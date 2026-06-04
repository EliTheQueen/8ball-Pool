package model;

import java.awt.*;

public class PowerCue extends Cue {

    public PowerCue() {
        super("Power Cue", Color.GREEN, 7);
    }

    @Override
    public void draw(Graphics2D g2d, double x, double y, double angle, double power, double radius) {
        double maxPower = 42;
        double ratio = power / maxPower;

        ratio = Math.max(0.0, Math.min(ratio, 1.0));

        int red = (int) (ratio * 255);
        int green = (int) ((1.0 - ratio) * 255);
        int blue = 0;

        Color dynamicColor = new Color(red, green, blue);

        double startDist = radius + 7 + (power * 2);
        int x1 = (int) (x + Math.cos(angle) * startDist);
        int y1 = (int) (y + Math.sin(angle) * startDist);
        int x2 = (int) (x + Math.cos(angle) * (startDist + 170));
        int y2 = (int) (y + Math.sin(angle) * (startDist + 170));

        g2d.setStroke(new BasicStroke(this.getThickness() + 5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(231, 223, 210, 110));
        g2d.drawLine(x1, y1, x2, y2);
        g2d.setStroke(new BasicStroke(this.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(dynamicColor);
        g2d.drawLine(x1, y1, x2, y2);
        drawAimLine(g2d, x, y, angle, radius);
    }

    private void drawAimLine(Graphics2D g2d, double x, double y, double angle, double radius) {
        float[] dashPattern = {10f, 10f};
        g2d.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
        g2d.setColor(new Color(231, 223, 210, 170));
        int targetX1 = (int) (x - Math.cos(angle) * (radius + 5));
        int targetY1 = (int) (y - Math.sin(angle) * (radius + 5));
        int targetX2 = (int) (x - Math.cos(angle) * 120);
        int targetY2 = (int) (y - Math.sin(angle) * 120);
        g2d.drawLine(targetX1, targetY1, targetX2, targetY2);
    }
}
