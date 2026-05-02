package core;

import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import cli.Actionable;
import cli.ConsoleOutput;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    /** Fut-e a játékmenet ({@link #startGame()} / {@link #endGame()}). A CLI tesztek többsége közvetlenül tickel, ezért a tickelvehető elemek feldolgozása nem függ ettől a flagtől. */
    private boolean gameInProgress;


    // --- KONSTRUKTOROK ---

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

    
    // --- GETTEREK ÉS SETTEREK ---
    
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


    // --- ACTIONABLE ---

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
                try {
                    ITickable tickable = (ITickable) obj;
                    if (!tickables.contains(tickable)) {
                        tickables.add(tickable);
                    }
                } catch (ClassCastException e) {
                    throw new Exception("Action failed: '" + args[0] + "' is not ITickable");
                }
                break;
            }
            default:
                throw new Exception();
        }
    }


    // --- METÓDUSOK ---

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
        gameInProgress = true;
        currentPlayerIndex = 0;
        turnsInitialized = false;
    }

    /**
     * Lezárja a játékmenetet: üríti a tickelhető elemek listáját (a regiszterben lévő objektumok nem törlődnek).
     */
    public void endGame() {
        gameInProgress = false;
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
        if (registry == null) {
            return;
        }

        List<String> ids = new ArrayList<>(registry.getObjects().keySet());
        Collections.sort(ids);

        List<Player> orderedPlayers = new ArrayList<>();
        for (String id : ids) {
            Object obj = registry.getObjects().get(id);
            try {
                Player p = (Player) obj;
                if (p.isCleaner()) {
                    orderedPlayers.add(p);
                }
            } catch (ClassCastException ignored) {
            }
        }
        for (String id : ids) {
            Object obj = registry.getObjects().get(id);
            try {
                Player p = (Player) obj;
                if (p.isBusDriver()) {
                    orderedPlayers.add(p);
                }
            } catch (ClassCastException ignored) {
            }
        }

        players = orderedPlayers;
        currentPlayerIndex = 0;
        turnsInitialized = !players.isEmpty();

        if (turnsInitialized) {
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
            initializeTurns(registry);
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
        Player current = getCurrentPlayer(registry);
        if (current == null) {
            System.out.println("> ERROR: No active player in Game mode.");
            return;
        }

        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            processTicks();
        }

        printCurrentPlayerStatus(registry);
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

        // Jobb olvashatóság: körváltáskor hagyunk egy üres sort.
        System.out.println();

        if (current.isBusDriver()) {
            printBusDriverStatus((BusDriver) current, registry);
            return;
        }
        if (current.isCleaner()) {
            printCleanerStatus((Cleaner) current, registry);
            return;
        }

        System.out.println("> Turn: " + registry.findId(current));
    }

    private void printBusDriverStatus(BusDriver driver, ObjectRegistry registry) {
        entities.Bus bus = driver.getManagedBus();
        String busId = registry.findId(bus);
        String laneId = (bus != null) ? registry.findId(bus.getCurrentLane()) : "null";
        String destinationId = (bus != null) ? registry.findId(bus.getEndNode()) : "null";

        ConsoleOutput.roleInfo("BusDriver", "Turn: BusDriver");
        ConsoleOutput.roleInfo("BusDriver", "name=" + driver.getName());
        ConsoleOutput.roleInfo("BusDriver", "score=" + driver.getScore());
        ConsoleOutput.roleInfo("BusDriver", "bus=" + busId + ", lane=" + laneId + ", destination=" + destinationId);
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

        StringBuilder invStatus = new StringBuilder("[");
        List<equipments.Consumable> inv = cleaner.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            equipments.Consumable c = inv.get(i);
            if (i > 0) invStatus.append(", ");
            invStatus.append(registry.findId(c)).append(":").append(c.getConsumableType());
        }
        invStatus.append("]");

        ConsoleOutput.roleInfo("Cleaner", "Turn: Cleaner");
        ConsoleOutput.roleInfo("Cleaner", "name=" + cleaner.getName());
        ConsoleOutput.roleInfo("Cleaner", "wallet=" + walletAmount);
        ConsoleOutput.roleInfo("Cleaner", "snowplows=" + fleetStatus);
        ConsoleOutput.roleInfo("Cleaner", "plowHeads=" + headsStatus);
        ConsoleOutput.roleInfo("Cleaner", "inventory=" + invStatus);
        if (fleet.size() > 1) {
            ConsoleOutput.roleInfo("Cleaner", "note=Multiple snowplows available, use snowplowID in commands.");
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
                try {
                    ITickable tickable = (ITickable) obj;
                    if (!tickables.contains(tickable)) {
                        tickables.add(tickable);
                    }
                } catch (ClassCastException e) {
                    throw new Exception("Action failed: '" + args[0] + "' is not ITickable");
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
