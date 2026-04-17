package equipments;

/**
 * Fogyóeszközök (például só, kavics, biokerozin) interfésze.
 */
public interface Consumable {
    /**
     * Visszaadja a rendelkezésre álló mennyiséget.
     */
    int getAmount();
    
    /**
     * Beállítja a mennyiséget.
     */
    void setAmount(int amount);

    /**
     * Elhasznál egy egységet a fogyóeszközből.
     */
    void use();
}
