package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(GameWindow window) {
        setLayout(new GridBagLayout()); setBackground(new Color(18, 90, 45));
        JPanel box = new JPanel(); box.setOpaque(false); box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("8-Ball Pool"); title.setFont(new Font("Arial", Font.BOLD, 42)); title.setForeground(Color.WHITE); title.setAlignmentX(CENTER_ALIGNMENT);
        box.add(title); box.add(Box.createVerticalStrut(30));
        box.add(button("Start Game", e -> window.showGame()));
        box.add(button("Settings", e -> window.showSettings()));
        box.add(button("Records", e -> window.showRecords()));
        box.add(button("Exit", e -> System.exit(0)));
        add(box);
    }
    private JButton button(String text, java.awt.event.ActionListener al) { JButton b = new JButton(text); b.setAlignmentX(CENTER_ALIGNMENT); b.setMaximumSize(new Dimension(220, 45)); b.addActionListener(al); return b; }
}
