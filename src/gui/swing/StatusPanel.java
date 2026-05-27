package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A felső státuszsáv panelje.
 * Tick, aktuális játékos és későbbi szerepkörfüggő értékek megjelenítésére szolgál.
 */
public class StatusPanel extends JPanel {
    private final JLabel playerLabel = new JLabel("Játékos: -");
    private final JLabel tickLabel = new JLabel("Tick: 0");
    private final JLabel roleValueLabel = new JLabel("Állapot: -");

    /**
     * Létrehozza a státuszpanel alapvető mezőit.
     */
    public StatusPanel() {
        setLayout(new GridLayout(1, 3, 8, 8));
        add(playerLabel);
        add(tickLabel);
        add(roleValueLabel);
    }

    /**
     * Frissíti a státuszsáv tartalmát az aktuális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void updateStatus(GameSnapshot snapshot) {
        if (snapshot == null) {
            playerLabel.setText("Játékos: -");
            tickLabel.setText("Tick: 0");
            return;
        }
        playerLabel.setText("Játékos: " + snapshot.getCurrentPlayerId());
        tickLabel.setText("Tick: " + snapshot.getTickCount());
    }
}