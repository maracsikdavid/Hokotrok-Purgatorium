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
        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet wallet = c.getWallet();
        Skeleton.registerObject(wallet, "wallet");

        DumpPlow p1 = new DumpPlow();
        Skeleton.registerObject(p1, "p1");

        DragonPlow p2 = new DragonPlow();
        Skeleton.registerObject(p2, "p2");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===     
        r.setTargetNode(n2);
        n1.addOutgoingRoad(r);
        r.addLane(l);
        //r.getLanes().add(l)
        c.setWallet(wallet);
        sp.setOwner(c);
        sp.equipPlow(p1);
        //p2-vel mi legyen?
        l.acceptVehicle(sp);
        sp.setCurrentLane(l);

        // === 3. MŰVELET VÉGREHAJTÁSA ===
        c.changePlowHead(sp, p2);
    }
}
