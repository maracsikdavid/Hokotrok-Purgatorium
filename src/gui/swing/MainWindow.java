package gui.swing;

import gui.application.GameSessionFactory;
import gui.application.MapCatalog;

/**
 * Kompatibilitasi nev a korabbi GUI belepesi pontokhoz.
 * Az uj kozponti ablakimplementacio a {@link MainFrame} osztalyban talalhato.
 */
public class MainWindow extends MainFrame {
    public MainWindow(MapCatalog mapCatalog, GameSessionFactory sessionFactory) {
        super(mapCatalog, sessionFactory);
    }
}