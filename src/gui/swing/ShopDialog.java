package gui.swing;

import core.Shop;
import core.ShopItem;
import gui.application.GameSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import actors.Cleaner;
import actors.Player;

/**
 * A játékképernyő fölött megjelenő bolti felugró ablak váza.
 */
public class ShopDialog extends JDialog {
    private final ShopPanel shopPanel;
    private final JButton purchaseButton = new JButton(SwingActionText.PURCHASE);
    private final JLabel walletLabel = new JLabel("Pénz: -", SwingConstants.CENTER);
    private final transient GameSession session;
    private final transient BiConsumer<String, FeedbackType> statusConsumer;
    private final transient Runnable refreshAction;

    /**
     * Létrehozza a bolti dialógus alapvető komponenseit.
     *
     * @param owner a dialógust birtokló ablak
     */
    public ShopDialog(Frame owner) {
        this(owner, null, null, null);
    }

    /**
     * Letrehozza a bolti dialogust statusz-visszajelzessel.
     *
     * @param owner a dialogust birtoklo ablak
     * @param session a futó játékmenet
     * @param statusConsumer a GamePanel statuszsoranak frissitoje
     * @param refreshAction sikeres vásárlás utáni GUI-frissítés
     */
    public ShopDialog(Frame owner, GameSession session, BiConsumer<String, FeedbackType> statusConsumer,
                      Runnable refreshAction) {
        super(owner, "Üdvözlünk a boltban!", true);
        this.session = session;
        this.statusConsumer = statusConsumer;
        this.refreshAction = refreshAction;
        Shop shop = session == null || session.getGame() == null ? new Shop() : session.getGame().getShop();
        this.shopPanel = new ShopPanel(shop);
        setIconImages(AppIcon.createIconImages());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 16));
        JLabel title = new JLabel("Üdvözlünk a boltban!", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        walletLabel.setFont(walletLabel.getFont().deriveFont(java.awt.Font.BOLD, 13f));
        headerPanel.add(title);
        headerPanel.add(walletLabel);
        add(headerPanel, BorderLayout.NORTH);

        add(shopPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 14, 14, 14));
        styleButton(purchaseButton);
        buttonPanel.add(purchaseButton, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        purchaseButton.addActionListener(event -> handlePurchase());
        refreshPurchasableItems();
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
        ShopItem item = shopPanel.getSelectedItem();
        if (item == null) {
            updateStatus("Válassz terméket a vásárláshoz.", FeedbackType.INFO);
            return;
        }
        if (session == null) {
            updateStatus("A vásárlás nem futtatható aktív játékmenet nélkül.", FeedbackType.ERROR);
            return;
        }

        try {
            session.buyItem(null, item.name());
            updateStatus("Sikeres vásárlás: " + shopPanel.getSelectedProductName()
                + " (" + shopPanel.getSelectedPrice() + ").", FeedbackType.SUCCESS);
            refreshPurchasableItems();
            if (refreshAction != null) {
                refreshAction.run();
            }
        } catch (Exception exception) {
            String message = exception.getMessage() == null ? "Sikertelen vásárlás." : exception.getMessage();
            if (message.contains("Insufficient funds")) {
                updateStatus("Nincs elegendő pénzed, ezt most nem tudod megvenni.", FeedbackType.ERROR);
            } else {
                updateStatus("Sikertelen vásárlás: " + message, FeedbackType.ERROR);
            }
        }
    }

    private void refreshPurchasableItems() {
        if (session == null || session.getGame() == null || session.getRegistry() == null) {
            return;
        }

        synchronized (session.getModelLock()) {
            Player currentPlayer = session.getGame().getCurrentPlayer(session.getRegistry());
            if (!Cleaner.class.isInstance(currentPlayer)) {
                return;
            }
            Cleaner cleaner = Cleaner.class.cast(currentPlayer);
            walletLabel.setText("Pénz: " + (cleaner.getWallet() == null ? 0 : cleaner.getWallet().getAmount()));
            int fleetSize = cleaner.getFleet() == null ? 0 : cleaner.getFleet().size();
            List<ShopItem> purchasable = new ArrayList<>();
            Shop shop = session.getGame().getShop();
            for (ShopItem item : shop.getItems()) {
                if (shop.canPurchase(cleaner, item) && isPurchasableForCleaner(cleaner, item, fleetSize)) {
                    purchasable.add(item);
                }
            }
            shopPanel.setPurchasableItems(purchasable);
        }
    }

    private boolean isPurchasableForCleaner(Cleaner cleaner, ShopItem item, int fleetSize) {
        Class<?> plowClass = plowClassFor(item);
        if (plowClass == null) {
            return true;
        }
        int owned = 0;
        for (Object plow : session.getRegistry().getByType(plowClass)) {
            if (equipments.Plow.class.isInstance(plow)
                && equipments.Plow.class.cast(plow).getOwner() == cleaner) {
                owned++;
            }
        }
        return owned < fleetSize;
    }

    private Class<?> plowClassFor(ShopItem item) {
        switch (item) {
            case DragonPlow:
                return equipments.DragonPlow.class;
            case SaltPlow:
                return equipments.SaltPlow.class;
            case DumpPlow:
                return equipments.DumpPlow.class;
            case SweeperPlow:
                return equipments.SweeperPlow.class;
            case IcebreakerPlow:
                return equipments.IcebreakerPlow.class;
            case GravelPlow:
                return equipments.GravelPlow.class;
            default:
                return null;
        }
    }

    private void updateStatus(String message, FeedbackType type) {
        if (statusConsumer != null) {
            statusConsumer.accept(message, type);
        }
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        button.setFont(button.getFont().deriveFont(java.awt.Font.BOLD, 12f));
    }
}
