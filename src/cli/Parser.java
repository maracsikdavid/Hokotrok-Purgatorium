package cli;

import cli.commands.ActionCommand;
import cli.commands.CreateCommand;
import cli.commands.DataCommand;
import cli.commands.LinkCommand;
import cli.commands.TestCommand;
import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import core.Game;
import core.GameRules;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import cli.commands.GameCommand;

/**
 * A bemeneti parancsok (sorok) értelmezéséért felelős központi osztály.
 * Felbontja a kapott sorokat, kikeresi a megfelelő {@link CommandFactory}-t, 
 * legyártja a parancsot, majd menedzseli a validálást és futtatást.
 */
public class Parser {
    private static final int MODE_UNRESTRICTED = -1;
    private static final Set<String> VALID_CATEGORIES = new HashSet<>();
    private static final Set<String> GAME_ALIASES = new HashSet<>();

    static {
        VALID_CATEGORIES.add("test");
        VALID_CATEGORIES.add("Lane");
        VALID_CATEGORIES.add("Road");
        VALID_CATEGORIES.add("SimpleRoad");
        VALID_CATEGORIES.add("Bridge");
        VALID_CATEGORIES.add("Tunnel");
        VALID_CATEGORIES.add("Intersection");
        VALID_CATEGORIES.add("Building");
        VALID_CATEGORIES.add("BusStop");
        VALID_CATEGORIES.add("Depot");
        VALID_CATEGORIES.add("Vehicle");
        VALID_CATEGORIES.add("Car");
        VALID_CATEGORIES.add("Bus");
        VALID_CATEGORIES.add("Snowplow");
        VALID_CATEGORIES.add("Cleaner");
        VALID_CATEGORIES.add("BusDriver");
        VALID_CATEGORIES.add("Plow");
        VALID_CATEGORIES.add("SaltPlow");
        VALID_CATEGORIES.add("DragonPlow");
        VALID_CATEGORIES.add("DumpPlow");
        VALID_CATEGORIES.add("SweeperPlow");
        VALID_CATEGORIES.add("IcebreakerPlow");
        VALID_CATEGORIES.add("GravelPlow");
        VALID_CATEGORIES.add("Consumable");
        VALID_CATEGORIES.add("Salt");
        VALID_CATEGORIES.add("Gravel");
        VALID_CATEGORIES.add("Biokerosene");
        VALID_CATEGORIES.add("CleanCondition");
        VALID_CATEGORIES.add("ThinSnowCondition");
        VALID_CATEGORIES.add("ThickSnowCondition");
        VALID_CATEGORIES.add("IceCondition");
        VALID_CATEGORIES.add("GraveledIceCondition");
        VALID_CATEGORIES.add("Wallet");
        VALID_CATEGORIES.add("Shop");
        VALID_CATEGORIES.add("Map");
        VALID_CATEGORIES.add("Game");
    }

    static {
        GAME_ALIASES.add("buy");
        GAME_ALIASES.add("equip");
        GAME_ALIASES.add("plow");
        GAME_ALIASES.add("bus");
        GAME_ALIASES.add("refill");
        GAME_ALIASES.add("status");
        GAME_ALIASES.add("s");
        GAME_ALIASES.add("whereami");
        GAME_ALIASES.add("w");
    }

    private ObjectRegistry registry;
    private Map<String, CommandFactory> factories;
    private int mode;
    private TestRunner sharedTestRunner;



    /**
     * Konstruktor, amely inicializálja a memóriatérképet és a Factory-kat.
     */
    public Parser() {
        this.registry = new ObjectRegistry();
        this.factories = new HashMap<>();
        this.mode = MODE_UNRESTRICTED;
    }

