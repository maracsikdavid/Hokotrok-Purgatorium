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
     * Lekéri a térképen található összes kapcsolódási pontot és csomópontot.
     *
     * @return A csomópontok listája.
     */
    public List<MapNode> getConnections() {
        return nodes;
    }

    /**
    * Az objektum állapotának és a hozzá tartozó csomópontok adatainak kiírása a standard kimenetre.
    *
    * @param id Az objektum azonosítója a regiszterben.
    * @param registry A központi objektumtár az azonosítók feloldásához.
    */
    public void printData(String id, cli.ObjectRegistry registry) {
        System.out.println("Map," + id);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(registry.findId(nodes.get(i)));
        }
        sb.append("]");
        System.out.println("mapNodes," + sb.toString());
    }}
