package statemachine;
import core.GameRules;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) vékony hóval borított állapotát reprezentáló osztály.
 * Ez egy átmeneti, instabil állapot: ha a havazás tovább folytatódik (5 tick), a hóréteg 
 * vastag hóvá (ThickSnowCondition) alakul. Ha viszont a sűrű forgalom (20 jármű) letapossa, 
 * az útfelület jéggé (IceCondition) változik. A takarító járművek sószóró (SaltPlow) vagy normál 
 * kotrófej segítségével képesek megtisztítani.
 */
public class ThinSnowCondition implements LaneCondition {
    /** @return Mindig igaz, ez az állapot vékony hó. */
    @Override public boolean isThinSnow() { return true; }
    private int saltTimer = -1;
    private int trampleCounter = 0;
    private int snowTicks = 0;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public ThinSnowCondition() {
    }

    /**
     * Paraméteres konstruktor a só hatásának aktivációs idejével és a letaposás számlálójával.
     *
     * @param saltTimer      a só hatásának aktivációs ideje tickekben
     * @param trampleCounter a letaposott járművek száma
     */
    public ThinSnowCondition(int saltTimer, int trampleCounter) {
        this.saltTimer = saltTimer;
        this.trampleCounter = trampleCounter;
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

    /**
     * Visszaadja a letaposási számlálót.
     *
     * @return a sávon áthaladt járművek száma
     */
    public int getTrampleCounter() {
        return trampleCounter;
    }

    /**
     * Beállítja a letaposási számlálót.
     *
     * @param trampleCounter a beállítandó áthaladási szám
     */
    public void setTrampleCounter(int trampleCounter) {
        this.trampleCounter = trampleCounter;
    }


    // --- METÓDUSOK ---

    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. Vékony hó esetén itt kezelhető az idő múlásával járó folyamat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük.
     */
    @Override
    public void tick(Lane lane) {
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // Alagútban nincs változás
        }

        if (saltTimer > 0) {
            saltTimer--;
            if (saltTimer == 0) {
                lane.setState(new CleanCondition(0, GameRules.SALT_SNOW_IMMUNITY_TICKS));
            }
            return;
        }

        snowTicks++;
        if (snowTicks >= GameRules.THICK_SNOW_AFTER_ADDITIONAL_TICKS) {
            lane.setState(new ThickSnowCondition());
        }
    }

    /**
     * Havazás (csapadék) éri a vékony hóval borított sávot. Ha a folyamatos 
     * havazás eléri a küszöbértéket, a sáv állapota vastag hóra 
     * (ThickSnowCondition) módosulhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a hó esik.
     */
    @Override
    public void addSnow(Lane lane) {
        if (saltTimer > 0) {
            return;
        }

        snowTicks++;
        if (snowTicks >= GameRules.THICK_SNOW_AFTER_ADDITIONAL_TICKS) {
            lane.setState(new ThickSnowCondition());
        }
    }

    /**
     * A sávon áthaladó járművek letapossák a vékony havat. Bizonyos számú 
     * áthaladás után a felület jégpáncéllá (IceCondition) alakulhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyet a járművek letaposnak.
     */
    public void trample(Lane lane) {
        trampleCounter++;
        if (trampleCounter >= GameRules.ICE_TRAMPLE_THRESHOLD) {
            lane.setState(new IceCondition());
        }
    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a vékony hótakaróra. 
     * Ennek hatására a hó elolvad, és az állapot tiszta (CleanCondition) lesz.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a sót szórják.
     */
    @Override
    public void applySalt(Lane lane) {
        saltTimer = GameRules.SALT_ACTIVATION_TICKS;
    }

    /**
     * Kavics szórása vékony hóval borított sávra. Ennek az állapotban nincs közvetlen hatása.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {
        // Vékony havon a zúzalék nem értelmezett állapotváltás.
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a vékony hóval borított sávra. 
     * Az áthaladás növeli a letaposási számlálót.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a jármű rálép.
     * @param v    Az a jármű, amelyik a sávra érkezik.
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle v) {
        if (v != null && v.isParalizable()) {
            trample(lane);
        }
        if (lane.getState().isIce() && v != null && v.isParalizable()) {
            v.paralyze(GameRules.COLLISION_PARALYZE_TICKS);
        }
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