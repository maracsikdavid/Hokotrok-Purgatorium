package cli;

/**
 * Közös interfész a parancstervezési minta (Command Pattern) megvalósításához.
 * Minden bemeneti parancs egy ebből származó objektummá alakul, amely egységbe zárja
 * a végrehajtáshoz szükséges logikát és paramétereket.
 */
public interface Command {

    
    /**
     * Ellenőrzi, hogy a parancs paraméterei és a hivatkozott objektumok érvényesek-e.
     * Ezt a metódust a végrehajtás előtt kell meghívni a hibák elkerülése végett.
     * 
     * * @return true, ha a parancs érvényes és végrehajtható, egyébként false
     */
    boolean validate();
    
    /**
     * Végrehajtja a parancshoz tartozó üzleti logikát a modellen (pl. állapotváltozás, 
     * mozgás, létrehozás).
     */
    void execute();
}
