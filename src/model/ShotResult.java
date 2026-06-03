package model;

import java.util.EnumSet;

public class ShotResult {
    private Ball firstHitBall;
    private boolean railHit;
    private final EnumSet<Ball.Group> cueContactGroups = EnumSet.noneOf(Ball.Group.class);

    public Ball getFirstHitBall() {
        return firstHitBall;
    }

    public void registerCueContact(Ball ball) {
        if (ball == null || ball.getGroup() == Ball.Group.CUE) return;
        if (this.firstHitBall == null) {
            this.firstHitBall = ball;
        }
        cueContactGroups.add(ball.getGroup());
    }

    public boolean hasCueContactWith(Ball.Group group) {
        return group != null && cueContactGroups.contains(group);
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
        this.cueContactGroups.clear();
    }
}
