package topology;

import java.util.List;
import java.util.ArrayList;

/**
 * Absztrakt ősosztály minden térképi csomóponthoz. Kezeli a belőle kiinduló utakat.
 */
public abstract class MapNode {
    private List<Road> outgoingRoads = new ArrayList<>();

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

    // --- METÓDUSOK ---
    /**
     * Járművek irányítása a csomópontról. A csomópont eldönti,
     * hogy mely járművek mely kimenő úton haladnak tovább.
     */
    public void routeVehicles() {

    }

    /**
     * Új út hozzáadása a csomóponthoz. Ez a metódus felelős azért, hogy a csomópont tudja, milyen utak vezetnek ki belőle.
     * 
     * @param r az új út, amely a csomópontból indul ki.
     */
    public void addOutgoingRoad(Road r){
    }
}