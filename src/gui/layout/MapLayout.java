package gui.layout;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A grafikus pályarajzoláshoz szükséges koordinátákat és egyszerű vizuális hint-eket tárolja.
 * A modell nem ismeri ezeket az adatokat, így a megjelenítés külön rétegben marad.
 */
public class MapLayout {
    private final Map<String, Point> nodePositions = new LinkedHashMap<>();
    private final Map<String, Integer> laneOffsets = new LinkedHashMap<>();
    private final Map<String, IconLayoutHint> iconHints = new LinkedHashMap<>();

    /**
     * Visszaadja a csomópontok koordinátáit csak olvasható formában.
     *
     * @return csomópont-azonosító szerint indexelt pozíciók
     */
    public Map<String, Point> getNodePositions() {
        return Collections.unmodifiableMap(nodePositions);
    }

    /**
     * Visszaadja a sávok rajzolási eltolásait csak olvasható formában.
     *
     * @return sávazonosító szerint indexelt eltolások
     */
    public Map<String, Integer> getLaneOffsets() {
        return Collections.unmodifiableMap(laneOffsets);
    }

    /**
     * Visszaadja az ikonokra vonatkozó hint-eket csak olvasható formában.
     *
     * @return objektumazonosító szerint indexelt ikon-hint gyűjtemény
     */
    public Map<String, IconLayoutHint> getIconHints() {
        return Collections.unmodifiableMap(iconHints);
    }

    /**
     * Lekéri egy csomópont képernyőpozícióját.
     *
     * @param id a csomópont azonosítója
     * @return a csomópont pozíciója, vagy null ha nincs layout adat
     */
    public Point getNodePosition(String id) {
        Point point = nodePositions.get(id);
        return point == null ? null : new Point(point);
    }

    /**
     * Beállítja egy csomópont képernyőpozícióját.
     *
     * @param id a csomópont azonosítója
     * @param point a csomópont pozíciója
     */
    public void putNodePosition(String id, Point point) {
        if (id != null && point != null) {
            nodePositions.put(id, new Point(point));
        }
    }

    /**
     * Lekéri egy sáv rajzolási eltolását.
     *
     * @param id a sáv azonosítója
     * @return az eltolás értéke, vagy 0 ha nincs külön beállítás
     */
    public int getLaneOffset(String id) {
        Integer offset = laneOffsets.get(id);
        return offset == null ? 0 : offset.intValue();
    }

    /**
     * Beállítja egy sáv rajzolási eltolását.
     *
     * @param id a sáv azonosítója
     * @param offset az eltolás pixelben értelmezett értéke
     */
    public void putLaneOffset(String id, int offset) {
        if (id != null) {
            laneOffsets.put(id, offset);
        }
    }

    /**
     * Lekéri egy objektum ikon-hint adatát.
     *
     * @param id az objektum azonosítója
     * @return az ikon-hint, vagy null ha nincs megadva
     */
    public IconLayoutHint getIconHint(String id) {
        return iconHints.get(id);
    }

    /**
     * Beállít egy objektumhoz tartozó ikon-hintet.
     *
     * @param hint a tárolandó ikon-hint
     */
    public void putIconHint(IconLayoutHint hint) {
        if (hint != null && hint.getId() != null) {
            iconHints.put(hint.getId(), hint);
        }
    }

    /**
     * Egy objektum javasolt ikonmegjelenítésének egyszerű értékobjektuma.
     */
    public static final class IconLayoutHint {
        private final String id;
        private final String type;
        private final String color;

        /**
         * Konstruktor az ikon-hint alapadatainak megadásához.
         *
         * @param id az érintett objektum azonosítója
         * @param type az ikon típusa
         * @param color az ikon javasolt színe
         */
        public IconLayoutHint(String id, String type, String color) {
            this.id = id;
            this.type = type;
            this.color = color;
        }

        /**
         * Visszaadja az érintett objektum azonosítóját.
         *
         * @return az objektum azonosítója
         */
        public String getId() {
            return id;
        }

        /**
         * Visszaadja az ikon típusát.
         *
         * @return az ikon típusa
         */
        public String getType() {
            return type;
        }

        /**
         * Visszaadja az ikon javasolt színét.
         *
         * @return az ikon színe
         */
        public String getColor() {
            return color;
        }
    }
}