package gui.application;

import cli.ObjectRegistry;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;
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
        List<String> reachableLaneIds = new ArrayList<>();
        
        try {
            Object obj = registry.getObject(vehicleId);
            if (!(obj instanceof Vehicle)) {
                return reachableLaneIds;
            }
            
            Vehicle vehicle = (Vehicle) obj;
            Lane currentLane = vehicle.getCurrentLane();
            
            if (currentLane == null || currentLane.getRoad() == null) {
                return reachableLaneIds; // Nincs úton, nem mehet sehova
            }
            
            // 1. Melyik csomópontba érkezik be az aktuális sáv?
            MapNode targetNode = currentLane.getRoad().getTargetNode();
            if (targetNode == null) {
                return reachableLaneIds; // Zsákutca
            }
            
            // 2. Melyik utak indulnak ki ebből a csomópontból?
            List<Road> outgoingRoads = targetNode.getOutgoingRoads();
            if (outgoingRoads != null) {
                // 3. Összegyűjtjük ezen utak összes sávját
                for (Road road : outgoingRoads) {
                    if (road.getLanes() != null) {
                        for (Lane lane : road.getLanes()) {
                            String laneId = registry.findId(lane);
                            if (laneId != null && !laneId.equals("?")) {
                                reachableLaneIds.add(laneId);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Hiba a célpontok számításakor: " + e.getMessage());
        }
        
        return reachableLaneIds;
    }
}
