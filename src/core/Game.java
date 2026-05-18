package core;

import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import cli.Actionable;
import cli.ConsoleOutput;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import entities.Bus;
import entities.Car;
import entities.Snowplow;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * A játék fő vezérlő osztálya.
 * Kezeli a térképet, a játékosokat, a boltot és az időben frissítendő elemeket.
 */
public class Game implements Actionable, Linkable, Printable {
    private Map map;
    private List<Player> players = new ArrayList<>();
    private Shop shop;
    private List<ITickable> tickables = new ArrayList<>();
    private int tickCount;
    private int currentPlayerIndex;
    private boolean turnsInitialized;



    /**
     * Alapértelmezett konstruktor.
     */
    public Game() {
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param map a játék térképe
     * @param players a játékosok listája
     * @param shop a játék boltja
     * @param tickables a tickelhető objektumok listája
     */
    public Game(Map map, List<Player> players, Shop shop, List<ITickable> tickables) {
        this.map = map;
        this.players = players;
        this.shop = shop;
        this.tickables = tickables;
    }

    
    
    /**
     * Visszaadja a játék térképét.
     *
     * @return a térkép
     */
    public Map getMap() {
        return map;
    }

    /**
     * Beállítja a játék térképét.
     *
     * @param map a beállítandó térkép
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Visszaadja a játékosok listáját.
     *
     * @return a játékosok listája
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Beállítja a játékosok listáját.
     *
     * @param players a beállítandó játékoslista
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Visszaadja a játék boltját.
     *
     * @return a bolt
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * Beállítja a játék boltját.
     *
     * @param shop a beállítandó bolt
     */
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    /**
     * Visszaadja a tickelhető objektumok listáját.
     *
     * @return a tickelhető objektumok listája
     */
    public List<ITickable> getTickables() {
        return tickables;
    }

    /**
     * Beállítja a tickelhető objektumok listáját.
     *
     * @param tickables a beállítandó tickelhető lista
     */
    public void setTickables(List<ITickable> tickables) {
        this.tickables = tickables;
    }

    /**
     * Visszaadja az eddig feldolgozott tickek számát.
     *
     * @return A tick számláló aktuális értéke.
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Beállítja a tick számláló értékét.
     *
     * @param tickCount A beállítandó tick számláló.
     */
    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }



    /**
     * Végrehajtja a megnevezett akciót a játék kontextusában.
     *
     * @param actionName Az akció neve (pl. "tick", "startGame", "endGame").
     * @param args       A parancssor további paraméterei.
     * @param registry   A központi objektumtár az azonosítók feloldásához.
     * @throws Exception Ha az akció ismeretlen vagy a végrehajtás során hiba történik.
     */
    @Override
    public void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception {
        switch (actionName) {
            case "tick":
                int times = 1;
                if (args.length >= 1) {
                    times = Integer.parseInt(args[0]);
                }
                if (times < 0) {
                    throw new Exception("Invalid argument type: " + args[0]);
                }
                for (int i = 0; i < times; i++) {
                    processTicks();
                }
                break;
            case "startGame":
                startGame();
                break;
            case "endGame":
                endGame();
                break;
            case "initTurns":
                initializeTurns(registry);
                break;
            case "status":
                printCurrentPlayerStatus(registry);
                break;
            case "finishTurn":
                finishTurn(registry);
                break;
            case "addTickable": {
                if (args.length < 1) throw new Exception("Action failed: addTickable requires an ID");
                Object obj = registry.getObject(args[0]);
                if (!ITickable.class.isInstance(obj)) {
                    throw new Exception("Action failed: '" + args[0] + "' is not ITickable");
                }
                ITickable tickable = (ITickable) obj;
                if (!tickables.contains(tickable)) {
                    tickables.add(tickable);
                }
                break;
            }
            default:
                throw new Exception();
        }
    }



    /**
     * Elindítja a szimulációt: biztosítja a térkép, bolt, listák létezését, nullázza a tick számlálót,
     * és megjelöli, hogy a játék aktív. Az egyes szimulációs elemeket ({@link ITickable}) előzőleg
     * vagy linkkel ({@code addTickable}), vagy közvetlenül a listába téve kell regisztrálni.
     */
    public void startGame() {
        if (map == null) {
            map = new Map();
        }
        if (players == null) {
            players = new ArrayList<>();
        }
        if (tickables == null) {
            tickables = new ArrayList<>();
        }
        if (shop == null) {
            shop = new Shop();
        }
        tickCount = 0;
        currentPlayerIndex = 0;
        turnsInitialized = false;
    }

