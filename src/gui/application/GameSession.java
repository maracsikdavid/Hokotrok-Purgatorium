package gui.application;

import actors.Player;
import cli.ObjectRegistry;
import cli.Parser;
import core.Game;
import entities.Bus;
import entities.Snowplow;
import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import gui.snapshot.GameSnapshotFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Egy grafikus játék munkamenet központi objektuma.
 * Összefogja a parsert, a regisztert, a modellt, a pályaleírót és a layoutot.
 */
public class GameSession {
    private static final String ROLE_UNKNOWN = "Unknown";
    private static final String ROLE_CLEANER = "Cleaner";
    private static final String ROLE_BUS_DRIVER = "BusDriver";

    private Parser parser;
    private ObjectRegistry registry;
    private Game game;
    private MapDescriptor mapDescriptor;
    private MapLayout mapLayout;
    private GameCommandService commandService;
    private GameSnapshotFactory snapshotFactory;
    private List<PlayerRegistration> playerRegistrations = new ArrayList<>();
    private final Object modelLock = new Object();
    private final TargetCalculator targetCalculator = new TargetCalculator();

    /**
     * Alapértelmezett konstruktor későbbi kézi bekötéshez.
     */
    public GameSession() {
        this.snapshotFactory = new GameSnapshotFactory();
    }

    /**
     * Konstruktor a session fő függőségeinek beállításához.
     *
     * @param parser a pálya bootstrapolását végző parser
     * @param registry a központi objektumtár
     * @param game a fő játékmodell
     * @param mapDescriptor az aktuális pálya leírója
     * @param mapLayout az aktuális pálya grafikus layoutja
     * @param commandService a típizosított parancsszolgáltatás
     */
    public GameSession(Parser parser, ObjectRegistry registry, Game game, MapDescriptor mapDescriptor,
                       MapLayout mapLayout, GameCommandService commandService) {
        this.parser = parser;
        this.registry = registry;
        this.game = game;
        this.mapDescriptor = mapDescriptor;
        this.mapLayout = mapLayout;
        this.commandService = commandService;
        this.snapshotFactory = new GameSnapshotFactory();
    }

    /**
     * Legyártja a munkamenet aktuális GUI-barát pillanatképét.
     *
     * @return az aktuális snapshot
     */
    public GameSnapshot getSnapshot() {
        synchronized (modelLock) {
            return snapshotFactory.createSnapshot(game, registry);
        }
    }

    /**
     * Lekérdezi az aktuális játékos szerepkörét.
     *
     * @return az aktuális játékos szerepköre szöveges formában
     */
    public String getCurrentPlayerRole() {
        synchronized (modelLock) {
            if (game == null || registry == null) {
                return ROLE_UNKNOWN;
            }
            Player player = game.getCurrentPlayer(registry);
            if (player == null) {
                return ROLE_UNKNOWN;
            }
            if (player.isCleaner()) {
                return ROLE_CLEANER;
            }
            if (player.isBusDriver()) {
                return ROLE_BUS_DRIVER;
            }
            return ROLE_UNKNOWN;
        }
    }

    /**
     * Kiírja a konzolra a jelenleg aktív játékos státuszát.
     */
    public void announceCurrentPlayerStatus() {
        synchronized (modelLock) {
            if (game != null && registry != null) {
                game.printCurrentPlayerStatus(registry);
            }
        }
    }

    /**
     * Visszaadja az aktuális játékos alapértelmezett járművével elérhető célsávokat.
     *
     * @return az elérhető célsávok azonosítói
     */
    public List<String> getReachableTargets() {
        synchronized (modelLock) {
            return getReachableTargetsInternal(resolveCurrentVehicleId());
        }
    }

    /**
     * Visszaadja a megadott járművel a következő csomópontból elérhető célsávokat.
     *
     * @param vehicleId a kijelölt jármű azonosítója
     * @return az elérhető célsávok azonosítói
     */
    public List<String> getReachableTargets(String vehicleId) {
        synchronized (modelLock) {
            return getReachableTargetsInternal(vehicleId);
        }
    }

