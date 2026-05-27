package gui.swing;

import gui.application.GameSession;
import gui.application.GameSessionFactory;
import gui.application.GuiConsoleCompanion;
import gui.application.GuiConsoleCompanion.CommandFeedbackType;
import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
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
    private final GuiConsoleCompanion consoleCompanion = new GuiConsoleCompanion();

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
        consoleCompanion.startIfNeeded();
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
        consoleCompanion.clearSession();
        cardLayout.show(cardsPanel, START_CARD);
    }

    /**
     * Megjeleniti a palyavalaszto panelt.
     */
    public void showMapSelection() {
        consoleCompanion.clearSession();
        cardLayout.show(cardsPanel, MAP_SELECTOR_CARD);
    }

    /**
     * Megnyitja a jatekosfelveteli panelt a kivalasztott palyaval.
     *
     * @param descriptor a kivalasztott palya
     */
    public void showPlayerRegistration(MapDescriptor descriptor) {
        consoleCompanion.clearSession();
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
        attachParallelConsole(session);
        gamePanel.bindSession(session, playerRegisterPanel.getMapDescriptor(), players);
        cardLayout.show(cardsPanel, GAME_CARD);
        gamePanel.requestFocusInWindow();
    }

    /**
     * Megjeleniti az eredmenypanelt.
     */
    public void showResults() {
        List<ResultPanel.PlayerResult> playerResults = gamePanel.createPlayerResults();
        List<ResultPanel.PlayerResult> resolvedResults = playerResults;
        if (resolvedResults.isEmpty()) {
            resolvedResults = createFallbackResults();
            resultPanel.setResults(playerRegisterPanel.getRegisteredPlayers());
        } else {
            resultPanel.setPlayerResults(resolvedResults);
        }

        consoleCompanion.announceGameFinished(resolveMapName(), formatResultLines(resolvedResults));
        consoleCompanion.clearSession();
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
            registerPlayers(session);
            showGame(session);
        } catch (Exception exception) {
            showMessage("A pálya előkészítése sikertelen: " + exception.getMessage());
        }
    }

    private void attachParallelConsole(GameSession session) {
        if (session == null) {
            return;
        }

        consoleCompanion.attachSession(session, (command, type, message) -> SwingUtilities.invokeLater(() -> {
            if (gamePanel.getSession() != session) {
                return;
            }
            if (type == CommandFeedbackType.SUCCESS) {
                gamePanel.refreshFromConsoleCommand(command);
            }
            gamePanel.showConsoleFeedback(message, mapFeedbackType(type));
        }));
    }

    private FeedbackType mapFeedbackType(CommandFeedbackType type) {
        if (type == CommandFeedbackType.SUCCESS) {
            return FeedbackType.SUCCESS;
        }
        if (type == CommandFeedbackType.ERROR) {
            return FeedbackType.ERROR;
        }
        if (type == CommandFeedbackType.WARNING) {
            return FeedbackType.WARNING;
        }
        return FeedbackType.INFO;
    }

    private String resolveMapName() {
        if (playerRegisterPanel.getMapDescriptor() == null) {
            return "ismeretlen pálya";
        }
        return playerRegisterPanel.getMapDescriptor().getDisplayName();
    }

    private List<ResultPanel.PlayerResult> createFallbackResults() {
        List<ResultPanel.PlayerResult> fallbackResults = new ArrayList<>();
        for (PlayerRegisterPanel.RegisteredPlayer player : playerRegisterPanel.getRegisteredPlayers()) {
            List<String> details = List.of(
                player.getPlayerRole().getScoreLabel() + ": " + player.getPlayerRole().getInitialValue());
            fallbackResults.add(new ResultPanel.PlayerResult(
                player.getName(),
                player.getRoleDisplayName(),
                details));
        }
        return fallbackResults;
    }

    private List<String> formatResultLines(List<ResultPanel.PlayerResult> results) {
        List<String> lines = new ArrayList<>();
        if (results == null || results.isEmpty()) {
            lines.add("Nincs rögzített játékos eredmény.");
            return lines;
        }

        int index = 1;
        for (ResultPanel.PlayerResult result : results) {
            lines.add(index + ". " + result.getPlayerName() + " (" + result.getRoleDisplayName() + ")");
            for (String detail : result.getDetails()) {
                lines.add("   " + detail);
            }
            index++;
        }
        return lines;
    }

    private void registerPlayers(GameSession session) throws Exception {
        for (PlayerRegisterPanel.RegisteredPlayer player : playerRegisterPanel.getRegisteredPlayers()) {
            session.registerPlayer(player.getName(), player.getRole());
        }
    }

}