    /**
     * Lezárja a játékmenetet: üríti a tickelhető elemek listáját (a regiszterben lévő objektumok nem törlődnek).
     */
    public void endGame() {
        turnsInitialized = false;
        if (tickables != null) {
            tickables.clear();
        }
    }

    /**
     * A fő szimulációs órajel: egy diszkrét időlépésben sorban meghívja minden regisztrált {@link ITickable}
     * {@code tick()} metódusát. Pillanatkép másolatot használ, így egy tick során újonnan felvett elemek
     * még nem futnak ebben a körben. Minden körben növeli {@link #tickCount}-ot.
     */
    public void processTicks() {
        if (tickables == null) {
            tickCount++;
            return;
        }

        List<ITickable> snapshot = new ArrayList<>(tickables);
        for (ITickable tickable : snapshot) {
            if (tickable != null) {
                tickable.tick();
            }
        }

        tickCount++;
    }

    /**
     * Felépíti a körsorrendet a regiszterben lévő játékosokból, és kiírja az első kör státuszát.
     * Cleaner játékosok megelőzik a BusDriver játékosokat, azonos csoporton belül az ID szerinti
     * növekvő sorrend érvényesül.
     *
     * @param registry A központi objektumtár.
     */
    public void initializeTurns(ObjectRegistry registry) {
        initializeTurns(registry, true);
    }

    /**
     * Felépíti a körsorrendet a regiszterben lévő játékosokból.
     * Cleaner játékosok megelőzik a BusDriver játékosokat, azonos csoporton belül az ID szerinti
     * növekvő sorrend érvényesül.
     *
     * @param registry A központi objektumtár.
     * @param announceCurrentTurn igaz esetben a sor eleji státusz azonnal kiírásra kerül
     */
    public void initializeTurns(ObjectRegistry registry, boolean announceCurrentTurn) {
        if (registry == null) {
            return;
        }

        List<String> ids = new ArrayList<>(registry.getObjects().keySet());
        Collections.sort(ids);

        List<Player> orderedPlayers = new ArrayList<>();
        for (String id : ids) {
            Player p = registry.getByType(Player.class)
                .stream()
                .filter(pl -> registry.findId(pl).equals(id) && pl.isCleaner())
                .findFirst().orElse(null);
            if (p != null) orderedPlayers.add(p);
        }
        for (String id : ids) {
            Player p = registry.getByType(Player.class)
                .stream()
                .filter(pl -> registry.findId(pl).equals(id) && pl.isBusDriver())
                .findFirst().orElse(null);
            if (p != null) orderedPlayers.add(p);
        }

        players = orderedPlayers;
        currentPlayerIndex = 0;
        turnsInitialized = !players.isEmpty();

        if (turnsInitialized && announceCurrentTurn) {
            printCurrentPlayerStatus(registry);
        }
    }

    /**
     * Visszaadja az aktuális körön lévő játékost.
     *
     * @param registry A központi objektumtár (ha szükséges az inicializáláshoz).
     * @return Az aktuális játékos, vagy null, ha nincs játékos.
     */
    public Player getCurrentPlayer(ObjectRegistry registry) {
        if (!turnsInitialized) {
            initializeTurns(registry, false);
        }
        if (players == null || players.isEmpty()) {
            return null;
        }
        if (currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        return players.get(currentPlayerIndex);
    }

    /**
     * Lezárja az aktuális játékos körét, vált a következőre, és ha kör vége volt,
     * végrehajt egy teljes tick kört.
     *
     * @param registry A központi objektumtár.
     */
    public void finishTurn(ObjectRegistry registry) {
        finishTurn(registry, true);
    }

    /**
     * Lezárja az aktuális játékos körét, és igény szerint kiírja az új aktív játékost.
     *
     * @param registry A központi objektumtár.
     * @param announceCurrentTurn igaz esetben kiírja a következő játékos státuszát
     */
    public void finishTurn(ObjectRegistry registry, boolean announceCurrentTurn) {
        Player current = getCurrentPlayer(registry);
        if (current == null) {
            System.out.println("> ERROR: No active player in Game mode.");
            return;
        }

        advanceTurnCursor();

        if (announceCurrentTurn) {
            printCurrentPlayerStatus(registry);
        }
    }

    private void advanceTurnCursor() {
        if (players == null || players.isEmpty()) {
            currentPlayerIndex = 0;
            return;
        }

        boolean roundSimulationProcessed = false;

        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            finishRoundSimulation();
            roundSimulationProcessed = true;
        }

        int skippedPlayers = 0;
        while (skippedPlayers < players.size() && shouldSkipPlayer(players.get(currentPlayerIndex))) {
            currentPlayerIndex++;
            if (currentPlayerIndex >= players.size()) {
                if (roundSimulationProcessed) {
                    currentPlayerIndex = 0;
                } else {
                    finishRoundSimulation();
                    roundSimulationProcessed = true;
                }
            }
            skippedPlayers++;
        }
    }

