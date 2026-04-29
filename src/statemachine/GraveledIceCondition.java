package statemachine;

import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) kavicsozott jég állapotát reprezentáló osztály.
 * Ebben az állapotban a felület jeges, de a kavics javítja a tapadást.
 */
public class GraveledIceCondition implements LaneCondition {
    
    // --- METÓDUSOK ---

    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő metódus. 
     * Kavicsozott jég esetén itt kezelhető az idő múlásával járó állapotváltozás.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük.
     */
    @Override
    public void tick(Lane lane) {

    }

    /**
     * Havazás (csapadék) éri a kavicsozott jeges sávot. A hóréteg elfedheti a kavicsot, 
     * ami visszavezethez a jeges vagy havas állapothoz.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a hó esik.
     *
     * Pszeudokód:
     * 1. Havazás esetén vékony hó állapotra vált.
     */
    @Override
    public void addSnow(Lane lane) {

    }

    /**
     * Sót juttat a kavicsozott jeges sávra. A só segít a jég megolvasztásában.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a sót szórják.
     */
    @Override
    public void applySalt(Lane lane) {

    }

    /**
     * Kavicsot juttat a már kavicsozott jeges sávra. Ennek az állapotban 
     * nincs további halmozódó hatása.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {

    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a kavicsozott jeges sávra. 
     * A kavics javítja a tapadást, így a megcsúszás esélye minimális vagy nulla.
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