package tests;

/**
 * TC_29: Busz kitérése jobbra akadály esetén.
 * 
 * Use-case neve: TC_29_BUS_OBSTACLE_RIGHT
 * 
 * Rövid leírás:
 * Egy autonóm jármű (Autó vagy Busz) halad a sávjában, de a mozgás (tick()) során 
 * akadályt észlel. A jármű automatikusan megpróbálja kikerülni azt a jobb oldali sávba sorolással.
 * 
 * Aktorok:
 * Tesztelő
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a Jármű tick(), majd move() metódusát.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van akadály a sávban a jármű előtt? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Jármű a belső logikája alapján automatikusan lekéri a jobb oldali sávot (getRightLane()).
 * 6. A Skeleton megkérdezi a Tesztelőt: "Elérhető és üres a jobb oldali sáv? (1: Igen, 0: Nem)" → Válasz: 1.
 * 7. A Jármű megkísérli a sávváltást (changeLane()), a jobb oldali sáv elfogadja, az aktuális eltávolítja.
 */
public class TC_29_BusObstacleRight extends TestCase {
    @Override
    public void run() {
    }
}
