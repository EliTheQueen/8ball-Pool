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
        setResizable(true);
        setMinimumSize(new Dimension(1040, 760));
        root.add(new MainMenuPanel(this), "menu");
        root.add(createGameScreen(), "game");
        root.add(new SettingsPanel(gameState, this), "settings");
        root.add(new RecordsPanel(this), "records");
        add(root);
        pack();
        setLocationRelativeTo(null);
        showMenu();
    }

    private JPanel createGameScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CHARCOAL);
        panel.add(new GamePanel(gameState, new PhysicsEngine(gameState)), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttons.setBackground(Theme.GUNMETAL);
        buttons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(176, 141, 87, 95)),
                BorderFactory.createEmptyBorder(6, 0, 6, 0)
        ));

        JButton menu = Theme.button("Menu");
        menu.addActionListener(e -> showMenu());

        JButton restart = Theme.button("Restart");
        restart.addActionListener(e -> gameState.resetGame());

        JButton fullscreen =Theme.button("Fullscreen");
        fullscreen.addActionListener(e -> toggleFullscreen());

        JComboBox<GameState.Spin> spin = new JComboBox<>(GameState.Spin.values());
        spin.setFont(Theme.BODY_FONT);
        spin.setBackground(Theme.IVORY);
        spin.setForeground(Theme.CHARCOAL);
        spin.addActionListener(e -> gameState.setSpin((GameState.Spin) spin.getSelectedItem()));

        JButton cue = Theme.button("Change Cue");
        cue.addActionListener(e -> gameState.nextCue());

        buttons.add(menu);
        buttons.add(restart);
        buttons.add(fullscreen);
        buttons.add(Theme.label("Spin", 14, Font.BOLD));
        buttons.add(spin);
        buttons.add(cue);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void toggleFullscreen() {
        int state = getExtendedState();
        boolean maximized = (state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
        setExtendedState(maximized ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
    }

    public void showMenu() { gameState.resetGame(); cards.show(root, "menu"); }
    public void showGame() { cards.show(root, "game"); }
    public void showSettings() { cards.show(root, "settings"); }
    public void showRecords() { root.add(new RecordsPanel(this), "records"); cards.show(root, "records"); }
    public GameState getGameState() { return gameState; }
}
