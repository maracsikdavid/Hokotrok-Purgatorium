package statemachine;

import core.Skeleton;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) vastag hóval (mély hóréteggel) borított állapotát reprezentáló osztály.
 * Ebben az állapotban az útfelület normál közlekedésre gyakorlatilag alkalmatlan.
 * A hagyományos járművek (Autó, Busz) elakadhatnak vagy mozgásképtelenné (lebénulttá) válhatnak rajta.
 * A Hókotrók (Snowplow) viszont szabadon haladhatnak, és a megfelelő kotrófejjel eltakaríthatják 
 * a havat, illetve a Sószóró (SaltPlow) segítségével feloldhatják ezt az állapotot.
 */
public class ThickSnowCondition implements LaneCondition {
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. A szimuláció során a sáv ezen a metóduson keresztül számolhatja 
     * például a kiszórt só hatásának aktivációs idejét (2 tick).
     *
     * @param lane az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük
     */
    @Override
    public void tick(Lane lane) {
        Skeleton.printCall(null, this, "tick");
        Skeleton.printReturn(this, "tick");
    }

    /**
     * Havazás (csapadék) éri a már vastag hóval borított sávot. Mivel a sáv 
     * hórétege már elérte a szimuláció szerinti maximális vastagságot 
     * (ThickSnowCondition), a további hóesés nem vált ki újabb állapotváltozást, 
     * a sáv állapota megmarad.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    @Override
    public void addSnow(Lane lane) {
        Skeleton.printCall(null, this, "addSnow");
        Skeleton.printReturn(this, "addSnow");
    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a vastag hótakaróra. 
     * A só hatásának aktivációs ideje 2 tick. Ennek letelte után a hó elolvad, 
     * a sáv tiszta állapotba (CleanCondition) vált, és további 4 tick-ig 
     * immunis lesz az újabb havazásra.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a sót szórják
     */
    @Override
    public void applySalt(Lane lane) {
        Skeleton.printCall(null, this, "applySalt");
        Skeleton.printReturn(this, "applySalt");
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a vastag hóval borított sávra. 
     * A normál járművek (Autó, Busz) mozgását a mély hóréteg megakadályozza, 
     * így azok elakadhatnak vagy megbénulhatnak. A takarítást végző Hókotrók 
     * viszont akadálytalanul befogadásra kerülnek, és folytathatják a munkájukat.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a jármű rálép
     * @param v    az a jármű (Autó, Busz vagy Hókotró), amelyik a sávra érkezik
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle v) {
        Skeleton.printCall(null, this, "acceptVehicle");
        Skeleton.printReturn(this, "acceptVehicle");
    }
}