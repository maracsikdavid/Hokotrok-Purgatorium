package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A játék végállapotát és az új játék/kilépés műveleteket megjelenítő panel váza.
 */
public class ResultPanel extends JPanel {
    private final JLabel resultLabel = new JLabel("Eredmények");
    private final JButton newGameButton = new JButton("Új játék");
    private final JButton exitButton = new JButton("Kilépés");

    /**
     * Létrehozza az eredménypanel alapvető komponenseit.
     */
    public ResultPanel() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);
        add(resultLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Frissíti az eredménypanel adatait.
     *
     * @param snapshot a játék végállapotának pillanatképe
     */
    public void refresh(GameSnapshot snapshot) {
    }

    /**
     * Beállítja az új játék gomb eseménykezelőjét.
     *
     * @param listener az új játék indításakor meghívandó kezelő
     */
    public void setNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Beállítja a kilépés gomb eseménykezelőjét.
     *
     * @param listener a kilépéskor meghívandó kezelő
     */
    public void setExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}