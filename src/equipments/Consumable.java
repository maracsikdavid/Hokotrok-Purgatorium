package equipments;

import cli.ObjectRegistry;

/**
 * Fogyóeszközök (például só, kavics, biokerozin) interfésze.
 */
public interface Consumable {

    // --- METÓDUSOK ---

    /**
     * Visszaadja a rendelkezésre álló mennyiséget.
     * 
     * @return az aktuális mennyisége.
     */
    int getAmount();
    
    /**
     * Beállítja a mennyiséget.
     * 
     * @param amount a beállítandó mennyiség
     */
    void setAmount(int amount);

    /**
     * Elhasznál egy egységet a fogyóeszközből.
     */
    void use();

    /** @return A fogyóéjű anyag típusát azonosító szöveg (pl. "Salt", "Gravel", "Biokerosene"). */
    String getConsumableType();

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     *
     * @param id az objektum egyedi azonosítója
     * @param registry a központi objektumtár
     */
    void printData(String id, ObjectRegistry registry);
}