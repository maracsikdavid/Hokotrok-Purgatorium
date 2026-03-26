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

public class Cleaner extends Player {
	public Wallet wallet = new Wallet();
	public List<Snowplow> fleet = new ArrayList<>();

	public void commandSnowplow(Snowplow sp, Road toRoad, Lane toLane, Plow withPlow) {
		Skeleton.printCall(null, this, "commandSnowplow");
		Skeleton.printReturn(this, "commandSnowplow");
	}

	public void buyItem(Shop shop, ShopItem item) {
		Skeleton.printCall(null, this, "buyItem");
		Skeleton.printReturn(this, "buyItem");
	}

	public void changePlowHead(Snowplow sp, Plow p) {
		Skeleton.printCall(null, this, "changePlowHead");
		Skeleton.printReturn(this, "changePlowHead");
	}

	public void achieveCoin() {
		Skeleton.printCall(null, this, "achieveCoin");
		Skeleton.printReturn(this, "achieveCoin");
	}
}
