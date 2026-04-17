package topology;

import entities.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt ősosztály minden térképi csomóponthoz. Kezeli a belőle kiinduló utakat.
 */
public abstract class MapNode {
    private List<Road> outgoingRoads = new ArrayList<>();


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    protected MapNode() {
    }

    /**
     * Paraméteres konstruktor a kimenő utak listájával.
     *
     * @param outgoingRoads a csomópontból kivezető utak
     */
    protected MapNode(List<Road> outgoingRoads) {
        this.outgoingRoads = outgoingRoads;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a csomópontból kivezető utak listáját.
     *
     * @return a kimenő utak listája
     */
    public List<Road> getOutgoingRoads() {
        return outgoingRoads;
    }

    /**
     * Beállítja a csomópontból kivezető utak listáját.
     *
     * @param outgoingRoads a beállítandó útlista
     */
    public void setOutgoingRoads(List<Road> outgoingRoads) {
        this.outgoingRoads = outgoingRoads;
    }

    
    // --- METÓDUSOK ---

    /**
     * Egy beérkezett jármű irányítása a csomópontból. A csomópont megkérdezi a járművet,
     * hogy melyik kimenő úton haladjon tovább.
     * * @param v a továbbirányítandó jármű
     */
    public void routeVehicle(Vehicle v) {
        if (v == null) return;

        // Megkérdezzük a járművet a következő útról (a Car itt használja a cache-t)
        Road nextRoad = v.chooseNextRoad(this);

        if (nextRoad != null && !nextRoad.getLanes().isEmpty()) {
            // Fizikai áthelyezés az új út első sávjára
            Lane targetLane = nextRoad.getLanes().get(0);
            v.setCurrentLane(targetLane);
            v.setProgress(0);
            
            // Regisztráljuk a járművet az új sávon
            targetLane.acceptVehicle(v);
        }
        // Ha null, a jármű (pl. Busz vagy Hókotró) várakozik a csomópontban a parancsig.
    }


    /**
     * Új út hozzáadása a csomóponthoz. Ez a metódus felelős azért, hogy a csomópont tudja, milyen utak vezetnek ki belőle.
     * 
     * @param r az új út, amely a csomópontból indul ki.
     */
    public void addOutgoingRoad(Road r){
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}