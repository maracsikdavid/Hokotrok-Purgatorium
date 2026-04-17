package equipments;

/**
 * A kavics, amelyet a GravelPlow kotrófej használ.
 */
public class Gravel implements Consumable {
    private int amount;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public Gravel() {
    }

    /**
     * Paraméteres konstruktor a mennyiség megadásához.
     *
     * @param amount a kavics mennyisége
     */
    public Gravel(int amount) {
        this.amount = amount;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a kavics aktuális mennyiségét.
     *
     * @return a kavics mennyisége
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Beállítja a kavics mennyiségét.
     *
     * @param amount a beállítandó mennyiség
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    

    // --- METÓDUSOK ---

    /**
     * Felhasználja a kavicsot a kavicsszóró fej működése közben.
     */
    public void use() {

    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}