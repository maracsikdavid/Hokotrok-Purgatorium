package tests;

import actors.Cleaner;
import core.Skeleton;
import core.Wallet;
import entities.Snowplow;
import equipments.Salt;
import equipments.SaltPlow;
import statemachine.IceCondition;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_20: Só aktiválódása (Salt Activation).
 * 
 * Use-case neve: TC_20_SALT_ACTIVATION
 * 
 * Rövid leírás:
 * A besózott jég a megadott idő (2 tick) elteltével feloldódik, és a sáv megtisztul.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy előre besózott jeges teszt pályát (a TC_19 alapján).
 * 3. Eltelik 1 tick (a számláló csökken).
 * 4. Eltelik a 2. tick (a számláló lejár, a jég elolvad, a sáv tiszta lesz).
 */
public class TC_20_SaltActivation extends TestCase {

    @Override
    public void run() {
        Skeleton.disableLogging();
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

        Wallet wallet = owner.getWallet();
        Skeleton.registerObject(wallet, "wallet");

        Salt saltSource = new Salt();
        Skeleton.registerObject(saltSource, "salt");

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
        p.setSaltSource(saltSource); // Or however it's given the salt
        sp.setCurrentLane(l1);
        sp.equipPlow(p);
        l1.acceptVehicle(sp);

        // A sáv előzetes besózása a naplózás engedélyezése előtt
        cond.applySalt(l1);

        Skeleton.enableLogging();
        // === 3. SZEKVENCIA ELINDÍTÁSA ===
        // Aktiváció és olvadás (saltplow_success_activation.txt)
        l1.tick(); // Első tick: a számláló 1-re csökken
        l1.tick(); // Második tick: a számláló 0 lesz -> új CleanCondition jön létre!
    }
}
