package tests;
import core.Skeleton;
import topology.Intersection;
import topology.Lane;
import topology.Road;
import topology.SimpleRoad;
import entities.Car;


/**
 * TC_01: Autó normális mozgása.
 * 
 * Use-case neve: TC_01_CAR_MOVE_NORMAL
 * 
 * Rövid leírás:
 * Egy autó halad a sávjában, a globális tick() hatására a pozíciója (progress) frissül, 
 * de még nem éri el a sáv végét, így a sávon marad.
 * 
 * Forgatókönyv:
 * 1.A Tesztelő elindítja a tesztet.
 * 2.A Szkeleton inicializál egy teszt pályát.
 * 3.A Skeleton meghívja a jármű tick() metódusát.
 * 4.Az Autó meghívja a saját belső, védett move() metódusát.
 * 5.A Skeleton megkérdezi a Tesztelőt: "Elérte az autó a sáv végét? (1: Igen, 0: Nem)" → Válasz: 0.
 * 6.Az Autó progress attribútuma megnő, a jármű a sávon marad, sávváltás nem történik.
 * 7.A teszt véget ér, a Skeleton kilogolja az eredményt.

 */
public class TC_01_CarMoveNormal extends TestCase {
    /**
     * A teszteset futtatása. Autó normális mozgásának szimulálása.
     */
    @Override
    public void run() {
        // === 1. OBJEKTUMOK LÉTREHOZÁSA ÉS REGISZTRÁCIÓJA ===
        Skeleton.setActiveTestCaseId(1);
        Skeleton.disableLogging();
        Intersection i1 = new Intersection();
        Skeleton.registerObject(i1, "i1");

        Intersection i2 = new Intersection();
        Skeleton.registerObject(i2, "i2");

        SimpleRoad r = new SimpleRoad();
        Skeleton.registerObject(r, "r");

        Lane l = new Lane();
        Skeleton.registerObject(l, "l");

        Car c = new Car();
        Skeleton.registerObject(c, "c");

        // === 2. KAPCSOLATOK BEÁLLÍTÁSA (INICIALIZÁLÁS) ===
        i1.addOutgoingRoad(r);
        r.setTargetNode(i2);
        r.addLane(l);
        l.acceptVehicle(c);
        c.setCurrentLane(l);

        // === 3. A SZEKVENCIA ELINDÍTÁSA ===
        // A Skeleton (vagy Tester) meghívja a jármű tick() metódusát 
        Skeleton.enableLogging();
        // A kérdés csak TC_01 esetén jelenjen meg a move() híváson belül
        Skeleton.setActiveTestCaseId(1);
        c.tick();
        Skeleton.setActiveTestCaseId(-1);
        
    
        
    }
}
