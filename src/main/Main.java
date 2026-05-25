package main;

import javax.swing.JFrame;
import view.GamePanel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("8-Ball Pool");

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();

        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
