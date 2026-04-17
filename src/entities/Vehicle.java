package entities;
import core.ITickable;
import topology.Lane;

/**
 * Absztrakt alap osztály minden járműhöz. 
 */
public abstract class Vehicle implements ITickable {
    private Lane currentLane;
    private int progress;
    private Lane targetLane;

    
    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    protected Vehicle() {
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param currentLane az aktuális sáv
     * @param progress az aktuális haladási pozíció
     * @param targetLane a cél sáv
     */
    protected Vehicle(Lane currentLane, int progress, Lane targetLane) {
        this.currentLane = currentLane;
        this.progress = progress;
        this.targetLane = targetLane;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a jármű aktuális sávját.
     *
     * @return az aktuális sáv
     */
    public Lane getCurrentLane() {
        return currentLane;
    }

    /**
     * Beállítja a jármű aktuális sávját.
     *
     * @param currentLane a beállítandó sáv
     */
    public void setCurrentLane(Lane currentLane) {
        this.currentLane = currentLane;
    }

    /**
     * Visszaadja a jármű aktuális haladási pozícióját.
     *
     * @return az aktuális haladási pozíció
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Beállítja a jármű haladási pozícióját.
     *
     * @param progress a beállítandó pozíció
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Visszaadja a jármű célsávját.
     *
     * @return a célsáv
     */
    public Lane getTargetLane() {
        return targetLane;
    }

    /**
     * Beállítja a jármű célsávját.
     *
     * @param targetLane a beállítandó célsáv
     */
    public void setTargetLane(Lane targetLane) {
        this.targetLane = targetLane;
    }


    // --- METÓDUSOK ---

    /**
     * Absztark metódus. Ennek megfeleően valósítja meg a több jármű, hogy hogyan változik az állapotuk az
     * idő függvényében
     */
    public void tick() {
    }

    /**
     * Védett metódus, amely a jármű mozgatását teszi lehetővé.
     */
    protected void move() {
    }

    /**
     * Megkísérli a jármű sávváltását az adott cél sávra.
     * A sáv elfogadása vagy elvetése a Lane osztálytól  illetve a Lane állapotától függ.
     *
     * @param target a cél sáv
     * @return igaz, ha a sávváltás sikeres lett
     */
    public boolean changeLane(Lane target) {
        return true;
    }

    /**
     * Annak a meghallgatása, hogy a jármű megbénulhat-e ütkorózesnél.
     * Például a hókotrok nem bénulhatnak meg, de az autók és buszok igen.
     *
     * @return igaz, ha a jármű bénulhat
     */
    public boolean isParalizable() {
        return false;
    }

    /**
     * Lebénítja a járművet a megadott időtartamra.
     *
     * @param time a bénulás időtartama (tick-ekben)
     */
    public void paralyze(int time) {
        
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}