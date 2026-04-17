package cli;

import java.io.File;
import java.util.Scanner;

/**
 * A tesztelés assert fázisáért felelős osztály. 
 * Két szöveges fájlt hasonlít össze sorról sorra, a felesleges szóközöket 
 * és a platformfüggő sortöréseket normalizálva, illetve figyelmen kívül hagyva.
 */
public class DiffChecker {
    
    // --- METÓDUSOK ---

    /**
     * Összehasonlítja a futás során generált és az elvárt kimenetet tartalmazó fájlokat.
     * Hiba esetén a standard kimenetre írja az eltérés pontos helyét és okát.
     * 
     * @param actualPath A tényleges kimenetet tartalmazó fájl elérési útja (test-xyz-result.txt)
     * @param expectedPath Az elvárt kimenetet tartalmazó (referencia) fájl elérési útja (test-xyz-correct.txt)
     * @return true, ha a két fájl tartalmilag megegyezik, egyébként false
     */
    public boolean compareFiles(String actualPath, String expectedPath) {
        return false;
    }
}