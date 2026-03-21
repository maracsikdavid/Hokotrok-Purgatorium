package topology;
import core.Skeleton;
import java.util.List;
import java.util.ArrayList;

public abstract class MapNode {
    protected List<Road> outgoingRoads = new ArrayList<>();

    public void routeVehicles() {
        Skeleton.printCall(null, this, "routeVehicles");
        Skeleton.printReturn(this, "routeVehicles");
    }

    public List<Road> getOutgoingRoads() {
        Skeleton.printCall(null, this, "getOutgoingRoads");
        Skeleton.printReturn(this, "getOutgoingRoads", "List<Road>");
        return outgoingRoads;
    }
}