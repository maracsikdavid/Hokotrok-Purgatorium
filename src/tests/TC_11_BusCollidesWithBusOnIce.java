package tests;

/**
 * TC_11: Busz ütközik busszal jégen.
 * 
 * Use-case neve: TC_11_BUS_COLLIDES_WITH_BUS_ON_ICE
 * 
 * Rövid leírás:
 * Egy Busz rálép egy jeges sávra, 20% esélyjel megcsúszik, és (2 tick ideig) megbénul a vele azonos sávon levő busszal.

 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a buszt (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a busz a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Buszon meghívódik a paralyze(time). A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_11_BusCollidesWithBusOnIce extends TestCase {
    /**
     * A teszteset futtatása. Busz és busz ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
    }
}
