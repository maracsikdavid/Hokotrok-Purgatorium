package entities;
import core.ITickable;
import core.Skeleton;
import topology.Lane;

/**
 * Absztrakt alap osztály minden járműhöz. 
 */
public abstract class Vehicle implements ITickable {
    private Lane currentLane;
    private int progress;
    private Lane targetLane;
    

    // --- GETTEREK ÉS SETTEREK ---
    public Lane getCurrentLane() {
        return currentLane;
    }
    public void setCurrentLane(Lane currentLane) {
        this.currentLane = currentLane;
    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Lane getTargetLane() {
        return targetLane;
    }
    public void setTargetLane(Lane targetLane) { 
        Skeleton.printCall(null, this, "setTargetLane");
        this.targetLane = targetLane; 
        Skeleton.printReturn(this, "setTargetLane");
    }

    
    // --- METÓDUSOK ---
    /**
     * Absztark metódus. Ennek megfeleően valósítja meg a több jármű, hogy hogyan változik az állapotuk az
     * idő függvényében
     */
    public void tick() {
        Skeleton.printCall(null, this, "tick");
        move();
        Skeleton.printReturn(this, "tick");
    }

    /**
     * Védett metódus, amely a jármű mozgatását teszi lehetővé.
     */
    protected void move() {
        Skeleton.printCall(null, this, "move");
        Skeleton.printReturn(this, "move");
    }

    /**
     * Megkísérli a jármű sávváltását az adott cél sávra.
     * A sáv elfogadása vagy elvetése a Lane osztálytól  illetve a Lane állapotától függ.
     *
     * @param target a cél sáv
     * @return igaz, ha a sávváltás sikeres lett
     */
    public boolean changeLane(Lane target) {
        Skeleton.printCall(null, this, "changeLane");
        Skeleton.printReturn(this, "changeLane", "true");
        return true;
    }

    /**
     * Annak a meghallgatása, hogy a jármű megbénulhat-e ütkorózesnél.
     * Például a hókotrok nem bénulhatnak meg, de az autók és buszok igen.
     *
     * @return igaz, ha a jármű bénulhat
     */
    public boolean isParalizable() {
        Skeleton.printCall(null, this, "isParalizable");
        Skeleton.printReturn(this, "isParalizable", "false");
        return false;
    }

    /**
     * Lebénítja a járművet a megadott időtartamra.
     *
     * @param time a bénulás időtartama (tick-ekben)
     */
    public void paralyze(int time) {
        Skeleton.printCall(null, this, "paralyze");
        Skeleton.printReturn(this, "paralyze");
    }
}