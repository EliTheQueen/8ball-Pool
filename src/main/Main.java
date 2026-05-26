package main;

import javax.swing.*;

import controller.PhysicsEngine;
import model.GameState;
import view.GamePanel;
import view.SettingsPanel;

public class Main {
    public static void main(String[] args) {
        JLayeredPane layeredPane = new JLayeredPane();
        JFrame frame = new JFrame("8-Ball Pool");
        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();
        GamePanel gamePanel = new GamePanel(gameState, physicsEngine);

        gamePanel.setBounds(0, 0, 800, 600);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        SettingsPanel settingsPanel = new SettingsPanel(gameState);
        settingsPanel.setBounds(300, 150, 200, 300);
        settingsPanel.setVisible(false);
        layeredPane.add(settingsPanel, JLayeredPane.PALETTE_LAYER);

        frame.add(layeredPane);
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
