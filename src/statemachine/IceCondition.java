package statemachine;

import core.Skeleton;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) jégpáncéllal borított állapotát reprezentáló osztály.
 * Ez a legveszélyesebb közlekedési körülmény a szimulációban.
 * A normál járművek (Autó, Busz) ezen a sávon 20% eséllyel
 * megcsúszhatnak és balesetet szenvedhetnek (lebénulhatnak). 
 * A Hókotrók (Snowplow) speciális felépítésük miatt immunisak a jégre.
 * Ezt az állapotot a Sószóró (SaltPlow), Tűzhányó (DragonPlow) vagy a Jégtörő (IcebreakerPlow) 
 * segítségével lehet megszüntetni.
 */
public class IceCondition implements LaneCondition {
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
     * Havazás (csapadék) éri a jéggel borított sávot. Intenzív hóesés (5 tick) 
     * hatására a jégpáncél felett vastag hótakaró alakulhat ki, így a sáv 
     * állapota ThickSnowCondition-re módosul.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    @Override
    public void addSnow(Lane lane) {
        Skeleton.printCall(null, this, "addSnow");
        Skeleton.printReturn(this, "addSnow");
    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a jégpáncélra. 
     * A só hatásának aktivációs ideje 2 tick. Ennek letelte után a só feloldja 
     * a jeget, a sáv tiszta állapotba (CleanCondition) vált, és további 4 tick-ig 
     * semmilyen csapadék nem marad meg rajta (immunis lesz a havazásra).
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a sót szórják
     */
    public void applySalt(Lane lane) {
        Skeleton.printCall(null, this, "applySalt");
        Skeleton.printReturn(this, "applySalt");
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a jeges sávra. 
     * Ha a jármű egy Autó vagy Busz, a rendszer 20% valószínűséggel megcsúszást 
     * generál, ami 2 tick ideig tartó bénulást (paralyze) és potenciális ütközést 
     * okoz a sávon tartózkodó többi járművel. A Hókotrók (isParalizable() == false) 
     * immunisak a jégre, és normálisan folytathatják a haladást.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a jármű rálép
     * @param v    az a jármű (Autó, Busz vagy Hókotró), amelyik a sávra érkezik
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle vehicle) {
        Skeleton.printCall(null, this, "acceptVehicle");
        
        // Polimorfikus hívás: a futási típus (Car, Bus, Snowplow) dönti el, mit ad vissza
        if (vehicle.isParalizable()) {
            
            // Mivel bénítható, a Skeleton bekéri a 20%-os esély eredményét
            int answer = Skeleton.getIntFromUser("Megcsúszik és lebénul a jármű a jégen? (1: Igen, 0: Nem)");
            
            if (answer == 1) {
                vehicle.paralyze(2);
            }
        }
        
        Skeleton.printReturn(this, "acceptVehicle");
    }
}