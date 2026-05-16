package gui.snapshot;

import actors.Player;
import cli.ObjectRegistry;
import core.Game;
import core.GameElement;
import core.GameElementVisitor;
import entities.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * A modell aktuális állapotából egyetlen univerzális {@link GameSnapshot} objektumot készítő factory.
 */
public class GameSnapshotFactory {

    /**
     * Elkészíti a játék aktuális GUI-pillanatképét.
     *
     * @param game a fő játékmodell
     * @param registry a központi objektumtár
     * @return a GUI számára átadható univerzális snapshot
     */
    public GameSnapshot createSnapshot(Game game, ObjectRegistry registry) {
        int tickCount = game == null ? 0 : game.getTickCount();
        String currentPlayerId = null;
        if (game != null && registry != null) {
            Player player = game.getCurrentPlayer(registry);
            currentPlayerId = registry.findId(player);
        }

        List<GameSnapshot.Entry> entries = new ArrayList<>();

        if (registry != null) {
            SnapshotVisitor visitor = new SnapshotVisitor(registry, entries);
            
            // 1. Típusbiztos lekérés a regisztrációs tárból
            // NINCS TÍPUSELLENŐRZÉS (instanceof)!
            Map<String, GameElement> gameElements = registry.getMapByType(GameElement.class);
            for (Map.Entry<String, GameElement> mapEntry : gameElements.entrySet()) {
                mapEntry.getValue().accept(visitor, mapEntry.getKey());
            }
            
            // 2. Egyéb objektumok (Pl. Wallet, Game, Shop) amikre a generikus leolvasás nem vonatkozott
            for (Map.Entry<String, Object> mapEntry : registry.getObjects().entrySet()) {
                String id = mapEntry.getKey();
                if (!gameElements.containsKey(id)) {
                    Object obj = mapEntry.getValue();
                    Map<String, String> attributes = new HashMap<>();
                    entries.add(new GameSnapshot.Entry(id, "other", obj.getClass().getSimpleName(), id, attributes));
                }
            }
        }

        return new GameSnapshot(tickCount, currentPlayerId, entries, new ArrayList<>());
    }

    private static class SnapshotVisitor implements GameElementVisitor {
        private final ObjectRegistry registry;
        private final List<GameSnapshot.Entry> entries;

        public SnapshotVisitor(ObjectRegistry registry, List<GameSnapshot.Entry> entries) {
            this.registry = registry;
            this.entries = entries;
        }

        @Override
        public void visit(Vehicle vehicle, String id) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("progress", String.valueOf(vehicle.getProgress()));
            attributes.put("isParalyzed", String.valueOf(vehicle.getIsParalyzed()));
            attributes.put("paralysisTimer", String.valueOf(vehicle.getParalysisTimer()));
            
            String laneId = registry.findId(vehicle.getCurrentLane());
            if (laneId != null) attributes.put("currentLaneId", laneId);
            
            String targetLaneId = registry.findId(vehicle.getTargetLane());
            if (targetLaneId != null) attributes.put("targetLaneId", targetLaneId);
            
            entries.add(new GameSnapshot.Entry(id, "vehicle", vehicle.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Lane lane, String id) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("length", String.valueOf(lane.getLength()));
            
            if (lane.getState() != null) {
                attributes.put("condition", lane.getState().getClass().getSimpleName());
            }
            
            String roadId = registry.findId(lane.getRoad());
            if (roadId != null) attributes.put("roadId", roadId);
            
            entries.add(new GameSnapshot.Entry(id, "lane", lane.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Road road, String id) {
            Map<String, String> attributes = new HashMap<>();
            String targetNodeId = registry.findId(road.getTargetNode());
            if (targetNodeId != null) attributes.put("targetNodeId", targetNodeId);
            
            entries.add(new GameSnapshot.Entry(id, "road", road.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(MapNode node, String id) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("isBusStop", String.valueOf(node.isBusStop()));
            
            entries.add(new GameSnapshot.Entry(id, "node", node.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Player player, String id) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("name", player.getName());
            
            entries.add(new GameSnapshot.Entry(id, "player", player.getClass().getSimpleName(), player.getName(), attributes));
        }
    }
}
