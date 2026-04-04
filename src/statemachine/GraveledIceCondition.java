package statemachine;

import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) kavicsozott jég állapotát reprezentáló osztály.
 * Ebben az állapotban a felület jeges, de a kavics javítja a tapadást.
 */
public class GraveledIceCondition implements LaneCondition {
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő metódus.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük
     */
    @Override
    public void tick(Lane lane) {

    }

    /**
     * Havazás (csapadék) éri a kavicsozott jeges sávot.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    @Override
    public void addSnow(Lane lane) {

    }

    /**
     * Sót juttat a kavicsozott jeges sávra.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a sót szórják
     */
    @Override
    public void applySalt(Lane lane) {

    }

    /**
     * Kavicsot juttat a már kavicsozott jeges sávra.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják
     */
    @Override
    public void applyGravel(Lane lane) {

    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a kavicsozott jeges sávra.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a jármű rálép
     * @param v    az a jármű, amelyik a sávra érkezik
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle v) {

    }
}