package actors;

import core.Shop;
import core.ShopItem;
import core.Wallet;
import core.Skeleton;
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

	
	// --- GETTEREK ÉS SETTEREK ---
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public List<Snowplow> getFleet() {
		return fleet;
	}
	public void setFleet(List<Snowplow> fleet) {
		this.fleet = fleet;
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
		Skeleton.printCall(null, this, "commandSnowplow");
		Skeleton.printReturn(this, "commandSnowplow");
	}

	/**
	 * A takarító egy felszerelést vásárol a boltból (pl. sót, biokerozint vagy kotrófejeket).
	 * A vásárlás sikerességétől függ, hogy rendelkezik-e elegendő pénzzel.
	 *
	 * @param shop a bolt, ahonnan vásárol
	 * @param item a megvásárolni kívánt felszerelés
	 */
	public void buyItem(Shop shop, ShopItem item) {
		Skeleton.printCall(null, this, "buyItem");
		Skeleton.printReturn(this, "buyItem");
	}

	/**
	 * A takarító lecseréli a hókotró kotrófejét egy másik fejre. Az új fej a takarító
	 * birtokában kell, hogy legyen.
	 *
	 * @param sp a kotrófejét lecserélendő hókotró
	 * @param p az új kotrófej
	 */
	public void changePlowHead(Snowplow sp, Plow p) {
		Skeleton.printCall(null, this, "changePlowHead");
		Skeleton.printReturn(this, "changePlowHead");
	}

	/**
	 * A takarító érméket szerez a sikeres hótakarítás után. Ez a metódus a Hókotró
	 * (Snowplow) osztály által hívódik meg a sikeres tisztítás után.
	 */
	public void achieveCoin() {
        Skeleton.printCall(null, this, "achieveCoin");
        if (wallet != null)
            wallet.add(1);
        Skeleton.printReturn(this, "achieveCoin");
    }
}
