package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.*;

public class Theme {
    public static final Color DEEP_GREEN = new Color(11, 61, 46);
    public static final Color CHARCOAL = new Color(18, 20, 23);
    public static final Color WALNUT = new Color(90, 58, 34);
    public static final Color IVORY = new Color(231, 223, 210);
    public static final Color BRASS = new Color(176, 141, 87);
    public static final Color EVERGREEN = new Color(26, 90, 67);
    public static final Color GUNMETAL = new Color(24, 27, 30);
    public static final Color SOFT_SHADOW = new Color(0, 0, 0, 95);
    public static final Color TABLE_FELT_DARK = new Color(6, 42, 32);
    public static final Color POCKET = new Color(5, 5, 6);

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 12);

    private Theme() {}

    public static JButton button(String text) {

        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(IVORY);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        button.setMaximumSize(new Dimension(314, 52));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;

    }

    public static JLabel label(String text, int size, int style) {

        JLabel label = new JLabel(text);
        label.setFont((new Font("Trebuchet MS", style, size)));
        label.setForeground(IVORY);
        return label;

    }

    public static JTextField textField(String text) {

        JTextField textField = new JTextField(text, 16) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(231, 223, 210, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                //why we use it?
                g2.dispose();
                super.paintComponent(g);
            }
        };
        textField.setOpaque(false);
        textField.setFont(BODY_FONT);
        textField.setForeground(CHARCOAL);
        textField.setCaretColor(DEEP_GREEN);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(176, 141, 87, 180), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(176, 141, 87, 155), 1, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        );
    }

    public static void paintGradient(Graphics2D g, int width,   int height) {
        Paint old = g.getPaint();
        g.setPaint(new LinearGradientPaint(0, 0, width, height,
                new float[]{0f, 0.48f, 1f},
                new Color[]{CHARCOAL, new Color(0x0A, 0x24, 0x1C), DEEP_GREEN}));
        g.fillRect(0, 0, width, height);
        g.setPaint(new RadialGradientPaint(
                new Point2D.Double(width * 0.28, height * 0.14),
                Math.max(width, height) * 0.72f,
                new float[]{0f, 1f},
                new Color[]{new Color(176, 141, 87, 76), new Color(0, 0, 0, 0)}
        ));
        g.fillRect(0, 0, width, height);
        g.setPaint(new RadialGradientPaint(
                new Point2D.Double(width * 0.88, height * 0.86),
                Math.max(width, height) * 0.55f,
                new float[]{0f, 1f},
                new Color[]{new Color(231, 223, 210, 34), new Color(0, 0, 0, 0)}
        ));
        g.fillRect(0, 0, width, height);
        g.setPaint(old);
    }

    public static void paintLineForBeauty(Graphics2D g, int width, int height) {

        Composite oldComposite = g.getComposite();
        g.setComposite(AlphaComposite.SrcOver.derive(0.10f));
        g.setColor(new Color(231, 223, 210, 42));
        g.fillRoundRect(width / 2 - Math.min(420, width / 3), 34, Math.min(840, width - 80), 2, 2, 2);
        g.setComposite(oldComposite);
    }

    private static class brassButton extends JButton {
        brassButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = 32;
            boolean armed = getModel().isArmed();
            boolean rollover = getModel().isRollover();
            g2.setColor(new Color(0, 0, 0, armed ? 95 : 70));
            g2.fillRoundRect(4, 7, getWidth() - 8, getHeight() - 7, arc, arc);
            Color top = rollover ? new Color(18, 74, 55) : new Color(13, 61, 46);
            Color bottom = armed ? new Color(64, 39, 22) : new Color(34, 42, 34);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRoundRect(0, 0, getWidth(), getHeight() - 4, arc, arc);
            g2.setColor(rollover ? new Color(231, 223, 210, 210) : new Color(176, 141, 87, 195));
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 7, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