    /**
     * Modelloldalon is létrehozza a regisztrációs képernyőn felvett játékost.
     *
     * @param playerName a játékos megjelenített neve
     * @param role a modellbeli szerepkör neve: Cleaner vagy BusDriver
     * @throws Exception ha a pályán nem hozható létre a kért játékos
     */
    public void registerPlayer(String playerName, String role) throws Exception {
        synchronized (modelLock) {
            registerPlayer(playerName, role, true);
        }
    }

    /**
     * Visszaadja a session parancsszolgáltatását.
     *
     * @return a parancsszolgáltatás
     */
    public GameCommandService getCommandService() {
        return commandService;
    }

    /**
     * Ugyanazon modellezaro objektum, amit GUI es konzol egyarant hasznalhat.
     *
     * @return a kozos model lock
     */
    public Object getModelLock() {
        return modelLock;
    }

    /**
     * Konzolos parancs vegrehajtasa ugyanazon Parser/Game allapoton, mint a GUI.
     *
     * @param line a futtatando konzolparancs
     */
    public void executeConsoleCommand(String line) {
        if (line == null || line.isBlank()) {
            return;
        }

        synchronized (modelLock) {
            if (parser == null) {
                throw new SessionLifecycleException("A konzolos parancs futtatásához nincs elérhető Parser.");
            }
            parser.parseLine(line);
            refreshModelReferences();
        }
    }

    /**
     * Hókotró mozgatása közös modellzár alatt.
     */
    public void moveSnowplow(String snowplowId, String roadId, String laneId) throws Exception {
        synchronized (modelLock) {
            requireCommandService().moveSnowplow(snowplowId, roadId, laneId);
        }
    }

    /**
     * Busz mozgatása közös modellzár alatt.
     */
    public void moveBus(String busId, String roadId, String laneId) throws Exception {
        synchronized (modelLock) {
            requireCommandService().moveBus(busId, roadId, laneId);
        }
    }

    public void finishCurrentTurn(boolean announceCurrentTurn) {
        synchronized (modelLock) {
            if (game == null) {
                throw new SessionLifecycleException("A körváltáshoz nincs elérhető Game objektum.");
            }
            game.finishTurn(registry, announceCurrentTurn);
        }
    }

    /**
     * Vasarlas közös modellzár alatt.
     */
    public void buyItem(String shopId, String itemName) throws Exception {
        synchronized (modelLock) {
            requireCommandService().buyItem(shopId, itemName);
        }
    }

    /**
     * Eke felszerelese közös modellzár alatt.
     */
    public void equipPlow(String snowplowId, String plowId) throws Exception {
        synchronized (modelLock) {
            requireCommandService().equipPlow(snowplowId, plowId);
        }
    }

    /**
     * Utantoltes közös modellzár alatt.
     */
    public void refill(String snowplowId, String consumableId) throws Exception {
        synchronized (modelLock) {
            requireCommandService().refill(snowplowId, consumableId);
        }
    }

    /**
     * Újratöltési belépési pont a későbbi session resethez.
     */
    public void reload() {
        synchronized (modelLock) {
            if (mapDescriptor == null) {
                throw new SessionLifecycleException("A munkamenet nem tölthető újra pályaleíró nélkül.");
            }

            try {
                List<PlayerRegistration> registrationsToRestore = new ArrayList<>(playerRegistrations);
                GameSession reloadedSession = new GameSessionFactory().createSession(mapDescriptor);
                for (PlayerRegistration registration : registrationsToRestore) {
                    reloadedSession.registerPlayer(registration.getName(), registration.getRole());
                }

                this.parser = reloadedSession.parser;
                this.registry = reloadedSession.registry;
                this.game = reloadedSession.game;
                this.mapLayout = reloadedSession.mapLayout;
                this.commandService = reloadedSession.commandService;
                this.snapshotFactory = reloadedSession.snapshotFactory;
                this.playerRegistrations = new ArrayList<>(reloadedSession.playerRegistrations);
            } catch (Exception exception) {
                throw new SessionLifecycleException("A munkamenet újratöltése sikertelen: " + exception.getMessage(), exception);
            }
        }
    }

