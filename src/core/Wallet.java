package core;

public class Wallet {
	public int amount;

	public void add(int amount) {
		Skeleton.printCall(null, this, "add");
		this.amount += amount;
		Skeleton.printReturn(this, "add");
	}

	public void spend(int amount) {
		Skeleton.printCall(null, this, "spend");
		this.amount -= amount;
		Skeleton.printReturn(this, "spend");
	}
}
