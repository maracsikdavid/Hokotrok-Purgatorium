package cli.commands;

import cli.Command;
import cli.ConsoleOutput;
import cli.TestRunner;

/**
 * A 'test' parancs megvalósítása.
 * Parancssoros Teszt mód esetén ez felel a belső teszt-motor elindításáért,
 * amely lefuttatja a specifikált tesztfájlokat.
 */
public class TestCommand implements Command {
    private String[] parts;
    private TestRunner testRunner;


    // --- KONSTRUKTOROK ---

    /**
     * Alapértelmezett konstruktor.
     */
    public TestCommand() {
    }

    /**
     * Paraméteres konstruktor a parancs inicializálásához.
     *
     * @param parts a felbontott bemeneti sor (pl. ["test", "run", "test_01"])
     * @param testRunner az automatizált teszteket futtató motor
     */
    public TestCommand(String[] parts, TestRunner testRunner) {
        this.parts = parts;
        this.testRunner = testRunner;
    }


    // --- GETTEREK ÉS SETTEREK ---

    /**
     * Visszaadja a felbontott bemeneti sort.
     *
     * @return a parancs elemeit tartalmazó tömb
     */
    public String[] getParts() {
        return parts;
    }

    /**
     * Beállítja a felbontott bemeneti sort.
     *
     * @param parts a beállítandó string tömb
     */
    public void setParts(String[] parts) {
        this.parts = parts;
    }

    /**
     * Visszaadja a tesztfutattó motort.
     *
     * @return a TestRunner példány
     */
    public TestRunner getTestRunner() {
        return testRunner;
    }

    /**
     * Beállítja a tesztfuttató motort.
     *
     * @param testRunner a beállítandó TestRunner példány
     */
    public void setTestRunner(TestRunner testRunner) {
        this.testRunner = testRunner;
    }


    // --- METÓDUSOK ---

    /**
     * Ellenőrzi, hogy a parancs paraméterei érvényesek-e.
     *
     * @return igaz, ha a parancs pontosan 3 elemű és a szintaxis helyes
     */
    @Override
    public boolean validate() {
        if (parts == null || parts.length != 3 || !"run".equals(parts[1])) {
            ConsoleOutput.error("Invalid command format for test.");
            return false;
        }
        return true;
    }

    /**
     * Végrehajtja a teszt futtatását a TestRunner meghívásával.
     */
    @Override
    public void execute() {
        String testName = parts[2];
        try {
            testRunner.runTest(testName);
        } catch (Exception e) {
            ConsoleOutput.error("File not found: " + testName);
        }
    }
}