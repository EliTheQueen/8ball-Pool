package model;

import java.awt.*;
import java.io.*;
import java.util.*;

public class GameState {
    public enum Spin { NONE, TOP, BACK, LEFT, RIGHT }

    private final ArrayList<Ball> balls = new ArrayList<>();
    private final ArrayList<Cue> availableCues = new ArrayList<>();
    private final Player[] players = { new Player("Player 1"), new Player("Player 2") };
    private final Table table = new Table();
    private Cue currentCue;
    private int currentCueIndex = 0, currentPlayer = 0, selectedPocket = -1;
    private Spin spin = Spin.NONE;
    private boolean shotInProgress = false, foul = false, ballInHand = false, gameOver = false;
    private String message = "Select a pocket, then drag from the white ball.";
    private final ArrayList<Ball> pottedThisShot = new ArrayList<>();
    private boolean openTable = true;
    private final ShotResult shotResult = new ShotResult();

    public GameState() {
        availableCues.add(new BasicCue()); availableCues.add(new PowerCue());
        currentCue = availableCues.get(0);
        resetGame();
    }

    public void resetGame() {
        balls.clear(); pottedThisShot.clear();
        for (Player p : players) p.reset();
        currentPlayer = new Random().nextInt(2); selectedPocket = -1; spin = Spin.NONE;
        shotInProgress = foul = ballInHand = gameOver = false;
        message = players[currentPlayer].getName() + " starts. Select a pocket.";
        createBalls();
        setOpenTable(true);
    }

    private void createBalls() {
        double r = 13;
        balls.add(new Ball(220, Table.HEIGHT / 2.0, r, 0, Ball.Group.CUE, Color.WHITE));
        int[][] nums = {{1},{9,2},{3,8,10},{11,4,12,5},{6,13,7,14,15}};
        double startX = 620, startY = Table.HEIGHT / 2.0, gap = r * 2 + 2;
        for (int row = 0; row < nums.length; row++) {
            for (int col = 0; col < nums[row].length; col++) {
                int n = nums[row][col];
                double x = startX + row * gap * 0.88;
                double y = startY + (col - row / 2.0) * gap;
                balls.add(new Ball(x, y, r, n, groupFor(n), colorFor(n)));
            }
        }
    }

    private Ball.Group groupFor(int n) { if (n == 8) return Ball.Group.EIGHT; return n < 8 ? Ball.Group.SOLID : Ball.Group.STRIPE; }
    private Color colorFor(int n) {
        Color[] c = {Color.WHITE, Color.YELLOW, Color.BLUE, Color.RED, new Color(90,0,120), Color.ORANGE, Color.GREEN, new Color(120,20,20), Color.BLACK,
                Color.YELLOW, Color.BLUE, Color.RED, new Color(90,0,120), Color.ORANGE, Color.GREEN, new Color(120,20,20)};
        return c[n];
    }

    public void startShot() {
        shotInProgress = true;
        foul = false;
        pottedThisShot.clear();
        message = "Balls moving...";
        shotResult.reset();
    }

    public void finishShotIfStopped() {
        if (!shotInProgress || !isEverythingStopped()) return;
        shotInProgress = false;
        applyShotRules();
        selectedPocket = -1;
    }

    private void applyShotRules() {
        Player p = players[currentPlayer];
        boolean pottedOwn = false;
        for (Ball b : pottedThisShot) {
            if (b.getGroup() == Ball.Group.CUE) foul = true;
            else if (b.getGroup() == Ball.Group.EIGHT) handleEightBall(b);
            else {
                if (players[0].getGroup() == null && players[1].getGroup() == null) assignGroups(b.getGroup());
                if (b.getGroup() == p.getGroup()) { p.addScore(); pottedOwn = true; }
            }
        }
        if (gameOver) return;
        if (foul) { ballInHand = true; switchTurn(); message = "FOUL! " + players[currentPlayer].getName() + " has ball in hand."; }
        else if (!pottedOwn) { switchTurn(); message = players[currentPlayer].getName() + " turn. Select a pocket."; }
        else message = p.getName() + " continues. Select a pocket.";
    }

    private void assignGroups(Ball.Group firstGroup) {
        players[currentPlayer].setGroup(firstGroup);
        players[1 - currentPlayer].setGroup(firstGroup == Ball.Group.SOLID ? Ball.Group.STRIPE : Ball.Group.SOLID);
        setOpenTable(false);
    }

    private void handleEightBall(Ball eight) {
        Player p = players[currentPlayer];
        boolean allDone = remainingOfGroup(p.getGroup()) == 0;
        if (allDone && selectedPocket >= 0) { gameOver = true; message = p.getName() + " wins!"; saveRecord(p.getName() + " won"); }
        else { gameOver = true; message = p.getName() + " loses by potting 8 early!"; saveRecord(players[1-currentPlayer].getName() + " won"); }
    }

    public int remainingOfGroup(Ball.Group group) {
        if (group == null) return 7;
        int count = 0; for (Ball b : balls) if (!b.isPotted() && b.getGroup() == group) count++;
        return count;
    }

    public void potBall(Ball b) {
        if (b.isPotted()) return;
        b.setPotted(true); b.stop(); pottedThisShot.add(b);
        if (b.getGroup() == Ball.Group.CUE) { b.setX(220); b.setY(Table.HEIGHT / 2.0); b.setPotted(false); ballInHand = true; foul = true; }
    }

    public void switchTurn() { currentPlayer = 1 - currentPlayer; }
    public boolean isEverythingStopped() { for (Ball b : balls) if (!b.isPotted() && b.isMoving()) return false; return true; }
    public Ball getCueBall() { return balls.get(0); }
    public ArrayList<Ball> getBalls() { return balls; }
    public Table getTable() { return table; }
    public Player getCurrentPlayer() { return players[currentPlayer]; }
    public Player[] getPlayers() { return players; }
    public Cue getCurrentCue() { return currentCue; }
    public int getSelectedPocket() { return selectedPocket; }
    public void setSelectedPocket(int i) { selectedPocket = i; message = "Pocket " + (i + 1) + " selected. Aim and shoot."; }
    public Spin getSpin() { return spin; }
    public void setSpin(Spin spin) { this.spin = spin; }
    public boolean isBallInHand() { return ballInHand; }
    public boolean isGameOver() { return gameOver; }
    public String getMessage() { return message; }
    public void setPlayerNames(String a, String b) { players[0].setName(a.isBlank()?"Player 1":a); players[1].setName(b.isBlank()?"Player 2":b); }
    public void clearBallInHand() { ballInHand = false; }
    public void nextCue() { currentCueIndex = (currentCueIndex + 1) % availableCues.size(); currentCue = availableCues.get(currentCueIndex); }

    private void saveRecord(String text) {
        try (FileWriter fw = new FileWriter("records.txt", true)) { fw.write(new Date() + " - " + text + "\n"); } catch (IOException ignored) {}
    }
    public static String loadRecords() {
        try { return new String(java.nio.file.Files.readAllBytes(new File("records.txt").toPath())); } catch (IOException e) { return "No records yet."; }
    }

    public boolean isOpenTable() {
        return openTable;
    }

    public void setOpenTable(boolean openTable) {
        this.openTable = openTable;
    }

    public ShotResult getShotResult() {
        return shotResult;
    }
}
