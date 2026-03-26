package tests;

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
    /**
     * A teszteset futtatása. Busz pont-szerzésének szimulálása.
     */
    @Override
    public void run() {
    }
}
