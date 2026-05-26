package model;

import java.awt.*;

public class PowerCue extends Cue {

    public PowerCue() {
        super("Power Cue", Color.GREEN, 6);
    }

    @Override
    public void draw(Graphics2D g2d, double x, double y, double angle, double power, double radius) {
        double maxPower = 20;
        double ratio = power / maxPower;

        ratio = Math.max(0.0, Math.min(ratio, 1.0));

        int red = (int) (ratio * 255);
        int green = (int) ((1.0 - ratio) * 255);
        int blue = 0;

        Color dynamicColor = new Color(red, green, blue);

        g2d.setColor(dynamicColor);
        g2d.setStroke(new BasicStroke(this.getThickness()));

        double startDist = radius + 5 + (power * 2);
        int x1 = (int) (x + Math.cos(angle) * startDist);
        int y1 = (int) (y + Math.sin(angle) * startDist);
        int x2 = (int) (x + Math.cos(angle) * (startDist + 150));
        int y2 = (int) (y + Math.sin(angle) * (startDist + 150));

        g2d.drawLine(x1, y1, x2, y2);

        drawAimLine(g2d, x, y, angle, radius);
    }

    private void drawAimLine(Graphics2D g2d, double x, double y, double angle, double radius) {
        float[] dashPattern = {10f, 10f};
        BasicStroke dashedStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                dashPattern, 0.0f);
        g2d.setStroke(dashedStroke);
        g2d.setColor(new Color(255, 255, 255, 150)); // سفید نیمه‌شفاف

        // کشیدن خط راهنما در جهت مخالف چوب (به سمت هدف)
        int targetX1 = (int) (x - Math.cos(angle) * (radius + 5));
        int targetY1 = (int) (y - Math.sin(angle) * (radius + 5));
        int targetX2 = (int) (x - Math.cos(angle) * 100);
        int targetY2 = (int) (y - Math.sin(angle) * 100);

        g2d.drawLine(targetX1, targetY1, targetX2, targetY2);
    }
}
