package statemachine;

import core.Skeleton;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) alapértelmezett, tiszta állapotát reprezentáló.
 * Ebben az állapotban az útfelület optimális és biztonságos,
 * nincsenek rajta fizikai akadályok (hó vagy jég). 
 * A járművek (Autó, Busz, Hókotró) megcsúszás és sebességkorlátozás nélkül, 
 * szabadon közlekedhetnek rajta. Havazás esetén ebből az állapotból 
 * vékony hó (ThinSnowCondition) állapotba léphet át.
 */
public class CleanCondition implements LaneCondition {
    
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. Tiszta sáv esetén a valós üzleti logikában itt történhet meg 
     * annak ellenőrzése, hogy elkezdett-e esni a hó, és ha igen, mennyi idő (tick) 
     * telt el.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük
     */
    @Override
    public void tick(Lane lane) {
        Skeleton.printCall(null, this, "tick");
        Skeleton.printReturn(this, "tick");
    }

    /**
     * Havazás (csapadék) éri a sávot. Ennek a metódusnak a hatására a tiszta sávon 
     * elkezd gyűlni a hó. Ha a folyamatos havazás időtartama eléri a megadott 
     * küszöbértéket (5 tick), a sáv megkéri a Lane objektumot, hogy váltson 
     * át ThinSnowCondition állapotba.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    @Override
    public void addSnow(Lane lane) {
        Skeleton.printCall(null, this, "addSnow");
        Skeleton.printReturn(this, "addSnow");
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni erre a sávra, vagy ezen a sávon halad. 
     * Mivel a sáv állapota tiszta (CleanCondition), a sáv mindenféle fizikai 
     * korlátozás vagy balesetveszély (pl. 20%-os megcsúszási esély, bénulás) 
     * nélkül, sikeresen befogadja a járművet.
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