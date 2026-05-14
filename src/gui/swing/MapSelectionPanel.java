package gui.swing;

import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A pályaválasztó képernyő Swing panelje.
 * A katalógusból kapott pályaleírókból kártyaszerű indítóelemeket készít.
 */
public class MapSelectionPanel extends JPanel {
    private MapCatalog catalog;
    private ActionListener selectionListener;
    private ActionListener backListener;
    private final JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 8, 8));
    private final JButton backButton = new JButton("Vissza");

    /**
     * Létrehozza a pályaválasztó panel alapvető komponensvázát.
     */
    public MapSelectionPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Pályaválasztás"), BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(event -> {
            if (backListener != null) {
                backListener.actionPerformed(event);
            }
        });
    }

    /**
     * Összeköti a panelt az elérhető pályák katalógusával.
     *
     * @param catalog a megjelenítendő pályakatalógus
     */
    public void bindCatalog(MapCatalog catalog) {
        this.catalog = catalog;
        rebuildCards();
    }

    /**
     * Beállítja a pályaválasztás eseménykezelőjét.
     *
     * @param listener a pályaválasztáskor meghívandó kezelő
     */
    public void setSelectionListener(ActionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * Beállítja a vissza gomb eseménykezelőjét.
     *
     * @param listener a visszalépéskor meghívandó kezelő
     */
    public void setBackListener(ActionListener listener) {
        this.backListener = listener;
    }

    private void rebuildCards() {
        cardsPanel.removeAll();
        if (catalog != null) {
            for (MapDescriptor descriptor : catalog.getAllMaps()) {
                JButton mapButton = new JButton(descriptor.getDisplayName());
                mapButton.setActionCommand(descriptor.getId());
                mapButton.addActionListener(event -> {
                    if (selectionListener != null) {
                        selectionListener.actionPerformed(event);
                    }
                });
                cardsPanel.add(mapButton);
            }
        }
        revalidate();
        repaint();
    }
}