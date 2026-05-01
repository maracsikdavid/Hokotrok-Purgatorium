package statemachine;
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
    private int saltTimer = -1;


    // --- KONSTRUKTOROK ---
    /**
     * Alapértelmezett konstruktor.
     */
    public IceCondition() {
    }

    /**
     * Paraméteres konstruktor a só hatásának aktivációs idejével.
     *
     * @param saltTimer a só hatásának aktivációs ideje tickekben
     */
    public IceCondition(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a sózás időzítőjét.
     *
     * @return a hátralévő tickek száma
     */
    public int getSaltTimer() {
        return saltTimer;
    }

    /**
     * Beállítja a sózás időzítőjét.
     *
     * @param saltTimer a beállítandó tickek száma
     */
    public void setSaltTimer(int saltTimer) {
        this.saltTimer = saltTimer;
    }


    // --- METÓDUSOK ---

    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő 
     * metódus. A szimuláció során a sáv ezen a metóduson keresztül számolhatja 
     * például a kiszórt só hatásának aktivációs idejét.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelynek az állapotát frissítjük.
     */
    @Override
    public void tick(Lane lane) {
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // No change inside tunnel
        }
        if (saltTimer == 0) {
            lane.setState(new CleanCondition());
        } else if (saltTimer > 0) {
            saltTimer--;
        }
    }

    /**
     * Havazás (csapadék) éri a jéggel borított sávot. Intenzív hóesés 
     * hatására a jégpáncél felett vastag hótakaró alakulhat ki, így a sáv 
     * állapota ThickSnowCondition-re módosulhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a hó esik.
     */
    @Override
    public void addSnow(Lane lane) {
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // No snow inside tunnel
        }
        lane.setState(new ThickSnowCondition());
    }

    /**
     * Sót juttat a jeges sávra. A só megolvasztja a jég.
     */
    @Override
    public void applySalt(Lane lane) {
        saltTimer = 1;  // Will melt after next tick
    }

    /**
     * Kavics szórása a jeges sávra, amely növeli a tapadást. 
     * Ennek hatására az állapot GraveledIceCondition-re változhat.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {

    }

    /**
     * Egy jármű (Vehicle) megkísérel rálépni a jeges sávra. 
     * Ha a jármű bénítható (Autó vagy Busz), a rendszer megcsúszást 
     * generálhat, ami időleges mozgásképtelenséget okoz. 
     * A Hókotrók immunisak a jégre.
     *
     * @param lane Az aktuális sáv (Lane) objektum, amelyre a jármű rálép.
     * @param vehicle Az a jármű, amelyik a sávra érkezik.
     */
    @Override
    public void acceptVehicle(Lane lane, Vehicle vehicle) {
        if (vehicle == null || !vehicle.isParalizable()) {
            return;
        }
        
        // Ütközés detektálás: ha már van más jármű a sávon
        if (lane.getVehicles() != null && lane.getVehicles().size() > 1) {
            // Megcsúszás/ütközés: mindkét járművet megbénítjuk
            for (Vehicle other : lane.getVehicles()) {
                if (other != vehicle) {
                    vehicle.paralyze(3);
                    other.paralyze(3);
                    break;
                }
            }
        }
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