    private void finishRoundSimulation() {
        currentPlayerIndex = 0;
        processRoundSimulationTick();
    }

    private void processRoundSimulationTick() {
        if (tickables == null) {
            tickCount++;
            return;
        }

        List<ITickable> snapshot = new ArrayList<>(tickables);
        for (ITickable tickable : snapshot) {
            if (tickable == null) {
                continue;
            }
            if (Bus.class.isInstance(tickable) || Snowplow.class.isInstance(tickable)) {
                continue;
            }
            int repetitions = Car.class.isInstance(tickable) ? GameRules.ROAD_TRAVERSAL_TICKS : 1;
            for (int step = 0; step < repetitions; step++) {
                tickable.tick();
            }
        }

        tickCount++;
    }

    private boolean shouldSkipPlayer(Player player) {
        if (player == null || !player.isBusDriver()) {
            return false;
        }
        Bus bus = BusDriver.class.cast(player).getManagedBus();
        return bus == null || bus.isSnowBlocked() || bus.getIsParalyzed();
    }

    /**
     * Kiírja az aktuális játékos kör eleji státuszát.
     *
     * @param registry A központi objektumtár.
     */
    public void printCurrentPlayerStatus(ObjectRegistry registry) {
        Player current = getCurrentPlayer(registry);
        if (current == null) {
            System.out.println("> ERROR: No active player in Game mode.");
            return;
        }

        ConsoleOutput.blankLine();

        if (current.isBusDriver()) {
            printBusDriverStatus((BusDriver) current);
        } else if (current.isCleaner()) {
            printCleanerStatus((Cleaner) current, registry);
        } else {
            System.out.println("> Turn: " + registry.findId(current));
        }

        ConsoleOutput.blankLine();
    }

    private void printBusDriverStatus(BusDriver driver) {
        ConsoleOutput.plain("Turn: BusDriver");
        ConsoleOutput.keyValueBus("name", driver.getName());
        ConsoleOutput.keyValueBus("score", String.valueOf(driver.getScore()));
    }

    private void printCleanerStatus(Cleaner cleaner, ObjectRegistry registry) {
        String walletAmount = (cleaner.getWallet() == null) ? "0" : String.valueOf(cleaner.getWallet().getAmount());

        StringBuilder fleetStatus = new StringBuilder("[");
        List<entities.Snowplow> fleet = cleaner.getFleet();
        for (int i = 0; i < fleet.size(); i++) {
            entities.Snowplow sp = fleet.get(i);
            if (i > 0) fleetStatus.append(", ");
            fleetStatus.append(registry.findId(sp))
                       .append("@")
                       .append(registry.findId(sp.getCurrentLane()));
        }
        fleetStatus.append("]");

        StringBuilder headsStatus = new StringBuilder("[");
        for (int i = 0; i < fleet.size(); i++) {
            entities.Snowplow sp = fleet.get(i);
            if (i > 0) headsStatus.append(", ");
            String headType = (sp.getEquippedPlow() == null) ? "none" : sp.getEquippedPlow().getClass().getSimpleName();
            headsStatus.append(registry.findId(sp)).append(":").append(headType);
        }
        headsStatus.append("]");

        ConsoleOutput.plain("Turn: Cleaner");
        ConsoleOutput.keyValueSnowplow("name", cleaner.getName());
        ConsoleOutput.keyValueSnowplow("wallet", walletAmount);
        ConsoleOutput.keyValueSnowplow("snowplows", fleetStatus.toString());
        ConsoleOutput.keyValueSnowplow("plowHeads", headsStatus.toString());
    }

