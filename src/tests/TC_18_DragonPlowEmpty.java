package tests;

/**
 * TC_18_DRAGONPLOW_EMPTY
 * 
 * Use-case neve: TC_18_DRAGON_PLOW_EMPTY
 * 
 * Rövid leírás:
 * Sárkányfejes hókotró próbál takarítani, de üres a tank.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefutásakor a Skeleton megkérdezi: "Van biokerozin? (1: Igen, 0: Nem)" → Válasz: 0.
 * 4. A takarítás megszakad, visszatérési érték false, a sáv állapota érintetlen marad.
 */
public class TC_18_DragonPlowEmpty extends TestCase {
    /**
     * A teszteset futtatása. DragonPlow üres működésének szimulálása.
     */
    @Override
    public void run() {
    }
}
