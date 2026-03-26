package tests;

/**
 * TC_10: Autó ütközik busszal jégen.
 * 
 * Use-case neve: TC_10_CAR_COLLIDES_WITH_BUS_ON_ICE
 * 
 * Rövid leírás:
 * Egy Autó rálép egy jeges sávra, 20% esélyjel megcsúszik, és (2 tick időre) megbénul a vele azonos sávon levő busszal.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a járművet (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a jármű a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A járművön meghívódik a paralyze(time) metódus. A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_10_CarCollidesWithBusOnIce extends TestCase {
    /**
     * A teszteset futtatása. Autó és busz ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
    }
}
