package cli;

/**
 * Gyárosztály (Factory) interfész, amely a feldarabolt szöveges bemenet alapján 
 * legyártja a megfelelő {@link Command} objektumot.
 */
public interface CommandFactory {
    

    /**
     * Egy feldarabolt szöveges sorból (pl. ["Lane", "create", "lane1"]) legyártja 
     * a specifikus parancs objektumot.
     * 
     * @param parts A bemeneti sor vesszők mentén feldarabolt szöveges tömbje
     * @return A legyártott, futtatásra kész {@link Command} objektum
     */
    Command createCommand(String[] parts);
}
