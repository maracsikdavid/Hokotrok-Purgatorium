package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.SaltPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_20: SaltPlow üres.
 * 
 * Use-case neve: TC_20_SALT_PLOW_EMPTY
 * 
 * Rövid leírás:
 * Sószórófejes hókotró próbál takarítani, de nincs sója.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefutásakor a Skeleton megkérdezi: "Van-e só tartályban? (1: Igen, 0: Nem)" → Válasz: 0.
 * 4. A takarítás megszakad, visszatérési érték false, a sáv állapota érintetlen marad.
 */
public class TC_20_SaltPlowEmpty extends TestCase {

    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l1 = new Lane();
        Skeleton.registerObject(l1, "l1");

        IceCondition cond = new IceCondition();
        Skeleton.registerObject(cond, "cond");

        Cleaner owner = new Cleaner();
        Skeleton.registerObject(owner, "owner");
        
        Wallet w = owner.getWallet();
        Skeleton.registerObject(w, "w");

        SaltPlow p = new SaltPlow();
        Skeleton.registerObject(p, "p");

        Snowplow sp = new Snowplow();
        Skeleton.registerObject(sp, "sp");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA ===
        r.setTargetNode(i2);
        i1.addOutgoingRoad(r);
        r.addLane(l1);
        l1.changeCondition(cond);

        sp.setOwner(owner);
        sp.setCurrentLane(l1);
        sp.equipPlow(p);
        l1.acceptVehicle(sp);

        // === 3. SZEKVENCIA ELINDÍTÁSA ===
        sp.tick();
    }
}
