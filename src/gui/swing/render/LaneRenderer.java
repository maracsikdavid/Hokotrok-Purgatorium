package gui.swing.render;

import gui.snapshot.GameSnapshot;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;

/**
 * Sávok és útviszonyok kirajzolásának renderelő váza.
 */
public class LaneRenderer {

    /**
     * Kirajzol egy sáv bejegyzést két csomópontpozíció között.
     *
     * @param graphics a rajzolási kontextus
     * @param lane a kirajzolandó univerzális snapshot bejegyzés
     * @param start a sáv kezdőpontja
     * @param end a sáv végpontja
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry lane, Point start, Point end) {
        if (start == null || end == null) return;

        String condition = lane.getAttribute("condition");
        if (condition == null || condition.trim().isEmpty()) {
            condition = lane.getAttribute("state");
        }

        String normalizedCondition = normalizeCondition(condition);
        Color laneColor = resolveLaneColor(normalizedCondition);

        // Körvonal kirajzolása (alulra, fekete színnel)
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(start.x, start.y, end.x, end.y);

        // Belső sáv kirajzolása (felülre, a megfelelő színnel)
        graphics.setColor(laneColor);
        graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(start.x, start.y, end.x, end.y);

        // Vonal körvonalának visszaállítása
        graphics.setStroke(new BasicStroke(1.0f));
    }

    /**
     * Kirajzol egy sávot tetszőleges alakzattal (pl. görbe Bezier útvonallal).
     *
     * @param graphics a rajzolási kontextus
     * @param lane a kirajzolandó univerzális snapshot bejegyzés
     * @param shape a kirajzolandó alakzat
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry lane, Shape shape) {
        if (shape == null) return;

        String condition = lane.getAttribute("condition");
        if (condition == null || condition.trim().isEmpty()) {
            condition = lane.getAttribute("state");
        }

        String normalizedCondition = normalizeCondition(condition);
        Color laneColor = resolveLaneColor(normalizedCondition);

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(shape);

        graphics.setColor(laneColor);
        graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(shape);

        graphics.setStroke(new BasicStroke(1.0f));
    }

    private String normalizeCondition(String conditionRaw) {
        if (conditionRaw == null) {
            return "";
        }

        String value = conditionRaw.trim();
        if (value.isEmpty()) {
            return "";
        }

        int lastDot = value.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < value.length() - 1) {
            value = value.substring(lastDot + 1);
        }

        if (value.endsWith("Condition") && value.length() > "Condition".length()) {
            value = value.substring(0, value.length() - "Condition".length());
        }

        return value.toLowerCase();
    }

    private Color resolveLaneColor(String normalizedCondition) {
        switch (normalizedCondition) {
            case "thicksnow":
                return Color.WHITE; // Vastag hó
            case "thinsnow":
                return Color.LIGHT_GRAY; // Vékony hó
            case "ice":
                return Color.CYAN; // Jég
            case "graveledice":
                return new Color(200, 180, 140); // Kavicsos jég
            case "clean":
            default:
                return Color.DARK_GRAY; // Letakarított, aszfalt
        }
    }
}