package statemachine;
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
    private int saltTimer = -1;
    private int trampleCounter = 0;


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

    }

    /**
     * A sávon áthaladó járművek letapossák a vékony havat. Bizonyos számú 
     * áthaladás után a felület jégpáncéllá (IceCondition) alakulhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyet a járművek letaposnak.
     */
    public void trample(Lane lane) {

    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a vékony hótakaróra. 
     * Ennek hatására a hó elolvad, és az állapot tiszta (CleanCondition) lesz.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a sót szórják.
     */
    @Override
    public void applySalt(Lane lane) {

    }

    /**
     * Kavics szórása vékony hóval borított sávra. Ennek az állapotban nincs közvetlen hatása.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {

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