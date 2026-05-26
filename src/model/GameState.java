package model;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    private ArrayList<Ball> balls;
    private Cue currentCue;

    public GameState() {
        balls = new ArrayList<>();

        Ball b1 = new Ball(700, 100, 30, 1, true, Color.WHITE);

        Ball b2 = new Ball(600, 100, 30, 2, true, Color.pink);

        balls.add(b1);
        balls.add(b2);

        currentCue = new BasicCue();

    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public Cue getCurrentCue() {
        return currentCue;
    }

    public void setCurrentCue(Cue currentCue) {
        this.currentCue = currentCue;
    }

    public boolean isEverythingStopped() {
        double stopThreshold = 0.1;
        for (Ball ball : balls) {

            double speed = Math.sqrt(ball.getVx() * ball.getVx() + ball.getVy() * ball.getVy());
            if (speed > stopThreshold) {
                return false;
            }
        }
        return true;
    }

}
