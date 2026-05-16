package gui.application;

import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import cli.ObjectRegistry;
import core.Game;
import core.Shop;
import core.ShopItem;
import entities.Bus;
import entities.Snowplow;
import equipments.Consumable;
import equipments.Plow;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import topology.Lane;
import topology.Road;

/**
 * Típusos parancsszolgáltatás a Swing felület és a modell között.
 * A GUI komponensei ezen keresztül adják majd ki a mozgás, vásárlás és felszerelés műveleteket.
 */
public class GameCommandService {
    private static final String TYPE_SNOWPLOW = "Snowplow";

    private ObjectRegistry registry;
    private Game game;
    private boolean turnFinishPending;

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
     * Busz mozgatási parancs.
     *
     * @param busId a mozgatandó busz azonosítója
     * @param roadId a cél út azonosítója
     * @param laneId a cél sáv azonosítója
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void moveBus(String busId, String roadId, String laneId) throws Exception {
        try {
            requireDependencies();
            BusDriver driver = requireCurrentBusDriver("bus");
            Bus bus = requireObject(busId, Bus.class, "Bus");
            Road road = requireObject(roadId, Road.class, "Road");
            Lane lane = requireObject(laneId, Lane.class, "Lane");

            driver.commandBus(bus, road, lane);
            turnFinishPending = true;
            finishTurnIfNeeded(false);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Bus movement failed.", exception);
        }
    }

    /**
     * Hókotró mozgatási parancs.
     *
     * @param snowplowId a mozgatandó hókotró azonosítója
     * @param roadId a cél út azonosítója
     * @param laneId a cél sáv azonosítója
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void moveSnowplow(String snowplowId, String roadId, String laneId) throws Exception {
        try {
            requireDependencies();
            Cleaner cleaner = requireCurrentCleaner("plow");
            Snowplow snowplow = requireObject(snowplowId, Snowplow.class, TYPE_SNOWPLOW);
            Road road = requireObject(roadId, Road.class, "Road");
            Lane lane = requireObject(laneId, Lane.class, "Lane");

            cleaner.commandSnowplow(snowplow, road, lane);
            turnFinishPending = true;
            finishTurnIfNeeded(false);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Snowplow movement failed.", exception);
        }
    }

    /**
     * Bolti vásárlási parancs.
     *
     * @param shopId a bolt azonosítója
     * @param itemName a vásárolni kívánt tétel neve
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void buyItem(String shopId, String itemName) throws Exception {
        try {
            requireDependencies();
            Cleaner cleaner = requireCurrentCleaner("buy");
            String resolvedShopId = resolveShopId(shopId);
            ShopItem item = resolveShopItem(itemName);

            cleaner.performAction("buyItem", new String[] {resolvedShopId, item.name()}, registry);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Purchase failed.", exception);
        }
    }

    /**
     * Eke felszerelési parancs.
     *
     * @param snowplowId a cél hókotró azonosítója
     * @param plowId a felszerelendő eke azonosítója
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void equipPlow(String snowplowId, String plowId) throws Exception {
        try {
            requireDependencies();
            Cleaner cleaner = requireCurrentCleaner("equip");
            requireObject(snowplowId, Snowplow.class, TYPE_SNOWPLOW);
            requireObject(plowId, Plow.class, "Plow");

            cleaner.performAction("equipPlowToSnowplow", new String[] {snowplowId, plowId}, registry);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Equip failed.", exception);
        }
    }

    /**
     * Fogyóeszköz-utántöltési parancs.
     *
     * @param snowplowId a cél hókotró azonosítója
     * @param consumableId a felhasználandó fogyóeszköz azonosítója
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void refill(String snowplowId, String consumableId) throws Exception {
        try {
            requireDependencies();
            Cleaner cleaner = requireCurrentCleaner("refill");
            requireObject(snowplowId, Snowplow.class, TYPE_SNOWPLOW);
            requireObject(consumableId, Consumable.class, "Consumable");

            cleaner.performAction("refillPlow", new String[] {snowplowId, consumableId}, registry);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Refill failed.", exception);
        }
    }

    /**
     * Körváltási ellenőrzés a grafikus akciók végén.
     *
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void finishTurnIfNeeded() throws Exception {
        finishTurnIfNeeded(true);
    }

    /**
     * Körváltási ellenőrzés választható konzolos státuszkiírással.
     *
     * @param announceCurrentTurn igaz esetben a modell kiírja az új aktuális játékost
     * @throws Exception ha a modellhívás hibát jelez
     */
    public void finishTurnIfNeeded(boolean announceCurrentTurn) throws Exception {
        if (!turnFinishPending) {
            return;
        }

        try {
            requireDependencies();
            requireCurrentPlayer();
            game.finishTurn(registry, announceCurrentTurn);
            turnFinishPending = false;
        } catch (Exception exception) {
            throw commandFailure("Action failed: Turn change failed.", exception);
        }
    }

