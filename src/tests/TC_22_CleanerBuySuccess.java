package tests;

/**
 * TC_22: Takarító vásárlása sikeres (elég pénz van).
 * 
 * Use-case neve: TC_22_CLEANER_BUY_SUCCESS
 * 
 * Rövid leírás:
 * A Takarító sikeresen új felszerelést (például sót a sószórójába) vásárol a boltban, 
 * mivel rendelkezik a tranzakcióhoz szükséges pénzügyi fedezettel.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Takarító meghívja a buyItem() metódust a kiválasztott elemre.
 * 4. A folyamat átkerül a Shop osztály tryPurchase() metódusába.
 * 5. A Szkeleton megkérdezi a Tesztelőt: "Van elég pénz a pénztárcában (Wallet)? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Shop levonja az összeget a Wallet.spend(amount) meghívásával.
 * 7. A tranzakció sikerrel zárul (true), a megvásárolt eszköz betöltésre kerül a hókotróba (lefut a refill() metódus).
 */
public class TC_22_CleanerBuySuccess extends TestCase {
    /**
     * A teszteset futtatása. Sikeres vásárlás szimulálása.
     */
    @Override
    public void run() {
    }
}
