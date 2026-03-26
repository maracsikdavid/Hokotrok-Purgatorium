package tests;

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
    }
}
