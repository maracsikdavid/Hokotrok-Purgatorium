package topology;
import core.Skeleton;
import entities.Car;

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
        if(valasz == 1){
            Car c = new Car();
            Skeleton.registerObject(c, "c");
            
            Lane next = new Lane();
            Skeleton.registerObject(next, "next");
            Skeleton.printCall(this, c, "changeLane");
            
            c.changeLane(next);
            
            Skeleton.printReturn(c, "changeLane", "true");
        }
        Skeleton.printReturn(this, "routeVehicles");
    }
    public void addOutgoingRoad(Road r){

    }
}