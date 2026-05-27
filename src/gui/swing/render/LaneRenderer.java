package gui.swing.render;

import gui.snapshot.GameSnapshot;
import gui.swing.GameColors;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Shape;

/**
 * Sávok és útviszonyok kirajzolásának renderelő váza.
 */
public class LaneRenderer {
    private static final String CONDITION_SUFFIX = "Condition";
    public static final int LANE_INNER_WIDTH = 3;
    public static final int LANE_OUTLINE_WIDTH = 5;


    /**
     * Kirajzol egy sáv bejegyzést két csomópontpozíció között.
     *
     * @param graphics a rajzolási kontextus
     * @param lane a kirajzolandó univerzális snapshot bejegyzés
     * @param start a sáv kezdőpontja
     * @param end a sáv végpontja
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry lane, Point start, Point end) {
        render(graphics, lane, start, end, false);
    }

    /**
     * Kirajzol egy sávot két pont között, opcionális kijelölési kiemeléssel.
     *
     * @param graphics a rajzolási kontextus
     * @param lane a kirajzolandó univerzális snapshot bejegyzés
     * @param start a sáv kezdőpontja
     * @param end a sáv végpontja
     * @param selected igaz, ha a sáv kijelölt állapotú
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry lane, Point start, Point end, boolean selected) {
        if (start == null || end == null) return;

        String condition = lane.getAttribute("condition");
        if (condition == null || condition.trim().isEmpty()) {
            condition = lane.getAttribute("state");
        }

        String normalizedCondition = normalizeCondition(condition);
        Color laneColor = GameColors.laneColor(normalizedCondition, isSaltedIce(lane));

        if (selected) {
            graphics.setColor(new Color(250, 204, 21));
            graphics.setStroke(new BasicStroke(9.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.drawLine(start.x, start.y, end.x, end.y);
        }

        // Körvonal kirajzolása (alulra, fekete színnel)
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(LANE_OUTLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine(start.x, start.y, end.x, end.y);

        // Belső sáv kirajzolása (felülre, a megfelelő színnel)
        graphics.setColor(laneColor);
        graphics.setStroke(new BasicStroke(LANE_INNER_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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
        render(graphics, lane, shape, false);
    }

    /**
     * Kirajzol egy sávot tetszőleges alakzattal, opcionális kijelölési kiemeléssel.
     *
     * @param graphics a rajzolási kontextus
     * @param lane a kirajzolandó univerzális snapshot bejegyzés
     * @param shape a kirajzolandó alakzat
     * @param selected igaz, ha a sáv kijelölt állapotú
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry lane, Shape shape, boolean selected) {
        if (shape == null) return;

        String condition = lane.getAttribute("condition");
        if (condition == null || condition.trim().isEmpty()) {
            condition = lane.getAttribute("state");
        }

        String normalizedCondition = normalizeCondition(condition);
        Color laneColor = GameColors.laneColor(normalizedCondition, isSaltedIce(lane));

        if (selected) {
            graphics.setColor(new Color(250, 204, 21));
            graphics.setStroke(new BasicStroke(9.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics.draw(shape);
        }

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(LANE_OUTLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(shape);

        graphics.setColor(laneColor);
        graphics.setStroke(new BasicStroke(LANE_INNER_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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

        if (value.endsWith(CONDITION_SUFFIX) && value.length() > CONDITION_SUFFIX.length()) {
            value = value.substring(0, value.length() - CONDITION_SUFFIX.length());
        }

        return value.toLowerCase();
    }

    private boolean isSaltedIce(GameSnapshot.Entry lane) {
        if (lane == null) {
            return false;
        }
        String saltTimer = lane.getAttribute("saltTimer");
        return saltTimer != null && !saltTimer.isBlank() && !"-1".equals(saltTimer);
    }
}