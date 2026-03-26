package topology;

/**
 * Az alagút egy olyan, felszín alatt elhelyezkedő közlekedési egység, amely az úthoz hasonlóan (egy speciális úttípus) egy vagy több sávból is állhat,
 * és elválaszthatatlan párt alkot egy felette elhelyezkedő híddal. 
 * Felelőssége a hó- és jégmentes közlekedés biztosítása: mivel az alagút védett és nem esik be a csapadék, 
 * megakadályozza a hóréteg és a jegesedés kialakulását a hozzá tartozó sávokon. 
 * Ezen kívül az úthoz hasonlóan összefogja azokat a sávokat, melyek két adott egymástól különböző csomópont között helyezkednek el, 
 * ezáltal lehetővé téve több jármű közlekedését egyszerre a csomópontok között.
 */

public class Tunnel extends Road {
	public Bridge paired;
}
