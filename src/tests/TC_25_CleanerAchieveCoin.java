package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.DumpPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_25: Takarító érméket szerez (takarítás jutalmazása).
 * 
 * Use-case neve: TC_25_CLEANER_ACHIEVE_COIN
 * 
 * Rövid leírás:
 * A Hókotró sikeresen letakarít egy problémás (havas vagy jeges) sávot, amiért pénz kap.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A globális tick() hatására a Hókotró végrehajtja a mozgást (move()), majd elindítja a takarítást a clearLane() metódussal.
 * 4. A Szkeleton megkérdezi a Tesztelőt: "Sikeres volt a takarítás? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A kotrófej CleanCondition-re módosítja a sáv állapotát, a clearLane() pedig true értékkel tér vissza.
 * 6. A sikeres takarítás hatására a Hókotró meghívja az achieveCoin() metódust a tulajdonosán (Cleaner).
 * 7. A Takarító pénztárcájában (Wallet) megnő az összeg az add(amount) metódus lefutásával.
 */
public class TC_25_CleanerAchieveCoin extends TestCase {
    @Override
    public void run() {
        // =====================================================================
        // 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA
        // =====================================================================
        Skeleton.setActiveTestCaseId(25);
        Skeleton.disableLogging();

        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        // A sáv jégpáncélos állapotú
        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        DumpPlow p = new DumpPlow();
        Skeleton.registerObject(p, "p");

        Cleaner c = new Cleaner();
        Skeleton.registerObject(c, "c");

        Wallet w = c.getWallet();
        Skeleton.registerObject(w, "w");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // =====================================================================
        // 2. KAPCSOLATOK BEÁLLÍTÁSA
        // =====================================================================
        i1.addOutgoingRoad(r);
        r.setTargetNode(i2);
        r.addLane(l);
        l.changeCondition(cond);

        sp.setOwner(c);
        sp.setCurrentLane(l);
        sp.equipPlow(p);
        l.acceptVehicle(sp);

        // =====================================================================
        // 3. A SZEKVENCIA ELINDÍTÁSA
        // =====================================================================
        Skeleton.enableLogging();
        sp.tick();

        Skeleton.setActiveTestCaseId(-1);
    }
}
