package gui.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * A játékképernyő fölött megjelenő bolti felugró ablak váza.
 */
public class ShopDialog extends JDialog {
    private final ShopPanel shopPanel = new ShopPanel();
    private final JButton closeButton = new JButton("Bezárás");

    /**
     * Létrehozza a bolti dialógus alapvető komponenseit.
     *
     * @param owner a dialógust birtokló ablak
     */
    public ShopDialog(Frame owner) {
        super(owner, "Üdvözlünk a boltban!", true);
        setLayout(new BorderLayout());
        add(shopPanel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
        closeButton.addActionListener(event -> setVisible(false));
        pack();
    }

    /**
     * Visszaadja a dialógusban lévő boltpanelt.
     *
     * @return a boltpanel
     */
    public ShopPanel getShopPanel() {
        return shopPanel;
    }
}