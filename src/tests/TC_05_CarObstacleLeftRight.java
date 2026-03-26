package tests;

/**
 * TC_05: Jármű akadályellenes manőver (bal és jobb oldal egyszerre).
 * 
 * Use-case neve: TC_05_CAR_OBSTACLE_LEFT_RIGHT
 * 
 * Rövid leírás:
 * Ugyanaz a szituáció, mint a TC_03 vagy a TC_04, de a jármű mindkét oldala blokkolt. Emiatt nem tud sávot váltani.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű automatikusan lekéri a bal oldali sávot (getLeftLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a bal oldali sáv? (1: Igen, 0: Nem)" → Válasz: 0.
 * 7. A Jármű automatikusan lekéri a jobb oldali sávot (getRightLane()).
 * 8. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)" → Válasz: 0.
 * 9. A sávváltás meghiúsul. A Jármű a jelenlegi sávján marad, és lefut az elakadás/bénulás logikája (stuck()).

 */
public class TC_05_CarObstacleLeftRight extends TestCase {
    /**
     * A teszteset futtatása. Jármű akadály-elkerülésének szimulálása (minden irány blokkolva).
     */
    @Override
    public void run() {
    }
}
