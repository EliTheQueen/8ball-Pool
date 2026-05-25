package controller;

import model.Ball;
import java.util.ArrayList;

public class PhysicsEngine {

    private static final double FRICTION = 0.99; // ضریب اصطکاک
    private static final double STOP_THRESHOLD = 0.1; // آستانه توقف

    public void updatePhysics(ArrayList<Ball> balls, int width, int height) {
        // ۱. حرکت و اصطکاک و برخورد با دیوار
        for (Ball b : balls) {
            applyFriction(b);
            moveBall(b);
            checkWallCollision(b, width, height);
        }

        // ۲. برخورد توپ با توپ
        handleBallCollisions(balls);
    }

    private void applyFriction(Ball b) {
        b.setVx(b.getVx() * FRICTION);
        b.setVy(b.getVy() * FRICTION);

        if (Math.abs(b.getVx()) < STOP_THRESHOLD) b.setVx(0);
        if (Math.abs(b.getVy()) < STOP_THRESHOLD) b.setVy(0);
    }

    private void moveBall(Ball b) {
        b.setX(b.getX() + b.getVx());
        b.setY(b.getY() + b.getVy());
    }

    private void checkWallCollision(Ball b, int width, int height) {
        double r = b.getRadius();
        if (b.getX() - r < 0) {
            b.setX(r);
            b.setVx(-b.getVx());
        } else if (b.getX() + r > width) {
            b.setX(width - r);
            b.setVx(-b.getVx());
        }

        if (b.getY() - r < 0) {
            b.setY(r);
            b.setVy(-b.getVy());
        } else if (b.getY() + r > height) {
            b.setY(height - r);
            b.setVy(-b.getVy());
        }
    }

    private void handleBallCollisions(ArrayList<Ball> balls) {
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                Ball b1 = balls.get(i);
                Ball b2 = balls.get(j);
                if (isColliding(b1, b2)) {
                    resolveCollision(b1, b2);
                }
            }
        }
    }

    private boolean isColliding(Ball b1, Ball b2) {
        double dx = b1.getX() - b2.getX();
        double dy = b1.getY() - b2.getY();
        double distSq = dx * dx + dy * dy;
        double rSum = b1.getRadius() + b2.getRadius();
        return distSq <= rSum * rSum;
    }

    private void resolveCollision(Ball b1, Ball b2) {
        // ۱. محاسبه فاصله
        double dx = b2.getX() - b1.getX();
        double dy = b2.getY() - b1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // جلوگیری از تقسیم بر صفر
        if (distance == 0) return;

        // ۲. بردار نرمال واحد
        double nx = dx / distance;
        double ny = dy / distance;

        // ۳. سرعت نسبی
        double relVx = b1.getVx() - b2.getVx();
        double relVy = b1.getVy() - b2.getVy();

        // ۴. سرعت در راستای برخورد
        double velAlongNormal = relVx * nx + relVy * ny;

        // ۵. اگر در حال دور شدن هستند، کاری نکن (حیاتی برای جلوگیری از چسبیدن)
        if (velAlongNormal < 0) return;

        // ۶. اعمال ضربه (Impulse) - فرض بر جرم مساوی
        b1.setVx(b1.getVx() - velAlongNormal * nx);
        b1.setVy(b1.getVy() - velAlongNormal * ny);
        b2.setVx(b2.getVx() + velAlongNormal * nx);
        b2.setVy(b2.getVy() + velAlongNormal * ny);

        // ۷. اصلاح هم‌پوشانی (Overlap Correction) - برای اینکه در هم گیر نکنند
        double overlap = (b1.getRadius() + b2.getRadius()) - distance;
        double percent = 0.5;
        double slop = 0.01;
        double correctionMagnitude = Math.max(overlap - slop, 0.0) * percent;

        double correctionX = correctionMagnitude * nx;
        double correctionY = correctionMagnitude * ny;

        b1.setX(b1.getX() - correctionX);
        b1.setY(b1.getY() - correctionY);
        b2.setX(b2.getX() + correctionX);
        b2.setY(b2.getY() + correctionY);
    }

}
