package core;

import java.util.ArrayList;
import java.util.List;
import topology.MapNode;

/**
 * A játék térképét reprezentáló osztály.
 * A térkép csomópontokból (MapNode) áll, amelyek az úthálózat szerkezetét adják.
 */
public class Map {
    private List<MapNode> nodes = new ArrayList<>();


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public Map() {
    }

    /**
     * Paraméteres konstruktor a csomópontlista megadásához.
     *
     * @param nodes a térkép csomópontjai
     */
    public Map(List<MapNode> nodes) {
        this.nodes = nodes;
    }

    
    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a térkép csomópontjainak listáját.
     *
     * @return a csomópontok listája
     */
    public List<MapNode> getNodes() {
        return nodes;
    }

    /**
     * Beállítja a térkép csomópontjainak listáját.
     *
     * @param nodes a beállítandó csomópontlista
     */
    public void setNodes(List<MapNode> nodes) {
        this.nodes = nodes;
    }


    // --- METÓDUSOK ---

    /**
     * Lekéri a térképen található kapcsolódási pontokat.
     *
     * @return a kapcsolódó csomópontok listája
     */
    public List<MapNode> getConnections() {
        return null;
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
