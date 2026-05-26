package model;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    private ArrayList<Ball> balls;
    private Cue currentCue = new Cue("Classic", new Color(139, 69, 19), 10);

    public GameState() {
        balls = new ArrayList<>();

        Ball b1 = new Ball(700, 100, 30, 1, true, Color.WHITE);

        Ball b2 = new Ball(600, 100, 30, 2, true, Color.pink);

        balls.add(b1);
        balls.add(b2);

    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public Cue getCurrentCue() {
        return currentCue;
    }
}
