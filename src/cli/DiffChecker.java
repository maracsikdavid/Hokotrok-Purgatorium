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
        File actualFile = new File(actualPath);
        File expectedFile = new File(expectedPath);

        if (!actualFile.exists()) {
            ConsoleOutput.error("Result file not found: " + actualPath);
            return false;
        }
        if (!expectedFile.exists()) {
            ConsoleOutput.error("Expected file not found: " + expectedPath);
            return false;
        }

        try {
            Scanner actualScanner = new Scanner(actualFile);
            Scanner expectedScanner = new Scanner(expectedFile);
            int lineNum = 0;
            boolean passed = true;

            while (actualScanner.hasNextLine() && expectedScanner.hasNextLine()) {
                lineNum++;
                String actualLine = actualScanner.nextLine().trim();
                String expectedLine = expectedScanner.nextLine().trim();

                if (!actualLine.equals(expectedLine)) {
                    ConsoleOutput.diff("DIFF at line " + lineNum + ":");
                    ConsoleOutput.diff("  Expected: " + expectedLine);
                    ConsoleOutput.diff("  Actual:   " + actualLine);
                    passed = false;
                }
            }

            while (actualScanner.hasNextLine()) {
                lineNum++;
                String extra = actualScanner.nextLine().trim();
                if (!extra.isEmpty()) {
                    ConsoleOutput.diff("DIFF: Extra line " + lineNum + " in result: " + extra);
                    passed = false;
                }
            }

            while (expectedScanner.hasNextLine()) {
                lineNum++;
                String missing = expectedScanner.nextLine().trim();
                if (!missing.isEmpty()) {
                    ConsoleOutput.diff("DIFF: Missing expected line " + lineNum + ": " + missing);
                    passed = false;
                }
            }

            actualScanner.close();
            expectedScanner.close();
            return passed;

        } catch (Exception e) {
            ConsoleOutput.error("Error comparing files: " + e.getMessage());
            return false;
        }
    }
}