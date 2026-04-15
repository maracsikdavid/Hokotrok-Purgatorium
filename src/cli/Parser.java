package cli;

import java.util.HashMap;
import java.util.Map;

/**
 * A bemeneti parancsok (sorok) értelmezéséért felelős központi osztály.
 * Felbontja a kapott sorokat, kikeresi a megfelelő {@link CommandFactory}-t, 
 * legyártja a parancsot, majd menedzseli a validálást és futtatást.
 */
public class Parser {
    private ObjectRegistry registry;
    private Map<String, CommandFactory> factories;

    // --- GETTEREK ÉS SETTEREK ---
    /**
     * Visszaadja az objektumregisztert.
     *
     * @return az objektumregiszter
     */
    public ObjectRegistry getRegistry() {
        return registry;
    }

    /**
     * Beállítja az objektumregisztert.
     *
     * @param registry a beállítandó regiszter
     */
    public void setRegistry(ObjectRegistry registry) {
        this.registry = registry;
    }

    /**
     * Visszaadja a parancsgyárakat.
     *
     * @return a gyártípusokat és factory-kat tartalmazó map
     */
    public Map<String, CommandFactory> getFactories() {
        return factories;
    }

    /**
     * Beállítja a parancsgyárakat.
     *
     * @param factories a beállítandó factory map
     */
    public void setFactories(Map<String, CommandFactory> factories) {
        this.factories = factories;
    }

    // --- KONSTRUKTOROK ---

    /**
     * Konstruktor, amely inicializálja a memóriatérképet és a Factory-kat.
     */
    public Parser() {
        this.registry = new ObjectRegistry();
        this.factories = new HashMap<>();
    }

    /**
     * Paraméteres konstruktor futási módhoz.
     *
     * @param mode a futási mód (0: test, 1: játék)
     */
    public Parser(int mode) {
        this();
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param registry az objektumregiszter
     * @param factories a parancsgyárak tárolója
     */
    public Parser(ObjectRegistry registry, Map<String, CommandFactory> factories) {
        this.registry = registry;
        this.factories = factories;
    }

    // --- METÓDUSOK ---
    /**
     * Egyetlen bemeneti sor feldolgozása. Kiszűri a kommenteket és az üres sorokat,
     * majd a validálás után végrehajtja a parancsot. Hibás bemenet esetén 
     * szabályozott hibaüzenetet ír a konzolra.
     * 
     * @param line A feldolgozandó, szabványos formátumú bemeneti sor
     */
    public void parseLine(String line) {

    }
}