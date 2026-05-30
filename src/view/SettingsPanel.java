package view;

import model.GameState;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel(GameState gameState, GameWindow window) {
        setLayout(new GridBagLayout()); setBackground(new Color(45,45,45));
        JPanel box = new JPanel(); box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS)); box.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        JTextField p1 = new JTextField("Player 1", 15), p2 = new JTextField("Player 2", 15);
        JLabel title = new JLabel("Settings"); title.setFont(new Font("Arial", Font.BOLD, 28)); title.setAlignmentX(CENTER_ALIGNMENT);
        JButton save = new JButton("Save"); save.setAlignmentX(CENTER_ALIGNMENT); save.addActionListener(e -> { gameState.setPlayerNames(p1.getText(), p2.getText()); window.showMenu(); });
        JButton cue = new JButton("Change Cue"); cue.setAlignmentX(CENTER_ALIGNMENT); cue.addActionListener(e -> gameState.nextCue());
        JButton back = new JButton("Back"); back.setAlignmentX(CENTER_ALIGNMENT); back.addActionListener(e -> window.showMenu());
        box.add(title); box.add(new JLabel("Player 1:")); box.add(p1); box.add(new JLabel("Player 2:")); box.add(p2); box.add(Box.createVerticalStrut(12)); box.add(cue); box.add(save); box.add(back);
        add(box);
    }
}
