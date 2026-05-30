package view;

import model.GameState;
import javax.swing.*;
import java.awt.*;

public class RecordsPanel extends JPanel {
    public RecordsPanel(GameWindow window) {
        setLayout(new BorderLayout());
        JTextArea area = new JTextArea(GameState.loadRecords()); area.setEditable(false);
        JLabel title = new JLabel("Records", SwingConstants.CENTER); title.setFont(new Font("Arial", Font.BOLD, 28));
        JButton back = new JButton("Back"); back.addActionListener(e -> window.showMenu());
        add(title, BorderLayout.NORTH); add(new JScrollPane(area), BorderLayout.CENTER); add(back, BorderLayout.SOUTH);
    }
}
