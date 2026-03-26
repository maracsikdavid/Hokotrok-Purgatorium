package tests;

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
    }
}
