package core;

import actors.Cleaner;

/**
 * Itt tudnak a Takarítók (Cleaner) az összegyűjtött pénzükből (Wallet) új felszereléseket, 
 * különböző kotrófejeket (Plow) vagy üzemanyagot/sót vásárolni.
 */
public class Shop {
    /**
     * Ellenőrzi, hogy a megadott Takarító pénztárcájában rendelkezésre áll-e a 
     * kiválasztott tétel megvásárlásához szükséges pénzügyi fedezet.
     *
     * @param cleaner A vásárlást kezdeményező Takarító (Cleaner) objektum.
     * @param item    A megvásárolni kívánt felszerelés vagy erőforrás (ShopItem).
     * @return        Igaz (true), ha a Takarító rendelkezik elegendő fedezettel, egyébként hamis (false).
     */
    public boolean canPurchase(Cleaner cleaner, ShopItem item) {
        return false;
    }

    /**
     * Megkísérli végrehajtani a vásárlási tranzakciót. A teljes játékban ez a metódus 
     * ellenőrzi a fedezetet, és sikeres tranzakció esetén levonja az összeget a Takarító 
     * pénztárcájából (Wallet).
     *
     * @param cleaner A vásárlást végrehajtani próbáló Takarító (Cleaner) objektum.
     * @param item    A megvásárolni kívánt felszerelés vagy erőforrás (ShopItem).
     * @return        Igaz (true), ha a tranzakció sikeres volt és az összeg levonásra került, egyébként hamis (false).
     */
    public boolean tryPurchase(Cleaner cleaner, ShopItem item) {
        return false;
    }
}