package controller;

import model.Ball;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CueController extends MouseAdapter {

    private Ball cueBall;
    private Point mousePoint = new Point();
    private boolean isDragging = false;
    private double power = 0;
    private double angle = 0;

    public CueController(Ball ball) {
        this.cueBall = ball;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isDragging){
            hitBall();
            isDragging = false;
            power = 0;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePoint = e.getPoint();
        calculateAngleAndPower();
    }

    private void calculateAngleAndPower() {
        double dx = mousePoint.x - cueBall.getX();
        double dy = mousePoint.y - cueBall.getY();

        // محاسبه زاویه بین موس و توپ
        angle = Math.atan2(dy, dx);

        // محاسبه قدرت (فاصله موس تا توپ)
        double distance = Math.sqrt(dx * dx + dy * dy);
        power = Math.min(distance / 10, 20);
    }

    private void hitBall() {
        // ضربه در جهت مخالف کشش موس
        double dx = mousePoint.x - cueBall.getX();
        double dy = mousePoint.y - cueBall.getY();

        // محاسبه زاویه ضربه (برعکس جهت کشیدن موس)
        double angle = Math.atan2(dy, dx);

        // اعمال سرعت به توپ سفید
        // ضربدر یک عدد بزرگتر (مثلا 0.2) کنید تا حرکت به وضوح دیده شود
        cueBall.setVx(-Math.cos(angle) * power * 0.2);
        cueBall.setVy(-Math.sin(angle) * power * 0.2);
    }

    // Getter ها برای استفاده در GamePanel (برای رسم چوب)
    public double getAngle() { return angle; }
    public double getPower() { return power; }
    public boolean isDragging() { return isDragging; }


}
