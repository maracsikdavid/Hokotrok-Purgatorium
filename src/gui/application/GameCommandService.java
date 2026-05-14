package gui.application;

import cli.ObjectRegistry;
import core.Game;

/**
 * Típusos parancsszolgáltatás a Swing felület és a modell között.
 * A GUI komponensei ezen keresztül adják majd ki a mozgás, vásárlás és felszerelés műveleteket.
 */
public class GameCommandService {
    private ObjectRegistry registry;
    private Game game;

    /**
     * Alapértelmezett konstruktor későbbi kézi bekötéshez.
     */
    public GameCommandService() {
    }

    /**
     * Konstruktor a modellhez szükséges két fő függőség beállításához.
     *
     * @param registry a központi objektumtár
     * @param game a fő játékmodell
     */
    public GameCommandService(ObjectRegistry registry, Game game) {
        this.registry = registry;
        this.game = game;
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
     * Busz mozgatási parancs helye.
     *
     * @param busId a mozgatandó busz azonosítója
     * @param roadId a cél út azonosítója
     * @param laneId a cél sáv azonosítója
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void moveBus(String busId, String roadId, String laneId) throws Exception {
    }

    /**
     * Hókotró mozgatási parancs helye.
     *
     * @param snowplowId a mozgatandó hókotró azonosítója
     * @param roadId a cél út azonosítója
     * @param laneId a cél sáv azonosítója
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void moveSnowplow(String snowplowId, String roadId, String laneId) throws Exception {
    }

    /**
     * Bolti vásárlási parancs helye.
     *
     * @param shopId a bolt azonosítója
     * @param itemName a vásárolni kívánt tétel neve
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void buyItem(String shopId, String itemName) throws Exception {
    }

    /**
     * Eke felszerelési parancs helye.
     *
     * @param snowplowId a cél hókotró azonosítója
     * @param plowId a felszerelendő eke azonosítója
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void equipPlow(String snowplowId, String plowId) throws Exception {
    }

    /**
     * Fogyóeszköz-utántöltési parancs helye.
     *
     * @param snowplowId a cél hókotró azonosítója
     * @param consumableId a felhasználandó fogyóeszköz azonosítója
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void refill(String snowplowId, String consumableId) throws Exception {
    }

    /**
     * Körváltási ellenőrzés helye a grafikus akciók végén.
     *
     * @throws Exception ha a későbbi modellhívás hibát jelez
     */
    public void finishTurnIfNeeded() throws Exception {
    }
}