package gui.snapshot;

import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import cli.ObjectRegistry;
import core.Game;
import core.GameElement;
import core.GameElementVisitor;
import core.Shop;
import core.ShopItem;
import core.Wallet;
import entities.Bus;
import entities.Snowplow;
import entities.Vehicle;
import equipments.Consumable;
import equipments.Plow;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import statemachine.IceCondition;
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
        Player player = null;
        String currentPlayerId = null;
        if (game != null && registry != null) {
            player = game.getCurrentPlayer(registry);
            currentPlayerId = registry.findId(player);
        }

        List<GameSnapshot.Entry> entries = new ArrayList<>();

        if (registry != null) {
            SnapshotVisitor visitor = new SnapshotVisitor(registry, entries);
            
            // 1. Típusbiztos lekérés a regisztrációs tárból
            // NINCS DIREKT TÍPUSELLENŐRZÉS!
            Map<String, GameElement> gameElements = registry.getMapByType(GameElement.class);
            for (Map.Entry<String, GameElement> mapEntry : gameElements.entrySet()) {
                mapEntry.getValue().accept(visitor, mapEntry.getKey());
            }
            
            addNonGameElementEntries(registry, gameElements, entries);
        }

        return new GameSnapshot(tickCount, currentPlayerId, entries, createMessages(tickCount, player, registry));
    }

    private void addNonGameElementEntries(ObjectRegistry registry, Map<String, GameElement> gameElements,
                                          List<GameSnapshot.Entry> entries) {
        Map<String, Game> games = registry.getMapByType(Game.class);
        Map<String, Wallet> wallets = registry.getMapByType(Wallet.class);
        Map<String, Shop> shops = registry.getMapByType(Shop.class);
        Map<String, Plow> plows = registry.getMapByType(Plow.class);
        Map<String, Consumable> consumables = registry.getMapByType(Consumable.class);

        for (Map.Entry<String, Object> mapEntry : registry.getObjects().entrySet()) {
            String id = mapEntry.getKey();
            if (gameElements.containsKey(id)) {
                continue;
            }

            if (games.containsKey(id)) {
                entries.add(createGameEntry(id, games.get(id), registry));
            } else if (wallets.containsKey(id)) {
                entries.add(createWalletEntry(id, wallets.get(id)));
            } else if (shops.containsKey(id)) {
                entries.add(createShopEntry(id, shops.get(id)));
            } else if (plows.containsKey(id)) {
                entries.add(createPlowEntry(id, plows.get(id), registry));
            } else if (consumables.containsKey(id)) {
                entries.add(createConsumableEntry(id, consumables.get(id)));
            } else {
                Object object = mapEntry.getValue();
                Map<String, String> attributes = new LinkedHashMap<>();
                entries.add(new GameSnapshot.Entry(id, "other", object.getClass().getSimpleName(), id, attributes));
            }
        }
    }

    private GameSnapshot.Entry createGameEntry(String id, Game game, ObjectRegistry registry) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("tickCount", String.valueOf(game.getTickCount()));
        putIds(attributes, "playerIds", game.getPlayers(), registry);
        putId(attributes, "shopId", game.getShop(), registry);
        putId(attributes, "currentPlayerId", game.getCurrentPlayer(registry), registry);
        return new GameSnapshot.Entry(id, "game", game.getClass().getSimpleName(), id, attributes);
    }

    private GameSnapshot.Entry createWalletEntry(String id, Wallet wallet) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("amount", String.valueOf(wallet.getAmount()));
        return new GameSnapshot.Entry(id, "wallet", wallet.getClass().getSimpleName(), id, attributes);
    }

    private GameSnapshot.Entry createShopEntry(String id, Shop shop) {
        Map<String, String> attributes = new LinkedHashMap<>();
        List<String> itemNames = new ArrayList<>();
        List<String> pricePairs = new ArrayList<>();
        if (shop.getItems() != null) {
            for (ShopItem item : shop.getItems()) {
                itemNames.add(item.name());
                pricePairs.add(item.name() + ":" + shop.getPrice(item));
            }
        }
        attributes.put("itemCount", String.valueOf(itemNames.size()));
        attributes.put("items", String.join(",", itemNames));
        attributes.put("prices", String.join(",", pricePairs));
        return new GameSnapshot.Entry(id, "shop", shop.getClass().getSimpleName(), id, attributes);
    }

    private GameSnapshot.Entry createPlowEntry(String id, Plow plow, ObjectRegistry registry) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("isEquipped", String.valueOf(plow.isEquipped()));
        String consumableType = plow.getConsumableType();
        if (consumableType != null) {
            attributes.put("consumableType", consumableType);
        }
        putId(attributes, "ownerId", plow.getOwner(), registry);
        return new GameSnapshot.Entry(id, "plow", plow.getClass().getSimpleName(), id, attributes);
    }

    private GameSnapshot.Entry createConsumableEntry(String id, Consumable consumable) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("amount", String.valueOf(consumable.getAmount()));
        attributes.put("consumableType", consumable.getConsumableType());
        return new GameSnapshot.Entry(id, "consumable", consumable.getClass().getSimpleName(), id, attributes);
    }

    private List<String> createMessages(int tickCount, Player player, ObjectRegistry registry) {
        List<String> messages = new ArrayList<>();
        messages.add("Tick: " + tickCount);
        if (player == null || registry == null) {
            messages.add("Nincs aktív játékos.");
            return messages;
        }

        if (player.isCleaner()) {
            Cleaner cleaner = Cleaner.class.cast(player);
            String walletAmount = cleaner.getWallet() == null ? "0" : String.valueOf(cleaner.getWallet().getAmount());
            messages.add("Kör: Hókotrós - " + cleaner.getName());
            messages.add("Pénz: " + walletAmount + ", hókotrók: " + idsOf(cleaner.getFleet(), registry));
        } else if (player.isBusDriver()) {
            BusDriver driver = BusDriver.class.cast(player);
            messages.add("Kör: Busz sofőr - " + driver.getName());
            messages.add("Pont: " + driver.getScore() + ", busz: " + safeId(driver.getManagedBus(), registry));
        } else {
            messages.add("Kör: " + safeId(player, registry));
        }
        return messages;
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
            Map<String, String> attributes = new LinkedHashMap<>();
            attributes.put("progress", String.valueOf(vehicle.getProgress()));
            attributes.put("isParalyzed", String.valueOf(vehicle.getIsParalyzed()));
            attributes.put("paralysisTimer", String.valueOf(vehicle.getParalysisTimer()));
            
            Lane currentLane = vehicle.getCurrentLane();
            putId(attributes, "currentLaneId", currentLane, registry);
            if (currentLane != null) {
                attributes.put("laneLength", String.valueOf(currentLane.getLength()));
                putId(attributes, "currentRoadId", currentLane.getRoad(), registry);
            }
            
            putId(attributes, "targetLaneId", vehicle.getTargetLane(), registry);

            if (vehicle.isBus()) {
                Bus bus = Bus.class.cast(vehicle);
                BusDriver driver = bus.getDriver();
                attributes.put("isSnowBlocked", String.valueOf(bus.isSnowBlocked()));
                putId(attributes, "startNodeId", bus.getStartNode(), registry);
                putId(attributes, "endNodeId", bus.getEndNode(), registry);
                putId(attributes, "driverId", driver, registry);
                putPlayerName(attributes, driver);
            } else if (Snowplow.class.isInstance(vehicle)) {
                Snowplow snowplow = Snowplow.class.cast(vehicle);
                Cleaner owner = snowplow.getOwner();
                putId(attributes, "ownerId", owner, registry);
                putPlayerName(attributes, owner);
                Plow equippedPlow = snowplow.getEquippedPlow();
                putId(attributes, "equippedPlowId", equippedPlow, registry);
                if (equippedPlow != null) {
                    attributes.put("equippedPlowType", equippedPlow.getClass().getSimpleName());
                    String consumableType = equippedPlow.getConsumableType();
                    if (consumableType != null) {
                        attributes.put("plowConsumableType", consumableType);
                    }
                }
            }
            
            entries.add(new GameSnapshot.Entry(id, "vehicle", vehicle.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Lane lane, String id) {
            Map<String, String> attributes = new LinkedHashMap<>();
            attributes.put("length", String.valueOf(lane.getLength()));
            
            if (lane.getState() != null) {
                attributes.put("condition", lane.getState().getClass().getSimpleName());
                if (IceCondition.class.isInstance(lane.getState())) {
                    IceCondition iceCondition = IceCondition.class.cast(lane.getState());
                    attributes.put("saltTimer", String.valueOf(iceCondition.getSaltTimer()));
                }
            }
            
            putId(attributes, "roadId", lane.getRoad(), registry);
            putId(attributes, "road", lane.getRoad(), registry);
            putIds(attributes, "vehicleIds", lane.getVehicles(), registry);
            attributes.put("vehicleCount", String.valueOf(lane.getVehicles() == null ? 0 : lane.getVehicles().size()));
            
            entries.add(new GameSnapshot.Entry(id, "lane", lane.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Road road, String id) {
            Map<String, String> attributes = new LinkedHashMap<>();
            String fromNodeId = findRoadSourceNodeId(road);
            if (isUsableId(fromNodeId)) {
                attributes.put("fromNodeId", fromNodeId);
                attributes.put("from", fromNodeId);
            }
            String targetNodeId = safeId(road.getTargetNode(), registry);
            if (isUsableId(targetNodeId)) {
                attributes.put("targetNodeId", targetNodeId);
                attributes.put("toNodeId", targetNodeId);
                attributes.put("to", targetNodeId);
            }
            putIds(attributes, "laneIds", road.getLanes(), registry);
            
            entries.add(new GameSnapshot.Entry(id, "road", road.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(MapNode node, String id) {
            Map<String, String> attributes = new LinkedHashMap<>();
            attributes.put("isBusStop", String.valueOf(node.isBusStop()));
            attributes.put("nodeType", node.getClass().getSimpleName());
            putIds(attributes, "outgoingRoadIds", node.getOutgoingRoads(), registry);
            
            entries.add(new GameSnapshot.Entry(id, "node", node.getClass().getSimpleName(), id, attributes));
        }

        @Override
        public void visit(Player player, String id) {
            Map<String, String> attributes = new LinkedHashMap<>();
            attributes.put("name", player.getName());

            if (player.isCleaner()) {
                Cleaner cleaner = Cleaner.class.cast(player);
                attributes.put("role", "Cleaner");
                putId(attributes, "walletId", cleaner.getWallet(), registry);
                attributes.put("walletAmount", cleaner.getWallet() == null ? "0" : String.valueOf(cleaner.getWallet().getAmount()));
                putIds(attributes, "fleetIds", cleaner.getFleet(), registry);
                attributes.put("fleetCount", String.valueOf(cleaner.getFleet() == null ? 0 : cleaner.getFleet().size()));
                putIds(attributes, "inventoryIds", cleaner.getInventory(), registry);
                attributes.put("inventoryCount", String.valueOf(cleaner.getInventory() == null ? 0 : cleaner.getInventory().size()));
            } else if (player.isBusDriver()) {
                BusDriver driver = BusDriver.class.cast(player);
                attributes.put("role", "BusDriver");
                attributes.put("score", String.valueOf(driver.getScore()));
                putId(attributes, "managedBusId", driver.getManagedBus(), registry);
            } else {
                attributes.put("role", "Unknown");
            }
            
            entries.add(new GameSnapshot.Entry(id, "player", player.getClass().getSimpleName(), player.getName(), attributes));
        }

        private String findRoadSourceNodeId(Road road) {
            for (Map.Entry<String, MapNode> nodeEntry : registry.getMapByType(MapNode.class).entrySet()) {
                MapNode node = nodeEntry.getValue();
                if (node.getOutgoingRoads() != null && node.getOutgoingRoads().contains(road)) {
                    return nodeEntry.getKey();
                }
            }
            return null;
        }
    }

    private static void putId(Map<String, String> attributes, String key, Object object, ObjectRegistry registry) {
        String id = safeId(object, registry);
        if (isUsableId(id)) {
            attributes.put(key, id);
        }
    }

    private static void putPlayerName(Map<String, String> attributes, Player player) {
        if (attributes == null || player == null) {
            return;
        }
        String playerName = player.getName();
        if (isUsableId(playerName)) {
            attributes.put("playerName", playerName);
        }
    }

    private static void putIds(Map<String, String> attributes, String key, List<?> objects, ObjectRegistry registry) {
        String ids = idsOf(objects, registry);
        attributes.put(key, ids);
    }

    private static String idsOf(List<?> objects, ObjectRegistry registry) {
        if (objects == null || registry == null) {
            return "";
        }
        List<String> ids = new ArrayList<>();
        for (Object object : objects) {
            String id = safeId(object, registry);
            if (isUsableId(id)) {
                ids.add(id);
            }
        }
        return String.join(",", ids);
    }

    private static String safeId(Object object, ObjectRegistry registry) {
        if (object == null || registry == null) {
            return null;
        }
        return registry.findId(object);
    }

    private static boolean isUsableId(String id) {
        return id != null && !id.isBlank() && !"?".equals(id) && !"null".equals(id);
    }
}