    /**
     * Paraméteres konstruktor futási módhoz.
     *
     * @param mode a futási mód (0: test, 1: játék)
     */
    public Parser(int mode) {
        this();
        this.mode = mode;
        if (mode == 1) {
            loadMapFile(GameRules.mapFileName);
            bootstrapGameWorld();
        }
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param registry az objektumregiszter
     * @param factories a parancsgyárak tárolója
     */
    public Parser(ObjectRegistry registry, Map<String, CommandFactory> factories) {
        this.registry = registry;
        this.factories = factories;
        this.mode = MODE_UNRESTRICTED;
    }



    /**
     * Visszaadja az objektumregisztert, amely a beolvasott objektumokat tárolja.
     *
     * @return Az objektumregiszter referenciája.
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja az objektumregisztert.
     *
     * @param registry A beállítandó regiszter.
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }

    /**
     * Visszaadja a parancsgyárakat tartalmazó tárolót.
     *
     * @return A gyártípusokat és Factory-kat tartalmazó Map.
     */
    public Map<String, CommandFactory> getFactories() {
        return factories;
    }

    /**
     * Beállítja a parancsgyárakat.
     *
     * @param factories A beállítandó Factory Map.
     */
    public void setFactories(Map<String, CommandFactory> factories) {
        this.factories = factories;
    }

    /**
     * Visszaadja az aktuális futási módot.
     *
     * @return A mód értéke (0: test, 1: game, -1: unrestricted).
     */
    public int getMode() {
        return mode;
    }

    /**
     * Beállítja az aktuális futási módot.
     *
     * @param mode A beállítandó módérték.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }


    
    /**
     * Egyetlen bemeneti sor feldolgozása. Kiszűri a kommenteket és az üres sorokat, 
     * ellenőrzi a futási módot, majd a validálás után végrehajtja a parancsot. 
     * Hibás bemenet esetén hibaüzenetet ír a konzolra.
     * 
     * @param line A feldolgozandó bemeneti sor.
     */
    public void parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String trimmed = line.trim();

        if (trimmed.startsWith("#") || trimmed.startsWith("//")) {
            return;
        }

        String[] parts = trimmed.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length >= 1) {
            boolean isTestCommand = "test".equals(parts[0]);
            boolean isGameAlias = GAME_ALIASES.contains(parts[0]);
            if (mode == 0 && !isTestCommand) {
                ConsoleOutput.error("Only 'test' commands are allowed in Test mode.");
                return;
            }
            if (mode == 1) {
                if (isTestCommand) {
                    ConsoleOutput.error("Command not allowed in Game mode.");
                    return;
                }
                if (parts.length >= 2 && ("create".equals(parts[1]) || "link".equals(parts[1]) || "action".equals(parts[1]) || "data".equals(parts[1]))) {
                    ConsoleOutput.error("Command not allowed in Game mode.");
                    return;
                }
                if (!isGameAlias) {
                    ConsoleOutput.error("Command not allowed in Game mode.");
                    return;
                }
            }
        }

