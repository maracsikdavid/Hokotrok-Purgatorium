package actors;

import cli.ObjectRegistry;
import cli.Printable;

/**
 * Absztrakt ősosztály a szimulációban szereplő felhasználókat/aktorokat 
 * (takarító, buszsofőr) reprezentáló osztályokhoz.
 * Biztosítja a közös attribútumokat (például a nevet) és a polimorfikus kezelést.
 */
public abstract class Player implements Printable {
    private String name;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    protected  Player() {
        this.name = "";
    }

    /**
     * Paraméteres konstruktor a játékos nevének beállításához.
     *
     * @param name A játékos azonosítója vagy neve.
     */
    protected Player(String name) {
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


    // --- METÓDUSOK ---

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     *
     * @param id az objektum egyedi azonosítója
     * @param registry a központi objektumtár
     */
    @Override
    public void printData(String id, ObjectRegistry registry) {
        System.out.println("Player," + id);
        System.out.println("name," + name);
    }
}