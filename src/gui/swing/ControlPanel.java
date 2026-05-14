package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Az aktuális játékoshoz tartozó vezérlőgombok panelje.
 * A gombok tényleges modellműveletei későbbi bekötési pontként maradnak meg.
 */
public class ControlPanel extends JPanel {
    private final JButton moveButton = new JButton("Művelet végrehajtása");
    private final JButton shopButton = new JButton("Bolt megnyitása");
    private final JButton equipButton = new JButton("Eke felszerelése");
    private final JButton refillButton = new JButton("Utántöltés");

    /**
     * Létrehozza a vezérlőpanel alapvető gombvázát.
     */
    public ControlPanel() {
        setLayout(new GridLayout(4, 1, 8, 8));
        add(moveButton);
        add(shopButton);
        add(equipButton);
        add(refillButton);
    }

    /**
     * Frissíti a gombok állapotát az aktuális univerzális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void updateButtons(GameSnapshot snapshot) {
    }
}