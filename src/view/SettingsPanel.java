package view;

import model.GameState;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private GameState gameState;

    public SettingsPanel(GameState gameState) {
        this.gameState = gameState;

        // تنظیمات ظاهری پنل
        this.setPreferredSize(new Dimension(200, 300));
        this.setBackground(new Color(50, 50, 50, 220)); // خاکستری تیره نیمه‌شفاف
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // چیدمان عمودی
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // عنوان منو
        JLabel title = new JLabel("Settings");
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 20))); // فاصله

        // دکمه تعویض چوب
        JButton changeCueBtn = createStyledButton("Change Cue");
        changeCueBtn.addActionListener(e -> {
            gameState.nextCue();
            // اینجا باید راهی برای آپدیت کردن متن دکمه پیدا کنیم
        });
        this.add(changeCueBtn);

        this.add(Box.createRigidArea(new Dimension(0, 10)));

        // دکمه شروع مجدد (بعدا کدش را می‌زنیم)
        JButton restartBtn = createStyledButton("Restart Game");
        this.add(restartBtn);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(150, 40));
        btn.setFocusable(false);
        // اینجا می‌توانید هر استایلی که دوست دارید بدهید
        return btn;
    }
}
