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

    // --- GETTEREK ÉS SETTEREK ---
    public List<MapNode> getNodes() {
        return nodes;
    }

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
}
