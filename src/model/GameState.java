package model;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    private ArrayList<Ball> balls;

    public GameState() {
        balls = new ArrayList<>();

        Ball b1 = new Ball(700, 100, 30, 1, false, Color.PINK);
        b1.setVx(7);
        b1.setVy(7);

        Ball b2 = new Ball(600, 100, 30, 2, false, Color.WHITE);
        b2.setVx(5);
        b2.setVy(6);

        balls.add(b1);
        balls.add(b2);

    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }
}
