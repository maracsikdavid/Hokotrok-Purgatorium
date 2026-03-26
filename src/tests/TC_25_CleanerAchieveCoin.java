package tests;

/**
 * TC_25: Takarító érméket szerez (takarítás jutalmazása).
 * 
 * Use-case neve: TC_25_CLEANER_ACHIEVE_COIN
 * 
 * Rövid leírás:
 * A Hókotró sikeresen letakarít egy problémás (havas vagy jeges) sávot, amiért pénz kap.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A globális tick() hatására a Hókotró végrehajtja a mozgást (move()), majd elindítja a takarítást a clearLane() metódussal.
 * 4. A Szkeleton megkérdezi a Tesztelőt: "Sikeres volt a takarítás? (1: Igen, 0: Nem)" → Válasz: 1.
 * 5. A kotrófej CleanCondition-re módosítja a sáv állapotát, a clearLane() pedig true értékkel tér vissza.
 * 6. A sikeres takarítás hatására a Hókotró meghívja az achieveCoin() metódust a tulajdonosán (Cleaner).
 * 7. A Takarító pénztárcájában (Wallet) megnő az összeg az add(amount) metódus lefutásával.
 */
public class TC_25_CleanerAchieveCoin extends TestCase {
    /**
     * A teszteset futtatása. Takarító érme-szerzésének szimulálása.
     */
    @Override
    public void run() {
    }
}
