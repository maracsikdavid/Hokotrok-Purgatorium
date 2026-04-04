package equipments;

import topology.Lane;

/**
 * Kavicsszóró típusú kotrófej. A jeges sávra kavicsot szór, hogy javítsa a tapadást.
 */
public class GravelPlow extends Plow {
    private Gravel gravelSource;

    // --- GETTEREK ÉS SETTEREK ---
    public Gravel getGravelSource() {
        return gravelSource;
    }

    public void setGravelSource(Gravel gravelSource) {
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
        this.gravelSource = gravel;
    }
}