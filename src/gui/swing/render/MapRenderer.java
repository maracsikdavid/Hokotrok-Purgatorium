package gui.swing.render;

import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import gui.swing.SelectionState;
import java.awt.Graphics2D;

/**
 * A teljes pálya kirajzolását koordináló renderelő váza.
 * A konkrét rajzolási részfeladatokat a csomópont-, sáv- és járműrenderelők kapják meg.
 */
public class MapRenderer {
    private final NodeRenderer nodeRenderer = new NodeRenderer();
    private final LaneRenderer laneRenderer = new LaneRenderer();
    private final VehicleRenderer vehicleRenderer = new VehicleRenderer();

    /**
     * Kirajzolja a teljes térképet az univerzális snapshot és layout alapján.
     *
     * @param graphics a rajzolási kontextus
     * @param snapshot az aktuális játékállapot pillanatképe
     * @param layout a rajzolási koordinátákat tartalmazó layout
     * @param selectionState az aktuális kijelölési állapot
     */
    public void render(Graphics2D graphics, GameSnapshot snapshot, MapLayout layout, SelectionState selectionState) {
    }

    /**
     * Visszaadja a csomópont-renderelőt.
     *
     * @return a node renderer
     */
    public NodeRenderer getNodeRenderer() {
        return nodeRenderer;
    }

    /**
     * Visszaadja a sáv-renderelőt.
     *
     * @return a lane renderer
     */
    public LaneRenderer getLaneRenderer() {
        return laneRenderer;
    }

    /**
     * Visszaadja a jármű-renderelőt.
     *
     * @return a vehicle renderer
     */
    public VehicleRenderer getVehicleRenderer() {
        return vehicleRenderer;
    }
}