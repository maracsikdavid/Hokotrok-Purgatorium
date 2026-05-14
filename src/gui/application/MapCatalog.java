package gui.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A grafikus módban választható pályák katalógusa.
 * Csak metaadatokat tárol, a tényleges modellobjektumokat a session factory építi fel.
 */
public class MapCatalog {
    private List<MapDescriptor> maps = new ArrayList<>();

    /**
     * Alapértelmezett konstruktor üres katalógussal.
     */
    public MapCatalog() {
    }

    /**
     * Paraméteres konstruktor előre megadott pályalistához.
     *
     * @param maps a katalógusba kerülő pályaleírók
     */
    public MapCatalog(List<MapDescriptor> maps) {
        this.maps = maps == null ? new ArrayList<>() : new ArrayList<>(maps);
    }

    /**
     * Betölti az alapértelmezett négy grafikus pályaleírót.
     */
    public void loadDefaults() {
        maps.clear();
        maps.add(new MapDescriptor("base", "Alappálya - Városi kör",
            "Referencia pálya az alap mechanikákhoz.", "Közepes",
            "maps/base-map-init.txt", "maps/base-map-layout.txt"));
        maps.add(new MapDescriptor("tutorial", "Oktatópálya - Rövid útvonal",
            "Kis pálya a vezérlés megismeréséhez.", "Könnyű",
            "maps/tutorial-map-init.txt", "maps/tutorial-map-layout.txt"));
        maps.add(new MapDescriptor("city", "Forgalmi pálya - Több megálló és kereszteződés",
            "Nagyobb pálya több konfliktushelyzettel.", "Közepes",
            "maps/city-map-init.txt", "maps/city-map-layout.txt"));
        maps.add(new MapDescriptor("blizzard", "Viharzóna - Nehéz mentési helyzet",
            "Nehéz pálya sok havas és jeges szakasszal.", "Nehéz",
            "maps/blizzard-map-init.txt", "maps/blizzard-map-layout.txt"));
    }

    /**
     * Visszaadja a teljes pályakatalógust csak olvasható listaként.
     *
     * @return a pályaleírók listája
     */
    public List<MapDescriptor> getAllMaps() {
        return Collections.unmodifiableList(maps);
    }

    /**
     * Beállítja a katalógusban tárolt pályákat.
     *
     * @param maps az új pályaleíró lista
     */
    public void setMaps(List<MapDescriptor> maps) {
        this.maps = maps == null ? new ArrayList<>() : new ArrayList<>(maps);
    }

    /**
     * Azonosító alapján megkeres egy pályaleírót.
     *
     * @param id a keresett pálya azonosítója
     * @return a megtalált pályaleíró, vagy null ha nincs ilyen pálya
     */
    public MapDescriptor findById(String id) {
        if (id == null) {
            return null;
        }
        for (MapDescriptor descriptor : maps) {
            if (id.equals(descriptor.getId())) {
                return descriptor;
            }
        }
        return null;
    }
}