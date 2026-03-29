package tests;

import entities.Car;
import topology.Intersection;
import topology.Lane;
import topology.SimpleRoad;
import core.*;
import entities.*;

/**
 * TC_27: Busz normál mozgása a sávjában.
 * 
 * Use-case neve: TC_27_BUS_MOVE_NORMAL
 * 
 * Rövid leírás:
 * Egy busz halad a sávjában, a globális tick() hatására a pozíciója (progress) frissül, 
 * de még nem éri el a sáv végét, így a sávon marad.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. A busz meghívja a saját belső, védett move() metódusát.
 * 5. A Skeleton megkérdezi a Tesztelőt: "Elérte a busz a sáv végét? (1: Igen, 0: Nem)" → Válasz: 0.
 * 6. A busz progress attribútuma megnő, a jármű a sávon marad, sávváltás nem történik.
 * 7. A teszt véget ér, a Skeleton kilogolja az eredményt.
 */
public class TC_27_BusMoveNormal extends TestCase {
    @Override
    public void run() {
        Skeleton.setActiveTestCaseId(27);
        Skeleton.disableLogging();
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        Bus b = new Bus();
        Skeleton.registerObject(b, "b");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA (INICIALIZÁLÁS) ===
        i1.addOutgoingRoad(r);
        r.setTargetNode(i2);
        r.addLane(l);
        l.acceptVehicle(b);
        b.setCurrentLane(l);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        // A Skeleton (vagy Tester) meghívja a jármű tick() metódusát 
        Skeleton.enableLogging();
        // A kérdés csak TC_01 esetén jelenjen meg a move() híváson belül
       
        b.tick();
        Skeleton.setActiveTestCaseId(-1);
        
    }
}
