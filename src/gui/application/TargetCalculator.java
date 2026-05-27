package gui.application;

import cli.ObjectRegistry;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * Számolja ki az egy adott jármű számára elérhető célpontokat (Sávokat/Lane-eket)
 * a következő lépésben, a csomóponti sávváltás elve alapján.
 */
public class TargetCalculator {

    /**
     * Visszaadja azon Sávok (Lane) ID-jait, ahova a megadott jármű a következő lépésben rákanyarodhat.
     * Mivel sávváltás csak csomópontban (Node) történhet, ha a jármű úton van,
     * akkor a sávnak be kell futnia egy csomópontba, majd annak kimenő útjainak sávjai az elérhetőek.
     *
     * @param vehicleId a vizsgált jármű ID-ja
     * @param registry az aktuális objektumtár
     * @return az elérhető cél sávok azonosítóinak (ID) listája
     */
    public List<String> getReachableLaneIds(String vehicleId, ObjectRegistry registry) {
        if (vehicleId == null || vehicleId.isBlank() || registry == null) {
            return new ArrayList<>();
        }

        Vehicle vehicle = registry.getMapByType(Vehicle.class).get(vehicleId.trim());
        MapNode currentNode = resolveCurrentNode(vehicle, registry);
        if (currentNode == null || currentNode.getOutgoingRoads() == null) {
            return new ArrayList<>();
        }

        Set<String> reachableLaneIds = new LinkedHashSet<>();
        for (Road road : currentNode.getOutgoingRoads()) {
            if (road == null || road.getLanes() == null) {
                continue;
            }
            for (Lane lane : road.getLanes()) {
                String laneId = registry.findId(lane);
                if (isUsableId(laneId)) {
                    reachableLaneIds.add(laneId);
                }
            }
        }

        return new ArrayList<>(reachableLaneIds);
    }

    private MapNode resolveCurrentNode(Vehicle vehicle, ObjectRegistry registry) {
        if (vehicle == null) {
            return null;
        }
        Lane currentLane = vehicle.getCurrentLane();
        if (currentLane == null || currentLane.getRoad() == null) {
            return null;
        }

        int progress = vehicle.getProgress();
        if (progress >= currentLane.getLength()) {
            return currentLane.getRoad().getTargetNode();
        }

        if (progress > 0) {
            return null;
        }

        return findRoadSourceNode(currentLane.getRoad(), registry);
    }

    private MapNode findRoadSourceNode(Road road, ObjectRegistry registry) {
        if (road == null || registry == null) {
            return null;
        }

        for (MapNode node : registry.getMapByType(MapNode.class).values()) {
            if (node != null && node.getOutgoingRoads() != null && node.getOutgoingRoads().contains(road)) {
                return node;
            }
        }
        return null;
    }

    private boolean isUsableId(String id) {
        return id != null && !id.isBlank() && !"?".equals(id) && !"null".equals(id);
    }
}
