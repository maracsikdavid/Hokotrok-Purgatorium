package gui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

final class AppIcon {
    private static final Color BACKGROUND = new Color(205, 205, 205);
    private static final Color OUTLINE = new Color(48, 62, 66);
    private static final Color BUS_BLUE = new Color(109, 201, 235);
    private static final Color BUS_WINDOW = new Color(113, 135, 137);
    private static final Color PLOW_ORANGE = new Color(255, 143, 35);
    private static final Color WHEEL = new Color(107, 124, 127);

    private AppIcon() {
    }

    static List<Image> createIconImages() {
        int[] sizes = {16, 24, 32, 48, 64, 128, 256};
        List<Image> images = new ArrayList<>();
        for (int size : sizes) {
            images.add(createIcon(size));
        }
        return images;
    }

    private static BufferedImage createIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.scale(size / 256.0, size / 256.0);

        drawBackground(graphics);
        drawBus(graphics);
        drawSnowplowCab(graphics);
        drawBlade(graphics);
        drawWheels(graphics);

        graphics.dispose();
        return image;
    }

    private static void drawBackground(Graphics2D graphics) {
        graphics.setColor(BACKGROUND);
        graphics.fill(new RoundRectangle2D.Double(5, 5, 246, 246, 42, 42));
    }

    private static void drawBus(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.setColor(BUS_BLUE);
        graphics.fill(new RoundRectangle2D.Double(112, 68, 124, 100, 18, 18));
        graphics.setColor(OUTLINE);
        graphics.draw(new RoundRectangle2D.Double(112, 68, 124, 100, 18, 18));

        drawWindow(graphics, 134, 88, 28, 36);
        drawWindow(graphics, 168, 88, 28, 36);
        drawWindow(graphics, 202, 88, 24, 36);

        graphics.setColor(OUTLINE);
        graphics.setStroke(new BasicStroke(11f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(134, 82, 134, 168);
        graphics.drawLine(129, 168, 222, 168);
    }

    private static void drawSnowplowCab(Graphics2D graphics) {
        Path2D cab = new Path2D.Double();
        cab.moveTo(66, 148);
        cab.lineTo(76, 88);
        cab.lineTo(116, 88);
        cab.lineTo(118, 148);
        cab.closePath();
        graphics.setColor(PLOW_ORANGE);
        graphics.fill(cab);
        graphics.setColor(OUTLINE);
        graphics.setStroke(new BasicStroke(11f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(cab);

        Path2D windshield = new Path2D.Double();
        windshield.moveTo(84, 95);
        windshield.lineTo(110, 95);
        windshield.lineTo(107, 126);
        windshield.lineTo(76, 126);
        windshield.closePath();
        graphics.setColor(BUS_WINDOW);
        graphics.fill(windshield);
        graphics.setColor(OUTLINE);
        graphics.draw(windshield);

        graphics.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(64, 149, 126, 149);
        graphics.drawLine(62, 126, 68, 94);
    }

    private static void drawBlade(Graphics2D graphics) {
        Path2D blade = new Path2D.Double();
        blade.moveTo(24, 140);
        blade.lineTo(87, 140);
        blade.curveTo(103, 150, 103, 198, 88, 206);
        blade.lineTo(20, 206);
        blade.curveTo(32, 187, 35, 160, 24, 140);
        blade.closePath();

        graphics.setColor(PLOW_ORANGE);
        graphics.fill(blade);
        graphics.setColor(OUTLINE);
        graphics.setStroke(new BasicStroke(11f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(blade);
        graphics.drawLine(96, 143, 105, 198);
    }

    private static void drawWheels(Graphics2D graphics) {
        drawWheel(graphics, 127, 173, 19);
        drawWheel(graphics, 184, 171, 23);
    }

    private static void drawWheel(Graphics2D graphics, int centerX, int centerY, int radius) {
        graphics.setColor(OUTLINE);
        graphics.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        graphics.setColor(WHEEL);
        graphics.fillOval(centerX - radius + 7, centerY - radius + 7, (radius - 7) * 2, (radius - 7) * 2);
        graphics.setColor(BACKGROUND);
        graphics.fillOval(centerX - 7, centerY - 7, 14, 14);
    }

    private static void drawWindow(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(BUS_WINDOW);
        graphics.fillRoundRect(x, y, width, height, 5, 5);
        graphics.setColor(OUTLINE);
        graphics.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawRoundRect(x, y, width, height, 5, 5);
    }
}