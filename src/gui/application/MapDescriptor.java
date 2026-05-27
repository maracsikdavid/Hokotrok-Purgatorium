package gui.application;

/**
 * Egy grafikus módban választható pálya alapadatait tartalmazó leíró objektum.
 * A GUI ebből olvassa ki a megjelenített nevet, a rövid leírást és a kapcsolódó fájlutakat.
 */
public class MapDescriptor {
    private String id;
    private String displayName;
    private String description;
    private String difficulty;
    private String initFilePath;
    private String layoutFilePath;

    /**
     * Alapértelmezett konstruktor későbbi kézi feltöltéshez.
     */
    public MapDescriptor() {
    }

    /**
     * Teljes konstruktor a pályaleíró minden mezőjének beállításához.
     *
     * @param id a pálya belső azonosítója
     * @param displayName a felhasználónak megjelenített pályanév
     * @param description rövid pályaleírás
     * @param difficulty a pálya nehézségi szintje
     * @param initFilePath a modell init fájl relatív útvonala
     * @param layoutFilePath a grafikus layout fájl relatív útvonala
     */
    public MapDescriptor(String id, String displayName, String description, String difficulty,
                         String initFilePath, String layoutFilePath) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.difficulty = difficulty;
        this.initFilePath = initFilePath;
        this.layoutFilePath = layoutFilePath;
    }

    /**
     * Visszaadja a pálya belső azonosítóját.
     *
     * @return a pálya azonosítója
     */
    public String getId() {
        return id;
    }

    /**
     * Beállítja a pálya belső azonosítóját.
     *
     * @param id az új azonosító
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Visszaadja a felületen megjelenő pályanevet.
     *
     * @return a megjelenített pályanév
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Beállítja a felületen megjelenő pályanevet.
     *
     * @param displayName az új megjelenített név
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Visszaadja a pálya rövid leírását.
     *
     * @return a pályaleírás
     */
    public String getDescription() {
        return description;
    }

    /**
     * Beállítja a pálya rövid leírását.
     *
     * @param description az új pályaleírás
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Visszaadja a pálya nehézségi szintjét.
     *
     * @return a nehézségi szint
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Beállítja a pálya nehézségi szintjét.
     *
     * @param difficulty az új nehézségi szint
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Visszaadja a modell init fájl relatív útvonalát.
     *
     * @return az init fájl útvonala
     */
    public String getInitFilePath() {
        return initFilePath;
    }

    /**
     * Beállítja a modell init fájl relatív útvonalát.
     *
     * @param initFilePath az új init fájl útvonal
     */
    public void setInitFilePath(String initFilePath) {
        this.initFilePath = initFilePath;
    }

    /**
     * Visszaadja a grafikus layout fájl relatív útvonalát.
     *
     * @return a layout fájl útvonala
     */
    public String getLayoutFilePath() {
        return layoutFilePath;
    }

    /**
     * Beállítja a grafikus layout fájl relatív útvonalát.
     *
     * @param layoutFilePath az új layout fájl útvonal
     */
    public void setLayoutFilePath(String layoutFilePath) {
        this.layoutFilePath = layoutFilePath;
    }
}