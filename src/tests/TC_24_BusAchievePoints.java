package tests;

import actors.BusDriver;
import core.Skeleton;
import entities.Bus;
import statemachine.CleanCondition;
import topology.BusStop;
import topology.Lane;
import topology.SimpleRoad;

/**
 * TC_24: Busz pontokat szerez (cél elérési jutalmazás).
 * 
 * Use-case neve: TC_24_BUS_ACHIEVE_POINTS
 * 
 * Rövid leírás:
 * Egy Busz a haladása során eléri a sáv végét, majd sikeresen megérkezik a célállomáshoz ezér a buszsofőr pontot kap.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A globális tick() hatására a Buszon lefut a move() metódus.
 * 4. A Busz eléri a sáv végét (progress >= length), majd lekéri a soron következő csomópontot az úttól (getTargetNode()).
 * 5. A Szkeleton megkérdezi a Tesztelőt: "A jelenlegi csomópont egyenlő a busz célcsomópontjával? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A feltétel teljesülése miatt a Busz meghívja a BusDriver objektumon az achievePoints() metódust.
 */
public class TC_24_BusAchievePoints extends TestCase {
    @Override
    public void run() {
        // =====================================================================
        // 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA
        // =====================================================================
        Skeleton.setActiveTestCaseId(24);
        Skeleton.disableLogging();

        BusStop startNode = new BusStop();
        Skeleton.registerObject(startNode, "startNode");

        BusStop endNode = new BusStop();
        Skeleton.registerObject(endNode, "endNode");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        CleanCondition cond = new CleanCondition();
        Skeleton.registerObject(cond, "cond");

        Lane currentLane = new Lane();
        Skeleton.registerObject(currentLane, "currentLane");

        Lane nextLane = new Lane();
        Skeleton.registerObject(nextLane, "nextLane");

        Bus b = new Bus();
        Skeleton.registerObject(b, "b");

        BusDriver bd = new BusDriver();
        Skeleton.registerObject(bd, "bd");

        // =====================================================================
        // 2. KAPCSOLATOK BEÁLLÍTÁSA
        // =====================================================================
        startNode.addOutgoingRoad(r);
        r.setTargetNode(endNode);
        r.addLane(nextLane);
        nextLane.changeCondition(cond);

        b.setStartNode(startNode);
        b.setEndNode(endNode);
        b.setDriver(bd);
        bd.setManagedBus(b);

        b.setCurrentLane(currentLane);
        b.setTargetLane(nextLane);
        currentLane.getVehicles().add(b);

        // =====================================================================
        // 3. A SZEKVENCIA ELINDÍTÁSA
        // =====================================================================
        Skeleton.enableLogging();
        b.tick();

        Skeleton.setActiveTestCaseId(-1);
    }
}