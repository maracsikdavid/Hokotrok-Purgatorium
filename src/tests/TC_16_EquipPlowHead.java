package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.DragonPlow;
import equipments.DumpPlow;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_16: Kotrófej felszerelése a hóekire.
 * 
 * Use-case neve: TC_16_EQUIP_PLOW_HEAD
 * 
 * Rövid leírás:
 * A Takarító lecseréli a hókotróján lévő aktuális kotrófejet egy másikra, ami a birtokában van (pl. Sárkányfejre).
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Takarító meghívja a saját changePlowHead(snowplow, dragonPlow) metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "A takarító birtokában van a kiválasztott kotrófej? (1: Igen, 0: Nem)" → Válasz: 1.

 */
public class TC_16_EquipPlowHead extends TestCase {
    /**
     * A teszteset futtatása. Kotrófej felszerelésének szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(16);
        Skeleton.disableLogging();

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        DragonPlow dp = new DragonPlow();
        Skeleton.registerObject(dp, "dp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        sp.setOwner(c);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        
        
        c.changePlowHead(sp, dp);

        Skeleton.setActiveTestCaseId(-1);
    }
}
