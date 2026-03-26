package tests;

/**
 * TC_04: Jármű akadályellenes manőver (bal oldal).
 * 
 * Use-case neve: TC_04_CAR_OBSTACLE_LEFT
 * 
 * Rövid leírás:
 * Ugyanaz a szituáció, mint a TC_03, de a jármű a bal oldali sávba sorol át az akadály miatt.
 * 
 * Forgatókönyv:
 * 1. 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű automatikusan lekéri a bal oldali sávot (getLeftLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)" → Válasz: 1.
 * 7. A Jármű megkísérli a sávváltást (changeLane()), sikeresen átkerül a bal sávba.
 */
public class TC_04_CarObstacleLeft extends TestCase {
    /**
     * A teszteset futtatása. Jármű akadály-elkerülésének szimulálása (bal oldal).
     */
    @Override
    public void run() {
    }
}
