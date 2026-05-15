package gui.swing;

import gui.application.GameSession;
import gui.application.GameSessionFactory;
import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A grafikus prototipus kozponti ablaka.
 * A kepernyok kozotti valtast egyetlen CardLayout koordinálja.
 */
public class MainFrame extends JFrame {
    private static final String START_CARD = "start";
    private static final String MAP_SELECTOR_CARD = "mapSelector";
    private static final String PLAYER_REGISTER_CARD = "playerRegister";
    private static final String GAME_CARD = "game";
    private static final String RESULT_CARD = "result";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    private final StartPanel startPanel = new StartPanel();
    private final MapSelectorPanel mapSelectorPanel = new MapSelectorPanel();
    private final PlayerRegisterPanel playerRegisterPanel = new PlayerRegisterPanel();
    private final GamePanel gamePanel = new GamePanel();
    private final ResultPanel resultPanel = new ResultPanel();
    private final MapCatalog mapCatalog;
    private final GameSessionFactory sessionFactory;

    /**
     * Letrehozza a foablakot es bekoti a panelnavigaciot.
     *
     * @param mapCatalog a grafikus palyakatalogus
     * @param sessionFactory a grafikus jatekmenet letrehozoje
     */
    public MainFrame(MapCatalog mapCatalog, GameSessionFactory sessionFactory) {
        super("Hókotrók Purgatórium");
        this.mapCatalog = mapCatalog == null ? new MapCatalog() : mapCatalog;
        this.sessionFactory = sessionFactory == null ? new GameSessionFactory() : sessionFactory;

        configureFrame();
        installCards();
        wireNavigation();
    }

    private void configureFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImages(AppIcon.createIconImages());
        setMinimumSize(new Dimension(900, 560));
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setContentPane(cardsPanel);
    }

    private void installCards() {
        cardsPanel.add(startPanel, START_CARD);
        cardsPanel.add(mapSelectorPanel, MAP_SELECTOR_CARD);
        cardsPanel.add(playerRegisterPanel, PLAYER_REGISTER_CARD);
        cardsPanel.add(gamePanel, GAME_CARD);
        cardsPanel.add(resultPanel, RESULT_CARD);
        mapSelectorPanel.bindCatalog(mapCatalog);
    }

    private void wireNavigation() {
        startPanel.setConsoleModeListener(event -> openConsoleMode());
        startPanel.setGraphicModeListener(event -> showMapSelection());
        startPanel.setExitListener(event -> System.exit(0));

        mapSelectorPanel.setBackListener(event -> showMenu());
        mapSelectorPanel.setSelectionListener(event -> {
            MapDescriptor descriptor = mapCatalog.findById(event.getActionCommand());
            if (descriptor == null) {
                showMessage("A kiválasztott pálya nem található.");
                return;
            }
            showPlayerRegistration(descriptor);
        });

        playerRegisterPanel.setBackListener(event -> showMapSelection());
        playerRegisterPanel.setStartListener(event -> startSelectedGame());
        gamePanel.setEndGameListener(event -> showResults());

        resultPanel.setNewGameListener(event -> showMapSelection());
        resultPanel.setExitListener(event -> System.exit(0));
    }

    /**
     * Megjeleniti a fomenut.
     */
    public void showMenu() {
        cardLayout.show(cardsPanel, START_CARD);
    }

    /**
     * Megjeleniti a palyavalaszto panelt.
     */
    public void showMapSelection() {
        cardLayout.show(cardsPanel, MAP_SELECTOR_CARD);
    }

    /**
     * Megnyitja a jatekosfelveteli panelt a kivalasztott palyaval.
     *
     * @param descriptor a kivalasztott palya
     */
    public void showPlayerRegistration(MapDescriptor descriptor) {
        playerRegisterPanel.setMapDescriptor(descriptor);
        cardLayout.show(cardsPanel, PLAYER_REGISTER_CARD);
    }

    /**
     * Megjeleniti a jatekpanelt a letrehozott sessionnel.
     *
     * @param session a grafikus session
     */
    public void showGame(GameSession session) {
        showGame(session, playerRegisterPanel.getRegisteredPlayers());
    }

    /**
     * Megjeleniti a jatekpanelt es atadja a prototipusban felvett jatekosokat.
     *
     * @param session a grafikus session
     * @param players a regisztralt jatekosok
     */
    public void showGame(GameSession session, List<PlayerRegisterPanel.RegisteredPlayer> players) {
        gamePanel.bindSession(session, playerRegisterPanel.getMapDescriptor(), players);
        cardLayout.show(cardsPanel, GAME_CARD);
        gamePanel.requestFocusInWindow();
    }

    /**
     * Megjeleniti az eredmenypanelt.
     */
    public void showResults() {
        List<ResultPanel.PlayerResult> playerResults = gamePanel.createPlayerResults();
        if (playerResults.isEmpty()) {
            resultPanel.setResults(playerRegisterPanel.getRegisteredPlayers());
        } else {
            resultPanel.setPlayerResults(playerResults);
        }
        cardLayout.show(cardsPanel, RESULT_CARD);
    }

    /**
     * Egyszeru informacios uzenetablak.
     *
     * @param text megjelenitendo szoveg
     */
    public void showMessage(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void startSelectedGame() {
        String validationMessage = playerRegisterPanel.getStartValidationMessage();
        if (validationMessage != null) {
            playerRegisterPanel.showPushNotification(validationMessage);
            return;
        }

        try {
            GameSession session = sessionFactory.createSession(playerRegisterPanel.getMapDescriptor());
            showGame(session);
        } catch (Exception exception) {
            showMessage("A pálya előkészítése sikertelen: " + exception.getMessage());
        }
    }

    private void openConsoleMode() {
        setVisible(false);
        Thread consoleThread = new Thread(this::invokeConsoleMain, "console-mode-launcher");
        consoleThread.setDaemon(false);
        consoleThread.start();
    }

    private void invokeConsoleMain() {
        boolean returnToGraphics = false;
        try {
            Method consoleMethod = Class.forName("Main").getMethod("runConsoleMode");
            Object result = consoleMethod.invoke(null);
            returnToGraphics = Boolean.TRUE.equals(result);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException exception) {
            System.err.println("Nem sikerült elindítani a konzolos belépési pontot: " + exception.getMessage());
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause() == null ? exception : exception.getCause();
            System.err.println("A konzolos mód hibával leállt: " + cause.getMessage());
        }

        if (returnToGraphics) {
            SwingUtilities.invokeLater(() -> {
                showMenu();
                setLocationRelativeTo(null);
                setVisible(true);
            });
        } else {
            System.exit(0);
        }
    }
}