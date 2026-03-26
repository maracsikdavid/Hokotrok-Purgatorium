package tests;

/**
 * TC_17: DragonPlow sikeresen működik (tüzével való jégolvasztás).
 * 
 * Use-case neve: TC_17_DRAGON_PLOW_SUCCESS
 * 
 * Rövid leírás:
 * Sárkányfejjel takarít a hókotró, van elég biokerozin.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefut.
 * 4. A Skeleton megkérdezi a Tesztelőt: "Van elég biokerozin a tartályban? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Biokerozin use() metódusa lefut. A sáv CleanCondition-re vált.
 */
public class TC_17_DragonPlowSuccess extends TestCase {
    /**
     * A teszteset futtatása. DragonPlow sikeres működésének szimulálása.
     */
    @Override
    public void run() {
    }
}
