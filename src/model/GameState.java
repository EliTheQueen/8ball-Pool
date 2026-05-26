package model;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    private ArrayList<Ball> balls;
    private Cue currentCue;
    private ArrayList<Cue> availableCues = new ArrayList<>();
    private int currentCueIndex = 0;

    public GameState() {
        balls = new ArrayList<>();

        Ball b1 = new Ball(700, 100, 30, 1, true, Color.WHITE);

        Ball b2 = new Ball(600, 100, 30, 2, true, Color.pink);

        balls.add(b1);
        balls.add(b2);

        availableCues.add(new BasicCue());
        availableCues.add(new PowerCue());

        this.currentCue = availableCues.get(0);
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

    public void nextCue() {
        currentCueIndex = (currentCueIndex + 1) % availableCues.size();
        this.currentCue = availableCues.get(currentCueIndex);
        System.out.println("Cue changed to: " + currentCue.getName());
    }

}
