package gui.layout;

import java.nio.file.Path;

/**
 * A grafikus layout fájlok feldolgozásának belépési pontja.
 * A részletes parse logika későbbi implementációra van előkészítve.
 */
public class MapLayoutParser {

    /**
     * Feldolgozza a megadott layout fájlt és visszaadja a hozzá tartozó layout objektumot.
     *
     * @param file a feldolgozandó layout fájl útvonala
     * @return az elkészített layout objektum
     * @throws Exception ha a későbbi parse folyamat hibát talál
     */
    public MapLayout parse(Path file) throws Exception {
        return new MapLayout();
    }

    /**
     * Egyetlen layout sor feldolgozásának későbbi helye.
     *
     * @param line a feldolgozandó sor
     * @throws Exception ha a sor formátuma hibás
     */
    public void parseLine(String line) throws Exception {
    }
}