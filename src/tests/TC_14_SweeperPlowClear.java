package tests;

/**
 * TC_14: Söprő fej takarítóművelete.
 * 
 * Use-case neve: TC_14_SWEEPER_PLOW_CLEAR
 * 
 * Rövid leírás:
 * Söprőfejjel felszerelt hókotró takarít egy sávot. A hó átkerül a szomszédos sávba.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A tick() során meghívódik a clearLane().
 * 4. A SweeperPlow lekéri a jobb oldali sávot (getRightLane()).
 * 5. A hókotró sávja CleanCondition-re vált.
 * 6. A szomszédos sáv állapota kap egy changeCondition() behatást. 
        (Havas lesz a sáv és ennek mértéke attól függ hogy a lesöpört sávon vékony vagy vastag hó volt.)
 */
public class TC_14_SweeperPlowClear extends TestCase {
    /**
     * A teszteset futtatása. Söprő fej takarítóművelete szimulálása.
     */
    @Override
    public void run() {
    }
}
