package topology;
import core.Skeleton;
import entities.Car;
import entities.Bus;

import java.util.List;
import java.util.ArrayList;

/**
 * Absztrakt ősosztály minden térképi csomóponthoz. Kezeli a belőle kiinduló utakat.
 */
public abstract class MapNode {
    private List<Road> outgoingRoads = new ArrayList<>();


    // --- GETTEREK ÉS SETTEREK ---
    public List<Road> getOutgoingRoads() {
        Skeleton.printCall(null, this, "getOutgoingRoads");
        Skeleton.printReturn(this, "getOutgoingRoads", "List<Road>");
        return outgoingRoads;
    }
    public void setOutgoingRoads(List<Road> outgoingRoads) {
        this.outgoingRoads = outgoingRoads;
    }


    /// --- METÓDUSOK ---
    /**
     * Járművek irányítása a csomópontról. A csomópont eldönti,
     * hogy mely járművek mely kimenő úton haladnak tovább.
     */
    public void routeVehicles() {
        Skeleton.printCall(this, this, "routeVehicles");
        this.getOutgoingRoads();

        int valasz = Skeleton.getIntFromUser("Sikeres a sávváltás az új útra? (1: Igen, 0: Nem)");
        if (valasz == 1 && !outgoingRoads.isEmpty()) {
            if (Skeleton.getActiveTestCaseId()== 1 || Skeleton.getActiveTestCaseId()==2){
                Car c = new Car();
                Skeleton.registerObject(c, "c");

                Lane l1 = new Lane();
                Skeleton.registerObject(l1, "l1");
                l1.acceptVehicle(c);

                Road firstRoad = outgoingRoads.get(0);
                List<Lane> lanes = firstRoad.getLanes();
                Lane l2 = lanes.isEmpty() ? null : lanes.get(0);
                if (l2 != null) {
                    Skeleton.registerObject(l2, "l2");
                    c.changeLane(l2);
                }
            }
            if (Skeleton.getActiveTestCaseId()== 27 || Skeleton.getActiveTestCaseId()==28){
                Bus b = new Bus();
                Skeleton.registerObject(b, "b");

                Lane l1 = new Lane();
                Skeleton.registerObject(l1, "l1");
                l1.acceptVehicle(b);

                Road firstRoad = outgoingRoads.get(0);
                List<Lane> lanes = firstRoad.getLanes();
                Lane l2 = lanes.isEmpty() ? null : lanes.get(0);
                if (l2 != null) {
                    Skeleton.registerObject(l2, "l2");
                    b.changeLane(l2);
                }
            }

        }
        Skeleton.printReturn(this, "routeVehicles");
    }
    public void addOutgoingRoad(Road r){
        Skeleton.printCall(this, this, "addOutgoingRoad");
        outgoingRoads.add(r);
        Skeleton.printReturn(this, "addOutgoingRoad");
    }
}