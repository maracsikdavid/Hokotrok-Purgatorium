package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.Biokerosene;
import equipments.DragonPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_17: DragonPlow sikeresen működik (tüzével való jégolvasztás).
 * 
 * Use-case neve: TC_17_DRAGON_PLOW_SUCCESS
 * 
 * Rövid leírás:
 * Sárkányfejjel takarít a hókotró, van elég biokerozin.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefut.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van elég biokerozin a tartályban? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Biokerozin use() metódusa lefut. A sáv CleanCondition-re vált.
 */
public class TC_17_DragonPlowSuccess extends TestCase {
    /**
     * A teszteset futtatása. DragonPlow sikeres működésének szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(17);
        Skeleton.disableLogging();

        Intersection n1 = new Intersection();
        Skeleton.registerObject(n1, "n1");

        Intersection n2 = new Intersection();
        Skeleton.registerObject(n2, "n2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane currentLane = new Lane();
        Skeleton.registerObject(currentLane, "currentLane");

        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        DragonPlow p = new DragonPlow();
        Skeleton.registerObject(p, "p");

        Biokerosene b = new Biokerosene();
        Skeleton.registerObject(b, "b");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet wallet = c.getWallet();
        Skeleton.registerObject(wallet, "wallet");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        n1.addOutgoingRoad(r);
        r.setTargetNode(n2);
        r.addLane(currentLane);
        currentLane.changeCondition(cond);

        sp.setOwner(c);
        sp.setCurrentLane(currentLane);
        
        
        p.setFuelSource(b);
        sp.equipPlow(p);
        
        currentLane.acceptVehicle(sp);
    
        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.enableLogging();
        sp.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
