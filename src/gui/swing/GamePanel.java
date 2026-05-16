package gui.swing;

import actors.BusDriver;
import actors.Cleaner;
import actors.Player;
import cli.ConsoleOutput;
import entities.Bus;
import gui.application.GameSession;
import gui.application.MapDescriptor;
import gui.snapshot.GameSnapshot;
import entities.Snowplow;
import equipments.Consumable;
import equipments.Plow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import topology.Lane;
import topology.Road;

/**
 * A futó játék fő nézete.
 * Osszefogja a terkepet, a szerepkorfuggo vezerloket es az also visszajelzo savot.
 */
public class GamePanel extends JPanel {
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color PANEL_BORDER = new Color(17, 24, 39);
    private static final Color INFO_BACKGROUND = new Color(239, 246, 255);
    private static final Color SUCCESS_TEXT = new Color(21, 128, 61);
    private static final Color ERROR_TEXT = new Color(185, 28, 28);
    private static final Color WARNING_TEXT = new Color(202, 138, 4);
    private static final Color INFO_TEXT = new Color(180, 83, 9);
    private static final String LANE_PLACEHOLDER = "Cél sáv";

    private GameSession session;
    private MapDescriptor mapDescriptor;
    private List<PlayerRegisterPanel.RegisteredPlayer> players = new ArrayList<>();
    private final Map<String, Integer> prototypeValues = new LinkedHashMap<>();
    private final Map<String, Integer> completedTurnsByPlayer = new LinkedHashMap<>();
    private int currentPlayerIndex;

    private final MapCanvas mapCanvas = new MapCanvas();
    private final SelectionState selectionState = new SelectionState();

    private final JLabel playerLabel = new JLabel("Játékos: -");
    private final JLabel roleLabel = new JLabel("Szerepkör: -", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Pénz/Pont: 0", SwingConstants.RIGHT);
    private final JButton shopButton = new JButton(SwingActionText.OPEN_SHOP);
    private final JButton equipButton = new JButton(SwingActionText.EQUIP_PLOW);
    private final JButton refillButton = new JButton(SwingActionText.REFILL);
    private final JButton helpButton = new JButton(SwingActionText.HELP);
    private final JButton endGameButton = new JButton(SwingActionText.END_GAME);
    private final JButton executeActionButton = new JButton(SwingActionText.EXECUTE_ACTION);
    private final JComboBox<String> roadComboBox = new JComboBox<>(new String[] {"Cél út"});
    private final JComboBox<String> laneComboBox = new JComboBox<>(new String[] {LANE_PLACEHOLDER});
    private final JLabel statusLabel = new JLabel("Várakozás műveletre.");
    private transient ActionListener endGameListener;
    private final Map<String, List<String>> laneIdsByRoadId = new LinkedHashMap<>();
    private boolean suppressTargetComboEvents;

    /**
     * Létrehozza a játéknézet alapvető elrendezését.
     */
    public GamePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        configureMapCanvas();
        configureActions();
        installKeyboardShortcuts();

        add(createTopBar(), BorderLayout.NORTH);
        add(mapCanvas, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.EAST);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Összeköti a panelt egy futó grafikus sessionnel.
     *
     * @param session a panelhez tartozó játékmenet
     */
    public void bindSession(GameSession session) {
        MapDescriptor descriptor = session == null ? null : session.getMapDescriptor();
        bindSession(session, descriptor, new ArrayList<>());
    }

    /**
     * Osszekoti a panelt egy sessionnel, palyaval es a prototipus-jatekoslistaval.
     *
     * @param session a panelhez tartozo jatekmenet
     * @param mapDescriptor a kivalasztott palya
     * @param registeredPlayers a regisztralt jatekosok
     */
    public void bindSession(GameSession session, MapDescriptor mapDescriptor,
                            List<PlayerRegisterPanel.RegisteredPlayer> registeredPlayers) {
        this.session = session;
        this.mapDescriptor = mapDescriptor;
        this.players = registeredPlayers == null ? new ArrayList<>() : new ArrayList<>(registeredPlayers);
        this.currentPlayerIndex = 0;
        rebuildPrototypeValues();
        selectionState.clear();
        rebuildMovementTargets();

        if (session != null) {
            mapCanvas.setLayout(session.getMapLayout());
            refresh(session.getSnapshot());
        } else {
            refresh(null);
        }
        publishStatus("Pálya betöltve: " + getMapName(), FeedbackType.INFO);
        announceCurrentPlayerStatus();
    }

    /**
     * Frissíti a teljes panelrendszert az aktuális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void refresh(GameSnapshot snapshot) {
        syncCurrentPlayerWithSession();
        mapCanvas.setSnapshot(snapshot);
        mapCanvas.setSelectionState(selectionState);
        updateHeader(snapshot);
        updateRoleControls();
    }

    /**
     * Visszaadja a panelhez kötött sessiont.
     *
     * @return az aktuális session
     */
    public GameSession getSession() {
        return session;
    }

    /**
     * Konzolos parancs utan frissiti a GUI-allapotot.
     *
     * @param command a vegrehajtott konzolparancs
     */
    public void refreshFromConsoleCommand(String command) {
        refreshFromSession();
    }

    /**
     * GUI visszajelzés frissítése konzolparancs eredménye alapján.
     * Ez nem tükröz vissza újra a konzolra.
     *
     * @param message a megjelenítendő üzenet
     * @param type a visszajelzés típusa
     */
    public void showConsoleFeedback(String message, FeedbackType type) {
        setStatusMessage(message, type);
    }

    public void setEndGameListener(ActionListener listener) {
        this.endGameListener = listener;
    }

    public List<ResultPanel.PlayerResult> createPlayerResults() {
        List<ResultPanel.PlayerResult> results = new ArrayList<>();
        for (PlayerRegisterPanel.RegisteredPlayer player : players) {
            results.add(new ResultPanel.PlayerResult(
                player.getName(),
                player.getRoleDisplayName(),
                createResultDetails(player)));
        }
        return results;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new GridLayout(1, 3, 12, 0));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, PANEL_BORDER),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        styleTopLabel(playerLabel);
        styleTopLabel(roleLabel);
        styleTopLabel(scoreLabel);

