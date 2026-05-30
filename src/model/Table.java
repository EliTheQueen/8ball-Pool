package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 560;
    public static final int RAIL = 45;
    public static final int POCKET_RADIUS = 30;

    public Rectangle playArea() { return new Rectangle(RAIL, RAIL, WIDTH - 2 * RAIL, HEIGHT - 2 * RAIL); }

    public List<Point> pockets() {
        Rectangle r = playArea();
        ArrayList<Point> p = new ArrayList<>();
        p.add(new Point(r.x, r.y));
        p.add(new Point(r.x + r.width / 2, r.y));
        p.add(new Point(r.x + r.width, r.y));
        p.add(new Point(r.x, r.y + r.height));
        p.add(new Point(r.x + r.width / 2, r.y + r.height));
        p.add(new Point(r.x + r.width, r.y + r.height));
        return p;
    }

    public int pocketAt(Point mouse) {
        List<Point> ps = pockets();
        for (int i = 0; i < ps.size(); i++) if (mouse.distance(ps.get(i)) <= POCKET_RADIUS + 12) return i;
        return -1;
    }
}
