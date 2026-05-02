package core;

/**
 * A Takarító (Cleaner) pénzügyeit és vagyonát kezelő osztály. 
 * Felelős a sikeres takarításért kapott jutalmak (érmék) tárolásáért, 
 * valamint a Boltban (Shop) történő vásárlások (pl. új kotrófejek, só, kerozin) 
 * fedezetének biztosításáért és a tranzakciók utáni levonásokért.
 */
public class Wallet implements cli.Linkable, cli.Printable {
    private int amount;

    
    /**
     * Alapértelmezett konstruktor.
     */
    public Wallet() {
    }

    /**
     * Paraméteres konstruktor a kezdőegyenleg megadásához.
     *
     * @param amount a kezdő egyenleg
     */
    public Wallet(int amount) {
        this.amount = amount;
    }



    /**
     * Visszaadja a pénztárca aktuális egyenlegét.
     *
     * @return az aktuális egyenleg
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Beállítja a pénztárca egyenlegét.
     *
     * @param amount a beállítandó egyenleg
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }



    /**
     * Növeli a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően egy problémás (havas vagy jeges) sáv sikeres letakarítása 
     * (clearLane) után hívódik meg a jutalom jóváírására.
     *
     * @param amount A hozzáadandó (jóváírandó) érmék mennyisége.
     *
     * Pszeudokód:
     * 1. Lekéri az aktuális amount értéket.
     * 2. Hozzáadja a paraméterként kapott mennyiséget.
     */
    public void add(int amount) {
        if (amount <= 0) {
            return;
        }
        this.amount += amount;
    }

    /**
     * Csökkenti a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően a Boltban (Shop) történő sikeres vásárlás (tryPurchase) 
     * esetén hívódik meg a vételár levonására.
     *
     * @param amount Az elköltött (levonandó) érmék mennyisége.
     * @throws IllegalStateException Ha nincs elegendő fedezet a pénztárcában.
     *
     * Pszeudokód:
     * 1. Ellenőrzi, hogy van-e elegendő fedezet.
     * 2. Ha igen, levonja az összeget.
     * 3. Ha nem, hibát jelez.
     */
    public void spend(int amount) {
        if (amount <= 0) {
            return;
        }
        if (this.amount < amount) {
            throw new IllegalStateException("Action failed: Insufficient funds in Wallet.");
        }
        this.amount -= amount;
    }

    /**
     * Összekapcsolja a pénztárca tulajdonságait a parancssori argumentumok alapján.
     *
     * @param property A beállítandó tulajdonság neve.
     * @param args A paraméterértékek.
     * @param registry A központi objektumtár.
     * @throws Exception Ha a property ismeretlen.
     */
    @Override
    public void performLink(String property, String[] args, cli.ObjectRegistry registry) throws Exception {
        switch (property) {
            case "amount":
            case "setAmount":
                setAmount(Integer.parseInt(args[0]));
                break;
            default:
                throw new Exception("Unknown link property '" + property + "' for Wallet");
        }
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     *
     * @param id Az objektum egyedi azonosítója a regiszterben.
     * @param registry A központi objektumtár az azonosítók feloldásához.
     */
    @Override
    public void printData(String id, cli.ObjectRegistry registry) {
        System.out.println("Wallet," + id);
        System.out.println("amount," + amount);
    }
}
