package tests;

/**
 * TC_09: Vékony hóból vastag hóvá alakulás (hóesés vékony hó állapotban).
 * 
 * Use-case neve: TC_09_THIN_SNOW_TO_THICK_SNOW
 * 
 * Rövid leírás:
 * Egy vékony hóval borított sávra tovább havazik. 
 * A hóréteg vastagsága eléri a kritikus küszöbértéket (5 tick), így a sáv állapota vastag hóra változik.
 * 
 * Forgatókönyv:
 * 1. A Tesztelő elindítja a tesztet.
 * 2. A Szkeleton inicializál egy teszt pályát.
 * 3. A Skeleton meghívja a sáv tick() metódusát.
 * 4. A sáv állapota megkapja az addSnow() hívást.
 * 5. A Skeleton megkérdezi a Tesztelőt: "A havazás után a hóréteg elérte a vastag hó küszöbértékét? (1: Igen, 0: Nem)" → Válasz: 1.
 * 6. A sáv állapota ThickSnowCondition-re módosul.
 */
public class TC_09_ThinSnowToThickSnow extends TestCase {
    /**
     * A teszteset futtatása. Vékony hóból vastag hóvá alakulásának szimulálása.
     */
    @Override
    public void run() {
    }
}
