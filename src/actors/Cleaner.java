package actors;

import core.Shop;
import core.ShopItem;
import core.Wallet;
import entities.Snowplow;
import equipments.Plow;

import java.util.ArrayList;
import java.util.List;
import topology.Lane;
import topology.Road;

/**
 * A takarító operátor a szimulációban. Feladata a hókotrók irányítása, felszerelések vásárlása,
 * kotrófejet cseréje, valamint a sikeres hótakarítás után érméket gyűjtése.
 */
public class Cleaner extends Player {
	private Wallet wallet = new Wallet();
	private List<Snowplow> fleet = new ArrayList<>();
	private Plow equippedPlow = null;

	
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
	 * Visszaadja a jelenleg felszerelt kotrófejet.
	 *
	 * @return a felszerelt kotrófej
	 */
	public Plow getEquippedPlow() {
		return equippedPlow;
	}

	/**
	 * Beállítja a jelenleg felszerelt kotrófejet.
	 *
	 * @param equippedPlow a beállítandó kotrófej
	 */
	public void setEquippedPlow(Plow equippedPlow) {
		this.equippedPlow = equippedPlow;
	}

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
	 * Paraméteres konstruktor minden attribútummal.
	 *
	 * @param name a takarító neve
	 * @param wallet a takarító pénztárcája
	 * @param fleet a takarító hókotró flottája
	 * @param equippedPlow az aktuális kotrófej
	 */
	public Cleaner(String name, Wallet wallet, List<Snowplow> fleet, Plow equippedPlow) {
		super(name);
		this.wallet = wallet;
		this.fleet = fleet;
		this.equippedPlow = equippedPlow;
	}

	/**
     * Felszereli (regisztrálja) az adott kotrófejet, amelyet a {@code buyItem()}
     * inicializációs kontextusban hivatkozni fog.
	 * 
     * @param plow a felszerelendő {@link Plow} objektum
     */
    public void equipPlow(Plow plow) {
    }


	// --- METÓDUSOK ---
	/**
	 * A hókotrót irányítja a megadott útra, sávra, és az adott kotrófej alatt.
	 *
	 * @param sp az irányítandó hókotró
	 * @param toRoad a cél úthálózat
	 * @param toLane a cél sáv az útban
	 * @param withPlow az alkalmazandó kotrófej
	 */
	public void commandSnowplow(Snowplow sp, Road toRoad, Lane toLane, Plow withPlow) {
	}

	/**
	 * A takarító egy felszerelést vásárol a boltból (pl. sót, biokerozint vagy kotrófejeket).
	 * A vásárlás sikerességétől függ, hogy rendelkezik-e elegendő pénzzel.
	 *
	 * @param shop a bolt, ahonnan vásárol
	 * @param item a megvásárolni kívánt felszerelés
	 */
	public void buyItem(Shop shop, ShopItem item) {

	}

	/**
	 * A takarító lecseréli a hókotró kotrófejét egy másik fejre. Az új fej a takarító
	 * birtokában kell, hogy legyen.
	 *
	 * @param sp a kotrófejét lecserélendő hókotró
	 * @param p az új kotrófej
	 */
	public void changePlowHead(Snowplow sp, Plow p) {
		
	}

	/**
	 * A takarító érméket szerez a sikeres hótakarítás után. Ez a metódus a Hókotró
	 * (Snowplow) osztály által hívódik meg a sikeres tisztítás után.
	 */
	public void achieveCoin() {

    }
}
