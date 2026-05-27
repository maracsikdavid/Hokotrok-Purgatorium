package cli;

/**
 * Közös interfész minden olyan entitásnak, amely parancssori "action" hívásokat
 * tud fogadni. Az entitás maga dönti el, hogyan értelmezi a kapott akciónevet
    * és paramétereket — ezzel elkerüljük a típusfüggő elágazásokat és a Reflection-t.
 */
public interface Actionable {

    /**
     * Végrehajtja a megnevezett akciót a megadott paraméterekkel.
     *
     * @param actionName az akció neve (pl. "clearLane", "buyItem")
     * @param args       a parancssor további paraméterei (ID-k, enum nevek stb.)
     * @param registry   a központi objektumtár, amiből a paraméter-objektumok kikereshetők
     * @throws Exception ha az akció sikertelen (pl. ismeretlen akciónév, hibás paraméter)
     */
    void performAction(String actionName, String[] args, ObjectRegistry registry) throws Exception;
}
