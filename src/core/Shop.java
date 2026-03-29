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

        boolean result;

        switch (Skeleton.getActiveTestCaseId()) {
            case 22, 26: {
            int answer = Skeleton.getIntFromUser("Van elég pénz a pénztárcában (Wallet)? (1: Igen, 0: Nem)");
            if (answer == 1) {
                    Wallet w = cleaner.getWallet();
                    if (w != null) {
                        w.spend(1);
                    }
                    result = true;
                } else {
                    result = false;
                }
                break;
            }
            case 23: {
                int answer = Skeleton.getIntFromUser("Van elég pénz a pénztárcában? (1: Yes, 0: No)");
                result = (answer == 1);
                break;
            }
            default: {
                int answer = Skeleton.getIntFromUser("Try purchase successful? (1:Yes, 0:No)");
                result = (answer == 1);
                if (result && cleaner.getWallet() != null) {
                    cleaner.getWallet().spend(1);
                }
                break;
            }
        }

        Skeleton.printReturn(this, "tryPurchase", String.valueOf(result));
        return result;
    }
}