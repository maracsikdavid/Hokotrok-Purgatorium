package tests;

/**
 * Teszteset absztrakt alaposztálya. Minden konkrét teszteset (TC_01, TC_02, stb.)
 * ebből az osztályból származik, és megvalósítja a run() metódust, amely a teszteset
 * kiértékelésének logikáját tartalmazza. Ez az alapintézmény a tesztvezérlés
 * egységesítéséhez a keretrendszerben.
 */
public abstract class TestCase {
	/**
	 * A teszteset futtatása. Minden teszteset implementálnia kell ezt a metódust
	 * a saját specifikus tesztkód futtatásához. A run() metódus a Main.java-ból
	 * hívódik meg runTestCase() metóduson keresztül.
	 */
	public abstract void run();
}
