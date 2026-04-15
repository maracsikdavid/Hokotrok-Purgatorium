package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Összeköti a felhasználó által a bemeneten megadott azonosítókat
 * (ID-kat, pl. "lane1") a tényleges memóriában lévő Java objektumokkal.
 */
public class ObjectRegistry {
    private Map<String, Object> objects = new HashMap<>();

    // --- GETTEREK ÉS SETTEREK ---
    /**
     * Visszaadja a regisztrált objektumok tárolóját.
     *
     * @return az azonosító-objektum párokat tartalmazó map
     */
    public Map<String, Object> getObjects() {
        return objects;
    }

    /**
     * Beállítja a regisztrált objektumok tárolóját.
     *
     * @param objects az új tároló map
     */
    public void setObjects(Map<String, Object> objects) {
        this.objects = objects;
    }

    // --- KONSTRUKTOROK ---
    /**
     * Alapértelmezett konstruktor.
     */
    public ObjectRegistry() {
    }

    /**
     * Paraméteres konstruktor a tároló map megadásához.
     *
     * @param objects az inicializáló tároló map
     */
    public ObjectRegistry(Map<String, Object> objects) {
        this.objects = objects;
    }

    /**
     * Új objektum bejegyzése a regiszterbe.
     * 
     * @param id Az objektum szöveges azonosítója
     * @param obj A tárolandó Java objektum referenciája
     * @throws Exception Ha az adott azonosítóval már létezik regisztrált bejegyzés
     */
    public void register(String id, Object obj) throws Exception {

    }

    /**
     * Visszaadja a megadott azonosítóhoz tartozó objektumot.
     * @param id A keresett objektum azonosítója
     * @return A megtalált Java objektum
     * @throws Exception Ha az objektum nem található a regiszterben
     */
    public Object getObject(String id) throws Exception {
        return null;
    }
}