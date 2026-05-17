package gui.swing.render;

import gui.snapshot.GameSnapshot;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;

/**
 * Csomópontok és címkéik kirajzolásának renderelő váza.
 */
public class NodeRenderer {

    public static final int BASE_NODE_DIAMETER = 28;

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
        if (position == null) return;

        int nodeDiameter = Math.max(BASE_NODE_DIAMETER, requestedDiameter);
        int left = position.x - (nodeDiameter / 2);
        int top = position.y - (nodeDiameter / 2);

        graphics.setColor(Color.BLACK);
        graphics.fillOval(left, top, nodeDiameter, nodeDiameter);

        // Címke kirajzolása (ha van)
        String label = node.getLabel();
        if (label == null || label.isEmpty()) {
            label = node.getAttribute("label");
        }
        if (label != null && !label.isEmpty()) {
            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("SansSerif", Font.BOLD, 12));
            // A szöveget a node jobb felső része mellé toljuk
            graphics.drawString(label, position.x + (nodeDiameter / 2) + 2, position.y - (nodeDiameter / 2));
        }
    }
}
