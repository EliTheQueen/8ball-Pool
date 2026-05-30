package view;

import controller.PhysicsEngine;
import model.GameState;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final GameState gameState = new GameState();

    public GameWindow() {
        super("8-Ball Pool");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        root.add(new MainMenuPanel(this), "menu");
        root.add(createGameScreen(), "game");
        root.add(new SettingsPanel(gameState, this), "settings");
        root.add(new RecordsPanel(this), "records");
        add(root); pack(); setLocationRelativeTo(null);
        showMenu();
    }

    private JPanel createGameScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new GamePanel(gameState, new PhysicsEngine()), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton menu = new JButton("Menu"); menu.addActionListener(e -> showMenu());
        JButton restart = new JButton("Restart"); restart.addActionListener(e -> gameState.resetGame());
        JComboBox<GameState.Spin> spin = new JComboBox<>(GameState.Spin.values()); spin.addActionListener(e -> gameState.setSpin((GameState.Spin) spin.getSelectedItem()));
        JButton cue = new JButton("Change Cue"); cue.addActionListener(e -> gameState.nextCue());
        buttons.add(menu); buttons.add(restart); buttons.add(new JLabel("Spin:")); buttons.add(spin); buttons.add(cue);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    public void showMenu() { cards.show(root, "menu"); }
    public void showGame() { cards.show(root, "game"); }
    public void showSettings() { cards.show(root, "settings"); }
    public void showRecords() { root.add(new RecordsPanel(this), "records"); cards.show(root, "records"); }
    public GameState getGameState() { return gameState; }
}
