package actors;

import core.Shop;
import core.ShopItem;
import core.Wallet;
import entities.Snowplow;
import equipments.Consumable;
import equipments.Plow;

import java.util.ArrayList;
import java.util.List;
import topology.Lane;
import topology.Road;

/**
 * A takarító operátor a szimulációban. Feladata a hókotrók irányítása, felszerelések vásárlása,
 * a gépek felszerelésének kezelése, nyersanyagok utántöltése a saját raktárából, 
 * valamint a sikeres hótakarítás után érmék gyűjtése.
 */
public class Cleaner extends Player {
    private Wallet wallet = new Wallet();
    private List<Snowplow> fleet = new ArrayList<>();
    private List<Consumable> inventory = new ArrayList<>();


    // --- KONSTRUKTOROK ---
	
    /**
     * Alapértelmezett konstruktor.
     */
    public Cleaner() {
        super();
    }

    /**
     * Paraméteres konstruktor a név megadásához.
     *
     * @param name a takarító neve
     */
    public Cleaner(String name) {
        super(name);
    }

    /**
     * Paraméteres konstruktor minden fő attribútummal.
     *
     * @param name a takarító neve
     * @param wallet a takarító pénztárcája
     * @param fleet a takarító hókotró flottája
     */
    public Cleaner(String name, Wallet wallet, List<Snowplow> fleet) {
        super(name);
        this.wallet = wallet;
        this.fleet = fleet;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a takarító pénztárcáját.
     *
     * @return a pénztárca
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Beállítja a takarító pénztárcáját.
     *
     * @param wallet a beállítandó pénztárca
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Visszaadja a takarító hókotró flottáját.
     *
     * @return a hókotrók listája
     */
    public List<Snowplow> getFleet() {
        return fleet;
    }

    /**
     * Beállítja a takarító hókotró flottáját.
     *
     * @param fleet a beállítandó flotta
     */
    public void setFleet(List<Snowplow> fleet) {
        this.fleet = fleet;
    }

    /**
     * Visszaadja a takarító raktárát (fogyóeszközök).
     *
     * @return a raktár tartalma
     */
    public List<Consumable> getInventory() {
        return inventory;
    }

    /**
     * Beállítja a takarító raktárát.
     *
     * @param inventory a beállítandó raktár
     */
    public void setInventory(List<Consumable> inventory) {
        this.inventory = inventory;
    }


    // --- METÓDUSOK ---

    /**
     * A hókotrót irányítja a megadott útra és sávra (sávváltás).
     *
     * @param sp az irányítandó hókotró
     * @param toRoad a cél úthálózat
     * @param toLane a cél sáv az útban
     */
    public void commandSnowplow(Snowplow sp, Road toRoad, Lane toLane) {
    }

    /**
     * A takarító egy felszerelést vásárol a boltból (pl. sót, biokerozint vagy kotrófejeket).
     * Kotrófej vásárlása esetén meg kell adni a cél hókotrót, nyersanyagnál ez null lehet.
     *
     * @param shop a bolt, ahonnan vásárol
     * @param item a megvásárolni kívánt felszerelés azonosítója
     * @param targetSp a cél hókotró (csak kotrófej vásárlásakor kötelező)
     */
    public void buyItem(Shop shop, ShopItem item, Snowplow targetSp) {
    }

    /**
     * Utasítja a hókotrót, hogy szerelje fel az adott típusú fejet a saját készletéből.
     *
     * @param sp a módosítandó hókotró
     * @param plowClass a felszerelni kívánt kotrófej típusa
     */
    public void equipPlowToSnowplow(Snowplow sp, Class<? extends Plow> plowClass) {
    }

    /**
     * Feltölti a hókotróra szerelt kotrófej tartályát a megadott nyersanyaggal, 
     * kivéve azt a takarító raktárából.
     *
     * @param sp a hókotró, amelynek a tartályát tölteni kell
     * @param resource a betöltendő nyersanyag példány a raktárból
     */
    public void refillPlow(Snowplow sp, Consumable resource) {
    }

    /**
     * Új nyersanyagot ad a takarító raktárához (vásárlás után).
     *
     * @param c a megvásárolt nyersanyag
     */
    public void addConsumable(Consumable c) {
    }

    /**
     * A takarító érméket szerez a sikeres hótakarítás után. Ez a metódus a Hókotró
     * (Snowplow) osztály által hívódik meg a sikeres tisztítás után.
     */
    public void achieveCoin() {
    }

    /**
     * Az objektum aktuális állapotának és attribútumainak kiírása a standard kimenetre.
     * * @param id Az objektum egyedi azonosítója, amellyel a Registry-ben szerepel.
     */
    public void printData(String id) {
    }
}