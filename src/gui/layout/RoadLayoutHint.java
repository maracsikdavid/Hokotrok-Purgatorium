package gui.layout;

/**
 * Egy út (road) megjelenítéséhez tartozó rajzolási hint.
 * Segít leírni, hogy az út egyenes/görbe legyen, mekkora görbületi eltolással,
 * és hogyan illeszkedjen a csomópont alakjához.
 */
public final class RoadLayoutHint {
    private final String roadId;
    private final String shape;
    private final int controlOffset;
    private final String nodeAlignment;

    private static final int AUTO_MIN_OFFSET = 18;
    private static final int AUTO_MAX_OFFSET = 52;

    /**
     * Konstruktor a road hint alapadatainak megadásához.
     *
     * @param roadId az út azonosítója
     * @param shape az út alakja (pl. "straight", "curve")
     * @param controlOffset görbületi kontrollpont eltolás pixelekben
     * @param nodeAlignment csomóponthoz illesztés (pl. "perpendicular", "parallel")
     */
    public RoadLayoutHint(String roadId, String shape, int controlOffset, String nodeAlignment) {
        this.roadId = roadId;
        this.shape = shape == null ? "" : shape;
        this.controlOffset = controlOffset;
        this.nodeAlignment = nodeAlignment == null ? "" : nodeAlignment;
    }

    /**
     * Visszaadja az út azonosítóját.
     *
     * @return road azonosító
     */
    public String getRoadId() {
        return roadId;
    }

    /**
     * Visszaadja az út alakját.
     *
     * @return alak (shape)
     */
    public String getShape() {
        return shape;
    }

    /**
     * Visszaadja a görbületi kontrollpont eltolást.
     *
     * @return kontrollpont eltolás pixelben
     */
    public int getControlOffset() {
        return controlOffset;
    }

    /**
     * Visszaadja a node illesztési módot.
     *
     * @return nodeAlignment értéke
     */
    public String getNodeAlignment() {
        return nodeAlignment;
    }

    /**
     * Igaz, ha a road görbítése engedélyezett.
     * Alapértelmezetten (üres shape esetén) is görbének tekintjük,
     * hogy hint nélkül is kapjunk vizuális ívet.
     *
     * @return true, ha görbe rajzolás legyen
     */
    public boolean isCurveEnabled() {
        if (shape == null || shape.trim().isEmpty()) {
            return true;
        }
        return !"straight".equalsIgnoreCase(shape.trim());
    }

    /**
     * Visszaadja a használható kontrollpont-eltolást.
     * Ha explicit offset van megadva (nem 0), azt használja.
     * Egyébként determinisztikus auto-görbületet számol roadId alapján.
     *
     * @param laneCount a roadhoz tartozó sávok száma
     * @param segmentLength a két node közti szakaszhossz
     * @return kontrollpont-eltolás pixelben
     */
    public int resolveAutoControlOffset(int laneCount, double segmentLength) {
        if (controlOffset != 0) {
            return controlOffset;
        }

        String key = roadId == null ? "road" : roadId;
        int hash = Math.abs(key.hashCode());

        int base = AUTO_MIN_OFFSET + (hash % (AUTO_MAX_OFFSET - AUTO_MIN_OFFSET + 1));

        // Több sávnál picit hangsúlyosabb ív
        int laneBoost = Math.max(0, laneCount - 1) * 4;

        // Nagyon rövid szakasznál ne legyen túl nagy az ív
        int lengthCap = (int) Math.max(14, Math.min(58, segmentLength * 0.22));

        return Math.min(base + laneBoost, lengthCap);
    }

    /**
     * Meghatározza, hogy az adott irányvektorhoz melyik node-tengely van közelebb:
     * - "parallel": a node elnyújtásának irányához (vízszintes tengely) közelibb
     * - "perpendicular": a merőleges tengelyhez (függőleges) közelibb
     *
     * @param ux normalizált irányvektor x komponense
     * @param uy normalizált irányvektor y komponense
     * @return "parallel" vagy "perpendicular"
     */
    public String resolveAlignmentMode(double ux, double uy) {
        if (nodeAlignment != null && !nodeAlignment.trim().isEmpty() && !"auto".equalsIgnoreCase(nodeAlignment.trim())) {
            return nodeAlignment.trim().toLowerCase();
        }
        return Math.abs(ux) >= Math.abs(uy) ? "parallel" : "perpendicular";
    }

    /**
     * Start oldali érintőirány meghatározása.
     *
     * @param ux normalizált szakaszirány x
     * @param uy normalizált szakaszirány y
     * @return [tx, ty] egységvektor
     */
    public double[] resolveStartTangent(double ux, double uy) {
        String mode = resolveAlignmentMode(ux, uy);
        if ("parallel".equals(mode)) {
            // vízszintes tengely mentén induljon: a cél felé mutató előjellel
            return new double[] { ux >= 0 ? 1.0 : -1.0, 0.0 };
        }
        // függőleges tengely mentén induljon
        return new double[] { 0.0, uy >= 0 ? 1.0 : -1.0 };
    }

    /**
     * End oldali érintőirány meghatározása (a cél node-ba "befelé" érkezéshez).
     *
     * @param ux normalizált szakaszirány x
     * @param uy normalizált szakaszirány y
     * @return [tx, ty] egységvektor
     */
    public double[] resolveEndTangent(double ux, double uy) {
        String mode = resolveAlignmentMode(ux, uy);
        if ("parallel".equals(mode)) {
            return new double[] { ux >= 0 ? -1.0 : 1.0, 0.0 };
        }
        return new double[] { 0.0, uy >= 0 ? -1.0 : 1.0 };
    }
}
