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
    private final Map<String, String> nodeLabels = new LinkedHashMap<>();
    private final Map<String, String> roadLabels = new LinkedHashMap<>();
    private final Map<String, Integer> laneOffsets = new LinkedHashMap<>();
    private final Map<String, RoadLayoutHint> roadHints = new LinkedHashMap<>();
    private final Map<String, IconLayoutHint> iconHints = new LinkedHashMap<>();
    private final Map<String, String> roadFromNodes = new LinkedHashMap<>();
    private final Map<String, String> roadToNodes = new LinkedHashMap<>();

    /**
     * Visszaadja a csomópontok koordinátáit csak olvasható formában.
     *
     * @return csomópont-azonosító szerint indexelt pozíciók
     */
    public Map<String, Point> getNodePositions() {
        return Collections.unmodifiableMap(nodePositions);
    }

    /**
     * Visszaadja a layout fájlban megadott csomópont címkéket csak olvasható formában.
     *
     * @return csomópont-azonosító szerint indexelt címkék
     */
    public Map<String, String> getNodeLabels() {
        return Collections.unmodifiableMap(nodeLabels);
    }

    /**
     * Visszaadja a layout fájlban megadott út címkéket csak olvasható formában.
     *
     * @return útazonosító szerint indexelt címkék
     */
    public Map<String, String> getRoadLabels() {
        return Collections.unmodifiableMap(roadLabels);
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
     * Visszaadja az utakra vonatkozó rajzolási hint-eket csak olvasható formában.
     *
     * @return útazonosító szerint indexelt road-hint gyűjtemény
     */
    public Map<String, RoadLayoutHint> getRoadHints() {
        return Collections.unmodifiableMap(roadHints);
    }

    /**
     * Visszaadja az utak kezdő csomópontjait csak olvasható formában.
     *
     * @return útazonosító szerint indexelt kezdő csomópontok
     */
    public Map<String, String> getRoadFromNodes() {
        return Collections.unmodifiableMap(roadFromNodes);
    }

    /**
     * Visszaadja az utak cél csomópontjait csak olvasható formában.
     *
     * @return útazonosító szerint indexelt cél csomópontok
     */
    public Map<String, String> getRoadToNodes() {
        return Collections.unmodifiableMap(roadToNodes);
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
     * Lekéri egy út kezdő csomópont azonosítóját.
     *
     * @param roadId az út azonosítója
     * @return a kezdő csomópont azonosítója vagy null
     */
    public String getRoadFromNode(String roadId) {
        return roadFromNodes.get(roadId);
    }

    /**
     * Lekéri egy út cél csomópont azonosítóját.
     *
     * @param roadId az út azonosítója
     * @return a cél csomópont azonosítója vagy null
     */
    public String getRoadToNode(String roadId) {
        return roadToNodes.get(roadId);
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
     * Lekéri egy csomópont layout címkéjét.
     *
     * @param id a csomópont azonosítója
     * @return a csomópont címkéje vagy null
     */
    public String getNodeLabel(String id) {
        return nodeLabels.get(id);
    }

    /**
     * Lekéri egy út layout címkéjét.
     *
     * @param id az út azonosítója
     * @return az út címkéje vagy null
     */
    public String getRoadLabel(String id) {
        return roadLabels.get(id);
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
     * Beállítja egy csomópont megjelenített címkéjét.
     *
     * @param id a csomópont azonosítója
     * @param label a csomópont címkéje
     */
    public void putNodeLabel(String id, String label) {
        if (id != null && label != null && !label.isBlank()) {
            nodeLabels.put(id, label);
        }
    }

    /**
     * Beállítja egy út megjelenített címkéjét.
     *
     * @param id az út azonosítója
     * @param label az út címkéje
     */
    public void putRoadLabel(String id, String label) {
        if (id != null && label != null && !label.isBlank()) {
            roadLabels.put(id, label);
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
     * Lekéri egy út road-hint adatát.
     *
     * @param roadId az út azonosítója
     * @return az út hintje, vagy null ha nincs megadva
     */
    public RoadLayoutHint getRoadHint(String roadId) {
        return roadHints.get(roadId);
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
     * Beállít egy úthoz tartozó road-hintet.
     *
     * @param hint a tárolandó road-hint
     */
    public void putRoadHint(RoadLayoutHint hint) {
        if (hint != null && hint.getRoadId() != null) {
            roadHints.put(hint.getRoadId(), hint);
        }
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
     * Beállítja egy út végpontjait.
     *
     * @param roadId az út azonosítója
     * @param fromNodeId kezdő csomópont
     * @param toNodeId cél csomópont
     */
    public void putRoadEndpoints(String roadId, String fromNodeId, String toNodeId) {
        if (roadId != null && fromNodeId != null && toNodeId != null) {
            roadFromNodes.put(roadId, fromNodeId);
            roadToNodes.put(roadId, toNodeId);
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