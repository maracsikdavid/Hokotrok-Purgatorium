package gui.swing;

import core.ShopItem;
import java.awt.Color;

/**
 * Kozos GUI szinpaletta a terkep, jarmuvek, ekek es bolti termekek egyeztetett megjelenitesehez.
 */
public final class GameColors {
    public static final Color ROAD_CLEAN = Color.BLACK;
    public static final Color ROAD_THIN_SNOW = new Color(209, 213, 219);
    public static final Color ROAD_THICK_SNOW = Color.WHITE;
    public static final Color ROAD_ICE = new Color(125, 211, 252);
    public static final Color ROAD_SALTED_ICE = new Color(30, 64, 175);
    public static final Color ROAD_GRAVEL = new Color(146, 64, 14);

    public static final Color BUS = new Color(250, 204, 21);
    public static final Color CAR = new Color(220, 38, 38);
    public static final Color SNOWPLOW = new Color(22, 163, 74);
    public static final Color INTERSECTION = ROAD_CLEAN;

    public static final Color PLOW_SWEEPER = new Color(100, 116, 139);
    public static final Color PLOW_DRAGON = new Color(244, 63, 94);
    public static final Color PLOW_SALT = ROAD_SALTED_ICE;
    public static final Color PLOW_DUMP = new Color(249, 115, 22);
    public static final Color PLOW_ICEBREAKER = new Color(6, 182, 212);
    public static final Color PLOW_GRAVEL = ROAD_GRAVEL;

    private static final Color BIOKEROSENE = new Color(234, 88, 12);

    private GameColors() {
    }

    public static Color laneColor(String normalizedCondition, boolean saltedIce) {
        if ("thicksnow".equals(normalizedCondition)) {
            return ROAD_THICK_SNOW;
        }
        if ("thinsnow".equals(normalizedCondition)) {
            return ROAD_THIN_SNOW;
        }
        if ("ice".equals(normalizedCondition)) {
            return saltedIce ? ROAD_SALTED_ICE : ROAD_ICE;
        }
        if ("graveledice".equals(normalizedCondition)) {
            return ROAD_GRAVEL;
        }
        return ROAD_CLEAN;
    }

    public static Color plowColor(String plowType) {
        String normalized = normalize(plowType);
        if ("dragonplow".equals(normalized)) {
            return PLOW_DRAGON;
        }
        if ("saltplow".equals(normalized)) {
            return PLOW_SALT;
        }
        if ("dumpplow".equals(normalized)) {
            return PLOW_DUMP;
        }
        if ("icebreakerplow".equals(normalized)) {
            return PLOW_ICEBREAKER;
        }
        if ("gravelplow".equals(normalized)) {
            return PLOW_GRAVEL;
        }
        return PLOW_SWEEPER;
    }

    public static Color shopItemColor(ShopItem item) {
        if (item == null) {
            return Color.WHITE;
        }
        switch (item) {
            case Biokerosene:
                return BIOKEROSENE;
            case Salt:
                return PLOW_SALT;
            case Gravel:
                return PLOW_GRAVEL;
            case DragonPlow, SaltPlow, DumpPlow, SweeperPlow, IcebreakerPlow, GravelPlow:
                return plowColor(item.name());
            case Snowplow:
                return SNOWPLOW;
            default:
                return Color.WHITE;
        }
    }

    public static Color readableText(Color background) {
        if (background == null) {
            return Color.BLACK;
        }
        double brightness = (background.getRed() * 0.299)
                + (background.getGreen() * 0.587)
                + (background.getBlue() * 0.114);
        return brightness < 140.0 ? Color.WHITE : Color.BLACK;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}