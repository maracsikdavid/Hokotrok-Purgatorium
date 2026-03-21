package statemachine;
import topology.Lane;
import entities.Vehicle;

public interface LaneCondition {
    void tick(Lane lane);

    void addSnow(Lane lane);

    void acceptVehicle(Lane lane, Vehicle v);
}