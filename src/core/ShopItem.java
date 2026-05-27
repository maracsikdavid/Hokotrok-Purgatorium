package core;

/**
 * Az ősszesen elérhető felszerelések felsorolása a boltban.
 * Ide tartoznak az "üzemanyag", "só", "kotrófejek" valamint a hókotró.
 */
public enum ShopItem {
	Biokerosene,
	Salt,
	Gravel,
	DragonPlow,
	SaltPlow,
	DumpPlow,
	SweeperPlow,
	IcebreakerPlow,
	GravelPlow,
	Snowplow;

	public String getDisplayName() {
		switch (this) {
			case Biokerosene:
				return "Biokerozin";
			case Salt:
				return "Só";
			case Gravel:
				return "Törmelék";
			case SaltPlow:
				return "Sószóró fej";
			case IcebreakerPlow:
				return "Jégtörő fej";
			case DragonPlow:
				return "Sárkány fej";
			case SweeperPlow:
				return "Söprő fej";
			case DumpPlow:
				return "Hányó fej";
			case GravelPlow:
				return "Törmelékszóró fej";
			case Snowplow:
				return "Hókotró";
			default:
				return name();
		}
	}
}
