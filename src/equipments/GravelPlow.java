package equipments;

import topology.Lane;

/**
 * Kavicsszóró típusú kotrófej. A jeges sávra kavicsot szór, hogy javítsa a tapadást.
 */
public class GravelPlow extends Plow {
    private Gravel gravelSource;

    // --- GETTEREK ÉS SETTEREK ---
    /**
     * Visszaadja a kavicsszóró fej kavicsforrását.
     *
     * @return a kavicsforrás
     */
    public Gravel getGravelSource() {
        return gravelSource;
    }

    /**
     * Beállítja a kavicsszóró fej kavicsforrását.
     *
     * @param gravelSource a beállítandó kavicsforrás
     */
    public void setGravelSource(Gravel gravelSource) {
        this.gravelSource = gravelSource;
    }

    // --- KONSTRUKTOROK ---
    /**
     * Alapértelmezett konstruktor.
     */
    public GravelPlow() {
        super();
    }

    /**
     * Paraméteres konstruktor a kavicsforrás megadásához.
     *
     * @param gravelSource a használt kavicsforrás
     */
    public GravelPlow(Gravel gravelSource) {
        super();
        this.gravelSource = gravelSource;
    }

    // --- METÓDUSOK ---
    /**
     * Takarítja a sávot kavicsszórással, ha van rendelkezésre álló kavics.
     *
     * @param lane a takarítandó sáv
     * @return igaz, ha sikeres volt a művelet
     */
    @Override
    public boolean clear(Lane lane) {
        return false;
    }

    /**
     * Újra feltölti kaviccsal a kavicsszóró fejet.
     *
     * @param gravel az új kavicsmennyiség
     */
    public void refill(Gravel gravel) {
    }
}