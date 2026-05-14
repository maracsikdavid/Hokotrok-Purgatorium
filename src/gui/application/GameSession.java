package gui.application;

import actors.Player;
import cli.ObjectRegistry;
import cli.Parser;
import core.Game;
import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import gui.snapshot.GameSnapshotFactory;
import java.util.Collections;
import java.util.List;

/**
 * Egy grafikus játék munkamenet központi objektuma.
 * Összefogja a parsert, a regisztert, a modellt, a pályaleírót és a layoutot.
 */
public class GameSession {
    private Parser parser;
    private ObjectRegistry registry;
    private Game game;
    private MapDescriptor mapDescriptor;
    private MapLayout mapLayout;
    private GameCommandService commandService;
    private GameSnapshotFactory snapshotFactory;

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
        return snapshotFactory.createSnapshot(game, registry);
    }

    /**
     * Lekérdezi az aktuális játékos szerepkörét.
     *
     * @return az aktuális játékos szerepköre szöveges formában
     */
    public String getCurrentPlayerRole() {
        if (game == null || registry == null) {
            return "Unknown";
        }
        Player player = game.getCurrentPlayer(registry);
        if (player == null) {
            return "Unknown";
        }
        if (player.isCleaner()) {
            return "Cleaner";
        }
        if (player.isBusDriver()) {
            return "BusDriver";
        }
        return "Unknown";
    }

    /**
     * Visszaadja a későbbi GUI-célpontlisták helyét.
     *
     * @return jelenleg üres célpontlista
     */
    public List<String> getReachableTargets() {
        return Collections.emptyList();
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
     * Újratöltési belépési pont a későbbi session resethez.
     */
    public void reload() {
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
}