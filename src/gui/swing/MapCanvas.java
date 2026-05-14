package gui.swing;

import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import gui.swing.render.MapRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * A pálya vizuális kirajzolásáért felelős Swing komponens.
 * A rajzolást a snapshot, a layout és a kijelölési állapot alapján a MapRenderer felé delegálja.
 */
public class MapCanvas extends JPanel {
    private GameSnapshot snapshot;
    private MapLayout layout;
    private SelectionState selectionState;
    private MapRenderer mapRenderer = new MapRenderer();

    /**
     * Beállítja a rajzolandó játékállapotot.
     *
     * @param snapshot az új snapshot
     */
    public void setSnapshot(GameSnapshot snapshot) {
        this.snapshot = snapshot;
        repaint();
    }

    /**
     * Beállítja a rajzoláshoz használt layoutot.
     *
     * @param layout az új layout
     */
    public void setLayout(MapLayout layout) {
        this.layout = layout;
        repaint();
    }

    /**
     * Beállítja az aktuális kijelölési állapotot.
     *
     * @param selectionState az új kijelölési állapot
     */
    public void setSelectionState(SelectionState selectionState) {
        this.selectionState = selectionState;
        repaint();
    }

    /**
     * Beállítja a térképet kirajzoló renderelő objektumot.
     *
     * @param mapRenderer az új renderelő
     */
    public void setMapRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    /**
     * A Swing rajzolási ciklus belépési pontja.
     *
     * @param graphics a rajzolási kontextus
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (mapRenderer != null && graphics instanceof Graphics2D) {
            mapRenderer.render((Graphics2D) graphics, snapshot, layout, selectionState);
        }
    }
}