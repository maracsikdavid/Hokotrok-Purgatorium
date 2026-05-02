package core;

/**
 * Központi játékszabály-konstansok időzítésekhez, árakhoz és valószínűségekhez.
 */
public final class GameRules {
    private GameRules() {
    }

    public static final int ROAD_TRAVERSAL_TICKS = 5;
    public static final int DEFAULT_LANE_LENGTH = ROAD_TRAVERSAL_TICKS;

    public static final int SALT_ACTIVATION_TICKS = 2;
    public static final int SALT_SNOW_IMMUNITY_TICKS = 4;

    public static final int THIN_SNOW_AFTER_TICKS = 5;
    public static final int THICK_SNOW_AFTER_ADDITIONAL_TICKS = 5;
    public static final int ICE_TRAMPLE_THRESHOLD = 20;

    public static final int COLLISION_PARALYZE_TICKS = 2;
    public static final double ICE_SLIP_PROBABILITY = 0.20;

    public static final int CAR_WORK_WAIT_TICKS = 10;
    public static final int CAR_HOME_WAIT_TICKS = 20;

    public static final int PRICE_SALT = 2;
    public static final int PRICE_GRAVEL = 2;
    public static final int PRICE_BIOKEROSENE = 4;

    public static final int PRICE_SWEEPER_PLOW = 0;
    public static final int PRICE_GRAVEL_PLOW = 8;
    public static final int PRICE_DUMP_PLOW = 10;
    public static final int PRICE_ICEBREAKER_PLOW = 10;
    public static final int PRICE_SALT_PLOW = 12;
    public static final int PRICE_DRAGON_PLOW = 15;
    public static final int PRICE_SNOWPLOW = 25;
}