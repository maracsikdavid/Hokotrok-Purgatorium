package cli;

/**
 * Közös interfész minden olyan entitásnak, amely parancssori "link" hívásokat
 * tud fogadni az objektumok összekapcsolásához és tulajdonságok beállításához
 * a tesztkörnyezet "Arrange" fázisában.
 */
public interface Linkable {

    /**
     * Beállítja a megnevezett tulajdonságot vagy végrehajtja a megnevezett
     * összekapcsolási műveletet a megadott paraméterekkel.
     *
     * @param property a beállítandó tulajdonság vagy metódus neve (pl. "setState", "addLane")
     * @param args     a parancssor további paraméterei (ID-k, típusnevek, értékek)
     * @param registry a központi objektumtár
     * @throws Exception ha a művelet sikertelen
     */
    void performLink(String property, String[] args, ObjectRegistry registry) throws Exception;
}
