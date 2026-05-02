package statemachine;
import core.GameRules;
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
    /** @return Mindig igaz, ez az állapot tiszta sáv. */
    @Override public boolean isClean() { return true; }
    private int snowTicks;
    private int noSnowTicks;

    public CleanCondition() {
        this(0, 0);
    }

    public CleanCondition(int snowTicks, int noSnowTicks) {
        this.snowTicks = Math.max(0, snowTicks);
        this.noSnowTicks = Math.max(0, noSnowTicks);
    }

    // --- METÓDUSOK ---

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
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // Alagútban nincs hó
        }

        if (noSnowTicks > 0) {
            noSnowTicks--;
            return;
        }

        snowTicks++;
        if (snowTicks >= GameRules.THIN_SNOW_AFTER_TICKS) {
            lane.setState(new ThinSnowCondition());
        }
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
        if (lane.getRoad() != null && lane.getRoad().getClass().getSimpleName().equals("Tunnel")) {
            return;  // Alagútban nincs hó
        }

        if (noSnowTicks > 0) {
            return;
        }

        snowTicks++;
        if (snowTicks >= GameRules.THIN_SNOW_AFTER_TICKS) {
            lane.setState(new ThinSnowCondition());
        }
    }

    /**
     * Megtisztított sáv esetén a sózásnak nincs hatása az állapotra.
     *
     * @param lane Az aktuális sáv objektum.
     */
    @Override
    public void applySalt(Lane lane) {
        noSnowTicks = Math.max(noSnowTicks, GameRules.SALT_SNOW_IMMUNITY_TICKS);
        snowTicks = 0;
    }

    /**
     * Kavics szórása tiszta sávra. Tiszta állapotban ez nem okoz állapotváltozást.
     *
     * @param lane Az aktuális sáv objektum, amelyre a kavicsot szórják.
     */
    @Override
    public void applyGravel(Lane lane) {

    }

    /**
     * Jármű elfogadása a sávra. Tiszta úton a járművek zavartalanul haladhatnak.
     *
     * @param lane Az aktuális sáv objektum, amelyre a jármű rálép.
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