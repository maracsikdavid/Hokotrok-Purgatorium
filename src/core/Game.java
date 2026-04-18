package core;

import actors.Player;
import cli.Actionable;
import cli.ObjectRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 * A játék fő vezérlő osztálya.
 * Kezeli a térképet, a játékosokat, a boltot és az időben frissítendő elemeket.
 */
public class Game implements Actionable {
    private Map map;
    private List<Player> players = new ArrayList<>();
    private Shop shop;
    private List<ITickable> tickables = new ArrayList<>();


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


    // --- ACTIONABLE ---

    /**
     * Végrehajtja a megnevezett akciót a játék kontextusában.
     *
     * @param actionName az akció neve (pl. "tick", "startGame", "endGame")
     * @param args       a parancssor további paraméterei
     * @param registry   a központi objektumtár
     * @throws Exception ha az akció sikertelen
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
            default:
                throw new Exception("Action failed: Unknown action '" + actionName + "' for Game");
        }
    }


    // --- METÓDUSOK ---

    /**
     * Elindítja a játékot.
     */
    public void startGame() {

    }

    /**
     * Lezárja a játékot.
     */
    public void endGame() {

    }

    /**
     * Feldolgozza az időlépéseket (tick), és frissíti a regisztrált elemeket.
     */
    public void processTicks() {

    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}
