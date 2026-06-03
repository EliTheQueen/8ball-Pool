package controller;

import javax.sound.sampled.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SoundManager {
    private static final float SAMPLE_RATE = 44100f;
    private static final ExecutorService AUDIO = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "pool-sound-engine");
        t.setDaemon(true);
        return t;
        });
    private static long lastBallSound = 0;
    private static long lastRailSound = 0;
    private static long lastPocketSound = 0;
    private static long lastFoulSound = 0;
    private static long lastCueSound = 0;
    private static long lastHandSound = 0;

    private SoundManager() {}

    public static void playCueStrike(double power) {
        long now = System.currentTimeMillis();
        if (now - lastCueSound < 90) {
            return;
        }
        lastCueSound = now;
        double volume = clamp(power / 46, 0.13, 0.48);
        AUDIO.submit(() -> {
            playTone(210, 130, 42, volume, 0.22, 9);
            sleep(14);
            playTone(88, 72, 55, volume * 0.34, 0.12, 6.5);
        });
    }

    public static void playBallCollision(double strength) {
        long now = System.currentTimeMillis();
        if (now - lastBallSound < 55) return;
        lastBallSound = now;
        double volume = clamp(strength / 25.0, 0.07, 0.50);
        AUDIO.submit(() -> {
            playTone(245, 180, 48, volume, 0.055, 8.8);
            playTone(540, 390, 24, volume * 0.18, 0.035, 11.5);
        });
    }

    public static void playRailCollision(double strength) {
        long now = System.currentTimeMillis();
        if (now - lastRailSound < 85) return;
        lastRailSound = now;
        double volume = clamp(strength / 20.0, 0.07, 0.38);
        AUDIO.submit(() -> {
            playTone(124, 88, 82, volume, 0.16, 6.8);
            playTone(74, 62, 55, volume * 0.24, 0.18, 5.5);
        });
    }

    public static void playPocketDrop() {
        long now = System.currentTimeMillis();
        if (now - lastPocketSound < 120) return;
        lastPocketSound = now;
        AUDIO.submit(() -> {
            playTone(132, 84, 120, 0.34, 0.26, 5.2);
            sleep(70);
            playTone(68, 46, 190, 0.32, 0.24, 4.6);
            sleep(38);
            playTone(42, 36, 110, 0.18, 0.35, 5.8);
        });
    }

    public static void playFoul() {
        long now = System.currentTimeMillis();
        if (now - lastFoulSound < 500) return;
        lastFoulSound = now;
        AUDIO.submit(() -> {
            playTone(493.88, 392.00, 180, 0.18, 0.012, 2.8);
            sleep(28);
            playTone(349.23, 261.63, 240, 0.16, 0.012, 3.0);
        });
    }

    public static void playBallInHand() {
        long now = System.currentTimeMillis();
        if (now - lastHandSound < 550) return;
        lastHandSound = now;
        AUDIO.submit(() -> {
            playTone(261.63, 329.63, 130, 0.15, 0.01, 2.6);
            sleep(24);
            playTone(392.00, 523.25, 190, 0.17, 0.01, 2.3);
        });
    }

    public static void playGameOver(boolean win) {
        AUDIO.submit(() -> {
            if (win) {
                playTone(261.63, 261.63, 120, 0.20, 0.006, 2.6);
                sleep(32); playTone(329.63, 329.63, 130, 0.21, 0.006, 2.5);
                sleep(32); playTone(392.00, 523.25, 230, 0.22, 0.006, 2.4);
                sleep(46); playTone(659.25, 783.99, 260, 0.18, 0.004, 2.5);
            } else {
                playTone(392.00, 349.23, 170, 0.18, 0.006, 2.9);
                sleep(34); playTone(329.63, 293.66, 210, 0.17, 0.006, 3.0);
                sleep(34); playTone(261.63, 220.00, 260, 0.15, 0.006, 3.2);
            }
        });
    }

    private static void playTone(double startHz, double endHz, int millis, double volume, double noiseMix, double decay) {
        try {
            byte[] data = synth(startHz, endHz, millis, volume, noiseMix, decay);
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format, data.length);
            line.start();
            line.write(data, 0, data.length);
            line.drain();
            line.stop();
            line.close();
        } catch (Exception ignored) {}
    }

    private static byte[] synth(double startHz, double endHz, int millis, double volume, double noiseMix, double decay) {
        int samples = (int) (SAMPLE_RATE * millis / 1000.0);
        byte[] out = new byte[samples * 2];
        double phase = 0;
        long seed = 0x8BA11L;
        for (int i = 0; i < samples; i++) {
            double t = i / (double) samples;
            double hz = startHz + (endHz - startHz) * t;
            phase += 2.0 * Math.PI * hz / SAMPLE_RATE;
            seed = seed * 1664525L + 1013904223L;
            double noise = (((seed >>> 16) & 0xffff) / 32768.0) - 1.0;
            double sine = Math.sin(phase);
            double warm = 0.78 * sine + 0.14 * Math.sin(phase * 0.5) + 0.08 * Math.sin(phase * 1.25);
            double body = warm * (1.0 - noiseMix) + noise * noiseMix;
            double attack = Math.min(1.0, t * 28.0);
            double envelope = attack * Math.exp(-decay * t) * (1.0 - 0.18 * t);
            short sample = (short) (Math.max(-1, Math.min(1, body * envelope * volume)) * Short.MAX_VALUE);
            out[i * 2] = (byte) (sample & 0xff);
            out[i * 2 + 1] = (byte) ((sample >> 8) & 0xff);
        }
        return out;
    }

    private static double clamp(double v, double min, double max) { return Math.max(min, Math.min(max, v)); }
    private static void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); } }
}


