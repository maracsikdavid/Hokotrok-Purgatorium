package core;

/**
 * Központi játékszabály-konstansok időzítésekhez, árakhoz és valószínűségekhez.
 */
public final class GameRules {
    private GameRules() {
    }

    /**
     * A konzolos mód fix pályaleíró fájlja.
     */
    public static final String DEFAULT_CONSOLE_MAP_FILE = "maps/console-map-init.txt";

    private static String mapFileName = DEFAULT_CONSOLE_MAP_FILE;

    /**
     * Visszaadja a Game mód indulásakor betöltendő pályaleíró fájlt.
     *
     * @return az aktuális pálya init fájl útvonala
     */
    public static String getMapFileName() {
        return mapFileName;
    }

    /**
     * Beállítja a Game mód indulásakor betöltendő pályaleíró fájlt.
     *
     * @param newMapFileName az új pálya init fájl útvonala
     */
    public static void setMapFileName(String newMapFileName) {
        if (newMapFileName == null || newMapFileName.isBlank()) {
            mapFileName = DEFAULT_CONSOLE_MAP_FILE;
            return;
        }
        mapFileName = newMapFileName;
    }

    public static final int ROAD_TRAVERSAL_TICKS = 5;
    public static final int DEFAULT_LANE_LENGTH = ROAD_TRAVERSAL_TICKS;

    public static final int SALT_ACTIVATION_TICKS = 2;
    public static final int SALT_SNOW_IMMUNITY_TICKS = 4;

    public static final int THIN_SNOW_AFTER_TICKS = 5;
    public static final int THICK_SNOW_AFTER_ADDITIONAL_TICKS = 5;
    public static final int ICE_TRAMPLE_THRESHOLD = 20;

    public static final int VEHICLE_BLOCK_TICKS = 2;
    public static final int COLLISION_PARALYZE_TICKS = VEHICLE_BLOCK_TICKS;
    public static final int THICK_SNOW_BLOCK_TICKS = VEHICLE_BLOCK_TICKS;
    public static final double ICE_SLIP_PROBABILITY = 0.20;

    public static final int CAR_WORK_WAIT_TICKS = 10;
    public static final int CAR_HOME_WAIT_TICKS = 20;

    public static final int STARTING_CLEANER_MONEY = 200;

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
