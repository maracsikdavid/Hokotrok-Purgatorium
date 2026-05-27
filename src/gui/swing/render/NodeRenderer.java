package gui.swing.render;

import gui.snapshot.GameSnapshot;
import gui.swing.GameColors;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;

/**
 * Csomópontok és címkéik kirajzolásának renderelő váza.
 */
public class NodeRenderer {
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final float LABEL_BORDER_WIDTH = 0.5f;

    public static final int BASE_NODE_DIAMETER = 24;

    /**
     * Kirajzol egy csomópont bejegyzést a megadott pozícióra.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó univerzális snapshot bejegyzés
     * @param position a csomópont pozíciója
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position) {
        render(graphics, node, position, BASE_NODE_DIAMETER);
    }

    /**
     * Kirajzol egy csomópontot dinamikus szélességgel.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó node
     * @param position középpont
     * @param requestedWidth kért szélesség (minimum BASE_NODE_WIDTH)
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position, int requestedDiameter) {
        render(graphics, node, position, requestedDiameter, null, false);
    }

    /**
     * Kirajzol egy csomópontot layout címkével és opcionális kijelöléssel.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó node
     * @param position középpont
     * @param requestedDiameter kért átmérő
     * @param layoutLabel a layout fájlból olvasott címke
     * @param selected igaz, ha kijelölt csomópont
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position, int requestedDiameter,
                       String layoutLabel, boolean selected) {
        if (position == null) return;

        int nodeDiameter = Math.max(BASE_NODE_DIAMETER, requestedDiameter);
        int left = position.x - (nodeDiameter / 2);
        int top = position.y - (nodeDiameter / 2);

        graphics.setColor(resolveNodeColor(node));
        graphics.fillOval(left, top, nodeDiameter, nodeDiameter);
        if (selected) {
            graphics.setColor(new Color(250, 204, 21));
            graphics.setStroke(new BasicStroke(3.0f));
            graphics.drawOval(left - 3, top - 3, nodeDiameter + 6, nodeDiameter + 6);
            graphics.setStroke(new BasicStroke(1.0f));
        }

        // Címke kirajzolása (ha van)
        String label = layoutLabel;
        if (label == null || label.isEmpty()) {
            label = node.getLabel();
        }
        if (label == null || label.isEmpty()) {
            label = node.getAttribute("label");
        }
        if (label != null && !label.isEmpty()) {
            drawNodeLabel(graphics, node, label, position, nodeDiameter);
        }
    }

    private void drawNodeLabel(Graphics2D graphics, GameSnapshot.Entry node, String label, Point position, int nodeDiameter) {
        graphics.setFont(LABEL_FONT);
        FontMetrics metrics = graphics.getFontMetrics();
        float x = position.x + (nodeDiameter / 2.0f) + 2.0f;
        float y = position.y - (nodeDiameter / 2.0f);
        GlyphVector glyphVector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), label);
        Shape textShape = glyphVector.getOutline(x, y);
        Stroke originalStroke = graphics.getStroke();
        Color originalColor = graphics.getColor();

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(LABEL_BORDER_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(textShape);
        graphics.setColor(resolveNodeLabelColor(node));
        graphics.fill(textShape);

        graphics.setStroke(originalStroke);
        graphics.setColor(originalColor);
    }

    private Color resolveNodeColor(GameSnapshot.Entry node) {
        if (node != null && "true".equalsIgnoreCase(node.getAttribute("isBusStop"))) {
            return GameColors.BUS;
        }
        String nodeType = node == null ? null : node.getType();
        String normalizedType = nodeType == null ? "" : nodeType.toLowerCase();
        if (normalizedType.contains("depot")) {
            return GameColors.SNOWPLOW;
        }
        if (normalizedType.contains("building")) {
            return GameColors.CAR;
        }
        return GameColors.INTERSECTION;
    }

    private Color resolveNodeLabelColor(GameSnapshot.Entry node) {
        Color nodeColor = resolveNodeColor(node);
        if (GameColors.INTERSECTION.equals(nodeColor)) {
            return Color.WHITE;
        }
        return nodeColor;
    }
}
