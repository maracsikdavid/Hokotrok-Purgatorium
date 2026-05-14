package gui.swing;

import gui.application.GameSession;
import gui.application.GameSessionFactory;
import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A grafikus alkalmazás legfelső Swing ablaka.
 * CardLayout segítségével vált a főmenü, pályaválasztás, regisztráció, játék és eredmény nézetek között.
 */
public class MainWindow extends JFrame {
    private static final String MENU_CARD = "menu";
    private static final String MAP_SELECTION_CARD = "mapSelection";
    private static final String PLAYER_REGISTER_CARD = "playerRegister";
    private static final String GAME_CARD = "game";
    private static final String RESULT_CARD = "result";

    private final JPanel contentPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final StartPanel startPanel = new StartPanel();
    private final MapSelectionPanel mapSelectionPanel = new MapSelectionPanel();
    private final PlayerRegisterPanel playerRegisterPanel = new PlayerRegisterPanel();
    private final GamePanel gamePanel = new GamePanel();
    private final ResultPanel resultPanel = new ResultPanel();
    private final MapCatalog mapCatalog;
    private final GameSessionFactory sessionFactory;

    /**
     * Konstruktor a főablak és az alapvető navigációs kapcsolatok létrehozásához.
     *
     * @param mapCatalog a grafikus pályakatalógus
     * @param sessionFactory a sessionök létrehozásáért felelős factory
     */
    public MainWindow(MapCatalog mapCatalog, GameSessionFactory sessionFactory) {
        super("Hókotrók Purgatórium");
        this.mapCatalog = mapCatalog;
        this.sessionFactory = sessionFactory;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        contentPanel.setLayout(cardLayout);
        contentPanel.add(startPanel, MENU_CARD);
        contentPanel.add(mapSelectionPanel, MAP_SELECTION_CARD);
        contentPanel.add(playerRegisterPanel, PLAYER_REGISTER_CARD);
        contentPanel.add(gamePanel, GAME_CARD);
        contentPanel.add(resultPanel, RESULT_CARD);
        setContentPane(contentPanel);

        mapSelectionPanel.bindCatalog(mapCatalog);
        wireNavigation();
    }

    private void wireNavigation() {
        startPanel.setConsoleModeListener(event -> showMessage("A konzolos mód a főmenüből indítható."));
        startPanel.setGraphicModeListener(event -> showMapSelection());
        startPanel.setExitListener(event -> dispose());
        mapSelectionPanel.setBackListener(event -> showMenu());
        mapSelectionPanel.setSelectionListener(event -> {
            MapDescriptor descriptor = mapCatalog.findById(event.getActionCommand());
            showPlayerRegistration(descriptor);
        });
        playerRegisterPanel.setBackListener(event -> showMenu());
        playerRegisterPanel.setStartListener(event -> startSelectedGame());
        resultPanel.setNewGameListener(event -> showMapSelection());
        resultPanel.setExitListener(event -> dispose());
    }

    /**
     * Megjeleníti a főmenü nézetet.
     */
    public void showMenu() {
        cardLayout.show(contentPanel, MENU_CARD);
    }

    /**
     * Megjeleníti a pályaválasztó nézetet.
     */
    public void showMapSelection() {
        cardLayout.show(contentPanel, MAP_SELECTION_CARD);
    }

    /**
     * Megjeleníti a játékos-regisztrációs nézetet a kiválasztott pályával.
     *
     * @param descriptor a kiválasztott pályaleíró
     */
    public void showPlayerRegistration(MapDescriptor descriptor) {
        playerRegisterPanel.setMapDescriptor(descriptor);
        cardLayout.show(contentPanel, PLAYER_REGISTER_CARD);
    }

    /**
     * Megjeleníti a játéknézetet, és összeköti a megadott sessionnel.
     *
     * @param session a megjelenítendő játék sessionje
     */
    public void showGame(GameSession session) {
        gamePanel.bindSession(session);
        cardLayout.show(contentPanel, GAME_CARD);
    }

    /**
     * Megjelenít egy egyszerű rendszerüzenetet a felhasználónak.
     *
     * @param text a megjelenítendő üzenet
     */
    public void showMessage(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void startSelectedGame() {
        try {
            GameSession session = sessionFactory.createSession(playerRegisterPanel.getMapDescriptor());
            showGame(session);
        } catch (Exception exception) {
            showMessage(exception.getMessage());
        }
    }
}