    private void requireDependencies() throws CommandServiceException {
        if (registry == null) {
            throw new CommandServiceException("Action failed: ObjectRegistry is not available.");
        }
        if (game == null) {
            throw new CommandServiceException("Action failed: Game is not available.");
        }
    }

    private Player requireCurrentPlayer() throws CommandServiceException {
        Player player = game.getCurrentPlayer(registry);
        if (player == null) {
            throw new CommandServiceException("Action failed: No active player in Game mode.");
        }
        return player;
    }

    private Cleaner requireCurrentCleaner(String commandName) throws CommandServiceException {
        Player player = requireCurrentPlayer();
        if (!player.isCleaner()) {
            throw new CommandServiceException("Action failed: '" + commandName + "' command is only allowed for Cleaner turn.");
        }
        return Cleaner.class.cast(player);
    }

    private BusDriver requireCurrentBusDriver(String commandName) throws CommandServiceException {
        Player player = requireCurrentPlayer();
        if (!player.isBusDriver()) {
            throw new CommandServiceException("Action failed: '" + commandName + "' command is only allowed for BusDriver turn.");
        }
        return BusDriver.class.cast(player);
    }

    private <T> T requireObject(String id, Class<T> type, String typeName) throws CommandServiceException {
        if (!hasText(id)) {
            throw new CommandServiceException("Action failed: Missing " + typeName + " ID.");
        }
        try {
            return type.cast(registry.getObject(id.trim()));
        } catch (ClassCastException exception) {
            throw new CommandServiceException("Action failed: '" + id + "' is not a valid " + typeName + ".", exception);
        } catch (Exception exception) {
            throw commandFailure("Action failed: Object not found: " + id, exception);
        }
    }

    private String resolveShopId(String shopId) throws CommandServiceException {
        if (hasText(shopId)) {
            requireObject(shopId, Shop.class, "Shop");
            return shopId.trim();
        }

        Shop modelShop = game.getShop();
        String modelShopId = registry.findId(modelShop);
        if (isRegisteredId(modelShopId)) {
            return modelShopId;
        }

        List<Shop> shops = registry.getByType(Shop.class);
        if (!shops.isEmpty()) {
            return registry.findId(shops.get(0));
        }

        throw new CommandServiceException("Action failed: Shop object not found.");
    }

    private ShopItem resolveShopItem(String itemName) throws CommandServiceException {
        if (!hasText(itemName)) {
            throw new CommandServiceException("Action failed: Missing shop item name.");
        }

        String normalized = normalizeItemName(itemName);
        for (ShopItem item : ShopItem.values()) {
            if (normalizeItemName(item.name()).equals(normalized)) {
                return item;
            }
        }

        switch (normalized) {
            case "bikakerozin", "fuel", "fuelpack":
                return ShopItem.Biokerosene;
            case "so", "socsomag", "saltpack":
                return ShopItem.Salt;
            case "kavics", "kavicspack", "kavicssomag", "gravelpack":
                return ShopItem.Gravel;
            case "sarkanyeke":
                return ShopItem.DragonPlow;
            case "sozoeke":
                return ShopItem.SaltPlow;
            case "domperplow", "dompereke":
                return ShopItem.DumpPlow;
            case "seproeke":
                return ShopItem.SweeperPlow;
            case "jegtoroeke":
                return ShopItem.IcebreakerPlow;
            case "kavicseke", "kavicsszoroeke":
                return ShopItem.GravelPlow;
            case "hokotro":
                return ShopItem.Snowplow;
            default:
                throw new CommandServiceException("Invalid argument type: " + itemName);
        }
    }

    private String normalizeItemName(String value) {
        String withoutAccents = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        return withoutAccents.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isRegisteredId(String id) {
        return id != null && !id.isBlank() && !"?".equals(id) && !"null".equals(id);
    }

    private CommandServiceException commandFailure(String fallbackMessage, Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            message = fallbackMessage;
        }
        return new CommandServiceException(message, exception);
    }

    private static class CommandServiceException extends Exception {
        CommandServiceException(String message) {
            super(message);
        }

        CommandServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}