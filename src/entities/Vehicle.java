package entities;

import cli.ObjectRegistry;
import cli.Printable;
import core.ITickable;
import topology.Lane;
import topology.MapNode;
import topology.Road;

/**
 * Az osztály az összes járműtípus absztrakt modelljéért felel. Feladata a sávon belüli pozíció,
 * a haladási irány és a célsáv nyilvántartása. Kezeli a diszkrét időlépésekben történő
 * állapotfrissítést (tick), a helyváltoztatást (move), valamint a sávváltási logikát. Szimulálja
 * továbbá a környezeti hatásokra adott reakciókat, mint például a jeges útfelületen történő
 * lebénulást és az ebből adódó mozgásképtelenséget.
 */
public abstract class Vehicle implements ITickable, Printable {
    protected Lane currentLane;
    protected int progress;
    protected Lane targetLane;
    protected boolean isParalyzed;
    protected int paralysisTimer;


    // --- KONSTRUKTOROK ---

    /**
     * Alap konstruktor.
     */
    protected Vehicle() {
        this.progress = 0;
        this.isParalyzed = false;
        this.paralysisTimer = 0;
    }

    /**
     * Teljes inicializálás.
     * @param currentLane Az aktuális sáv.
     * @param progress A haladás pozíciója a sávon belül.
     * @param targetLane Következő / cél sáv.
     */
    protected Vehicle(Lane currentLane, int progress, Lane targetLane) {
        this.currentLane = currentLane;
        this.progress = progress;
        this.targetLane = targetLane;
        this.isParalyzed = false;
        this.paralysisTimer = 0;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja az aktuális sáv referenciáját.
     *
     * @return Az aktuális sáv, amin a jármű tartózkodik.
     */
    public Lane getCurrentLane() {
        return currentLane;
    }

    /**
     * Beállítja az aktuális sávot.
     *
     * @param currentLane Az új sáv referenciája.
     */
    public void setCurrentLane(Lane currentLane) {
        this.currentLane = currentLane;
    }

    /**
     * Lekérdezi a sávon belüli haladást.
     *
     * @return A haladás mértéke (pozíció) a sávon belül.
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Beállítja a haladást (pl. tick után növelve, vagy sávváltáskor alaphelyzetbe állítva).
     *
     * @param progress Az új haladási érték.
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Megadja a célsávot.
     *
     * @return A sáv, amely felé a jármű haladni kíván.
     */
    public Lane getTargetLane() {
        return targetLane;
    }

    /**
     * Beállítja a célsávot (útvonaltervezés vagy játékos parancs alapján).
     *
     * @param targetLane A beállítandó célsáv.
     */
    public void setTargetLane(Lane targetLane) {
        this.targetLane = targetLane;
    }

    /**
     * Visszaadja, hogy a jármű átmenetileg cselekvőképtelen-e (pl. jégen megcsúszott vagy hóban elakadt).
     *
     * @return Igaz, ha a jármű bénult állapotban van, egyébként hamis.
     */
    public boolean getIsParalyzed() {
        return isParalyzed;
    }

    /**
     * Beállítja a jármű bénultsági állapotát. Szükséges az állapotgép hatásainak érvényesítéséhez, 
     * illetve a tesztmotor link parancsához.
     *
     * @param isParalyzed A beállítandó bénultsági állapot.
     */
    public void setIsParalyzed(boolean isParalyzed) {
        this.isParalyzed = isParalyzed;
    }

    /**
     * Visszaadja a bénultsági időzítőt.
     *
     * @return A hátralévő idő (tickekben) a bénultság megszűnéséig.
     */
    public int getParalysisTimer() {
        return paralysisTimer;
    }

    /**
     * Beállítja a bénultsági időzítőt.
     *
     * @param paralysisTimer A beállítandó időtartam tickekben.
     */
    public void setParalysisTimer(int paralysisTimer) {
        this.paralysisTimer = paralysisTimer;
    }


    // --- METÓDUSOK ---

    /**
     * Az autó időzítés lépése, amely az idő függvényében történő változásokat valósítja meg.
     * Frissíti a bénultsági állapotot és ha a jármű mozgásképes, meghívja a move() metódust.
	 *
	 * Pszeudokód:
	 * 1. Kezeli a bénultsági időzítőt.
	 * 2. Ha mozgásképes, meghívja a move() metódust.
     */
    @Override
    public void tick() {
        if (isParalyzed){
            if(paralysisTimer > 0){
                paralysisTimer--;
                if (paralysisTimer == 0){
                    isParalyzed = false;
                }
            }
            return;
        }
        move();
    }

    /**
     * Az autó mozgatása. Ha nincs bénultság, a progress érték növekszik. 
     * Ha eléri a sáv végét, a jármű megkísérel átlépni a következő sávra.
     */
    protected abstract void move();

    /**
     * Sávváltási kísérlet a megadott célsávra. 
     * Sikeres váltás esetén (ha a cél sáv létezik és a jármű nem bénult) igazzal tér vissza.
     *
     * @param target A célként megjelölt sáv.
     * @return Igaz, ha a sávváltás sikeres volt, egyébként hamis.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi a cél sáv és állapot érvényességét.
	 * 2. Eltávolítja a járművet a jelenlegi sávról.
	 * 3. Regisztrálja a járművet a cél sávon.
     */
    public boolean changeLane(Lane target) {
        if (target == null || isParalyzed || stuck()){
            return false;
        }
        if (currentLane != null){
            currentLane.removeVehicle(this);
        }
        target.acceptVehicle(this);
        this.currentLane = target;
        this.progress = 0;
        return true;
    }

    /**
     * Meghatározza, hogy a járműtípus képes-e a bénulásra.
     *
     * @return Igaz, ha a jármű lebénulhat, egyébként hamis.
     */
    public abstract boolean isParalizable();

    /**
     * A jármű bénult állapotba helyezése megadott ideig.
     *
     * @param time A bénultság időtartama tickekben kifejezve.
	 *
	 * Pszeudokód:
	 * 1. Ellenőrzi, hogy a jármű bénítható-e.
	 * 2. Beállítja a bénultsági állapotot és az időzítőt.
     */
    public void paralyze(int time) {
        if (!this.isParalizable()){
            return;
        }
        if (time <= 0){
            return;
        }
        this.isParalyzed = true;
        this.paralysisTimer = Math.max(this.paralysisTimer, time);
    }
    
    /**
     * Kiválasztja az aktuális haladási iránynak megfelelő következő utat egy csomópontban.
     *
     * @param currentNode Az aktuális csomópont, ahol a döntést meg kell hozni.
     * @return A választott következő út referenciája.
     */
    public abstract Road chooseNextRoad(MapNode currentNode);

    /**
     * Ellenőrzi, hogy a jármű elakadt-e (pl. elzárt útszakasz miatt).
     *
     * @return Igaz, ha a jármű mozgásképtelen vagy elakadt, egyébként hamis.
     */
    public abstract boolean stuck();

    /**
     * Az objektum aktuális állapotának és adatainak kiírása a megadott regiszter segítségével.
     *
     * @param id Az objektum azonosítója a regiszterben.
     * @param registry Az objektumokat nyilvántartó regiszter.
     */
    @Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println(this.getClass().getSimpleName() + "," + id);
        System.out.println("currentLane," + registry.findId(currentLane));
        System.out.println("progress," + progress);
        System.out.println("isParalyzed," + isParalyzed);
    }
}