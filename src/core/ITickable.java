package core;

/**
 * Minden olyan entitásnak és komponensnek (például a Járműveknek, a Sávoknak, 
 * vagy a Sávokhoz tartozó Időjárási állapotoknak) meg kell valósítania ezt az 
 * interfészt, amely a globális idő múlásával (tick) dinamikusan megváltoztatja 
 * a belső állapotát, helyzetét, vagy valamilyen időhöz kötött eseményt hajt végre.
 */
public interface ITickable {
    
    /**
     * A globális időzítő egyetlen ütemére lefutó állapotfrissítő metódus.
     * Ennek a metódusnak a meghívásakor az implementáló objektum
     * elvégzi a rá vonatkozó üzleti logikát a szimuláció szabályai szerint.
     */
    void tick();
}