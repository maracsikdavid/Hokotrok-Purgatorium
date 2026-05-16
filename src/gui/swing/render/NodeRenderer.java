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

    public static final int BASE_NODE_WIDTH = 28;
    public static final int BASE_NODE_HEIGHT = 14;
    public static final int BASE_NODE_ARC = 14;

    /**
     * Kirajzol egy csomópont bejegyzést a megadott pozícióra.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó univerzális snapshot bejegyzés
     * @param position a csomópont pozíciója
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position) {
        render(graphics, node, position, BASE_NODE_WIDTH, BASE_NODE_HEIGHT);
    }

    /**
     * Kirajzol egy csomópontot dinamikus szélességgel.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó node
     * @param position középpont
     * @param requestedWidth kért szélesség (minimum BASE_NODE_WIDTH)
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position, int requestedWidth) {
        render(graphics, node, position, requestedWidth, BASE_NODE_HEIGHT);
    }

    /**
     * Kirajzol egy csomópontot dinamikus szélességgel és magassággal.
     *
     * @param graphics a rajzolási kontextus
     * @param node a kirajzolandó node
     * @param position középpont
     * @param requestedWidth kért szélesség (minimum BASE_NODE_WIDTH)
     * @param requestedHeight kért magasság (minimum BASE_NODE_HEIGHT)
     */
    public void render(Graphics2D graphics, GameSnapshot.Entry node, Point position, int requestedWidth, int requestedHeight) {
        if (position == null) return;

        int nodeWidth = Math.max(BASE_NODE_WIDTH, requestedWidth);
        int nodeHeight = Math.max(BASE_NODE_HEIGHT, requestedHeight);
        int arc = Math.max(BASE_NODE_ARC, Math.min(nodeWidth, nodeHeight)); // lekerekített végű "capsule"
        int left = position.x - (nodeWidth / 2);
        int top = position.y - (nodeHeight / 2);

        graphics.setColor(Color.BLACK);
        graphics.fillRoundRect(left, top, nodeWidth, nodeHeight, arc, arc);

        // Címke kirajzolása (ha van)
        String label = node.getLabel();
        if (label == null || label.isEmpty()) {
            label = node.getAttribute("label");
        }
        if (label != null && !label.isEmpty()) {
            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("SansSerif", Font.BOLD, 12));
            // A szöveget a node jobb felső része mellé toljuk
            graphics.drawString(label, position.x + (nodeWidth / 2) + 2, position.y - (nodeHeight / 2));
        }
    }
}
