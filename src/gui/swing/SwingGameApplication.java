package gui.swing;

import gui.application.GameSessionFactory;
import gui.application.MapCatalog;
import javax.swing.SwingUtilities;

/**
 * A grafikus alkalmazás belépési pontja.
 * Gondoskodik arról, hogy a Swing felület az Event Dispatch Threaden induljon el.
 */
public class SwingGameApplication {
    private MainFrame mainFrame;

    /**
     * Elindítja a grafikus alkalmazást a Swing eseménykezelő szálán.
     */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            MapCatalog catalog = new MapCatalog();
            catalog.loadDefaults();
            mainFrame = new MainFrame(catalog, new GameSessionFactory());
            mainFrame.showMenu();
            mainFrame.setVisible(true);
        });
    }

    /**
     * Megjeleníti a kezdőképernyőt, ha a főablak már létrejött.
     */
    public void showMainMenu() {
        if (mainFrame != null) {
            mainFrame.showMenu();
        }
    }

    /**
     * Visszaadja a grafikus alkalmazás főablakát.
     *
     * @return a főablak, vagy null ha még nem jött létre
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Kompatibilitasi alias a korabbi hivo kodokhoz.
     *
     * @return a foablak, vagy null ha meg nem jott letre
     */
    public MainFrame getMainWindow() {
        return mainFrame;
    }
}