    public void printStatusDetails(ObjectRegistry registry) {
        Player current = getCurrentPlayer(registry);
        if (current == null) return;

        ConsoleOutput.blankLine();
        ConsoleOutput.plain("--- Where to move ---");

        if (current.isCleaner()) {
            Cleaner cleaner = (Cleaner) current;
            for (entities.Snowplow sp : cleaner.getFleet()) {
                String spId = registry.findId(sp);
                String laneId = registry.findId(sp.getCurrentLane());
                ConsoleOutput.snowplow(spId + " @ " + laneId + ":");
                printReachableByRoad(sp.getCurrentLane(), current, registry);
            }
        } else if (current.isBusDriver()) {
            BusDriver driver = (BusDriver) current;
            entities.Bus bus = driver.getManagedBus();
            if (bus != null) {
                String busId = registry.findId(bus);
                String laneId = registry.findId(bus.getCurrentLane());
                ConsoleOutput.bus(busId + " @ " + laneId + ":");
                printReachableByRoad(bus.getCurrentLane(), current, registry);
            }
        }
        ConsoleOutput.blankLine();
    }

    private void printReachableByRoad(Lane currentLane, Player current, ObjectRegistry registry) {
        if (currentLane == null) {
            ConsoleOutput.plain("  (no current lane)");
            return;
        }

        java.util.LinkedHashMap<Road, java.util.List<Lane>> byRoad = new java.util.LinkedHashMap<>();
        Road currentRoad = currentLane.getRoad();

        if (currentRoad != null && currentRoad.getLanes() != null) {
            byRoad.put(currentRoad, new java.util.ArrayList<>(currentRoad.getLanes()));
        }

        for (Lane adj : currentLane.getAdjacentLanes()) {
            Road adjRoad = adj.getRoad();
            if (adjRoad != null && adjRoad != currentRoad) {
                byRoad.computeIfAbsent(adjRoad, r -> new java.util.ArrayList<>()).add(adj);
            }
        }

        MapNode nextNode = (currentRoad != null) ? currentRoad.getTargetNode() : null;
        if (nextNode != null && nextNode.getOutgoingRoads() != null) {
            for (Road outRoad : nextNode.getOutgoingRoads()) {
                if (outRoad == null || outRoad.getLanes() == null) continue;
                byRoad.computeIfAbsent(outRoad, r -> new java.util.ArrayList<>(r.getLanes()));
            }
        }

        for (java.util.Map.Entry<Road, java.util.List<Lane>> entry : byRoad.entrySet()) {
            String roadId = registry.findId(entry.getKey());
            StringBuilder sb = new StringBuilder("  " + roadId + ": ");
            List<Lane> lanes = entry.getValue();
            for (int i = 0; i < lanes.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(registry.findId(lanes.get(i)));
            }
            if (current != null && current.isBusDriver()) {
                ConsoleOutput.bus(sb.toString());
            } else {
                ConsoleOutput.snowplow(sb.toString());
            }
        }
    }

    /**
     * Összekapcsolja a játék objektumot más objektumokkal a parancssori argumentumok alapján.
     *
     * @param property A beállítandó tulajdonság neve.
     * @param args     Az összekapcsoláshoz szükséges argumentumok.
     * @param registry A központi objektumtár.
     * @throws Exception Ha a tulajdonság ismeretlen vagy az összekapcsolás sikertelen.
     */
    @Override
    public void performLink(String property, String[] args, ObjectRegistry registry) throws Exception {
        switch (property) {
            case "addTickable": {
                Object obj = registry.getObject(args[0]);
                if (!ITickable.class.isInstance(obj)) {
                    throw new Exception("Action failed: '" + args[0] + "' is not ITickable");
                }
                ITickable tickable = (ITickable) obj;
                if (!tickables.contains(tickable)) {
                    tickables.add(tickable);
                }
                break;
            }
            case "addPlayer": {
                Player p = (Player) registry.getObject(args[0]);
                players.add(p);
                break;
            }
            case "map":
            case "setMap": {
                Map m = (Map) registry.getObject(args[0]);
                setMap(m);
                break;
            }
            case "shop":
            case "setShop": {
                Shop s = (Shop) registry.getObject(args[0]);
                setShop(s);
                break;
            }
            default:
                throw new Exception("Action failed: Unknown link property '" + property + "' for Game");
        }
    }

    /**
     * Az objektum állapotának és alapvető játékadatainak kiírása a standard kimenetre.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry A központi objektumtár.
     */
    @Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println("Game," + id);
        System.out.println("tickCount," + tickCount);
    }
}