        topBar.add(playerLabel);
        topBar.add(roleLabel);
        topBar.add(scoreLabel);
        return topBar;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(241, 245, 249));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, PANEL_BORDER),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        controlPanel.setPreferredSize(new Dimension(210, 0));

        JLabel title = new JLabel("Vezérlés", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        controlPanel.add(title);
        controlPanel.add(Box.createVerticalStrut(16));

        addControlButton(controlPanel, shopButton);
        addControlButton(controlPanel, equipButton);
        addControlButton(controlPanel, refillButton);
        controlPanel.add(Box.createVerticalGlue());
        addControlButton(controlPanel, helpButton);
        addControlButton(controlPanel, endGameButton);
        return controlPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, PANEL_BORDER));

        JPanel actionRow = new JPanel(new BorderLayout(12, 0));
        actionRow.setBackground(Color.WHITE);
        actionRow.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JPanel targetPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        targetPanel.setOpaque(false);
        targetPanel.add(roadComboBox);
        targetPanel.add(laneComboBox);
        styleButton(executeActionButton);
        actionRow.add(targetPanel, BorderLayout.CENTER);
        actionRow.add(executeActionButton, BorderLayout.WEST);

        JPanel feedbackRow = new JPanel(new BorderLayout(12, 0));
        feedbackRow.setBackground(INFO_BACKGROUND);
        feedbackRow.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 12f));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setVerticalAlignment(SwingConstants.CENTER);
        statusLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        statusLabel.setVerticalTextPosition(SwingConstants.CENTER);
        feedbackRow.add(statusLabel, BorderLayout.CENTER);

        bottomPanel.add(actionRow);
        bottomPanel.add(feedbackRow);
        return bottomPanel;
    }

    private void configureMapCanvas() {
        mapCanvas.setBackground(Color.WHITE);
        mapCanvas.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        mapCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    handleMapSelection(event);
                } else if (SwingUtilities.isRightMouseButton(event)) {
                    handleMapInfoRequest(event);
                }
            }
        });
    }

    private void configureActions() {
        shopButton.addActionListener(event -> openShopDialog());
        equipButton.addActionListener(event -> openPlowSelectionDialog());
        refillButton.addActionListener(event -> refillActiveSnowplow());
        roadComboBox.addActionListener(event -> updateLaneTargetsForSelectedRoad());
        executeActionButton.addActionListener(event -> executeSelectedAction());
        helpButton.addActionListener(event -> showHelp());
        endGameButton.addActionListener(event -> {
            if (endGameListener != null) {
                endGameListener.actionPerformed(event);
            }
        });
    }

    private void installKeyboardShortcuts() {
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearSelection");
        getActionMap().put("clearSelection", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearSelection();
            }
        });

        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "executeAction");
        getActionMap().put("executeAction", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                executeActionButton.doClick();
            }
        });
    }

    private void addControlButton(JPanel panel, JButton button) {
        styleButton(button);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
    }

    private static void styleTopLabel(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
    }

    private void handleMapSelection(MouseEvent event) {
        String selection = event.getX() + "," + event.getY();
        selectionState.setSelectedLaneId(selection);
        mapCanvas.repaint();
        publishStatus("Kijelölés frissítve a térképen: " + selection, FeedbackType.INFO);
    }

    private void handleMapInfoRequest(MouseEvent event) {
        publishStatus("Lekérdezett pont: x=" + event.getX() + ", y=" + event.getY(), FeedbackType.INFO);
    }

    private void executeSelectedAction() {
        if (roadComboBox.getSelectedIndex() <= 0 || laneComboBox.getSelectedIndex() <= 0) {
            String message = "Válassz cél utat és cél sávot a mozgáshoz.";
            publishStatus(message, FeedbackType.ERROR);
            return;
        }

        String roadId = String.valueOf(roadComboBox.getSelectedItem());
        String laneId = String.valueOf(laneComboBox.getSelectedItem());

        if (session == null) {
            String message = "A mozgás nem futtatható aktív játékmenet nélkül.";
            publishStatus(message, FeedbackType.ERROR);
            return;
        }

        try {
            if (isCleanerTurn()) {
                Snowplow snowplow = resolveActiveSnowplow();
                if (snowplow == null) {
                    throw new IllegalStateException("Nincs kiválasztható hókotró a mozgáshoz.");
                }
                session.moveSnowplow(objectId(snowplow), roadId, laneId);
            } else {
                Bus bus = resolveActiveBus();
                if (bus == null) {
                    throw new IllegalStateException("Nincs kiválasztható busz a mozgáshoz.");
                }
                session.moveBus(objectId(bus), roadId, laneId);
            }

            refreshFromSession();
            String message = "Mozgás végrehajtva, a következő játékos következik.";
            publishStatus(message, FeedbackType.SUCCESS);
            announceCurrentPlayerStatus();
        } catch (Exception exception) {
            String message = "Sikertelen mozgás: " + exception.getMessage();
            publishStatus(message, FeedbackType.ERROR);
        }
    }

    private void openShopDialog() {
        if (!isCleanerTurn()) {
            publishStatus("A bolt csak Hókotrós körben érhető el.", FeedbackType.ERROR);
            return;
        }

        Frame owner = JOptionPane.getFrameForComponent(this);
        ShopDialog dialog = new ShopDialog(owner, session, this::publishStatus, this::refreshFromSession);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openPlowSelectionDialog() {
        if (!isCleanerTurn()) {
            publishStatus("Ekeválasztás csak Hókotrós körben érhető el.", FeedbackType.ERROR);
            return;
        }
        Snowplow snowplow = resolveActiveSnowplow();
        if (snowplow == null) {
            publishStatus("Nincs kiválasztható hókotró az ekeváltáshoz.", FeedbackType.ERROR);
            return;
        }

        List<PlowSelectionDialog.PlowOption> plows = collectAvailablePlows(snowplow);
        if (plows.isEmpty()) {
            publishStatus("Nincs saját eke, amit fel lehetne szerelni.", FeedbackType.ERROR);
            return;
        }

        Frame owner = JOptionPane.getFrameForComponent(this);
        PlowSelectionDialog dialog = new PlowSelectionDialog(owner, session, objectId(snowplow), plows,
            this::publishStatus, this::refreshFromSession);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refillActiveSnowplow() {
        Snowplow snowplow = resolveActiveSnowplow();
        Plow equippedPlow = snowplow == null ? null : snowplow.getEquippedPlow();
        if (!supportsRefill(equippedPlow)) {
            publishStatus("Az aktuális ekéhez nem tartozik utántölthető tartály.", FeedbackType.ERROR);
            return;
        }

        Cleaner cleaner = getCurrentCleanerModel();
        Consumable consumable = findCompatibleConsumable(cleaner, equippedPlow.getConsumableType());
        if (consumable == null) {
            publishStatus("Nincs kompatibilis anyag az utántöltéshez: "
                + equippedPlow.getConsumableType() + ".", FeedbackType.ERROR);
            return;
        }

        try {
            session.refill(objectId(snowplow), objectId(consumable));
            publishStatus("Sikeres utántöltés: " + objectId(snowplow) + " <- "
                + objectId(consumable) + ".", FeedbackType.SUCCESS);
            refreshFromSession();
        } catch (Exception exception) {
            publishStatus("Sikertelen utántöltés: " + exception.getMessage(), FeedbackType.ERROR);
        }
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
            "Bal kattintás: kijelölés\nJobb kattintás: információ\nEsc: kijelölés törlése\nEnter: mozgás végrehajtása",
            "Súgó", JOptionPane.INFORMATION_MESSAGE);
        publishStatus("Súgó megnyitva.", FeedbackType.INFO);
    }

    private void clearSelection() {
        selectionState.clear();
        mapCanvas.repaint();
        publishStatus("Kijelölés törölve.", FeedbackType.INFO);
    }

    private void updateHeader(GameSnapshot snapshot) {
        PlayerRegisterPanel.RegisteredPlayer player = getCurrentPlayer();
        if (player != null) {
            playerLabel.setText("Játékos: " + player.getName());
            roleLabel.setText("Szerepkör: " + player.getRoleDisplayName());
            scoreLabel.setText(player.getPlayerRole().getScoreLabel() + ": " + prototypeValues.getOrDefault(player.getName(), 0));
            return;
        }

        String playerId = snapshot == null || snapshot.getCurrentPlayerId() == null ? "-" : snapshot.getCurrentPlayerId();
        String role = session == null ? "-" : roleDisplayName(session.getCurrentPlayerRole());
        playerLabel.setText("Játékos: " + playerId);
        roleLabel.setText("Szerepkör: " + role);
        scoreLabel.setText("Pénz/Pont: 0");
    }

    private void updateRoleControls() {
        boolean cleanerTurn = isCleanerTurn();
        shopButton.setVisible(cleanerTurn);
        equipButton.setVisible(cleanerTurn);
        refillButton.setVisible(cleanerTurn);
        refillButton.setEnabled(cleanerTurn && supportsRefill(resolveEquippedPlow()));
        executeActionButton.setText(cleanerTurn ? SwingActionText.MOVE_SNOWPLOW : SwingActionText.MOVE_BUS);
        revalidate();
        repaint();
    }

    private boolean isCleanerTurn() {
        if (session != null) {
            return "Cleaner".equals(session.getCurrentPlayerRole());
        }
        PlayerRegisterPanel.RegisteredPlayer player = getCurrentPlayer();
        if (player != null) {
            return player.isCleaner();
        }
        return true;
    }

    private void refreshFromSession() {
        rebuildMovementTargets();
        refresh(session == null ? null : session.getSnapshot());
    }

    private void announceCurrentPlayerStatus() {
        if (session != null) {
            session.announceCurrentPlayerStatus();
        }
    }

    private Bus resolveActiveBus() {
        if (session == null || session.getGame() == null || session.getRegistry() == null) {
            return null;
        }
        Player player = session.getGame().getCurrentPlayer(session.getRegistry());
        if (!BusDriver.class.isInstance(player)) {
            return null;
        }
        return BusDriver.class.cast(player).getManagedBus();
    }

    private Cleaner getCurrentCleanerModel() {
        if (session == null || session.getGame() == null || session.getRegistry() == null) {
            return null;
        }
        Player player = session.getGame().getCurrentPlayer(session.getRegistry());
        if (player != null && player.isCleaner()) {
            return Cleaner.class.cast(player);
        }
        return null;
    }

    private Snowplow resolveActiveSnowplow() {
        Cleaner cleaner = getCurrentCleanerModel();
        if (cleaner == null) {
            return null;
        }
        String selectedVehicleId = selectionState.getSelectedVehicleId();
        if (selectedVehicleId != null && session != null && session.getRegistry() != null) {
            Object selectedObject = session.getRegistry().getObjects().get(selectedVehicleId);
            if (Snowplow.class.isInstance(selectedObject)) {
                Snowplow selectedSnowplow = Snowplow.class.cast(selectedObject);
                if (selectedSnowplow.getOwner() == cleaner || cleaner.getFleet().contains(selectedSnowplow)) {
                    return selectedSnowplow;
                }
            }
        }
        return cleaner.getFleet().isEmpty() ? null : cleaner.getFleet().get(0);
    }

    private Plow resolveEquippedPlow() {
        Snowplow snowplow = resolveActiveSnowplow();
        return snowplow == null ? null : snowplow.getEquippedPlow();
    }

    private boolean supportsRefill(Plow plow) {
        if (plow == null || plow.getConsumableType() == null) {
            return false;
        }
        String consumableType = plow.getConsumableType();
        return "Biokerosene".equals(consumableType) || "Salt".equals(consumableType)
            || "Gravel".equals(consumableType);
    }

    private Consumable findCompatibleConsumable(Cleaner cleaner, String consumableType) {
        if (cleaner == null || consumableType == null) {
            return null;
        }
        for (Consumable consumable : cleaner.getInventory()) {
            if (consumableType.equals(consumable.getConsumableType())) {
                return consumable;
            }
        }
        return null;
    }

    private List<PlowSelectionDialog.PlowOption> collectAvailablePlows(Snowplow snowplow) {
        List<PlowSelectionDialog.PlowOption> options = new ArrayList<>();
        Cleaner cleaner = getCurrentCleanerModel();
        if (session == null || session.getRegistry() == null || cleaner == null) {
            return options;
        }

        for (Plow plow : session.getRegistry().getByType(Plow.class)) {
            if (plow.getOwner() == cleaner) {
                String id = objectId(plow);
                if (isUsableObjectId(id)) {
                    options.add(new PlowSelectionDialog.PlowOption(id, displayPlowName(plow), plow.isEquipped()));
                }
            }
        }

        Plow equippedPlow = snowplow == null ? null : snowplow.getEquippedPlow();
        String equippedId = objectId(equippedPlow);
        if (equippedPlow != null && isUsableObjectId(equippedId) && !containsPlowOption(options, equippedId)) {
            options.add(new PlowSelectionDialog.PlowOption(equippedId, displayPlowName(equippedPlow), true));
        }
        return options;
    }

    private boolean containsPlowOption(List<PlowSelectionDialog.PlowOption> options, String id) {
        for (PlowSelectionDialog.PlowOption option : options) {
            if (option.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private String displayPlowName(Plow plow) {
        if (plow == null) {
            return "Ismeretlen eke";
        }
        switch (plow.getClass().getSimpleName()) {
            case "DragonPlow":
                return "Sárkány eke";
            case "SaltPlow":
                return "Sózó eke";
            case "DumpPlow":
                return "Dömper eke";
            case "SweeperPlow":
                return "Seprő eke";
            case "IcebreakerPlow":
                return "Jégtörő eke";
            case "GravelPlow":
                return "Kavicsszóró eke";
            default:
                return plow.getClass().getSimpleName();
        }
    }

    private String objectId(Object object) {
        if (object == null || session == null || session.getRegistry() == null) {
            return "null";
        }
        return session.getRegistry().findId(object);
    }

    private void rebuildMovementTargets() {
        suppressTargetComboEvents = true;
        laneIdsByRoadId.clear();
        roadComboBox.removeAllItems();
        laneComboBox.removeAllItems();
        roadComboBox.addItem("Cél út");
        laneComboBox.addItem(LANE_PLACEHOLDER);

        if (session != null && session.getRegistry() != null) {
            List<Road> roads = session.getRegistry().getByType(Road.class);
            for (Road road : roads) {
                String roadId = objectId(road);
                if (!isUsableObjectId(roadId)) {
                    continue;
                }

                List<String> laneIds = new ArrayList<>();
                for (Lane lane : road.getLanes()) {
                    String laneId = objectId(lane);
                    if (isUsableObjectId(laneId)) {
                        laneIds.add(laneId);
                    }
                }
                if (laneIds.isEmpty()) {
                    for (Lane lane : session.getRegistry().getByType(Lane.class)) {
                        if (lane.getRoad() == road) {
                            String laneId = objectId(lane);
                            if (isUsableObjectId(laneId)) {
                                laneIds.add(laneId);
                            }
                        }
                    }
                }
                if (!laneIds.isEmpty()) {
                    laneIdsByRoadId.put(roadId, laneIds);
                    roadComboBox.addItem(roadId);
                }
            }
        }

        suppressTargetComboEvents = false;
    }

    private void updateLaneTargetsForSelectedRoad() {
        if (suppressTargetComboEvents) {
            return;
        }

        suppressTargetComboEvents = true;
        laneComboBox.removeAllItems();
        laneComboBox.addItem(LANE_PLACEHOLDER);
        String roadId = String.valueOf(roadComboBox.getSelectedItem());
        List<String> laneIds = laneIdsByRoadId.get(roadId);
        if (laneIds != null) {
            for (String laneId : laneIds) {
                laneComboBox.addItem(laneId);
            }
        }
        suppressTargetComboEvents = false;
    }

    private void syncCurrentPlayerWithSession() {
        if (session == null || session.getGame() == null || session.getRegistry() == null || players.isEmpty()) {
            return;
        }

        Player currentPlayer = session.getGame().getCurrentPlayer(session.getRegistry());
        if (currentPlayer == null || currentPlayer.getName() == null) {
            return;
        }

        for (int index = 0; index < players.size(); index++) {
            if (players.get(index).getName().equalsIgnoreCase(currentPlayer.getName())) {
                currentPlayerIndex = index;
                return;
            }
        }
    }

    private boolean isUsableObjectId(String id) {
        return id != null && !id.isBlank() && !"?".equals(id) && !"null".equals(id);
    }

    private PlayerRegisterPanel.RegisteredPlayer getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(Math.floorMod(currentPlayerIndex, players.size()));
    }

    private void rebuildPrototypeValues() {
        prototypeValues.clear();
        completedTurnsByPlayer.clear();
        for (PlayerRegisterPanel.RegisteredPlayer player : players) {
            prototypeValues.put(player.getName(), player.getPlayerRole().getInitialValue());
            completedTurnsByPlayer.put(player.getName(), 0);
        }
    }

    private List<String> createResultDetails(PlayerRegisterPanel.RegisteredPlayer player) {
        List<String> details = new ArrayList<>();
        details.add(player.getPlayerRole().getScoreLabel() + ": " + prototypeValues.getOrDefault(
            player.getName(), player.getPlayerRole().getInitialValue()));
        details.add("Lezárt körök: " + completedTurnsByPlayer.getOrDefault(player.getName(), 0));
        if (player.isCleaner()) {
            details.add("Jármű induláskor: 1 hókotró");
            details.add("Felszerelés induláskor: SweeperPlow");
            details.add("Anyagkészlet induláskor: üres");
        } else {
            details.add("Jármű induláskor: 1 busz");
            details.add("Pontszerzés: célba érkezéssel");
        }
        return details;
    }

    private String getMapName() {
        return mapDescriptor == null ? "ismeretlen pálya" : mapDescriptor.getDisplayName();
    }

    private static String roleDisplayName(String modelRoleName) {
        if ("BusDriver".equals(modelRoleName)) {
            return "Busz sofőr";
        }
        if ("Cleaner".equals(modelRoleName)) {
            return "Hókotrós";
        }
        return "-";
    }

    private void setStatusMessage(String message) {
        setStatusMessage(message, FeedbackType.INFO);
    }

    private void setStatusMessage(String message, FeedbackType type) {
        statusLabel.setText(message == null || message.isBlank() ? "Várakozás műveletre." : message);
        if (type == FeedbackType.SUCCESS) {
            statusLabel.setForeground(SUCCESS_TEXT);
        } else if (type == FeedbackType.ERROR) {
            statusLabel.setForeground(ERROR_TEXT);
        } else if (type == FeedbackType.WARNING) {
            statusLabel.setForeground(WARNING_TEXT);
        } else {
            statusLabel.setForeground(INFO_TEXT);
        }
    }

    private void publishStatus(String message, FeedbackType type) {
        setStatusMessage(message, type);
        mirrorStatusToConsole(message, type);
    }

    private void mirrorStatusToConsole(String message, FeedbackType type) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (type == FeedbackType.SUCCESS) {
            ConsoleOutput.success(message);
        } else if (type == FeedbackType.ERROR) {
            ConsoleOutput.error(message);
        } else if (type == FeedbackType.WARNING) {
            ConsoleOutput.warning(message);
        } else {
            ConsoleOutput.plain(message);
        }
    }
}