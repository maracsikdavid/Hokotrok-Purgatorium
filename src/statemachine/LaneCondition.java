package statemachine;
import topology.Lane;
import entities.Vehicle;

/**
 * A sávok (Lane) időjárási és felületi állapotát leíró központi interfész.
 * Az aktuális állapot (tiszta, vékony hó, vastag hó vagy jég) határozza meg, hogy a
 * sávra rálépő járművek (Vehicle) hogyan viselkednek, illetve hogyan változik a sáv 
 * a folyamatos havazás vagy az idő múlása hatására.
 */
public interface LaneCondition {
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. Felelős az idő múlásával összefüggő automatikus állapotváltozások 
     * (például a kiszórt só olvasztó hatásának időzítése) kezeléséért.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük
     */
    void tick(Lane lane);

    /**
     * Havazás (csapadék) szimulálására szolgáló metódus. A folyamatos havazás 
     * (tick-ek múlása) hatására a sáv felgyülemlő hórétege elérhet bizonyos 
     * küszöbértékeket, ami állapotváltást (pl. tiszta sávról vékony hóra, 
     * vagy vékony hóról vastag hóra) eredményezhet.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a hó esik
     */
    void addSnow(Lane lane);

    /**
     * Kezeli egy jármű sávra lépését az aktuális felületi állapot (State) 
     * függvényében. A különböző állapotok (például a jég) befolyásolhatják 
     * a jármű viselkedését: a Hókotrókat átengedhetik, míg a normál járműveket 
     * (Autó, Busz) megcsúsztathatják vagy lebéníthatják.
     *
     * @param lane az aktuális sáv (Lane) objektum, amelyre a jármű rálép
     * @param v    az a jármű (Autó, Busz vagy Hókotró), amelyik a sávra érkezik
     */
    void acceptVehicle(Lane lane, Vehicle v);
}