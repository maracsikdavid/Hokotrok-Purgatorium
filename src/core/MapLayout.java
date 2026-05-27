package core;

import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * A pálya layout fájlból beolvasott, megjelenítéshez használt leírója.
 * A struktúra a csomópont, út, sáv és ikon alapú vizuális hint-eket tárolja.
 */
public class MapLayout {
    private final java.util.Map<String, NodeLayoutHint> nodeHints = new LinkedHashMap<>();
    private final java.util.Map<String, RoadLayoutHint> roadHints = new LinkedHashMap<>();
    private final java.util.Map<String, LaneLayoutHint> laneHints = new LinkedHashMap<>();
    private final java.util.Map<String, IconLayoutHint> iconHints = new LinkedHashMap<>();

    /**
     * Visszaadja a csomópontokra vonatkozó vizuális hint-eket.
     *
     * @return Azonosító szerint indexelt, csak olvasható csomópont-hint gyűjtemény.
     */
    public java.util.Map<String, NodeLayoutHint> getNodeHints() {
        return Collections.unmodifiableMap(nodeHints);
    }

    /**
     * Visszaadja az utakra vonatkozó vizuális hint-eket.
     *
     * @return Azonosító szerint indexelt, csak olvasható út-hint gyűjtemény.
     */
    public java.util.Map<String, RoadLayoutHint> getRoadHints() {
        return Collections.unmodifiableMap(roadHints);
    }

    /**
     * Visszaadja a sávokra vonatkozó vizuális hint-eket.
     *
     * @return Azonosító szerint indexelt, csak olvasható sáv-hint gyűjtemény.
     */
    public java.util.Map<String, LaneLayoutHint> getLaneHints() {
        return Collections.unmodifiableMap(laneHints);
    }

    /**
     * Visszaadja az ikonokra vonatkozó vizuális hint-eket.
     *
     * @return Azonosító szerint indexelt, csak olvasható ikon-hint gyűjtemény.
     */
    public java.util.Map<String, IconLayoutHint> getIconHints() {
        return Collections.unmodifiableMap(iconHints);
    }

    /**
     * Új csomópont-hint bejegyzése.
     *
     * @param hint A felvenni kívánt csomópont-hint.
     */
    public void putNodeHint(NodeLayoutHint hint) {
        nodeHints.put(hint.getId(), hint);
    }

    /**
     * Új út-hint bejegyzése.
     *
     * @param hint A felvenni kívánt út-hint.
     */
    public void putRoadHint(RoadLayoutHint hint) {
        roadHints.put(hint.getId(), hint);
    }

    /**
     * Új sáv-hint bejegyzése.
     *
     * @param hint A felvenni kívánt sáv-hint.
     */
    public void putLaneHint(LaneLayoutHint hint) {
        laneHints.put(hint.getId(), hint);
    }

    /**
     * Új ikon-hint bejegyzése.
     *
     * @param hint A felvenni kívánt ikon-hint.
     */
    public void putIconHint(IconLayoutHint hint) {
        iconHints.put(hint.getId(), hint);
    }

    /**
     * Csomópont megjelenítési adatainak értékobjektuma.
     */
    public static final class NodeLayoutHint {
        private final String id;
        private final int x;
        private final int y;
        private final String label;

        /**
         * Konstruktor a csomópont koordináta- és címkeadatainak beállításához.
         *
         * @param id A csomópont azonosítója.
         * @param x A vízszintes képernyőkoordináta.
         * @param y A függőleges képernyőkoordináta.
         * @param label A csomópont felirata.
         */
        public NodeLayoutHint(String id, int x, int y, String label) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * Út megjelenítési adatainak értékobjektuma.
     */
    public static final class RoadLayoutHint {
        private final String id;
        private final String fromNodeId;
        private final String toNodeId;

        /**
         * Konstruktor az út két végpontjának beállításához.
         *
         * @param id Az út azonosítója.
         * @param fromNodeId A kezdő csomópont azonosítója.
         * @param toNodeId A cél csomópont azonosítója.
         */
        public RoadLayoutHint(String id, String fromNodeId, String toNodeId) {
            this.id = id;
            this.fromNodeId = fromNodeId;
            this.toNodeId = toNodeId;
        }

        public String getId() {
            return id;
        }

        public String getFromNodeId() {
            return fromNodeId;
        }

        public String getToNodeId() {
            return toNodeId;
        }
    }

    /**
     * Sáv megjelenítési adatainak értékobjektuma.
     */
    public static final class LaneLayoutHint {
        private final String id;
        private final int offset;

        /**
         * Konstruktor a sáv vizuális eltolásának beállításához.
         *
         * @param id A sáv azonosítója.
         * @param offset A sáv rajzolási eltolása.
         */
        public LaneLayoutHint(String id, int offset) {
            this.id = id;
            this.offset = offset;
        }

        public String getId() {
            return id;
        }

        public int getOffset() {
            return offset;
        }
    }

    /**
     * Ikon megjelenítési adatainak értékobjektuma.
     */
    public static final class IconLayoutHint {
        private final String id;
        private final String type;
        private final String color;

        /**
         * Konstruktor az ikon típus- és színadataihoz.
         *
         * @param id Az ikon azonosítója.
         * @param type Az ikon típusa.
         * @param color Az ikon színe.
         */
        public IconLayoutHint(String id, String type, String color) {
            this.id = id;
            this.type = type;
            this.color = color;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getColor() {
            return color;
        }
    }
}