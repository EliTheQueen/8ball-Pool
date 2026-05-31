package model;

public class ShotResult {
    private Ball firstHitBall;
    private boolean railHit;

    public Ball getFirstHitBall() {
        return firstHitBall;
    }

    public void setFirstHitBall(Ball firstHitBall) {
        if (this.firstHitBall == null) {
            this.firstHitBall = firstHitBall;
        }
    }

    public boolean isRailHit() {
        return railHit;
    }

    public void setRailHit(boolean railHit) {
        this.railHit = railHit;
    }

    public void reset() {
        this.firstHitBall = null;
        this.railHit = false;
    }
}
