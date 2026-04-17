package equipments;

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

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id);
}