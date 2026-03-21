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

public class Cleaner extends Player {
	public Wallet wallet = new Wallet();
	public List<Snowplow> fleet = new ArrayList<>();

	public void commandSnowplow(Snowplow sp, Road toRoad, Lane toLane, Plow withPlow) {
	}

	public void buyItem(Shop shop, ShopItem item) {
	}

	public void changePlowHead(Snowplow sp, Plow p) {
	}

	public void achieveCoin() {
	}
}
