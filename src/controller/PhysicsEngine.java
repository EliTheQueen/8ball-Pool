package controller;

import model.Ball;
import java.util.ArrayList;

public class PhysicsEngine {

    private void resolveCollision(Ball b1, Ball b2) {
        // ۱. محاسبه فاصله و بردار بین دو مرکز
        double dx = b2.getX() - b1.getX();
        double dy = b2.getY() - b1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // اگر توپ‌ها دقیقاً روی هم باشند یا برخوردی نباشد، خارج شو
        if (distance == 0 || distance > (b1.getRadius() + b2.getRadius())) return;

        // ۲. بردار نرمال واحد (Unit Normal Vector) - جهتی که ضربه وارد می‌شود
        double nx = dx / distance;
        double ny = dy / distance;

        // ۳. سرعت نسبی دو توپ
        double relativeVx = b1.getVx() - b2.getVx();
        double relativeVy = b1.getVy() - b2.getVy();

        // ۴. محاسبه سرعت در راستای بردار نرمال (Scalar Product)
        double velocityAlongNormal = (relativeVx * nx + relativeVy * ny);

        // ۵. بسیار مهم: اگر توپ‌ها در حال دور شدن هستند، عملیات را متوقف کن
        // این خط جلوی "چسبیدن" توپ‌ها به هم را می‌گیرد
        if (velocityAlongNormal < 0) return;

        // ۶. در بیلیارد فرض می‌کنیم جرم توپ‌ها برابر است (Mass1 = Mass2)
        // پس سرعت‌ها در راستای برخورد کاملاً عوض می‌شوند (Impulse)
        double impulse = velocityAlongNormal;

        // ۷. اعمال ضربه به سرعت‌ها
        b1.setVx(b1.getVx() - impulse * nx);
        b1.setVy(b1.getVy() - impulse * ny);
        b2.setVx(b2.getVx() + impulse * nx);
        b2.setVy(b2.getVy() + impulse * ny);

        // ۸. اصلاح موقعیت (Anti-clumping) برای اینکه در هم گیر نکنند
        // توپ‌ها را به اندازه هم‌پوشانی از هم دور می‌کنیم
        double overlap = (b1.getRadius() + b2.getRadius()) - distance;
        double percent = 0.5; // هر توپ نیمی از راه را برگردد
        double slop = 0.01; // مقدار ناچیز برای جلوگیری از لرزش
        double correctionMagnitude = Math.max(overlap - slop, 0.0) * percent;

        double correctionX = correctionMagnitude * nx;
        double correctionY = correctionMagnitude * ny;

        b1.setX(b1.getX() - correctionX);
        b1.setY(b1.getY() - correctionY);
        b2.setX(b2.getX() + correctionX);
        b2.setY(b2.getY() + correctionY);
    }

    public void updatePhysics(ArrayList<Ball> balls, int width, int height) {
        double friction = 0.99; // مقدار اصطکاک

        for (Ball b : balls) {
            // ۱. اعمال اصطکاک
            b.setVx(b.getVx() * friction);
            b.setVy(b.getVy() * friction);

            // ۲. توقف سرعت‌های ناچیز
            if (Math.abs(b.getVx()) < 0.1) b.setVx(0);
            if (Math.abs(b.getVy()) < 0.1) b.setVy(0);

            // ۳. حرکت
            b.setX(b.getX() + b.getVx());
            b.setY(b.getY() + b.getVy());

            // ۴. برخورد با دیوار
            b.checkWallCollision(width, height);
        }

        // ۵. برخورد توپ با توپ
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                if (balls.get(i).isCollidingWith(balls.get(j))) {
                    resolveCollision(balls.get(i), balls.get(j));
                }
            }
        }
    }


}
