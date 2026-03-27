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
        Skeleton.printCall(null, this, "canPurchase");
        boolean result = Skeleton.getIntFromUser("Can purchase? (1:Yes, 0:No)") == 1;
        Skeleton.printReturn(this, "canPurchase", String.valueOf(result));
        return result;
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
        Skeleton.printCall(null, this, "tryPurchase");
        boolean result = Skeleton.getIntFromUser("Try purchase successful? (1:Yes, 0:No)") == 1;
        Skeleton.printReturn(this, "tryPurchase", String.valueOf(result));
        return result;
    }
}