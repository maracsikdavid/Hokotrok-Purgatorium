package gui.swing;

import gui.application.GameSessionFactory;
import gui.application.MapCatalog;
import javax.swing.SwingUtilities;

/**
 * A grafikus alkalmazás belépési pontja.
 * Gondoskodik arról, hogy a Swing felület az Event Dispatch Threaden induljon el.
 */
public class SwingGameApplication {
    private MainWindow mainWindow;

    /**
     * Elindítja a grafikus alkalmazást a Swing eseménykezelő szálán.
     */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            MapCatalog catalog = new MapCatalog();
            catalog.loadDefaults();
            mainWindow = new MainWindow(catalog, new GameSessionFactory());
            mainWindow.showMenu();
            mainWindow.setVisible(true);
        });
    }

    /**
     * Megjeleníti a kezdőképernyőt, ha a főablak már létrejött.
     */
    public void showMainMenu() {
        if (mainWindow != null) {
            mainWindow.showMenu();
        }
    }

    /**
     * Visszaadja a grafikus alkalmazás főablakát.
     *
     * @return a főablak, vagy null ha még nem jött létre
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }
}