package model;

public class Player {
    private String name;
    private Ball.Group group;
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Ball.Group getGroup() { return group; }
    public void setGroup(Ball.Group group) { this.group = group; }
    public int getScore() { return score; }
    public void addScore() { score++; }
    public void reset() { group = null; score = 0; }
    public String groupText() { return group == null ? "Open" : group.toString(); }
}
