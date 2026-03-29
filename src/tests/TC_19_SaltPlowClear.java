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
 * TC_19: SaltPlow Clear (só szórása: esettől függően vagy sikerül, vagy nincs só).
 * 
 * Use-case neve: TC_19_SALT_PLOW_CLEAR
 * 
 * Rövid leírás:
 * Sószórófejes hókotró próbál sót szórni a jégre. A felhasználó adja meg, hogy van-e só.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() indul.
 * 4. A Skeleton kérdezi: "Van-e só a tartályban? (1: Igen, 0: Nem)".
 * 5. Ha van só, a Só use() hívása lefut, a sáv megkapja az applySalt() üzenetet. Ha nincs, a takarítás megszakad.
 */
public class TC_19_SaltPlowClear extends TestCase {

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
        p.setSaltSource(saltSource);
        sp.setCurrentLane(l1);
        sp.equipPlow(p);
        l1.acceptVehicle(sp);

        Skeleton.enableLogging();
        // === 3. SZEKVENCIA ELINDÍTÁSA ===
        Skeleton.setActiveTestCaseId(19);
        sp.tick();
        Skeleton.setActiveTestCaseId(-1);
    }
}
