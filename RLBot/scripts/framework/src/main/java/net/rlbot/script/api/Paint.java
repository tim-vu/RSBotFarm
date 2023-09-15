package net.rlbot.script.api;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class Paint {

    private static final Font FONT = new Font("Arial", Font.BOLD, 13);

    private static final int WIDTH_OFFSET = 8;

    private static final int HEIGHT_OFFSET = 35;

    private static final int HEIGHT_SEPARATOR = 16;


    public static void drawStrings(Graphics graphics, List<String> lines) {

        Graphics2D g = (Graphics2D) graphics;

        g.setFont(FONT);
        g.setColor(Color.GREEN);

        int y = HEIGHT_OFFSET;

        for (String line : lines) {
            g.drawString(line, WIDTH_OFFSET, y);
            y += HEIGHT_SEPARATOR;
        }
    }

    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;

    public static String formatRuntime(Duration runtime) {
        long hours = runtime.getSeconds() / SECONDS_IN_HOUR;
        long minutes = (runtime.getSeconds() % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        long seconds = (runtime.getSeconds() % SECONDS_IN_MINUTE);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
