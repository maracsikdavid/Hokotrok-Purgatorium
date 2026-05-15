package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JPanel;

/**
 * A bolt tartalmát és vásárlási vezérlőit tartalmazó panel.
 * Csak Cleaner körben lesz aktív a későbbi teljes bekötés során.
 */
public class ShopPanel extends JPanel {
    private static final Color CARD_BORDER = new Color(17, 24, 39);
    private static final String[][] PRODUCTS = {
        {"sweeperPlow", "Seprő eke", "100"},
        {"dumpPlow", "Dömper eke", "100"},
        {"saltPlow", "Sózó eke", "100"},
        {"dragonPlow", "Sárkány eke", "100"},
        {"gravelPack", "Bikakerozin", "100"},
        {"saltPack", "Só csomag", "100"},
        {"repairKit", "Javító készlet", "100"},
        {"sensor", "Szenzor", "100"}
    };

    private final ButtonGroup productGroup = new ButtonGroup();
    private String selectedProductName;
    private int selectedPrice;

    /**
     * Letrehozza a bolti termekkartyakat.
     */
    public ShopPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel productsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        productsPanel.setOpaque(false);
        for (String[] product : PRODUCTS) {
            productsPanel.add(createProductButton(product[0], product[1], Integer.parseInt(product[2])));
        }
        add(productsPanel, BorderLayout.CENTER);
    }

    /**
     * Visszaadja a kivalasztott termek nevet.
     *
     * @return termeknev, vagy null ha nincs kivalasztas
     */
    public String getSelectedProductName() {
        return selectedProductName;
    }

    /**
     * Visszaadja a kivalasztott termek arat.
     *
     * @return ar
     */
    public int getSelectedPrice() {
        return selectedPrice;
    }

    /**
     * Frissíti a boltpanel tartalmát az aktuális univerzális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void showShop(GameSnapshot snapshot) {
        repaint();
    }

    private JToggleButton createProductButton(String productId, String displayName, int price) {
        JToggleButton button = new JToggleButton("<html><center>" + displayName + "<br><b>" + price + "</b></center></html>");
        button.setActionCommand(productId);
        button.setPreferredSize(new Dimension(120, 74));
        button.setFocusPainted(false);
        button.setBackground(new Color(248, 250, 252));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
        button.addActionListener(event -> {
            selectedProductName = displayName;
            selectedPrice = price;
        });
        productGroup.add(button);
        return button;
    }
}