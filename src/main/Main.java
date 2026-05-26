package main;

import javax.swing.JFrame;

import controller.PhysicsEngine;
import model.GameState;
import view.GamePanel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("8-Ball Pool");

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();

        GamePanel gamePanel = new GamePanel(gameState, physicsEngine);

        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
