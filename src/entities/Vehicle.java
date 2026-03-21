package entities;
import core.ITickable;
import core.Skeleton;
import topology.Lane;

public abstract class Vehicle implements ITickable {
    public Lane currentLane;
    public int progress;
    public Lane targetLane;
    
    public void tick() {
        Skeleton.printCall(null, this, "tick");
        move();
        Skeleton.printReturn(this, "tick");
    }

    protected void move() {
        Skeleton.printCall(null, this, "move");
        Skeleton.printReturn(this, "move");
    }
    
    public void setTargetLane(Lane target) {
        Skeleton.printCall(null, this, "setTargetLane");
        this.targetLane = target;
        Skeleton.printReturn(this, "setTargetLane");
    }

    public boolean changeLane(Lane target) {
        Skeleton.printCall(null, this, "changeLane");
        Skeleton.printReturn(this, "changeLane", "true");
        return true;
    }

    public boolean isParalizable() {
        Skeleton.printCall(null, this, "isParalizable");
        Skeleton.printReturn(this, "isParalizable", "true");
        return true;
    }
}