package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Összeköti a felhasználó által a bemeneten megadott azonosítókat
 * (ID-kat, pl. "lane1") a tényleges memóriában lévő Java objektumokkal.
 */
public class ObjectRegistry {
    private Map<String, Object> objects = new HashMap<>();



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
     * Visszaadja a regisztrált objektumok tárolóját.
     *
     * @return Az azonosító-objektum párokat tartalmazó Map.
     */
    public Map<String, Object> getObjects() {
        return objects;
    }

    /**
     * Beállítja a regisztrált objektumok tárolóját.
     *
     * @param objects Az új tároló Map.
     */
    public void setObjects(Map<String, Object> objects) {
        this.objects = objects;
    }



    /**
     * Új objektum regisztrálása a tárban egy egyedi azonosítóval.
     * 
     * @param id Az objektum szöveges azonosítója (pl. "lane1").
     * @param obj A tárolandó Java objektum referenciája.
     * @throws Exception Ha az adott azonosítóval már létezik regisztrált objektum.
     */
    public void register(String id, Object obj) throws Exception {
        if (objects.containsKey(id)) {
            throw new Exception("Object already exists with ID: " + id);
        }
        objects.put(id, obj);
    }

    /**
     * Visszaadja a megadott azonosítóhoz tartozó objektumot a tárból.
     * 
     * @param id A keresett objektum azonosítója.
     * @return A megtalált Java objektum.
     * @throws Exception Ha az objektum nem található a tárban.
     */
    public Object getObject(String id) throws Exception {
        Object obj = objects.get(id);
        if (obj == null) {
            throw new Exception("Object not found: " + id);
        }
        return obj;
    }

    /**
    * Visszaadja az összes regisztrált objektumot, amelyek a megadott típus példányai.
    * Így a hívó kód típusbiztos listát kap explicit típusellenőrzés és
    * {@code ClassCastException} kezelése nélkül.
     *
     * @param <T>  A kért típus.
     * @param type A kért típus {@link Class} objektuma.
     * @return Az egyező típusú objektumok típusbiztos listája.
     */
    public <T> java.util.List<T> getByType(Class<T> type) {
        java.util.List<T> result = new java.util.ArrayList<>();
        for (Object obj : objects.values()) {
            if (type.isInstance(obj)) {
                result.add(type.cast(obj));
            }
        }
        return result;
    }

    /**
     * Visszakeresi egy objektum azonosítóját a memóriabeli referenciája alapján.
     *
     * @param obj A keresett objektum referenciája.
     * @return Az objektum szöveges azonosítója, vagy "?" ha az objektum nincs regisztrálva.
     */
    public String findId(Object obj) {
        if (obj == null) return "null";
        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            if (entry.getValue() == obj) {
                return entry.getKey();
            }
        }
        return "?";
    }
}
