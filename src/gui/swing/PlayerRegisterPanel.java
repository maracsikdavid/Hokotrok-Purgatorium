package gui.swing;

import gui.application.MapDescriptor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * A pályaválasztás utáni játékos-regisztrációs panel.
 * A tényleges modellbeli játékoslétrehozás későbbi bekötési pontként marad meg.
 */
public class PlayerRegisterPanel extends JPanel {
    private static final int MIN_PLAYER_NAME_LENGTH = 4;
    private static final int MAX_PLAYERS = 10;
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color CARD_BORDER = new Color(17, 24, 39);

    private MapDescriptor mapDescriptor;
    private final JTextField playerNameField = new JTextField();
    private final JRadioButton cleanerRoleButton = new JRadioButton("Hókotrós", true);
    private final JRadioButton busDriverRoleButton = new JRadioButton("Busz sofőr");
    private final JButton addButton = new JButton(SwingActionText.ADD_PLAYER);
    private final JButton startButton = new JButton(SwingActionText.START_MAP);
    private final JButton backButton = new JButton(SwingActionText.BACK_TO_MAP_SELECTOR);
    private final DefaultListModel<String> playersModel = new DefaultListModel<>();
    private final JList<String> playersList = new JList<>(playersModel);
    private final JLabel selectedMapLabel = new JLabel("Kiválasztott pálya: -");
    private final JLabel notificationLabel = new JLabel(" ", SwingConstants.CENTER);
    private final List<RegisteredPlayer> registeredPlayers = new ArrayList<>();
    private transient ActionListener startListener;
    private transient ActionListener backListener;
    private transient Timer notificationTimer;

