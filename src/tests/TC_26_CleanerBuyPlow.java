package tests;

/**
 * TC_26: Takarító kotrófejet vásárol.
 * 
 * Use-case neve: TC_26_CLEANER_BUY_PLOW
 * 
 * Rövid leírás:
 * A Takarító egy új kotrófejet (pl. SweeperPlow vagy DragonPlow) vásárol a boltban. 
 * Mivel rendelkezik a szükséges pénzügyi fedezettel, a tranzakció sikeres, 
 * és az új eszköz a birtokába kerül.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Takarító meghívja a buyItem() metódust a kiválasztott kotrófejre (pl. ShopItem.SweeperPlow).
 * 4. A folyamat átkerül a Shop osztály tryPurchase() metódusába.
 * 5. A Szkeleton megkérdezi a Tesztelőt: "Van elég pénz a pénztárcában (Wallet)? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Shop levonja az összeget a Wallet spend(amount) meghívásával.
 * 7. A tranzakció sikerrel zárul (true), az új kotrófej (Plow) objektum létrejön, 
 *    és bekerül a Takarító birtokába (eszköztárába), amelyet később felszerelhet a hókotrójára.
 */
public class TC_26_CleanerBuyPlow extends TestCase {
    @Override
    public void run() {
    }
}
