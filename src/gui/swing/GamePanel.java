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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
    private static final Color HEADER_DEFAULT_TEXT = new Color(15, 23, 42);
    private static final String ROAD_PLACEHOLDER = "Cél út";
    private static final String LANE_PLACEHOLDER = "Cél sáv";
    private static final String ROLE_CLEANER = "Cleaner";
    private static final String ROLE_BUS_DRIVER = "BusDriver";
    private static final String ROLE_LABEL_PREFIX = "Szerepkör: ";

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
    private final JButton endGameButton = new JButton(SwingActionText.END_GAME);
    private final JButton executeActionButton = new JButton(SwingActionText.EXECUTE_ACTION);
    private final JComboBox<TargetOption> roadComboBox = new JComboBox<>();
    private final JComboBox<TargetOption> laneComboBox = new JComboBox<>();
    private final JLabel statusLabel = new JLabel("Várakozás műveletre.");
    private transient ActionListener endGameListener;
    private final Map<String, List<String>> laneIdsByRoadId = new LinkedHashMap<>();
    private boolean suppressTargetComboEvents;
    private boolean suppressBlockedTurnSkip;

    /**
     * Létrehozza a játéknézet alapvető elrendezését.
     */
    public GamePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        configureMapCanvas();
        configureActions();

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
        String skippedBusMessage = suppressBlockedTurnSkip ? null : skipBlockedBusTurnIfNeeded();
        if (skippedBusMessage != null) {
            suppressBlockedTurnSkip = true;
            try {
                refreshFromSession();
                publishStatus(skippedBusMessage, FeedbackType.WARNING);
            } finally {
                suppressBlockedTurnSkip = false;
            }
            return;
        }
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
        ResultPanel.PlayerResult winner = createWinnerResult();
        if (winner != null) {
            results.add(winner);
        }
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
        mapCanvas.setMapClickListener(this::handleMapClick);
    }

    private void configureActions() {
        shopButton.addActionListener(event -> openShopDialog());
        equipButton.addActionListener(event -> openPlowSelectionDialog());
        refillButton.addActionListener(event -> refillActiveSnowplow());
        roadComboBox.addActionListener(event -> updateLaneTargetsForSelectedRoad());
        executeActionButton.addActionListener(event -> executeSelectedAction());
        endGameButton.addActionListener(event -> {
            if (endGameListener != null) {
                endGameListener.actionPerformed(event);
            }
        });
    }

    private void handleMapClick(String elementId, String category, boolean rightClick) {
        if (!isUsableObjectId(elementId)) {
            if (!rightClick) {
                selectionState.clear();
                refreshFromSession();
            }
            publishStatus("Nincs kiválasztható pályaelem ezen a ponton.", FeedbackType.INFO);
            return;
        }

        if (rightClick) {
            publishStatus(createElementDetails(elementId), FeedbackType.INFO);
            return;
        }

        selectMapElement(elementId, category);
    }

    private void selectMapElement(String elementId, String category) {
        if ("vehicle".equals(category)) {
            selectionState.setSelectedVehicleId(elementId);
            rebuildMovementTargets();
            refreshFromSession();
            publishStatus("Jármű kijelölve: " + elementId + ".", FeedbackType.INFO);
            return;
        }
        if ("lane".equals(category)) {
            selectTargetLane(elementId);
            return;
        }
        if ("node".equals(category)) {
            selectionState.setHoveredElementId(elementId);
            mapCanvas.repaint();
            publishStatus(createElementDetails(elementId), FeedbackType.INFO);
            return;
        }
        publishStatus("Kijelölt elem: " + elementId + ".", FeedbackType.INFO);
    }

    private void selectTargetLane(String laneId) {
        if (session == null || session.getRegistry() == null) {
            publishStatus("A sáv kijelölése nem futtatható aktív játékmenet nélkül.", FeedbackType.ERROR);
            return;
        }
        Object laneObject = session.getRegistry().getObjects().get(laneId);
        if (!Lane.class.isInstance(laneObject)) {
            publishStatus("A kijelölt elem nem sáv: " + laneId + ".", FeedbackType.ERROR);
            return;
        }
        Lane lane = Lane.class.cast(laneObject);
        String roadId = objectId(lane.getRoad());
        if (!containsReachableLane(roadId, laneId)) {
            selectionState.setSelectedLaneId(laneId);
            selectionState.setSelectedRoadId(roadId);
            mapCanvas.repaint();
            publishStatus("Ez a sáv most nem elérhető célpont: " + describeLane(lane) + ".", FeedbackType.WARNING);
            return;
        }

        selectionState.setSelectedRoadId(roadId);
        selectionState.setSelectedLaneId(laneId);
        selectRoadOptionById(roadId);
        updateLaneTargetsForSelectedRoad();
        selectLaneOptionById(laneId);
        mapCanvas.repaint();
        publishStatus("Cél kijelölve: " + describeLane(lane) + ".", FeedbackType.INFO);
    }

    private boolean containsReachableLane(String roadId, String laneId) {
        List<String> laneIds = laneIdsByRoadId.get(roadId);
        return laneIds != null && laneIds.contains(laneId);
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

    private void executeSelectedAction() {
        if (roadComboBox.getSelectedIndex() <= 0 || laneComboBox.getSelectedIndex() <= 0) {
            String message = "Válassz cél utat és cél sávot a mozgáshoz.";
            publishStatus(message, FeedbackType.ERROR);
            return;
        }

        String roadId = selectedTargetId(roadComboBox);
        String laneId = selectedTargetId(laneComboBox);
        if (!isUsableObjectId(roadId) || !isUsableObjectId(laneId)) {
            String message = "Válassz cél utat és cél sávot a mozgáshoz.";
            publishStatus(message, FeedbackType.ERROR);
            return;
        }

        if (session == null) {
            String message = "A mozgás nem futtatható aktív játékmenet nélkül.";
            publishStatus(message, FeedbackType.ERROR);
            return;
        }

        try {
            MovementResult movementResult = executeMovement(roadId, laneId);

            refreshFromSession();
            publishStatus(movementResult.message, movementResult.feedbackType);
            announceCurrentPlayerStatus();
        } catch (Exception exception) {
            String message = "Sikertelen mozgás: " + exception.getMessage();
            publishStatus(message, FeedbackType.ERROR);
        }
    }

    private MovementResult executeMovement(String roadId, String laneId) throws Exception {
        if (isCleanerTurn()) {
            return executeCleanerMovement(roadId, laneId);
        }
        return executeBusMovement(roadId, laneId);
    }

    private MovementResult executeCleanerMovement(String roadId, String laneId) throws Exception {
        Cleaner cleaner = getCurrentCleanerModel();
        int walletBefore = walletAmount(cleaner);
        Snowplow snowplow = resolveActiveSnowplow();
        if (snowplow == null) {
            throw new IllegalStateException("Nincs kiválasztható hókotró a mozgáshoz.");
        }

        session.moveSnowplow(objectId(snowplow), roadId, laneId);
        int walletAfter = walletAmount(cleaner);
        if (walletAfter > walletBefore) {
            return MovementResult.success("Mozgás végrehajtva, takarításért +" + (walletAfter - walletBefore) + " pénz.");
        }
        return MovementResult.success("Mozgás végrehajtva, a következő játékos következik.");
    }

    private MovementResult executeBusMovement(String roadId, String laneId) throws Exception {
        BusDriver driver = resolveActiveBusDriver();
        Bus bus = resolveActiveBus();
        if (bus == null) {
            throw new IllegalStateException("Nincs kiválasztható busz a mozgáshoz.");
        }

        int scoreBefore = driver == null ? 0 : driver.getScore();
        session.moveBus(objectId(bus), roadId, laneId);
        if (bus.isSnowBlocked()) {
            return MovementResult.warning("A busz elakadt: " + describeLane(bus.getCurrentLane())
                + ". Hókotrós következik, amíg fel nem szabadítják.");
        }
        if (bus.getIsParalyzed()) {
            return MovementResult.warning("Ütközés történt: " + describeLane(bus.getCurrentLane())
                + ". A busz " + bus.getParalysisTimer() + " tickig áll.");
        }
        if (driver != null && driver.getScore() > scoreBefore) {
            return MovementResult.success("A busz célba ért, +" + (driver.getScore() - scoreBefore)
                + " pont. Az új cél az ellenkező végállomás.");
        }
        return MovementResult.success("Mozgás végrehajtva, a következő játékos következik.");
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

    private void updateHeader(GameSnapshot snapshot) {
        PlayerRegisterPanel.RegisteredPlayer player = getCurrentPlayer();
        if (player != null) {
            playerLabel.setText("Játékos: " + player.getName());
            roleLabel.setText(ROLE_LABEL_PREFIX + player.getRoleDisplayName());
            scoreLabel.setText(player.getPlayerRole().getScoreLabel() + ": " + resolveModelValue(player));
            applyHeaderRoleColors(player.getRole());
            return;
        }

        String playerId = snapshot == null || snapshot.getCurrentPlayerId() == null ? "-" : snapshot.getCurrentPlayerId();
        String modelRole = session == null ? null : session.getCurrentPlayerRole();
        String role = modelRole == null ? "-" : roleDisplayName(modelRole);
        playerLabel.setText("Játékos: " + playerId);
        roleLabel.setText(ROLE_LABEL_PREFIX + role);
        scoreLabel.setText("Pénz/Pont: 0");
        applyHeaderRoleColors(modelRole);
    }

    private void applyHeaderRoleColors(String modelRole) {
        Color roleColor = HEADER_DEFAULT_TEXT;
        if (ROLE_CLEANER.equals(modelRole)) {
            roleColor = GameColors.SNOWPLOW;
        } else if (ROLE_BUS_DRIVER.equals(modelRole)) {
            roleColor = GameColors.BUS;
        }

        playerLabel.setForeground(roleColor);
        roleLabel.setForeground(roleColor);
    }

    private void updateRoleControls() {
        boolean cleanerTurn = isCleanerTurn();
        shopButton.setVisible(cleanerTurn);
        equipButton.setVisible(cleanerTurn);
        refillButton.setVisible(cleanerTurn);
        refillButton.setEnabled(cleanerTurn && supportsRefill(resolveEquippedPlow()));
        executeActionButton.setText(cleanerTurn ? SwingActionText.MOVE_SNOWPLOW : SwingActionText.MOVE_BUS);
        executeActionButton.setEnabled(roadComboBox.getItemCount() > 1);
        revalidate();
        repaint();
    }

    private boolean isCleanerTurn() {
        if (session != null) {
            return ROLE_CLEANER.equals(session.getCurrentPlayerRole());
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

    private BusDriver resolveActiveBusDriver() {
        if (session == null || session.getGame() == null || session.getRegistry() == null) {
            return null;
        }
        Player player = session.getGame().getCurrentPlayer(session.getRegistry());
        if (!BusDriver.class.isInstance(player)) {
            return null;
        }
        return BusDriver.class.cast(player);
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

    private String skipBlockedBusTurnIfNeeded() {
        BusDriver driver = resolveActiveBusDriver();
        if (driver == null || session == null) {
            return null;
        }
        Bus bus = driver.getManagedBus();
        if (bus == null) {
            session.finishCurrentTurn(false);
            return "A buszsofőrhöz nem tartozik busz, ezért Hókotrós következik.";
        }
        if (bus.isSnowBlocked()) {
            session.finishCurrentTurn(false);
            return "A busz elakadt: " + describeLane(bus.getCurrentLane())
                + ". Nem tud továbbhaladni, Hókotrós következik.";
        }
        if (bus.getIsParalyzed()) {
            session.finishCurrentTurn(false);
            return "Ütközés történt: " + describeLane(bus.getCurrentLane())
                + ". A busz " + bus.getParalysisTimer() + " tickig áll, Hókotrós következik.";
        }
        if (bus.getCurrentLane() == null) {
            session.finishCurrentTurn(false);
            return "A busz nincs sávon, ezért Hókotrós következik.";
        }
        return null;
    }

    private int walletAmount(Cleaner cleaner) {
        if (cleaner == null || cleaner.getWallet() == null) {
            return 0;
        }
        return cleaner.getWallet().getAmount();
    }

    private String describeLane(Lane lane) {
        if (lane == null) {
            return "ismeretlen sáv";
        }
        Road road = lane.getRoad();
        String roadId = objectId(road);
        String roadLabel = null;
        if (session != null && session.getMapLayout() != null) {
            roadLabel = session.getMapLayout().getRoadLabel(roadId);
        }
        if (roadLabel == null || roadLabel.isBlank()) {
            roadLabel = isUsableObjectId(roadId) ? roadId : "ismeretlen út";
        }

        int laneIndex = 1;
        if (road != null && road.getLanes() != null) {
            int index = road.getLanes().indexOf(lane);
            if (index >= 0) {
                laneIndex = index + 1;
            }
        }
        return laneIndex + ". sáv (" + roadLabel + ")";
    }

    private String createElementDetails(String elementId) {
        GameSnapshot.Entry entry = findSnapshotEntry(elementId);
        if (entry == null) {
            return "Nincs részletes adat ehhez az elemhez: " + elementId + ".";
        }
        if ("lane".equals(entry.getCategory())) {
            return createLaneDetails(entry);
        }
        if ("vehicle".equals(entry.getCategory())) {
            return createVehicleDetails(entry);
        }
        if ("node".equals(entry.getCategory())) {
            return createNodeDetails(entry);
        }
        return entry.getCategory() + ": " + elementId + " (" + entry.getType() + ").";
    }

    private GameSnapshot.Entry findSnapshotEntry(String elementId) {
        if (session == null) {
            return null;
        }
        GameSnapshot snapshot = session.getSnapshot();
        return snapshot == null ? null : snapshot.findEntryById(elementId);
    }

    private String createLaneDetails(GameSnapshot.Entry laneEntry) {
        Object laneObject = session == null || session.getRegistry() == null
            ? null : session.getRegistry().getObjects().get(laneEntry.getId());
        String laneName = Lane.class.isInstance(laneObject)
            ? describeLane(Lane.class.cast(laneObject)) : laneEntry.getId();
        String condition = laneEntry.getAttribute("condition");
        String vehicles = laneEntry.getAttribute("vehicleIds");
        return "Sáv: " + laneName
            + ", állapot: " + displayCondition(condition)
            + ", hossz: " + nullSafe(laneEntry.getAttribute("length"))
            + ", járművek: " + emptyListText(vehicles) + ".";
    }

    private String createVehicleDetails(GameSnapshot.Entry vehicleEntry) {
        String type = displayVehicleType(vehicleEntry.getType());
        String laneId = vehicleEntry.getAttribute("currentLaneId");
        String progress = nullSafe(vehicleEntry.getAttribute("progress"));
        String laneLength = nullSafe(vehicleEntry.getAttribute("laneLength"));
        String paralyzed = "true".equalsIgnoreCase(vehicleEntry.getAttribute("isParalyzed")) ? "igen" : "nem";
        return "Jármű: " + type + " (" + vehicleEntry.getId() + ")"
            + ", sáv: " + laneId
            + ", haladás: " + progress + "/" + laneLength
            + ", bénult: " + paralyzed + ".";
    }

    private String createNodeDetails(GameSnapshot.Entry nodeEntry) {
        String label = nodeEntry.getLabel() == null || nodeEntry.getLabel().isBlank()
            ? nodeEntry.getId() : nodeEntry.getLabel();
        return "Csomópont: " + label + " (" + nodeEntry.getType() + ")"
            + ", kimenő utak: " + emptyListText(nodeEntry.getAttribute("outgoingRoadIds")) + ".";
    }

    private String displayCondition(String condition) {
        if (condition == null) {
            return "ismeretlen";
        }
        switch (condition) {
            case "CleanCondition":
                return "tiszta";
            case "ThinSnowCondition":
                return "vékony hó";
            case "ThickSnowCondition":
                return "vastag hó";
            case "IceCondition":
                return "jég";
            case "GraveledIceCondition":
                return "kavicsos jég";
            default:
                return condition;
        }
    }

    private String displayVehicleType(String type) {
        if (type == null) {
            return "ismeretlen";
        }
        if (type.contains("Bus")) {
            return "busz";
        }
        if (type.contains("Snowplow")) {
            return "hókotró";
        }
        if (type.contains("Car")) {
            return "autó";
        }
        return type;
    }

    private String nullSafe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String emptyListText(String value) {
        if (value == null || value.isBlank() || "[]".equals(value)) {
            return "nincs";
        }
        return value;
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
                    options.add(new PlowSelectionDialog.PlowOption(id, displayPlowName(plow),
                        plow.getClass().getSimpleName(), plow.isEquipped()));
                }
            }
        }

        Plow equippedPlow = snowplow == null ? null : snowplow.getEquippedPlow();
        String equippedId = objectId(equippedPlow);
        if (equippedPlow != null && isUsableObjectId(equippedId) && !containsPlowOption(options, equippedId)) {
            options.add(new PlowSelectionDialog.PlowOption(equippedId, displayPlowName(equippedPlow),
                equippedPlow.getClass().getSimpleName(), true));
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
        roadComboBox.addItem(TargetOption.placeholder(ROAD_PLACEHOLDER));
        laneComboBox.addItem(TargetOption.placeholder(LANE_PLACEHOLDER));

        if (session != null && session.getRegistry() != null) {
            String vehicleId = resolveActiveVehicleId();
            for (String laneId : session.getReachableTargets(vehicleId)) {
                addReachableLaneTarget(laneId);
            }
        }

        suppressTargetComboEvents = false;
    }

    private void addReachableLaneTarget(String laneId) {
        Object laneObject = session.getRegistry().getObjects().get(laneId);
        if (Lane.class.isInstance(laneObject)) {
            Lane lane = Lane.class.cast(laneObject);
            String roadId = objectId(lane.getRoad());
            if (isUsableObjectId(roadId) && isUsableObjectId(laneId)) {
                List<String> laneIds = laneIdsByRoadId.get(roadId);
                if (laneIds == null) {
                    laneIds = new ArrayList<>();
                    laneIdsByRoadId.put(roadId, laneIds);
                    roadComboBox.addItem(new TargetOption(roadId, resolveRoadDisplayName(roadId)));
                }
                laneIds.add(laneId);
            }
        }
    }

    private String resolveActiveVehicleId() {
        if (isCleanerTurn()) {
            return objectId(resolveActiveSnowplow());
        }
        return objectId(resolveActiveBus());
    }

    private void updateLaneTargetsForSelectedRoad() {
        if (suppressTargetComboEvents) {
            return;
        }

        suppressTargetComboEvents = true;
        laneComboBox.removeAllItems();
        laneComboBox.addItem(TargetOption.placeholder(LANE_PLACEHOLDER));
        String roadId = selectedTargetId(roadComboBox);
        List<String> laneIds = laneIdsByRoadId.get(roadId);
        if (laneIds != null) {
            for (String laneId : laneIds) {
                laneComboBox.addItem(createLaneOption(laneId));
            }
        }
        suppressTargetComboEvents = false;
    }

    private TargetOption createLaneOption(String laneId) {
        if (session != null && session.getRegistry() != null) {
            Object laneObject = session.getRegistry().getObjects().get(laneId);
            if (Lane.class.isInstance(laneObject)) {
                return new TargetOption(laneId, describeLane(Lane.class.cast(laneObject)));
            }
        }
        return new TargetOption(laneId, laneId);
    }

    private String resolveRoadDisplayName(String roadId) {
        if (session != null && session.getMapLayout() != null) {
            String roadLabel = session.getMapLayout().getRoadLabel(roadId);
            if (roadLabel != null && !roadLabel.isBlank()) {
                return roadLabel;
            }
        }
        return roadId;
    }

    private void selectRoadOptionById(String roadId) {
        if (!isUsableObjectId(roadId)) {
            roadComboBox.setSelectedIndex(0);
            return;
        }
        for (int index = 0; index < roadComboBox.getItemCount(); index++) {
            TargetOption option = roadComboBox.getItemAt(index);
            if (option != null && roadId.equals(option.getId())) {
                roadComboBox.setSelectedIndex(index);
                return;
            }
        }
        roadComboBox.setSelectedIndex(0);
    }

    private void selectLaneOptionById(String laneId) {
        if (!isUsableObjectId(laneId)) {
            laneComboBox.setSelectedIndex(0);
            return;
        }
        for (int index = 0; index < laneComboBox.getItemCount(); index++) {
            TargetOption option = laneComboBox.getItemAt(index);
            if (option != null && laneId.equals(option.getId())) {
                laneComboBox.setSelectedIndex(index);
                return;
            }
        }
        laneComboBox.setSelectedIndex(0);
    }

    private String selectedTargetId(JComboBox<TargetOption> comboBox) {
        TargetOption option = comboBox == null ? null : TargetOption.class.cast(comboBox.getSelectedItem());
        return option == null ? null : option.getId();
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
        Player modelPlayer = findModelPlayerByName(player.getName());
        if (Cleaner.class.isInstance(modelPlayer)) {
            appendCleanerResult(details, Cleaner.class.cast(modelPlayer));
            return details;
        }
        if (BusDriver.class.isInstance(modelPlayer)) {
            appendBusDriverResult(details, BusDriver.class.cast(modelPlayer));
            return details;
        }
        details.add(player.getPlayerRole().getScoreLabel() + ": " + resolveModelValue(player));
        return details;
    }

    private ResultPanel.PlayerResult createWinnerResult() {
        if (session == null || session.getGame() == null || session.getGame().getPlayers().isEmpty()) {
            return null;
        }
        Player bestPlayer = null;
        int bestValue = Integer.MIN_VALUE;
        for (Player player : session.getGame().getPlayers()) {
            int value = scoreValue(player);
            if (value > bestValue) {
                bestValue = value;
                bestPlayer = player;
            }
        }
        if (bestPlayer == null) {
            return null;
        }
        List<String> details = new ArrayList<>();
        details.add("Legjobb érték: " + bestValue);
        details.add(ROLE_LABEL_PREFIX + roleDisplayName(bestPlayer.isCleaner() ? ROLE_CLEANER : ROLE_BUS_DRIVER));
        return new ResultPanel.PlayerResult(bestPlayer.getName(), "Összegzés", details);
    }

    private int scoreValue(Player player) {
        if (Cleaner.class.isInstance(player)) {
            return walletAmount(Cleaner.class.cast(player));
        }
        if (BusDriver.class.isInstance(player)) {
            return BusDriver.class.cast(player).getScore();
        }
        return 0;
    }

    private void appendCleanerResult(List<String> details, Cleaner cleaner) {
        details.add("Pénz: " + walletAmount(cleaner));
        details.add("Hókotrók száma: " + (cleaner.getFleet() == null ? 0 : cleaner.getFleet().size()));
        details.add("Felszerelések: " + describeFleet(cleaner));
        details.add("Raktár: " + describeInventory(cleaner));
    }

    private void appendBusDriverResult(List<String> details, BusDriver driver) {
        Bus bus = driver.getManagedBus();
        details.add("Pontszám: " + driver.getScore());
        details.add("Busz: " + objectId(bus));
        details.add("Aktuális sáv: " + (bus == null ? "-" : describeLane(bus.getCurrentLane())));
        details.add("Állapot: " + describeBusState(bus));
    }

    private String describeFleet(Cleaner cleaner) {
        if (cleaner.getFleet() == null || cleaner.getFleet().isEmpty()) {
            return "nincs";
        }
        List<String> parts = new ArrayList<>();
        for (Snowplow snowplow : cleaner.getFleet()) {
            Plow plow = snowplow.getEquippedPlow();
            parts.add(objectId(snowplow) + " / " + displayPlowName(plow));
        }
        return String.join(", ", parts);
    }

    private String describeInventory(Cleaner cleaner) {
        if (cleaner.getInventory() == null || cleaner.getInventory().isEmpty()) {
            return "üres";
        }
        List<String> parts = new ArrayList<>();
        for (Consumable consumable : cleaner.getInventory()) {
            parts.add(objectId(consumable) + " (" + consumable.getConsumableType()
                + ", mennyiség: " + consumable.getAmount() + ")");
        }
        return String.join(", ", parts);
    }

    private String describeBusState(Bus bus) {
        if (bus == null) {
            return "nincs busz";
        }
        if (bus.isSnowBlocked()) {
            return "elakadt vastag hóban";
        }
        if (bus.getIsParalyzed()) {
            return "ütközés miatt áll (" + bus.getParalysisTimer() + " tick)";
        }
        return "menetkész";
    }

    private int resolveModelValue(PlayerRegisterPanel.RegisteredPlayer registeredPlayer) {
        if (registeredPlayer == null || session == null || session.getGame() == null) {
            return registeredPlayer == null ? 0 : prototypeValues.getOrDefault(
                registeredPlayer.getName(), registeredPlayer.getPlayerRole().getInitialValue());
        }
        Player modelPlayer = findModelPlayerByName(registeredPlayer.getName());
        if (Cleaner.class.isInstance(modelPlayer)) {
            return walletAmount(Cleaner.class.cast(modelPlayer));
        }
        if (BusDriver.class.isInstance(modelPlayer)) {
            return BusDriver.class.cast(modelPlayer).getScore();
        }
        return prototypeValues.getOrDefault(registeredPlayer.getName(), registeredPlayer.getPlayerRole().getInitialValue());
    }

    private Player findModelPlayerByName(String playerName) {
        if (session == null || session.getGame() == null) {
            return null;
        }
        String normalizedName = playerName == null ? "" : playerName.trim();
        for (Player player : session.getGame().getPlayers()) {
            if (player != null && player.getName() != null && player.getName().equalsIgnoreCase(normalizedName)) {
                return player;
            }
        }
        return null;
    }

    private String getMapName() {
        return mapDescriptor == null ? "ismeretlen pálya" : mapDescriptor.getDisplayName();
    }

    private static String roleDisplayName(String modelRoleName) {
        if (ROLE_BUS_DRIVER.equals(modelRoleName)) {
            return "Busz sofőr";
        }
        if (ROLE_CLEANER.equals(modelRoleName)) {
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

    private static final class TargetOption {
        private final String id;
        private final String label;

        private TargetOption(String id, String label) {
            this.id = id;
            this.label = label;
        }

        private static TargetOption placeholder(String text) {
            return new TargetOption(null, text);
        }

        private String getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static final class MovementResult {
        private final String message;
        private final FeedbackType feedbackType;

        private MovementResult(String message, FeedbackType feedbackType) {
            this.message = message;
            this.feedbackType = feedbackType;
        }

        private static MovementResult success(String message) {
            return new MovementResult(message, FeedbackType.SUCCESS);
        }

        private static MovementResult warning(String message) {
            return new MovementResult(message, FeedbackType.WARNING);
        }
    }
}