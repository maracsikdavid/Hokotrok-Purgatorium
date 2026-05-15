package gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A játékképernyő fölött megjelenő bolti felugró ablak váza.
 */
public class ShopDialog extends JDialog {
    private final ShopPanel shopPanel = new ShopPanel();
    private final JButton purchaseButton = new JButton(SwingActionText.PURCHASE);
    private final transient Consumer<String> statusConsumer;

    /**
     * Létrehozza a bolti dialógus alapvető komponenseit.
     *
     * @param owner a dialógust birtokló ablak
     */
    public ShopDialog(Frame owner) {
        this(owner, null);
    }

    /**
     * Letrehozza a bolti dialogust statusz-visszajelzessel.
     *
     * @param owner a dialogust birtoklo ablak
     * @param statusConsumer a GamePanel statuszsoranak frissitoje
     */
    public ShopDialog(Frame owner, Consumer<String> statusConsumer) {
        super(owner, "Üdvözlünk a boltban!", true);
        this.statusConsumer = statusConsumer;
        setIconImages(AppIcon.createIconImages());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel title = new JLabel("Üdvözlünk a boltban!", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 16));
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        add(shopPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 14, 14, 14));
        styleButton(purchaseButton);
        buttonPanel.add(purchaseButton);
        add(buttonPanel, BorderLayout.SOUTH);

        purchaseButton.addActionListener(event -> handlePurchase());
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

    private void handlePurchase() {
        String productName = shopPanel.getSelectedProductName();
        if (productName == null) {
            updateStatus("Válassz terméket a vásárláshoz.");
            return;
        }
        updateStatus("Hiba: nincs elég pénz a(z) " + productName
            + " megvásárlásához. TODO: vásárlás modellbekötése.");
    }

    private void updateStatus(String message) {
        if (statusConsumer != null) {
            statusConsumer.accept(message);
        }
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        button.setFont(button.getFont().deriveFont(java.awt.Font.BOLD, 12f));
    }
}