    /**
     * Létrehozza a játékos-regisztrációs panel alapvető komponensvázát.
     */
    public PlayerRegisterPanel() {
        setLayout(new BorderLayout(24, 24));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createEmptyBorder(34, 48, 34, 48)));

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(cleanerRoleButton);
        roleGroup.add(busDriverRoleButton);

        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersList.setCellRenderer(new DefaultListCellRenderer());
        configureNotificationLabel();

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        addButton.addActionListener(event -> addPlayerFromFields());
        playerNameField.addActionListener(event -> addPlayerFromFields());
        startButton.addActionListener(event -> {
            if (startListener != null) {
                startListener.actionPerformed(event);
            }
        });
        backButton.addActionListener(event -> {
            if (backListener != null) {
                backListener.actionPerformed(event);
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new GridLayout(4, 1, 0, 6));
        header.setOpaque(false);

        JLabel title = new JLabel("Játékosok felvétele", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Add meg a játékos nevét és szerepkörét.", SwingConstants.CENTER);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.BOLD, 13f));
        selectedMapLabel.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(title);
        header.add(subtitle);
        header.add(selectedMapLabel);
        header.add(notificationLabel);
        return header;
    }

    private void configureNotificationLabel() {
        notificationLabel.setOpaque(true);
        notificationLabel.setVisible(false);
        notificationLabel.setForeground(new Color(127, 29, 29));
        notificationLabel.setBackground(new Color(254, 226, 226));
        notificationLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(248, 113, 113), 2),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        notificationLabel.setFont(notificationLabel.getFont().deriveFont(Font.BOLD, 12f));
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 28, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(createInputCard());
        centerPanel.add(createListCard());
        return centerPanel;
    }

    private JPanel createInputCard() {
        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setBackground(Color.WHITE);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 3),
            BorderFactory.createEmptyBorder(22, 26, 22, 26)));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 10, 0);

        JLabel sectionTitle = new JLabel("Új játékos", SwingConstants.CENTER);
        sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 18f));
        inputCard.add(sectionTitle, constraints);

        constraints.gridy = 1;
        inputCard.add(new JLabel("Név:"), constraints);

        constraints.gridy = 2;
        playerNameField.setPreferredSize(new Dimension(240, 34));
        inputCard.add(playerNameField, constraints);

        constraints.gridy = 3;
        cleanerRoleButton.setOpaque(false);
        inputCard.add(cleanerRoleButton, constraints);

        constraints.gridy = 4;
        busDriverRoleButton.setOpaque(false);
        inputCard.add(busDriverRoleButton, constraints);

        constraints.gridy = 5;
        constraints.insets = new Insets(12, 0, 0, 0);
        styleButton(addButton);
        inputCard.add(addButton, constraints);
        return inputCard;
    }

    private JPanel createListCard() {
        JPanel listCard = new JPanel(new BorderLayout(0, 12));
        listCard.setBackground(Color.WHITE);
        listCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 3),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JLabel title = new JLabel("Felvett játékosok", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        listCard.add(title, BorderLayout.NORTH);
        listCard.add(new JScrollPane(playersList), BorderLayout.CENTER);
        return listCard;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 28, 0));
        buttonPanel.setOpaque(false);

        styleButton(backButton);
        styleButton(startButton);

        JPanel backButtonCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backButtonCell.setOpaque(false);
        backButtonCell.add(backButton);

        JPanel startButtonCell = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        startButtonCell.setOpaque(false);
        startButtonCell.add(startButton);

        buttonPanel.add(backButtonCell);
        buttonPanel.add(startButtonCell);
        return buttonPanel;
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 2),
            BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
    }

    /**
     * Beállítja az aktuálisan kiválasztott pályát.
     *
     * @param mapDescriptor a kiválasztott pályaleíró
     */
    public void setMapDescriptor(MapDescriptor mapDescriptor) {
        this.mapDescriptor = mapDescriptor;
        String displayName = mapDescriptor == null ? "-" : mapDescriptor.getDisplayName();
        selectedMapLabel.setText("Kiválasztott pálya: " + displayName);
        clearPlayers();
    }

    /**
     * Visszaadja az aktuálisan kiválasztott pályát.
     *
     * @return a kiválasztott pályaleíró
     */
    public MapDescriptor getMapDescriptor() {
        return mapDescriptor;
    }

    /**
     * Visszaadja a regisztrált játékosok listáját.
     *
     * @return regisztrált játékosok csak olvasható listája
     */
    public List<RegisteredPlayer> getRegisteredPlayers() {
        return Collections.unmodifiableList(registeredPlayers);
    }

    /**
     * Jelzi, hogy van-e legalabb egy felvett jatekos.
     *
     * @return igaz, ha a lista nem ures
     */
    public boolean hasPlayers() {
        return !registeredPlayers.isEmpty();
    }

    public boolean hasRequiredRoles() {
        return hasCleanerPlayer() && hasBusDriverPlayer();
    }

    public String getStartValidationMessage() {
        if (registeredPlayers.isEmpty()) {
            return "Adj hozzá legalább egy Hókotrós és egy Busz sofőr játékost.";
        }
        if (!hasCleanerPlayer()) {
            return "Legalább egy Hókotrós játékos szükséges az indításhoz.";
        }
        if (!hasBusDriverPlayer()) {
            return "Legalább egy Busz sofőr játékos szükséges az indításhoz.";
        }
        return null;
    }

    public void showPushNotification(String message) {
        if (notificationTimer != null && notificationTimer.isRunning()) {
            notificationTimer.stop();
        }
        notificationLabel.setText(message == null || message.isBlank() ? "Nem indítható a pálya." : message);
        notificationLabel.setVisible(true);
        notificationTimer = new Timer(3500, event -> notificationLabel.setVisible(false));
        notificationTimer.setRepeats(false);
        notificationTimer.start();
    }

    /**
     * Beállítja az indítás gomb eseménykezelőjét.
     *
     * @param listener az indításkor meghívandó kezelő
     */
    public void setStartListener(ActionListener listener) {
        this.startListener = listener;
    }

    /**
     * Beállítja a vissza gomb eseménykezelőjét.
     *
     * @param listener a visszalépéskor meghívandó kezelő
     */
    public void setBackListener(ActionListener listener) {
        this.backListener = listener;
    }

    /**
     * Törli a felvett játékosok listáját.
     */
    public void clearPlayers() {
        registeredPlayers.clear();
        playersModel.clear();
        if (notificationTimer != null && notificationTimer.isRunning()) {
            notificationTimer.stop();
        }
        notificationLabel.setVisible(false);
    }

    private void addPlayerFromFields() {
        if (registeredPlayers.size() >= MAX_PLAYERS) {
            showPushNotification("Legfeljebb 10 játékos vehető fel egy pályára.");
            return;
        }
        String name = playerNameField.getText() == null ? "" : playerNameField.getText().trim();
        if (name.length() < MIN_PLAYER_NAME_LENGTH) {
            showPushNotification("A játékos neve legalább 4 karakter hosszú legyen.");
            return;
        }
        if (containsName(name)) {
            showPushNotification("Már létezik játékos ezzel a névvel.");
            return;
        }
        PlayerRole role = cleanerRoleButton.isSelected() ? PlayerRole.CLEANER : PlayerRole.BUS_DRIVER;
        RegisteredPlayer player = new RegisteredPlayer(name, role);
        registeredPlayers.add(player);
        playersModel.addElement(player.toString());
        playerNameField.setText("");
        if (hasRequiredRoles()) {
            notificationLabel.setVisible(false);
        }
    }

    private boolean containsName(String name) {
        String normalizedName = name == null ? "" : name.trim();
        for (RegisteredPlayer player : registeredPlayers) {
            if (player.getName().equalsIgnoreCase(normalizedName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCleanerPlayer() {
        for (RegisteredPlayer player : registeredPlayers) {
            if (player.isCleaner()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBusDriverPlayer() {
        for (RegisteredPlayer player : registeredPlayers) {
            if (player.getPlayerRole() == PlayerRole.BUS_DRIVER) {
                return true;
            }
        }
        return false;
    }

    /**
     * A grafikus prototipusban valaszthato szerepkorok.
     */
    public enum PlayerRole {
        CLEANER("Cleaner", "Hókotrós", "Pénz", 0),
        BUS_DRIVER("BusDriver", "Busz sofőr", "Pontszáma", 0);

        private final String modelName;
        private final String displayName;
        private final String scoreLabel;
        private final int initialValue;

        PlayerRole(String modelName, String displayName, String scoreLabel, int initialValue) {
            this.modelName = modelName;
            this.displayName = displayName;
            this.scoreLabel = scoreLabel;
            this.initialValue = initialValue;
        }

        public String getModelName() {
            return modelName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getScoreLabel() {
            return scoreLabel;
        }

        public int getInitialValue() {
            return initialValue;
        }

        public static PlayerRole fromModelName(String roleName) {
            if (BUS_DRIVER.modelName.equals(roleName)) {
                return BUS_DRIVER;
            }
            return CLEANER;
        }
    }

    /**
     * A regisztrációs panelen felvett játékos egyszerű adatrekordja.
     */
    public static class RegisteredPlayer {
        private final String name;
        private final PlayerRole role;

        /**
         * Konstruktor a regisztrált játékos adataihoz.
         *
         * @param name a játékos neve
         * @param role a választott szerepkör
         */
        public RegisteredPlayer(String name, PlayerRole role) {
            this.name = name == null || name.isEmpty() ? "Játékos" : name;
            this.role = role == null ? PlayerRole.CLEANER : role;
        }

        /**
         * Kompatibilis konstruktor a korabbi szoveges szerepkorokhoz.
         *
         * @param name a jatekos neve
         * @param role a modellbeli szerepkor neve
         */
        public RegisteredPlayer(String name, String role) {
            this(name, PlayerRole.fromModelName(role));
        }

        /**
         * Visszaadja a játékos nevét.
         *
         * @return a játékos neve
         */
        public String getName() {
            return name;
        }

        /**
         * Visszaadja a játékos szerepkörét.
         *
         * @return a szerepkör
         */
        public String getRole() {
            return role.getModelName();
        }

        /**
         * Visszaadja a megjelenitett szerepkornevet.
         *
         * @return magyar szerepkornev
         */
        public String getRoleDisplayName() {
            return role.getDisplayName();
        }

        /**
         * Visszaadja a tipusos szerepkort.
         *
         * @return szerepkor enum
         */
        public PlayerRole getPlayerRole() {
            return role;
        }

        /**
         * Jelzi, hogy hokotros jatekosrol van-e szo.
         *
         * @return igaz hokotros eseten
         */
        public boolean isCleaner() {
            return role == PlayerRole.CLEANER;
        }

        @Override
        public String toString() {
            return name + " - " + role.getDisplayName();
        }
    }
}