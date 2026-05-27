package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Az aktuális játékoshoz tartozó vezérlőgombok panelje.
 * A gombokhoz külső modellműveletek köthetők.
 */
public class ControlPanel extends JPanel {
    private final JButton moveButton = new JButton(SwingActionText.EXECUTE_ACTION);
    private final JButton shopButton = new JButton(SwingActionText.OPEN_SHOP);
    private final JButton equipButton = new JButton(SwingActionText.EQUIP_PLOW);
    private final JButton refillButton = new JButton(SwingActionText.REFILL);

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

    public void setMoveAction(ActionListener listener) {
        replaceActionListener(moveButton, listener);
    }

    public void setShopAction(ActionListener listener) {
        replaceActionListener(shopButton, listener);
    }

    public void setEquipAction(ActionListener listener) {
        replaceActionListener(equipButton, listener);
    }

    public void setRefillAction(ActionListener listener) {
        replaceActionListener(refillButton, listener);
    }

    /**
     * Frissíti a gombok állapotát az aktuális univerzális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void updateButtons(GameSnapshot snapshot) {
        boolean enabled = snapshot != null;
        moveButton.setEnabled(enabled);
        shopButton.setEnabled(enabled);
        equipButton.setEnabled(enabled);
        refillButton.setEnabled(enabled);
    }

    private void replaceActionListener(JButton button, ActionListener listener) {
        for (ActionListener existingListener : button.getActionListeners()) {
            button.removeActionListener(existingListener);
        }
        if (listener != null) {
            button.addActionListener(listener);
        }
    }
}