package gui.swing;

import core.Shop;
import core.ShopItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
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

    private final ButtonGroup productGroup = new ButtonGroup();
    private final JPanel productsPanel = new JPanel(new GridLayout(2, 5, 10, 10));
    private final List<JToggleButton> productButtons = new ArrayList<>();
    private String selectedProductName;
    private int selectedPrice;
    private ShopItem selectedItem;

    /**
     * Letrehozza a bolti termekkartyakat.
     */
    public ShopPanel() {
        this(new Shop());
    }

    public ShopPanel(Shop shop) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        productsPanel.setOpaque(false);
        populateProducts(shop);
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

    public ShopItem getSelectedItem() {
        return selectedItem;
    }

    private void populateProducts(Shop shop) {
        productsPanel.removeAll();
        productButtons.clear();
        Shop resolvedShop = shop == null ? new Shop() : shop;
        List<ShopItem> items = new ArrayList<>(resolvedShop.getItems());
        if (items.isEmpty()) {
            for (ShopItem item : ShopItem.values()) {
                items.add(item);
            }
        }
        for (ShopItem item : items) {
            JToggleButton button = createProductButton(item, item.getDisplayName(), resolvedShop.getPrice(item));
            productButtons.add(button);
            productsPanel.add(button);
        }
    }

    public void setPurchasableItems(List<ShopItem> purchasableItems) {
        if (purchasableItems == null) {
            return;
        }
        for (JToggleButton button : productButtons) {
            ShopItem item = ShopItem.valueOf(button.getActionCommand());
            boolean enabled = purchasableItems.contains(item);
            button.setEnabled(enabled);
            if (!enabled && item == selectedItem) {
                selectedItem = null;
                selectedProductName = null;
                selectedPrice = 0;
                productGroup.clearSelection();
            }
        }
    }

    private JToggleButton createProductButton(ShopItem item, String displayName, int price) {
        Color itemColor = GameColors.shopItemColor(item);
        Color textColor = GameColors.readableText(itemColor);
        String htmlColor = toHtmlColor(textColor);
        JToggleButton button = new JToggleButton("<html><center><font color='" + htmlColor + "'>"
            + displayName + "<br><b>" + price + "</b></font></center></html>");
        button.setActionCommand(item.name());
        button.setPreferredSize(new Dimension(120, 74));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(itemColor);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 12f));
        button.addActionListener(event -> {
            selectedProductName = displayName;
            selectedPrice = price;
            selectedItem = item;
        });
        productGroup.add(button);
        return button;
    }

    private String toHtmlColor(Color color) {
        Color resolvedColor = color == null ? Color.BLACK : color;
        return String.format("#%02x%02x%02x", resolvedColor.getRed(), resolvedColor.getGreen(), resolvedColor.getBlue());
    }

}
