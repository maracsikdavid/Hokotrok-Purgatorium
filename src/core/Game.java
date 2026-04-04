package core;

import actors.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * A játék fő vezérlő osztálya.
 * Kezeli a térképet, a játékosokat, a boltot és az időben frissítendő elemeket.
 */
public class Game {
    private Map map;
    private List<Player> players = new ArrayList<>();
    private Shop shop;
    private List<ITickable> tickables = new ArrayList<>();

    // --- GETTEREK ÉS SETTEREK ---
    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ITickable> getTickables() {
        return tickables;
    }

    public void setTickables(List<ITickable> tickables) {
        this.tickables = tickables;
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
}
