package core;

import actors.Cleaner;
import java.util.ArrayList;
import java.util.List;

/**
 * Itt tudnak a Takarítók (Cleaner) az összegyűjtött pénzükből (Wallet) új felszereléseket, 
 * különböző kotrófejeket (Plow) vagy üzemanyagot/sót vásárolni.
 */
public class Shop implements cli.Printable {
    private List<ShopItem> items = new ArrayList<>();


    /**
     * Alapértelmezett konstruktor.
     */
    public Shop() {
        for (ShopItem item : ShopItem.values()) {
            items.add(item);
        }
    }

    /**
     * Paraméteres konstruktor, amely inicializálja a bolt kínálatát.
     *
     * @param items a boltban elérhető termékek listája
     */
    public Shop(List<ShopItem> items) {
        this.items = items;
    }


    /**
     * Visszaadja a boltban elérhető termékek listáját.
     *
     * @return a termékek listája
     */
    public List<ShopItem> getItems() {
        return items;
    }

    /**
     * Beállítja a boltban elérhető termékek listáját.
     *
     * @param items a beállítandó terméklista
     */
    public void setItems(List<ShopItem> items) {
        this.items = items;
    }


    /**
     * Ellenőrzi, hogy a megadott Takarító pénztárcájában rendelkezésre áll-e a 
     * kiválasztott tétel megvásárlásához szükséges pénzügyi fedezet.
     *
     * @param cleaner A vásárlást kezdeményező Takarító (Cleaner) objektum.
     * @param item    A megvásárolni kívánt felszerelés vagy erőforrás (ShopItem).
     * @return        Igaz (true), ha a Takarító rendelkezik elegendő fedezettel, egyébként hamis (false).
     *
     * Pszeudokód:
     * 1. Lekéri a takarító pénztárcájának egyenlegét.
     * 2. Összehasonlítja a kiválasztott termék árával.
     * 3. Visszatérési érték: true/false.
     */
    public boolean canPurchase(Cleaner cleaner, ShopItem item) {
        if (cleaner == null || item == null || cleaner.getWallet() == null) {
            return false;
        }
        if (!items.contains(item)) {
            return false;
        }

        return cleaner.getWallet().getAmount() >= getPrice(item);
    }

    /**
     * Megkísérli végrehajtani a vásárlási tranzakciót. A teljes játékban ez a metódus 
     * ellenőrzi a fedezetet, és sikeres tranzakció esetén levonja az összeget a Takarító 
     * pénztárcájából (Wallet).
     *
     * @param cleaner A vásárlást végrehajtani próbáló Takarító (Cleaner) objektum.
     * @param item    A megvásárolni kívánt felszerelés vagy erőforrás (ShopItem).
     * @return        Igaz (true), ha a tranzakció sikeres volt és az összeg levonásra került, egyébként hamis (false).
     *
     * Pszeudokód:
     * 1. Meghívja a canPurchase metódust.
     * 2. Ha true, levonja a vételárat és sikeresen visszatér.
     * 3. Ha false, sikertelen vásárlással tér vissza.
     */
    public boolean tryPurchase(Cleaner cleaner, ShopItem item) {
        if (!canPurchase(cleaner, item)) {
            return false;
        }

        cleaner.getWallet().spend(getPrice(item));
        return true;
    }

    private int getPrice(ShopItem item) {
        switch (item) {
            case Salt:
                return GameRules.PRICE_SALT;
            case Gravel:
                return GameRules.PRICE_GRAVEL;
            case Biokerosene:
                return GameRules.PRICE_BIOKEROSENE;
            case SweeperPlow:
                return GameRules.PRICE_SWEEPER_PLOW;
            case GravelPlow:
                return GameRules.PRICE_GRAVEL_PLOW;
            case DumpPlow:
                return GameRules.PRICE_DUMP_PLOW;
            case IcebreakerPlow:
                return GameRules.PRICE_ICEBREAKER_PLOW;
            case SaltPlow:
                return GameRules.PRICE_SALT_PLOW;
            case DragonPlow:
                return GameRules.PRICE_DRAGON_PLOW;
            case Snowplow:
                return GameRules.PRICE_SNOWPLOW;
            default:
                return Integer.MAX_VALUE;
        }
    }
    
    /**
     * Az objektum aktuális állapotának és adatainak kiírása.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry Az objektumtár.
     */
    @Override
    public void printData(String id, cli.ObjectRegistry registry) {
        System.out.println("Shop," + id);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(items.get(i).name());
        }
        sb.append("]");
        System.out.println("items," + sb.toString());
    }
}
