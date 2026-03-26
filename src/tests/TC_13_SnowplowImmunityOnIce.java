package tests;

/**
 * TC_13: Hóeki immunitása a jégen (isParalizable() = false).
 * 
 * Use-case neve: TC_13_SNOWPLOW_IMMUNITY_ON_ICE
 * 
 * Rövid leírás:
 * Egy hókotró rálép egy jeges sávra, de speciális felépítése miatt immunis a jégre, nem csúszik meg.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A sáv acceptVehicle érzékeli a hókotrót.
 * 4. A hókotró isParalizable() metódusa false-szal tér vissza.
 * 5. A jármű nem kap paralyze hívást, normálisan folytatja a mozgását.
 */
public class TC_13_SnowplowImmunityOnIce extends TestCase {
    /**
     * A teszteset futtatása. Hóeki immunitásának szimulálása jégen.
     */
    @Override
    public void run() {
    }
}
