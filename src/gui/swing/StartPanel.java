package gui.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A grafikus alkalmazás kezdőképernyője.
 * Innen választható a konzolos mód, a grafikus mód és az alkalmazás bezárása.
 */
public class StartPanel extends JPanel {
    private final JButton consoleModeButton = new JButton("Konzolos mód");
    private final JButton graphicModeButton = new JButton("Grafikus mód");
    private final JButton exitButton = new JButton("Kilépés");

    /**
     * Létrehozza a kezdőpanel alapvető komponensvázát.
     */
    public StartPanel() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Hókotrók Purgatórium - Grafikus prototípus", SwingConstants.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        buttonPanel.add(consoleModeButton);
        buttonPanel.add(graphicModeButton);
        buttonPanel.add(exitButton);
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Beállítja a konzolos mód gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setConsoleModeListener(ActionListener listener) {
        consoleModeButton.addActionListener(listener);
    }

    /**
     * Beállítja a grafikus mód gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setGraphicModeListener(ActionListener listener) {
        graphicModeButton.addActionListener(listener);
    }

    /**
     * Beállítja a kilépés gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}