        try {
            Command command = createCommand(parts);
            if (command != null && command.validate()) {
                command.execute();
            }
        } catch (Exception e) {
            ConsoleOutput.error(e.getMessage());
        }
    }

    /**
     * A megfelelő Command objektum létrehozása a parancs típusa és a bemeneti darabok alapján.
     * 
     * @param parts A felbontott bemeneti sor elemei.
     * @return A létrehozott parancsobjektum.
     * @throws Exception Ha a parancs formátuma érvénytelen vagy a típusa ismeretlen.
     */
    private Command createCommand(String[] parts) throws Exception {
        if (mode == 1 && parts.length >= 1 && GAME_ALIASES.contains(parts[0])) {
            return new GameCommand(parts, registry);
        }

        if (parts.length < 2) {
            throw new Exception("Invalid command format.");
        }

        if (!VALID_CATEGORIES.contains(parts[0])) {
            throw new Exception("Unknown command category: " + parts[0]);
        }

        if ("test".equals(parts[0])) {
            if (sharedTestRunner == null) {
                sharedTestRunner = new TestRunner(this);
            }
            return new TestCommand(parts, sharedTestRunner);
        }

        String commandType = parts[1];

        switch (commandType) {
            case "create":
                return new CreateCommand(parts, registry);
            case "action":
                return new ActionCommand(parts, registry);
            case "link":
                return new LinkCommand(parts, registry);
            case "data":
                return new DataCommand(parts, registry);
            default:
                throw new Exception("Unknown command type: " + commandType);
        }
    }

    /**
     * Betölti a pálya inicializáló fájlt a megadott útvonalról.
     * A betöltés közben a mód ideiglenesen korlátozás nélküli, hogy a create/link
     * parancsok ne legyenek blokkolva. Az OK üzenetek el vannak nyomva.
     *
     * @param path A betöltendő fájl elérési útja.
     */
    private void loadMapFile(String path) {
        java.io.File mapFile = new java.io.File(path);
        if (!mapFile.exists()) {
            ConsoleOutput.error("Map file not found: " + path);
            return;
        }
        int savedMode = this.mode;
        boolean wasTestMode = ConsoleOutput.isTestMode();
        this.mode = MODE_UNRESTRICTED;
        ConsoleOutput.setTestMode(true);
        try (java.util.Scanner sc = new java.util.Scanner(mapFile)) {
            while (sc.hasNextLine()) {
                parseLine(sc.nextLine());
            }
        } catch (Exception e) {
            ConsoleOutput.error("Failed to load map: " + e.getMessage());
        } finally {
            this.mode = savedMode;
            ConsoleOutput.setTestMode(wasTestMode);
        }
    }

    /**
     * Game módban inicializálja a világot: spawn szabályok és köralapú állapot.
     */
    private void bootstrapGameWorld() {
        java.util.List<topology.Depot> depots = registry.getByType(topology.Depot.class);
        java.util.List<topology.BusStop> busStops = registry.getByType(topology.BusStop.class);
        java.util.List<entities.Snowplow> snowplows = registry.getByType(entities.Snowplow.class);
        java.util.List<entities.Bus> buses = registry.getByType(entities.Bus.class);
        java.util.List<core.Game> games = registry.getByType(core.Game.class);

        topology.Lane depotSpawnLane = resolveFirstOutgoingLane(depots);
        for (entities.Snowplow sp : snowplows) {
            if (depotSpawnLane != null) {
                if (sp.getCurrentLane() != null) {
                    sp.getCurrentLane().removeVehicle(sp);
                }
                depotSpawnLane.acceptVehicle(sp);
                sp.setCurrentLane(depotSpawnLane);
                sp.setProgress(0);
            }
        }

        if (busStops.size() != 2) {
            ConsoleOutput.error("Invalid map setup: expected exactly 2 BusStop nodes for Game mode.");
        } else {
            topology.BusStop fallbackA = busStops.get(0);
            topology.BusStop fallbackB = busStops.get(1);
            for (entities.Bus bus : buses) {
                topology.BusStop configuredStart = bus.getStartNode();
                topology.BusStop configuredEnd = bus.getEndNode();

                if (configuredStart == null && configuredEnd == null) {
                    bus.setStartNode(fallbackA);
                    bus.setEndNode(fallbackB);
                } else if (configuredStart == null) {
                    bus.setStartNode((configuredEnd == fallbackA) ? fallbackB : fallbackA);
                } else if (configuredEnd == null) {
                    bus.setEndNode((configuredStart == fallbackA) ? fallbackB : fallbackA);
                }

                topology.Lane busSpawnLane = resolveFirstOutgoingLane(bus.getStartNode());
                if (busSpawnLane != null) {
                    if (bus.getCurrentLane() != null) {
                        bus.getCurrentLane().removeVehicle(bus);
                    }
                    busSpawnLane.acceptVehicle(bus);
                    bus.setCurrentLane(busSpawnLane);
                    bus.setProgress(0);
                }
            }
        }

        for (core.Game game : games) {
            game.startGame();
            game.initializeTurns(registry);
        }
    }

    /**
     * Visszaadja az első elérhető induló sávot az első depóból.
     */
    private topology.Lane resolveFirstOutgoingLane(java.util.List<topology.Depot> depots) {
        if (depots == null || depots.isEmpty()) {
            return null;
        }
        return resolveFirstOutgoingLane(depots.get(0));
    }

    /**
     * Visszaadja az adott csomópont első kimenő útjának első sávját.
     */
    private topology.Lane resolveFirstOutgoingLane(topology.MapNode node) {
        if (node == null || node.getOutgoingRoads() == null || node.getOutgoingRoads().isEmpty()) {
            return null;
        }
        topology.Road road = node.getOutgoingRoads().get(0);
        if (road == null || road.getLanes() == null || road.getLanes().isEmpty()) {
            return null;
        }
        return road.getLanes().get(0);
    }

    /**
     * Visszaadja a regiszterben található első Game példányt.
     *
     * @return Az első Game objektum, vagy null ha nincs.
     */
    public Game getPrimaryGame() {
        java.util.List<Game> games = registry.getByType(Game.class);
        return games.isEmpty() ? null : games.get(0);
    }

    /**
     * Visszaadja az aktuális körös játékost Game módban.
     *
     * @return Az aktuális játékos, vagy null ha nem elérhető.
     */
    public Player getCurrentGamePlayer() {
        Game game = getPrimaryGame();
        if (game == null) {
            return null;
        }
        return game.getCurrentPlayer(registry);
    }

    /**
     * Visszaadja az aktuális körös játékos szerepkörét.
     *
     * @return "Cleaner", "BusDriver" vagy "Unknown".
     */
    public String getCurrentGamePlayerRole() {
        Player player = getCurrentGamePlayer();
        if (player == null) return "Unknown";
        if (player.isCleaner()) return "Cleaner";
        if (player.isBusDriver()) return "BusDriver";
        return "Unknown";
    }
}
