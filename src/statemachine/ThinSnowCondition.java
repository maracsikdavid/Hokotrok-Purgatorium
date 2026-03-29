package statemachine;

import core.Skeleton;
import entities.Vehicle;
import topology.Lane;

/**
 * A sávok (Lane) vékony hóval borított állapotát reprezentáló osztály.
 * Ez egy átmeneti, instabil állapot: ha a havazás tovább folytatódik (5 tick), a hóréteg 
 * vastag hóvá (ThickSnowCondition) alakul. Ha viszont a sűrű forgalom (20 jármű) letapossa, 
 * az útfelület jéggé (IceCondition) változik. A takarító járművek sószóró (SaltPlow) vagy normál 
 * kotrófej segítségével képesek megtisztítani.
 */
public class ThinSnowCondition implements LaneCondition {
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. A szimuláció során a sáv ezen a metóduson keresztül számolhatja 
     * például a havazás idejét vagy a kiszórt só hatásának aktivációs idejét.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük
     */
    @Override
    public void tick(Lane lane) {
        Skeleton.printCall(null, this, "tick");
        Skeleton.printReturn(this, "tick");
    }

    /**
     * Havazás (csapadék) éri a vékony hóval borított sávot. Ha a folyamatos 
     * havazás időtartama eléri a küszöbértéket (újabb 5 tick), a hóréteg 
     * vastagsága kritikussá válik, és a sáv állapota vastag hóra 
     * (ThickSnowCondition) módosul.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    @Override
    public void addSnow(Lane lane) {
        Skeleton.printCall(null, this, "addSnow");
        Skeleton.printReturn(this, "addSnow");
    }

    /**
     * A sávon áthaladó járművek (Autók, Buszok) súlya letapossa és tömöríti 
     * a vékony havat. Ha pontosan 20 darab jármű halad át a sávon, a letaposás 
     * hatására a felület jégpáncéllá (IceCondition) alakul.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyet a járművek letaposnak
     */
    public void trample(Lane lane) {
        Skeleton.printCall(null, this, "trample");
        Skeleton.printReturn(this, "trample");
    }

    /**
     * Egy sószórófejes hókotró (SaltPlow) sót juttat a vékony hótakaróra. 
     * A só hatásának aktivációs ideje 2 tick. Ennek letelte után a vékony hó 
     * teljesen elolvad, a sáv tiszta állapotba (CleanCondition) vált, és további 
     * 4 tick-ig immunis lesz az újabb csapadékra.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a sót szórják
     */
    @Override
    public void applySalt(Lane lane) {
        Skeleton.printCall(null, this, "applySalt");
        Skeleton.printReturn(this, "applySalt");
    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a vékony hóval borított sávra. 
     * Maga a vékony hó nem akadályozza a haladást és nem okoz balesetet, 
     * azonban minden egyes áthaladó jármű meghívja a sáv trample() metódusát, 
     * közelebb hozva ezzel a jéggé válás (20 jármű) küszöbértékét.
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