package gui.swing;

import gui.application.GameSession;
import gui.application.MapDescriptor;
import gui.snapshot.GameSnapshot;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A futó játék fő nézete.
 * Osszefogja a terkepet, a szerepkorfuggo vezerloket es az also visszajelzo savot.
 */
public class GamePanel extends JPanel {
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color PANEL_BORDER = new Color(17, 24, 39);
    private static final Color INFO_BACKGROUND = new Color(239, 246, 255);

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
    private final JButton snowplowMoveButton = new JButton(SwingActionText.MOVE_SNOWPLOW);
    private final JButton busMoveButton = new JButton(SwingActionText.MOVE_BUS);
    private final JButton helpButton = new JButton(SwingActionText.HELP);
    private final JButton finishTurnButton = new JButton(SwingActionText.FINISH_TURN);
    private final JButton endGameButton = new JButton(SwingActionText.END_GAME);
    private final JButton executeActionButton = new JButton(SwingActionText.EXECUTE_ACTION);
    private final JComboBox<String> roadComboBox = new JComboBox<>(new String[] {"Cél út", "M1", "M2", "Belvárosi út"});
    private final JComboBox<String> laneComboBox = new JComboBox<>(new String[] {"Cél sáv", "1. sáv", "2. sáv"});
    private final JLabel statusLabel = new JLabel("Várakozás műveletre.");
    private final JTextArea infoArea = new JTextArea("Jobb kattintással kérdezhetsz le pályainformációt.");
    private transient ActionListener endGameListener;

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

        if (session != null) {
            mapCanvas.setLayout(session.getMapLayout());
            refresh(session.getSnapshot());
        } else {
            refresh(null);
        }
        setStatusMessage("Pálya betöltve: " + getMapName());
    }

    /**
     * Frissíti a teljes panelrendszert az aktuális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void refresh(GameSnapshot snapshot) {
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
        addControlButton(controlPanel, snowplowMoveButton);
        addControlButton(controlPanel, busMoveButton);
        controlPanel.add(Box.createVerticalGlue());
        addControlButton(controlPanel, helpButton);
        addControlButton(controlPanel, finishTurnButton);
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
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setRows(2);
        feedbackRow.add(statusLabel, BorderLayout.WEST);
        feedbackRow.add(new JScrollPane(infoArea), BorderLayout.CENTER);

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
        equipButton.addActionListener(event -> setStatusMessage("TODO: Eke felszerelése modellművelet bekötése."));
        refillButton.addActionListener(event -> setStatusMessage("TODO: Utántöltés modellművelet bekötése."));
        snowplowMoveButton.addActionListener(event -> setStatusMessage("TODO: Hókotró mozgatása modellművelet bekötése."));
        busMoveButton.addActionListener(event -> setStatusMessage("TODO: Busz mozgatása modellművelet bekötése."));
        executeActionButton.addActionListener(event -> executeSelectedAction());
        helpButton.addActionListener(event -> showHelp());
        finishTurnButton.addActionListener(event -> finishTurn());
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
        setStatusMessage("Kijelölés frissítve a térképen: " + selection);
    }

    private void handleMapInfoRequest(MouseEvent event) {
        infoArea.setText("Lekérdezett pont: x=" + event.getX() + ", y=" + event.getY()
            + "\nTODO: itt jelennek meg a pályaelem részletes adatai.");
    }

    private void executeSelectedAction() {
        String road = String.valueOf(roadComboBox.getSelectedItem());
        String lane = String.valueOf(laneComboBox.getSelectedItem());
        setStatusMessage("TODO: művelet végrehajtása célponttal: " + road + ", " + lane + ".");
    }

    private void openShopDialog() {
        if (!isCleanerTurn()) {
            setStatusMessage("A bolt csak Hókotrós körben érhető el.");
            return;
        }

        Frame owner = JOptionPane.getFrameForComponent(this);
        ShopDialog dialog = new ShopDialog(owner, this::setStatusMessage);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
            "Bal kattintás: kijelölés\nJobb kattintás: információ\nEsc: kijelölés törlése\nEnter: művelet végrehajtása",
            "Súgó", JOptionPane.INFORMATION_MESSAGE);
    }

    private void finishTurn() {
        if (players.isEmpty()) {
            setStatusMessage("TODO: modellbeli körváltás bekötése.");
            return;
        }
        PlayerRegisterPanel.RegisteredPlayer currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            completedTurnsByPlayer.merge(currentPlayer.getName(), 1, Integer::sum);
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updateHeader(session == null ? null : session.getSnapshot());
        updateRoleControls();
        setStatusMessage("Kör befejezve. Aktuális játékos: " + getCurrentPlayer().getName());
    }

    private void clearSelection() {
        selectionState.clear();
        mapCanvas.repaint();
        setStatusMessage("Kijelölés törölve.");
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
        snowplowMoveButton.setVisible(cleanerTurn);
        busMoveButton.setVisible(!cleanerTurn);
        revalidate();
        repaint();
    }

    private boolean isCleanerTurn() {
        PlayerRegisterPanel.RegisteredPlayer player = getCurrentPlayer();
        if (player != null) {
            return player.isCleaner();
        }
        return session == null || !"BusDriver".equals(session.getCurrentPlayerRole());
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
            details.add("Ekei: sweeperPlow, dumpPlow, saltPlow, dragonPlow");
            details.add("Anyagai: Só: 20, Bikakerozin: 10");
        } else {
            details.add("Állapota: busz útvonal készenlétben");
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
        statusLabel.setText(message == null || message.isBlank() ? "Várakozás műveletre." : message);
    }
}