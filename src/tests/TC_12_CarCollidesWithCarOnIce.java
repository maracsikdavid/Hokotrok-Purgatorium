package tests;

/**
 * TC_12: Autó ütközik autóval jégen.
 * 
 * Use-case neve: TC_12_CAR_COLLIDES_WITH_CAR_ON_ICE
 * 
 * Rövid leírás:
 * Egy Autó rálép egy jeges sávra, megcsúszik, és (2 tick ideig) megbénul a vele azonos sávon levő autóval.


 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a jármű tick() metódusát.
 * 4. Az IceCondition érzékeli a buszt (acceptVehicle).
 * 5. A Skeleton megkérdezi a Tesztelőt: "Megcsúszik és lebénul a busz a jégen? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A Buszon meghívódik a paralyze(time). A jármű 2 tick ideig mozgásképtelen.
 */
public class TC_12_CarCollidesWithCarOnIce extends TestCase {
    /**
     * A teszteset futtatása. Autó és autó ütközésének szimulálása jégen.
     */
    @Override
    public void run() {
    }
}
