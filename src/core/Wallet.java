package core;

/**
 * A Takarító (Cleaner) pénzügyeit és vagyonát kezelő osztály. 
 * Felelős a sikeres takarításért kapott jutalmak (érmék) tárolásáért, 
 * valamint a Boltban (Shop) történő vásárlások (pl. új kotrófejek, só, kerozin) 
 * fedezetének biztosításáért és a tranzakciók utáni levonásokért.
 */
public class Wallet {
    private int amount;

    // --- GETTEREK ÉS SETTEREK ---
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

    // --- KONSTRUKTOROK ---
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

    // --- METÓDUSOK ---
    /**
     * Növeli a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően egy problémás (havas vagy jeges) sáv sikeres letakarítása 
     * (clearLane) után hívódik meg a jutalom jóváírására.
     *
     * @param amount a hozzáadandó (jóváírandó) érmék mennyisége
     */
    public void add(int amount) {
    }

    /**
     * Csökkenti a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően a Boltban (Shop) történő sikeres vásárlás (tryPurchase) 
     * esetén hívódik meg a vételár levonására.
     *
     * @param amount az elköltött (levonandó) érmék mennyisége
     */
    public void spend(int amount) {
    }
}