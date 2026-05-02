package statemachine;
import core.GameRules;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) vastag hóval (mély hóréteggel) borított állapotát reprezentáló osztály.
 * Ebben az állapotban az útfelület normál közlekedésre gyakorlatilag alkalmatlan.
 * A hagyományos járművek (Autó, Busz) elakadhatnak vagy mozgásképtelenné (lebénulttá) válhatnak rajta.
 * A Hókotrók (Snowplow) viszont szabadon haladhatnak, és a megfelelő kotrófejjel eltakaríthatják 
 * a havat, illetve a Sószóró (SaltPlow) segítségével feloldhatják ezt az állapotot.
 */
public class ThickSnowCondition implements LaneCondition {
    /** @return Mindig igaz, ez az állapot vastag hó. */
    @Override public boolean isThickSnow() { return true; }
    private int saltTimer = -1;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public ThickSnowCondition() {
    }

    /**
     * Paraméteres konstruktor a só hatásának aktivációs idejével.
     *
     * @param saltTimer a só hatásának aktivációs ideje tickekben
     */
    public ThickSnowCondition(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a sózás időzítőjét.
     *
     * @return a hátralévő tickek száma
     */
    public int getSaltTimer() {
        return saltTimer;
    }

    /**
     * Beállítja a sózás időzítőjét.
     *
     * @param saltTimer a beállítandó tickek száma
     */
    public void setSaltTimer(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- METÓDUSOK ---

    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. Vastag hó esetén itt kezelhető az olvadási folyamat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük.
     */
    @Override
    public void tick(Lane lane) {
        if (saltTimer > 0) {
            saltTimer--;
            if (saltTimer == 0) {
                lane.setState(new CleanCondition(0, GameRules.SALT_SNOW_IMMUNITY_TICKS));
            }
        }
    }

    /**
     * Havazás (csapadék) éri a már vastag hóval borított sávot. Mivel az állapot
     * már elérte a maximális hóvastagságot, további havazás nem változtat az állapoton.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a hó esik.
     */
    @Override
    public void addSnow(Lane lane) {
        // Már vastag hó, nincs állapotváltás
    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a vastag hótakaróra. 
     * Ennek hatására a hó bizonyos idő után elolvad, és az állapot tiszta (CleanCondition) lesz.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a sót szórják.
     */
    @Override
    public void applySalt(Lane lane) {
        saltTimer = GameRules.SALT_ACTIVATION_TICKS;
    }

    /**
     * Kavics szórása vastag hóval borított sávra. Ennek az állapotban nincs közvetlen hatása.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {

    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a vastag hóval borított sávra. 
     * A normál járművek elakadhatnak vagy lebénulhatnak a mély hóban. 
     * A Hókotrók akadálytalanul haladhatnak.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a jármű rálép.
     * @param v    Az a jármű, amelyik a sávra érkezik.
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle v) {

    }

    /**
     * Az objektum állapotának és adatainak kiírása.
     *
     * @param id Az objektum azonosítója.
     * @param registry Az objektumtár.
     */
    public void printData(String id, cli.ObjectRegistry registry) {
        System.out.println(this.getClass().getSimpleName());
    }
}