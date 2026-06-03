package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(GameWindow window) {
        setLayout(new GridBagLayout());
        setOpaque(true);

        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(42, 72, 48, 72));

        BilliardLogo logo = new BilliardLogo();
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("8-Ball Pool");
        title.setFont(Theme.TITLE_FONT.deriveFont(Font.BOLD, 43f));
        title.setForeground(Theme.IVORY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(18));
        card.add(title);
        card.add(Box.createVerticalStrut(34));
        card.add(button("Start Game", () -> window.showGame()));
        card.add(Box.createVerticalStrut(13));
        card.add(button("Settings", () -> window.showSettings()));
        card.add(Box.createVerticalStrut(13));
        card.add(button("Records", () -> window.showRecords()));
        card.add(Box.createVerticalStrut(13));
        card.add(button("Exit", () -> System.exit(0)));
        add(card);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Theme.paintGradient(g2, getWidth(), getHeight());

        int w = Math.min(560, getWidth() - 64);
        int h = 500;
        int x = (getWidth() - w) / 2;
        int y = Math.max(32, (getHeight() - h) / 2);

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(x + 14, y + 18, w, h, 46, 46);
        g2.setPaint(new LinearGradientPaint(x, y, x, y + h,
                new float[]{0f, .55f, 1f},
                new Color[]{new Color(24, 27, 30, 246), new Color(13, 43, 34, 242), new Color(11, 16, 17, 248)}));
        g2.fillRoundRect(x, y, w, h, 46, 46);

        g2.setComposite(AlphaComposite.SrcOver.derive(.16f));
        g2.setPaint(new RadialGradientPaint(new Point2D.Double(x + w / 2.0, y + 86), w * .55f,
                new float[]{0f, 1f}, new Color[]{Theme.BRASS, new Color(0,0,0,0)}));
        g2.fillRoundRect(x, y, w, h, 46, 46);
        g2.setComposite(AlphaComposite.SrcOver);

        g2.setColor(new Color(176, 141, 87, 190));
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 46, 46);
        g2.setColor(new Color(231, 223, 210, 42));
        g2.drawRoundRect(x + 10, y + 10, w - 21, h - 21, 36, 36);
        g2.dispose();
    }

    private JButton button(String text, Runnable action) {
        JButton b = Theme.button(text);
        b.addActionListener(e -> action.run());
        return b;
    }

    private static class BilliardLogo extends JComponent {
        BilliardLogo() {
            setPreferredSize(new Dimension(118, 118));
            setMaximumSize(new Dimension(118, 118));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int s = Math.min(getWidth(), getHeight()) - 12;
            int x = (getWidth() - s) / 2;
            int y = (getHeight() - s) / 2;
            g2.setColor(new Color(0, 0, 0, 95));
            g2.fillOval(x + 7, y + 10, s, s);
            g2.setPaint(new RadialGradientPaint(new Point2D.Double(x + s * .33, y + s * .26), s,
                    new float[]{0f, .42f, 1f},
                    new Color[]{new Color(62, 64, 68), new Color(9, 10, 12), new Color(0, 0, 0)}));
            g2.fillOval(x, y, s, s);
            g2.setPaint(new RadialGradientPaint(new Point2D.Double(x + s * .30, y + s * .24), s * .55f,
                    new float[]{0f, 1f}, new Color[]{new Color(255,255,255,90), new Color(255,255,255,0)}));
            g2.fillOval(x + 8, y + 7, s - 16, s - 16);
            int badge = (int)(s * .48);
            int bx = x + (s - badge) / 2;
            int by = y + (s - badge) / 2;
            g2.setPaint(new RadialGradientPaint(new Point2D.Double(bx + badge * .35, by + badge * .28), badge,
                    new float[]{0f, .72f, 1f},
                    new Color[]{Color.WHITE, new Color(245, 241, 231), new Color(213, 205, 190)}));
            g2.fillOval(bx, by, badge, badge);
            g2.setColor(new Color(18, 20, 23, 235));
            g2.setFont(new Font("SansSerif", Font.BOLD, 34));
            String text = "8";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, x + s / 2 - fm.stringWidth(text) / 2, y + s / 2 + (fm.getAscent() - fm.getDescent()) / 2 - 1);
            g2.setColor(new Color(176, 141, 87, 185));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x + 1, y + 1, s - 2, s - 2);
            g2.dispose();
        }
    }
}
