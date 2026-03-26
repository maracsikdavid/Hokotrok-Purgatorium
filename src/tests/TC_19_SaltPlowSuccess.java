package tests;

/**
 * TC_19: SaltPlow sikeresen működik (sóval való jég feloldása).
 * 
 * Use-case neve: TC_19_SALT_PLOW_SUCCESS
 * 
 * Rövid leírás:
 * Sószórófejes hókotró szórja a sót a hó/jégre, mivel van elég só.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A clearLane() indul.
 * 4. A Skeleton kérdezi: "Van-e só a tartályban? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A Só use() hívása lefut, a sáv megkapja az applySalt() üzenetet.
 * 6. A jég vagy bármely más típusú sávon lévő csapadék idővel elolvad.(2 tick után a sáv CleanCondition-re vált).
 */
public class TC_19_SaltPlowSuccess extends TestCase {
    /**
     * A teszteset futtatása. SaltPlow sikeres működésének szimulálása.
     */
    @Override
    public void run() {
    }
}
