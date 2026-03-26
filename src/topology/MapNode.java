package topology;
import core.Skeleton;
import java.util.List;
import java.util.ArrayList;

/**
 * Absztrakt ősosztály minden térképi csomóponthoz. Kezeli a belőle kiinduló utakat.
 */
public abstract class MapNode {
    /**
     * A csomópontból kiinduló utak listája.
     */
    protected List<Road> outgoingRoads = new ArrayList<>();

    /**
     * Járművek irányítása a csomópontról. A csomópont eldönti,
     * hogy mely járművek mely kimenő úton haladnak tovább.
     */
    public void routeVehicles() {
        Skeleton.printCall(null, this, "routeVehicles");
        Skeleton.printReturn(this, "routeVehicles");
    }

    /**
     * A csomópontból induló összes út lekérése.
     *
     * @return a kimenő utak listája
     */
    public List<Road> getOutgoingRoads() {
        Skeleton.printCall(null, this, "getOutgoingRoads");
        Skeleton.printReturn(this, "getOutgoingRoads", "List<Road>");
        return outgoingRoads;
    }
}