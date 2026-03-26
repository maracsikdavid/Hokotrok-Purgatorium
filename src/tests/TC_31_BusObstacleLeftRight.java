package tests;

/**
 * TC_31: Busz elakadása – mindkét oldal blokkolt.
 * 
 * Use-case neve: TC_31_BUS_OBSTACLE_LEFT_RIGHT
 * 
 * Rövid leírás:
 * Ugyanaz a szituáció, mint a TC_29 vagy a TC_30, de a jármű mindkét oldala blokkolt. 
 * Emiatt nem tud sávot váltani.
 * 
 * Aktorok:
 * Tesztelő
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
public class TC_31_BusObstacleLeftRight extends TestCase {
    @Override
    public void run() {
    }
}