    /**
     * Visszaadja a sessionhez tartozó parsert.
     *
     * @return a parser
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Beállítja a sessionhez tartozó parsert.
     *
     * @param parser az új parser
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * Visszaadja a központi objektumtárat.
     *
     * @return az objektumregiszter
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja a központi objektumtárat.
     *
     * @param registry az új objektumregiszter
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }

    /**
     * Visszaadja a fő játékmodellt.
     *
     * @return a játékmodell
     */
    public Game getGame() {
        return game;
    }

    /**
     * Beállítja a fő játékmodellt.
     *
     * @param game az új játékmodell
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Visszaadja az aktuális pályaleírót.
     *
     * @return a pályaleíró
     */
    public MapDescriptor getMapDescriptor() {
        return mapDescriptor;
    }

    /**
     * Beállítja az aktuális pályaleírót.
     *
     * @param mapDescriptor az új pályaleíró
     */
    public void setMapDescriptor(MapDescriptor mapDescriptor) {
        this.mapDescriptor = mapDescriptor;
    }

    /**
     * Visszaadja az aktuális grafikus layoutot.
     *
     * @return a map layout
     */
    public MapLayout getMapLayout() {
        return mapLayout;
    }

    /**
     * Beállítja az aktuális grafikus layoutot.
     *
     * @param mapLayout az új map layout
     */
    public void setMapLayout(MapLayout mapLayout) {
        this.mapLayout = mapLayout;
    }

    /**
     * Beállítja a session parancsszolgáltatását.
     *
     * @param commandService az új parancsszolgáltatás
     */
    public void setCommandService(GameCommandService commandService) {
        this.commandService = commandService;
    }

    private void registerPlayer(String playerName, String role, boolean rememberRegistration) throws Exception {
        if (parser == null) {
            throw new SessionLifecycleException("A játékos regisztrációhoz nincs elérhető Parser.");
        }

        if (ROLE_CLEANER.equals(role)) {
            parser.registerCleanerPlayer(playerName);
        } else if (ROLE_BUS_DRIVER.equals(role)) {
            parser.registerBusDriverPlayer(playerName);
        } else {
            throw new SessionLifecycleException("Ismeretlen játékosszerep: " + role);
        }

        refreshModelReferences();
        if (game != null) {
            game.initializeTurns(registry, false);
        }
        if (rememberRegistration) {
            playerRegistrations.add(new PlayerRegistration(playerName, role));
        }
    }

    private void refreshModelReferences() {
        if (parser == null) {
            return;
        }
        registry = parser.getRegistry();
        game = parser.getPrimaryGame();
        if (commandService == null) {
            commandService = new GameCommandService(registry, game);
        } else {
            commandService.setRegistry(registry);
            commandService.setGame(game);
        }
    }

    private GameCommandService requireCommandService() {
        if (commandService == null) {
            refreshModelReferences();
        }
        if (commandService == null) {
            throw new SessionLifecycleException("A parancsvégrehajtáshoz nincs elérhető GameCommandService.");
        }
        return commandService;
    }

    private List<String> getReachableTargetsInternal(String vehicleId) {
        if (registry == null || vehicleId == null || vehicleId.isBlank()) {
            return Collections.emptyList();
        }
        return targetCalculator.getReachableLaneIds(vehicleId, registry);
    }

    private String resolveCurrentVehicleId() {
        if (game == null || registry == null) {
            return null;
        }

        Player player = game.getCurrentPlayer(registry);
        if (player == null) {
            return null;
        }
        if (player.isCleaner()) {
            actors.Cleaner cleaner = actors.Cleaner.class.cast(player);
            if (cleaner.getFleet() == null || cleaner.getFleet().isEmpty()) {
                return null;
            }
            Snowplow snowplow = cleaner.getFleet().get(0);
            return registry.findId(snowplow);
        }
        if (player.isBusDriver()) {
            actors.BusDriver driver = actors.BusDriver.class.cast(player);
            Bus bus = driver.getManagedBus();
            return registry.findId(bus);
        }
        return null;
    }

    /**
     * A reload során visszaállítandó modelloldali játékos-regisztráció.
     */
    public static class PlayerRegistration {
        private final String name;
        private final String role;

        public PlayerRegistration(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }
    }

    private static class SessionLifecycleException extends IllegalStateException {
        SessionLifecycleException(String message) {
            super(message);
        }

        SessionLifecycleException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}