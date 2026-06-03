package view;

import model.GameState;
import javax.swing.*;
import java.awt.*;

public class RecordsPanel extends JPanel {
    public RecordsPanel(GameWindow window) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(28, 34, 28, 34));
        JTextArea area = new JTextArea(GameState.loadRecords());
        area.setEditable(false);
        area.setFont(Theme.BODY_FONT.deriveFont(Font.PLAIN, 13f));
        area.setForeground(Theme.IVORY);
        area.setBackground(new Color(18,20,23));
        area.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        JLabel title = new JLabel("Records", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.IVORY);
        JButton back = Theme.button("Back to Menu");
        back.addActionListener(e -> window.showMenu());
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(back);
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.paintGradient(g2, getWidth(), getHeight());
        Theme.paintLineForBeauty(g2, getWidth(), getHeight());
        g2.dispose();
    }
}
