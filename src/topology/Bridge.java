package topology;

/**
 * Híd: egy speciális út, amely alagúttal párosított.
 * Felelőssége megegyezik a normál útéval
 */
public class Bridge extends Road {
	/**
	 * A hídhoz párosított alagút. A járművek a hídról az alagútra átléphetnek,
	 * és fordítva.
	 */
	public Tunnel paired;
}
