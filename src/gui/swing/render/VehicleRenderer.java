package gui.swing.render;

import gui.snapshot.GameSnapshot;
import gui.swing.GameColors;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

/**
 * Járművek pozíciójának, típusának és állapotának kirajzolási váza.
 */
public class VehicleRenderer {
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 11);
    private static final float LABEL_BORDER_WIDTH = 0.5f;
    private static final Color LABEL_BORDER = Color.BLACK;

    /**
     * Kirajzol egy jármű bejegyzést a megadott pozícióra.
     *
     * @param graphics a rajzolási kontextus
     * @param vehicle a kirajzolandó univerzális snapshot bejegyzés
     * @param position a jármű pozíciója
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry vehicle, Point position) {
        render(graphics, vehicle, position, 0.0, false);
    }

    /**
     * Kirajzol egy járművet egy sáv két végpontja között, a modell progress értéke alapján interpolálva.
     *
     * @param graphics a rajzolási kontextus
     * @param vehicle a kirajzolandó jármű snapshot bejegyzése
     * @param start a sáv rajzolt kezdőpontja
     * @param end a sáv rajzolt végpontja
     * @param selected igaz, ha a jármű kijelölt állapotú
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry vehicle, Point start, Point end, boolean selected) {
        if (start == null || end == null) {
            return;
        }
        double ratio = resolveProgressRatio(vehicle);
        double x = start.x + (end.x - start.x) * ratio;
        double y = start.y + (end.y - start.y) * ratio;
        double angle = Math.atan2((double) end.y - start.y, (double) end.x - start.x);
        render(graphics, vehicle, new Point((int) Math.round(x), (int) Math.round(y)), angle, selected);
    }

    /**
     * Kirajzol egy járművet a haladási irányba forgatva.
     *
     * @param graphics a rajzolási kontextus
     * @param vehicle a kirajzolandó univerzális snapshot bejegyzés
     * @param position a jármű pozíciója
     * @param angleRadians a jármű irányszöge radiánban
     * @param selected igaz, ha a jármű kijelölt állapotú
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry vehicle, Point position, double angleRadians, boolean selected) {
        if (graphics == null || vehicle == null || position == null) {
            return;
        }

        AffineTransform originalTransform = graphics.getTransform();
        graphics.translate(position.x, position.y);
        graphics.rotate(angleRadians);

        String type = vehicle.getType() == null ? "" : vehicle.getType().toLowerCase();
        if (type.contains("bus")) {
            drawBus(graphics, selected);
        } else if (type.contains("snowplow")) {
            drawSnowplow(graphics, selected, GameColors.plowColor(vehicle.getAttribute("equippedPlowType")));
        } else {
            drawCar(graphics, selected);
        }

        if ("true".equalsIgnoreCase(vehicle.getAttribute("isParalyzed"))) {
            graphics.setColor(new Color(220, 38, 38));
            graphics.setStroke(new BasicStroke(2.0f));
            graphics.drawLine(-8, -8, 8, 8);
            graphics.drawLine(-8, 8, 8, -8);
        }

        graphics.setTransform(originalTransform);
        drawVehicleLabel(graphics, vehicle, position);
    }

    private void drawVehicleLabel(Graphics2D graphics, GameSnapshot.Entry vehicle, Point position) {
        String label = resolveVehicleLabel(vehicle);
        if (label == null || label.isBlank()) {
            return;
        }

        graphics.setFont(LABEL_FONT);
        FontMetrics metrics = graphics.getFontMetrics();
        float x = position.x - (metrics.stringWidth(label) / 2.0f);
        float y = position.y - 16.0f;
        drawOutlinedText(graphics, label, x, y, resolveVehicleLabelColor(vehicle));
    }

    private void drawOutlinedText(Graphics2D graphics, String text, float x, float y, Color fillColor) {
        GlyphVector glyphVector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);
        Shape textShape = glyphVector.getOutline(x, y);
        Stroke originalStroke = graphics.getStroke();
        Color originalColor = graphics.getColor();

        graphics.setColor(LABEL_BORDER);
        graphics.setStroke(new BasicStroke(LABEL_BORDER_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(textShape);
        graphics.setColor(fillColor == null ? Color.WHITE : fillColor);
        graphics.fill(textShape);

        graphics.setStroke(originalStroke);
        graphics.setColor(originalColor);
    }

    private String resolveVehicleLabel(GameSnapshot.Entry vehicle) {
        String type = vehicle.getType() == null ? "" : vehicle.getType().toLowerCase();
        if (type.contains("bus")) {
            return resolvePlayerName(vehicle, "Busz");
        }
        if (type.contains("snowplow")) {
            return resolvePlayerName(vehicle, "Hókotró");
        }
        return "Autó";
    }

    private String resolvePlayerName(GameSnapshot.Entry vehicle, String fallback) {
        String playerName = vehicle.getAttribute("playerName");
        return playerName == null || playerName.isBlank() ? fallback : playerName;
    }

    private Color resolveVehicleLabelColor(GameSnapshot.Entry vehicle) {
        String type = vehicle.getType() == null ? "" : vehicle.getType().toLowerCase();
        if (type.contains("bus")) {
            return GameColors.BUS;
        }
        if (type.contains("snowplow")) {
            return GameColors.SNOWPLOW;
        }
        return GameColors.CAR;
    }

    private void drawBus(Graphics2D graphics, boolean selected) {
        drawSelection(graphics, selected, -13, -8, 26, 16);
        graphics.setColor(GameColors.BUS);
        graphics.fillRoundRect(-13, -8, 26, 16, 5, 5);
        graphics.setColor(new Color(15, 23, 42));
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawRoundRect(-13, -8, 26, 16, 5, 5);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(-7, -5, 5, 4);
        graphics.fillRect(1, -5, 5, 4);
    }

    private void drawSnowplow(Graphics2D graphics, boolean selected, Color bladeColor) {
        drawSelection(graphics, selected, -15, -9, 30, 18);
        graphics.setColor(GameColors.SNOWPLOW);
        graphics.fillRoundRect(-8, -7, 21, 14, 4, 4);
        graphics.setColor(bladeColor == null ? GameColors.PLOW_SWEEPER : bladeColor);
        Polygon blade = new Polygon();
        blade.addPoint(-15, -9);
        blade.addPoint(-8, -6);
        blade.addPoint(-8, 6);
        blade.addPoint(-15, 9);
        graphics.fillPolygon(blade);
        graphics.setColor(new Color(15, 23, 42));
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawRoundRect(-8, -7, 21, 14, 4, 4);
        graphics.drawPolygon(blade);
    }

    private void drawCar(Graphics2D graphics, boolean selected) {
        drawSelection(graphics, selected, -10, -6, 20, 12);
        graphics.setColor(GameColors.CAR);
        graphics.fillRoundRect(-10, -6, 20, 12, 5, 5);
        graphics.setColor(new Color(15, 23, 42));
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawRoundRect(-10, -6, 20, 12, 5, 5);
    }

    private void drawSelection(Graphics2D graphics, boolean selected, int x, int y, int width, int height) {
        if (!selected) {
            return;
        }
        graphics.setColor(new Color(250, 204, 21));
        graphics.setStroke(new BasicStroke(3.0f));
        graphics.drawRoundRect(x - 3, y - 3, width + 6, height + 6, 7, 7);
    }

    private double resolveProgressRatio(GameSnapshot.Entry vehicle) {
        if (vehicle == null) {
            return 0.5;
        }
        double progress = parseDouble(vehicle.getAttribute("progress"), 0.0);
        double laneLength = parseDouble(vehicle.getAttribute("laneLength"), 0.0);
        if (laneLength <= 0.0) {
            return 0.5;
        }
        return clamp(progress / laneLength, 0.0, 1.0);
    }

    private double parseDouble(String value, double fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String trimmedValue = value.trim();
        if (!trimmedValue.matches("-?\\d+(\\.\\d+)?")) {
            return fallback;
        }
        return Double.parseDouble(trimmedValue);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}