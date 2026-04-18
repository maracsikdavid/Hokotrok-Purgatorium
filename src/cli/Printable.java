package cli;

/**
 * Közös interfész minden olyan entitásnak, amelynek belső állapota
 * szöveges formátumban kiírható a konzolra (a "data" parancs során).
 */
public interface Printable {

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     *
     * @param id       az objektum egyedi azonosítója, amellyel a Registry-ben szerepel
     * @param registry a központi objektumtár (ID-k visszakeresésére hivatkozott objektumokhoz)
     */
    void printData(String id, ObjectRegistry registry);
}
