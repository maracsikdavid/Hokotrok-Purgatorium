package core;

import actors.Cleaner;

public class Shop {
	public boolean canPurchase(Cleaner cleaner, ShopItem item) {
		Skeleton.printCall(null, this, "canPurchase");
		boolean result = Skeleton.getIntFromUser("Can purchase? (1:Yes, 0:No)") == 1;
		Skeleton.printReturn(this, "canPurchase", String.valueOf(result));
		return result;
	}

	public boolean tryPurchase(Cleaner cleaner, ShopItem item) {
		Skeleton.printCall(null, this, "tryPurchase");
		boolean result = Skeleton.getIntFromUser("Try purchase successful? (1:Yes, 0:No)") == 1;
		Skeleton.printReturn(this, "tryPurchase", String.valueOf(result));
		return result;
	}
}
