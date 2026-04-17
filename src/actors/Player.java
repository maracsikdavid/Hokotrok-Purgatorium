package actors;

/**
 * Absztrakt ősosztály a szimulációban szereplő felhasználókat/aktorokat 
 * (takarító, buszsofőr) reprezentáló osztályokhoz.
 * Biztosítja a közös attribútumokat (például a nevet) és a polimorfikus kezelést.
 */
public abstract class Player {
    private String name;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public Player() {
        this.name = "";
    }

    /**
     * Paraméteres konstruktor a játékos nevének beállításához.
     *
     * @param name A játékos azonosítója vagy neve.
     */
    public Player(String name) {
        this.name = name;
    }

	
    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a játékos azonosítóját vagy nevét.
     *
     * @return a játékos neve
     */
    public String getName() {
        return name;
    }

    /**
     * Beállítja a játékos azonosítóját vagy nevét.
     *
     * @param name a beállítandó név
     */
    public void setName(String name) {
        this.name = name;
    }
}