package gui.swing;

import gui.application.MapDescriptor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * A pályaválasztás utáni játékos-regisztrációs panel.
 * A tényleges modellbeli játékoslétrehozás későbbi bekötési pontként marad meg.
 */
public class PlayerRegisterPanel extends JPanel {
    private MapDescriptor mapDescriptor;
    private final JTextField playerNameField = new JTextField();
    private final JRadioButton cleanerRoleButton = new JRadioButton("Hókotrós", true);
    private final JRadioButton busDriverRoleButton = new JRadioButton("Buszsofőr");
    private final JButton addButton = new JButton("Hozzáadás");
    private final JButton startButton = new JButton("Pálya indítása");
    private final JButton backButton = new JButton("Vissza a főmenübe");
    private final DefaultListModel<String> playersModel = new DefaultListModel<>();
    private final List<RegisteredPlayer> registeredPlayers = new ArrayList<>();
    private ActionListener startListener;
    private ActionListener backListener;

    /**
     * Létrehozza a játékos-regisztrációs panel alapvető komponensvázát.
     */
    public PlayerRegisterPanel() {
        setLayout(new BorderLayout());
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(cleanerRoleButton);
        roleGroup.add(busDriverRoleButton);

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        inputPanel.add(new JLabel("Játékos neve"));
        inputPanel.add(playerNameField);
        inputPanel.add(cleanerRoleButton);
        inputPanel.add(busDriverRoleButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        buttonPanel.add(addButton);
        buttonPanel.add(startButton);
        buttonPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JList<>(playersModel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(event -> addPlayerFromFields());
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

    /**
     * Beállítja az aktuálisan kiválasztott pályát.
     *
     * @param mapDescriptor a kiválasztott pályaleíró
     */
    public void setMapDescriptor(MapDescriptor mapDescriptor) {
        this.mapDescriptor = mapDescriptor;
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
    }

    private void addPlayerFromFields() {
        String name = playerNameField.getText() == null ? "" : playerNameField.getText().trim();
        String role = cleanerRoleButton.isSelected() ? "Cleaner" : "BusDriver";
        RegisteredPlayer player = new RegisteredPlayer(name, role);
        registeredPlayers.add(player);
        playersModel.addElement(player.getName() + " - " + player.getRole());
        playerNameField.setText("");
    }

    /**
     * A regisztrációs panelen felvett játékos egyszerű adatrekordja.
     */
    public static class RegisteredPlayer {
        private final String name;
        private final String role;

        /**
         * Konstruktor a regisztrált játékos adataihoz.
         *
         * @param name a játékos neve
         * @param role a választott szerepkör
         */
        public RegisteredPlayer(String name, String role) {
            this.name = name == null || name.isEmpty() ? "Játékos" : name;
            this.role = role;
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
            return role;
        }
    }
}