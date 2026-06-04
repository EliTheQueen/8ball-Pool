package view;

import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GamePanel extends JPanel {
    private final GameState gameState;
    private final PhysicsEngine physicsEngine;
    private final CueController cueController;
    private int tableOffsetX = 0;
    private int tableOffsetY = 0;

    public GamePanel(GameState gameState, PhysicsEngine physicsEngine) {
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;
        setPreferredSize(new Dimension(1180, 760));
        setFocusable(true);
        setBackground(Theme.CHARCOAL);
        cueController = new CueController(gameState);
        addMouseListener(cueController);
        addMouseMotionListener(cueController);
        new Timer(16, e -> { physicsEngine.updatePhysics(gameState); repaint(); }).start();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        updateTableOffset();
        drawBackground(g2d);

        Graphics2D tableGraphics = (Graphics2D) g2d.create();
        tableGraphics.translate(tableOffsetX, tableOffsetY);
        drawTable(tableGraphics);
        drawAimAssist(tableGraphics);
        drawLegalHighlights(tableGraphics);
        drawBalls(tableGraphics);
        drawCue(tableGraphics);
        tableGraphics.dispose();

        drawHud(g2d);
        drawBallInHandNotice(g2d);
        g2d.dispose();
    }

    private void updateTableOffset() {
        int desiredSidePanel = 270;
        int minGap = 34;
        int totalNeeded = Table.WIDTH + desiredSidePanel * 2 + minGap * 2;
        if (getWidth() >= totalNeeded) {
            tableOffsetX = (getWidth() - Table.WIDTH) / 2;
        } else {
            tableOffsetX = Math.max(20, (getWidth() - Table.WIDTH) / 2);
        }
        int bottomControls = 92;
        int topInfo = 86;
        int availableH = Math.max(Table.HEIGHT, getHeight() - topInfo - bottomControls);
        tableOffsetY = topInfo + Math.max(0, (availableH - Table.HEIGHT) / 2);
    }

    public int getTableOffsetX() { return tableOffsetX; }
    public int getTableOffsetY() { return tableOffsetY; }

    private void drawBackground(Graphics2D g) {
        Theme.paintGradient(g, getWidth(), getHeight());
    }

    private void drawTable(Graphics2D g) {
        int shadowOffset = 8;
        g.setColor(Theme.SOFT_SHADOW);
        g.fillRoundRect(10 + shadowOffset, 10 + shadowOffset, Table.WIDTH - 20, Table.HEIGHT - 20, 42, 42);

        g.setPaint(new GradientPaint(0, 0, Theme.WALNUT, 0, Table.HEIGHT, Theme.WALNUT));
        g.fillRoundRect(10, 10, Table.WIDTH - 20, Table.HEIGHT - 20, 42, 42);
        g.setColor(new Color(231, 223, 210, 125));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(16, 16, Table.WIDTH - 32, Table.HEIGHT - 32, 34, 34);

        Rectangle a = gameState.getTable().playArea();
        g.setPaint(new LinearGradientPaint(a.x, a.y, a.x, a.y + a.height,
                new float[]{0f, .34f, .72f, 1f},
                new Color[]{new Color(20, 105, 67), new Color(13, 83, 54), new Color(9, 65, 43), Theme.TABLE_FELT_DARK}));
        g.fillRoundRect(a.x, a.y, a.width, a.height, 18, 18);

        Composite feltComposite = g.getComposite();
        g.setComposite(AlphaComposite.SrcOver.derive(0.055f));
        for (int i = 0; i < 720; i++) {
            int gx = a.x + 18 + Math.floorMod(i * 79, Math.max(1, a.width - 36));
            int gy = a.y + 16 + Math.floorMod(i * 47 + i / 5, Math.max(1, a.height - 32));
            g.setColor((i % 4 == 0) ? new Color(255,255,255,120) : new Color(0,0,0,110));
            g.fillRect(gx, gy, 1, 1);
        }
        g.setComposite(AlphaComposite.SrcOver.derive(0.13f));
        g.setPaint(new RadialGradientPaint(new Point2D.Double(a.x + a.width * .46, a.y + a.height * .30), a.width * .70f,
                new float[]{0f, .58f, 1f}, new Color[]{new Color(255,255,255,82), new Color(255,255,255,16), new Color(0,0,0,0)}));
        g.fillRoundRect(a.x, a.y, a.width, a.height, 18, 18);
        g.setComposite(AlphaComposite.SrcOver.derive(0.12f));
        g.setPaint(new LinearGradientPaint(a.x, a.y, a.x, a.y + a.height,
                new float[]{0f, .10f, .88f, 1f},
                new Color[]{new Color(0,0,0,120), new Color(0,0,0,0), new Color(0,0,0,0), new Color(0,0,0,130)}));
        g.fillRoundRect(a.x, a.y, a.width, a.height, 18, 18);
        g.setComposite(feltComposite);

        for (int i = 0; i < gameState.getTable().pockets().size(); i++) {
            Point p = gameState.getTable().pockets().get(i);
            int rim = Table.POCKET_RADIUS + 8;
            g.setPaint(new RadialGradientPaint(new Point2D.Double(p.x - 7, p.y - 8), rim,
                    new float[]{0f, .58f, 1f},
                    new Color[]{new Color(231, 223, 210, 155), new Color(116, 74, 38, 235), new Color(28, 18, 13, 245)}));
            g.fillOval(p.x - rim, p.y - rim, rim * 2, rim * 2);
            g.setColor(new Color(231, 223, 210, 135));
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(p.x - rim, p.y - rim, rim * 2, rim * 2);
            if (i == gameState.getSelectedPocket()) {
                g.setColor(new Color(231, 223, 210, 80));
                g.fillOval(p.x - 41, p.y - 41, 82, 82);
                g.setColor(new Color(176, 141, 87, 230));
                g.setStroke(new BasicStroke(3f));
                g.drawOval(p.x - 39, p.y - 39, 78, 78);
            }
            g.setPaint(new RadialGradientPaint(new Point2D.Double(p.x - 5, p.y - 6), Table.POCKET_RADIUS,
                    new float[]{0f, .70f, 1f}, new Color[]{new Color(23, 27, 31), new Color(7, 9, 11), Theme.POCKET}));
            g.fillOval(p.x - Table.POCKET_RADIUS, p.y - Table.POCKET_RADIUS, Table.POCKET_RADIUS * 2, Table.POCKET_RADIUS * 2);
            g.setColor(new Color(231, 223, 210, 95));
            g.setStroke(new BasicStroke(1.2f));
            g.drawOval(p.x - Table.POCKET_RADIUS, p.y - Table.POCKET_RADIUS, Table.POCKET_RADIUS * 2, Table.POCKET_RADIUS * 2);
        }

        g.setColor(new Color(231, 223, 210, 110));
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{8f, 10f}, 0));
        g.drawLine(220, a.y + 8, 220, a.y + a.height - 8);
    }

    private void drawAimAssist(Graphics2D g) {
        if (!gameState.isEverythingStopped() || !cueController.isDragging()) return;

        Ball cue = gameState.getCueBall();
        double angle = cueController.getAngle();
        double dx = -Math.cos(angle), dy = -Math.sin(angle);
        double sx = cue.getX() + dx * (cue.getRadius() + 4);
        double sy = cue.getY() + dy * (cue.getRadius() + 4);

        AimHit first = traceAim(sx, sy, dx, dy, 1500, null);
        drawAimSegment(g, sx, sy, first.x, first.y, 3.2f, new Color(255, 255, 255, 185));
        drawAimSegment(g, sx, sy, first.x, first.y, 1.2f, new Color(176, 141, 87, 145));

        g.setColor(new Color(255, 255, 255, 230));
        g.fill(new Ellipse2D.Double(first.x - 4, first.y - 4, 8, 8));

        if (first.type == AimType.WALL) {
            double rdx = dx, rdy = dy;
            if (first.wallVertical) rdx = -rdx;
            if (first.wallHorizontal) rdy = -rdy;
            AimHit second = traceAim(first.x + rdx * 3, first.y + rdy * 3, rdx, rdy, 850, null);
            drawAimSegment(g, first.x, first.y, second.x, second.y, 2.2f, new Color(255, 255, 255, 120));
            drawAimSegment(g, first.x, first.y, second.x, second.y, 1.0f, new Color(176, 141, 87, 110));
        } else if (first.type == AimType.BALL && first.ball != null) {
            Ball target = first.ball;
            g.setStroke(new BasicStroke(1.4f));
            g.setColor(new Color(255, 255, 255, 105));
            g.draw(new Ellipse2D.Double(target.getX() - target.getRadius() - 6, target.getY() - target.getRadius() - 6,
                    (target.getRadius() + 6) * 2, (target.getRadius() + 6) * 2));
        }
    }

    private void drawAimSegment(Graphics2D g, double x1, double y1, double x2, double y2, float width, Color color) {
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{16f, 13f}, 0));
        g.setColor(color);
        g.draw(new Line2D.Double(x1, y1, x2, y2));
        g.setStroke(old);
    }

    private AimHit traceAim(double sx, double sy, double dx, double dy, double maxDistance, Ball ignore) {
        Rectangle a = gameState.getTable().playArea();
        double x = sx, y = sy;
        double traveled = 0;
        double stepSize = 3.0;
        while (traveled < maxDistance) {
            double nextX = x + dx * stepSize;
            double nextY = y + dy * stepSize;
            traveled += stepSize;

            for (Point p : gameState.getTable().pockets()) {
                if (p.distance(nextX, nextY) <= Table.POCKET_RADIUS) {
                    return new AimHit(AimType.POCKET, nextX, nextY, false, false, null);
                }
            }

            for (Ball b : gameState.getBalls()) {
                if (b == ignore || b.isPotted() || b.getGroup() == Ball.Group.CUE) continue;
                if (Point2D.distance(nextX, nextY, b.getX(), b.getY()) <= b.getRadius() + gameState.getCueBall().getRadius()) {
                    return new AimHit(AimType.BALL, nextX, nextY, false, false, b);
                }
            }

            boolean wallV = nextX < a.x + gameState.getCueBall().getRadius() || nextX > a.x + a.width - gameState.getCueBall().getRadius();
            boolean wallH = nextY < a.y + gameState.getCueBall().getRadius() || nextY > a.y + a.height - gameState.getCueBall().getRadius();
            if (wallV || wallH) {
                nextX = Math.max(a.x + gameState.getCueBall().getRadius(), Math.min(a.x + a.width - gameState.getCueBall().getRadius(), nextX));
                nextY = Math.max(a.y + gameState.getCueBall().getRadius(), Math.min(a.y + a.height - gameState.getCueBall().getRadius(), nextY));
                return new AimHit(AimType.WALL, nextX, nextY, wallV, wallH, null);
            }

            x = nextX; y = nextY;
        }
        return new AimHit(AimType.NONE, x, y, false, false, null);
    }

    private enum AimType { NONE, WALL, BALL, POCKET }

    private static class AimHit {
        final AimType type;
        final double x, y;
        final boolean wallVertical, wallHorizontal;
        final Ball ball;

        AimHit(AimType type, double x, double y, boolean wallVertical, boolean wallHorizontal, Ball ball) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.wallVertical = wallVertical;
            this.wallHorizontal = wallHorizontal;
            this.ball = ball;
        }
    }

    private void drawLegalHighlights(Graphics2D g) {
        if (!gameState.isEverythingStopped() || !cueController.isDragging() || gameState.isOpenTable()) return;
        Ball.Group group = gameState.getCurrentPlayer().getGroup();
        if (group == null) return;
        if (gameState.remainingOfGroup(group) == 0) group = Ball.Group.EIGHT;
        g.setStroke(new BasicStroke(3.5f));
        for (Ball b : gameState.getBalls()) {
            if (b.isPotted() || b.getGroup() != group) continue;
            int d = (int) (2 * b.getRadius() + 10);
            g.setColor(new Color(231, 223, 210, 190));
            g.drawOval((int) (b.getX() - d / 2.0), (int) (b.getY() - d / 2.0), d, d);
            g.setColor(new Color(49, 150, 205, 170));
            g.drawOval((int) (b.getX() - d / 2.0 + 3), (int) (b.getY() - d / 2.0 + 3), d - 6, d - 6);
        }
    }

    private void drawBalls(Graphics2D g) {
        for (Ball b : gameState.getBalls()) {
            if (b.isPotted() && !b.isPocketAnimationActive()) continue;
            double progress = b.isPotted() ? b.pocketAnimationProgress() : 0.0;
            double scale = b.isPotted() ? Math.max(0.08, 1.0 - progress * 0.92) : 1.0;
            int d = Math.max(2, (int)(2 * b.getRadius() * scale));
            int x = (int)(b.getX() - d / 2.0), y = (int)(b.getY() - d / 2.0 + progress * 8);
            Composite oldBallComposite = g.getComposite();
            if (b.isPotted()) g.setComposite(AlphaComposite.SrcOver.derive((float)Math.max(0.0, 1.0 - progress)));
            g.setColor(new Color(0, 0, 0, 62));
            g.fillOval(x + 5, y + 7, d, d);
            if (b.getGroup() == Ball.Group.STRIPE) {
                g.setPaint(new RadialGradientPaint(new Point2D.Double(x + d * .34, y + d * .26), d,
                        new float[]{0f, .72f, 1f}, new Color[]{Color.WHITE, new Color(245, 242, 234), new Color(198, 194, 184)}));
                g.fillOval(x, y, d, d);
                Shape oldClip = g.getClip();
                g.setClip(new Ellipse2D.Double(x, y, d, d));
                int stripeH = Math.max(11, (int)(d * .46));
                int stripeY = y + (d - stripeH) / 2;
                g.setPaint(new GradientPaint(x, stripeY, b.getColor().brighter(), x, stripeY + stripeH, b.getColor().darker()));
                g.fillRoundRect(x - 2, stripeY, d + 4, stripeH, 10, 10);
                g.setColor(new Color(255, 255, 255, 95));
                g.drawLine(x + 3, stripeY + 2, x + d - 3, stripeY + 2);
                g.setClip(oldClip);
            } else {
                g.setPaint(new RadialGradientPaint(new Point2D.Double(x + d * .34, y + d * .28), d,
                        new float[]{0f, .38f, 1f}, new Color[]{Color.WHITE, b.getColor(), b.getColor().darker()}));
                g.fillOval(x, y, d, d);
                if (b.getGroup() == Ball.Group.SOLID) {
                    g.setColor(new Color(255, 255, 255, 52));
                    g.setStroke(new BasicStroke(2.2f));
                    g.drawOval(x + 3, y + 3, d - 6, d - 6);
                }
            }
            g.setColor(new Color(255, 255, 255, 155));
            g.fillOval(x + 5, y + 4, Math.max(5, d / 4), Math.max(5, d / 4));
            g.setColor(new Color(10, 14, 16, 210));
            g.setStroke(new BasicStroke(1.6f));
            g.drawOval(x, y, d, d);
            if (b.getNumber() > 0 && d >= 13) {
                int cx = x + d / 2, cy = y + d / 2;
                int badge = Math.max(17, (int)(d * 0.44));
                g.setColor(new Color(255, 255, 255, 244));
                g.fillOval(cx - badge / 2, cy - badge / 2, badge, badge);
                g.setColor(new Color(18, 20, 23, 80));
                g.setStroke(new BasicStroke(1.1f));
                g.drawOval(cx - badge / 2, cy - badge / 2, badge, badge);
                g.setColor(new Color(18, 20, 23, 235));
                g.setFont(new Font("SansSerif", Font.BOLD, b.getNumber() >= 10 ? 9 : 10));
                String n = String.valueOf(b.getNumber());
                FontMetrics fm = g.getFontMetrics();
                g.drawString(n, cx - fm.stringWidth(n)/2, cy + (fm.getAscent() - fm.getDescent()) / 2 - 1);
            }
            if (b.isPotted()) g.setComposite(oldBallComposite);
        }
    }

    private void drawCue(Graphics2D g) {
        if (gameState.isEverythingStopped() && cueController.isDragging()) {
            Ball cue = gameState.getCueBall();
            gameState.getCurrentCue().draw(g, cue.getX(), cue.getY(), cueController.getAngle(), cueController.getPower(), cue.getRadius());
        }
    }

    private void drawHud(Graphics2D g) {
        Player p1 = gameState.getPlayers()[0], p2 = gameState.getPlayers()[1];
        int cardW = Math.min(260, Math.max(205, (getWidth() - Table.WIDTH - 118) / 2));
        int cardH = 142;
        int cardY = tableOffsetY + Math.max(70, (Table.HEIGHT - cardH) / 2);
        int leftX = Math.max(28, tableOffsetX - cardW - 44);
        int rightX = Math.min(getWidth() - cardW - 28, tableOffsetX + Table.WIDTH + 44);

        if (leftX + cardW < tableOffsetX - 8) drawPlayerCard(g, p1, leftX, cardY, cardW, cardH, gameState.getCurrentPlayer() == p1);
        else drawPlayerCard(g, p1, 26, tableOffsetY + Table.HEIGHT + 18, 240, 86, gameState.getCurrentPlayer() == p1);

        if (rightX > tableOffsetX + Table.WIDTH + 8) drawPlayerCard(g, p2, rightX, cardY, cardW, cardH, gameState.getCurrentPlayer() == p2);
        else drawPlayerCard(g, p2, getWidth() - 266, tableOffsetY + Table.HEIGHT + 18, 240, 86, gameState.getCurrentPlayer() == p2);

        int infoW = Math.min(760, Math.max(420, getWidth() - 80));
        int infoX = (getWidth() - infoW) / 2;
        int infoY = Math.max(14, tableOffsetY - 62);
        g.setColor(new Color(18, 20, 23, 220));
        g.fillRoundRect(infoX, infoY, infoW, 56, 30, 30);
        g.setColor(new Color(176, 141, 87, 160));
        g.setStroke(new BasicStroke(1.6f));
        g.drawRoundRect(infoX, infoY, infoW, 56, 30, 30);

        g.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 11f));
        g.setColor(Theme.IVORY);
        String center = (gameState.isBreakShot() ? "Break shot: no pocket selection needed" : "Cue: " + gameState.getCurrentCue().getName())
                + "   •   Spin: " + gameState.getSpin()
                + "   •   Timer: " + gameState.getRemainingShotSeconds() + "s";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(center, infoX + (infoW - fm.stringWidth(center)) / 2, infoY + 22);

        g.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 11f));
        g.setColor(gameState.isGameOver() ? Theme.BRASS : Theme.BRASS);
        String msg = gameState.getMessage();
        if (fm.stringWidth(msg) > infoW - 46) {
            while (msg.length() > 8 && fm.stringWidth(msg + "...") > infoW - 46) msg = msg.substring(0, msg.length() - 1);
            msg += "...";
        }
        fm = g.getFontMetrics();
        g.drawString(msg, infoX + (infoW - fm.stringWidth(msg)) / 2, infoY + 44);
    }


    private void drawBallInHandNotice(Graphics2D g) {
        if (!gameState.shouldShowBallInHandNotice()) return;
        Composite oldComposite = g.getComposite();
        Font oldFont = g.getFont();

        g.setComposite(AlphaComposite.SrcOver.derive(0.82f));
        int w = Math.min(560, getWidth() - 80);
        int h = 132;
        int x = (getWidth() - w) / 2;
        int y = Math.max(120, Table.HEIGHT / 2 - h / 2);
        g.setColor(new Color(18, 20, 23, 236));
        g.fillRoundRect(x, y, w, h, 38, 38);
        g.setColor(new Color(176, 141, 87, 220));
        g.setStroke(new BasicStroke(3f));
        g.drawRoundRect(x, y, w, h, 38, 38);

        g.setFont(Theme.TITLE_FONT.deriveFont(Font.BOLD, 40f));
        g.setColor(Theme.BRASS);
        String title = "BALL IN HAND";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, x + (w - fm.stringWidth(title)) / 2, y + 56);

        g.setFont(Theme.BODY_FONT.deriveFont(Font.PLAIN, 16f));
        g.setColor(Theme.IVORY);
        String sub = "Place the cue ball behind the head string";
        fm = g.getFontMetrics();
        g.drawString(sub, x + (w - fm.stringWidth(sub)) / 2, y + 88);
        g.setComposite(oldComposite);
        g.setFont(oldFont);
    }

    private void drawPlayerCard(Graphics2D g, Player p, int x, int y, int w, int h, boolean active) {
        g.setColor(new Color(0, 0, 0, 78));
        g.fillRoundRect(x + 5, y + 8, w, h, 34, 34);
        g.setPaint(new GradientPaint(x, y, active ? new Color(11, 61, 46, 246) : new Color(18, 20, 23, 232),
                x, y + h, active ? new Color(42, 52, 40, 238) : new Color(36, 34, 30, 224)));
        g.fillRoundRect(x, y, w, h, 34, 34);
        g.setColor(active ? Theme.BRASS : new Color(176, 141, 87, 132));
        g.setStroke(new BasicStroke(active ? 2.8f : 1.4f));
        g.drawRoundRect(x, y, w, h, 34, 34);

        g.setFont(Theme.HEADER_FONT.deriveFont(Font.BOLD, 13f));
        g.setColor(Theme.IVORY);
        g.drawString((active ? "● " : "○ ") + p.getName(), x + 18, y + 28);
        g.setFont(Theme.BODY_FONT.deriveFont(Font.PLAIN, 11f));
        drawGroupPill(g, p.groupText(), x + 18, y + 42, Math.min(112, w - 36), 22);
        g.setColor(Theme.IVORY);
        g.drawString("Score  " + p.getScore(), x + 18, y + 76);
        if (h > 100) g.drawString(active ? "Now playing" : "Waiting", x + 18, y + 98);
    }

    private void drawGroupPill(Graphics2D g, String group, int x, int y, int w, int h) {
        String text = group == null ? "OPEN" : group.toUpperCase();
        boolean stripe = text.contains("STRIPE");
        boolean solid = text.contains("SOLID");
        Color fill = stripe ? new Color(231, 223, 210, 230) : solid ? new Color(31, 106, 73, 225) : new Color(36, 39, 43, 215);
        Color stroke = stripe ? new Color(176, 141, 87, 210) : solid ? new Color(231, 223, 210, 140) : new Color(176, 141, 87, 150);
        g.setColor(fill);
        g.fillRoundRect(x, y, w, h, h, h);
        if (stripe) {
            g.setColor(new Color(31, 106, 73, 215));
            g.fillRoundRect(x + 6, y + h / 2 - 3, w - 12, 6, 6, 6);
            g.setColor(new Color(18, 20, 23, 230));
        } else {
            g.setColor(Theme.IVORY);
        }
        g.setStroke(new BasicStroke(1.2f));
        g.setColor(stroke);
        g.drawRoundRect(x, y, w, h, h, h);
        g.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 9f));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(stripe ? new Color(18, 20, 23, 235) : Theme.IVORY);
        g.drawString(text, x + (w - fm.stringWidth(text)) / 2, y + (h + fm.getAscent() - fm.getDescent()) / 2 - 1);
    }
}
