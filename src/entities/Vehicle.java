package entities;
import core.ITickable;
import core.Skeleton;
import topology.Lane;

/**
 * Absztrakt alap osztály minden járműhöz. 
 */
public abstract class Vehicle implements ITickable {
    public Lane currentLane;
    public int progress;
    public Lane targetLane;
    
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
     * Beállítja a következõ cél sávot, ahová a jármű váltani kíván.
     *
     * @param target a kívánatos cél sáv
     */
    public void setTargetLane(Lane target) {
        Skeleton.printCall(null, this, "setTargetLane");
        this.targetLane = target;
        Skeleton.printReturn(this, "setTargetLane");
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
        Skeleton.printReturn(this, "isParalizable", "true");
        return true;
    }
}