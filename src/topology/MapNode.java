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

        // 1. Megkérdezzük a járművet, hova akar menni
        Road nextRoad = v.chooseNextRoad(this);

        // 2. Ha van kiválasztott út (pl. NPC autó), áthelyezzük
        if (nextRoad != null && nextRoad.getLanes() != null && !nextRoad.getLanes().isEmpty()) {
            Lane startingLane = nextRoad.getLanes().get(0); // Általában a legkülső sáv
            v.setCurrentLane(startingLane);
            v.setProgress(0);
            
            // Itt érdemes lehet majd a sávba is beregisztrálni:
            // startingLane.acceptVehicle(v);
        }
        // 3. Ha null (Busz/Hókotró), a jármű várakozik a csomópontban.
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