package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Összeköti a felhasználó által a bemeneten megadott azonosítókat
 * (ID-kat, pl. "lane1") a tényleges memóriában lévő Java objektumokkal.
 */
public class ObjectRegistry {
    private Map<String, Object> objects = new HashMap<>();


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


    // --- METÓDUSOK ---

    /**
     * Új objektum bejegyzése a regiszterbe.
     * 
     * @param id Az objektum szöveges azonosítója
     * @param obj A tárolandó Java objektum referenciája
     * @throws Exception Ha az adott azonosítóval már létezik regisztrált bejegyzés
     */
    public void register(String id, Object obj) throws Exception {
        if (objects.containsKey(id)) {
            throw new Exception("Object already exists with ID: " + id);
        }
        objects.put(id, obj);
    }

    /**
     * Visszaadja a megadott azonosítóhoz tartozó objektumot.
     * @param id A keresett objektum azonosítója
     * @return A megtalált Java objektum
     * @throws Exception Ha az objektum nem található a regiszterben
     */
    public Object getObject(String id) throws Exception {
        Object obj = objects.get(id);
        if (obj == null) {
            throw new Exception("Object not found: " + id);
        }
        return obj;
    }

    /**
     * Visszakeresi egy objektum registry ID-ját a referenciája alapján.
     *
     * @param obj a keresett objektum referenciája
     * @return az objektum ID-ja, vagy "?" ha nem található
     */
    public String findId(Object obj) {
        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            if (entry.getValue() == obj) {
                return entry.getKey();
            }
        }
        return "?";
    }
}