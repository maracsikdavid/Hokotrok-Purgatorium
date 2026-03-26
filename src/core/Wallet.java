package core;

/**
 * A Takarító (Cleaner) pénzügyeit és vagyonát kezelő osztály. 
 * Felelős a sikeres takarításért kapott jutalmak (érmék) tárolásáért, 
 * valamint a Boltban (Shop) történő vásárlások (pl. új kotrófejek, só, kerozin) 
 * fedezetének biztosításáért és a tranzakciók utáni levonásokért.
 */
public class Wallet {
    
    /**
     * A pénztárcában aktuálisan rendelkezésre álló pénzmennyiség (érmék száma).
     */
    public int amount;

    /**
     * Növeli a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően egy problémás (havas vagy jeges) sáv sikeres letakarítása 
     * (clearLane) után hívódik meg a jutalom jóváírására.
     *
     * @param amount a hozzáadandó (jóváírandó) érmék mennyisége
     */
    public void add(int amount) {
        Skeleton.printCall(null, this, "add");
        this.amount += amount;
        Skeleton.printReturn(this, "add");
    }

    /**
     * Csökkenti a pénztárca egyenlegét a megadott összeggel. 
     * Jellemzően a Boltban (Shop) történő sikeres vásárlás (tryPurchase) 
     * esetén hívódik meg a vételár levonására.
     *
     * @param amount az elköltött (levonandó) érmék mennyisége
     */
    public void spend(int amount) {
        Skeleton.printCall(null, this, "spend");
        this.amount -= amount;
        Skeleton.printReturn(this, "spend");
    }
}