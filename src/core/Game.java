package core;

import actors.Player;
import cli.Actionable;
import cli.Linkable;
import cli.ObjectRegistry;
import cli.Printable;
import java.util.ArrayList;
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
                processTicks();
                break;
            case "startGame":
                startGame();
                break;
            case "endGame":
                endGame();
                break;
            case "addTickable": {
                if (args.length < 1) throw new Exception("Action failed: addTickable requires an ID");
                Object obj = registry.getObject(args[0]);
                if (obj instanceof ITickable) {
                    tickables.add((ITickable) obj);
                } else {
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
    }

    /**
     * Lezárja a játékmenetet: üríti a tickelhető elemek listáját (a regiszterben lévő objektumok nem törlődnek).
     */
    public void endGame() {
        gameInProgress = false;
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
                if (obj instanceof ITickable) {
                    tickables.add((ITickable) obj);
                } else {
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
