package tests;

/**
 * TC_20: SaltPlow üres.
 * 
 * Use-case neve: TC_20_SALT_PLOW_EMPTY
 * 
 * Rövid leírás:
 * Sószórófejes hókotró próbál takarítani, de nincs sója.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() lefutásakor a Skeleton megkérdezi: "Van-e só tartályban? (1: Igen, 0: Nem)" → Válasz: 0.
 * 4. A takarítás megszakad, visszatérési érték false, a sáv állapota érintetlen marad.
 */
public class TC_20_SaltPlowEmpty extends TestCase {
    /**
     * A teszteset futtatása. SaltPlow üres (só nélküli) működésének szimulálása.
     */
    @Override
    public void run() {
    }
}
