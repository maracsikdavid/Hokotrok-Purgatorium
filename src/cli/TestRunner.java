package cli;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Az automatizált tesztesetek kötegelt lefuttatásáért felelős motor.
 * Kezeli a fájl I/O műveleteket az Arrange-Act-Assert (AAA) minta alapján.
 */
public class TestRunner {
    private Parser parser;
    private DiffChecker diffChecker;

    
    // --- KONSTRUKTOROK ---
    
    /**
     * Alapértelmezett konstruktor.
     */
    public TestRunner() {
        this.parser = new Parser();
        this.diffChecker = new DiffChecker();
    }

    /**
     * Konstruktor, amely inicializálja a tesztfuttatót.
     * 
     * @param parser A parancsokat értelmező {@link Parser} példány, amellyel a teszt dolgozik
     */
    public TestRunner(Parser parser) {
        this.parser = parser;
        this.diffChecker = new DiffChecker();
    }

    /**
     * Paraméteres konstruktor minden attribútummal.
     *
     * @param parser a parser példány
     * @param diffChecker a diff ellenőrző példány
     */
    public TestRunner(Parser parser, DiffChecker diffChecker) {
        this.parser = parser;
        this.diffChecker = diffChecker;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a tesztfuttató parserét.
     *
     * @return a parser példány
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Beállítja a tesztfuttató parserét.
     *
     * @param parser a beállítandó parser
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * Visszaadja a diff ellenőrzőt.
     *
     * @return a diff checker példány
     */
    public DiffChecker getDiffChecker() {
        return diffChecker;
    }

    /**
     * Beállítja a diff ellenőrzőt.
     *
     * @param diffChecker a beállítandó diff checker
     */
    public void setDiffChecker(DiffChecker diffChecker) {
        this.diffChecker = diffChecker;
    }


    // --- METÓDUSOK ---

    /**
     * Lefuttat egy adott nevű automatizált tesztet a "tests" mappából. 
     * Sorrendben beolvassa az init fájlt, végrehajtja az act fájl parancsait 
     * (kimenet átirányításával), majd a diff checkerrel validálja a végeredményt.
     * A végén kiírja a [PASS] vagy [FAIL] eredményt a konzolra.
     * 
     * @param testName A futtatandó teszt pontos neve (mappa és fájl prefixe)
     */
    public void runTest(String testName) {
        String testDir = "tests" + File.separator + testName + File.separator;
        String initPath = testDir + testName + "-init.txt";
        String actPath = testDir + testName + ".txt";
        String resultPath = testDir + testName + "-result.txt";
        String correctPath = testDir + testName + "-correct.txt";

        // --- ARRANGE fázis: init fájl parancsainak végrehajtása ---
        // A Parser minden runTest-hez friss állapottal indul
        Parser testParser = new Parser();
        try {
            File initFile = new File(initPath);
            if (!initFile.exists()) {
                ConsoleOutput.error("Init file not found: " + initPath);
                return;
            }
            Scanner initScanner = new Scanner(initFile);
            while (initScanner.hasNextLine()) {
                testParser.parseLine(initScanner.nextLine());
            }
            initScanner.close();
        } catch (Exception e) {
            ConsoleOutput.error("Failed to read init file: " + e.getMessage());
            return;
        }

        // --- ACT + ASSERT fázis: act fájl parancsainak végrehajtása, kimenet fájlba ---
        try {
            File actFile = new File(actPath);
            if (!actFile.exists()) {
                ConsoleOutput.error("Act file not found: " + actPath);
                return;
            }

            // Kimenet átirányítása fájlba
            PrintStream originalOut = System.out;
            PrintStream resultStream = new PrintStream(new File(resultPath));
            System.setOut(resultStream);

            Scanner actScanner = new Scanner(actFile);
            while (actScanner.hasNextLine()) {
                testParser.parseLine(actScanner.nextLine());
            }
            actScanner.close();

            // Kimenet visszaállítása
            resultStream.flush();
            resultStream.close();
            System.setOut(originalOut);

        } catch (Exception e) {
            ConsoleOutput.error("Failed during act phase: " + e.getMessage());
            return;
        }

        // --- DIFF fázis: eredmény összehasonlítása az elvárt kimenettel ---
        boolean passed = diffChecker.compareFiles(resultPath, correctPath);
        if (passed) {
            ConsoleOutput.pass(testName);
        } else {
            ConsoleOutput.fail(testName);
        }
    }
}