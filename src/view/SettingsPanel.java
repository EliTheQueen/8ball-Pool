package view;

import model.GameState;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(GameState gameState, GameWindow window) {
        setLayout(new GridBagLayout());
        setOpaque(true);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(18,20,23,218));
        box.setBorder(Theme.cardBorder());

        JLabel title = Theme.label("Settings", 34, Font.BOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel hint = Theme.label("Player names, cue style and table options", 14, Font.PLAIN);
        hint.setForeground(Theme.BRASS);
        hint.setAlignmentX(CENTER_ALIGNMENT);

        JTextField p1 = Theme.textField(gameState.getPlayers()[0].getName());
        JTextField p2 = Theme.textField(gameState.getPlayers()[1].getName());
        JLabel p1Label = Theme.label("Player 1", 13, Font.BOLD);
        JLabel p2Label = Theme.label("Player 2", 13, Font.BOLD);
        JButton cue = Theme.button("Change Cue Style");
        cue.addActionListener(e -> gameState.nextCue());
        JButton save = Theme.button("Save Settings");
        save.addActionListener(e -> {
            gameState.setPlayerNames(p1.getText(), p2.getText());
            window.showMenu();
        });
        JButton back = Theme.button("Back to Menu");
        back.addActionListener(e -> window.showMenu());

        box.add(title);
        box.add(Box.createVerticalStrut(6));
        box.add(hint);
        box.add(Box.createVerticalStrut(28));
        box.add(p1Label);
        box.add(Box.createVerticalStrut(6));
        box.add(p1);
        box.add(Box.createVerticalStrut(14));
        box.add(p2Label);
        box.add(Box.createVerticalStrut(6));
        box.add(p2);
        box.add(Box.createVerticalStrut(24));
        box.add(cue);
        box.add(Box.createVerticalStrut(12));
        box.add(save);
        box.add(Box.createVerticalStrut(12));
        box.add(back);
        add(box);

    }
}
