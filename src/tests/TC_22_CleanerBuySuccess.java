package tests;

import actors.Cleaner;
import core.Shop;
import core.ShopItem;
import core.Skeleton;
import core.Wallet;
import equipments.Biokerosene;
import equipments.DragonPlow;
import equipments.Salt;
import equipments.SaltPlow;

/**
 * TC_22: A Takarító sikeresen vásárol utánpótlást (Sót vagy Biokerozint) a Boltban.
 * * Use-case neve: TC_22_CLEANER_BUY_SUCCESS
 * * Rövid leírás:
 * A Takarító sikeresen új felszerelést vásárol a boltban, mivel rendelkezik a 
 * tranzakcióhoz szükséges pénzügyi fedezettel.
 * * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát (Bolt, Takarító, Kotrófej, Utánpótlás).
 * 3. A Takarító meghívja a buyItem() metódust a kiválasztott elemre.
 * 4. A folyamat átkerül a Shop osztály tryPurchase() metódusába.
 * 5. A Szkeleton megkérdezi a Tesztelőt: "Van elég pénz a pénztárcában? (1: Igen, 0: Nem)" -> Válasz: 1.
 * 6. A Shop levonja az összeget a Wallet.spend(amount) meghívásával.
 * 7. A tranzakció sikerrel zárul (true), a megvásárolt eszköz betöltésre kerül a hókotróba.
 */
public class TC_22_CleanerBuySuccess extends TestCase {
    @Override
    public void run() {
        // =====================================================================
        // 0. BIZTONSÁGOS FELHASZNÁLÓI BEMENET (Csak 0 vagy 1)
        // =====================================================================
        int choice = -1;
        while (choice != 0 && choice != 1) {
            choice = Skeleton.getIntFromUser("Melyik felszerelésre fusson a teszt? (0: SaltPlow + Só, 1: DragonPlow + Biokerozin)");
            if (choice != 0 && choice != 1) {
                System.out.println("Hibás bemenet! Kérlek, kizárólag 1-et vagy 0-t adj meg.");
            }
        }

        // =====================================================================
        // 1. KÖZÖS OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA
        // =====================================================================
        Skeleton.setActiveTestCaseId(22);
        Skeleton.disableLogging();

        Shop shop = new Shop();
        Skeleton.registerObject(shop, "shop");

        Wallet w = new Wallet();
        Skeleton.registerObject(w, "w");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");
        c.setWallet(w);

        // =====================================================================
        // 2. OPCIONÁLIS BEÁLLÍTÁSOK A VÁLASZTÁS ALAPJÁN
        // =====================================================================
        if (choice == 0) {
            SaltPlow p = new SaltPlow();
            Skeleton.registerObject(p, "p");
            
            Salt s = new Salt();
            Skeleton.registerObject(s, "s");
            
            c.equipPlow(p);

            // =====================================================================
            // 3. A SZEKVENCIA ELINDÍTÁSA (SÓ)
            // =====================================================================
            Skeleton.enableLogging();
            c.buyItem(shop, ShopItem.Salt);
            
        } else {
            DragonPlow p = new DragonPlow();
            Skeleton.registerObject(p, "p");
            
            Biokerosene b = new Biokerosene();
            Skeleton.registerObject(b, "b");
            
            c.equipPlow(p);

            // =====================================================================
            // 3. A SZEKVENCIA ELINDÍTÁSA (BIOKEROZIN)
            // =====================================================================
            Skeleton.enableLogging();
            // Feltételezve, hogy a ShopItem enumban szerepel a Biokerosene
            c.buyItem(shop, ShopItem.Biokerosene); 
        }

        Skeleton.setActiveTestCaseId(-1);
    }
}