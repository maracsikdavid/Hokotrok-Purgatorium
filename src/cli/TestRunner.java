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
    private int passCount = 0;
    private int failCount = 0;

    
    
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



    /**
     * Visszaadja a tesztfuttató által használt parancsértelmezőt.
     *
     * @return A Parser példány referenciája.
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Beállítja a tesztfuttató parancsértelmezőjét.
     *
     * @param parser A beállítandó Parser példány.
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * Visszaadja a fájlok összehasonlításáért felelős objektumot.
     *
     * @return A DiffChecker példány referenciája.
     */
    public DiffChecker getDiffChecker() {
        return diffChecker;
    }

    /**
     * Beállítja a fájlok összehasonlításáért felelős objektumot.
     *
     * @param diffChecker A beállítandó DiffChecker példány.
     */
    public void setDiffChecker(DiffChecker diffChecker) {
        this.diffChecker = diffChecker;
    }



    /**
     * Lefuttat egy automatizált tesztesetet a megadott név alapján. 
     * A folyamat során beolvassa az inicializáló fájlt, végrehajtja a tesztlépéseket, 
     * a kimenetet egy eredményfájlba menti, majd összehasonlítja azt az elvárt kimenettel.
     * 
     * @param testName A futtatandó teszt neve (pl. "test-cleaner-buy-success").
     */
    public void runTest(String testName) {
        String testDir = "tests" + File.separator + testName + File.separator;
        String initPath = testDir + testName + "-init.txt";
        String actPath = testDir + testName + ".txt";
        String resultPath = testDir + testName + "-result.txt";
        String correctPath = testDir + testName + "-correct.txt";

        File initFile = new File(initPath);
        File actFile = new File(actPath);

        if (!initFile.exists() && !actFile.exists()) {
            String normalizedPath = initPath.replace(File.separatorChar, '/');
            ConsoleOutput.error("File not found: " + normalizedPath);
            return;
        }

        ConsoleOutput.setTestMode(true);

        Parser testParser = new Parser();
        try {
            if (initFile.exists()) {
                Scanner initScanner = new Scanner(initFile);
                while (initScanner.hasNextLine()) {
                    testParser.parseLine(initScanner.nextLine());
                }
                initScanner.close();
            }
        } catch (Exception e) {
            ConsoleOutput.error("Failed to read init file: " + e.getMessage());
            ConsoleOutput.setTestMode(false);
            return;
        }

        try {
            if (!actFile.exists()) {
                ConsoleOutput.error("Act file not found: " + actPath);
                return;
            }

            PrintStream originalOut = System.out;
            PrintStream resultStream = new PrintStream(new File(resultPath));
            System.setOut(resultStream);

            Scanner actScanner = new Scanner(actFile);
            while (actScanner.hasNextLine()) {
                testParser.parseLine(actScanner.nextLine());
            }
            actScanner.close();

            resultStream.flush();
            resultStream.close();
            System.setOut(originalOut);

        } catch (Exception e) {
            ConsoleOutput.error("Failed during act phase: " + e.getMessage());
            return;
        }

        ConsoleOutput.setTestMode(false);
        
        boolean passed = diffChecker.compareFiles(resultPath, correctPath);
        if (passed) {
            passCount++;
            ConsoleOutput.pass(testName);
        } else {
            failCount++;
            ConsoleOutput.fail(testName);
        }
    }

    /**
     * Kiírja az összesített teszt eredményt.
     */
    public void printSummary() {
        int total = passCount + failCount;
        System.out.println("> --- Summary: " + passCount + "/" + total + " PASS, " + failCount + "/" + total + " FAIL ---");
    }

    /**
     * Visszaadja a sikeres tesztesetek számát.
     *
     * @return Az átment tesztek száma.
     */
    public int getPassCount() { return passCount; }

    /**
     * Visszaadja a megbukott tesztesetek számát.
     *
     * @return A sikertelen tesztek száma.
     */
    public int getFailCount() { return failCount; }
}
